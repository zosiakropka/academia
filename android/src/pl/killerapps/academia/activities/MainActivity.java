package pl.killerapps.academia.activities;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;

import pl.killerapps.academia.R;
import pl.killerapps.academia.activities.subject.SubjectsActivity;
import pl.killerapps.academia.api.command.authenticate.Hello;
import pl.killerapps.academia.api.command.authenticate.Login;
import pl.killerapps.academia.preferences.Preferences;
import pl.killerapps.academia.preferences.Preferences.UninitializedException;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.webkit.CookieSyncManager;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("MainActivity", "onCreate.");
        super.onCreate(savedInstanceState);
        Preferences.init(getBaseContext());

        CookieSyncManager.createInstance(this); 
        CookieSyncManager.getInstance().startSync();
        goNext();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MainActivity", "onResume.");
        Preferences.init(getBaseContext());
        CookieSyncManager.getInstance().stopSync();
        goNext();
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	CookieSyncManager.getInstance().sync();
    }

    private void login() {
        (new Thread(new Runnable() {
			
			@Override
			public void run() {
                try {
                	Preferences.Getter prefs = Preferences.get();
        			(new Hello(getBaseContext())).hello();
                    (new Login(prefs.academiaUrl(), getBaseContext())).login(prefs.username(), prefs.password());
                } catch (URISyntaxException ex) {
                    Log.e("login", "can't login", ex);
				} catch (ClientProtocolException ex) {
                    Log.e("connect", "client protocol exception", ex);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (UninitializedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		})).start();
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
	            login();
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
