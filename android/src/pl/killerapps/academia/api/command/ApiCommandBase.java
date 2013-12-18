package pl.killerapps.academia.api.command;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

public abstract class ApiCommandBase {

    protected URI uri;
    HttpClient client = new DefaultHttpClient();

    protected ApiCommandBase(URI uri) {
        this.uri = uri;
    }

    protected ApiCommandBase(String _base_url, String _method_path) throws URISyntaxException {
        String base_url;
        String method_path;
        if (_base_url.endsWith("/")) {
            base_url = (String) (_base_url.subSequence(0, _base_url.length() - 1));
        } else {
            base_url = _base_url;
        }

        if (!_method_path.startsWith("/")) {
            method_path = "/" + _method_path;
        } else {
            method_path = _method_path;
        }
        uri = new URI("http://" + base_url + "/api" + method_path);
    }

    protected HttpResponse raw_request_response(HttpPost post) throws ClientProtocolException, IOException {
        post.setURI(uri);
        HttpResponse response = client.execute(post);
        return response;
    }

    protected void handleFailure(Exception ex) {
        ex.printStackTrace();
    }
}
