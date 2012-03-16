package day12.jan.y2012;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;

public class PrefsActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//rev 3
		super.onCreate(savedInstanceState);
		
		addPreferencesFromResource(R.xml.myprefs);
		
		
	}

}
