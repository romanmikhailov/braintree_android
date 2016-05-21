package com.paypal.android.sdk.onetouch.core.browser;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.FrameLayout;

public class PayPalAuthorizeActivity extends Activity {

    public static final String REDIRECT_URI_SCHEME_ARG = "PayPalAuthorizeActivity.RedirectUri";

    private String cancelUrl;
    private String baToken;

    @Override
    public void onBackPressed() {
        Uri.Builder builder = Uri.parse(cancelUrl).buildUpon();
        if (!TextUtils.isEmpty(baToken)) {
            builder.appendQueryParameter("ba_token", baToken);
        }
        Uri uri = builder.build();
        finishWithResult(uri);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout rootView = (FrameLayout) findViewById(android.R.id.content);

        String uriScheme = getIntent().getStringExtra(REDIRECT_URI_SCHEME_ARG);
        Uri uri = getIntent().getData();
        cancelUrl = uri.getQueryParameter("x-cancel");
        if (TextUtils.isEmpty(cancelUrl)) {
            cancelUrl = uri.getQueryParameter("cancel_url");
        }
        baToken = uri.getQueryParameter("ba_token");

        PayPalAuthorizeWebView payPalAuthorizeWebView = new PayPalAuthorizeWebView(this);
        payPalAuthorizeWebView.init(this, uriScheme);
        payPalAuthorizeWebView.loadUrl(getIntent().getData().toString());

        rootView.addView(payPalAuthorizeWebView);
    }

    public void finishWithResult(String url) {
        finishWithResult(Uri.parse(url));
    }

    private void finishWithResult(Uri url) {
        setResult(Activity.RESULT_OK,  new Intent(Intent.ACTION_VIEW, url));
        finish();
    }
}
