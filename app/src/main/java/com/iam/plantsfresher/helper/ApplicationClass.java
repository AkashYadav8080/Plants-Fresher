package com.iam.plantsfresher.helper;

import android.app.Application;
import com.onesignal.Continue;
import com.onesignal.OneSignal;
import com.onesignal.debug.LogLevel;
import com.iam.plantsfresher.BuildConfig;

public class ApplicationClass extends Application {
//    private static final String ONESIGNAL_APP_ID = "bb3c3f99-5628-42f3-92d8-a0495fe77a06";
    private static final String ONESIGNAL_APP_ID = BuildConfig.ONESIGNAL_APP_ID;


    @Override
    public void onCreate() {
        super.onCreate();

        // Enable verbose logging for debugging (remove in production)
        OneSignal.getDebug().setLogLevel(LogLevel.VERBOSE);
        // Initialize with your OneSignal App ID
        OneSignal.initWithContext(this, ONESIGNAL_APP_ID);

    }
}
