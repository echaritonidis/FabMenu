package com.mkchx.widget.demo;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.mkchx.widget.fabmenu.ClockWiseView;
import com.mkchx.widget.fabmenu.RollUpView;
import com.mkchx.widget.fabmenu.interfaces.IViewClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rollUpViewExample();
        //clockWiseViewExample();
    }

    private void rollUpViewExample() {

        RollUpView rollUpView = (RollUpView) findViewById(R.id.rollup_view);
        rollUpView.setMainFabIcon(ContextCompat.getDrawable(this, R.drawable.close));
        rollUpView.setMainFabRotation(45f);
        rollUpView.setOnItemClickListener(new IViewClick() {
            @Override
            public void onItemClick(View caller, int position) {
                Toast.makeText(MainActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
            }
        });

        rollUpView.addChildView(ContextCompat.getDrawable(this, R.drawable.backup), getString(R.string.backup_text));
        rollUpView.addChildView(ContextCompat.getDrawable(this, R.drawable.bug), getString(R.string.bug_text));
        rollUpView.addChildView(ContextCompat.getDrawable(this, R.drawable.build), getString(R.string.build_text));
        rollUpView.addChildView(ContextCompat.getDrawable(this, R.drawable.dashboard), getString(R.string.dashboard_text));
    }

    private void clockWiseViewExample() {
        ClockWiseView clockWiseView = (ClockWiseView) findViewById(R.id.clockwise_view);
        clockWiseView.setMainFabIcon(ContextCompat.getDrawable(this, R.drawable.close));
        clockWiseView.setMainFabRotation(45f);
        clockWiseView.setOnItemClickListener(new IViewClick() {
            @Override
            public void onItemClick(View caller, int position) {
                Toast.makeText(MainActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
            }
        });

        clockWiseView.addChildView(ContextCompat.getDrawable(this, R.drawable.backup));
        clockWiseView.addChildView(ContextCompat.getDrawable(this, R.drawable.bug));
        clockWiseView.addChildView(ContextCompat.getDrawable(this, R.drawable.build));
        clockWiseView.addChildView(ContextCompat.getDrawable(this, R.drawable.dashboard));

        clockWiseView.create();
    }

}
