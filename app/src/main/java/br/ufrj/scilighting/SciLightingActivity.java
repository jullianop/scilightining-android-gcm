package br.ufrj.scilighting;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import android.util.Log;



public class SciLightingActivity extends Activity {
    /** Called when the activity is first created. */

    static final String TAG = "SciLightning";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;


    private OnClickListener clickRegisterNowListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(v.getContext(), RegisterMobileActivity.class);
			startActivity(intent);
			
		}
	};
	
	@Override
    protected void onResume() {
		super.onResume();
        checkPlayServices();
		// verifyRegistered();

	};
	
	
	@Override
	public void onAttachedToWindow()  {
			super.onAttachedToWindow();
			verifyRegistered();

	};
		
	@Override
    public void onCreate(Bundle savedInstanceState) {

        if (checkPlayServices()) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
          
        Button button = (Button)findViewById(R.id.buttonRegisterNow);
        button.setOnClickListener(clickRegisterNowListener);

		//verifyRegistered();
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }
        

    }
	

	private void verifyRegistered() {
		SharedPreferences settings = getSharedPreferences(Config.PREFS_NAME, 0);
		Boolean registered = settings.getBoolean("registered",false);
		
        if(registered){

    		Intent intent = new Intent(getApplicationContext(), NotificationsListActivity.class);
    		startActivity(intent);
            	
        	
       }
	}

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}