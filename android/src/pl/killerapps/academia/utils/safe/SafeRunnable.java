/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.killerapps.academia.utils.safe;

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

  SafeActivity activity;

  public String getActivityName() {
    return activity.getActivityName();
  }

  public SafeRunnable(SafeActivity activity) {
    this.activity = activity;
  }

  public void run() {
    try {
      safeRun();
    } catch (NoConnectionDetailsException ex) {
      ExceptionsHandler.handleNoConnectionDetails(activity, ex);
    } catch (HttpHostConnectException ex) {
      ExceptionsHandler.handleHttpHostConnect(activity, ex);
    } catch (URISyntaxException ex) {
      ExceptionsHandler.handleNoConnectionDetails(activity, ex);
    } catch (MalformedURLException ex) {
      ExceptionsHandler.handleNoConnectionDetails(activity, ex);
    } catch (HelloRequiredException ex) {
      ExceptionsHandler.handleHelloRequiredException(activity, ex);
    } catch (IOException ex) {
      ExceptionsHandler.handleIO(activity, ex);
    } catch (PreferencesUninitializedException ex) {
      ExceptionsHandler.handlePreferencesUninitialized(activity, ex);
    }
  }

  public abstract void safeRun() throws PreferencesUninitializedException, NoConnectionDetailsException, URISyntaxException, MalformedURLException, IOException, HelloRequiredException, HttpHostConnectException;
}
