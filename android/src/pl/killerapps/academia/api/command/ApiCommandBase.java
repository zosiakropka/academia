package pl.killerapps.academia.api.command;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;
import pl.killerapps.academia.utils.Log;
import pl.killerapps.academia.utils.exceptions.FaultyConnectionDetailsException;
import pl.killerapps.academia.utils.exceptions.HelloRequiredException;
import pl.killerapps.academia.utils.exceptions.LoginRequiredException;
import pl.killerapps.academia.utils.exceptions.PreferencesUninitializedException;

import pl.killerapps.academia.utils.Preferences;

public abstract class ApiCommandBase {
  
  protected URI uri;
  protected Log log = new Log("ApiCommandBase");

  HttpClient client = new DefaultHttpClient();

  protected ApiCommandBase(String command_path) throws URISyntaxException, PreferencesUninitializedException, FaultyConnectionDetailsException {

    URI base_uri = Preferences.get().academiaApiUri();

    if (!command_path.startsWith("/")) {
      command_path = "/" + command_path;
    }

    this.uri = base_uri.resolve("/api" + command_path);
  }

  protected HttpResponse raw_post(HttpPost post, HttpContext localContext) throws ClientProtocolException, IOException {
    post.setURI(uri);
    HttpResponse response = client.execute(post, localContext);
    return response;
  }

  protected HttpResponse raw_post(HttpPost post) throws ClientProtocolException, IOException, HelloRequiredException, LoginRequiredException, PreferencesUninitializedException {
    log.d("Request path: " + uri.getHost() + ":" + uri.getPort() + uri.getPath());
    post.setURI(uri);
    Preferences.Getter prefs = Preferences.get();
    post.addHeader("Cookie", "csrftoken=" + prefs.csrfToken() + "; " + "sessionid=" + prefs.sessionId());
    post.addHeader("X-CSRFToken", prefs.csrfToken());
    HttpResponse response = client.execute(post);
    return response;
  }

  protected HttpResponse raw_get(HttpGet get, String params) throws ClientProtocolException, IOException, HelloRequiredException, LoginRequiredException, URISyntaxException, PreferencesUninitializedException {
    get.setURI(new URI(uri.toString() + "?" + params));
    get.addHeader("Cookie", "sessionid=" + Preferences.get().sessionId());
    HttpResponse response = client.execute(get);
    return response;
  }

  protected void handleFailure(Exception ex) {
    ex.printStackTrace();
  }
}
