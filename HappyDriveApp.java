package kz.telecom.happydrive;

import android.app.Application;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.SaveCallback;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKSdk;

import io.fabric.sdk.android.Fabric;
import kz.telecom.happydrive.data.DataManager;
import kz.telecom.happydrive.data.network.GlideCacheSignature;
import kz.telecom.happydrive.data.network.NetworkManager;
import kz.telecom.happydrive.util.Logger;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Sanzhar Zholdiyarov on 10/27/16.
 */
public class HappyDriveApp extends Application {
    private final static String TAG = HappyDriveApp.class.getSimpleName();
    private final static String PARSE_TOKEN = " "; // TODO: Should be available only from gradle
    private final static String PARSE_TOKEN_2 = " ";
    
    
    @Override
    public void onCreate() {
        super.onCreate();


        VKAccessTokenTracker vkAccessTokenTracker = new VKAccessTokenTracker() {
            @Override
            public void onVKAccessTokenChanged(VKAccessToken oldToken, VKAccessToken newToken) {
                if (newToken == null) {
                    Log.d(TAG, "vk token is invalid");
                }
            }
        };
        vkAccessTokenTracker.startTracking();
        VKSdk.initialize(this);

        Parse.initialize(this, PARSE_TOKEN, PARSE_TOKEN_2);
        ParseInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Exception with parse occured. The stacktrace : " + e.getMessage()); // TODO: Add Firabase crash report to log
                }
            }
        });

        Fabric.with(this, new Crashlytics.Builder()
                .core(new CrashlyticsCore.Builder()
                        .disabled(BuildConfig.DEBUG)
                        .build())
                .build());

        Logger.setLevel(BuildConfig.DEBUG ?
                Logger.Level.VERBOSE : Logger.Level.WARNING);
        NetworkManager.init(this);
        DataManager.init(this);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                .addCustomStyle(AppCompatButton.class, R.attr.mediumButtonStyle)
                .setFontAttrId(R.attr.fontPath)
                .build());
    }
}
