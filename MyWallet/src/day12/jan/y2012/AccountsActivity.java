package day12.jan.y2012;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.app.ListActivity;
import android.content.ContentValues;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AccountsActivity extends ListActivity {

	private static final String TAG = AccountsActivity.class.getSimpleName();
	private static final int ADD_NEW_ACCOUNT = Menu.FIRST;
	private static final int REPORTS = Menu.FIRST +1;

	private String[] Accounts = new String[] { "hello", "no accounts to show",
			"add new account from menu" };
	private ArrayList<String> acc;

	ListView lvAccount;
	int requestCode;
	String accountName = "no accounts to display";//"drop table if exists account";
	ContentValues contval;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		acc = new ArrayList<String>();

		MyWalletApplication.db = MyWalletApplication.walletDbHelper
				.getWritableDatabase(); // open db for writing
		try {
			Cursor cursor = MyWalletApplication.db.query(
					WalletDb.TABLE_ACCOUNT, null, null, null, null, null, null);

			while (cursor.moveToNext()) {
				acc.add(cursor.getString(cursor
						.getColumnIndex(WalletDb.C_ACC_NAME)));

			}
		} catch (SQLException e) {
			// TODO: handle exception
		}
	

		MyWalletApplication.db.close();// close after writing
		// Log.d(TAG,String.valueOf(acc.isEmpty()));
		
	
		
		if (acc.isEmpty())
			acc = new ArrayList<String>(Arrays.asList(Accounts));

		setListAdapter(new ArrayAdapter<String>(this, R.layout.account, acc));

		lvAccount = getListView();
		lvAccount.setTextFilterEnabled(true);// //set this from user pereference
												// setting for list search

		lvAccount.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				/*
				 * call the edit accounts from here
				 */
				String editSelectedString;
				editSelectedString = parent.getItemAtPosition(position).toString();
//				Toast.makeText(getApplicationContext(),
//						editSelectedString, Toast.LENGTH_SHORT).show();
				
				
								
				startAddorEdit(editSelectedString);
				

			}

		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, ADD_NEW_ACCOUNT, 0, R.string.menu_add_account);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case ADD_NEW_ACCOUNT:
			// String accountName ="Saving";///get this from a dialogbox or from
			// a new activity
			/*
			ContentValues data = new ContentValues();
			MyWalletApplication.db = MyWalletApplication.walletDbHelper
					.getWritableDatabase(); // open db for writing

			data.put(WalletDb.C_ACC_NAME, accountName);
//			 MyWalletApplication.db.insertOrThrow(WalletDb.TABLE_ACCOUNT,
//			 null,
//			 data);

//			 MyWalletApplication.db.execSQL(accountName);

			 MyWalletApplication.db.query(WalletDb.TABLE_ACCOUNT, null , null,
			 null, null, null, null);

			MyWalletApplication.db.close();// close after writing
*/
			startAddorEdit(null); //no intent is passed however new intent is created while calling the next activity

			Log.d(TAG, acc.toString());
			// testing

			break;
		case REPORTS:
			
			break;
		default:
			break;
		}

		// lvAccount.setSelection(0);
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		if (accountName == "")
			acc.add(accountName);
		lvAccount.setSelection(0);
		super.onResume();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			Bundle b = data.getExtras();
			Log.d(TAG, b.toString());
			Log.d(TAG, b.getParcelable("val").toString()); // "val" should be
															// exact of that in
															// AddnewActivity
															// class
															// (b.putParceble("val"));
			 contval = b.getParcelable("val");
			Log.d(TAG, contval.get(WalletDb.C_ACC_NAME).toString());
			acc.add(contval.get(WalletDb.C_ACC_NAME).toString());
		}

		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private void startAddorEdit(String i){

		if (i == null){
						
		startActivityForResult(
				new Intent(this, AddNewAccountActivity.class), requestCode);
		}
		else
			
		{
			Intent ii = new Intent(this, AddNewAccountActivity.class);
			ii.putExtra(WalletDb.C_ACC_NAME, i);
			startActivityForResult(
					ii, requestCode);
		}
	}
}
