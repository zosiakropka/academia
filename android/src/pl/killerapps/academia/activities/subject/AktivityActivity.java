package pl.killerapps.academia.activities.subject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import pl.killerapps.academia.R;
import pl.killerapps.academia.api.command.note.NoteList;
import pl.killerapps.academia.entities.Note;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.io.IOException;
import java.net.MalformedURLException;
import org.apache.http.conn.HttpHostConnectException;
import pl.killerapps.academia.activities.pad.PadActivity;
import pl.killerapps.academia.utils.Preferences;
import pl.killerapps.academia.utils.exceptions.ApiPermissionDeniedException;
import pl.killerapps.academia.utils.exceptions.FaultyConnectionDetailsException;
import pl.killerapps.academia.utils.exceptions.HelloFailedException;
import pl.killerapps.academia.utils.exceptions.HelloRequiredException;
import pl.killerapps.academia.utils.exceptions.LoginRequiredException;
import pl.killerapps.academia.utils.exceptions.PreferencesUninitializedException;
import pl.killerapps.academia.utils.safe.SafeActivity;
import pl.killerapps.academia.utils.safe.SafeRunnable;

public class AktivityActivity extends SafeActivity {

  @Override
  protected void safeOnResume() throws FaultyConnectionDetailsException, PreferencesUninitializedException, URISyntaxException {
    setContentView(R.layout.activity_aktivity);

    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      final SafeActivity ctx = this;

      int aktivity_id = extras.getInt("AKTIVITY_ID");

      NoteList noteListCommand = new NoteList(this) {
        @Override
        public void on_response(final List<Note> notes) {

          ctx.runOnUiThread(new SafeRunnable(ctx) {

            @Override
            public void safeRun() throws PreferencesUninitializedException, FaultyConnectionDetailsException, URISyntaxException, MalformedURLException, IOException, HelloRequiredException, LoginRequiredException, HttpHostConnectException, HelloFailedException, ApiPermissionDeniedException {

              final LinearLayout layout = (LinearLayout) findViewById(R.id.aktivity_layout);
              for (final Note note : notes) {
                TextView tv = new TextView(ctx);
                tv.setText(note.title);
                if (note.canEdit()) {
                  tv.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                      Intent padActivityIntent = new Intent(ctx, PadActivity.class);
                      padActivityIntent.putExtra("NOTE_ID", note.id);
                      startActivity(padActivityIntent);
                    }
                  });
                } else {
                  tv.setTextAppearance(ctx, android.R.style.TextAppearance_Inverse);
                }
                layout.addView(tv);
              }
            }
          });
        }

        @Override
        public void on_failure() {
          throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
      };
      List<NameValuePair> params = new ArrayList<NameValuePair>();
      params.add(new BasicNameValuePair("activity_id", "" + aktivity_id));
      noteListCommand.send_request(params);

    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.aktivity, menu);
    return true;
  }

}
