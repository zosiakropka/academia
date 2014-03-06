package pl.killerapps.academia.activities;

import pl.killerapps.academia.R;
import pl.killerapps.academia.activities.subject.SubjectsActivity;
import pl.killerapps.academia.utils.Preferences;
import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.webkit.CookieSyncManager;
import pl.killerapps.academia.utils.exceptions.FaultyConnectionDetailsException;
import pl.killerapps.academia.utils.exceptions.PreferencesUninitializedException;
import pl.killerapps.academia.utils.safe.SafeActivity;

public class MainActivity extends SafeActivity {

  @Override
  protected void safeOnCreate(Bundle savedInstanceState) throws FaultyConnectionDetailsException, PreferencesUninitializedException {

    CookieSyncManager.createInstance(this);
    CookieSyncManager.getInstance().startSync();
  }

  @Override
  protected void safeOnResume() throws FaultyConnectionDetailsException, PreferencesUninitializedException {
    CookieSyncManager.getInstance().stopSync();
    Preferences.init(getBaseContext());
    Intent nxtActivityIntent = new Intent(this, SubjectsActivity.class);
    startActivity(nxtActivityIntent);
  }

  @Override
  protected void onPause() {
    super.onPause();
    CookieSyncManager.getInstance().sync();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }
}
