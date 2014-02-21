package pl.killerapps.academia.activities;

import pl.killerapps.academia.R;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import pl.killerapps.academia.utils.exceptions.PreferencesUninitializedException;

import pl.killerapps.academia.utils.preferences.Preferences;
import pl.killerapps.academia.utils.safe.SafeActivity;
import pl.killerapps.academia.utils.safe.SafeOnClickListener;

public class ConnectActivity extends SafeActivity {

  @Override
  protected void safeOnResume() {
    setContentView(R.layout.activity_connect);

    Button connectButton = (Button) findViewById(R.id.button_connect);

    connectButton.setOnClickListener(new SafeOnClickListener(this) {

      @Override
      public void safeOnClick(View arg0) throws PreferencesUninitializedException {
        EditText urlEdit = (EditText) findViewById(R.id.conn_server_url);
        final String url = urlEdit.getText().toString();

        EditText padPortEdit = (EditText) findViewById(R.id.conn_pad_port);
        int padPort = Integer.parseInt(padPortEdit.getText().toString());

        EditText usernameEdit = (EditText) findViewById(R.id.conn_username);
        final String username = usernameEdit.getText().toString();

        EditText passwordEdit = (EditText) findViewById(R.id.conn_password);
        final String password = passwordEdit.getText().toString();

        Preferences.Setter prefs = Preferences.set();
        prefs.academiaPadPort(padPort);
        prefs.academiaUrl(url);
        prefs.credentials(username, password);

        finish();
      }
    });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.connect, menu);
    return true;
  }
}
