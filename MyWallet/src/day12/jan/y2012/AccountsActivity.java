package day12.jan.y2012;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class AccountsActivity extends ListActivity {

	private static final String TAG = AccountsActivity.class.getSimpleName();
	private static final int ADD_NEW_ACCOUNT = Menu.FIRST;
	private static final int REPORTS = Menu.FIRST +1;
	
	private static final int DELETE_ACCOUNT = 0;

	private String[] Accounts = new String[] { "hello", "no accounts to show",
			"add new account from menu" };
	private ArrayList<String> acc;

	ListView lvAccount;
	int requestCode;
	String accountName = "no accounts to display";//"drop table if exists account";
	ContentValues contval;
	
	private String editSelectedString;
	
	private boolean longpressed = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		acc = new ArrayList<String>();

		MyWalletApplication.db = MyWalletApplication.walletDbHelper
				.getWritableDatabase(); // open db for writing ....actually writing is not neccesary here
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
				if(!longpressed){
				
				/*
				 * call the edit accounts from here
				 */
				
				editSelectedString = parent.getItemAtPosition(position).toString();
//				Toast.makeText(getApplicationContext(),
//						editSelectedString, Toast.LENGTH_SHORT).show();
				
				
								
				startAddorEdit(editSelectedString);
				

			}
				longpressed = false; /*
				this is to handle the differentiation between a press and a longpress
				
				*/
			}

		});
		
		lvAccount.setOnItemLongClickListener(new OnItemLongClickListener() {
			
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(getApplicationContext(), "want to delete!!", Toast.LENGTH_SHORT).show();
			
				/*
				 * do the confirmation here
				 * 
				 * deletion will be done in the dialog onclick method
				 *
				 */
				editSelectedString = parent.getItemAtPosition(position).toString();
				
				showDialog(DELETE_ACCOUNT);
				
				longpressed = true;
				
				return false;
			}
		});
		
/// add code referesh UI

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
		
		/*
		 * this code is used to update UI (list view with the account lists)
		 */
		/*
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
			/*
			 * updates the oldname from ui
			 */
		/*
			if(contval.get("oldname") != ""){
				acc.remove(contval.get("oldname"));
				Log.d(TAG,contval.get("oldname").toString());
			}
			acc.add(contval.get(WalletDb.C_ACC_NAME).toString());
		}
*/
		/*
		 * Late i discovered that onCreate recreates everything so mannual UI update is not neccessary
		 * as oncreates retrieve data from database
		 */
		
		super.onActivityResult(requestCode, resultCode, data);
		onCreate(null); /*
		this code alones update UI*/
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
	
	
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dlg;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		switch (id) {
		case DELETE_ACCOUNT:
			builder.setTitle(R.string.deleteConfirmation);
			builder.setPositiveButton(R.string.delete, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//delete the codes from here..
					Log.d(TAG, "deleted");
					
					MyWalletApplication.db = MyWalletApplication.walletDbHelper
					.getWritableDatabase();
					
					MyWalletApplication.db.delete(WalletDb.TABLE_ACCOUNT, WalletDb.C_ACC_NAME+" = ?", new String [] { editSelectedString});
					
					MyWalletApplication.db.close();// close after writing
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
}
