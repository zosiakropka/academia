package pl.killerapps.academia.activities;

import pl.killerapps.academia.R;
import pl.killerapps.academia.activities.subject.SubjectsActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MainActivity", "Main activity created.");
        super.onCreate(savedInstanceState);
    	goNext();
    }

    @Override
    protected void onResume() {
    	super.onResume();
    	goNext();
    }
    protected void goNext() {
        Log.i("main", "Chosing nxt activity");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String url = preferences.getString("academia-url", null);
        int pad_port = preferences.getInt("academia-padport", -1);
        Intent nxtActivityIntent;
        if (url != null && pad_port > -1) {
            Log.i("main", "entering subjects activity");
            nxtActivityIntent = new Intent(this, SubjectsActivity.class);
        } else {
            Log.i("main", "let's connect!");
            nxtActivityIntent = new Intent(this, ConnectActivity.class);
        }
        startActivity(nxtActivityIntent);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
