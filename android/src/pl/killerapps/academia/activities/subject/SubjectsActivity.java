package pl.killerapps.academia.activities.subject;

import pl.killerapps.academia.R;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import java.net.URISyntaxException;
import java.util.List;
import pl.killerapps.academia.api.command.subject.SubjectsByAktivity;
import pl.killerapps.academia.entities.Aktivity;
import pl.killerapps.academia.entities.Subject;
import pl.killerapps.academia.utils.exceptions.FaultyConnectionDetailsException;
import pl.killerapps.academia.utils.exceptions.PreferencesUninitializedException;
import pl.killerapps.academia.utils.safe.SafeActivity;
import pl.killerapps.academia.utils.safe.SafeRunnable;

public class SubjectsActivity extends SafeActivity {

  @Override
  protected void safeOnResume() throws PreferencesUninitializedException, FaultyConnectionDetailsException, URISyntaxException {
    setContentView(R.layout.activity_subjects);
    setupActionBar();

    SubjectsByAktivity subjectsByAktivityCommand;
    final SafeActivity ctx = this;
    subjectsByAktivityCommand = new SubjectsByAktivity(this) {
      @Override
      public void on_response(final List<Subject> subjects) {
        ctx.runOnUiThread(new SafeRunnable(ctx) {
          public void safeRun() {
            LinearLayout layout = (LinearLayout) findViewById(R.id.subjects_layout);
            for (Subject subject : subjects) {
              TextView tv = new TextView(ctx);
              tv.setText(subject.name);
              layout.addView(tv);
              ListView lv = new ListView(ctx);
              layout.addView(lv);
              for (final Aktivity aktivity : subject.aktivities) {
                TextView tv2 = new TextView(ctx);
                tv2.setText("-" + subject.abbr + ": " + aktivity.type);
                tv2.setOnClickListener(new OnClickListener() {

                  @Override
                  public void onClick(View v) {
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
      }

      @Override
      public void on_failure() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
      }
    };
    subjectsByAktivityCommand.send_request();
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
