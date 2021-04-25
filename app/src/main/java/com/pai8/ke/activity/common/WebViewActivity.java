package com.pai8.ke.activity.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.just.agentweb.AgentWeb;
import com.lhs.library.base.BaseActivity;
import com.lhs.library.base.NoViewModel;
import com.pai8.ke.R;
import com.pai8.ke.databinding.ActivityWebviewBinding;
import com.pai8.ke.utils.ResUtils;
import com.pai8.ke.utils.StringUtils;

import org.jetbrains.annotations.Nullable;

/**
 * 通用的webView
 * Created by gh on 2020/12/21.
 */
public class WebViewActivity extends BaseActivity<NoViewModel, ActivityWebviewBinding> {

    private AgentWeb mAgentWeb;
    private String mUrl;
    private String mTitle;

    public static void launch(Context context, String url) {
        launch(context, url, "");
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
    public void initView(@Nullable Bundle savedInstanceState) {
        Bundle bundle = getIntent().getExtras();
        mUrl = bundle.getString("url");
        mTitle = bundle.getString("title");
        setWebViewTitle(mTitle);
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(mBinding.llWrap, new LinearLayout.LayoutParams(-1, -1))
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
        mAgentWeb.getAgentWebSettings().getWebSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mAgentWeb.getAgentWebSettings().getWebSettings().setLoadWithOverviewMode(true);
        //自适应屏幕
        mAgentWeb.getAgentWebSettings().getWebSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

    }

    @Override
    public void onBackPressed() {
        mAgentWeb.getAgentWebSettings().getWebSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        if (!mAgentWeb.back()) {
            finish();
        }
    }

    private void setWebViewTitle(String title) {
        if (TextUtils.isEmpty(title)) {
            return;
        }

        if (title.length() > 12) {
            setToolBarTitle(StringUtils.subString(mTitle, 12) + "...");
        } else {
            setToolBarTitle(title);
        }
    }

    private WebChromeClient mWebChromeClient = new WebChromeClient() {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            setWebViewTitle(title);
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
