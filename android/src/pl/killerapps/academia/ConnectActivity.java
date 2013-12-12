package pl.killerapps.academia;

import pl.killerapps.academia.pad.PadActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
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
				Intent padActivityIntent = new Intent(getApplicationContext(), PadActivity.class);
				Bundle extras = new Bundle();
				EditText ipEdit = (EditText)findViewById(R.id.conn_server_ip);
				EditText appPortEdit = (EditText)findViewById(R.id.conn_server_port);
				extras.putString("ip", ipEdit.getText().toString());
				extras.putInt("port", Integer.parseInt(appPortEdit.getText().toString()));
				padActivityIntent.putExtras(extras);
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
