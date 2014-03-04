package pl.killerapps.academia.activities.note;

import android.os.Bundle;
import android.widget.TextView;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.message.BasicNameValuePair;
import pl.killerapps.academia.R;
import pl.killerapps.academia.api.command.note.NoteGet;
import pl.killerapps.academia.entities.Note;
import pl.killerapps.academia.utils.exceptions.HelloRequiredException;
import pl.killerapps.academia.utils.exceptions.FaultyConnectionDetailsException;
import pl.killerapps.academia.utils.exceptions.PreferencesUninitializedException;
import pl.killerapps.academia.utils.safe.SafeActivity;

/**
 *
 * @author zosia
 */
public class NoteActivity extends SafeActivity {

  @Override
  protected void safeOnCreate(Bundle savedInstanceState) throws PreferencesUninitializedException, FaultyConnectionDetailsException, URISyntaxException, MalformedURLException, IOException, HelloRequiredException, HttpHostConnectException {
    setContentView(R.layout.activity_note);
  }

  /**
   * Called when the activity is first created.
   *
   * @throws
   * pl.killerapps.academia.utils.exceptions.FaultyConnectionDetailsException
   * @throws
   * pl.killerapps.academia.utils.exceptions.PreferencesUninitializedException
   * @throws java.net.URISyntaxException
   * @throws java.net.MalformedURLException
   */
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
              final TextView text = (TextView) findViewById(R.id.pad_content);
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
    }
  }
}
