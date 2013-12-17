package pl.killerapps.academia.activities;

import pl.killerapps.academia.R;
import pl.killerapps.academia.activities.subject.SubjectsActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;

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

                EditText appPortEdit = (EditText) findViewById(R.id.conn_pad_port);
                int port = Integer.parseInt(appPortEdit.getText().toString());

                Intent padActivityIntent = new Intent(getApplicationContext(), SubjectsActivity.class);
                Editor preferencesEditor = getPreferences(MODE_PRIVATE).edit();
                preferencesEditor.putString("academia_url", url);
                preferencesEditor.putInt("academia_padport", port);
                preferencesEditor.commit();

                startActivity(padActivityIntent);
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
