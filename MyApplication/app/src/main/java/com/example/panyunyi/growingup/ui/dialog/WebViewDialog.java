package com.example.panyunyi.growingup.ui.dialog;

import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.panyunyi.growingup.R;


/**
 * Created by panyunyi on 2017/7/30.
 */

public class WebViewDialog extends DialogFragment {
    private WebView webView;
    static String mContent;
    private static final WebViewDialog dialog = new WebViewDialog();

    public static WebViewDialog newInstance(String content) {
        mContent=content;

        return dialog;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(">>>",mContent);
        View view=inflater.inflate(R.layout.dialog_webview_content,container,false);

        webView=(WebView) view.findViewById(R.id.dialog_webview);
        webView.getSettings().setDefaultTextEncodingName("UTF-8");//设置默认为utf-8
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().getAllowContentAccess();
        webView.getSettings().getAllowUniversalAccessFromFileURLs();
        webView.getSettings().setAllowFileAccess(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.loadUrl(mContent);

        return view;
    }

}
