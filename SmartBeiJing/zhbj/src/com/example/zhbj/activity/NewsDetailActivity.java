package com.example.zhbj.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.example.zhbj.R;

public class NewsDetailActivity extends Activity implements OnClickListener{
    private ImageView iv_menu;
    private ImageView iv_back;
    private ImageView iv_share;
    private ImageView iv_textsize;
    private WebView mWebView;
    private ProgressBar mProgressBar;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
    	setContentView(R.layout.activity_news_detail);
    	
    	initView();
    	initData();
    }

	private void initView() {
		iv_menu = (ImageView) findViewById(R.id.iv_menu);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_share = (ImageView) findViewById(R.id.iv_share);
        iv_textsize =  (ImageView) findViewById(R.id.iv_textsize);
        mWebView = (WebView) findViewById(R.id.webView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        
        iv_back.setVisibility(View.VISIBLE);
        iv_menu.setVisibility(View.INVISIBLE);
        iv_share.setVisibility(View.VISIBLE);
        iv_textsize.setVisibility(View.VISIBLE);
        
        iv_back.setOnClickListener(this);
        iv_share.setOnClickListener(this);
        iv_textsize.setOnClickListener(this);
		
	}

	private void initData() {
        String url = getIntent().getStringExtra("url");
        
        mWebView.loadUrl(url);
        settings = mWebView.getSettings();
        //显示缩放按钮
        settings.setBuiltInZoomControls(true);
        //双击缩放
        settings.setUseWideViewPort(true);
        settings.setJavaScriptEnabled(true);
	    mWebView.setWebViewClient(new WebViewClient(){
	    	@Override//开始加载网页
	    	public void onPageStarted(WebView view, String url, Bitmap favicon) {
	    		mProgressBar.setVisibility(View.VISIBLE);
	    		super.onPageStarted(view, url, favicon);
	    	}
	    	@Override//网页加载结束
	    	public void onPageFinished(WebView view, String url) {
	    		mProgressBar.setVisibility(View.INVISIBLE);
	    		super.onPageFinished(view, url);
	    	}
	    	@Override
	    	public boolean shouldOverrideUrlLoading(WebView view, String url) {
	    		//强制让网页的跳转都在webview里面
	    		view.loadUrl(url);
	    		return true;
	    	}
	    });
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.iv_textsize:
			showDialog();
			break;
		case R.id.iv_share:
			showShare();
			break;
		}
		
	}
	
	private void showShare() {
		 ShareSDK.initSDK(this);
		 OnekeyShare oks = new OnekeyShare();
		 //关闭sso授权
		 oks.disableSSOWhenAuthorize(); 

		// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
		 //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
		 // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		 oks.setTitle("分享");
		 // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		 oks.setTitleUrl("http://sharesdk.cn");
		 // text是分享文本，所有平台都需要这个字段
		 oks.setText("我是分享文本");
		 // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		 //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
		 // url仅在微信（包括好友和朋友圈）中使用
		 oks.setUrl("http://sharesdk.cn");
		 // comment是我对这条分享的评论，仅在人人网和QQ空间使用
		 oks.setComment("我是测试评论文本");
		 // site是分享此内容的网站名称，仅在QQ空间使用
		 oks.setSite(getString(R.string.app_name));
		 // siteUrl是分享此内容的网站地址，仅在QQ空间使用
		 oks.setSiteUrl("http://sharesdk.cn");

		// 启动分享GUI
		 oks.show(this);
		 }

	

	int mTempItem; //确定之前在对话框中选中的字体
	int mCurrentItem =2;//当前选中的字体
	private WebSettings settings;
	
	private void showDialog() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("设置字体大小");
		
		
		String[] items = new String[]{"超大号字体","大号字体","普通字体","小号字体","超小号字体"};
		
		dialog.setSingleChoiceItems(items, mCurrentItem, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				mTempItem = which;
			}
		});
		
		dialog.setPositiveButton("确定", new  DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (mTempItem) {
				case 0:
					settings.setTextSize(TextSize.LARGEST);
					break;
				case 1:
					settings.setTextSize(TextSize.LARGER);
					break;
				case 2:
					settings.setTextSize(TextSize.NORMAL);
					break;
				case 3:
					settings.setTextSize(TextSize.SMALLER);
					break;
				case 4:
					settings.setTextSize(TextSize.SMALLEST);
					break;							

				}
				mCurrentItem = mTempItem;
				
			}
		});
		
		dialog.setNegativeButton("取消", null);
		
		dialog.show();
	}
}
