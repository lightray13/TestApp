package my.project.testapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.appsflyer.AppsFlyerLib;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.applinks.AppLinkData;
import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.push.YandexMetricaPush;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent("I am working");
        YandexMetrica.reportEvent("I am working");

        Map<String,Object> eventValues = new HashMap<>();
        eventValues.put("work", "ok");
        AppsFlyerLib.getInstance().trackEvent(getApplicationContext(), "I am working", eventValues);

        webView = (WebView) findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setFocusable(true);
        webView.setFocusableInTouchMode(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.loadUrl("https://www.google.com");

        AppLinkData.fetchDeferredAppLinkData(this,
                new AppLinkData.CompletionHandler() {
                    @Override
                    public void onDeferredAppLinkDataFetched(AppLinkData appLinkData) {
                        try {
                            String link = appLinkData.getTargetUri().getAuthority();
                            Log.d("myLogs", link);
                            if (link.equals("yandex")) {
                                webView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        webView.loadUrl("https://yandex.ru");
                                    }
                                });
                            }
                        } catch (NullPointerException e) {

                        }
                    }
                }
        );


        Intent intent = getIntent();
        String action = intent.getAction();
        if (YandexMetricaPush.OPEN_DEFAULT_ACTIVITY_ACTION.equals(action)) {
            //It's push from AppMetrica Push SDK. Got it.
            handlePayload(getIntent());
        }
    }

    private void handlePayload(final Intent intent) {
        String payload = intent.getStringExtra(YandexMetricaPush.EXTRA_PAYLOAD);
        if (!TextUtils.isEmpty(payload)) {
            YandexMetrica.reportEvent("Handle payload");
        }
    }

    @Override
    protected void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);
        handlePayload(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        YandexMetrica.resumeSession(this);
    }

    @Override
    protected void onPause() {
        YandexMetrica.pauseSession(this);
        super.onPause();
    }
}
