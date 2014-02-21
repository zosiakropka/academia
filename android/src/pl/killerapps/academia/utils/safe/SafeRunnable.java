/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.killerapps.academia.utils.safe;

import android.app.Activity;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import org.apache.http.conn.HttpHostConnectException;
import pl.killerapps.academia.utils.exceptions.HelloRequiredException;
import pl.killerapps.academia.utils.exceptions.NoConnectionDetailsException;
import pl.killerapps.academia.utils.exceptions.PreferencesUninitializedException;

/**
 *
 * @author zosia
 */
public abstract class SafeRunnable implements Runnable {

  Activity activity;

  public SafeRunnable(Activity activity) {
    this.activity = activity;
  }

  public void run() {
    try {
      safeRun();
    } catch (PreferencesUninitializedException ex) {
      ExceptionsHandler.handlePreferencesUninitialized(activity, ex);
    } catch (HttpHostConnectException ex) {
      ExceptionsHandler.handleHttpHostConnect(activity, ex);
    } catch (NoConnectionDetailsException ex) {
      ExceptionsHandler.handleNoConnectionDetails(activity, ex);
    } catch (URISyntaxException ex) {
      ExceptionsHandler.handleNoConnectionDetails(activity, ex);
    } catch (MalformedURLException ex) {
      ExceptionsHandler.handleNoConnectionDetails(activity, ex);
    } catch (HelloRequiredException ex) {
      ExceptionsHandler.handleHelloRequiredException(activity, ex);
    } catch (IOException ex) {
      ExceptionsHandler.handleIO(activity, ex);
    }
  }

  public abstract void safeRun() throws PreferencesUninitializedException, NoConnectionDetailsException, URISyntaxException, MalformedURLException, IOException, HelloRequiredException, HttpHostConnectException;
}
