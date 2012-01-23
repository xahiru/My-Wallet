package day12.jan.y2012;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
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
		
		Bundle editBundle = getIntent().getExtras();
		
		if(editBundle != null ){
			//if (string  stored in the bundleextra get the cursor
			 String comp = editBundle.get(WalletDb.C_ACC_NAME).toString();
								 
			 Log.d(TAG,comp);
			 
			 
			 MyWalletApplication.db = MyWalletApplication.walletDbHelper
				.getReadableDatabase(); // open db for writing
		try {
			Cursor cursor = MyWalletApplication.db.query(
					WalletDb.TABLE_ACCOUNT, null, null, null, null, null, null);

//			Log.d(TAG,cursor.getString(cursor.getColumnIndex(WalletDb.C_ACC_TYPE)));
			if(cursor!= null){
				//set ui here
				btnAdd.setText(R.string.edit);
				
			}
			
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
			MyWalletApplication.db.insertOrThrow(WalletDb.TABLE_ACCOUNT, null,
					values);

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

}
