package pl.killerapps.academia.activities.pad;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import pl.killerapps.academia.R;
import pl.killerapps.academia.api.command.note.NoteGet;
import pl.killerapps.academia.api.command.note.NoteList;
import pl.killerapps.academia.entities.Note;
import pl.killerapps.academia.preferences.Preferences;
import pl.killerapps.academia.preferences.Preferences.UninitializedException;
import android.os.Bundle;
import android.os.Looper;
import android.app.Activity;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class PadActivity extends Activity {

  PadClient client;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    Looper.prepare();

    Bundle extras = getIntent().getExtras();
    if (extras != null) {
	    try {
	    int note_id = extras.getInt("NOTE_ID");

	    setContentView(R.layout.activity_pad);
	
	    String url = Preferences.get().academiaUrl();

	    NoteGet noteGetCommand = new  NoteGet(url) {

	          @Override
	          public void on_response(final Note note) {
	        	  
	        	final EditText text = (EditText) findViewById(R.id.pad_content);
	        	text.setText(note.content);
	          }
		};
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("note_id", "" + 1));
		noteGetCommand.send_request(params);
	    
	    String ip;
//	    ip = (new URL(url)).getHost();
//	    int padPort = Preferences.get().academiaPadPort();
//	
//	    client = new PadClient(ip, padPort) {
//	
//	      @Override
//	      public void onMessage(PadMessage message) {
//	        try {
//	          message.set_string("purpose", "test");
//	          message.set_string("message", "got it");
//	          send(message);
//	        } catch (UnsupportedOperationException e) {
//	          e.printStackTrace();
//	        } catch (IOException e) {
//	          e.printStackTrace();
//	        }
//	
//	      }
//	
//	      @Override
//	      protected void onFailure(Exception e) {
//	        handleFailure(e);
//	      }
//	    };
//	    client.start();
//    } catch (MalformedURLException e1) {
//        e1.printStackTrace();
//        finish();
      } catch (UninitializedException e1) {
        e1.printStackTrace();
        finish();
      } catch (URISyntaxException e1) {
		e1.printStackTrace();
        finish();
      }
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
