package pl.killerapps.academia.pad;

import pl.killerapps.academia.ConnectActivity;
import pl.killerapps.academia.R;
import pl.killerapps.academia.R.layout;
import pl.killerapps.academia.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;

public class PadActivity extends Activity {

	PadClient client;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pad);
		
		Bundle extras = getIntent().getExtras();
		String ip = extras.getString("ip");
		int appPort = extras.getInt("port");
		int socketPort = 5001; // TODO: should be obtained via application
		
		client = new PadClient(ip, socketPort) {
			
			@Override
			public void onMessage(PadMessage message) {
				Log.i("PadSocket", message.get("purpose"));
				
			}
			@Override
			protected void onFailure(Exception e) {
				handleFailure(e);
			}
		};
		client.start();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pad, menu);
		return true;
	}
	
	private void handleFailure(Exception e) {
		e.printStackTrace();
		Intent aboutActivityIntent = new Intent(this, ConnectActivity.class);
		startActivity(aboutActivityIntent);
	}
}
