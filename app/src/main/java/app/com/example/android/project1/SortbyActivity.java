package app.com.example.android.project1;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SortbyActivity extends PreferenceActivity
//implements Preference.OnPreferenceChangeListener
{

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.sort_settings);
    }
}
