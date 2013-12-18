package pl.killerapps.academia.api.command.authenticate;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import pl.killerapps.academia.api.command.ApiCommandBase;

public class Login extends ApiCommandBase {

    String base_url;

    public Login(String base_url) throws URISyntaxException {
        super(base_url, "/api/login/");
    }

    public void execute(String login, String password) throws ClientProtocolException, IOException {
        DefaultHttpClient httpclient = new DefaultHttpClient();

        HttpPost httpost = new HttpPost();

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("username", "username"));
        params.add(new BasicNameValuePair("password", "password"));

        httpost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

        HttpResponse response = raw_request_response(httpost);
        HttpEntity entity = response.getEntity();

        System.out.println("Login form get: " + response.getStatusLine());
        if (entity != null) {
            entity.consumeContent();
        }

        System.out.println("Post logon cookies:");
        List<Cookie> cookies = httpclient.getCookieStore().getCookies();
        if (cookies.isEmpty()) {
            System.out.println("None");
        } else {
            for (int i = 0; i < cookies.size(); i++) {
                System.out.println("- " + cookies.get(i).toString());
            }
        }

	        // When HttpClient instance is no longer needed, 
        // shut down the connection manager to ensure
        // immediate deallocation of all system resources
        httpclient.getConnectionManager().shutdown();
    }
}
