package br.ufrj.scilighting;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MessageActivity extends Activity {

	private OnClickListener clickRegisterListener = new OnClickListener() {
		public void onClick(View v) {
			TextView tvLogin = (TextView) findViewById(R.id.editLoginRegister);
			TextView tvPassword = (TextView) findViewById(R.id.editPasswordRegister);


			
			 SharedPreferences settings = getSharedPreferences(Config.PREFS_NAME, 0);
		     Editor editor = settings.edit();
		     editor.putString("login", tvLogin.getText().toString());
		     editor.putString("password", tvPassword.getText().toString());
		     
		     editor.commit();
		     
		     Intent registrationIntent = new Intent("com.google.android.c2dm.intent.REGISTER");
		     registrationIntent.putExtra("app", PendingIntent.getBroadcast(v.getContext(), 0, new Intent(), 0)); // boilerplate
		     registrationIntent.putExtra("sender", "jullianop@gmail.com");
		     startService(registrationIntent);
			
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

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message);

		/*
	
		Intent registrationIntent = new Intent(
				"com.google.android.c2dm.intent.REGISTER");
		registrationIntent.putExtra("app",
				PendingIntent.getBroadcast(this, 0, new Intent(), 0)); // boilerplate
		registrationIntent.putExtra("sender", "jullianop@gmail.com");
		startService(registrationIntent);

		// Notificando o usuário

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

		button.setOnClickListener(clickRegisterListener);

	}

}
	
