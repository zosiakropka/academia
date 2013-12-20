package pl.killerapps.academia.api.command.authenticate;

import android.content.Context;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import android.util.Log;

import pl.killerapps.academia.api.command.ApiCommandBase;

/**
 *
 * @todo preferences.CsrfToken should fetch csrf token from server instead of
 * Login command
 * @author zosia
 */
public class Login extends ApiCommandBase {

    Context context;

    public Login(String base_url, Context context) throws URISyntaxException {
        super(base_url, "/auth/signin/");
        this.context = context;
    }

    public void login(final String username, final String password) {

        Runnable runnable = new Runnable() {

            public void run() {

                DefaultHttpClient httpclient = new DefaultHttpClient();

                try {
                    GetCsrfToken csrf_token_getter = new GetCsrfToken(base_url, context);
                    String csrftoken = csrf_token_getter.get();

                    if (csrftoken != null) {

                        HttpPost post = new HttpPost(uri);

                        Log.d("auth", "CSRF token: " + csrftoken);

                        HttpContext localContext = new BasicHttpContext();
                        BasicCookieStore cookieStore = new BasicCookieStore();
                        BasicClientCookie csrf_token_cookie = new BasicClientCookie("csrftoken", csrftoken);
                        cookieStore.addCookie(csrf_token_cookie);
                        csrf_token_cookie.setDomain(base_url);
                        csrf_token_cookie.setPath("/");

                        localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("username", username));
                        params.add(new BasicNameValuePair("password", password));

                        post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

                        httpclient.setCookieStore(cookieStore);
                        HttpResponse response = httpclient.execute(post);

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

                    }

                } catch (URISyntaxException ex) {
                    Log.e("login", "uri syntax", ex);
                } catch (IOException ex) {
                    Log.e("login", "io exception", ex);
                }

                // When HttpClient instance is no longer needed, 
                // shut down the connection manager to ensure
                // immediate deallocation of all system resources
                httpclient.getConnectionManager().shutdown();
            }
        };

        (new Thread(runnable)).start();

    }
}
