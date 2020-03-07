package com.example.libai;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.libai.activity.GuideActivity;
import com.example.libai.activity.MainActivity;
import com.example.libai.utils.CacheUtils;

public class StartActivity extends AppCompatActivity {

    public static final String START_MAIN = "start_main";
    @BindView(R.id.iv_start)
    ImageView ivStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);

        //渐变动画
        AlphaAnimation aa = new AlphaAnimation(1,1);
        aa.setDuration(1000);

        //设置动画效果
        ivStart.startAnimation(aa);

        //监听动画
        aa.setAnimationListener(new MyAnimationListener());
    }

    class MyAnimationListener implements Animation.AnimationListener{

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

            //判断是否进入过主界面
            boolean isStartMain = CacheUtils.getBoolean(StartActivity.this,START_MAIN);
            Intent intent;
            if (isStartMain){
                //如果进入过主页面，直接进入主页面
                intent = new Intent(StartActivity.this, MainActivity.class);
            }else{
                //如果没有进入过主页面，就进入引导页面
                intent = new Intent(StartActivity.this, GuideActivity.class);
            }
            startActivity(intent);

            //关闭当前页面
            finish();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
