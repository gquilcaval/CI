package com.dms.tareosoft.presentation.activities.splash;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;

import com.dms.tareosoft.R;
import com.dms.tareosoft.base.BasePresenter;

import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.pojos.ListImei;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import javax.inject.Inject;

public class SplashPresenter extends BasePresenter<ISplashContract.View> implements ISplashContract.Presenter {

    private String TAG = SplashPresenter.class.getSimpleName();
    private ISplashContract.View view;
    private PreferenceManager preferenceManager;
    private String mImei;
    private FirebaseRemoteConfig firebaseRemoteConfig;
    @Inject
    public SplashPresenter(PreferenceManager preferenceManager) {
        this.preferenceManager = preferenceManager;
        this.firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
    }

    @Override
    public void attachView(ISplashContract.View mvpView) {
        this.view = mvpView;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public ISplashContract.View getView() {
        return view;
    }


    @Override
    public void setImei(String imei) {
        Log.e(TAG, "isRegistered imei: " + imei);
        mImei = imei;
        Log.e(TAG, "isRegistered mImei: " + mImei);
        if (!preferenceManager.getIsRegistered()) {
            obtainConfigFirebase();
        } else {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                onSplashDone();
            }, SplashActivity.MILLIS_WAIT);
        }
    }

    private void obtainConfigFirebase() {
        try {
            Log.d(TAG, "obtainConfigFirebase");
            firebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config);
            FirebaseRemoteConfigSettings remoteConfigSettings = new FirebaseRemoteConfigSettings.Builder()
                    .setMinimumFetchIntervalInSeconds(0)
                    .build();
            firebaseRemoteConfig.setConfigSettingsAsync(remoteConfigSettings);
            fetchRemoteConfigValues(firebaseRemoteConfig);
        } catch (Error | Exception e) {
            e.printStackTrace();
        }
    }

    private void fetchRemoteConfigValues(FirebaseRemoteConfig remoteConfig) {
        Log.e(TAG, "fetchRemoteConfigValues remoteConfig: " + remoteConfig.toString());
        remoteConfig.fetchAndActivate()
                .addOnCompleteListener(task -> {
                    if (getView() == null)
                        return;
                    if (task.isSuccessful()) {
                        Log.e(TAG, "fetchRemoteConfigValues isSuccessful: " + task.getResult());
                        processRemoteConfig(remoteConfig);
                    } else {
                        Log.e(TAG, "fetchRemoteConfigValues not isSuccessful: ");
                        getView().viewMessage("Ocurrio un problema al licenciar su dispositivo.\n" +
                                "Reinicie su dispositivo.\n" +
                                "Vuela a intentarlo");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "fetchRemoteConfigValues addOnFailureListener: " + e.getMessage());
                });
    }


    private void processRemoteConfig(FirebaseRemoteConfig remoteConfig) {
        Log.e(TAG, "processRemoteConfig remoteConfig: " + remoteConfig.toString());
        String listImei = remoteConfig.getString("listImei");
        Log.e(TAG, "processRemoteConfig listImei: " + listImei);
        if (!TextUtils.isEmpty(listImei)) {
            ArrayList<ListImei> jsonArray = new Gson().fromJson(listImei, new TypeToken<List<ListImei>>() {
            }.getType());
            Log.e(TAG, "processRemoteConfig jsonArray: " + jsonArray);
            boolean isRegistered = isRegistered(jsonArray);
            Log.e(TAG, "processRemoteConfig isRegistered: " + isRegistered);
            if (isRegistered) {
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    onSplashDone();
                }, SplashActivity.MILLIS_WAIT);
            } else {
                getView().viewMessage("Su dispositivo no se encuenta habilitado para el uso de este aplicativo.\n" +
                        "Comuniquese con DMS para brindarle una solución e indique el sgte número " + mImei + ", para poder validar su dispositivo.");
            }
        } else {
            obtainConfigFirebase();
        }
    }

    private boolean isRegistered(ArrayList<ListImei> jsonArray) {
        if (jsonArray != null && jsonArray.size() > 0) {
            Log.e(TAG, "isRegistered jsonArray: " + jsonArray.toString());
            for (ListImei imei : jsonArray) {
                Log.e(TAG, "isRegistered imei: " + imei);
                if (!TextUtils.isEmpty(imei.getImei())) {
                    if (mImei.equalsIgnoreCase(imei.getImei())) {
                        Log.e(TAG, "isRegistered registro exitoso del imei: " + imei.getImei());
                        preferenceManager.setIsRegistered(true);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void onSplashDone() {
        getView().showLogin(2000);
    }
}
