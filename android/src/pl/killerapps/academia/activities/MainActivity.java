package pl.killerapps.academia.activities;

import java.io.IOException;
import java.net.URISyntaxException;

import pl.killerapps.academia.R;
import pl.killerapps.academia.activities.subject.SubjectsActivity;
import pl.killerapps.academia.api.command.authenticate.Hello;
import pl.killerapps.academia.api.command.authenticate.Login;
import pl.killerapps.academia.utils.preferences.Preferences;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.webkit.CookieSyncManager;
import pl.killerapps.academia.utils.exceptions.HelloRequiredException;
import pl.killerapps.academia.utils.exceptions.NoConnectionDetailsException;
import pl.killerapps.academia.utils.exceptions.PreferencesUninitializedException;
import pl.killerapps.academia.utils.safe.SafeActivity;
import pl.killerapps.academia.utils.safe.SafeRunnable;

public class MainActivity extends SafeActivity {

  @Override
  protected void safeOnCreate(Bundle savedInstanceState) throws NoConnectionDetailsException, PreferencesUninitializedException {

    Preferences.init(getBaseContext());

    CookieSyncManager.createInstance(this);
    CookieSyncManager.getInstance().startSync();
    goNext();
  }

  @Override
  protected void safeOnResume() throws NoConnectionDetailsException, PreferencesUninitializedException {
    Preferences.init(getBaseContext());
    CookieSyncManager.getInstance().stopSync();
    goNext();
  }

  @Override
  protected void onPause() {
    super.onPause();
    CookieSyncManager.getInstance().sync();
  }

  private void login() {

    (new Thread(new SafeRunnable(this) {

      @Override
      public void safeRun() throws URISyntaxException, URISyntaxException, PreferencesUninitializedException, NoConnectionDetailsException, IOException, HelloRequiredException {
        Log.d("main", "hello and login");
        Preferences.Getter prefs = Preferences.get();
        (new Hello(getBaseContext())).hello();
        (new Login(prefs.academiaUrl(), getBaseContext())).login(prefs.username(), prefs.password());
      }
    })).start();
  }

  protected void goNext() throws NoConnectionDetailsException, PreferencesUninitializedException {
    login();
    Intent nxtActivityIntent = new Intent(this, SubjectsActivity.class);
    startActivity(nxtActivityIntent);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }
}
