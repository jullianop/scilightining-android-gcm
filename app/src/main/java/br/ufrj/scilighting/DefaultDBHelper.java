package br.ufrj.scilighting;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class DefaultDBHelper extends SQLiteOpenHelper {

        public final static String NOTIFICATIONS_TABLE = "t_person";
        public final static String MY_DATABASE_NAME = "mysqliteDB";
        public DefaultDBHelper(Context context, String name, CursorFactory factory, int version) {
                super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
                
                db.execSQL("CREATE TABLE IF NOT EXISTS " 
                + NOTIFICATIONS_TABLE 
                + " (WorkFlow VARCHAR, Notification VARCHAR," 
                + "  Time INT(8)," 
                + "  New INT(8)," +
                 "   type INT(8) );");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                Log.w("My DB", "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS notes");
        onCreate(db);
        }
}