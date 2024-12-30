package com.dms.tareosoft.domain;

import android.content.Context;

import androidx.annotation.NonNull;

import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.repository.DataSourceRemote;
import com.dms.tareosoft.util.Constants;
import com.dms.tareosoft.util.DateUtil;
import com.dms.tareosoft.util.MyCallback;
import com.dms.tareosoft.util.TextUtil;
import com.dms.tareosoft.util.UtilsMethods;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DateTimeManager {

    private final PreferenceManager preference;
    private final DataSourceRemote remote;
    private final Context context;

    @Inject
    public DateTimeManager(@NonNull PreferenceManager preferenceManager,
                           @NonNull DataSourceRemote remote, @NonNull Context context) {
        this.preference = preferenceManager;
        this.remote = remote;
        this.context = context;
    }

    public String getFechaSincronizada(String format) {
        Long fechaLongEquipo = DateUtil.longFechaHoraEquipo();
        fechaLongEquipo += preference.getMargenTiempo();
        return DateUtil.longToStringFormat(fechaLongEquipo, format);
    }

    public int getYear() {
        return Integer.parseInt(getFechaSincronizada("yyyy"));
    }

    public int getMonth() {
        return Integer.parseInt(getFechaSincronizada("MM"));
    }

    public int getDay() {
        return Integer.parseInt(getFechaSincronizada("dd"));
    }

    public int getHour() {
        return Integer.parseInt(getFechaSincronizada("HH"));
    }

    public int getMinute() {
        return Integer.parseInt(getFechaSincronizada("mm"));
    }

    public void verificarFechaEquipo(MyCallback callback) {
        Long tiempoTranscurrido = DateUtil.longFechaHoraEquipo() - DateUtil.stringToLongFormat(
                preference.getFechaUltimaValidacion(), Constants.F_LECTURA); //
        if (preference.getCambioManualFechaHora() && tiempoTranscurrido > (600000)) { //Mayor a 10 minutos
            sincronizarFechaHora(callback);
        } else {
            callback.onSucess();
        }
    }

    public String validarFechaHora() {
        String fechaServidor = preference.getFechaUltimaValidacion();
        if (!fechaServidor.equals(Constants.sinFecha)) {
            //getMargenTiempo : Diferencia que diferencia el servidor del equipo
            if (preference.getMargenTiempo() > TextUtil.convertIntToLong(preference.getMaximoMargen())) {
                preference.setFechaDesfasada(true);
                return String.format("El equipo movil se encuentra retrasado más de %s min.",
                        preference.getMaximoMargen());
            } else if (preference.getMargenTiempo() < -((preference.getMaximoMargen() * 60) * 1000)) {
                preference.setFechaDesfasada(true);
                return String.format("El equipo movil se encuentra adelantado más de %s min.",
                        preference.getMaximoMargen());
            } else {
                preference.setFechaDesfasada(false);
            }
        }
        return null;
    }

    public void sincronizarFechaHora(MyCallback callback) {
        //Actualizamos la fecha con el ultimo intento realizado
        preference.setFechaUltimaValidacion(DateUtil.obtenerFechaHoraEquipo(Constants.F_LECTURA));
        remote.obtenerFechaHora().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String fechaServidor = response.body();
                String fechaEquipo = DateUtil.obtenerFechaHoraEquipo(Constants.F_LECTURA);

                preference.setFechaHoraServidor(fechaServidor);
                preference.setMargenTiempo(DateUtil.stringToLongFormat(fechaServidor, Constants.F_FECHAHORA_WS) - DateUtil.stringToLongFormat(fechaEquipo, Constants.F_LECTURA));
                preference.setTiempoValido(true);

                callback.onSucess();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                preference.setTiempoValido(false);
                callback.onFailure(t.getMessage());
            }
        });
    }
}