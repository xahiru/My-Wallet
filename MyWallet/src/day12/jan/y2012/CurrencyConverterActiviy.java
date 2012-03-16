package day12.jan.y2012;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CurrencyConverterActiviy extends Activity implements
		OnItemSelectedListener {
	//rev 3

	private static final String TAG = CurrencyConverterActiviy.class
			.getSimpleName();

	
	
	Spinner spnCurrencySelector;
	Spinner spnToCurrency;
	TextView txtResult;
	EditText edttxtCurrencyInput;
	ListView lvCurrencyList;
	Dialog inputDlg;
	// today's at start No.line= 117

	Context mContext;

	private static final int ADD_CURRENCY_DGL_ID = 0;
	
	private static final int SPN_FROM_CURRENCY = 0;
	private static final int SPN_TO_CURRENCY=1;

	private final int MENU_ADD_CURRENCY = Menu.FIRST;
	private final int MENU_DEL_CURRENCY = Menu.FIRST + 1;
	private final int MEN_UPDATE_CURRENCY = Menu.FIRST + 2;

	//private final int DOLLAR_RATE = 15;
	//private final int RUFIYA_RATE = 1;
	
	private double rateOne;
	private double rateTwo;
	
	
//	int position1=1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		ArrayList<String> currencyArray = new ArrayList<String>();
		ArrayList<HashMap<String, String>> currencyList = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> hashMap;

		setContentView(R.layout.currencyconvertor);
		
		
		
		

		MyWalletApplication.db = MyWalletApplication.walletDbHelper
				.getWritableDatabase(); // open db for writing ....actually
										// writing is not neccesary here
		try {
			Cursor cursor = MyWalletApplication.db
					.query(WalletDb.TABLE_CURRENCY, null, null, null, null,
							null, null);

			while (cursor.moveToNext()) {
				currencyArray.add(cursor.getString(cursor
						.getColumnIndex(WalletDb.CURRENCY_NAME)));
				String crncyName = cursor.getString(cursor
						.getColumnIndex(WalletDb.CURRENCY_NAME));
				String crncySymbol = cursor.getString(cursor
						.getColumnIndex(WalletDb.CURRENCY_SYMBOL));
				String crncyRate = cursor.getString(cursor
						.getColumnIndex(WalletDb.CURRENCY_RATE));
				
				hashMap = new HashMap<String, String>();
				
				hashMap.put(WalletDb.CURRENCY_NAME, crncyName);
				hashMap.put(WalletDb.CURRENCY_RATE, crncyRate);
				hashMap.put(WalletDb.CURRENCY_SYMBOL, crncySymbol);
				currencyList.add(hashMap);
				

			}
		} catch (SQLException e) {
			// TODO: handle exception
		}

		spnCurrencySelector = (Spinner) findViewById(R.id.spnCurrencySelector);
		spnCurrencySelector.setId(SPN_FROM_CURRENCY);
		spnCurrencySelector.setOnItemSelectedListener(this);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, currencyArray);
		
		
		lvCurrencyList = (ListView)findViewById(R.id.lvCurrency);
		
		SimpleAdapter adapter2 = new SimpleAdapter(this, currencyList,
				R.layout.transactionrecords,
				new String[] { WalletDb.CURRENCY_NAME, WalletDb.CURRENCY_SYMBOL, WalletDb.CURRENCY_RATE }, new int[] {
						R.id.textRow1, R.id.textRow2, R.id.textRow3 });
		lvCurrencyList.setAdapter(adapter2);
			
		spnToCurrency = (Spinner)findViewById(R.id.spnToCurrency);
		spnToCurrency.setId(SPN_TO_CURRENCY);
		spnToCurrency.setOnItemSelectedListener(this);
		
		
		
		if (!currencyArray.isEmpty()) {
			Log.d(TAG, currencyArray.toString());
			spnCurrencySelector.setAdapter(adapter);
			spnToCurrency.setAdapter(adapter);
		}

		txtResult = (TextView) findViewById(R.id.txtcurrencyResult);
		edttxtCurrencyInput = (EditText) findViewById(R.id.edttxtCurrencyInput);

		mContext = getApplicationContext();

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		
		
		double result = 0;
		String currencySelected = parent.getItemAtPosition(position).toString();
		double  input = Double.parseDouble(edttxtCurrencyInput.getText().toString());
		
		Log.d(TAG, "Value entered :"+ String.valueOf(input));
		
		Log.d(TAG, "currencySelected :"+ currencySelected);
		
		
		
		switch(parent.getId()){
		case SPN_FROM_CURRENCY:
			
//			position1 = position;
			
			rateOne = readRateFromDB(position);
			
			
//			Log.d(TAG, "rateOne :"+ String.valueOf(rateOne));
			Log.d(TAG, "Result :"+ String.valueOf(result));
			
			
			break;
		case SPN_TO_CURRENCY:
			
			
				rateTwo = readRateFromDB(position);
			
		
			
			Log.d(TAG, "RateTwo :"+ String.valueOf(rateTwo));
//			Log.d(TAG, "Position Two:"+ String.valueOf(position));
			
//			Log.d(TAG, "Result :"+ String.valueOf(result));
//			result = 1;
			
			break;
			
			default:
			break;
		
			
		}
		
		if(rateOne != 0 && rateTwo !=0){
		result = input*rateOne;
		result /= rateTwo;
		
		txtResult.setText(String.valueOf(result));
		}
		else
		{
			txtResult.setText(String.valueOf(result));//set error message here
		}
		
		/*		
		switch (position) {
		case 0:
			int result;
			result = Integer.parseInt(edttxtCurrencyInput.getText().toString());
			result *= DOLLAR_RATE;
			txtResult.setText(String.valueOf(result));
			break;
		case 1:
			// no calcution is required as converting to MRF is 1
			txtResult.setText(edttxtCurrencyInput.getText().toString());
			break;

		default:
			break;
		}
*/
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// do nothing

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		super.onCreateOptionsMenu(menu);

		menu.add(0, MENU_ADD_CURRENCY, 0, R.string.add);
		menu.add(0, MENU_DEL_CURRENCY, 0, R.string.delete);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_ADD_CURRENCY:
			// create dialog here
			// AlertDialog dlgt = new AlertDialog(null);

			inputDlg = new Dialog(this);
			showDialog(ADD_CURRENCY_DGL_ID);

			break;

		case MENU_DEL_CURRENCY:
			/*
			 * create a delete case for delete wherecurrency list will be shown
			 * and user can select the currency to be deleteda pop will be shown
			 * to confirm the delete
			 */
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dlg;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		switch (id) {
		case ADD_CURRENCY_DGL_ID:

			LayoutInflater inflator = LayoutInflater.from(mContext);
			View layout = inflator.inflate(R.layout.addnewcurrency, null);

			// its much better to inflate them using xml
			builder.setMessage("Add currency dialog");

			final EditText edttxtCurrencyTxt = (EditText) layout
					.findViewById(R.id.edttxtCurrencyName);
			// edttxtCurrencyTxt.setHint(R.string.currencyname);
			// builder.setView(edttxtCurrencyTxt);

			final EditText edttxtRate = (EditText) layout
					.findViewById(R.id.edttxtRate);
			// edttxtRate.setHint(R.string.rate);
			// builder.setView(edttxtRate);
			//

			final EditText edttxtSymbol = (EditText) layout
					.findViewById(R.id.edttxtSimbol);
			// edttxtSymbol.setText ( "78" + (char) 0x00B0 );

			final EditText edttxtURL = (EditText) layout
					.findViewById(R.id.edttxtServerURL);
			// edttxtURL.setHint(R.string.serverURL);
			// builder.setView(edttxtURL);

			builder.setView(layout);

			builder.setCancelable(true);
			builder.setPositiveButton("ok", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					String CurrencyName;
					double exchangeRate;
					String updateURL, symbol;
					ContentValues content = new ContentValues();

					CurrencyName = edttxtCurrencyTxt.getText().toString();
					exchangeRate = Double.valueOf(edttxtRate.getText()
							.toString());
					symbol = edttxtSymbol.getText().toString();
					updateURL = edttxtURL.getText().toString();
					
					
					content.put(WalletDb.CURRENCY_NAME, CurrencyName);
					content.put(WalletDb.CURRENCY_RATE, exchangeRate);
					content.put(WalletDb.CURRENCY_SYMBOL, symbol);
					content.put(WalletDb.CURRENCY_UDATE_URL, updateURL);
					/*
					 * add currency to the database
					 */
					writetoDB(content);
					Toast.makeText(getApplicationContext(), CurrencyName,
							Toast.LENGTH_SHORT).show();
					
					onCreate(null); // refereshes UI

				}
			});
			dlg = builder.create();

			break;

		default:
			/*
			 * this part is only for testing purpose make sure to nullify the
			 * dlg object after testting
			 */

			builder.setMessage("test");
			builder.setCancelable(true);
			builder.setPositiveButton("ok", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dismissDialog(ADD_CURRENCY_DGL_ID);

				}
			});
			dlg = builder.create();

			// dlg = null;
			break;
		}

		return dlg;
	}

	public static int writetoDB(ContentValues cv) {
		
		MyWalletApplication.db = MyWalletApplication.walletDbHelper
				.getWritableDatabase();

		MyWalletApplication.db.insert(WalletDb.TABLE_CURRENCY, null, cv);

		MyWalletApplication.db.close();

	
		return 0;
	}
	
	private static double readRateFromDB(int selection){
		MyWalletApplication.db = MyWalletApplication.walletDbHelper
		.getReadableDatabase();
		
		double	exchangeRate =0; //if no record is found zero is returned
		String Selection = String.valueOf(selection+1);//one is added to match list view positon to that of autoincrement in db id field
		
		try {
			Cursor cursor = MyWalletApplication.db.query(
					WalletDb.TABLE_CURRENCY, null, WalletDb.CURRENCY_ID+"=?", new String []{Selection}, null, null, null);
			
			
			while (cursor.moveToNext()) {
				exchangeRate = cursor.getDouble(cursor.getColumnIndex(WalletDb.CURRENCY_RATE));
			
				Log.d(TAG,"exchangerate = "+String.valueOf(exchangeRate));
				Log.d(TAG,"Selection = "+Selection);
				
			}
			
			
		} catch (SQLException e) {
			// TODO: handle exception
		}
		
		
		Log.d(TAG,"selection(StringValue) = "+Selection);
		Log.d(TAG,"exchangerate to be returned = "+String.valueOf(exchangeRate));
		
		return exchangeRate;
	}

}
