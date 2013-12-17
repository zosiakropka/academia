package pl.killerapps.academia.activities;

import pl.killerapps.academia.R;
import pl.killerapps.academia.activities.subject.SubjectsActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MainActivity", "Main activity created.");
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        String url = preferences.getString("academia_url", null);
        int pad_port = preferences.getInt("academia_padport", 0);
        Intent nxtActivityIntent;
        if (url != null && pad_port > 0) {
            nxtActivityIntent = new Intent(this, SubjectsActivity.class);
        } else {
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
