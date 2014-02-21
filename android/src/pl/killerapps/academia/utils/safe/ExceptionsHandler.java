/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.killerapps.academia.utils.safe;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import java.io.IOException;
import java.net.URISyntaxException;
import pl.killerapps.academia.activities.ConnectActivity;
import pl.killerapps.academia.api.command.authenticate.Hello;
import pl.killerapps.academia.utils.exceptions.NoConnectionDetailsException;
import pl.killerapps.academia.utils.exceptions.PreferencesUninitializedException;

/**
 *
 * @author zosia
 */
public class ExceptionsHandler {

  public static void handleHelloRequiredException(Activity activity, Exception ex) {
    Log.e("HelloRequired", ex.getMessage(), ex);
    try {
      (new Hello(activity.getBaseContext())).hello();
    } catch (PreferencesUninitializedException ex1) {
      handlePreferencesUninitialized(activity, ex1);
    } catch (URISyntaxException ex1) {
      handleNoConnectionDetails(activity, ex1);
    } catch (NoConnectionDetailsException ex1) {
      handleNoConnectionDetails(activity, ex1);
    } catch (IOException ex1) {
      handleIO(activity, ex1);
    }
  }

  public static void handleHttpHostConnect(Activity activity, Exception ex) {
    Log.e("HttpHostConnect", ex.getMessage(), ex);
    throw new UnsupportedOperationException("handleHttpHostConnect");
    //startActivity(activity, ???.class);
  }

  public static void handleIO(Activity activity, Exception ex) {
    Log.e("IOException", ex.getMessage(), ex);
    throw new UnsupportedOperationException("handleIO");
    //startActivity(activity, ???.class);
  }

  public static void handlePreferencesUninitialized(Activity activity, Exception ex) {
    Log.e("PreferencesUninitialized", ex.getMessage(), ex);
    throw new UnsupportedOperationException("handlePreferencesUninitialized");
    //startActivity(activity, ???.class);
  }

  public static void handleNoConnectionDetails(Activity activity, Exception ex) {
    Log.e("NoConnectionDetails", ex.getMessage(), ex);
    startActivity(activity, ConnectActivity.class);
  }

  private static void startActivity(Activity activity, Class<?> cls) {
    activity.startActivity(new Intent(activity, cls));
  }
}
