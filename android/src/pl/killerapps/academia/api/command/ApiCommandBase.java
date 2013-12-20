package pl.killerapps.academia.api.command;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;

public abstract class ApiCommandBase {

    protected URI uri;

    protected String base_url;
    protected String command_path;

    HttpClient client = new DefaultHttpClient();

    protected ApiCommandBase(String _base_url, String _command_path) throws URISyntaxException {
        if (_base_url.endsWith("/")) {
            base_url = (String) (_base_url.subSequence(0, _base_url.length() - 1));
        } else {
            base_url = _base_url;
        }

        if (!_command_path.startsWith("/")) {
            command_path = "/" + _command_path;
        } else {
            command_path = _command_path;
        }

        this.uri = new URI("http://" + base_url + "/api" + command_path);
    }

    protected HttpResponse raw_post(HttpPost post, HttpContext localContext) throws ClientProtocolException, IOException {
        post.setURI(uri);
        HttpResponse response = client.execute(post, localContext);
        return response;
    }

    protected HttpResponse raw_post(HttpPost post) throws ClientProtocolException, IOException {
        post.setURI(uri);
        HttpResponse response = client.execute(post);
        return response;
    }

    protected void handleFailure(Exception ex) {
        ex.printStackTrace();
    }
}
