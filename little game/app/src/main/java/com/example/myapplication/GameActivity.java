package com.example.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.*;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.*;
import java.lang.ref.WeakReference;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements View.OnTouchListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        initView();
        next(500);
    }

    private ImageView image;
    private TextView view;
    private int max_Count = 20; //最多只会达到20只地鼠
    private int count = 0; //打中地鼠的数量
    private int score = 0;  //打中的得分

    private MyHandler mMyHandler = new MyHandler(this);

    //定义地鼠的位置
    public int[][] mPosition = new int[][]{
            {25,100},{156,100},{287,100},
            {25,230},{156,230},{287,230},
            {25,361},{156,361},{287,361},
            {25,492},{156,492},{287,492}
    };

    private void initView() {
        image = (ImageView) findViewById(R.id.imageView_rat01);
        image.setOnTouchListener(this);
        view = (TextView) findViewById(R.id.textView);
    }

    //每次触碰到地鼠，则地鼠消失，分数增加1
    public boolean onTouch(View v, MotionEvent event) {
        v.setVisibility(View.GONE);
        score++;
        view.setText("Your Score: "+score);
        return false;
    }


    //游戏的一次循环
    public void next(int delayTime) {
        //利用地鼠位置产生一个随机数
        int positon = new Random().nextInt(12);
        Message message = Message.obtain();
        message.what = 1;//赋值给msg的what属性
        message.arg1 = positon;
        mMyHandler.sendMessageDelayed(message, delayTime);
        //每发送一次消息，总数就加一
        count++;
    }

    //单位转换
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    //活动结束,弹出对话框
    public void End(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Info").
                setMessage("There were only 20 ground squirrels.  " +
                        "You've killed "+score+" ground squirrels").
                setPositiveButton("sure", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).show();
    }

    class MyHandler extends Handler {
        //对Activity的弱引用
        private WeakReference<GameActivity> mActivityRef;
        public MyHandler(GameActivity activity) {
            mActivityRef = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            GameActivity activity = mActivityRef.get();
            switch (msg.what) {
                case 1:
                    if (activity.count >max_Count) {
                        //游戏结束
                        End();
                        return;
                    } else {
                        int position = msg.arg1;
                        image.setX(dp2px(activity,mPosition[position][0]));
                        if(position<3){
                            image.setY(dp2px(activity,mPosition[position][1])-60);
                        }
                        else if(position>=3 && position<6){
                            image.setY(dp2px(activity,mPosition[position][1])-80);
                        }
                        else if(position>=6 && position<9){
                            image.setY(dp2px(activity,mPosition[position][1])-110);
                        }
                        else{
                            image.setY(dp2px(activity,mPosition[position][1])-145);
                        }
                        activity.image.setVisibility(View.VISIBLE);
                        //在随机位置上显示地鼠之后，再次发送消息
                        int randomTime = new Random().nextInt(500) + 500;
                        activity.next(randomTime);
                    }
                    break;
            }
        }
    }
}
