package pl.killerapps.academia.activities;

import pl.killerapps.academia.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;
import java.net.URISyntaxException;
import pl.killerapps.academia.api.command.authenticate.Login;

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
                String url = urlEdit.getText().toString();

                EditText padPortEdit = (EditText) findViewById(R.id.conn_pad_port);
                int padPort = Integer.parseInt(padPortEdit.getText().toString());

                EditText usernameEdit = (EditText) findViewById(R.id.conn_username);
                String username = usernameEdit.getText().toString();

                EditText passwordEdit = (EditText) findViewById(R.id.conn_password);
                String password = passwordEdit.getText().toString();

                Editor prefsEditor;
                prefsEditor = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit();
                prefsEditor.clear();
                prefsEditor.putString("academia-url", url);
                prefsEditor.putInt("academia-padport", padPort);
                try {
                    Login loginCommand = new Login(url, getBaseContext());
                    loginCommand.login(username, password);
                    if (prefsEditor.commit()) {
                        finish();
                    }
                } catch (URISyntaxException ex) {
                    Log.e("login", "cant login", ex);
                }
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
