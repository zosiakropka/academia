package pl.killerapps.academia.activities.subject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import pl.killerapps.academia.R;
import pl.killerapps.academia.activities.pad.PadActivity;
import pl.killerapps.academia.api.command.note.NoteList;
import pl.killerapps.academia.entities.Note;
import pl.killerapps.academia.preferences.Preferences;
import pl.killerapps.academia.preferences.Preferences.UninitializedException;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AktivityActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_aktivity);

    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      final Activity ctx = this;

      int aktivity_id = extras.getInt("AKTIVITY_ID");

      String url;
      try {
        url = Preferences.get().academiaUrl();
        NoteList noteListCommand = new NoteList(url) {
          @Override
          public void on_response(final List<Note> notes) {

            ctx.runOnUiThread(new Runnable() {
              public void run() {
                final LinearLayout layout = (LinearLayout) findViewById(R.id.aktivity_layout);
                for (final Note note : notes) {
                  TextView tv = new TextView(ctx);
                  tv.setText(note.title);
                  tv.setOnClickListener(new OnClickListener() {
					
                    @Override
					public void onClick(View v) {
                      Intent padActivityIntent = new Intent(ctx, PadActivity.class);;
                      padActivityIntent.putExtra("NOTE_ID", note.id);
                      startActivity(padActivityIntent);
                    }
				});
                  layout.addView(tv);
                }
              }
            });
          }
        };
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("activity_id", "" + aktivity_id));
        noteListCommand.send_request(params);

      } catch (UninitializedException e) {
        e.printStackTrace();
      } catch (URISyntaxException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.aktivity, menu);
    return true;
  }
}
