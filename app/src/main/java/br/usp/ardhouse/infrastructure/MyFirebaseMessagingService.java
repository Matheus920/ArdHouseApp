package br.usp.ardhouse.infrastructure;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.google.firebase.messaging.RemoteMessage;
import br.usp.ardhouse.activity.MainActivity;
import br.usp.R;

public class MyFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.w("newToken", token);
        getSharedPreferences("_", MODE_PRIVATE).edit().putString("fb", token).apply();
    }

    public static String getToken(Context context) {
        return context.getSharedPreferences("_", MODE_PRIVATE).getString("fb", "empty");
    }

    public void onMessageReceived(RemoteMessage remoteMessage) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String channelId = getString(R.string.default_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ardhousefundoverde)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}
