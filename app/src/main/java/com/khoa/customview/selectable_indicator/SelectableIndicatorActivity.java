package com.khoa.customview.selectable_indicator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.khoa.customview.R;
import com.khoa.selectableindicatorview.OnSelectIndicator;
import com.khoa.selectableindicatorview.SelectableIndicatorView;

public class SelectableIndicatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectable_indicator);

        SelectableIndicatorView selectableIndicatorView = findViewById(R.id.toggle_button_view);
        selectableIndicatorView.setNames(new String[]{"one", "two", "three", "four", "five"});
        selectableIndicatorView.setSelectIndicator(new OnSelectIndicator() {
            @Override
            public void onChangeIndicator(int selectedPosition) {
                Toast.makeText(getApplicationContext(), selectedPosition+"", Toast.LENGTH_SHORT).show();
            }
        });

        selectableIndicatorView.setSeletedPosition(3);
    }
}
