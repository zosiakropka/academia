package pl.killerapps.academia.activities;

import android.os.Bundle;
import pl.killerapps.academia.R;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.conn.HttpHostConnectException;
import pl.killerapps.academia.utils.Defaults;
import pl.killerapps.academia.utils.exceptions.FaultyConnectionDetailsException;
import pl.killerapps.academia.utils.exceptions.HelloFailedException;
import pl.killerapps.academia.utils.exceptions.HelloRequiredException;
import pl.killerapps.academia.utils.exceptions.PreferencesUninitializedException;

import pl.killerapps.academia.utils.Preferences;
import pl.killerapps.academia.utils.safe.SafeActivity;
import pl.killerapps.academia.utils.safe.SafeOnClickListener;

public class ConnectActivity extends SafeActivity {

  @Override
  protected void safeOnCreate(Bundle savedInstanceState) throws PreferencesUninitializedException, FaultyConnectionDetailsException, URISyntaxException, MalformedURLException, IOException, HelloRequiredException, HttpHostConnectException, HelloFailedException {

    setContentView(R.layout.activity_connect);

    Button connectButton = (Button) findViewById(R.id.button_connect);

    connectButton.setOnClickListener(new SafeOnClickListener(this) {

      @Override
      public void safeOnClick(View arg0) throws PreferencesUninitializedException {

        final String apiUri = apiUriEdit().getText().toString();

        final String padUri = padUriEdit().getText().toString();

        final String username = usernameEdit().getText().toString();

        final String password = passwordEdit().getText().toString();

        if (!username.equals("") && !apiUri.equals("") && !padUri.equals("")) {
          Preferences.Setter prefsSetter = Preferences.set();

          prefsSetter.academiaPadUri(padUri);
          prefsSetter.academiaApiUri(apiUri);
          prefsSetter.credentials(username, password);

          finish();
        }
      }
    });

    apiUriEdit().setOnFocusChangeListener(new View.OnFocusChangeListener() {

      @Override
      public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus && padUriEdit().getText().toString().isEmpty() && !apiUriEdit().getText().toString().isEmpty()) {
          try {
            String padUri = (new URI(Defaults.HTTP_PREFIX + apiUriEdit().getText().toString())).getHost() + ":" + Defaults.PAD_PORT;
            padUriEdit().setText(padUri);
          } catch (URISyntaxException ex) {
          }
        }
      }
    });
  }

  @Override
  protected void safeOnResume() {

    try {
      Preferences.Getter prefs = Preferences.get();
      try {
        URI apiUri = prefs.academiaApiUri();
        String apiUriStr = apiUri.toString().replace(Defaults.HTTP_PREFIX, "");
        apiUriEdit().setText(apiUriStr);
      } catch (FaultyConnectionDetailsException e) {
      }
      try {
        URI padUri = prefs.academiaPadUri();
        String padUriStr = padUri.toString().replace(Defaults.HTTP_PREFIX, "");
        padUriEdit().setText(padUriStr);
      } catch (FaultyConnectionDetailsException e) {
      }
      try {
        String username = prefs.username();
        usernameEdit().setText(username);
      } catch (FaultyConnectionDetailsException e) {
      }
    } catch (PreferencesUninitializedException e) {
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.connect, menu);
    return true;
  }

  private EditText apiUriEdit() {
    return (EditText) findViewById(R.id.conn_api_uri);
  }

  private EditText padUriEdit() {
    return (EditText) findViewById(R.id.conn_pad_uri);
  }

  private EditText usernameEdit() {
    return (EditText) findViewById(R.id.conn_username);
  }

  private EditText passwordEdit() {
    return (EditText) findViewById(R.id.conn_password);
  }

}
