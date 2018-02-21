package demo.zkttestdemo.effect.lettersidebar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import demo.zkttestdemo.R;

public class LetterSideBarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letter_side_bar);

        final TextView tv_letter = findViewById(R.id.tv_letter);
        LetterSideBar_Copy letter_side_bar = findViewById(R.id.letter_side_bar);

        letter_side_bar.setOnTouchLetterListener(new LetterSideBar_Copy.OnTouchLetterListener() {
            @Override
            public void onTouchLetter(String letter, boolean isTouch) {
                tv_letter.setVisibility(isTouch ? View.VISIBLE : View.GONE);
                tv_letter.setText(letter);
            }
        });
//
//        letter_side_bar.setOnTouchLetterListener(new LetterSideBar.OnTouchLetterListener() {
//            @Override
//            public void onTouch(String letter, boolean isTouch) {
//                tv_letter.setVisibility(isTouch ? View.VISIBLE : View.GONE);
//                tv_letter.setText(letter);
//            }
//        });

    }
}
