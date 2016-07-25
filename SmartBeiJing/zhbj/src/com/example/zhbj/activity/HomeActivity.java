package com.example.zhbj.activity;

import com.example.zhbj.R;
import com.example.zhbj.fragment.ContentFragment;
import com.example.zhbj.fragment.LeftMenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.view.WindowManager;

public class HomeActivity extends SlidingFragmentActivity {
     private static final String TAG_LEFT_MENU = "left_menu";
     private static final String TAG_HOME = "home";

	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	setContentView(R.layout.activity_home);
    	
    	setBehindContentView(R.layout.fragment_left_menu);
    	SlidingMenu slidingMenu = getSlidingMenu();
    	slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
    	//slidingMenu.setBehindOffset(200);
    	WindowManager wm = getWindowManager();
    	int width = wm.getDefaultDisplay().getWidth();
    	//屏幕预留宽度
    	slidingMenu.setBehindOffset(width*200/320);
    	
    	initFragment();
    }
     
     public void initFragment(){
    	 FragmentManager fm = getSupportFragmentManager();
    	 FragmentTransaction transaction = fm.beginTransaction();
    	 transaction.replace(R.id.fl_left_menu, new LeftMenuFragment(),TAG_LEFT_MENU);
    	 transaction.replace(R.id.fl_home, new ContentFragment(), TAG_HOME);
    	 transaction.commit();
    	 
     }
     
     /**获取侧边栏对象
     * @return
     */
    public LeftMenuFragment getLeftMenuFragment(){
    	 FragmentManager fm = getSupportFragmentManager();
    	 LeftMenuFragment leftMenuFragment = (LeftMenuFragment) fm.findFragmentByTag(TAG_LEFT_MENU);
         return leftMenuFragment;
     }
    
    /**获取主页面对象
     * @return
     */
    public ContentFragment getContentFragment(){
    	FragmentManager fm = getSupportFragmentManager();
    	ContentFragment contentFragment = (ContentFragment) fm.findFragmentByTag(TAG_HOME);
    	return contentFragment;
    }
    
    
}
