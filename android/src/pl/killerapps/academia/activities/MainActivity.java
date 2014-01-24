package pl.killerapps.academia.activities;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;

import pl.killerapps.academia.R;
import pl.killerapps.academia.activities.subject.SubjectsActivity;
import pl.killerapps.academia.api.command.authenticate.Hello;
import pl.killerapps.academia.preferences.Preferences;
import pl.killerapps.academia.preferences.Preferences.UninitializedException;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("MainActivity", "Main activity created.");
        super.onCreate(savedInstanceState);
        try {
            Preferences.init(getBaseContext());
			clearConnectionPrefs();
			(new Hello(getBaseContext())).hello();
		} catch (UninitializedException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @Override
    protected void onResume() {
        super.onResume();
        goNext();
    }

    protected void goNext() {
        Log.i("main", "Chosing nxt activity");
        String url;
		try {
			url = Preferences.get().academiaUrl();
	        int pad_port = Preferences.get().academiaPadPort();
	        Intent nxtActivityIntent;
	        if (url != null && pad_port > -1) {
	            Log.i("main", "entering subjects activity");
	            nxtActivityIntent = new Intent(this, SubjectsActivity.class);
	        } else {
	            Log.i("main", "let's connect!");
	            nxtActivityIntent = new Intent(this, ConnectActivity.class);
	        }
	        startActivity(nxtActivityIntent);
		} catch (UninitializedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private boolean clearConnectionPrefs() throws UninitializedException {
    	return Preferences.clear().execute();
    }
}
