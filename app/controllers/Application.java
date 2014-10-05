package controllers;

import notification.NotificationCenter;
import tasks.StopMonitorTask;
import play.mvc.Controller;
import play.mvc.Result;

import javax.xml.bind.JAXBException;
import java.net.URISyntaxException;

import static contentproviders.StopMonitoringProvider.SAMPLE_STOP_CODE;
import static contentproviders.StopMonitoringProvider.SAMPLE_LINE_REF;

public class Application extends Controller {

  // A sanity check api to verify the communication channel functions correctly.
  public static Result ping(String notificationtoken) throws JAXBException, URISyntaxException {
    NotificationCenter.send("Connected", notificationtoken);
    return ok();
  }

  public static Result doSomething() {
    StopMonitorTask.Params params =
        new StopMonitorTask.Params(SAMPLE_STOP_CODE, SAMPLE_LINE_REF, 3);
    new StopMonitorTask(params).schedule(StopMonitorTask.DURATION_NOW);
    return ok();
  }

  /**
   * http://localhost:9000/stopmonitor/300067/MTA%20NYCT_B9/3
   * @param stopCode
   * @param lineRef
   * @param stopsAway
   * @return
   */
  public static Result stopMonitor(
      int stopCode,
      String lineRef,
      int stopsAway,
      final String deviceToken) {

    StopMonitorTask.Params params =
        new StopMonitorTask.Params(stopCode, lineRef, stopsAway);

    StopMonitorTask task = new StopMonitorTask(params);

    task.setListener(
        new StopMonitorTask.Listener() {
          @Override
          public void onFinish() {
            NotificationCenter.send("bus's approaching", deviceToken);
          }
        }
    );
    task.schedule(StopMonitorTask.DURATION_NOW);

    return ok();
  }

}


