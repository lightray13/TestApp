package my.project.testapp;

import android.app.Application;
import android.util.Log;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.YandexMetricaConfig;
import com.yandex.metrica.push.YandexMetricaPush;

import java.util.Map;

public class App extends Application {

    String API_key = "b96481ca-190a-4691-8ca4-87762a21ccb4";
    private static final String AF_DEV_KEY = "eacgzcTscFoRGRDxTv59dg";

    @Override
    public void onCreate() {
        super.onCreate();
        // Создание расширенной конфигурации библиотеки.
        YandexMetricaConfig config = YandexMetricaConfig.newConfigBuilder(API_key).build();
        // Инициализация AppMetrica SDK.
        YandexMetrica.activate(getApplicationContext(), config);
        // Отслеживание активности пользователей.
        YandexMetrica.enableActivityAutoTracking(this);

        YandexMetricaPush.init(getApplicationContext());

        AppsFlyerConversionListener conversionDataListener =
                new AppsFlyerConversionListener() {
                    @Override
                    public void onInstallConversionDataLoaded(Map<String, String> map) {
                        Log.d("myLogs", "onInstallConversionDataLoaded");
                    }

                    @Override
                    public void onInstallConversionFailure(String s) {
                        Log.d("myLogs", s);
                    }

                    @Override
                    public void onAppOpenAttribution(Map<String, String> map) {
                        Log.d("myLogs", "onAppOpenAttribution");
                    }

                    @Override
                    public void onAttributionFailure(String s) {
                        Log.d("myLogs", s);
                    }
                };
        AppsFlyerLib.getInstance().init(AF_DEV_KEY, conversionDataListener, getApplicationContext());
        AppsFlyerLib.getInstance().startTracking(this);
    }
}
