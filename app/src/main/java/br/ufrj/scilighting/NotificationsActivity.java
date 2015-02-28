package br.ufrj.scilighting;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;



public class NotificationsActivity extends Activity {
    /** Called when the activity is first created. */
   

	
@Override
public void onAttachedToWindow() {
	// TODO Auto-generated method stub
	super.onAttachedToWindow();
	
	SharedPreferences settings = getSharedPreferences(Config.PREFS_NAME, 0);
	//String workflow = settings.getString("workflow", "");
	String message = settings.getString("message", "No message was received");
	
	//TextView tvWorkflow = (TextView)findViewById(R.id.workflow);
	TextView tvMessage = (TextView)findViewById(R.id.message);

	//tvWorkflow.setText(workflow);
	tvMessage.setText(message);
	
	
	
	
	
	
	
	
}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications);
        
        
	  /*  CharSequence contentTitle = "MafftAdaptive_7 - Phylogeny using RAXML";
	    CharSequence contentText = "A error ocurred during the workflow execution. Workspace /root/exp/readseq/73/. Exitstatus 126. VM number 2. Duration 1.009 seconds.";
        
        String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);

		int icon = R.drawable.icon;
		
		long when = System.currentTimeMillis();

		Notification notification = new Notification(icon, "",
				when);

		Context context2 = getApplicationContext();
		
		
		Intent notificationIntent = new Intent(this,
				SciLightingActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this,
				0, notificationIntent, 0);

		notification.setLatestEventInfo(this.getBaseContext(), contentTitle,
				contentText, contentIntent);

		int HELLO_ID = 1;

		mNotificationManager.notify(HELLO_ID, notification);
*/
          
        
        

    }
}