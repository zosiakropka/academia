package pl.killerapps.academia.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

abstract class ApiClient {

    public String base_url;
    protected String method_path;
    HttpClient client = new DefaultHttpClient();

    public ApiClient(String base_url, String method_path) {
        if (base_url.endsWith("/")) {
            this.base_url = (String) (base_url.subSequence(0, base_url.length() - 1));
        } else {
            this.base_url = base_url;
        }
        this.base_url = base_url;

        if (!method_path.startsWith("/")) {
            this.method_path = method_path;
        } else {
            this.method_path = "/" + method_path;
        }
    }

    protected JSONObject get_json_response(List<NameValuePair> params) {
        try {
            String response = send_request(params);
            JSONObject json_object = new JSONObject(response);
            return json_object;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String send_request(List<NameValuePair> params) {

        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(base_url + method_path);

        try {
            httppost.setEntity(new UrlEncodedFormEntity(params));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);

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

        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }
        return null;
    }
}
