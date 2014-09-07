package tasks;

import contentproviders.StopMonitoringProvider;
import parsers.SiriDistanceExtension;
import parsers.SiriXmlParser;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;
import uk.org.siri.siri.MonitoredStopVisitStructure;
import uk.org.siri.siri.MonitoredVehicleJourneyStructure;
import uk.org.siri.siri.StopMonitoringDeliveryStructure;

import javax.xml.bind.JAXBException;
import java.util.concurrent.TimeUnit;

public class StopMonitorTask extends SelfRetryTask {

  public static class Params {
    final int stopCode;
    final String lineRef;
    final int stopsAway;

    public Params(int stopCode, String lineRef, int stopsAway) {
      this.stopCode = stopCode;
      this.lineRef = lineRef;
      this.stopsAway = stopsAway;
    }
  }

  public static interface Listener {
    public void onFinish();
  }

  private Listener mListener;

  private Params mParams;

  public void setListener(Listener l) {
    mListener = l;
  }

  public StopMonitorTask(Params params) {
    mParams = params;
  }

  @Override
  protected FiniteDuration runAndRetry() {
    System.out.println(toString() + ":run=>" + System.currentTimeMillis()/1000);

    StopMonitoringProvider provider = new StopMonitoringProvider();

    StopMonitoringDeliveryStructure stopMonitoringDeliveryStructure =  provider
        .get(new StopMonitoringProvider.Request(mParams.stopCode, mParams.lineRef));

    FiniteDuration duration = isArriving(stopMonitoringDeliveryStructure, mParams) ?
        DURATION_NONE : Duration.create(30, TimeUnit.SECONDS);

    if (duration == DURATION_NONE && mListener != null) {
      mListener.onFinish();
    }

    return duration;
  }

  private static boolean isArriving(
      StopMonitoringDeliveryStructure stopMonitoringDeliveryStructure,
      Params params) {

    System.out.println(stopMonitoringDeliveryStructure.getMonitoredStopVisit().get(0)
        .getMonitoredVehicleJourney().getMonitoredCall().getStopPointName().getValue());
    for (MonitoredStopVisitStructure stopVisitStructure :
        stopMonitoringDeliveryStructure.getMonitoredStopVisit()) {

      MonitoredVehicleJourneyStructure vehicleJourneyStructure =
          stopVisitStructure.getMonitoredVehicleJourney();

      if (isArriving(vehicleJourneyStructure, params)) {
        System.out.println("isArriving");
        return true;
      }
    }

    return false;
  }

  private static boolean isArriving(
      MonitoredVehicleJourneyStructure vehicleJourneyStructure,
      Params params) {
    try {
      SiriDistanceExtension distanceExtension =
          SiriXmlParser.getExtensionWrapper(vehicleJourneyStructure);

      System.out.println("stopsFromCall = " + distanceExtension.getStopsFromCall());

      if (distanceExtension.getStopsFromCall() <= params.stopsAway) {
        return true;
      }
    } catch (JAXBException e) {
      e.printStackTrace();
    }
    return false;
  }

}
