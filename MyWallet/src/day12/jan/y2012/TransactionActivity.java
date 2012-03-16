package day12.jan.y2012;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

//upload all the codes to web
//rev 3
public class TransactionActivity extends Activity implements
		OnItemClickListener, OnItemSelectedListener, OnItemLongClickListener {

	private static final String TAG = TransactionActivity.class.getSimpleName();

	private final int ADD_NEW_TRANSACTION = Menu.FIRST;

	private static final int TRANSACTION_LIST = 1;

	private static final int DELETE_TRANSACTION = 0;

	private ListView lvTransactions;
	int listID;

	private Spinner spnAccounts;
	private ArrayList<String> accountsArray;

	// private int listPosition = 0;
	private ArrayList<String> idList; // saves transaction ids with listview id
	private ArrayList<String> accList;

	private EditText accBalance;
	private double intAccBalance;
	private String accountSelected;

	private boolean longpressed = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.transactions);

		accountsArray = new ArrayList<String>();
		accList = new ArrayList<String>();
		idList = new ArrayList<String>();

		MyWalletApplication.db = MyWalletApplication.walletDbHelper
				.getWritableDatabase(); // open db for writing ....actually
										// writing is not neccesary here
		try {
			Cursor cursor = MyWalletApplication.db.query(
					WalletDb.TABLE_ACCOUNT, null, null, null, null, null, null);

			while (cursor.moveToNext()) {
				accountsArray.add(cursor.getString(cursor
						.getColumnIndex(WalletDb.C_ACC_NAME)));

			}
		} catch (SQLException e) {
			// TODO: handle exception
		}

		lvTransactions = (ListView) findViewById(R.id.lvTransactions);
		lvTransactions.setId(TRANSACTION_LIST);
		lvTransactions.setOnItemClickListener(this);
		lvTransactions.setOnItemLongClickListener(this);

		accBalance = (EditText) findViewById(R.id.accBalance);

		spnAccounts = (Spinner) findViewById(R.id.spnAccount);

		accBalance.setText(String.valueOf(intAccBalance));

		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, accountsArray);
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnAccounts.setAdapter(adapter1);

		spnAccounts.setOnItemSelectedListener(this);

		/*
		 * set listview... This view will be filled with the transactions..if
		 * not transaction is done...list will be empty
		 */

		ArrayList<HashMap<String, String>> trsArrayList = new ArrayList<HashMap<String, String>>();

		HashMap<String, String> hashMap;

		MyWalletApplication.db = MyWalletApplication.walletDbHelper
				.getWritableDatabase(); // open db for writing ....actually
										// writing is not neccesary here
		try {

			Cursor cursor = MyWalletApplication.db.query(
					WalletDb.TABLE_TRANSACTION, null, null, null, null, null,
					null);

			while (cursor.moveToNext()) {

				String trnsAmount = cursor.getString(cursor
						.getColumnIndex(WalletDb.TR_AMOUNT));// + "\t\t";
				String payeeName = cursor.getString(cursor
						.getColumnIndex(WalletDb.TR_PAYEE));// +
															// "\t\t\t\t\t\t\t\t";
				String trnsType = cursor.getString(cursor
						.getColumnIndex(WalletDb.TR_TYPE));// +
															// "\t\t\t\t\t\t\t\t";

				String trnsAcc = cursor.getString(cursor
						.getColumnIndex(WalletDb.TR_ACCOUNT));
				
				String trnsID = cursor.getString(cursor
						.getColumnIndex(WalletDb.TRANSACTION_ID));

				idList.add(trnsID);
				accList.add(trnsAcc);

				Log.d(TAG, payeeName);
				hashMap = new HashMap<String, String>();
				hashMap.put("abc", payeeName);

				hashMap.put("def", trnsType);
				hashMap.put("tel", trnsAmount);
				trsArrayList.add(hashMap);

			}
		} catch (SQLException e) {
			// TODO: handle exception
		}

		Log.d(TAG, "*******************");
		String[] abc = new String[idList.toArray().length];

		idList.toArray(abc);

		for (int i = 0; i < abc.length; i++) {

			Log.d(TAG, abc[i]);
		}

		Log.d(TAG, "*******************");

		Log.d(TAG, trsArrayList.toString());

		SimpleAdapter adapter = new SimpleAdapter(this, trsArrayList,
				R.layout.transactionrecords,
				new String[] { "abc", "def", "tel" }, new int[] {
						R.id.textRow1, R.id.textRow2, R.id.textRow3 });
		lvTransactions.setAdapter(adapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		menu.add(0, ADD_NEW_TRANSACTION, 0, R.string.add);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case ADD_NEW_TRANSACTION:
			startActivity(new Intent(this, AddTransactionActivity.class));
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemClick(AdapterView<?> trnsList, View view, int position,
			long id) {
		// startActivity(new Intent(this, AddTransactionActivity.class));

		int thisID = (int) id;
		if (!longpressed) {
			if (trnsList.getId() == TRANSACTION_LIST) {
				Log.d(TAG,
						"touched list id:" + String.valueOf(idList.get(thisID)));
				Toast.makeText(this, "touched :" + idList.get(thisID),
						Toast.LENGTH_SHORT).show();

				// listID = (int) id; this part is nolonger required as position
				// is not used anymore

				Intent i = new Intent(this, AddTransactionActivity.class);

				i.putExtra("ListID", idList.get(thisID));// by doing this,
			//	i.putExtra("Account", accList.get(thisID));											// primary key is
															// passed instead of
															// listview
				startActivity(i);

				/*
				 * open db here for editing the transaction //no need to open db
				 * here just pass the list id to addtransaction
				 * 
				 * write the values to a bundle send it to
				 * newtransactionActivity and change its ui
				 */
				longpressed = false;
			}

		}
	}

	@Override
	public void onItemSelected(AdapterView<?> list, View view, int position,
			long id) {
		
		idList.clear();
//		accList.clear();
		accountSelected = list.getItemAtPosition(position).toString();

		updateUI();

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	public void updateUI() {

		// double totalTransAmount =0;
		String accountBalance = "0";

		MyWalletApplication.db = MyWalletApplication.walletDbHelper
				.getWritableDatabase();

		ArrayList<HashMap<String, String>> ntrsArrayList = new ArrayList<HashMap<String, String>>();

		HashMap<String, String> nHashMap;

		try {
			Cursor cursor = MyWalletApplication.db.query(
					WalletDb.TABLE_TRANSACTION, null, WalletDb.C_ACC_NAME
							+ "=?", new String[] { accountSelected }, null,
					null, null);

			Cursor cursor2 = MyWalletApplication.db.query(
					WalletDb.TABLE_ACCOUNT, null, WalletDb.C_ACC_NAME + "=?",
					new String[] { accountSelected }, null, null, null);
			while (cursor2.moveToNext()) {
				accountBalance = cursor2.getString(cursor2
						.getColumnIndex(WalletDb.C_BALANCE));

				Log.d(TAG, accountBalance);
			}

			while (cursor.moveToNext()) {

				String trnsAmount = cursor.getString(cursor
						.getColumnIndex(WalletDb.TR_AMOUNT));// + "\t\t";
				String payeeName = cursor.getString(cursor
						.getColumnIndex(WalletDb.TR_PAYEE));// +
															// "\t\t\t\t\t\t\t\t";
				String trnsType = cursor.getString(cursor
						.getColumnIndex(WalletDb.TR_TYPE));// +
															// "\t\t\t\t\t\t\t\t";
				
				String trnsID = cursor.getString(cursor
						.getColumnIndex(WalletDb.TRANSACTION_ID));
				

//				String trnsAcc = cursor.getString(cursor
//						.getColumnIndex(WalletDb.TR_ACCOUNT));

				idList.add(trnsID);
//				accList.add(trnsAcc);

				Log.d(TAG,
						"Db record"
								+ String.valueOf(cursor.getInt(cursor
										.getColumnIndex(WalletDb.TR_TYPE))));

				// totalTransAmount +=
				// cursor.getDouble(cursor.getColumnIndex(WalletDb.TR_AMOUNT));

				// Log.d(TAG,payeeName);
				nHashMap = new HashMap<String, String>();
				nHashMap.put("abc", payeeName);

				nHashMap.put("def", trnsType);
				nHashMap.put("tel", trnsAmount);
				ntrsArrayList.add(nHashMap);

			
			}
		} catch (SQLException e) {
			// TODO: handle exception
			
		}
		

		MyWalletApplication.db.close();

		SimpleAdapter adapter = new SimpleAdapter(this, ntrsArrayList,
				R.layout.transactionrecords,
				new String[] { "abc", "def", "tel" }, new int[] {
						R.id.textRow1, R.id.textRow2, R.id.textRow3 });
		lvTransactions.setAdapter(adapter);

		// accBalance.setText(String.valueOf(totalTransAmount));
		accBalance.setText(accountBalance);

	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		listID = (int) id;

		showDialog(DELETE_TRANSACTION);
		longpressed = true;

		return false;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dlg;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		switch (id) {
		case DELETE_TRANSACTION:
			builder.setTitle(R.string.deleteConfirmation);
			builder.setPositiveButton(R.string.delete, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// delete the codes from here..
					
					
					int restoreAmount = 0;
					int oldBalance = 0;
					String restoreAccount= null;
					String restoreType = null;

					MyWalletApplication.db = MyWalletApplication.walletDbHelper
							.getWritableDatabase();

					Cursor newCursor = MyWalletApplication.db.query(
							WalletDb.TABLE_TRANSACTION, null,
							WalletDb.TRANSACTION_ID + " = ?",
							new String[] { idList.get(listID) }, null, null,
							null);
					
					while (newCursor.moveToNext()){
						
						restoreAmount = newCursor.getInt(newCursor.getColumnIndex(WalletDb.TR_AMOUNT));
						restoreAccount = newCursor.getString(newCursor.getColumnIndex(WalletDb.TR_ACCOUNT));
						restoreType = newCursor.getString(newCursor.getColumnIndex(WalletDb.TR_TYPE));
						
					}
					
					 newCursor = MyWalletApplication.db.query(
							WalletDb.TABLE_ACCOUNT, null,
							WalletDb.C_ACC_NAME + " = ?",
							new String[] { restoreAccount }, null, null,
							null);
					 
					 while (newCursor.moveToNext()){
						 
						 oldBalance = newCursor.getInt(newCursor.getColumnIndex(WalletDb.C_BALANCE));
					 }
					
						ContentValues cv = new ContentValues();
						
					if(!restoreType.equals("Income") ){
						
						//oldBalance + restoreAmount;
						
						cv.put(WalletDb.C_BALANCE, oldBalance + restoreAmount);
						MyWalletApplication.db.update(WalletDb.TABLE_ACCOUNT, cv, WalletDb.C_ACC_NAME+"=?",new String[] {restoreAccount} );
						
												
					}else {
					
						cv.put(WalletDb.C_BALANCE, oldBalance - restoreAmount);
						MyWalletApplication.db.update(WalletDb.TABLE_ACCOUNT, cv, WalletDb.C_ACC_NAME+"=?",new String[] {restoreAccount} );
					}
					
					
					
					Log.d(TAG,String.valueOf(restoreAmount)+" , account: "+restoreAccount+", type: "+restoreType );

					MyWalletApplication.db.delete(WalletDb.TABLE_TRANSACTION,
							WalletDb.TRANSACTION_ID + " = ?",
							new String[] { idList.get(listID) });
					// make sure revert all the account balances
					
					MyWalletApplication.db.close();// close after writing
					
					Log.d(TAG, "deleted");
					onResume();
				}
			});
			builder.setCancelable(true);

			break;

		default:
			break;
		}
		dlg = builder.create();
		return dlg;
	}
	
	@Override
	protected void onResume() {
//		super.onResume();
		onCreate(null);
	}
}
