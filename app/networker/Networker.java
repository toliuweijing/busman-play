package networker;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

/**
 * Created by weijingliu on 9/1/14.
 */
public class Networker {

    static final private Networker sNetworker = new Networker();

    private DefaultHttpClient mHttpClient = new DefaultHttpClient();

    public static Networker get() {
        return sNetworker;
    }

    public String executeGet(URI uri) throws IOException {
        HttpGet request = new HttpGet(uri);
        String buffer = "";
        HttpResponse response = mHttpClient.execute(request);
        BufferedReader br = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));
        String line = "";
        while ((line = br.readLine()) != null) {
            buffer += line;
        }
        return buffer;
    }

}

