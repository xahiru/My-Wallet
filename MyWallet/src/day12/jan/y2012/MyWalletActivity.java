package day12.jan.y2012;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MyWalletActivity extends Activity implements OnClickListener {
	private static final int SETTINGS = Menu.FIRST;
	private static final int SEARCH = Menu.FIRST + 1;
	private static final int CURRENCY_CONVERTER = Menu.FIRST +2;
	private static final String TAG = MyWalletActivity.class.getSimpleName();
	// private MyWalletApplication wallet;
	private Button btnAccount;
	private AlertDialog dialog;
	static int retryCounter = 0;
	
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// wallet = (MyWalletApplication) getApplication();
		
		

		btnAccount = (Button) findViewById(R.id.btnAccount);
		btnAccount.setOnClickListener(this);

		/*
		 * If its not first run and preference is checked for loging at startup
		 * login-screen appears
		 */
		if (MyWalletApplication.runCount != 0 ){
			if( MyWalletApplication.login)
			popDialog(); // login screen

		}else{
			
		}
			
			
		MyWalletApplication.runCount++;

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		menu.add(0, SETTINGS, 0, R.string.setting);
		menu.add(0, SEARCH, 0, R.string.search);
		menu.add(0,CURRENCY_CONVERTER,0, R.string.currencyConv);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case SETTINGS:
			startActivity(new Intent(this, PrefsActivity.class));

			break;
			
		case SEARCH:
			
			//
//			MyWalletApplication.
			
			break;
			
		case CURRENCY_CONVERTER:
			startActivity(new Intent(this, CurrencyConverterActiviy.class));
			break;

		default:

			Log.i(TAG, (String) item.getTitle());
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View view) {
		if (view == btnAccount) {

			// change the body of this actionlistener
			startActivity(new Intent(this,AccountsActivity.class));
			

		}

	}

	private void popDialog() {
		/*
		 * This method is called to prompt for username and password at login.
		 * Its button events are handled internally in this method
		 */

		AlertDialog dlg;

		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		/*
		 * instead of passing the creating an application context I'm using
		 * context of this class to get create the LayoutInflator object
		 * http://developer.android.com/guide/topics/ui/dialogs.html if a local
		 * context object is referred to the application context,
		 * nullPointExeption occurs
		 */

		LayoutInflater inflator = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflator.inflate(R.layout.login,
				(ViewGroup) findViewById(R.id.layout_root));

		final EditText edttxtUsername = (EditText) layout
				.findViewById(R.id.edttxtLoginUsername);
		final EditText edttxtPassword = (EditText) layout
				.findViewById(R.id.edttxtLoginPassword);

		final TextView txtLoginFail = (TextView) layout
				.findViewById(R.id.txtRetryCounter);
		Button btnCancel = (Button) layout.findViewById(R.id.btnCancelLogin);
		Button btnLogin = (Button) layout.findViewById(R.id.btnLogin);

		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// System.exit(0);
				retryCounter = 0;
				finish();
			}
		});

		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Login login = new Login(edttxtUsername.getText().toString(),
						edttxtPassword.getText().toString());

				if (login.authenticate()) {

					Log.d(TAG, "passed");
					Log.d(TAG, edttxtPassword.getText().toString());
					retryCounter = 0;

					dismisDial();

				}

				else {
					Resources r = getResources();
					String loginfail = r.getString(R.string.loginFailed);
					String retry = r.getString(R.string.retriesleft);
					if (retryCounter >= 2)
						finish();
					retryCounter++;
					Log.d(TAG, "failed");
					txtLoginFail.setText(loginfail + " " + retry + " "
							+ (3 - retryCounter));
					edttxtPassword.setText("");
					edttxtUsername.setText("");

				}
			}
		});

		alert.setView(layout);
		dlg = alert.create();

		dlg.setCancelable(false);

		setDial(dlg);
		dlg.show();

	}

	/*
	 * seDial and dimisDial() methods are created in order to dismiss the login
	 * dialog This helps not create a dialog helper class, since its a custom
	 * dialog from xml layout it cannot be dismissed before creation within the
	 * popDialog method, thus a member dialog is created and first created
	 * dialog is assigned to it. If a helper class is to created then a context
	 * has to be provided
	 */

	private void setDial(AlertDialog dialog) {
		this.dialog = dialog;
	}

	private void dismisDial() {
		dialog.dismiss();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

}