package pl.killerapps.academia.utils.safe;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import org.apache.http.conn.HttpHostConnectException;
import pl.killerapps.academia.utils.exceptions.HelloRequiredException;
import pl.killerapps.academia.utils.exceptions.FaultyConnectionDetailsException;
import pl.killerapps.academia.utils.exceptions.HelloFailedException;
import pl.killerapps.academia.utils.exceptions.PreferencesUninitializedException;

/**
 *
 * @author zosia
 */
public abstract class SafeActivity extends Activity {

  public String getActivityName() {
    Class<?> enclosingClass = getClass().getEnclosingClass();
    if (enclosingClass != null) {
      return enclosingClass.getName();
    } else {
      return getClass().getName();
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.i("createdActivity", getActivityName());
    try {
      safeOnCreate(savedInstanceState);
    } catch (FaultyConnectionDetailsException ex) {
      ExceptionsHandler.handleNoConnectionDetails(this, ex);
    } catch (HttpHostConnectException ex) {
      ExceptionsHandler.handleHttpHostConnect(this, ex);
    } catch (URISyntaxException ex) {
      ExceptionsHandler.handleNoConnectionDetails(this, ex);
    } catch (MalformedURLException ex) {
      ExceptionsHandler.handleNoConnectionDetails(this, ex);
    } catch (HelloRequiredException ex) {
      ExceptionsHandler.handleHelloRequired(this, ex);
    } catch (IOException ex) {
      ExceptionsHandler.handleIO(this, ex);
    } catch (PreferencesUninitializedException ex) {
      ExceptionsHandler.handlePreferencesUninitialized(this, ex);
    } catch (HelloFailedException ex) {
      ExceptionsHandler.handleHelloFailed(this, ex);
    }
  }

  protected void safeOnCreate(Bundle savedInstanceState) throws PreferencesUninitializedException, FaultyConnectionDetailsException, URISyntaxException, MalformedURLException, IOException, HelloRequiredException, HttpHostConnectException, HelloFailedException {
  }

  @Override
  protected void onResume() {
    super.onResume();
    Log.i("resumedActivity", getActivityName());
    try {
      safeOnResume();
    } catch (PreferencesUninitializedException ex) {
      ExceptionsHandler.handlePreferencesUninitialized(this, ex);
    } catch (HttpHostConnectException ex) {
      ExceptionsHandler.handleHttpHostConnect(this, ex);
    } catch (FaultyConnectionDetailsException ex) {
      ExceptionsHandler.handleNoConnectionDetails(this, ex);
    } catch (URISyntaxException ex) {
      ExceptionsHandler.handleNoConnectionDetails(this, ex);
    } catch (MalformedURLException ex) {
      ExceptionsHandler.handleNoConnectionDetails(this, ex);
    } catch (HelloRequiredException ex) {
      ExceptionsHandler.handleHelloRequired(this, ex);
    } catch (IOException ex) {
      ExceptionsHandler.handleIO(this, ex);
    } catch (HelloFailedException ex) {
      ExceptionsHandler.handleHelloFailed(this, ex);
    }
  }

  protected abstract void safeOnResume() throws PreferencesUninitializedException, FaultyConnectionDetailsException, URISyntaxException, MalformedURLException, IOException, HelloRequiredException, HttpHostConnectException, HelloFailedException;
}
