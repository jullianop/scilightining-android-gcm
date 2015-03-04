/* Copyright 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.ufrj.scilighting;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.jar.Attributes.Name;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import br.ufrj.scilighting.R.string;
import br.ufrj.scilighting.DAO.RegisterDAO;

// import com.example.jumpnote.android.Config;
import com.google.android.c2dm.C2DMBaseReceiver;
import com.google.android.c2dm.C2DMessaging;

/**
 * Broadcast receiver that handles Android Cloud to Data Messaging (AC2DM)
 * messages, initiated by the JumpNote App Engine server and routed/delivered by
 * Google AC2DM servers. The only currently defined message is 'sync'.
 */
public class C2DMReceiver extends C2DMBaseReceiver {

	public C2DMReceiver() {
		super("jullianop@gmail.com");
	}

	public C2DMReceiver(String senderId) {
		super(senderId);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Called when a registration token has been received.
	 */
	@Override
	public void onRegistered(Context context, String registrationId)
			throws IOException {

		SharedPreferences settings = getSharedPreferences(Config.PREFS_NAME, 0);
		String login = settings.getString("login", "");
		String password = settings.getString("password", "");

		try {

			String data = URLEncoder.encode("login", "UTF-8") + "="
					+ URLEncoder.encode(login, "UTF-8");
			data += "&" + URLEncoder.encode("password", "UTF-8") + "="
					+ URLEncoder.encode(password, "UTF-8");
			data += "&" + URLEncoder.encode("token", "UTF-8") + "="
					+ URLEncoder.encode(registrationId, "UTF-8");

			URL url = new URL(Config.SERVER_URL + "/RegisterDevice");
			URLConnection connection = url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestProperty("METHOD", "POST");
			HttpURLConnection httpConnection = (HttpURLConnection) connection;

			/*
			 * Remover httpConnection.addRequestProperty("login", login);
			 * httpConnection.addRequestProperty("password", password);
			 * httpConnection.addRequestProperty("token", registrationId);
			 */

			httpConnection.setRequestMethod("POST");

			OutputStreamWriter wr = new OutputStreamWriter(
					httpConnection.getOutputStream());
			wr.write(data);
			wr.flush();

		    // Get the response
		    BufferedReader rd = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
		    String line;
		    /*while ((line = rd.readLine()) != null) {
		        Log.d("C2DM", line);
		    }*/
		    line = rd.readLine();
		    CharSequence tickerText = "";
		    CharSequence contentTitle = "";
		    CharSequence contentText = "";
		    if(line.equalsIgnoreCase("NOK")){
		    	tickerText = getString(R.string.msgTickerLoginFailed);
		    	contentTitle = getString(R.string.msgTickerLoginFailed);
		    	contentText = getString(R.string.msgLoginFailed);
		    }else{
		    	tickerText = getString(R.string.msgTickerRegisterSuccess);
		    	contentTitle = getString(R.string.msgTickerRegisterSuccess);
		    	contentText = getString(R.string.msgRegisterSuccess).replace("?", line);

		    	
		    	
		    	
		    }
		    
		    wr.close();
		    rd.close();
			
			
/*			// httpConnection.
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
					connection.getOutputStream()));
			out.write("ola");
			out.flush();
			out.close();*/

			// TODO

			Integer responseCode = httpConnection.getResponseCode();
			String responseMessage = httpConnection.getResponseMessage();

			// toast = Toast.makeText(context, responseCode.toString(),
			// duration);
			// toast.show();

			if (responseCode == HttpURLConnection.HTTP_OK) {

				String ns = Context.NOTIFICATION_SERVICE;
				NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);

				int icon = R.drawable.icon;
				
				long when = System.currentTimeMillis();

				Notification notification = new Notification(icon, tickerText,
						when);

				Context context2 = getApplicationContext();
				
				
				Intent notificationIntent = new Intent(this,
						SciLightingActivity.class);
				PendingIntent contentIntent = PendingIntent.getActivity(this,
						0, notificationIntent, 0);

				notification.setLatestEventInfo(context, contentTitle,
						contentText, contentIntent);

				int HELLO_ID = 1;

				mNotificationManager.notify(HELLO_ID, notification);

			     Editor editor = settings.edit();
			     editor.putBoolean("registered", true);
			     editor.commit();			
				
				/*
				 * Toast toast = Toast.makeText(context,
				 * httpConnection.getResponseMessage(),Toast.LENGTH_LONG);
				 * toast.show();
				 */

				/*
				 * Intent intent = new Intent(context, MessageActivity.class);
				 * intent.set
				 * 
				 * Intent startMain = new Intent(Intent.ACTION_MAIN);
				 * startMain.addCategory(Intent.CATEGORY_HOME);
				 * startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				 * 
				 * startActivity(intent);
				 */
			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		String messageComplete = intent.getExtras().getString("message");

		StringTokenizer st = new StringTokenizer(messageComplete,"|");
		
		
		/*
		while(st.hasMoreElements()) {
		   System.out.println(st.nextElement());
		}
*/   	String message = st.nextToken();
		String type = st.nextToken();
		
		
		String workflow = intent.getExtras().getString("workflow");
		
		CharSequence tickerText = workflow;
	    CharSequence contentTitle = workflow;
	    CharSequence contentText = message;
	    
	    
	    
         SQLiteOpenHelper dbHelper = new DefaultDBHelper(this, DefaultDBHelper.MY_DATABASE_NAME, null, 1);
        SQLiteDatabase myDB = this.openOrCreateDatabase(DefaultDBHelper.MY_DATABASE_NAME, SQLiteDatabase.OPEN_READWRITE, null);
        try { 
             /* Create the Database (no Errors if it already exists) */
               myDB = dbHelper.getWritableDatabase();
               dbHelper.onCreate(myDB);
              
                myDB.execSQL("INSERT INTO " 
                    + DefaultDBHelper.NOTIFICATIONS_TABLE 
                    + " (WorkFlow, Notification, Time, New, type )" 
                    + " VALUES ('"+ workflow+"','"+message+"', strftime('%s','now'), 1,"+type +" );"); 

                
                   
                
                
        } catch (SQLiteException e) { 
        	Toast.makeText(this, e.getMessage()+ e.getStackTrace(), Toast.LENGTH_LONG).show();
        	
        } finally { 
             if (myDB != null) 
                  myDB.close(); 
        }
	    
	    
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);

		int icon = R.drawable.icon;
		
		long when = System.currentTimeMillis();

		Notification notification = new Notification(icon, tickerText,
				when);

		Context context2 = getApplicationContext();
		
		
		Intent notificationIntent = new Intent(this,
				NotificationsListActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this,
				0, notificationIntent, 0);

		notification.setLatestEventInfo(context, contentTitle,
				contentText, contentIntent);

		int HELLO_ID = 1;

		mNotificationManager.notify(HELLO_ID, notification);
		
		/*System.out.print("ola");

		String message = intent.getExtras().getString("message");

		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);

		int icon = R.drawable.icon;
		CharSequence tickerText = message;
		long when = System.currentTimeMillis();

		Notification notification = new Notification(icon, tickerText, when);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		mNotificationManager.notify(1, notification);*/

		// Intent intent2 = new Intent(v.getContext(),
		// RegisterMobileActivity.class)
		/*
		 * CharSequence contentTitle = "My notification"; CharSequence
		 * contentText = "Hello World!"; CharSequence tickerText = "Hello";
		 * 
		 * int notificationID = 1; String ns = Context.NOTIFICATION_SERVICE;
		 * NotificationManager mNotificationManager = (NotificationManager)
		 * getSystemService(ns);
		 * 
		 * 
		 * Util.sendNotification(notificationID, contentTitle, contentText,
		 * tickerText, context, this, mNotificationManager);
		 */
		// TODO Auto-generated method stub

	}

	@Override
	public void onError(Context context, String errorId) {
		System.out.print("ola");

		// TODO Auto-generated method stub

	}

}
/*
 * 
 * 
 * protected void onMessage(Context context, Intent intent){
 * 
 * String accountName = intent.getExtras().getString("account_name"); String
 * message = intent.getExtras().getString("message");
 * 
 * if (Log.isLoggable(TAG, Log.DEBUG)) { Log.d(TAG,
 * "Messaging request received for account " + accountName); }
 * 
 * 
 * 
 * 
 * 
 * };
 * 
 * static final String TAG = Config.makeLogTag(C2DMReceiver.class);
 * 
 * public C2DMReceiver() { super(Config.C2DM_SENDER); }
 * 
 * @Override public void onError(Context context, String errorId) {
 * Toast.makeText(context, "Messaging registration error: " + errorId,
 * Toast.LENGTH_LONG).show(); }
 * 
 * @Override protected void onMessage(Context context, Intent intent) { String
 * accountName = intent.getExtras().getString(Config.C2DM_ACCOUNT_EXTRA); String
 * message = intent.getExtras().getString(Config.C2DM_MESSAGE_EXTRA); if
 * (Config.C2DM_MESSAGE_SYNC.equals(message)) { if (accountName != null) { if
 * (Log.isLoggable(TAG, Log.DEBUG)) { Log.d(TAG,
 * "Messaging request received for account " + accountName); }
 * 
 * ContentResolver.requestSync( new Account(accountName,
 * SyncAdapter.GOOGLE_ACCOUNT_TYPE), JumpNoteContract.AUTHORITY, new Bundle());
 * } } }
 *//**
 * Register or unregister based on phone sync settings. Called on each
 * performSync by the SyncAdapter.
 */
/*
 * public static void refreshAppC2DMRegistrationState(Context context) { //
 * Determine if there are any auto-syncable accounts. If there are, make sure we
 * are // registered with the C2DM servers. If not, unregister the application.
 * boolean autoSyncDesired = false; if
 * (ContentResolver.getMasterSyncAutomatically()) { AccountManager am =
 * AccountManager.get(context); Account[] accounts =
 * am.getAccountsByType(SyncAdapter.GOOGLE_ACCOUNT_TYPE); for (Account account :
 * accounts) { if (ContentResolver.getIsSyncable(account,
 * JumpNoteContract.AUTHORITY) > 0 &&
 * ContentResolver.getSyncAutomatically(account, JumpNoteContract.AUTHORITY)) {
 * autoSyncDesired = true; break; } } }
 * 
 * boolean autoSyncEnabled =
 * !C2DMessaging.getRegistrationId(context).equals("");
 * 
 * if (autoSyncEnabled != autoSyncDesired) { Log.i(TAG,
 * "System-wide desirability for JumpNote auto sync has changed; " +
 * (autoSyncDesired ? "registering" : "unregistering") +
 * " application with C2DM servers.");
 * 
 * if (autoSyncDesired == true) { C2DMessaging.register(context,
 * Config.C2DM_SENDER); } else { C2DMessaging.unregister(context); } } } }
 */