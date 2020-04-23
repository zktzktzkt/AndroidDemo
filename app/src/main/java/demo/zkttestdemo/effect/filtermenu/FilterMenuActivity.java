package demo.zkttestdemo.effect.filtermenu;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import demo.zkttestdemo.R;

public class FilterMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_menu);

        FilterMenuView filter_view = findViewById(R.id.filter_view);
        filter_view.setAdapter(new FilterMenuAdapter(this));
    }
}
