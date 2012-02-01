package day12.jan.y2012;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddNewAccountActivity extends Activity implements OnClickListener,
		OnItemSelectedListener {
	private static final String TAG = AddNewAccountActivity.class
			.getSimpleName();
	private Button btnAdd;
	private Button btnCancel;
	private Spinner spnAccType;
	private Spinner spnCurrency;
	private EditText edtxtAccName;
	private EditText edtxtAccDetails;
	private EditText edtxtOPBalance;
	private EditText edtxtMinBalance;

	private String accountName;
	private String accountDetails;
	private int openingBalance;
	private int openDate;
	private int balance;
	private String currency;
	private int minBalance;
	private String accountType;

	private ContentValues values;

	private static final int SPINNER_ACCTYPE = 1;
	private static final int SPINNER_CURENCY = 2;

	private Intent intent;
	String str = "val";
	Bundle editBundle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.newaccount);

		edtxtAccName = (EditText) findViewById(R.id.edttxtAccName);
		edtxtAccDetails = (EditText) findViewById(R.id.edttxtAccDetails);
		edtxtOPBalance = (EditText) findViewById(R.id.edttxtAccBalance);
		edtxtMinBalance = (EditText) findViewById(R.id.edttxtminBalance);

		spnAccType = (Spinner) findViewById(R.id.spnAccType);
		spnAccType.setId(SPINNER_ACCTYPE);
		spnAccType.setOnItemSelectedListener(this);
		spnCurrency = (Spinner) findViewById(R.id.spnCurrency);
		spnCurrency.setId(SPINNER_CURENCY);
		spnCurrency.setOnItemSelectedListener(this);

		btnAdd = (Button) findViewById(R.id.btnAddAccount);
		btnAdd.setOnClickListener(this);
		btnCancel = (Button) findViewById(R.id.btnCancelAccountAdd);
		btnCancel.setOnClickListener(this);

		editBundle = getIntent().getExtras();

		if (editBundle != null) {
			// if (string stored in the bundleextra get the cursor
			String comp = editBundle.get(WalletDb.C_ACC_NAME).toString();
			/*
			 * above code is not need..its not logic to compare the string beign
			 * passed with sting store coz the passed one is the stored one
			 * instead u can just get the cursor with the value
			 */

			Log.d(TAG, comp);

			MyWalletApplication.db = MyWalletApplication.walletDbHelper
					.getReadableDatabase(); // open db for writing
			try {
				Cursor cursor = MyWalletApplication.db.query(
						WalletDb.TABLE_ACCOUNT, WalletDb.accountColArray, null,
						null, null, null, null);

				// Cursor cursor =
				// MyWalletApplication.db.rawQuery("Select * from accounts",
				// null);
				// Log.d(TAG,cursor.getString(cursor.getColumnIndex(WalletDb.C_ACC_TYPE)));

				while (cursor.moveToNext()) {
					Log.d(TAG, (cursor.getString(cursor
							.getColumnIndex(WalletDb.C_ACC_NAME))));

					if (cursor.getString(
							cursor.getColumnIndex(WalletDb.C_ACC_NAME)).equals(
							comp)) {

						Log.d(TAG, "this is the raw");

						Log.d(TAG, (cursor.getString(cursor
								.getColumnIndex(WalletDb.C_OPENING_BALANCE))));
						Log.d(TAG, (cursor.getString(cursor
								.getColumnIndex(WalletDb.C_CURRENCY))));
						Log.d(TAG, (cursor.getString(cursor
								.getColumnIndex(WalletDb.C_DETAILS))));
						Log.d(TAG, (cursor.getString(cursor
								.getColumnIndex(WalletDb.C_MIN_BALANCE))));
						Log.d(TAG, (cursor.getString(cursor
								.getColumnIndex(WalletDb.C_ACC_TYPE))));

						// set ui here
						btnAdd.setText(R.string.saveChanges);
						edtxtAccName.setText(cursor.getString(cursor
								.getColumnIndex(WalletDb.C_ACC_NAME)));
						edtxtAccDetails.setText(cursor.getString(cursor
								.getColumnIndex(WalletDb.C_DETAILS)));
						edtxtOPBalance.setText(cursor.getString(cursor
								.getColumnIndex(WalletDb.C_OPENING_BALANCE)));
						edtxtMinBalance.setText(cursor.getString(cursor
								.getColumnIndex(WalletDb.C_MIN_BALANCE)));

						// set Spinners type and currenc
						Resources res = getResources();
						String acctypes[] = res.getStringArray(R.array.accType);
						String currency[] = res
								.getStringArray(R.array.currencyType);

						Log.d(TAG, String.valueOf(getArrayIndex(acctypes,
								cursor.getString(cursor
										.getColumnIndex(WalletDb.C_ACC_TYPE)))));

						spnAccType.setSelection(getArrayIndex(acctypes, cursor
								.getString(cursor
										.getColumnIndex(WalletDb.C_ACC_TYPE))));
						spnCurrency.setSelection(getArrayIndex(currency, cursor
								.getString(cursor
										.getColumnIndex(WalletDb.C_CURRENCY))));

					}

				}
				/*
				 * 
				 * if(cursor!= null){ //set ui here
				 * btnAdd.setText(R.string.saveChanges); //
				 * edtxtAccName.setText(
				 * cursor.getString(cursor.getColumnIndex(WalletDb
				 * .C_ACC_NAME)));
				 * 
				 * // Log.d(TAG,cursor.getString(cursor.getColumnIndex(WalletDb.
				 * C_ACC_TYPE))); Log.d(TAG,String.valueOf(cursor.getCount()));
				 * 
				 * Log.d(TAG,cursor.toString());
				 * 
				 * }
				 */

			} catch (SQLException e) {
				// TODO: handle exception
			}

			MyWalletApplication.db.close();// close after writing

		}

		intent = new Intent();

	}

	@Override
	public void onClick(View v) {

		if (v == btnAdd) {

			accountName = edtxtAccName.getText().toString();
			openingBalance = Integer.parseInt(edtxtOPBalance.getText()
					.toString());
			balance = openingBalance;
			minBalance = Integer.parseInt(edtxtMinBalance.getText().toString());

			accountDetails = edtxtAccDetails.getText().toString();

			Date date = new Date();
			openDate = date.getDate();

			if (balance < minBalance)
				Toast.makeText(this, "", Toast.LENGTH_SHORT).show();

			values = new ContentValues();

			values.put(WalletDb.C_ACC_NAME, accountName);
			values.put(WalletDb.C_BALANCE, balance);
			values.put(WalletDb.C_ACC_TYPE, accountType);
			values.put(WalletDb.C_DETAILS, accountDetails);
			values.put(WalletDb.C_MIN_BALANCE, minBalance);
			values.put(WalletDb.C_CURRENCY, currency);
			values.put(WalletDb.C_OPEN_DATE, openDate);
			values.put(WalletDb.C_OPENING_BALANCE, openingBalance);

			Log.d(TAG, values.toString());
			// writing to database

			MyWalletApplication.db = MyWalletApplication.walletDbHelper
					.getWritableDatabase(); // open db for writing

			if (editBundle == null) {
				// insert new account into db
				MyWalletApplication.db.insertOrThrow(WalletDb.TABLE_ACCOUNT,
						null, values);
				values.put("oldname", "");// this is to inform accounts activity
											// that its a new ui
											// string(accountname)
			} else {
				// update account here
				//some checks needs to be performed here ....its should be mentioned in the documentation as constrains
				
				 int i = MyWalletApplication.db.update(WalletDb.TABLE_ACCOUNT, values,WalletDb.C_ACC_NAME+" = ?" , new String [] { editBundle.get(WalletDb.C_ACC_NAME).toString()} );
				
				Log.d(TAG, "updated: "+i+" rows");
				
				values.put("oldname", editBundle.get(WalletDb.C_ACC_NAME)
						.toString());// this is for UI to remove old name and
										// repalce with new name
				// setResult(Activity.RESULT_CANCELED, intent);
			}

			MyWalletApplication.db.close();

			// sending the data back to calling activity
			Bundle b = new Bundle();
			b.putParcelable("val", values);

			intent.putExtras(b);

			setResult(Activity.RESULT_OK, intent);
			Log.d(TAG, b.getParcelable("val").toString());

			finish();
			// finally finish

		}

		if (v == btnCancel) {
			intent.putExtra("val", str);
			setResult(Activity.RESULT_CANCELED, intent);
			finish();
		}

	}

	@Override
	public void onItemSelected(AdapterView<?> list, View view, int position,
			long id) {
		switch (list.getId()) {

		case SPINNER_ACCTYPE:
			accountType = list.getItemAtPosition(position).toString();
			// Toast.makeText(this, list.getItemAtPosition(position).toString(),
			// Toast.LENGTH_SHORT).show();
			Log.d(TAG, accountType + ":accountype");
			break;
		case SPINNER_CURENCY:
			currency = list.getItemAtPosition(position).toString();
			// Toast.makeText(this, list.getItemAtPosition(position).toString(),
			// Toast.LENGTH_SHORT).show();
			Log.d(TAG, currency + "currency");

		default:
			break;
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// do nothing

	}

	private int getArrayIndex(String[] searchArr, String item) {

		int length = searchArr.length;
		for (int i = 0; i < length; i++) {
			// Log.d("InsideLoop", String.valueOf(i));
			if (searchArr[i].equals(item))
				return i;
		}
		return -1;
	}

}
