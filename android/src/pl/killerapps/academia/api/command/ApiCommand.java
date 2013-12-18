package pl.killerapps.academia.api.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONArray;
import org.json.JSONException;

public abstract class ApiCommand<Entity> extends ApiCommandBase {

    public ApiCommand(URI uri) {
        super(uri);
    }

    protected ApiCommand(String base_url, String method_path) throws URISyntaxException {
        super(base_url, method_path);
    }

    public Entity send_request_get_response() throws JSONException,
            ClientProtocolException, IOException {
        return send_request_get_response(new ArrayList<NameValuePair>());
    }

    /**
     *
     * @param params
     * @return
     * @throws JSONException
     * @throws IOException
     * @throws ClientProtocolException
     */
    public Entity send_request_get_response(final List<NameValuePair> params)
            throws JSONException, ClientProtocolException, IOException {
        String response = real_request(params);
        JSONArray json_array = new JSONArray(response);
        return process_json(json_array);
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

    protected String real_request(List<NameValuePair> params)
            throws ClientProtocolException, IOException {

        HttpPost httppost = new HttpPost();
        httppost.setEntity(new UrlEncodedFormEntity(params));

        HttpResponse response = raw_request_response(httppost);

        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            StringBuilder builder = new StringBuilder();
            HttpEntity entity = response.getEntity();
            InputStream content = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(content));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            return builder.toString();
        }

        return null;
    }

    protected void handleFailure(Exception ex) {
        ex.printStackTrace();
    }
}
