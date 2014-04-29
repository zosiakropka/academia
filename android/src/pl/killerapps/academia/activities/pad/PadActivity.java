package pl.killerapps.academia.activities.pad;

import java.io.IOException;
import java.net.MalformedURLException;
import pl.killerapps.academia.R;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.widget.EditText;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.message.BasicNameValuePair;
import pl.killerapps.academia.api.command.note.NoteGet;
import pl.killerapps.academia.entities.Note;
import pl.killerapps.academia.utils.exceptions.FaultyConnectionDetailsException;
import pl.killerapps.academia.utils.exceptions.HelloFailedException;
import pl.killerapps.academia.utils.exceptions.HelloRequiredException;
import pl.killerapps.academia.utils.exceptions.PreferencesUninitializedException;
import pl.killerapps.academia.utils.safe.SafeActivity;

public class PadActivity extends SafeActivity {

  PadClient client;
  PadMonitor monitor;

  @Override
  protected void safeOnCreate(Bundle savedInstanceState) throws PreferencesUninitializedException, FaultyConnectionDetailsException, URISyntaxException, MalformedURLException, IOException, HelloRequiredException, HttpHostConnectException, HelloFailedException {
    setContentView(R.layout.activity_pad);
    monitor = new PadMonitor(this, (EditText) findViewById(R.id.pad_content)) {

      @Override
      public void on_local_patches(String patches_text) {
        PadMessage msg = new PadMessage();
        msg.set_string("purpose", "patches");
        msg.set_string("token", "todo:propertoken");
        msg.set_string("message", patches_text);
        try {
          client.send(msg);
        } catch (IOException ex) {
          log.e("cant send patches");
          // @todo: queue patches
        }
      }
    };
  }

  @Override
  protected void safeOnResume() throws FaultyConnectionDetailsException, PreferencesUninitializedException, URISyntaxException, MalformedURLException {

    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      final int note_id = extras.getInt("NOTE_ID");

      NoteGet noteGetCommand;
      noteGetCommand = new NoteGet(this) {
        @Override
        public void on_response(final Note note) {
          runOnUiThread(new Runnable() {
            public void run() {
              final EditText text = (EditText) findViewById(R.id.pad_content);
              text.setText(note.content);
              monitor.start();
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

      client = new PadClient() {

        @Override
        public void onMessage(PadMessage message) {
          log.i("message: " + message.get_string("message"));
          /**
           * @todo Apply changes
           */
          if (message.get_string("purpose").equals("patches") && message.contains("message")) {
            monitor.push_patches_text(message.get_string("message"));
          }
        }

        @Override
        protected void onFailure(Exception e) {
          handleFailure(e);
        }

        @Override
        protected void onReady() {

          PadMessage msg = new PadMessage();
          msg.set_string("purpose", "join");
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
