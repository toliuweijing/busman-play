package notification;

import javapns.Push;
import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;
import javapns.notification.PushedNotification;
import javapns.notification.PushedNotifications;

public class NotificationCenter {
  public static void send(String message, String deviceToken) {

    try {
      PushedNotifications noti = Push.alert(
          message,
          "/Users/toliuweijing/Project/busman-play/apns/MyCertificates.p12",
          "toliuweijing",
          false,
          deviceToken);
      for (PushedNotification notification : noti) {
        if (notification.isSuccessful()) {
          System.out.println("sent");
        } else {
          System.out.println(notification.getException().toString());
        }
      }
    } catch (CommunicationException e) {
      e.printStackTrace();
    } catch (KeystoreException e) {
      e.printStackTrace();
    }

  }
}
