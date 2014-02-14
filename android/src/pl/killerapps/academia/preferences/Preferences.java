package pl.killerapps.academia.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;

public class Preferences {

  static String USERNAME = "username";
  static String PASSWORD = "password";
  static String CSRF_TOKEN = "csrftoken";
  static String SESSION_ID = "sessionid";
  static String ACADEMIA_URL = "academia-url";
  static String ACADEMIA_PAD_PORT = "academia-pad-port";

  private static SharedPreferences preferences = null;

  public static void init(Context context) {
    if (preferences == null) {
      Log.d("prefs", "initializing preferences");
      preferences = PreferenceManager.getDefaultSharedPreferences(context);
      preferences.edit().clear();
    }
  }

  public static Getter get()
          throws UninitializedException {
    return new Getter(getOrExcept());
  }

  public static Setter set()
          throws UninitializedException {
    return new Setter(getOrExcept());
  }

  public static Cleaner clear()
          throws UninitializedException {
    return new Cleaner(getOrExcept());
  }

  private static SharedPreferences getOrExcept()
          throws UninitializedException {
    if (preferences == null) {
      throw new UninitializedException();
    } else {
      return preferences;
    }
  }

  public static class UninitializedException extends Exception {

    private static final long serialVersionUID = 1L;

  }

  public static class Getter {

    private SharedPreferences preferences;

    public Getter(SharedPreferences preferences) {
      this.preferences = preferences;
    }

    public String username() {
      return preferences.getString(Preferences.USERNAME, null);
    }

    public String password() {
      return preferences.getString(Preferences.PASSWORD, null);
    }

    public String csrfToken() {
      return preferences.getString(Preferences.CSRF_TOKEN, null);
    }

    public String sessionId() {
      return preferences.getString(Preferences.SESSION_ID, "");
    }

    public String academiaUrl() {
      return preferences.getString(Preferences.ACADEMIA_URL, null);
    }

    public int academiaPadPort() {
      return preferences.getInt(Preferences.ACADEMIA_PAD_PORT, -1);
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

    public boolean academiaUrl(String academia_url) {
      return editor.putString(Preferences.ACADEMIA_URL, academia_url).commit();
    }

    public boolean academiaPadPort(int academia_pad_port) {
      return editor.putInt(Preferences.ACADEMIA_PAD_PORT, academia_pad_port).commit();
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
      editor.remove(Preferences.ACADEMIA_PAD_PORT);
      editor.remove(Preferences.ACADEMIA_URL);
      return editor.commit();
    }
  }
}
