package com.estimote.examples.demos.activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;
import com.estimote.examples.demos.R;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import java.util.List;
import java.util.UUID;
import com.nuance.speechkit.Audio;
import com.nuance.speechkit.Language;
import com.nuance.speechkit.Session;
import com.nuance.speechkit.Transaction;
import com.nuance.speechkit.TransactionException;
import java.util.concurrent.TimeUnit;



import static com.estimote.sdk.BeaconManager.MonitoringListener;

/**
 * Demo that shows how to use region monitoring. Two important steps are:
 * <ul>
 * <li>start monitoring region, in example in {@link #onResume()}</li>
 * <li>respond to monitoring changes by registering {@link MonitoringListener} in {@link BeaconManager}</li>
 * </ul>
 *
 * @author wiktor@estimote.com (Wiktor Gworek)
 */
public class NotifyDemoActivity extends BaseActivity {

  private static final int NOTIFICATION_ID = 123;

  private BeaconManager beaconManager;
  private NotificationManager notificationManager;
  private Region region;
  

  @Override protected int getLayoutResId() {
    return R.layout.notify_demo;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    //Beacon beacon = getIntent().getParcelableExtra(ListBeaconsActivity.EXTRAS_BEACON);
    region = new Region("regionId", UUID.fromString("b9407f30-f5f8-466e-aff9-25556b57fe6d"),23105,null);
    //region2 = new Region("regionId", UUID.fromString("b9407f30-f5f8-466e-aff9-25556b57fe6d"),23105,23011);
    notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    beaconManager = new BeaconManager(this);

    // Default values are 5s of scanning and 25s of waiting time to save CPU cycles.
    // In order for this demo to be more responsive and immediate we lower down those values.
    beaconManager.setBackgroundScanPeriod(TimeUnit.SECONDS.toMillis(1), 0);

    beaconManager.setMonitoringListener(new MonitoringListener() {
      @Override
      public void onEnteredRegion(Region region, List<Beacon> beacons) {
        postNotification("You are about to enter a construction zone!");
      }

      @Override
      public void onExitedRegion(Region region) {
        postNotification("You are in a safe zone now!");
      }
    });
  }

  @Override
  protected void onResume() {
    super.onResume();

    notificationManager.cancel(NOTIFICATION_ID);
    beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
      @Override
      public void onServiceReady() {
        beaconManager.startMonitoring(region);
      }
    });
  }

  @Override
  protected void onDestroy() {
    notificationManager.cancel(NOTIFICATION_ID);
    beaconManager.disconnect();
    super.onDestroy();
  }

  private void postNotification(String msg) {
    //intent = new Intent(this, TTSActivity.class);
    Intent notifyIntent = new Intent(NotifyDemoActivity.this, NotifyDemoActivity.class);
    notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    PendingIntent pendingIntent = PendingIntent.getActivities(
        NotifyDemoActivity.this,
        0,
        new Intent[]{notifyIntent},
        PendingIntent.FLAG_UPDATE_CURRENT);
    Notification notification = new Notification.Builder(NotifyDemoActivity.this)
        .setSmallIcon(R.drawable.beacon_gray)
        .setContentTitle("Estimote Notifications")
        .setContentText(msg)
        .setAutoCancel(true)
        .setContentIntent(pendingIntent)
        .build();
    notification.defaults |= Notification.DEFAULT_SOUND;
    notification.defaults |= Notification.DEFAULT_LIGHTS;
    notificationManager.notify(NOTIFICATION_ID, notification);

    TextView statusTextView = (TextView) findViewById(R.id.status);
    statusTextView.setText(msg);
    speakNotification(msg);
  }
  
  private void speakNotification(String notification) {
    Session session = Session.Factory.session(this, Configuration.SERVER_URI, Configuration.APP_KEY);
    Transaction.Options options = new Transaction.Options();
    options.setLanguage(new Language("eng-USA"));
    Transaction transaction = session.speakString(notification, options, new Transaction.Listener() {
      public void onAudio(Transaction transaction, Audio audio){}
      public void onSuccess(Transaction transaction, String s) {}
      public void onError(Transaction transaction, String s, TransactionException e) {}
    });
  }
}
