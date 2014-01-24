package pl.killerapps.academia.activities.subject;

import pl.killerapps.academia.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

import java.net.URISyntaxException;
import java.util.List;
import pl.killerapps.academia.activities.ConnectActivity;
import pl.killerapps.academia.api.command.note.NoteListBySubjectByAktivity;
import pl.killerapps.academia.entities.Subject;
import pl.killerapps.academia.preferences.Preferences;
import pl.killerapps.academia.preferences.Preferences.UninitializedException;

public class SubjectsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);
        setupActionBar();

        String url;
		try {
			url = Preferences.get().academiaUrl();
	        if (url == null) {
	            Intent nxtActivityIntent = new Intent(this, ConnectActivity.class);
	            startActivity(nxtActivityIntent);
	        } else {

	            NoteListBySubjectByAktivity noteListCommand;
	            final Activity curr_activity = this;
	            try {
	                noteListCommand = new NoteListBySubjectByAktivity(url) {
	                    @Override
	                    public void on_response(List<Subject> response) {
	                        NavUtils.navigateUpFromSameTask(curr_activity);
	                    }
	                };
	                noteListCommand.send_request();
	            } catch (URISyntaxException e) {
	                e.printStackTrace();
	                finish();
	            }
	        }
		} catch (UninitializedException e1) {
			e1.printStackTrace();
		}
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.subjects, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. Use NavUtils to allow users
                // to navigate up one level in the application structure. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back
                //
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
