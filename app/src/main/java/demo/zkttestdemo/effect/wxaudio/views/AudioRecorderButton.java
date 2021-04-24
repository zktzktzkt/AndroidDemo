package demo.zkttestdemo.effect.wxaudio.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;

import demo.zkttestdemo.R;
import demo.zkttestdemo.effect.wxaudio.managers.DialogManager;
import demo.zkttestdemo.effect.wxaudio.managers.RecorderManager;


/**
 * Created by cooffee on 15/10/19.
 */
public class AudioRecorderButton extends AppCompatButton implements RecorderManager.AudioStateListener {

    private static final int DISTANCE_CANCEL      = 50;
    private static final int STATE_NORMAL         = 1;
    private static final int STATE_RECORDING      = 2;
    private static final int STATE_WANT_TO_CANCEL = 3;

    private int mCurState = STATE_NORMAL;

    private DialogManager mDialogManager;

    private RecorderManager mRecorderManager;

    private float mTime;

    /**
     * 已经开始录音
     */
    private boolean isRecording = false;

    /**
     * 是否触发longclick
     */
    private boolean mLongClickReady;

    public AudioRecorderButton(Context context) {
        this(context, null);
    }

    public AudioRecorderButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        mDialogManager = new DialogManager(getContext());

        // String dir = Environment.getExternalStorageDirectory() + "/imooc_recorder_audios";
        String dir = getContext().getExternalCacheDir() + "/imooc_recorder_audios";
        mRecorderManager = RecorderManager.getInstance(dir);
        mRecorderManager.setOnAudioStateListner(this);

        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d("LONG", "onLongClick");
                mLongClickReady = true;
                mRecorderManager.prepareAudio();
                return false;
            }
        });
    }

    /**
     * 录音完成后的回调
     */
    public interface AudioFinishRecorderListener {
        void onFinish(float seconds, String filePath);
    }

    private AudioFinishRecorderListener mListener;

    public void setAudioFinishRecorderListener(AudioFinishRecorderListener listener) {
        mListener = listener;
    }

    /**
     * 获取音量大小的Runnable
     */
    private Runnable mGetVoiceLevelRunnable = new Runnable() {

        @Override
        public void run() {
            while (isRecording) {
                try {
                    //计时
                    Thread.sleep(100);
                    mTime += 0.1f;
                    //发送音量改变的消息
                    mHandler.sendEmptyMessage(MSG_VOICE_CHANGE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private static final int MSG_AUDIO_PREPARED = 0x110;
    private static final int MSG_VOICE_CHANGE   = 0x111;
    private static final int MSG_DIALOG_DIMISS  = 0x112;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_AUDIO_PREPARED:
                    Log.d("LONG", "MSG_AUDIO_PREPARED");
                    // 显示应该在audio end prepared以后
                    mDialogManager.showRecordingDialog();
                    isRecording = true;

                    // 开启线程，监听音量变化
                    new Thread(mGetVoiceLevelRunnable).start();
                    break;

                case MSG_VOICE_CHANGE:
                    mDialogManager.updateVoiceLevel(mRecorderManager.getVoiceLevel(7));
                    break;

                case MSG_DIALOG_DIMISS:
                    mDialogManager.dimissDialog();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 录音初始化完成
     * {@link RecorderManager.AudioStateListener}
     */
    @Override
    public void wellPrepared() {
        Log.d("LONG", "wellPrepared");
        mHandler.sendEmptyMessage(MSG_AUDIO_PREPARED);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x      = (int) event.getX();
        int y      = (int) event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                Log.d("手势事件", "按下 y->" + y);
                changeState(STATE_RECORDING);
                break;

            case MotionEvent.ACTION_MOVE:
                Log.d("手势事件", "移动 y->" + y);
                // 根据x, y的坐标，判断是否想要取消
                if (wantToCancel(x, y)) {
                    changeState(STATE_WANT_TO_CANCEL);
                } else {
                    changeState(STATE_RECORDING);
                }
                break;

            case MotionEvent.ACTION_UP:
                Log.d("手势事件", "抬起 y->" + y);
                /**
                 * 1. 未触发onLongClick
                 * 2. prepared没有完毕已经up
                 * 3. 录音时间小于预定的值，这个值我们设置为在onLongClick之前
                 */
                // 未触发onLongClick
                if (!mLongClickReady) {
                    changeState(STATE_NORMAL);
                    reset();
                    return super.onTouchEvent(event);
                }

                // prepared没有完毕 或 录音时间过短
                if (!isRecording || mTime < 0.6f) {
                    isRecording = false;
                    mDialogManager.tooShort();
                    mRecorderManager.cancel();
                    // 延迟发送消息，让对话框停留1.3秒
                    mHandler.sendEmptyMessageDelayed(MSG_DIALOG_DIMISS, 1300);
                }

                // 正常录制结束
                else if (STATE_RECORDING == mCurState) {
                    mDialogManager.dimissDialog();
                    mRecorderManager.release();
                    if (mListener != null) {
                        mListener.onFinish(mTime, mRecorderManager.getCurrentFilePath());
                    }
                }

                //松开手指，取消发送
                else if (STATE_WANT_TO_CANCEL == mCurState) {
                    mDialogManager.dimissDialog();
                    mRecorderManager.cancel();
                }

                changeState(STATE_NORMAL);
                reset();
                break;
        }

        return super.onTouchEvent(event);
    }

    /**
     * 恢复状态及标志位
     */
    private void reset() {
        isRecording = false;
        mLongClickReady = false;
        mTime = 0;
        mCurState = STATE_NORMAL;
    }

    private boolean wantToCancel(int x, int y) {
        if (x < 0 || x > getWidth()) {
            return true;
        }
        if (y < -DISTANCE_CANCEL || y > getHeight() + DISTANCE_CANCEL) {
            return true;
        }
        return false;
    }

    private void changeState(int state) {
        if (mCurState != state) {
            mCurState = state;
            switch (mCurState) {
                case STATE_NORMAL:
                    setBackgroundResource(R.drawable.btn_recorder_normal);
                    setText(R.string.str_recorder_normal);
                    break;
                case STATE_RECORDING:
                    setBackgroundResource(R.drawable.btn_recorder_recording);
                    setText(R.string.str_recorder_recording);
                    if (isRecording) {
                        mDialogManager.recording();
                    }
                    break;
                case STATE_WANT_TO_CANCEL:
                    setBackgroundResource(R.drawable.btn_recorder_recording);
                    setText(R.string.str_recorder_want_cancel);
                    mDialogManager.wantToCancel();
                    break;
            }
        }
    }


}
