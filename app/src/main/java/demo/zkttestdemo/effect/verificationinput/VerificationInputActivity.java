package demo.zkttestdemo.effect.verificationinput;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import demo.zkttestdemo.R;

/**
 * 仿滴滴出行验证码控件
 */
public class VerificationInputActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_input);

        VerificationCodeInput verificationCodeInput = findViewById(R.id.verificationCodeInput);
        verificationCodeInput.setOnCompleteListener(new VerificationCodeInput.Listener() {
            @Override
            public void onComplete(String content) {
                Toast.makeText(getApplicationContext(), content, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
