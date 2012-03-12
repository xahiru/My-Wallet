package day12.jan.y2012;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

//upload all the codes to web
public class TransactionActivity extends Activity implements
		OnItemClickListener, OnItemSelectedListener {

	private static final String TAG = TransactionActivity.class.getSimpleName();

	private final int ADD_NEW_TRANSACTION = Menu.FIRST;
	
	private static final int TRANSACTION_LIST = 1;

	private ListView lvTransactions;
	private Spinner spnAccounts;
	private ArrayList<String> accountsArray;
	private EditText accBalance;
	private double intAccBalance;
	private String accountSelected;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.transactions);

		accountsArray = new ArrayList<String>();

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
		
		accBalance = (EditText)findViewById(R.id.accBalance);

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
			WalletDb.TABLE_TRANSACTION, null, null, null, null, null, null);


	while (cursor.moveToNext()) {
		
		String trnsAmount = cursor.getString(cursor.getColumnIndex(WalletDb.TR_AMOUNT));//+ "\t\t";
		String payeeName = cursor.getString(cursor.getColumnIndex(WalletDb.TR_PAYEE));//+ "\t\t\t\t\t\t\t\t";
		String trnsType = cursor.getString(cursor.getColumnIndex(WalletDb.TR_TYPE));//+ "\t\t\t\t\t\t\t\t";
		
		Log.d(TAG,payeeName);
		hashMap = new HashMap<String, String>();
		hashMap.put("abc", payeeName);

		hashMap.put("def", trnsType);
		hashMap.put("tel", trnsAmount);
		trsArrayList.add(hashMap);

	}
} catch (SQLException e) {
	// TODO: handle exception
}


Log.d(TAG,trsArrayList.toString());		

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
	public void onItemClick(AdapterView<?> trnsList, View view, int position, long id) {
//		startActivity(new Intent(this, AddTransactionActivity.class));
		if(trnsList.getId() == TRANSACTION_LIST){
		Log.d(TAG,"touched list id:"+String.valueOf(id));
		Toast.makeText(this, "touched :"+view.toString(), Toast.LENGTH_SHORT).show();
		
		int listID = (int) id;
		
		Intent i = new Intent(this, AddTransactionActivity.class);
		i.putExtra("ListID", listID);
		startActivity(i);
		
		
		/*
		 * open db here for editing the transaction //no need to open db here just pass the list id to addtransaction
		 * 
		 * write the values to a bundle
		 * send it to newtransactionActivity and change its ui
		 */
		
		
		
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> list, View view, int position,
			long id) {
		
		accountSelected = list.getItemAtPosition(position).toString();
			
			updateUI();

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}
	
	public void updateUI(){
		
//		double totalTransAmount =0;
		String accountBalance = "0";
		
		MyWalletApplication.db = MyWalletApplication.walletDbHelper
		.getWritableDatabase(); 
		
		ArrayList<HashMap<String, String>> ntrsArrayList = new ArrayList<HashMap<String, String>>();

		HashMap<String, String> nHashMap;
		
		try {
			Cursor cursor = MyWalletApplication.db.query(
					WalletDb.TABLE_TRANSACTION, null, WalletDb.C_ACC_NAME+"=?", new String []{accountSelected}, null, null, null);

			Cursor cursor2 = MyWalletApplication.db.query(
					WalletDb.TABLE_ACCOUNT, null, WalletDb.C_ACC_NAME+"=?", new String []{accountSelected}, null, null, null);
			while (cursor2.moveToNext()) {
				accountBalance = cursor2.getString(cursor2.getColumnIndex(WalletDb.C_BALANCE));
			
				Log.d(TAG,accountBalance);
			}
			
			
			
			while (cursor.moveToNext()) {
				
				String trnsAmount = cursor.getString(cursor.getColumnIndex(WalletDb.TR_AMOUNT));//+ "\t\t";
				String payeeName = cursor.getString(cursor.getColumnIndex(WalletDb.TR_PAYEE));//+ "\t\t\t\t\t\t\t\t";
				String trnsType = cursor.getString(cursor.getColumnIndex(WalletDb.TR_TYPE));//+ "\t\t\t\t\t\t\t\t";
				
				Log.d(TAG,"Db record"+String.valueOf(cursor.getInt(cursor.getColumnIndex(WalletDb.TR_TYPE))));
				
//				totalTransAmount += cursor.getDouble(cursor.getColumnIndex(WalletDb.TR_AMOUNT));
								
//				Log.d(TAG,payeeName);
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
		
		
//		accBalance.setText(String.valueOf(totalTransAmount));
		accBalance.setText(accountBalance);
		
		
	}
}
