package com.example.zhbj.activity;

import com.example.zhbj.R;
import com.example.zhbj.R.layout;
import com.example.zhbj.R.menu;
import com.example.zhbj.utils.PrefsUtil;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        
        RelativeLayout rl_root = (RelativeLayout) findViewById(R.id.rl_root);
        
        //设置动画
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setFillAfter(true);
        
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(1000);
        scaleAnimation.setFillAfter(true);
        
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(2000);
        alphaAnimation.setFillAfter(true);
        
        //三个动画同时播放
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(alphaAnimation);
        set.addAnimation(scaleAnimation);
        set.addAnimation(rotateAnimation);
        
        rl_root.startAnimation(set);
        
        set.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
								
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
								
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
					Intent intent ;
					boolean isFirstEnter = PrefsUtil.getBoolean(getApplicationContext(), "is_first_enter", true);
					
					if(isFirstEnter){
						//进入新手引导页
						intent = new Intent(getApplicationContext(),NewGuideAvtivity.class);
						
					}else{
						//进入主页面
						intent = new Intent(getApplicationContext(),HomeActivity.class);
					}
					startActivity(intent);
					finish();
			}
		});
    }

    
}
