package br.ufrj.scilighting;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import br.ufrj.scilighting.workflowList.NotificationsWorkflowListActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class NotificationsListActivity extends Activity  implements OnItemClickListener {
    /** Called when the activity is first created. */
          private final String MY_DATABASE_NAME = "mysqliteDB";  
          private ListView listView;
          private AdapterListView adapterListView;
          private ArrayList<ItemListView> itens;
             
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
        
        itens = new ArrayList<ItemListView>();
      
		
 //setContentView(R.layout.notificationslist);
        ArrayList results = new ArrayList();
        SQLiteOpenHelper dbHelper = new DefaultDBHelper(this, MY_DATABASE_NAME, null, 1);
        SQLiteDatabase myDB = this.openOrCreateDatabase(MY_DATABASE_NAME, SQLiteDatabase.OPEN_READWRITE, null);
        try { 
          //    Create the Database (no Errors if it already exists) 
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
                
                
                String[] collums = {"WorkFlow", "max(Time) AS Time"};
               
			String SQL = " SELECT t.workflow, t.time, count , countNew  FROM "
					+ "	     (SELECT WorkFlow, max(Time) AS Time, count(*) count "
					+ "       From " + DefaultDBHelper.NOTIFICATIONS_TABLE
					+ "       GROUP BY Workflow) as T, "
					+ "       (SELECT WorkFlow, count(*) countNew "
					+ "        From " + DefaultDBHelper.NOTIFICATIONS_TABLE
					+ "        WHERE New = ?              "
					+ "        GROUP BY Workflow) as T1" +
							"  WHERE t.workflow = t1.workflow ;" +
							"  ";

               // 
                
                
                Cursor c = myDB.rawQuery(SQL,new String [] {"1"});
                
              /*  Cursor c = myDB.query( DefaultDBHelper.NOTIFICATIONS_TABLE, 
            		                 collums, 
            		                 "New=?", 
            		                 new String [] {"1"}, 
            		                 "WorkFlow", 
            		                 null, 
            		                 null);*/
              // Cursor c = myDB.query(DefaultDBHelper.NOTIFICATIONS_TABLE, null, null, null, null, null,"Time desc");
               //  Check if our result was valid.  
           
                
                
                if (c != null) {
            
                c.moveToFirst(); // it's very important to do this action otherwise your Cursor object did not get work
                int workflowColumn = c.getColumnIndex("WorkFlow"); 
               // int notificationColumn = c.getColumnIndex("Notification"); 
                int timeColumn = c.getColumnIndex("Time");
                int countColumn = c.getColumnIndex("count");
                int countNewColumn = c.getColumnIndex("countNew");


           //       Check if at least one Result was returned.  
                
               

                
                
                
                if (c.isFirst()) { 
                      int i = 0; 
                      // Loop through all Results  
                      do { 
                           i++; 
                           String workflow = c.getString(workflowColumn);
                           String notification = "";//c.getString(notificationColumn); 
                           String time = c.getString(timeColumn);                          
                           SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                           Date data = new Date(c.getLong(timeColumn)*1000);
                           //String dataString = dateFormat.format(data);
                           String dataString = Util.getElapsedTime(data);
                           //dataString = String.valueOf(data.getTime());
                        //    Add current Entry to results.  
                          
                           String count = c.getString(countColumn) + " notification";
                           
                           count = (c.getInt(countColumn) > 1 )?  count+"s" : count;
                           
                           
                           String countNew = c.getString(countNewColumn);
                           
                          // results.add("" + i + ": " + workflow + " - " + notification + " - " + time + " - " + dataString); 
//                           ItemListView item1 = new ItemListView("" + i + ": " + workflow + " - " + notification + " - " + time + " - " + dataString, R.drawable.icon);

                           ItemListView item1 = new ItemListView(workflow, count, countNew, dataString, R.drawable.icon) ;
                        	   
                        	   
//                        	   new ItemListView("" + i + ": " + workflow + " - " + notification + " - " , R.drawable.icon);
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
        adapterListView = new AdapterListView(this, itens);
        
        //Define o Adapter
       // this.setListAdapter(new ArrayAdapter(this, R.layout.notificationsitemlist, itens));
        
        listView.setAdapter(adapterListView);
        //Cor quando a lista é selecionada para ralagem.
        /*  //listView.setCacheColorHint(Color.TRANSPARENT);
        */
        
	}
	

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		    // Toast.makeText(this, "Você Clicou em: ", Toast.LENGTH_LONG ).show();
			 //Pega o item que foi selecionado.
	        ItemListView item = adapterListView.getItem(arg2);
	        //Demostração
/*	        Toast.makeText(this, "Você Clicou em: " + item.getText(), Toast.LENGTH_LONG).show();
*/			
			// Get the item that was clicked
			//Object o = this.getListAdapter().getItem(position);
			String keyword = item.getText();
			
			 SharedPreferences settings = getSharedPreferences(Config.PREFS_NAME, 0);
		     Editor editor = settings.edit();
		     editor.putString("workflow", keyword);
		     editor.commit();
		     
	 		Intent intent = new Intent(getApplicationContext(), NotificationsWorkflowListActivity.class);
	 		
	 		
			startActivity(intent);
		     // TODO Auto-generated method stub
			
		
		
	}
}