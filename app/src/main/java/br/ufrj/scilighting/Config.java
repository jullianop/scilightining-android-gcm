package br.ufrj.scilighting;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Config extends Activity {


    static final String SERVER_URL = "http://192.168.1.102:8080/SciLightining-server";


    public static final String PREFS_NAME = "MyPrefsFile";


    public static final String PREF_GCM_REGISTRATION_ID = "gcm_registration_id";
    public static final String PREF_LAST_NOTIFICATION_TIME = "lastNotificationTime";


    @SuppressWarnings("unchecked")
    public static String makeLogTag(Class cls) {
        String tag = "SciLightning_" + cls.getSimpleName();
        return (tag.length() > 23) ? tag.substring(0, 23) : tag;
    }


	
}
