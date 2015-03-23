package br.ufrj.scilighting.workflowList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import br.ufrj.scilighting.AdapterListView;
import br.ufrj.scilighting.Config;
import br.ufrj.scilighting.DefaultDBHelper;
import br.ufrj.scilighting.ItemListView;
import br.ufrj.scilighting.NotificationsActivity;
import br.ufrj.scilighting.R;
import br.ufrj.scilighting.Util;
import br.ufrj.scilighting.R.drawable;
import br.ufrj.scilighting.R.id;
import br.ufrj.scilighting.R.layout;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class NotificationsWorkflowListActivity extends Activity  implements OnItemClickListener {
    /** Called when the activity is first created. */
          private final String MY_DATABASE_NAME = "mysqliteDB";  
          private ListView listView;
          private AdapterWorkflowListView adapterListView;
          private ArrayList<ItemWorkflowListView> itens;
          
          
          
          public void onCreate(Bundle savedInstanceState) {
              super.onCreate(savedInstanceState);
              //Pega a referencia do ListView 
      		setContentView(R.layout.notificationslist);
      		
      		listView =  (ListView) findViewById(R.id.listView2);
              listView.setOnItemClickListener(this);
              
          /*    RelativeLayout rl = (RelativeLayout) findViewById(R.id.relativeLayout1);
              rl.setOnClickListener(listView);
           */   refreshList();
              
          }
          

      	private void refreshList() {
              
              itens = new ArrayList<ItemWorkflowListView>();
            
      		
       //setContentView(R.layout.notificationslist);
              ArrayList results = new ArrayList();
              SQLiteOpenHelper dbHelper = new DefaultDBHelper(this, MY_DATABASE_NAME, null, 1);
              SQLiteDatabase myDB = this.openOrCreateDatabase(MY_DATABASE_NAME, SQLiteDatabase.OPEN_READWRITE, null);
              try { 
                //  
     			 SharedPreferences settings = getSharedPreferences(Config.PREFS_NAME, 0);
    		     String selectedWorkflow = settings.getString("workflow", "");
    		     setTitle(selectedWorkflow);
            	 /* 
            	  Toast.makeText(this, "selectedWorkflow: " + selectedWorkflow, Toast.LENGTH_LONG).show();
*/
            	  
            	  //Create the Database (no Errors if it already exists) 
                     myDB = dbHelper.getWritableDatabase();
                     dbHelper.onCreate(myDB);
                    /*
                      myDB.execSQL("INSERT INTO " 
                          + DefaultDBHelper.NOTIFICATIONS_TABLE 
                          + " (WorkFlow, Notification, Time )" 
                          + " VALUES ('Workflow ', ' Notification', 0);"); 

                      */
                              // -- openOrCreateDatabase(name, mode, factory)
                      myDB = dbHelper.getReadableDatabase();
                      
                      
                     
                     
      			String SQL = "SELECT T.WORKFLOW, T.Notification, T.TIME, T.NEW as new, T.TYPE as type " +
      					"     FROM " + DefaultDBHelper.NOTIFICATIONS_TABLE + " T " +
      					"     WHERE T.WORKFLOW = ? "	;

                     // 
                      
                      
                      Cursor c = myDB.rawQuery(SQL,new String [] {selectedWorkflow});
                      
                    /*  Cursor c = myDB.query( DefaultDBHelper.NOTIFICATIONS_TABLE, 
                  		                 collums, 
                  		                 "New=?", 
                  		                 new String [] {"1"}, 
                  		                 "WorkFlow", 
                  		                 null, 
                  		                 null);*/
                    // Cursor c = myDB.query(DefaultDBHelper.NOTIFICATIONS_TABLE, null, null, null, null, null,"Time desc");
                     //  Check if our result was valid.  
                      //int workflowColumn = c.getColumnIndex("workflow"); 
                      int notificationColumn = c.getColumnIndex("Notification"); 
                      int timeColumn = c.getColumnIndex("Time");
                      int typeColumn = c.getColumnIndex("type");
                      int newColumn = c.getColumnIndex("new");
                       

                      
                      
                      if (c != null) {
                  
                      c.moveToFirst(); // it's very important to do this action otherwise your Cursor object did not get work



                 //       Check if at least one Result was returned.  
                      if (c.isFirst()) { 
                            int i = 0; 
                            // Loop through all Results  
                            do { 
                                 i++; 
                                // String workflow = c.getString(workflowColumn);
                                 String notification = c.getString(notificationColumn); 
                                 int isNew =  c.getInt(newColumn);
                                 int type = c.getInt(typeColumn); 
                                 int imageCode = Util.getImage(type);
                                 
                                 
                                 Date data = new Date(c.getLong(timeColumn)*1000);
                                 
                                 
                                 
                                 
                                 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                 
                                 
                                 String dataString = dateFormat.format(data);
                                 //String dataString = Util.getElapsedTime(data);
                                 //dataString = String.valueOf(data.getTime());
                              //    Add current Entry to results.  
                                

                                 
                                // results.add("" + i + ": " + workflow + " - " + notification + " - " + time + " - " + dataString); 
//                                 ItemListView item1 = new ItemListView("" + i + ": " + workflow + " - " + notification + " - " + time + " - " + dataString, R.drawable.icon);

                                 ItemWorkflowListView item1 = new ItemWorkflowListView(notification, dataString, imageCode, isNew) ;
                              	   
                              	   
//                              	   new ItemListView("" + i + ": " + workflow + " - " + notification + " - " , R.drawable.icon);
                                 itens.add(item1);
                            
                            } while (c.moveToNext()); 
                       } 
                  }
                      
              } catch (SQLiteException e) { 
              	Toast.makeText(this, e.getMessage()+ e.getStackTrace(), Toast.LENGTH_LONG).show();
              }  catch (Exception e) { 
              	Toast.makeText(this, e.getMessage()+ e.getStackTrace(), Toast.LENGTH_LONG).show();
              } finally { 
                   if (myDB != null) 
                        myDB.close(); 
              }
              
              // -- android.R.layout.simple_list_item_1 is object which belong to ListActivity itself
              // -- you only need to add list object in your main layout file
              //this.setListAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, results));
      	
              //Criamos nossa lista que preenchera o ListView
                      

              
              
      		//Define o Listener quando alguem clicar no item.
              //listView.setOnItemClickListener(this);



             //Cria o adapter
              adapterListView = new AdapterWorkflowListView(this, itens);
              
              //Define o Adapter
             // this.setListAdapter(new ArrayAdapter(this, R.layout.notificationsitemlist, itens));
              
              listView.setAdapter(adapterListView);
              //Cor quando a lista ? selecionada para ralagem.
              /*  //listView.setCacheColorHint(Color.TRANSPARENT);
              */
              
      	}
      	

      	@Override
      	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
      		    // Toast.makeText(this, "Voc? Clicou em: ", Toast.LENGTH_LONG ).show();
      			 //Pega o item que foi selecionado.
      	        ItemWorkflowListView item = adapterListView.getItem(arg2);
      	        //Demostra??o
/*      	        Toast.makeText(this, "Voc? Clicou em: " + item.getNotification(), Toast.LENGTH_LONG).show();
*/      			
      			// Get the item that was clicked
      			//Object o = this.getListAdapter().getItem(position);
      			String keyword = item.getNotification();
      			
      			 SharedPreferences settings = getSharedPreferences(Config.PREFS_NAME, 0);
      		     Editor editor = settings.edit();
      		     editor.putString("message", keyword);
      		     editor.commit();
      		     
      	 		Intent intent = new Intent(getApplicationContext(), NotificationsActivity.class);
      			startActivity(intent);
      		     // TODO Auto-generated method stub
      			
      		
      		
      	}
}