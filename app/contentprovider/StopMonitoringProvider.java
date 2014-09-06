package contentprovider;

import com.google.common.collect.Lists;
import networker.Networker;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import parser.SiriXmlParser;
import uk.org.siri.siri.Siri;
import uk.org.siri.siri.StopMonitoringDeliveryStructure;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by weijingliu on 9/1/14.
 */
public class StopMonitoringProvider extends ContentProvider<
    StopMonitoringProvider.Request,
    StopMonitoringDeliveryStructure> {

  public static final String HOST_OBANYC_COM = "api.prod.obanyc.com";
  public static final String QUERY_STOP_MONITORING_XML = "/api/siri/stop-monitoring.xml";
  public static final int PORT = 80;
  public static final String VALUE_KEY = "cfb3c75b-5a43-4e66-b7f8-14e666b0c1c1";
  public static final String PARAM_KEY = "key";
  public static final String PARAM_MONITORING_REF = "MonitoringRef";
  public static final String PARAM_LINE_REF = "LineRef";

  public static final int SAMPLE_STOP_CODE = 300067;
  public static final String SAMPLE_LINE_REF = "MTA NYCT_B9";
  private SiriXmlParser mXmlParser;

  public StopMonitoringProvider() {
    try {
      mXmlParser = new SiriXmlParser();
    } catch (JAXBException e) {
      e.printStackTrace();
    }
  }

  public static class Request {
    String stopCode;
    String lineRef;

    public Request(int stopCode, String lineRef) {
      this.stopCode = String.valueOf(stopCode);
      this.lineRef = lineRef;
    }

    URI siriRequest() {
      List<BasicNameValuePair> params = Lists.newArrayList(
          new BasicNameValuePair(PARAM_KEY, VALUE_KEY),
          new BasicNameValuePair(PARAM_MONITORING_REF, stopCode),
          new BasicNameValuePair(PARAM_LINE_REF, lineRef));

      URI uri = null;
      try {
        uri = new URIBuilder()
            .setScheme("http")
            .setHost(HOST_OBANYC_COM)
            .setPort(PORT)
            .setPath(QUERY_STOP_MONITORING_XML)
            .addParameter(PARAM_KEY, VALUE_KEY)
            .addParameter(PARAM_MONITORING_REF, stopCode)
            .addParameter(PARAM_LINE_REF, lineRef)
            .build();
      } catch (URISyntaxException e) {
        e.printStackTrace();
      }
      return uri;
    }
  }

  @Override
  public StopMonitoringDeliveryStructure get(Request req) {
    URI uri = req.siriRequest();
    try {
      String content = Networker.get().executeGet(uri);
      Siri siri = mXmlParser.process(content);
      // This is a container
      return siri.getServiceDelivery().getStopMonitoringDelivery().get(0);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (JAXBException e) {
      e.printStackTrace();
    }
    return null;
  }

}
