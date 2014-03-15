package pl.killerapps.academia.utils.safe;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import org.apache.http.conn.HttpHostConnectException;
import pl.killerapps.academia.utils.Log;
import pl.killerapps.academia.utils.exceptions.HelloRequiredException;
import pl.killerapps.academia.utils.exceptions.FaultyConnectionDetailsException;
import pl.killerapps.academia.utils.exceptions.HelloFailedException;
import pl.killerapps.academia.utils.exceptions.LoginRequiredException;
import pl.killerapps.academia.utils.exceptions.PreferencesUninitializedException;

/**
 *
 * @author zosia
 */
public abstract class SafeRunnable implements Runnable {

  SafeActivity activity;

  protected Log log;

  public String getActivityName() {
    return activity.getActivityName();
  }

  public SafeRunnable(SafeActivity activity) {
    this.activity = activity;
    log = new Log(activity.getActivityName());
  }

  public void run() {
    try {
      try {
        safeRun();
      } catch (FaultyConnectionDetailsException ex) {
        ExceptionsHandler.handleNoConnectionDetails(activity, ex);
      } catch (HttpHostConnectException ex) {
        ExceptionsHandler.handleHttpHostConnect(activity, ex);
      } catch (URISyntaxException ex) {
        ExceptionsHandler.handleNoConnectionDetails(activity, ex);
      } catch (MalformedURLException ex) {
        ExceptionsHandler.handleNoConnectionDetails(activity, ex);
      } catch (HelloRequiredException ex) {
        ExceptionsHandler.handleHelloRequired(activity, ex);
      } catch (IOException ex) {
        ExceptionsHandler.handleIO(activity, ex);
      } catch (HelloFailedException ex) {
        ExceptionsHandler.handleHelloFailed(activity, ex);
      } catch (LoginRequiredException ex) {
        ExceptionsHandler.handleLoginRequired(activity, ex);
      }
    } catch (PreferencesUninitializedException ex) {
      ExceptionsHandler.handlePreferencesUninitialized(activity, ex);
    }
  }

  public abstract void safeRun()
          throws PreferencesUninitializedException,
          FaultyConnectionDetailsException, URISyntaxException,
          MalformedURLException, IOException, HelloRequiredException,
          LoginRequiredException, HttpHostConnectException,
          HelloFailedException;
}
