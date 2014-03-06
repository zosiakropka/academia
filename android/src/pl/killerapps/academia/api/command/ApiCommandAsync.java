package pl.killerapps.academia.api.command;

import android.util.Log;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import pl.killerapps.academia.utils.exceptions.FaultyConnectionDetailsException;
import pl.killerapps.academia.utils.exceptions.HelloRequiredException;
import pl.killerapps.academia.utils.exceptions.LoginRequiredException;
import pl.killerapps.academia.utils.exceptions.PreferencesUninitializedException;
import pl.killerapps.academia.utils.safe.SafeActivity;
import pl.killerapps.academia.utils.safe.SafeRunnable;

public abstract class ApiCommandAsync<Entity> extends ApiCommand<Entity> {

  protected boolean get = false;
  protected SafeActivity activity;

  public ApiCommandAsync(String method_path, SafeActivity activity)
          throws URISyntaxException, PreferencesUninitializedException, FaultyConnectionDetailsException {
    super(method_path);
    this.activity = activity;
  }

  /**
   * Implement a successful response callback.
   *
   * @param response
   */
  public abstract void on_response(Entity response);

  /**
   * Implement a successful response callback.
   */
  public abstract void on_failure();

  /**
   * Implementation of this method should process JSON response to extract
   * required data and return right Entity object representing data gathered
   * from HTTP response.
   *
   * @param json
   * @return Type
   */
  protected abstract Entity process_json(JSONArray json);

  public void send_request() {
    send_request(new ArrayList<NameValuePair>());
  }

  public void send_request(final List<NameValuePair> params) {
    Runnable thread;
    thread = new SafeRunnable(this.activity) {

      public void safeRun() throws HelloRequiredException, LoginRequiredException, URISyntaxException, PreferencesUninitializedException {

        try {
          Log.d("command", "Sending request.");
          String response;
          if (get) {
            response = real_get(params);
            Log.d("command", "get.");
          } else {
            response = real_post(params);
            Log.d("command", "post.");
          }
          if (response != null) {
            JSONArray json_array = new JSONArray(response);

            Entity entity = process_json(json_array);
            on_response(entity);
          } else {
            on_failure();
          }
        } catch (JSONException e) {
          Log.e("academia_api", e.getLocalizedMessage());
        } catch (ClientProtocolException e) {
          Log.e("academia_api", e.getLocalizedMessage());
        } catch (IOException e) {
          Log.e("academia_api", e.getLocalizedMessage());
        }
      }
    };
    (new Thread(thread)).start();
  }

}
