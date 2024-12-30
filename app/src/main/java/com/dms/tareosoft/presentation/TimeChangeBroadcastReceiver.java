package com.dms.tareosoft.presentation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.dms.tareosoft.data.PreferenceManager;

public class TimeChangeBroadcastReceiver extends BroadcastReceiver {

    public TimeChangeBroadcastReceiver() {}

    @Override
    public void onReceive(Context context, Intent intent) {
        PreferenceManager preferenceManager = new PreferenceManager(context);
        preferenceManager.setMargenTiempo(0);
        preferenceManager.setCambioManualFechaHora(true);
        preferenceManager.setTiempoValido(false);
    }

}
