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
import org.apache.http.impl.client.DefaultHttpClient;

import pl.killerapps.academia.preferences.Preferences;
import pl.killerapps.academia.preferences.Preferences.UninitializedException;

/**
 * s * @author zosia
 */
public class Hello {

  URI uri;
  Context context;

  public Hello(Context context)
    throws URISyntaxException, UninitializedException {
    String _base_url = Preferences.get().academiaUrl();
    Log.d("csrftoken", "Hostname: " + _base_url);
    if (_base_url.endsWith("/")) {
      _base_url = _base_url.substring(0, _base_url.length() - 1);
    }
    this.uri = new URI("http://" + _base_url + "/api/auth/csrf");
    this.context = context;
  }

  public void hello()
    throws ClientProtocolException, IOException {

    HttpGet httpGet = new HttpGet(this.uri);
    HttpResponse response;
    response = (new DefaultHttpClient()).execute(httpGet);
    Header[] set_cookie_headers = response.getHeaders("Set-Cookie");
    Log.d("set_cookie headers", "headers len: " + set_cookie_headers.length);
    for (Header header : set_cookie_headers) {
      String[] units = header.getValue().split("; ");
      for (String unit : units) {
        String[] keyval = unit.split("=");
        Log.d("header_unit", "content: " + unit);
        if (keyval[0].equals("csrftoken")) {
          String csrftoken = keyval[1];
          try {
            Preferences.set().csrfToken(csrftoken);
          } catch (UninitializedException e) {
            e.printStackTrace();
          }
        }
      }
    }
  }

}
