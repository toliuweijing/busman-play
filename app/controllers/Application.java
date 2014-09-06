package controllers;

import tasks.StopMonitorTask;
import play.mvc.Controller;
import play.mvc.Result;

import javax.xml.bind.JAXBException;
import java.net.URISyntaxException;

import static contentprovider.StopMonitoringProvider.*;

public class Application extends Controller {

  public static Result index() throws JAXBException, URISyntaxException {
    return doSomething();
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
      int stopsAway) {
    StopMonitorTask.Params params =
        new StopMonitorTask.Params(stopCode, lineRef, stopsAway);
    new StopMonitorTask(params).schedule(StopMonitorTask.DURATION_NOW);
    return ok();
  }
}


