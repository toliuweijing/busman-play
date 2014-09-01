package controllers;

import com.google.common.collect.Lists;
import contentproviders.StopMonitoringProvider;
import org.apache.commons.lang3.StringUtils;
import parsers.SiriDistanceExtension;
import parsers.SiriXmlParser;
import play.mvc.Controller;
import play.mvc.Result;
import uk.org.siri.siri.MonitoredStopVisitStructure;
import uk.org.siri.siri.MonitoredVehicleJourneyStructure;
import uk.org.siri.siri.StopMonitoringDeliveryStructure;

import javax.xml.bind.JAXBException;
import java.net.URISyntaxException;
import java.util.List;

public class Application extends Controller {

    public static Result stopInfo(int stopCode) throws JAXBException, URISyntaxException {
        StopMonitoringProvider provider = new StopMonitoringProvider();
        StopMonitoringProvider.Request req = provider.new Request(StopMonitoringProvider.SAMPLE_STOP_CODE);

        StopMonitoringDeliveryStructure s = provider.get(req);
        List<String> results = Lists.newArrayList();
        String stopName = null;
        for (MonitoredStopVisitStructure stopVisitStructure : s.getMonitoredStopVisit()) {
            MonitoredVehicleJourneyStructure journey = stopVisitStructure.getMonitoredVehicleJourney();
            SiriDistanceExtension distance = SiriXmlParser.getDistanceExtension(journey);
            results.add(journey.getPublishedLineName().getValue() + " " + distance.getPresentableDistance());
            if (stopName == null) {
                stopName = journey.getMonitoredCall().getStopPointName().getValue();
            }
        }
        results.add(0, stopName);

        String ret = StringUtils.join(results, "\n");
        return ok(ret);
    }

}


