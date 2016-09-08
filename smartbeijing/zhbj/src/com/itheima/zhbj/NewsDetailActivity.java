package com.itheima.zhbj;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

import com.itheima.zhbj.global.GlobalConstants;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class NewsDetailActivity extends Activity implements OnClickListener{

	@ViewInject(R.id.ll_control)
	private LinearLayout llControl;
	@ViewInject(R.id.btn_back)
	private ImageButton btnBack;
	@ViewInject(R.id.btn_textsize)
	private ImageButton btnTextSize;
	@ViewInject(R.id.btn_share)
	private ImageButton btnShare;
	@ViewInject(R.id.ib_menu)
	private ImageButton btnMenu;
	@ViewInject(R.id.wv_news_detail)
	private WebView webView;
	@ViewInject(R.id.pb_loading)
	private ProgressBar pbLoading;
	
	private int mTempWhich;// 记录临时选择的字体大小(点击确定之前)

	private int mCurrenWhich = 2;// 记录当前选中的字体大小(点击确定之后), 默认正常字体

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_news_detail);
		ViewUtils.inject(this);
		
		llControl.setVisibility(View.VISIBLE);
		btnBack.setVisibility(View.VISIBLE);
		btnMenu.setVisibility(View.GONE);
		String url = getIntent().getStringExtra("url");
		url = GlobalConstants.SERVER_URL + url.split("zhbj")[1];
		int position = url.lastIndexOf(".");
		url = url.substring(0, position) + ".jsp";
		//webView.loadUrl("http://www.itheima.com");
	
		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);//支持js功能
		settings.setBuiltInZoomControls(true); //显示缩放按钮
		settings.setUseWideViewPort(true);//支持双击缩放
		settings.setDomStorageEnabled(true);
		settings.setDefaultTextEncodingName("UTF-8");
		webView.loadUrl(url);
		webView.setWebViewClient(new WebViewClient(){
			
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				//开始加载
				pbLoading.setVisibility(View.VISIBLE);
				System.out.println("...开始加载");
				super.onPageStarted(view, url, favicon);
			}
			
			@Override
			public void onPageFinished(WebView view, String url) {
				//加载结束
				System.out.println("...加载结束");
				pbLoading.setVisibility(View.INVISIBLE);
				super.onPageFinished(view, url);
			}
			
			
		});
		
		btnBack.setOnClickListener(this);
		btnShare.setOnClickListener(this);
		btnTextSize.setOnClickListener(this);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.news_detail, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;

		case R.id.btn_textsize:
			setTextSize();
			break;
			
	   case R.id.btn_share:
			showShare();
			break;
		}
		
	}

	private void setTextSize() {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("字体设置");

		String[] items = new String[] { "超大号字体", "大号字体", "正常字体", "小号字体",
				"超小号字体" };

		builder.setSingleChoiceItems(items, mCurrenWhich,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						mTempWhich = which;
					}
				});

		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 根据选择的字体来修改网页字体大小

				WebSettings settings = webView.getSettings();

				switch (mTempWhich) {
				case 0:
					// 超大字体
					settings.setTextSize(TextSize.LARGEST);
					// settings.setTextZoom(22);
					break;
				case 1:
					// 大字体
					settings.setTextSize(TextSize.LARGER);
					break;
				case 2:
					// 正常字体
					settings.setTextSize(TextSize.NORMAL);
					break;
				case 3:
					// 小字体
					settings.setTextSize(TextSize.SMALLER);
					break;
				case 4:
					// 超小字体
					settings.setTextSize(TextSize.SMALLEST);
					break;

				default:
					break;
				}

				mCurrenWhich = mTempWhich;
			}
		});

		builder.setNegativeButton("取消", null);

		builder.show();
		
	}
     //  1595fe0b8ad13
	
	
	private void showShare() {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		oks.setTheme(OnekeyShareTheme.SKYBLUE);//修改主题样式
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();

		// 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(getString(R.string.share));
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl("http://sharesdk.cn");
		// text是分享文本，所有平台都需要这个字段
		oks.setText("谢联武文本");
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		oks.setImagePath("/sdcard/test.jpg");// 确保SDcard下面存在此张图片
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
}
