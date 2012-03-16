package day12.jan.y2012;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.util.Log;


public class MyWalletApplication extends Application  implements OnSharedPreferenceChangeListener
{
	//rev 3
	private static final String TAG = MyWalletApplication.class.getSimpleName();
	public static int runCount =0;
	private SharedPreferences sharedPrefs;
//	private MyWalletActivity wallet;
	public static String username="user";
	public static String password="pass";
	public static boolean login=false; 
	
	static WalletDb walletDbHelper;
	 static SQLiteDatabase db;
	 
//	 Context c;
	
	@Override
	public void onCreate() {
		
		walletDbHelper = new WalletDb(getApplicationContext());
//		getApplicationContext().deleteDatabase(WalletDb.DB_NAME);
		
		//gets the preference manager
		this.sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		//register the preferences with the application context
		this.sharedPrefs.registerOnSharedPreferenceChangeListener(this);
		Log.i(TAG,"created");
		
		
		
	}
	
	
	/*
	public  SQLiteDatabase getDB(){
		return db = walletDbHelper.getWritableDatabase();
	}
*/
	public  void closeDB(){
//		getApplicationContext().deleteDatabase(WalletDb.DB_NAME);
	}
	

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		MyWalletApplication.username = sharedPreferences.getString("username", "no usrname");
		MyWalletApplication.password = sharedPreferences.getString("password", "no pass");
		MyWalletApplication.login = sharedPreferences.getBoolean("login", false);
		
	}
	
	

}
