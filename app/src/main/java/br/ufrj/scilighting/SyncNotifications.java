package br.ufrj.scilighting;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class SyncNotifications {

	public void sync(int last) throws IOException{
		
		
		String lastString = String.valueOf(last);
		
		String data = URLEncoder.encode("last", "UTF-8") + "="
		+ URLEncoder.encode(lastString, "UTF-8");
		
		
		URL url = new URL(Config.SERVER_URL + "/SyncNotifications");
		
		URLConnection connection = url.openConnection();
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setRequestProperty("METHOD", "POST");
		HttpURLConnection httpConnection = (HttpURLConnection) connection;
		
		httpConnection.setRequestMethod("POST");

		OutputStreamWriter wr = new OutputStreamWriter(
				httpConnection.getOutputStream());
		wr.write(data);
		wr.flush();
		
		
	}
	
	
}
