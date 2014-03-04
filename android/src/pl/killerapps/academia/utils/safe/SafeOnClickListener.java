package pl.killerapps.academia.utils.safe;

import android.view.View;
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
public abstract class SafeOnClickListener implements View.OnClickListener {

  SafeActivity activity;

  public String getActivityName() {
    return activity.getActivityName();
  }

  public SafeOnClickListener(SafeActivity activity) {
    this.activity = activity;
  }

  public void onClick(View arg0) {
    try {
      safeOnClick(arg0);
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
    } catch (PreferencesUninitializedException ex) {
      ExceptionsHandler.handlePreferencesUninitialized(activity, ex);
    } catch (HelloFailedException ex) {
      ExceptionsHandler.handleHelloFailed(activity, ex);
    }
  }

  public abstract void safeOnClick(View arg0) throws PreferencesUninitializedException, FaultyConnectionDetailsException, URISyntaxException, MalformedURLException, IOException, HelloRequiredException, HttpHostConnectException, HelloFailedException;
}
