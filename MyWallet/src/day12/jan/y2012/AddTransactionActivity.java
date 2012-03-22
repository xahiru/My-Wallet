package day12.jan.y2012;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
//import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddTransactionActivity extends Activity implements
		OnItemSelectedListener, OnClickListener {

	// rev 3

	private static final String TAG = AddTransactionActivity.class
			.getSimpleName();
	private static final int TRANSFER_TO_ACCOUNT = 3;
	private final int ACCOUNT_SPINNER = 0;
	private final int TYPE_SPINNER = 1;
	private final int STATUS_SPINNER = 2;

	private final int DIALOG_ID_DATE = 0;
	private final int DIALOG_ID_PAYEE = 1;

	private ArrayList<String> accountsArray;
	private Spinner spnAccounts;
	private Spinner spnType;
	private Spinner spnStatus;
	private Spinner spnTransferTo;
	private Button btnAdd;
	private Button btnCancel;
	private EditText edtxtAmount;
	private EditText edtxtDate;
	private EditText edtxtDetails;
	private EditText edtxtPayee;
	private Button btnSetPayee;
	private Button btnSetDate;

	private AlertDialog alert = null;
	private TextView txtToAccount;

	private int mDay;
	private int mMonth;
	private int mYear;

	ArrayList<String> payeelist;

	// this area defines variables for db input

	private String Trns_Account;
	private String Trns_Type;
	private String Trns_Status;
	private double Trns_Amount;
	private String Trns_Date;
	private String Trns_Payee;
	private String Trns_Details;
	private String Trns_Category;
	private String Trns_ToAccount;

	private String Old_Trns_Account;
	private String Old_Trns_Type = "";
	private String Old_Trns_Status = "";
	private double Old_Trns_Amount;
	private String Old_Trns_Date;
	private String Old_Trns_Payee;
	private String Old_Trns_Details;
	private String Old_Trns_Category;
	private String Old_Trns_ToAccount;

	private Bundle editBundle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.addnewtransaction);

		editBundle = getIntent().getExtras();

		if (editBundle != null) {
			Log.d(TAG, "At start bundle value" + editBundle.getString("ListID"));
		} else {
			Log.d(TAG, "nothing passed");
		}

		accountsArray = new ArrayList<String>();
		payeelist = new ArrayList<String>();
		payeelist.add("no payee yet");

		Log.d(TAG, payeelist.toString());

		MyWalletApplication.db = MyWalletApplication.walletDbHelper
				.getWritableDatabase(); // open db for writing ....actually
										// writing is not neccesary here
		try {
			Cursor cursor = MyWalletApplication.db.query(
					WalletDb.TABLE_ACCOUNT, null, null, null, null, null, null);

			if (editBundle != null) {
				Cursor editCursor = MyWalletApplication.db.query(
						WalletDb.TABLE_TRANSACTION, null,
						WalletDb.TRANSACTION_ID + " = ?",
						new String[] { editBundle.getString("ListID") }, null,
						null, null);

				// now it has changed no need to increment list id as primary
				// keyis being used

				// /*
				// * 1 is added to listID passed through bundle, in the above
				// code
				// * in order to match with the rawid of the transactionr table
				// as it is set to autoincrement
				// */

				while (editCursor.moveToNext()) {
					Log.d(TAG, String.valueOf((editCursor.getInt(editCursor
							.getColumnIndex(WalletDb.TRANSACTION_ID)))));

					Old_Trns_Amount = editCursor.getDouble(editCursor
							.getColumnIndex(WalletDb.TR_AMOUNT));
					Old_Trns_Date = editCursor.getString(editCursor
							.getColumnIndex(WalletDb.TR_DATE));
					Old_Trns_Payee = editCursor.getString(editCursor
							.getColumnIndex(WalletDb.TR_PAYEE));
					Old_Trns_Details = editCursor.getString(editCursor
							.getColumnIndex(WalletDb.TR_DETAILS));
					Old_Trns_Account = editCursor.getString(editCursor
							.getColumnIndex(WalletDb.TR_ACCOUNT));
					Old_Trns_Status = editCursor.getString(editCursor
							.getColumnIndex(WalletDb.TR_STATUS));
					Old_Trns_Type = editCursor.getString(editCursor
							.getColumnIndex(WalletDb.TR_TYPE));
					 Old_Trns_ToAccount =
					 editCursor.getString(editCursor.getColumnIndex(WalletDb.TR_TO_ACCOUNT));

				}

			}
			while (cursor.moveToNext()) {
				accountsArray.add(cursor.getString(cursor
						.getColumnIndex(WalletDb.C_ACC_NAME)));

			}

		} catch (SQLException e) {

		}

		int position = 0;
		if (Old_Trns_Type.equals("Transfer")) {
			position = 2;
		} else if (Old_Trns_Type.equals("Income")) {
			position = 0;
		} else {

			position = 1;
		}

		spnAccounts = (Spinner) findViewById(R.id.nTrsspnAccount);
		spnAccounts.setId(ACCOUNT_SPINNER);
		spnAccounts.setOnItemSelectedListener(this);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, accountsArray);
		spnAccounts.setAdapter(adapter);

		spnType = (Spinner) findViewById(R.id.nTrsspnType);
		spnType.setId(TYPE_SPINNER);
		spnType.setSelection(position);
		spnType.setOnItemSelectedListener(this);

		position = 1;
		if (Old_Trns_Status.equals("Cleared")) {
			position = 2;
		} else if (Old_Trns_Status.equals("Reconciled")) {
			position = 0;
		} else {

			position = 1;
		}

		spnStatus = (Spinner) findViewById(R.id.nTrsspnStatus);
		spnStatus.setId(STATUS_SPINNER);
		spnStatus.setSelection(position);
		spnStatus.setOnItemSelectedListener(this);

		spnTransferTo = (Spinner) findViewById(R.id.spnTransferToAccount);

		spnTransferTo.setId(TRANSFER_TO_ACCOUNT);
		spnTransferTo.setOnItemSelectedListener(this);
		spnTransferTo.setAdapter(adapter);
		spnTransferTo.setVisibility(View.INVISIBLE);

		txtToAccount = (TextView) findViewById(R.id.txttoAccount);
		txtToAccount.setVisibility(View.INVISIBLE);

		btnSetDate = (Button) findViewById(R.id.nTrsbtnDate);
		btnSetDate.setOnClickListener(this);

		btnSetPayee = (Button) findViewById(R.id.nTransbtnPayee);
		btnSetPayee.setOnClickListener(this);

		btnAdd = (Button) findViewById(R.id.nTrnsBtnAdd);
		btnAdd.setOnClickListener(this);

		btnCancel = (Button) findViewById(R.id.nTrnsBtnCancel);
		btnCancel.setOnClickListener(this);

		// if date variables are not initialized, the system crashes cox year
		// and day are equal
		final Calendar calendar = Calendar.getInstance();
		mYear = calendar.get(Calendar.YEAR);
		mMonth = calendar.get(Calendar.MONTH);
		mDay = calendar.get(Calendar.DAY_OF_MONTH);

		edtxtDate = (EditText) findViewById(R.id.nTrsspnEdtxtDate);
		edtxtPayee = (EditText) findViewById(R.id.nTrsEdtxtPayee);
		edtxtDetails = (EditText) findViewById(R.id.nTrsEdtxtDetails);
		edtxtAmount = (EditText) findViewById(R.id.nTrsspnEdtxtAmount);

		if (editBundle != null) {
			edtxtDate.setText(Old_Trns_Date);
			edtxtPayee.setText(Old_Trns_Payee);
			edtxtDetails.setText(Old_Trns_Details);
			edtxtAmount.setText(String.valueOf(Old_Trns_Amount));
			
			spnAccounts.setSelection(accountsArray.indexOf(Old_Trns_Account));
						
			
			btnAdd.setText(R.string.save);

		}

	}

	private DatePickerDialog.OnDateSetListener mSetDareCallback = new OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			setDateText();
		}

	};

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		// do the initializations here// remove the initialization otherwise
		// default is taken

		// Trns_Type = spnType.getItemAtPosition(0).toString();
		// Trns_Status = spnStatus.getItemAtPosition(0).toString();
		// Trns_Account = spnAccounts.getItemAtPosition(0).toString();

		switch (parent.getId()) {
		case TYPE_SPINNER:
			Trns_Type = parent.getItemAtPosition(pos).toString();

			if (Trns_Type.equals("Transfer")) {
				spnTransferTo.setVisibility(View.VISIBLE);
				spnTransferTo.setSelection(accountsArray.indexOf(Old_Trns_ToAccount)); //
				txtToAccount.setVisibility(View.VISIBLE);
				edtxtPayee.setVisibility(View.INVISIBLE);
				btnSetPayee.setVisibility(View.INVISIBLE);

			} else {
				spnTransferTo.setVisibility(View.INVISIBLE);
				txtToAccount.setVisibility(View.INVISIBLE);
				edtxtPayee.setVisibility(View.VISIBLE);
				btnSetPayee.setVisibility(View.VISIBLE);
			}
			break;
		case STATUS_SPINNER:
			Trns_Status = parent.getItemAtPosition(pos).toString();
			break;

		case ACCOUNT_SPINNER:
			Trns_Account = parent.getItemAtPosition(pos).toString();
			break;
		case TRANSFER_TO_ACCOUNT:
			Trns_ToAccount = parent.getItemAtPosition(pos).toString();

			break;

		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_ID_DATE:

			return new DatePickerDialog(this, mSetDareCallback, mYear, mMonth,
					mDay);

		case DIALOG_ID_PAYEE:
			payeelist.clear();
			MyWalletApplication.db = MyWalletApplication.walletDbHelper
					.getReadableDatabase();

			try {
				Cursor cr = MyWalletApplication.db.query(WalletDb.TABLE_PAYEE,
						new String[] { WalletDb.PAYEE_NAME }, null, null, null,
						null, null);
				while (cr.moveToNext()) {
					payeelist.add(cr.getString(cr
							.getColumnIndex(WalletDb.PAYEE_NAME)));
				}
			} catch (SQLException e) {
				// TODO: handle exception
			}

			AlertDialog.Builder builder = new AlertDialog.Builder(this);

			builder.setSingleChoiceItems(
					payeelist.toArray(new String[payeelist.size()]), -1,
					mDialogListener);

			AlertDialog dlg = builder.create();

			alert = dlg;

			dlg.show();

		default:
			break;
		}

		return null;
	}

	private DialogInterface.OnClickListener mDialogListener = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			edtxtPayee.setText(payeelist.get(which));
			alert.dismiss();

		}

	};

	@Override
	public void onClick(View v) {
		if (v == btnSetDate) {
			showDialog(DIALOG_ID_DATE);
		}

		if (v == btnSetPayee) {
			showDialog(DIALOG_ID_PAYEE);
			Toast.makeText(this, "ooo", Toast.LENGTH_SHORT).show();

		}

		if (v == btnCancel)
			finish();

		if (v == btnAdd) {
			if (!(Trns_Account.equals(Trns_ToAccount) && Trns_Type
					.equals("Transfer"))) {

				if (editBundle != null) {
					Log.d(TAG, "hello");
					revertTransaction();
				}
				writeToDB();
				finish();
			} else
				Toast.makeText(this, "Cannot transfer to the same account",
						Toast.LENGTH_LONG).show();

		}
	}

	private void setDateText() {
		edtxtDate.setText(new StringBuilder()
				// Month is 0 based so add 1 //this is with accordance to google
				.append(mMonth + 1).append("/").append(mDay).append("/")
				.append(mYear).append(" "));

		Trns_Date = new Date(mYear, mMonth, mDay).toString();
		Log.d("DATE_TEST", Trns_Date.toString());
	}

	private void writeToDB() {

		Trns_Amount = Double.parseDouble(edtxtAmount.getText().toString());
		// Trns_Amount = edtxtAmount.getText().toString(); this is no longer
		// required as now the amount data type is double
		/*
		 * if the transaction type an expense store a negative value
		 */

		Trns_Details = edtxtDetails.getText().toString();
		Trns_Payee = edtxtPayee.getText().toString();

		ContentValues values = new ContentValues();

		values.put(WalletDb.TR_ACCOUNT, Trns_Account);
		values.put(WalletDb.TR_AMOUNT, Trns_Amount);
		values.put(WalletDb.TR_DATE, Trns_Date);
		values.put(WalletDb.TR_DETAILS, Trns_Details);
		values.put(WalletDb.TR_PAYEE, Trns_Payee);
		values.put(WalletDb.TR_TYPE, Trns_Type);
		values.put(WalletDb.TR_STATUS, Trns_Status);
		values.put(WalletDb.TR_TO_ACCOUNT, Trns_ToAccount);

		MyWalletApplication.db = MyWalletApplication.walletDbHelper
				.getWritableDatabase();
		try {
			double newBalance, storedBalance;
			double newBalanceForTransAcc = 0, storedBalanceForTrnsAcc = 0;

			if(editBundle!=null){
				
				MyWalletApplication.db.update(WalletDb.TABLE_TRANSACTION, values, 	WalletDb.TRANSACTION_ID + " = ?",
						new String[] {editBundle.getString("ListID") });
				
			}else{
				
			
			MyWalletApplication.db.insertOrThrow(WalletDb.TABLE_TRANSACTION,
					null, values);
			
			}
			// query the account table to get the balance
			// recalculate the balance
			// update the account table
			// Cursor cr = MyWalletApplication.db.query(WalletDb.TABLE_ACCOUNT,
			// new String[] {WalletDb.C_BALANCE},Trns_Account , null, null,
			// null, null);

			String sql = "Select * from " + WalletDb.TABLE_ACCOUNT + " where "
					+ WalletDb.C_ACC_NAME + " =?";

			Log.d("TransAccount", Trns_Account);

			Cursor cr = MyWalletApplication.db.rawQuery(sql,
					new String[] { Trns_Account });

			Cursor cr2 = MyWalletApplication.db.rawQuery(sql,
					new String[] { Trns_ToAccount });

			if (cr2.moveToNext()) {

				storedBalanceForTrnsAcc = Double.parseDouble(cr2.getString(cr2
						.getColumnIndex(WalletDb.C_BALANCE)));
			}

			// Log.d("EROOR", cr.toString());

			if (cr.moveToFirst()) {

				// Log.d("do nothing here",
				// cr.getString(cr.getColumnIndex(WalletDb.C_BALANCE)));

				storedBalance = Double.parseDouble(cr.getString(cr
						.getColumnIndex(WalletDb.C_BALANCE)));

				if (!(Trns_Type.equals("Income"))) {

					if (storedBalance > Trns_Amount) {
						newBalance = storedBalance - Trns_Amount;
						if (Trns_Type.equals("Transfer")) {

							newBalanceForTransAcc = Trns_Amount
									+ storedBalanceForTrnsAcc;

						}

						Log.d(TAG,
								"case 1 :" + Trns_Type + " "
										+ String.valueOf(newBalance));

					} else {
						newBalance = storedBalance;
						Log.d(TAG,
								"case 3 no balance:"
										+ String.valueOf(newBalance));
						Toast.makeText(this, "Insufficient fund",
								Toast.LENGTH_SHORT).show();
					}

				} else {

					newBalance = storedBalance + Trns_Amount;
					Log.d(TAG,
							"case 2 :" + Trns_Type + " "
									+ String.valueOf(newBalance));
				}

				ContentValues value = new ContentValues();
				value.put(WalletDb.C_BALANCE, newBalance);
				MyWalletApplication.db.update(WalletDb.TABLE_ACCOUNT, value,
						WalletDb.C_ACC_NAME + " = ?",
						new String[] { Trns_Account });

				if (Trns_Type.equals("Transfer")) {

					value.put(WalletDb.C_BALANCE, newBalanceForTransAcc);
					MyWalletApplication.db.update(WalletDb.TABLE_ACCOUNT,
							value, WalletDb.C_ACC_NAME + " = ?",
							new String[] { Trns_ToAccount });
				}

				// Cursor cc = MyWalletApplication.db.rawQuery(sql, new
				// String [] {Trns_Account});
				// if(cr.moveToFirst())
				// Log.d("do nothing here",
				// cc.getString(cc.getColumnIndex(WalletDb.C_BALANCE)));
				// Log.d("insert status", String.valueOf(i));

				Toast.makeText(this,
						"New Balance: " + String.valueOf(newBalance),
						Toast.LENGTH_SHORT).show();

			}

		}

		catch (SQLException e) {

			/*
			 * Log.d("ERROR","can't write to db transaction table with the error:"
			 * + e.toString()); Log.d("ERROR",WalletDb.CREATE_TRANSACTION);
			 * Log.d("Error", WalletDb.CREATE_CURRENCY);
			 * Log.d("Error",WalletDb.CREATE_TABLE_ACCOUNT);
			 */
			Log.d("Error", values.toString());
		}

		MyWalletApplication.db.close();

	}

	void revertTransaction() {

		MyWalletApplication.db = MyWalletApplication.walletDbHelper
				.getWritableDatabase();

		double Old_storedBalance = 0;
		double Old_storedBalanceForToAccnt = 0;

		double newBalanceForTransAcc = 0;
		double newBalanceForToTransAcc = 0;

		ContentValues value = new ContentValues();

		String sql = "Select * from " + WalletDb.TABLE_ACCOUNT + " where "
				+ WalletDb.C_ACC_NAME + " =?";

		Log.d(TAG, sql);

		Cursor crs = MyWalletApplication.db.rawQuery(sql,
				new String[] { Old_Trns_Account });

		if (Old_Trns_Type.equals("Transfer")) {

			Cursor crs2 = MyWalletApplication.db.rawQuery(sql,
					new String[] { Old_Trns_ToAccount });
			Log.d(TAG, crs2.toString());

			if (crs2.moveToNext()) {

				Old_storedBalanceForToAccnt = Double.parseDouble(crs2
						.getString(crs2.getColumnIndex(WalletDb.C_BALANCE)));
			}
		}

		Log.d(TAG, crs.toString());

		// Log.d("EROOR", cr.toString());

		if (crs.moveToNext()) {

//			 Log.d("do nothing here",
//			 crs.getString(crs.getColumnIndex(WalletDb.C_BALANCE)));

			Old_storedBalance = Double.parseDouble(crs.getString(crs
					.getColumnIndex(WalletDb.C_BALANCE)));

		}

		
		
		if (Old_Trns_Type.equals("Income")) {
			

			newBalanceForTransAcc = Old_storedBalance - Old_Trns_Amount;
			
			Log.d(TAG,String.valueOf(newBalanceForTransAcc)+": 1");

		} else if (Old_Trns_Type.equals("Expense")) {

			newBalanceForTransAcc = Old_storedBalance + Old_Trns_Amount;
			Log.d(TAG,String.valueOf(newBalanceForTransAcc)+": 2");
			
		} else  if (Old_Trns_Type.equals("Transfer")){

			newBalanceForTransAcc = Old_storedBalance + Old_Trns_Amount;
			newBalanceForToTransAcc = Old_storedBalanceForToAccnt
					- Old_Trns_Amount;
			
			Log.d(TAG,String.valueOf(newBalanceForTransAcc)+": 3");

			value.put(WalletDb.C_BALANCE, newBalanceForToTransAcc);
			MyWalletApplication.db.update(WalletDb.TABLE_ACCOUNT, value,
					WalletDb.C_ACC_NAME + " = ?",
					new String[] { Old_Trns_ToAccount });

		}

		

		Log.d(TAG, Old_Trns_Type+"::  here here here"+ String.valueOf(newBalanceForTransAcc));
		
		value.put(WalletDb.C_BALANCE, newBalanceForTransAcc);
		MyWalletApplication.db
				.update(WalletDb.TABLE_ACCOUNT, value, WalletDb.C_ACC_NAME
						+ " = ?", new String[] { Old_Trns_Account });

		MyWalletApplication.db.close();
	}

}