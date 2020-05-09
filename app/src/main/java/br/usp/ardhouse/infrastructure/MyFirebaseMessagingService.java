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

/*
    Essa classe é responsável por gerenciar as notificações recebidas pelo Firebase.
 */
public class MyFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    // Quando um novo token é gerado, ele é salvo nas configurações do celular
    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.w("newToken", token);
        getSharedPreferences("_", MODE_PRIVATE).edit().putString("fb", token).apply();
    }

    // Quando uma mensagem é recebida é montada a notificação para ser exibida ao usuário
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
