package br.ufrj.scilighting;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
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

    // Constants
    // The authority for the sync adapter's content provider
    public static final String AUTHORITY = "com.example.android.datasync.provider";
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "example.com";
    // The account name
    public static final String ACCOUNT = "dummyaccount";
    // Instance fields
    Account mAccount;



    private OnClickListener clickRegisterNowListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
           // ContentResolver.requestSync(new Account(ACCOUNT,ACCOUNT_TYPE), AUTHORITY, null);

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

        mAccount = CreateSyncAccount(this);

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


    /**
     * Create a new dummy account for the sync adapter
     *
     * @param context The application context
     */
    public static Account CreateSyncAccount(Context context) {
        // Create the account type and default account
        Account newAccount = new Account(
                ACCOUNT, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(
                        ACCOUNT_SERVICE);
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
            //context.setIsSyncable(account, AUTHORITY, 1);
            Log.i(TAG, "Account added Explicity.");
        } else {
            /*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */
            Log.i(TAG, "Error on add Acoount Explicity.");
        }
        return newAccount;
    }
}