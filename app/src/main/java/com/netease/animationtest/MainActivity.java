package com.netease.animationtest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.netease.animationtest.column.ColumnTopEditFragment;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment fragment = Fragment.instantiate(this, ColumnTopEditFragment.class.getName());
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content_main, fragment, ColumnTopEditFragment.class.getName())
                .commitAllowingStateLoss();
    }
}
