/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.killerapps.academia.utils.safe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    } catch (PreferencesUninitializedException ex) {
      ExceptionsHandler.handlePreferencesUninitialized(this, ex);
    } catch (HttpHostConnectException ex) {
      ExceptionsHandler.handleHttpHostConnect(this, ex);
    } catch (NoConnectionDetailsException ex) {
      ExceptionsHandler.handleNoConnectionDetails(this, ex);
    } catch (URISyntaxException ex) {
      ExceptionsHandler.handleNoConnectionDetails(this, ex);
    } catch (MalformedURLException ex) {
      ExceptionsHandler.handleNoConnectionDetails(this, ex);
    } catch (HelloRequiredException ex) {
      ExceptionsHandler.handleHelloRequiredException(this, ex);
    } catch (IOException ex) {
      ExceptionsHandler.handleIO(this, ex);
    }
  }

  protected void safeOnCreate(Bundle savedInstanceState) throws PreferencesUninitializedException, NoConnectionDetailsException, URISyntaxException, MalformedURLException, IOException, HelloRequiredException, HttpHostConnectException {

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
    } catch (NoConnectionDetailsException ex) {
      ExceptionsHandler.handleNoConnectionDetails(this, ex);
    } catch (URISyntaxException ex) {
      ExceptionsHandler.handleNoConnectionDetails(this, ex);
    } catch (MalformedURLException ex) {
      ExceptionsHandler.handleNoConnectionDetails(this, ex);
    } catch (HelloRequiredException ex) {
      ExceptionsHandler.handleHelloRequiredException(this, ex);
    } catch (IOException ex) {
      ExceptionsHandler.handleIO(this, ex);
    }
  }

  protected abstract void safeOnResume() throws PreferencesUninitializedException, NoConnectionDetailsException, URISyntaxException, MalformedURLException, IOException, HelloRequiredException, HttpHostConnectException;

}
