package controllers;

import contentprovider.StopMonitoringProvider;
import parser.SiriDistanceExtension;
import parser.SiriFetcher;
import parser.SiriXmlParser;
import play.mvc.Controller;
import play.mvc.Result;
import uk.org.siri.siri.MonitoredStopVisitStructure;
import uk.org.siri.siri.Siri;
import uk.org.siri.siri.StopMonitoringDeliveryStructure;

import javax.xml.bind.JAXBException;
import java.net.URISyntaxException;

import static contentprovider.StopMonitoringProvider.*;

public class Application extends Controller {

    public static Result index() throws JAXBException, URISyntaxException {
        return doSomething();
    }

    public static Result doSomething() {
        StopMonitoringProvider provider = new StopMonitoringProvider();
        StopMonitoringProvider.Request req = provider.new Request(StopMonitoringProvider.SAMPLE_STOP_CODE);

        StopMonitoringDeliveryStructure s = provider.get(req);
        for (MonitoredStopVisitStructure stopVisitStructure : s.getMonitoredStopVisit()) {
            SiriDistanceExtension wrapper = null;
            try {
                wrapper = SiriXmlParser.getExtensionWrapper(
                        stopVisitStructure.getMonitoredVehicleJourney());
            } catch (JAXBException e) {
                e.printStackTrace();
            }
            System.out.print(wrapper.getPresentableDistance());
            return ok(wrapper.getPresentableDistance());
        }
        return ok();
    }
}


