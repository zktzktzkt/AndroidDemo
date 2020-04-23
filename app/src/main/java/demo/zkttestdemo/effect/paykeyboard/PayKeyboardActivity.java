package demo.zkttestdemo.effect.paykeyboard;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

import demo.zkttestdemo.R;


public class PayKeyboardActivity extends AppCompatActivity {

    private CustomKeyboard mCustomKeyboard;
    private PasswordEdittext mPasswordEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_keyboard);

        mPasswordEt = (PasswordEdittext) findViewById(R.id.password_et);
        mCustomKeyboard = (CustomKeyboard) findViewById(R.id.custom_keyboard);

        mCustomKeyboard.setOnCustomKeyboardClickListener(new CustomKeyboard.CustomKeyboardClickListener() {
            @Override
            public void click(String number) {
                //Toast.makeText(this, number, Toast.LENGTH_SHORT).show();
                mPasswordEt.addPassword(number);
                //防止弹出系统键盘
                mPasswordEt.setEnabled(false);
            }

            @Override
            public void delete() {
                //   Toast.makeText(this, "删除", Toast.LENGTH_SHORT).show();
                mPasswordEt.deleteLastPassword();
            }
        });

        mPasswordEt.setOnPasswordFullListener(new PasswordEdittext.PasswordFullListener() {
            @Override
            public void passwordFull(String password) {
                Toast.makeText(PayKeyboardActivity.this, "密码输入完毕->" + password, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
