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
import android.widget.Toast;

public class AddTransactionActivity extends Activity implements
		OnItemSelectedListener, OnClickListener {

	private final int ACCOUNT_SPINNER = 0;
	private final int TYPE_SPINNER = 1;
	private final int STATUS_SPINNER = 2;

	private final int DIALOG_ID_DATE = 0;
	private final int DIALOG_ID_PAYEE= 1;

	private ArrayList<String> accountsArray;
	private Spinner spnAccounts;
	private Spinner spnType;
	private Spinner spnStatus;
	private Button btnAdd;
	private Button btnCancel;
	private EditText edtxtAmount;
	private EditText edtxtDate;
	private EditText edtxtDetails;
	private EditText edtxtPayee;
	private Button btnSetPayee;
	private Button btnSetDate;

	private int mDay;
	private int mMonth;
	private int mYear;

	ArrayList<String> payeelist;
	
	//this area defines variables for db input
	
	private String Trns_Account;
	private String Trns_Type;
	private String Trns_Status;
	private double Trns_Amount;
	private String Trns_Date;
	private String Trns_Payee;
	private String Trns_Details;
	private String Trns_Category;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.addnewtransaction);

		accountsArray = new ArrayList<String>();
		payeelist = new ArrayList<String>();
		payeelist.add("no payee yet");

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

		spnAccounts = (Spinner) findViewById(R.id.nTrsspnAccount);
		spnAccounts.setId(ACCOUNT_SPINNER);
		spnAccounts.setOnItemSelectedListener(this);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, accountsArray);
		spnAccounts.setAdapter(adapter);

		spnType = (Spinner) findViewById(R.id.nTrsspnType);
		spnType.setId(TYPE_SPINNER);
		spnType.setOnItemSelectedListener(this);

		spnStatus = (Spinner) findViewById(R.id.nTrsspnStatus);
		spnStatus.setId(STATUS_SPINNER);
		spnStatus.setOnItemSelectedListener(this);

		btnSetDate = (Button) findViewById(R.id.nTrsbtnDate);
		btnSetDate.setOnClickListener(this);
		
		btnSetPayee = (Button)findViewById(R.id.nTransbtnPayee);
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
		
		edtxtDate = (EditText)findViewById(R.id.nTrsspnEdtxtDate);
		edtxtPayee = (EditText)findViewById(R.id.nTrsEdtxtPayee);
		edtxtDetails = (EditText)findViewById(R.id.nTrsEdtxtDetails);
		edtxtAmount = (EditText)findViewById(R.id.nTrsspnEdtxtAmount);
		
		

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
		//do the initializations here 
	
		Trns_Type = spnType.getItemAtPosition(0).toString();
		Trns_Status = spnStatus.getItemAtPosition(0).toString();
		Trns_Account = spnAccounts.getItemAtPosition(0).toString();
		
		
		switch (parent.getId()) {
		case TYPE_SPINNER:
			Trns_Type = parent.getItemAtPosition(pos).toString();
			break;
		case STATUS_SPINNER:
			Trns_Status = parent.getItemAtPosition(pos).toString();
			break;

		case ACCOUNT_SPINNER:
			Trns_Account = parent.getItemAtPosition(pos).toString();
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
			/*
			 * get the payees list from db and settextbox if available else display an alert saying "no payee inthe local db"
			 * 
			 */
			
			//open the db here
												
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			
			builder.setSingleChoiceItems(payeelist.toArray(new String[payeelist.size()]), -1, mDialogListener);

			AlertDialog dlg = builder.create();
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
			
			
		}
	};

	@Override
	public void onClick(View v) {
		if (v == btnSetDate) {
			showDialog(DIALOG_ID_DATE);
		}
		
		if (v == btnSetPayee){
			showDialog(DIALOG_ID_PAYEE);
			Toast.makeText(this, "ooo",Toast.LENGTH_SHORT).show();
		}

		if (v == btnCancel)
			finish();
		
		if(v==btnAdd){
			writeToDB();
			finish();
		}
	}


private void setDateText() {
    edtxtDate.setText(
        new StringBuilder()
                // Month is 0 based so add 1 //this is with accordance to google
                .append(mMonth + 1).append("/")
                .append(mDay).append("/")
                .append(mYear).append(" "));
    
    Trns_Date = new Date(mYear, mMonth, mDay).toString();
    Log.d("DATE_TEST",Trns_Date.toString());
}


private void writeToDB(){
	
	Trns_Amount = Double.parseDouble(edtxtAmount.getText().toString());
	
//	Trns_Amount = edtxtAmount.getText().toString(); this is no longer required as now the amount data type is double
	Trns_Details = edtxtDetails.getText().toString();
	Trns_Payee = edtxtPayee.getText().toString();
	
	
	ContentValues values = new ContentValues();
	
	values.put(WalletDb.TR_ACCOUNT, Trns_Account);
	values.put(WalletDb.TR_AMOUNT, Trns_Amount);
	values.put(WalletDb.TR_DATE, Trns_Date);
	values.put(WalletDb.TR_DETAILS,Trns_Details);
	values.put(WalletDb.TR_PAYEE, Trns_Payee);
	values.put(WalletDb.TR_TYPE, Trns_Type);
	values.put(WalletDb.TR_STATUS, Trns_Status);

	
	
	MyWalletApplication.db = MyWalletApplication.walletDbHelper.getWritableDatabase();
	try {
		double newBalance, storedBalance;
		
		MyWalletApplication.db.insertOrThrow(WalletDb.TABLE_TRANSACTION, null, values);
		//query the  account table to get the balance
		//recalculate the balance
		//update the account table
//		Cursor cr = MyWalletApplication.db.query(WalletDb.TABLE_ACCOUNT, new String[] {WalletDb.C_BALANCE},Trns_Account , null, null, null, null);
		
		String sql = "Select * from "+WalletDb.TABLE_ACCOUNT+ " where "+WalletDb.C_ACC_NAME+" =?";
		
//		Log.d("Error",sql);
	
		Cursor cr = MyWalletApplication.db.rawQuery(sql, new String [] {Trns_Account});
		
//		Log.d("EROOR", cr.toString());
		
		if(cr.moveToFirst()){
		
//			Log.d("do nothing here", cr.getString(cr.getColumnIndex(WalletDb.C_BALANCE)));
			
			storedBalance = Double.parseDouble(cr.getString(cr.getColumnIndex(WalletDb.C_BALANCE)));
		
			if(storedBalance>Trns_Amount){
				newBalance = storedBalance - Trns_Amount;
				ContentValues value = new ContentValues();
				value.put(WalletDb.C_BALANCE, newBalance);
				MyWalletApplication.db.update(WalletDb.TABLE_ACCOUNT, value, WalletDb.C_ACC_NAME + " = ?", new String [] {Trns_Account});
			
//				 Cursor cc = MyWalletApplication.db.rawQuery(sql, new String [] {Trns_Account});
//				 if(cr.moveToFirst())
//					 Log.d("do nothing here", cc.getString(cc.getColumnIndex(WalletDb.C_BALANCE)));
//				Log.d("insert status", String.valueOf(i));
					 
			}
				
		}
		
		
	} catch (SQLException e) {
		
		/*Log.d("ERROR","can't write to db transaction table with the error:"+ e.toString());
		Log.d("ERROR",WalletDb.CREATE_TRANSACTION);
		Log.d("Error", WalletDb.CREATE_CURRENCY);
		Log.d("Error",WalletDb.CREATE_TABLE_ACCOUNT);
		*/
		Log.d("Error",values.toString());
	}

	
	MyWalletApplication.db.close();

}




}