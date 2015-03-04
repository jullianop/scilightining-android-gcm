package br.ufrj.scilighting;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.atomic.AtomicInteger;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class RegisterMobileActivity extends Activity {


    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    /**
     * Substitute you own sender ID here. This is the project number you got
     * from the API Console, as described in "Getting Started."
     */
    String SENDER_ID = "693862710514";

    /**
     * Tag used on log messages.
     */
    static final String TAG = "SciLightning";

    TextView mDisplay;
    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    SharedPreferences prefs;
    Context context;

    String regid;




	private OnClickListener clickRegisterListener = new OnClickListener() {

        public void onClick(View v) {
			TextView tvLogin = (TextView) findViewById(R.id.editLoginRegister);
			TextView tvPassword = (TextView) findViewById(R.id.editPasswordRegister);


			
			 SharedPreferences settings = getSharedPreferences(Config.PREFS_NAME, 0);
		     Editor editor = settings.edit();
		     editor.putString("login", tvLogin.getText().toString());
		     editor.putString("password", tvPassword.getText().toString());
		     
		     editor.commit();


            context = getApplicationContext();

            // Check device for Play Services APK. If check succeeds, proceed with
            //  GCM registration.

                regid = getRegistrationId(context);

                if (regid.isEmpty()) {
                    registerInBackground();
                }





            /*
            Registro antigo
            Intent registrationIntent = new Intent("com.google.android.c2dm.intent.REGISTER");
		     registrationIntent.putExtra("app", PendingIntent.getBroadcast(v.getContext(), 0, new Intent(), 0)); // boilerplate
		     registrationIntent.putExtra("sender", "jullianop@gmail.com");
		     startService(registrationIntent);
			*/
			/*
			
			Context context = getApplicationContext();
			CharSequence text = "This device was registered for user "
					+ textViewEmail.getText() + " with sucessfull!";
			int duration = Toast.LENGTH_LONG;

			Toast toast = Toast.makeText(context, text, duration);
			//toast.show();
*/
			/*URL url;
			try {
				url = new URL(Config.SERVER_URL+"/RegisterDevice");
				URLConnection connection = url.openConnection();
				connection.setDoOutput(true);
				connection.setDoInput(true);
				connection.setRequestProperty("METHOD", "POST");
				HttpURLConnection httpConnection = (HttpURLConnection)connection;
				
				httpConnection.addRequestProperty("login", "textViewEmail");
				httpConnection.addRequestProperty("password", "textViewEmail");
				httpConnection.addRequestProperty("token", "textViewEmail");
				
				//TODO 
				
				Integer responseCode = httpConnection.getResponseCode();

				//toast = Toast.makeText(context, responseCode.toString(), duration);
				//toast.show();
				
				        if (responseCode == HttpURLConnection.HTTP_OK) {
				        	
							toast = Toast.makeText(context, httpConnection.getResponseMessage(), duration);
							toast.show();
				}

			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}           

*/

		}







	};


    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing registration ID is not guaranteed to work with
        // the new app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the registration ID in your app is up to you.
        return getSharedPreferences(RegisterMobileActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }


    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and the app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;

                    // You should send the registration ID to your server over HTTP, so it
                    // can use GCM/HTTP or CCS to send messages to your app.
                    sendRegistrationIdToBackend();

                    // For this demo: we don't need to send it because the device will send
                    // upstream messages to a server that echo back the message using the
                    // 'from' address in the message.

                    // Persist the regID - no need to register again.
                    storeRegistrationId(context, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                mDisplay.append(msg + "\n");
            }
        }.execute(null, null, null);


    }

    private void sendRegistrationIdToBackend() {
        // Your implementation here.
    }

    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }





    /** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registermobile);

        mDisplay = (TextView) findViewById(R.id.display);
		/*
	
		Intent registrationIntent = new Intent(
				"com.google.android.c2dm.intent.REGISTER");
		registrationIntent.putExtra("app",
				PendingIntent.getBroadcast(this, 0, new Intent(), 0)); // boilerplate
		registrationIntent.putExtra("sender", "jullianop@gmail.com");
		startService(registrationIntent);

		// Notificando o usu�rio

		CharSequence contentTitle = "My notification";
		CharSequence contentText = "Hello World!";
		CharSequence tickerText = "Hello";
		Context context = getApplicationContext();
		int notificationID = 1;
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);

		Util.sendNotification(notificationID, contentTitle, contentText,
				tickerText, context, this, mNotificationManager);*/

		Button button = (Button) findViewById(R.id.buttonRegister);

        gcm = GoogleCloudMessaging.getInstance(this);
		button.setOnClickListener(clickRegisterListener);



	}

}
	
