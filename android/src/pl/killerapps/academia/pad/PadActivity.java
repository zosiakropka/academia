package pl.killerapps.academia.pad;

import java.io.IOException;

import pl.killerapps.academia.ConnectActivity;
import pl.killerapps.academia.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class PadActivity extends Activity {

    PadClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pad);

        Bundle extras = getIntent().getExtras();
        String ip = extras.getString("ip");
        int padPort = extras.getInt("padport");

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pad, menu);
        return true;
    }

    private void handleFailure(Exception e) {
        e.printStackTrace();
        Intent aboutActivityIntent = new Intent(this, ConnectActivity.class);
        startActivity(aboutActivityIntent);
    }
}
