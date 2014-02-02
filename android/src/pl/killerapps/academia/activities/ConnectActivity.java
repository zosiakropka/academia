package pl.killerapps.academia.activities;

import pl.killerapps.academia.R;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;

import pl.killerapps.academia.preferences.Preferences;

public class ConnectActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_connect);

    Button connectButton = (Button) findViewById(R.id.button_connect);

    connectButton.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View arg0) {
        EditText urlEdit = (EditText) findViewById(R.id.conn_server_url);
        final String url = urlEdit.getText().toString();

        EditText padPortEdit = (EditText) findViewById(R.id.conn_pad_port);
        int padPort = Integer.parseInt(padPortEdit.getText().toString());

        EditText usernameEdit = (EditText) findViewById(R.id.conn_username);
        final String username = usernameEdit.getText().toString();

        EditText passwordEdit = (EditText) findViewById(R.id.conn_password);
        final String password = passwordEdit.getText().toString();

        try {
          Preferences.set().academiaPadPort(padPort);
          Preferences.set().academiaUrl(url);
          Preferences.set().credentials(username, password);
        } catch (Preferences.UninitializedException ex) {
          Log.e("connect", "uninitialized pref", ex);
        }

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
