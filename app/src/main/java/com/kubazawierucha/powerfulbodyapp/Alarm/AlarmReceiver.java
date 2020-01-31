package com.kubazawierucha.powerfulbodyapp.Alarm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.kubazawierucha.powerfulbodyapp.Workout.WorkoutDetailsActivity;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int notificationId = intent.getIntExtra("notificationId", -1);
        String message = intent.getStringExtra("todo");

        Intent mainIntent = new Intent(context, WorkoutDetailsActivity.class);
        mainIntent.putExtra("workoutDayId", notificationId);
        PendingIntent contentIntent = PendingIntent.getActivity(context, notificationId, mainIntent, 0);

        NotificationManager myNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(android.R.drawable.ic_dialog_info)
        .setContentTitle("Reminder!")
        .setContentText(message)
        .setAutoCancel(true)
        .setContentIntent(contentIntent);


        if (Build.VERSION.SDK_INT >= 26) {
            String channelId = "workout_reminder_channel";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Workout reminder channel.",
                    NotificationManager.IMPORTANCE_HIGH);
            myNotificationManager.createNotificationChannel(channel);

            builder.setChannelId(channelId);
        }

        myNotificationManager.notify(notificationId, builder.build());
    }
}
