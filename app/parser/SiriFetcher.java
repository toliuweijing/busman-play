package parser;

import com.google.common.collect.Lists;
import networker.Networker;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import uk.org.siri.siri.Siri;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Created by toliuweijing on 7/10/14.
 */
public class SiriFetcher {

    public static final String HOST_OBANYC_COM = "api.prod.obanyc.com";
    public static final String QUERY_STOP_MONITORING_XML = "/api/siri/stop-monitoring.xml";
    public static final int PORT = 80;
    public static final String VALUE_KEY = "cfb3c75b-5a43-4e66-b7f8-14e666b0c1c1";
    public static final String PARAM_KEY = "key";
    public static final String PARAM_MONITORING_REF = "MonitoringRef";
    public static final String SAMPLE_STOP_CODE = "300067";

    private SiriXmlParser mXmlParser;

    public SiriFetcher() throws JAXBException {
        mXmlParser = new SiriXmlParser();
    }

    public Siri getSiri(URI uri) throws JAXBException {
        String response = null;
        try {
            response = Networker.get().executeGet(uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mXmlParser.process(response);
    }

    public Siri getStopMonitoringSample() throws JAXBException, URISyntaxException {
        return getStopMonitoring(SAMPLE_STOP_CODE);
    }

    public Siri getStopMonitoring(String stopCode) throws JAXBException, URISyntaxException {
        ArrayList<BasicNameValuePair> params = Lists.newArrayList(
                new BasicNameValuePair(PARAM_KEY, VALUE_KEY),
                new BasicNameValuePair(PARAM_MONITORING_REF, stopCode)
        );

        URI uri = URIUtils.createURI(
                "http",
                HOST_OBANYC_COM,
                PORT,
                QUERY_STOP_MONITORING_XML,
                URLEncodedUtils.format(params, "UTF-8"),
                null);

        return getSiri(uri);
    }

}
