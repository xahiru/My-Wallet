package day12.jan.y2012;

import java.util.ArrayList;

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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CurrencyConverterActiviy extends Activity implements
		OnItemSelectedListener {

	private static final String TAG = CurrencyConverterActiviy.class.getSimpleName();
	
	Spinner spnCurrencySelector;
	TextView txtResult;
	EditText edttxtCurrencyInput;
	Dialog inputDlg;
	// today's at start No.line= 117

	Context mContext;

	private static final int ADD_CURRENCY_DGL_ID = 0;
	

	private final int MENU_ADD_CURRENCY = Menu.FIRST;
	private final int MENU_DEL_CURRENCY = Menu.FIRST + 1;
	private final int MEN_UPDATE_CURRENCY = Menu.FIRST + 2;

	private final int DOLLAR_RATE = 15;
	private final int RUFIYA_RATE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		ArrayList<String> currencyArray = new ArrayList<String>();

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

			}
		} catch (SQLException e) {
			// TODO: handle exception
		}

		spnCurrencySelector = (Spinner) findViewById(R.id.spnCurrencySelector);
		spnCurrencySelector.setOnItemSelectedListener(this);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, currencyArray);
		
		if (!currencyArray.isEmpty()) {
			Log.d(TAG,currencyArray.toString());
			spnCurrencySelector.setAdapter(adapter);
		}

		txtResult = (TextView) findViewById(R.id.txtcurrencyResult);
		edttxtCurrencyInput = (EditText) findViewById(R.id.edttxtCurrencyInput);

		mContext = getApplicationContext();

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
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
					String updateURL;
					ContentValues content = new ContentValues();

					CurrencyName = edttxtCurrencyTxt.getText().toString();
					exchangeRate = Double.valueOf(edttxtRate.getText()
							.toString());
					updateURL = edttxtURL.getText().toString();

					content.put(WalletDb.CURRENCY_NAME, CurrencyName);
					content.put(WalletDb.CURRENCY_RATE, exchangeRate);
					content.put(WalletDb.CURRENCY_UDATE_URL, updateURL);
					/*
					 * add currency to the database
					 */

					MyWalletApplication.db = MyWalletApplication.walletDbHelper
							.getWritableDatabase();

					MyWalletApplication.db.insert(WalletDb.TABLE_CURRENCY,
							null, content);

					MyWalletApplication.db.close();

					Toast.makeText(getApplicationContext(), CurrencyName,
							Toast.LENGTH_SHORT).show();
					
					onCreate(null); //refereshes UI

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

}
