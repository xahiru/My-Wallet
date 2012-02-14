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
	private static final int DB_VERSION = 2;
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

	// public static final String[] DB_ACCOUNT_COLUMNS = { C_ID, C_ACC_NAME,
	// C_BALANCE, C_MIN_BALANCE, C_OPENING_BALANCE, C_DETAILS, C_ACC_TYPE,
	// C_CURRENCY, C_OPEN_DATE };
	// AUTOINCREMENT

	public static final String CREATE_TABLE_ACCOUNT = "CREATE TABLE "
			+ TABLE_ACCOUNT + " (" + C_ID + " int primary key , " + C_ACC_NAME
			+ " text," + C_BALANCE + " double," + C_MIN_BALANCE + " double,"
			+ C_OPENING_BALANCE + " double," + C_ACC_TYPE + " text," + C_CURRENCY
			+ " text," + C_DETAILS + " text," + C_OPEN_DATE + " int)";

	public static final String[] accountColArray = { C_ACC_NAME, C_BALANCE,
			C_MIN_BALANCE, C_OPENING_BALANCE, C_DETAILS, C_ACC_TYPE,
			C_CURRENCY, C_OPEN_DATE };
	/*
	 * Expense Table
	 */
	public static final String TABLE_EXPENSE = "expense";
	public static final String TABLE_PAYEE = "payee";
	public static final String TABLE_INCOME = "income";

	/*
	 * Currency Table
	 */
	public static final String TABLE_CURRENCY = "currency";

	public static final String CURRENCY_NAME = "currencyname";
	public static final String CURRENCY_RATE = "exchangerate";
	public static final String CURRENCY_UDATE_URL = "url";

	public static final String CREATE_CURRENCY = "CREATE TABLE "
			+ TABLE_CURRENCY + "( " + CURRENCY_NAME + " text," + CURRENCY_RATE
			+ " int," + CURRENCY_UDATE_URL + " text)";

	/*
	 * Transaction Table
	 */

	public static final String TABLE_TRANSACTION = "transactionr"; //cannot use the word "transaction" as it is a reserve word of sqlite

	public static final String TRANSACTION_ID = BaseColumns._ID;
	public static final String TR_PAYEE = "payee";
	public static final String TR_TYPE = "type";
	public static final String TR_DATE = "date";
	public static final String TR_AMOUNT = "amount";
	public static final String TR_ACCOUNT = C_ACC_NAME;
	public static final String TR_CATOGARY = "catogary";
	public static final String TR_DETAILS = C_DETAILS;
	public static final String TR_STATUS = "status";

	//+ TRANSACTION_ID + " int primary key,"
	
	public static final String CREATE_TRANSACTION = "CREATE TABLE "
			+ TABLE_TRANSACTION + "(" +TRANSACTION_ID+ "int primary key,"
			
			+ TR_PAYEE + " text," + TR_TYPE + " text," + TR_DATE + " text,"
			+ TR_AMOUNT + " double," + TR_ACCOUNT + " text," + TR_CATOGARY
			+ " text," + TR_DETAILS + " text," + TR_STATUS + " text)";
	
	
	Context context;

	public WalletDb(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		this.context = context;

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL(CREATE_TABLE_ACCOUNT);
		db.execSQL(CREATE_CURRENCY);
		db.execSQL(CREATE_TRANSACTION);

		Log.i(TAG, "DB created :" + CREATE_TABLE_ACCOUNT);
		Log.i(TAG, "DB created :" + CREATE_CURRENCY);
		Log.i(TAG,"DB created:"+ CREATE_TRANSACTION);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		db.execSQL("drop table if exists " + WalletDb.TABLE_ACCOUNT);
		db.execSQL("drop table if exists " + WalletDb.TABLE_CURRENCY);
		db.execSQL("drop table if exists "+ WalletDb.TABLE_TRANSACTION);

		onCreate(db);

	}

}
