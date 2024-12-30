package com.dms.tareosoft.presentation.activities.splash;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import com.dms.tareosoft.R;
import com.dms.tareosoft.base.BaseActivity;
import com.dms.tareosoft.presentation.activities.login.LoginActivity;
import com.dms.tareosoft.util.UtilsMethods;
import com.permissionx.guolindev.PermissionX;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * {@link SplashActivity} pantalla de presentación de la aplicación que dirige a la pantalla de login
 * luego de que el tiempo especificado transcurra.
 */

public class SplashActivity extends BaseActivity implements ISplashContract.View {

    private String TAG = SplashActivity.class.getSimpleName();

    @Inject
    SplashPresenter presenter;
    public static final int MILLIS_WAIT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        ButterKnife.bind(this);
        presenter.attachView(this);
        checkPermissionReadState();
    }

    @Override
    protected int getLayout() { return R.layout.activity_splash; }

    @Override
    public void showLogin(long time) {
        new Handler().postDelayed(() -> {
            SplashActivity.this.startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        },time);
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onDestroy(){
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public void viewMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_alert)
                .setTitle("Atencion")
                .setMessage(message)
                .setPositiveButton("Cerrar", (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void checkPermissionReadState() {
        Log.i(TAG, "checkPermissionReadState: ");
        PermissionX.init(this)
                .permissions(Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.ACCESS_WIFI_STATE)
                .onExplainRequestReason((scope, deniedList, beforeRequest) -> {
                    scope.showRequestReasonDialog(deniedList,
                            "Para un buen uso de la aplicación es necesario que habilite los permisos correspodientes",
                            "Aceptar",
                            "Cancelar");
                })

                .onForwardToSettings((scope, deniedList) -> {
                    scope.showForwardToSettingsDialog(deniedList,
                            "Para continuar con el uso de la aplicación es necesario que habilite los permisos de manera manual",
                            "Config. manual",
                            "Cancelar");
                })
                .request((allGranted, grantedList, deniedList) -> {
                    if (allGranted) {
                        presenter.setImei(UtilsMethods.getIMEI(this));
                        //presenter.onSplashDone();
                    }
                });
    }
}
