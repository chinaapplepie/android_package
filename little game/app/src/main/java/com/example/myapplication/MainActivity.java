package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //点击游戏开始
    public void Start_Click(View view){
        //显示启动game活动
        Intent intent = new Intent(MainActivity.this,GameActivity.class);
        startActivity(intent);
    }

    //点击退出游戏
    public void Exit_Click(View view){
        this.finish();
    }
}
