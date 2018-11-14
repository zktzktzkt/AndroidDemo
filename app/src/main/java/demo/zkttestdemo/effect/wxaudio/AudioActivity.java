package demo.zkttestdemo.effect.wxaudio;

import android.Manifest;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import demo.zkttestdemo.R;
import demo.zkttestdemo.effect.wxaudio.bean.Recorder;
import demo.zkttestdemo.effect.wxaudio.managers.PlayerManager;
import demo.zkttestdemo.effect.wxaudio.views.AudioRecorderButton;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

/**
 * 仿微信录制语音
 */
public class AudioActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private ListView mListView;
    private ArrayAdapter<Recorder> mAdapter;
    private List<Recorder> mDatas = new ArrayList<Recorder>();
    private View mAnimView;

    private AudioRecorderButton mAudioRecorderButton;

    String[] perms = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO
    };
//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        mListView = (ListView) findViewById(R.id.id_listview);
        mAudioRecorderButton = (AudioRecorderButton) findViewById(R.id.id_recorder_button);
        //权限
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(
                    new PermissionRequest.Builder(this, 1, perms)
                            .setRationale("需要开启录音权限~")
                            .setPositiveButtonText("开启")
                            .setNegativeButtonText("关闭")
                            .build());
        }

        mAudioRecorderButton.setAudioFinishRecorderListener(new AudioRecorderButton.AudioFinishRecorderListener() {
            @Override
            public void onFinish(float seconds, String filePath) {
                Recorder recorder = new Recorder(seconds, filePath);
                mDatas.add(recorder);
                mAdapter.notifyDataSetChanged();
                mListView.setSelection(mDatas.size() - 1);
            }
        });//

        mAdapter = new RecorderAdapter(this, mDatas);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "点击" + position, Toast.LENGTH_SHORT).show();
                //实现单条播放的动画效果，利用“同一个对象”的特性
                //注意：adapter.notifyDataSetChanged() 这样不会显示动画
                if (mAnimView != null) {
                    mAnimView.setBackgroundResource(R.drawable.adj);
                    mAnimView = null;
                }
                // 播放动画
                mAnimView = view.findViewById(R.id.id_recorder_anim);
                mAnimView.setBackgroundResource(R.drawable.play_anim);
                AnimationDrawable anim = (AnimationDrawable) mAnimView.getBackground();
                anim.start();
                // 播放音频
                PlayerManager.playSound(mDatas.get(position).getFilePath(), new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mAnimView.setBackgroundResource(R.drawable.adj);
                    }
                });
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        Toast.makeText(this, "权限被授予", Toast.LENGTH_SHORT).show();//
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, Arrays.asList(perms))) {
            new AppSettingsDialog.Builder(this)
                    .setTitle("未开启相关权限")
                    .setRationale("录音功能需要权限，否则无法使用")
                    .build()
                    .show();
        }
    }
}
