package br.ufrj.scilighting;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class Util{

	static void sendNotification(int notificationID, CharSequence contentTitle, CharSequence contentText, CharSequence tickerText, Context context, Context activity, NotificationManager mNotificationManager) {

		int icon = R.drawable.icon;
		
		long when = System.currentTimeMillis();

		Notification notification = new Notification(icon, tickerText, when);


		Intent notificationIntent = new Intent(activity,
				RegisterMobileActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(activity, 0,
				notificationIntent, 0);

		notification.setLatestEventInfo(context, contentTitle, contentText,
				contentIntent);

		mNotificationManager.notify(notificationID, notification);
	}
	
	/**
	 * Formats a given {@link Date} to display a since-then timespan.
	 * @param created
	 * @return String with a format like "3 minutes ago"
	 */
	public static String getElapsedTime(Date created) {
	    long diff = System.currentTimeMillis() - created.getTime();
	    long secondInMillis = 1000;
	    long minuteInMillis = secondInMillis * 60;
	    long hourInMillis = minuteInMillis * 60;
	    long dayInMillis = hourInMillis * 24;
	    long yearInMillis = dayInMillis * 365;

	    long elapsedYears = diff / yearInMillis;
	    diff = diff % yearInMillis;
	    long elapsedDays = diff / dayInMillis;
	    diff = diff % dayInMillis;
	    long elapsedHours = diff / hourInMillis;
	    diff = diff % hourInMillis;
	    long elapsedMinutes = diff / minuteInMillis;
	    diff = diff % minuteInMillis;
	    long elapsedSeconds = diff / secondInMillis;
	    
	    if (elapsedYears > 0) {
	        return elapsedYears + " years";
	    }
	    
	    if (elapsedDays > 0) {
	        return elapsedDays + " days";
	    }
	    if (elapsedHours > 0) {
	        return elapsedHours + " hrs";
	    }
	    if (elapsedMinutes > 0) {
	        return elapsedMinutes + " minutes";
	    }

	    return elapsedSeconds + " seconds";
	}
	
	public static int getImage(int type){
/*		1;"ERROR"
		2;"FINISHED"
		3;"FILE"
		4;"MILESTONE RUNNING"
		5;"MILESTONE FINISHED"
		6;"UNUSUAL"

		*/
		
		switch (type) {
		case 1:
			return R.drawable.error;
		case 2:
			return R.drawable.finished_flag;
		case 3:
			return R.drawable.document;
		case 4:
			return R.drawable.green_flag;
		case 5:
			return R.drawable.blue_flag;
		case 6:
			return R.drawable.time;
			
		default:
			break;
		}  
		return R.drawable.icon;
		
	}
	
	
	
}
