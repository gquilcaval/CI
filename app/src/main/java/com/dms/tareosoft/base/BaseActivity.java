package com.dms.tareosoft.base;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dms.tareosoft.App;
import com.dms.tareosoft.R;
import com.dms.tareosoft.injection.ActivityComponent;
import com.dms.tareosoft.injection.DaggerActivityComponent;
import com.dms.tareosoft.util.CustomDialog;
import com.dms.tareosoft.util.CustomToast;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity implements IBaseContract.View {
    private ActivityComponent activityComponent;
    private ProgressDialog progress;
    private final Handler handler = new Handler();
    private ProgressDialog horizontalProgressDialog;
    private Boolean erroresDetallados = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());

        ButterKnife.bind(this);
        activityComponent = DaggerActivityComponent.builder().applicationComponent(App.get(this).getAppComponent()).build();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//TODO save state
    }

    protected abstract int getLayout();

    public ActivityComponent getActivityComponent() {
        return activityComponent;
    }

    public void clearDaggerComponent() {
        App.get(this).clearDaggerComponent();
    }

    @Override
    public void showProgressbar(String titulo, String mensaje) {
        progress = ProgressDialog.show(this, titulo, mensaje, true);
        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
            }
        }, 60);
    }

    @Override
    public void hiddenProgressbar() {
        if (progress != null) {
            progress.dismiss();
        }
    }

    @Override
    public void updateProgressbar(String mensaje) {
        if (progress != null) {
            progress.setMessage(mensaje);
        }
    }

    @Override
    public void showProgressPercentage(String titulo, String mensaje) {
        horizontalProgressDialog = new ProgressDialog(this);
        horizontalProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        horizontalProgressDialog.setTitle(titulo);
        horizontalProgressDialog.setMessage(mensaje);
        horizontalProgressDialog.setCancelable(false);
        horizontalProgressDialog.setMax(100);
        horizontalProgressDialog.show();
    }

    @Override
    public void hiddenProgressPercentage() {
        if (horizontalProgressDialog != null) {
            getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                }
            }, 60);

            horizontalProgressDialog.dismiss();
            horizontalProgressDialog.setMessage("");
        }
    }

    @Override
    public void updatePercentage(int progress, String message) {
        horizontalProgressDialog.setProgress(progress);
        horizontalProgressDialog.setMessage(message);
    }

    @Override
    public void showSuccessMessage(String mensaje) {
        hiddenProgressbar();
        hiddenProgressPercentage();

        new CustomToast.Builder(this, getLayoutInflater(), mensaje)
                .setBackgroundColor(this.getResources().getColor(R.color.colorSuccess))
                .setIcon(this.getResources().getDrawable(R.drawable.ic_done_all))
                .build().show();
    }

    @Override
    public void showWarningMessage(String mensaje) {
        hiddenProgressbar();
        hiddenProgressPercentage();
        new CustomToast.Builder(this, getLayoutInflater(), mensaje).build().show();
    }

    @Override
    public void showErrorMessage(String mensaje, String mensajeDetallado) {
        hiddenProgressbar();
        hiddenProgressPercentage();
        if (erroresDetallados) {
            new CustomDialog.Builder(this)
                    .setTitle(getMessage(R.string.title_error))
                    .setMessage(mensajeDetallado)
                    .setIcon(R.drawable.ic_help_outline)
                    .build().show();
        } else {
            new CustomToast.Builder(this, getLayoutInflater(), mensaje)
                    .setBackgroundColor(this.getResources().getColor(R.color.colorError))
                    .setIcon(this.getResources().getDrawable(R.drawable.ic_error))
                    .setDuration(Toast.LENGTH_LONG)
                    .build().show();
        }
    }

    public Handler getHandler() {
        return handler;
    }

    @Override
    public String getMessage(int id) {
        return getString(id);
    }

    @Override
    public void setErroresDetallados(Boolean erroresDetallados) {
        this.erroresDetallados = erroresDetallados;
    }

    protected void buildComponent() {
        activityComponent = DaggerActivityComponent.builder()
                .applicationComponent(App.getInstance().getAppComponent())
                .build();
    }
}