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

public abstract class ApiCommandAsync<Entity> extends ApiCommand<Entity> {

  protected boolean get = false;

  public ApiCommandAsync(String base_url, String method_path)
          throws URISyntaxException {
    super(base_url, method_path);
  }

  /**
   * This method should be implemented as final reaction for send request.
   *
   * @param response
   */
  public void on_response(Entity response) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

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
    thread = new Runnable() {

      public void run() {

        try {
          String response;
          if (get) {
            response = real_get(params);
          } else {
            response = real_post(params);
          }
          if (response != null) {
            JSONArray json_array = new JSONArray(response);

            Entity entity = process_json(json_array);
            on_response(entity);
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
