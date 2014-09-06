package tasks;


import play.libs.Akka;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

import java.util.concurrent.TimeUnit;

public abstract class SelfRetryTask {

  public static final FiniteDuration DURATION_NOW = Duration.create(0, TimeUnit.MILLISECONDS);

  public static final FiniteDuration DURATION_NONE = null;

  /**
   * Schedule to run task asynchronously. Passing DURATION_NONE has no effects.
   */
  final public void schedule(FiniteDuration duration) {

    if (duration == DURATION_NONE) {
      return;
    }

    Akka.system().scheduler().scheduleOnce(
        duration,
        new Runnable() {
          @Override
          public void run() {
            FiniteDuration retry = SelfRetryTask.this.runAndRetry();
            schedule(retry);
          }
        },
        Akka.system().dispatcher());

  }

  /**
   * Used to customize the actual code to be run.
   * @return when should the task retry. Return DURATION_NONE to stop retrying.
   */
  protected abstract FiniteDuration runAndRetry();

}
