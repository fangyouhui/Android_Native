package com.pai8.ke.activity.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hjq.bar.OnTitleBarListener;
import com.just.agentweb.AgentWeb;
import com.pai8.ke.R;
import com.pai8.ke.base.BaseActivity;
import com.pai8.ke.utils.ResUtils;
import com.pai8.ke.utils.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 通用的webView
 * Created by gh on 2020/12/21.
 */
public class WebViewActivity extends BaseActivity {

    @BindView(R.id.ll_wrap)
    LinearLayout llWrap;

    private AgentWeb mAgentWeb;
    private String mUrl;
    private String mTitle;

    public static void launch(Context context, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void launch(Context context, String url, String title) {
        Intent intent = new Intent(context, WebViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putString("title", title);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_webview;
    }

    @Override
    public void initView() {
        Bundle bundle = getIntent().getExtras();
        mUrl = bundle.getString("url");
        mTitle = bundle.getString("title");
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(llWrap, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator(ResUtils.getColor(R.color.colorPrimary))
                .setSecurityType(AgentWeb.SecurityType.DEFAULT_CHECK)
                .setWebChromeClient(mWebChromeClient)
                .createAgentWeb()
                .ready()
                .go(mUrl);

        //支持javascript
        mAgentWeb.getAgentWebSettings().getWebSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        mAgentWeb.getAgentWebSettings().getWebSettings().setSupportZoom(false);
        // 设置出现缩放工具
        mAgentWeb.getAgentWebSettings().getWebSettings().setBuiltInZoomControls(true);
        //扩大比例的缩放
        mAgentWeb.getAgentWebSettings().getWebSettings().setUseWideViewPort(true);
        //自适应屏幕
        mAgentWeb.getAgentWebSettings().getWebSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm
                .SINGLE_COLUMN);
        mAgentWeb.getAgentWebSettings().getWebSettings().setLoadWithOverviewMode(true);
        //自适应屏幕
        mAgentWeb.getAgentWebSettings().getWebSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm
                .SINGLE_COLUMN);

        mTitleBar.setOnTitleBarListener(new OnTitleBarListener() {
            @Override
            public void onLeftClick(View v) {
                mAgentWeb.getAgentWebSettings().getWebSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                if (!mAgentWeb.back()) {
                    finish();
                }
            }

            @Override
            public void onTitleClick(View v) {

            }

            @Override
            public void onRightClick(View v) {

            }
        });

    }

    @Override
    public void initData() {
        if (mTitle != null) {
            if (mTitle.length() > 12) {
                mTitleBar.setTitle(StringUtils.subString(mTitle, 12) + "...");
            } else {
                mTitleBar.setTitle(mTitle);
            }
        }
    }

    private WebChromeClient mWebChromeClient = new WebChromeClient() {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (StringUtils.isNotEmpty(mTitle)) {
                return;
            }
            if (StringUtils.isNotEmpty(title)) {
                if (title.length() > 12) {
                    mTitleBar.setTitle(StringUtils.subString(title, 12) + "...");
                } else {
                    mTitleBar.setTitle(title);
                }
            }
        }
    };

    @Override
    protected void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            mAgentWeb.getAgentWebSettings().getWebSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAgentWeb.getWebLifeCycle().onDestroy();
    }

}
