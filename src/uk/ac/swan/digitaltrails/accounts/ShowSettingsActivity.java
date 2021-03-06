package uk.ac.swan.digitaltrails.accounts;

import uk.ac.swan.digitaltrails.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

/**
 * @author Chris Lewis
 * Class to display user settings.
 */
public class ShowSettingsActivity extends Activity {

 /* (non-Javadoc)
 * @see android.app.Activity#onCreate(android.os.Bundle)
 */
@Override
 protected void onCreate(Bundle savedInstanceState) {

  super.onCreate(savedInstanceState);
  setContentView(R.layout.show_settings_layout);

  SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

  StringBuilder builder = new StringBuilder();

  builder.append("\n" + sharedPrefs.getBoolean("perform_updates", false));
  builder.append("\n" + sharedPrefs.getString("updates_interval", "-1"));
  builder.append("\n" + sharedPrefs.getString("welcome_message", "NULL"));

  TextView settingsTextView = (TextView) findViewById(R.id.settings_text_view);
  settingsTextView.setText(builder.toString());

 }

}
