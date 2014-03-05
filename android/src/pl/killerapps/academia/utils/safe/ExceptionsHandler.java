package pl.killerapps.academia.utils.safe;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.conn.HttpHostConnectException;
import pl.killerapps.academia.activities.ConnectActivity;
import pl.killerapps.academia.api.command.authenticate.Hello;
import pl.killerapps.academia.api.command.authenticate.Login;
import pl.killerapps.academia.utils.exceptions.FaultyConnectionDetailsException;
import pl.killerapps.academia.utils.exceptions.HelloFailedException;
import pl.killerapps.academia.utils.exceptions.HelloRequiredException;
import pl.killerapps.academia.utils.exceptions.LoginFailedException;
import pl.killerapps.academia.utils.exceptions.LoginRequiredException;
import pl.killerapps.academia.utils.exceptions.PreferencesUninitializedException;

/**
 *
 * @author zosia
 */
public class ExceptionsHandler {

  public static void handleHttpHostConnect(Activity activity, HttpHostConnectException ex) {
    Log.e("HttpHostConnect", ex.getMessage(), ex);
    throw new UnsupportedOperationException("handleHttpHostConnect");
    //startActivity(activity, ???.class);
  }

  public static void handleIO(Activity activity, IOException ex) {
    Log.e("IOException", ex.getMessage(), ex);
    throw new UnsupportedOperationException("handleIO");
    //startActivity(activity, ???.class);
  }

  /**
   * @todo Collect missing preferences
   * @param activity
   * @param ex
   */
  public static void handlePreferencesUninitialized(Activity activity, PreferencesUninitializedException ex) {
    Log.e("PreferencesUninitialized", ex.getMessage(), ex);
    handleNoConnectionDetails(activity, ex);
    //startActivity(activity, ???.class);
  }

  public static void handleNoConnectionDetails(Activity activity, Exception ex) {
    Log.e("NoConnectionDetails", ex.getMessage(), ex);
    startActivity(activity, ConnectActivity.class);
  }

  public static void handleHelloRequired(Activity activity, Exception ex) {
    Log.e("HelloRequired", ex.getMessage(), ex);
    try {
      (new Hello(activity.getBaseContext())).hello();
    } catch (PreferencesUninitializedException ex1) {
      handlePreferencesUninitialized(activity, ex1);
    } catch (URISyntaxException ex1) {
      handleNoConnectionDetails(activity, ex1);
    } catch (FaultyConnectionDetailsException ex1) {
      handleNoConnectionDetails(activity, ex1);
    } catch (IOException ex1) {
      handleIO(activity, ex1);
    } catch (HelloFailedException ex1) {
      handleHelloFailed(activity, ex1);
    }
    activity.finish();
  }

  public static void handleHelloFailed(Activity activity, HelloFailedException ex) {
    Log.e("HelloFailedException", ex.getMessage(), ex);
    activity.finish();
  }

  static void handleLoginRequired(SafeActivity activity, LoginRequiredException ex) {
    Log.e("LoginRequiredException", ex.getMessage(), ex);
    try {
      try {
        (new Login(activity.getBaseContext())).login();
      } catch (HelloRequiredException ex1) {
        Logger.getLogger(ExceptionsHandler.class.getName()).log(Level.SEVERE, null, ex1);
      } catch (PreferencesUninitializedException ex1) {
        Logger.getLogger(ExceptionsHandler.class.getName()).log(Level.SEVERE, null, ex1);
      } catch (IOException ex1) {
        Logger.getLogger(ExceptionsHandler.class.getName()).log(Level.SEVERE, null, ex1);
      } catch (FaultyConnectionDetailsException ex1) {
        Logger.getLogger(ExceptionsHandler.class.getName()).log(Level.SEVERE, null, ex1);
      }
    } catch (URISyntaxException ex1) {
      Logger.getLogger(ExceptionsHandler.class.getName()).log(Level.SEVERE, null, ex1);
    }
    activity.finish();
  }

  public static void handleLoginFailed(SafeActivity activity, LoginFailedException ex) {
    Log.e("LoginFailedException", ex.getMessage(), ex);
    activity.finish();
  }

  private static void startActivity(Activity activity, Class<?> cls) {
    activity.startActivity(new Intent(activity, cls));
  }
}
