package demo.zkttestdemo.effect.wavrecord;

import android.Manifest;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import demo.zkttestdemo.MyApplication;
import demo.zkttestdemo.R;
import demo.zkttestdemo.effect.wavrecord.model.AudioChannel;
import demo.zkttestdemo.effect.wavrecord.model.AudioSampleRate;
import demo.zkttestdemo.effect.wavrecord.model.AudioSource;
import omrecorder.AudioChunk;
import omrecorder.OmRecorder;
import omrecorder.PullTransport;
import omrecorder.Recorder;
import pub.devrel.easypermissions.EasyPermissions;

public class WAVActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks,
        PullTransport.OnAudioChunkPulledListener, MediaPlayer.OnCompletionListener {
    private MediaPlayer player;
    private TextView timerView;
    private ImageButton restartView;
    private ImageButton recordView;
    private ImageButton playView;
    private Timer timer;

    private String filePath = MyApplication.getContext().getExternalCacheDir() + "/recorded_audio.wav";
    private AudioSource source = AudioSource.MIC;
    private AudioChannel channel = AudioChannel.MONO;
    private AudioSampleRate sampleRate = AudioSampleRate.HZ_8000;

    /**
     * 播放秒数计时
     */
    private int playerSecondsElapsed;
    /**
     * 录音秒数计时
     */
    private int recorderSecondsElapsed;

    private Recorder recorder;
    private boolean isRecording;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wav);

        timerView = (TextView) findViewById(R.id.timer);
        restartView = (ImageButton) findViewById(R.id.restart);
        recordView = (ImageButton) findViewById(R.id.record);
        playView = (ImageButton) findViewById(R.id.play);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        if (requestCode == 111) {
            stopPlaying(); //先停止播放

            Util.wait(100, new Runnable() {
                @Override
                public void run() {
                    if (isRecording) {  //暂停或继续录音
                        pauseRecording();
                    } else {
                        resumeRecording();
                    }
                }
            });
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Some permissions have been denied
        // ...
    }

    /**
     * 播放录音
     *
     * @param v
     */
    public void togglePlaying(View v) {
        pauseRecording();
        Util.wait(100, new Runnable() {
            @Override
            public void run() {
                if (isPlaying()) {
                    stopPlaying();
                } else {
                    startPlaying();
                }
            }
        });
    }

    /**
     * 停止播放
     */
    private void stopPlaying() {
        playView.setImageResource(R.drawable.aar_ic_play);

        if (player != null) {
            try {
                player.stop();
                player.reset();
            } catch (Exception e) {
            }
        }

        stopTimer();
    }

    /**
     * 录音开始 或 暂停
     *
     * @param v
     */
    public void toggleRecording(View v) {
        String[] perms = {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(getApplicationContext(), perms)) {
            stopPlaying(); //先停止播放

            Util.wait(100, new Runnable() {
                @Override
                public void run() {
                    if (isRecording) {  //暂停或继续录音
                        pauseRecording();
                    } else {
                        resumeRecording();
                    }
                }
            });
        } else {
            EasyPermissions.requestPermissions(this, "需要录音权限", 111, perms);
        }


    }

    /**
     * 开始录音
     */
    private void resumeRecording() {
        isRecording = true;

        restartView.setVisibility(View.INVISIBLE);
        playView.setVisibility(View.INVISIBLE);
        recordView.setImageResource(R.drawable.aar_ic_pause);
        playView.setImageResource(R.drawable.aar_ic_play);

        if (recorder == null) {
            timerView.setText("00:00:00");

            recorder = OmRecorder.wav(
                    new PullTransport.Default(Util.getMic(source, channel, sampleRate), this),
                    new File(filePath));
            recorder.startRecording();
        }
        recorder.resumeRecording();

        startTimer();
    }

    /**
     * 停止计时，timer初始化
     */
    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

    /**
     * 开始播放
     */
    private void startPlaying() {
        try {
            stopRecording();
            player = new MediaPlayer();
            player.setDataSource(filePath);
            player.prepare();
            player.start();

            timerView.setText("00:00:00");
            playView.setImageResource(R.drawable.aar_ic_stop);

            playerSecondsElapsed = 0;
            startTimer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止录音
     */
    private void stopRecording() {
        recorderSecondsElapsed = 0;
        if (recorder != null) {
            try {
                recorder.stopRecording();
            } catch (IOException e) {
                e.printStackTrace();
            }
            recorder = null;
        }

        stopTimer();
    }

    /**
     * 开始计时
     */
    private void startTimer() {
        stopTimer();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateTimer();
            }
        }, 0, 1000);
    }

    /**
     * 更新时间
     */
    private void updateTimer() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isRecording) {
                    recorderSecondsElapsed++;
                    timerView.setText(Util.formatSeconds(recorderSecondsElapsed));
                } else if (isPlaying()) {
                    playerSecondsElapsed++;
                    timerView.setText(Util.formatSeconds(playerSecondsElapsed));
                }
            }
        });
    }

    public void restartRecording(View v) {
        if (isRecording) {
            stopRecording();
        } else if (isPlaying()) {
            stopPlaying();
        }

        restartView.setVisibility(View.INVISIBLE);
        playView.setVisibility(View.INVISIBLE);
        recordView.setImageResource(R.drawable.aar_ic_rec);
        timerView.setText("00:00:00");
        recorderSecondsElapsed = 0;
        playerSecondsElapsed = 0;
    }

    private void pauseRecording() {
        isRecording = false;

        restartView.setVisibility(View.VISIBLE);
        playView.setVisibility(View.VISIBLE);
        recordView.setImageResource(R.drawable.aar_ic_rec);
        playView.setImageResource(R.drawable.aar_ic_play);

        if (recorder != null) {
            recorder.pauseRecording();
        }

        stopTimer();
    }

    private boolean isPlaying() {
        try {
            return player != null && player.isPlaying() && !isRecording;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        stopPlaying();
    }

    @Override
    public void onAudioChunkPulled(AudioChunk audioChunk) {
        float amplitude = isRecording ? (float) audioChunk.maxAmplitude() : 0f;
    }
}
