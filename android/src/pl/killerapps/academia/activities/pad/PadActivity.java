package pl.killerapps.academia.activities.pad;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import pl.killerapps.academia.R;
import pl.killerapps.academia.utils.preferences.Preferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.widget.EditText;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import pl.killerapps.academia.api.command.note.NoteGet;
import pl.killerapps.academia.entities.Note;
import pl.killerapps.academia.utils.exceptions.NoConnectionDetailsException;
import pl.killerapps.academia.utils.exceptions.PreferencesUninitializedException;
import pl.killerapps.academia.utils.safe.SafeActivity;

public class PadActivity extends SafeActivity {

  PadClient client;

  @Override
  protected void safeOnResume() throws NoConnectionDetailsException, PreferencesUninitializedException, URISyntaxException, MalformedURLException {

    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      final int note_id = extras.getInt("NOTE_ID");

      setContentView(R.layout.activity_pad);

      String url = Preferences.get().academiaUrl();

      NoteGet noteGetCommand;
      noteGetCommand = new NoteGet(url, this) {

        @Override
        public void on_response(final Note note) {
          runOnUiThread(new Runnable() {
            public void run() {
              final EditText text = (EditText) findViewById(R.id.pad_content);
              text.setText(note.content);
            }
          });
        }

        @Override
        public void on_failure() {
          throw new UnsupportedOperationException("on_failure()");
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
