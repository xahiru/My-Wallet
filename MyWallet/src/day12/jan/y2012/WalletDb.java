package day12.jan.y2012;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class WalletDb extends SQLiteOpenHelper {
	private static final String TAG = WalletDb.class.getSimpleName();
	public static final String DB_NAME = "wallet.db";
	private static final int DB_VERSION = 1;
	/*
	 * Account Table
	 */
	public static final String TABLE_ACCOUNT = "account";
	public static final String C_ID = BaseColumns._ID;
	public static final String C_ACC_NAME = "accountname";
	public static final String C_BALANCE = "balance";
	public static final String C_MIN_BALANCE = "minbalance";
	public static final String C_OPENING_BALANCE = "opbalance";
	public static final String C_DETAILS = "details";
	public static final String C_ACC_TYPE = "type";
	public static final String C_CURRENCY = "currency";
	public static final String C_OPEN_DATE = "opendate";

//	public static final String[] DB_ACCOUNT_COLUMNS = { C_ID, C_ACC_NAME,
//			C_BALANCE, C_MIN_BALANCE, C_OPENING_BALANCE, C_DETAILS, C_ACC_TYPE,
//			C_CURRENCY, C_OPEN_DATE };
//	AUTOINCREMENT
	
	public static final String CREATE_TABLE_ACCOUNT = "CREATE TABLE "
			+ TABLE_ACCOUNT + " (" + C_ID + " int primary key , " + C_ACC_NAME
			+ " text," + C_BALANCE + " int," + C_MIN_BALANCE + " int,"
			+ C_OPENING_BALANCE + " int," + C_ACC_TYPE + " text," + C_CURRENCY
			+ " text," + C_DETAILS + " text," + C_OPEN_DATE + " int)";

	public static final String[] accountColArray = {C_ACC_NAME,C_BALANCE,C_MIN_BALANCE,C_OPENING_BALANCE,C_DETAILS,C_ACC_TYPE,C_CURRENCY,C_OPEN_DATE};
	/*
	 * Expense Table
	 */
	private static final String TABLE_EXPENSE = "expense";
	private static final String TABLE_PAYEE = "payee";
	private static final String TABLE_INCOME = "income";
	private static final String TABLE_CURRENCY = "currency";

	Context context;

	public WalletDb(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		this.context = context;

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL(CREATE_TABLE_ACCOUNT);
		Log.i(TAG, "DB created :" + CREATE_TABLE_ACCOUNT);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		db.execSQL("drop table if exists "+WalletDb.TABLE_ACCOUNT);
		onCreate(db);
		
	}

}
