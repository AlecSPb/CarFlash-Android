package com.carflash.user.others;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import com.carflash.user.samwork.Config;

public class MyBroadcastReceiver extends BroadcastReceiver {

//    Dialog dialog;

    @Override
    public void onReceive(final Context context, Intent intent) {

        String ride_status = intent.getExtras().getString("ride_status");
        String ride_id = intent.getExtras().getString("ride_id");

        Intent i = new Intent(Config.BROADCASTNAME);
        i.putExtra("ride_id",ride_id);
        i.putExtra("ride_status",ride_status);
        context.sendBroadcast(i);

        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context, notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
