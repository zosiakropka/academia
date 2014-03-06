package pl.killerapps.academia.api.command.authenticate;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;
import pl.killerapps.academia.utils.exceptions.FaultyConnectionDetailsException;
import pl.killerapps.academia.utils.exceptions.HelloFailedException;
import pl.killerapps.academia.utils.exceptions.PreferencesUninitializedException;

import pl.killerapps.academia.utils.preferences.Preferences;

/**
 * s * @author zosia
 */
public class Hello {

  URI uri;
  Context context;

  public Hello(Context context)
          throws URISyntaxException, PreferencesUninitializedException, FaultyConnectionDetailsException {
    URI base_uri = Preferences.get().academiaApiUri();
    this.uri = base_uri.resolve("/api/auth/csrf");
    Log.d("csrftoken", "Path: " + this.uri.toString());
    this.context = context;
  }

  public void hello()
          throws HttpHostConnectException, ClientProtocolException, IOException, PreferencesUninitializedException, HelloFailedException {
    HttpGet httpGet = new HttpGet(this.uri);
    httpGet.setHeader("", null);
    HttpResponse response;
    response = (new DefaultHttpClient()).execute(httpGet);
    Header[] set_cookie_headers = response.getHeaders("Set-Cookie");
    for (Header header : set_cookie_headers) {
      String[] units = header.getValue().split("; ");
      for (String unit : units) {
        String[] keyval = unit.split("=");
        if (keyval[0].equals("csrftoken")) {
          String csrftoken = keyval[1];
          Preferences.set().csrfToken(csrftoken);
        }
      }
    }
    throw new HelloFailedException();

  }

}
