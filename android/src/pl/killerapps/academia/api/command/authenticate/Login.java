package pl.killerapps.academia.api.command.authenticate;

import android.content.Context;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.util.Log;
import java.io.UnsupportedEncodingException;

import pl.killerapps.academia.api.command.ApiCommandBase;
import pl.killerapps.academia.utils.exceptions.FaultyConnectionDetailsException;
import pl.killerapps.academia.utils.exceptions.HelloRequiredException;
import pl.killerapps.academia.utils.exceptions.PreferencesUninitializedException;
import pl.killerapps.academia.utils.Preferences;

/**
 *
 * @todo preferences.CsrfToken should fetch csrf token from server instead of
 * Login command
 * @author zosia
 */
public class Login extends ApiCommandBase {

  Context context;

  public Login(Context context)
          throws URISyntaxException, PreferencesUninitializedException, FaultyConnectionDetailsException {
    super("/auth/signin/");
    this.context = context;
  }

  public void login() throws HelloRequiredException, PreferencesUninitializedException, UnsupportedEncodingException, IOException, FaultyConnectionDetailsException {
    Preferences.Getter prefs = Preferences.get();
    final String username = prefs.username();
    final String password = prefs.password();
    DefaultHttpClient httpclient = new DefaultHttpClient();

    String csrftoken = Preferences.get().csrfToken();

    Log.i("login", "csrftoken");

    HttpPost post = new HttpPost(uri);
    post.addHeader("Cookie", "csrftoken=" + csrftoken);
    post.addHeader("X-CSRFToken", csrftoken);

    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("username", username));
    params.add(new BasicNameValuePair("password", password));

    post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

    HttpResponse response = httpclient.execute(post);

    HttpEntity entity = response.getEntity();

    System.out.println("Login form get: " + response.getStatusLine());
    if (entity != null) {
      entity.consumeContent();
    }

    List<Cookie> cookies = httpclient.getCookieStore().getCookies();
    if (!cookies.isEmpty()) {
      for (int i = 0; i < cookies.size(); i++) {
        if (cookies.get(i).getName().equals("sessionid")) {
          Preferences.set().sessionId(cookies.get(i).getValue());
        }
      }
    }

    // When HttpClient instance is no longer needed, 
    // shut down the connection manager to ensure
    // immediate deallocation of all system resources
    httpclient.getConnectionManager().shutdown();
  }

}
