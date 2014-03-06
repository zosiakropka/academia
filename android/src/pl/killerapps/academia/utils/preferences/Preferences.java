package pl.killerapps.academia.utils.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;
import java.net.URI;
import java.net.URISyntaxException;
import pl.killerapps.academia.utils.exceptions.FaultyConnectionDetailsException;
import pl.killerapps.academia.utils.exceptions.HelloRequiredException;
import pl.killerapps.academia.utils.exceptions.LoginRequiredException;
import pl.killerapps.academia.utils.exceptions.PreferencesUninitializedException;

public class Preferences {

  static String USERNAME = "username";
  static String PASSWORD = "password";
  static String CSRF_TOKEN = "csrftoken";
  static String SESSION_ID = "sessionid";
  static String ACADEMIA_API_URI = "academia-api-uri";
  static String ACADEMIA_PAD_URI = "academia-pad-uri";

  private static SharedPreferences preferences = null;

  public static void init(Context context) {
    if (preferences == null) {
      Log.i("prefs", "initializing preferences");
      preferences = PreferenceManager.getDefaultSharedPreferences(context);
      preferences.edit().clear();
    }
  }

  public static Getter get()
          throws PreferencesUninitializedException {
    return new Getter(getOrExcept());
  }

  public static Setter set()
          throws PreferencesUninitializedException {
    return new Setter(getOrExcept());
  }

  public static Cleaner clear()
          throws PreferencesUninitializedException {
    return new Cleaner(getOrExcept());
  }

  private static SharedPreferences getOrExcept()
          throws PreferencesUninitializedException {
    if (preferences == null) {
      throw new PreferencesUninitializedException();
    } else {
      return preferences;
    }
  }

  public static class Getter {

    private final SharedPreferences preferences;

    public Getter(SharedPreferences preferences) {
      this.preferences = preferences;
    }

    public String username() throws FaultyConnectionDetailsException {
      String username = preferences.getString(Preferences.USERNAME, null);
      if (username == null) {
        throw new FaultyConnectionDetailsException();
      }
      return username;
    }

    public String password() throws FaultyConnectionDetailsException {
      String password = preferences.getString(Preferences.PASSWORD, null);
      if (password == null) {
        throw new FaultyConnectionDetailsException();
      }
      return password;
    }

    public String csrfToken() throws HelloRequiredException {
      String csrftoken = preferences.getString(Preferences.CSRF_TOKEN, null);
      if (csrftoken == null) {
        throw new HelloRequiredException();
      }
      return csrftoken;
    }

    public String sessionId() throws LoginRequiredException {
      String sid = preferences.getString(Preferences.SESSION_ID, null);
      if (sid == null) {
        throw new LoginRequiredException();
      }
      return sid;
    }

    public URI academiaApiUri() throws FaultyConnectionDetailsException {
      String uri = preferences.getString(Preferences.ACADEMIA_API_URI, "");
      if (uri != null && !uri.equals("")) {
        try {
          return new URI("http://" + uri);
        } catch (URISyntaxException ex) {
        }
      }
      throw new FaultyConnectionDetailsException();
    }

    public URI academiaPadUri() throws FaultyConnectionDetailsException {
      String uri = preferences.getString(Preferences.ACADEMIA_PAD_URI, "");
      if (uri != null) {
        try {
          return new URI("http://" + uri);
        } catch (URISyntaxException ex) {
        }
      }
      throw new FaultyConnectionDetailsException();
    }
  }

  public static class Setter {

    Editor editor;

    public Setter(SharedPreferences preferences) {
      this.editor = preferences.edit();
    }

    public boolean credentials(String username, String password) {
      editor.putString(Preferences.USERNAME, username);
      editor.putString(Preferences.PASSWORD, password);
      return editor.commit();
    }

    public boolean csrfToken(String csrftoken) {
      return editor.putString(Preferences.CSRF_TOKEN, csrftoken).commit();
    }

    public boolean sessionId(String sessionid) {
      return editor.putString(Preferences.SESSION_ID, sessionid).commit();
    }

    public boolean academiaApiUri(String academia_url) {
      return editor.putString(Preferences.ACADEMIA_API_URI, academia_url).commit();
    }

    public boolean academiaPadUri(String academia_pad_uri) {
      return editor.putString(Preferences.ACADEMIA_PAD_URI, academia_pad_uri).commit();
    }
  }

  public static class Cleaner {

    Editor editor;

    public Cleaner(SharedPreferences preferences) {
      this.editor = preferences.edit();
    }

    public boolean execute() {
      editor.remove(Preferences.CSRF_TOKEN);
      editor.remove(Preferences.SESSION_ID);
      editor.remove(Preferences.USERNAME);
      editor.remove(Preferences.PASSWORD);
      editor.remove(Preferences.ACADEMIA_PAD_URI);
      editor.remove(Preferences.ACADEMIA_API_URI);
      return editor.commit();
    }
  }
}
