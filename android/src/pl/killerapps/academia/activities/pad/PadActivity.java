package pl.killerapps.academia.activities.pad;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import pl.killerapps.academia.R;
import pl.killerapps.academia.preferences.Preferences;
import pl.killerapps.academia.preferences.Preferences.UninitializedException;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.NavUtils;
import android.view.Menu;

public class PadActivity extends Activity {

    PadClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_pad);

            String url = Preferences.get().academiaUrl();
            String ip;
            ip = (new URL(url)).getHost();
            int padPort = Preferences.get().academiaPadPort();

            client = new PadClient(ip, padPort) {

                @Override
                public void onMessage(PadMessage message) {
                    try {
                        message.set_string("purpose", "test");
                        message.set_string("message", "got it");
                        send(message);
                    } catch (UnsupportedOperationException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                protected void onFailure(Exception e) {
                    handleFailure(e);
                }
            };
            client.start();
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
            // @todo go back
        } catch (UninitializedException e1) {
            // @todo go back
			e1.printStackTrace();
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
