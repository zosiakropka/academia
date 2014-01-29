package pl.killerapps.academia.activities.subject;

import pl.killerapps.academia.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

import java.net.URISyntaxException;
import java.util.List;
import pl.killerapps.academia.activities.ConnectActivity;
import pl.killerapps.academia.api.command.subject.SubjectsByAktivity;
import pl.killerapps.academia.entities.Aktivity;
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

            SubjectsByAktivity subjectsByAktivityCommand;
//            final Activity curr_activity = this;
            try {
            	final Activity ctx = this;
                subjectsByAktivityCommand = new SubjectsByAktivity(url) {
                    @Override
                    public void on_response(final List<Subject> subjects) {
                    	ctx.runOnUiThread(new Runnable() {
                		  public void run() {
                          	LinearLayout layout = (LinearLayout) findViewById(R.id.subjects_layout);
                        	for (Subject subject: subjects) {
                        		TextView tv = new TextView(ctx);
                        		tv.setText(subject.name);
                        		layout.addView(tv);
                        		ListView lv = new ListView(ctx);
                        		layout.addView(lv);
                        		for (final Aktivity aktivity: subject.aktivities) {
                            		TextView tv2 = new TextView(ctx);
                        			tv2.setText("-" + subject.abbr + ": " + aktivity.type);
                        			tv2.setOnClickListener(new OnClickListener() {
										
										@Override
										public void onClick(View v) {
											// TODO Auto-generated method stub

									        Intent aktivityActivityIntent = new Intent(ctx, AktivityActivity.class);;
									        aktivityActivityIntent.putExtra("AKTIVITY_ID", aktivity.id);
									        startActivity(aktivityActivityIntent);
										}
									});
                        			layout.addView(tv2);
                        		}
                        	}

                		  }
                		});
//                        NavUtils.navigateUpFromSameTask(curr_activity);
                    }
                };
                subjectsByAktivityCommand.send_request();
            } catch (URISyntaxException e) {
                e.printStackTrace();
                finish();
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
