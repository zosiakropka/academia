package pl.killerapps.academia.activities.pad;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import pl.killerapps.academia.R;
import pl.killerapps.academia.preferences.Preferences;
import pl.killerapps.academia.preferences.Preferences.UninitializedException;
import android.os.Bundle;
import android.app.Activity;
import android.os.Looper;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.widget.EditText;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import pl.killerapps.academia.api.command.note.NoteGet;
import pl.killerapps.academia.entities.Note;

public class PadActivity extends Activity {

  PadClient client;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      try {
        final int note_id = extras.getInt("NOTE_ID");

        setContentView(R.layout.activity_pad);

        String url = Preferences.get().academiaUrl();

        NoteGet noteGetCommand;
        noteGetCommand = new NoteGet(url) {

          @Override
          public void on_response(final Note note) {
            runOnUiThread(new Runnable() {
              public void run() {
                final EditText text = (EditText) findViewById(R.id.pad_content);
                text.setText(note.content);
              }
            });
          }
        };
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("note_id", "" + note_id));
        noteGetCommand.send_request(params);

        String ip = (new URL("http://" + url)).getHost();
        int padPort = Preferences.get().academiaPadPort();

        client = new PadClient(ip, padPort) {

          @Override
          public void onMessage(PadMessage message) {
            /**
             * @todo Apply changes
             */
          }

          @Override
          protected void onFailure(Exception e) {
            handleFailure(e);
          }

          @Override
          protected void onReady() {

            PadMessage msg = new PadMessage();
            msg.set_string("purpose", "pad");
            msg.set_string("message", "" + note_id);
            try {
              client.send(msg); // just testing sending msg
            } catch (IOException ex) {
              /**
               * @todo: This should add message to some queue
               */
            }
          }
        };
        client.start();

      } catch (MalformedURLException e) {
        handleFailure(e);
      } catch (UninitializedException e) {
        handleFailure(e);
      } catch (URISyntaxException e) {
        handleFailure(e);
      }
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    try {
      client.stop();
    } catch (IOException e) {
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.pad, menu);
    return true;
  }

  private void handleFailure(Exception e) {
    e.printStackTrace();
    NavUtils.navigateUpFromSameTask(this);
  }
}
