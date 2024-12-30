package com.dms.tareosoft.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.dms.tareosoft.R;
import com.dms.tareosoft.injection.ActivityComponent;
import com.dms.tareosoft.util.CustomDialog;
import com.dms.tareosoft.util.CustomToast;

import java.util.List;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment implements IBaseContract.View {

    private ProgressDialog progress;
    private ProgressDialog horizontalProgressDialog;
    private Boolean erroresDetallados = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupVariables();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), container, false);
        ButterKnife.bind(this, view);
        initEvents();
        return view;
    }

    protected BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    protected ActivityComponent getActivityComponent() {
        return getBaseActivity().getActivityComponent();
    }

    protected abstract void setupVariables();

    protected abstract void setupView();

    protected abstract int getLayout();

    protected abstract void initEvents();

    @Override
    public void showProgressbar(String titulo, String mensaje) {
        progress = ProgressDialog.show(getContext(), titulo, mensaje, true);
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
        horizontalProgressDialog = new ProgressDialog(getContext());
        horizontalProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        horizontalProgressDialog.setTitle(titulo);
        horizontalProgressDialog.setMessage(mensaje);
        horizontalProgressDialog.setCancelable(false);
        horizontalProgressDialog.setMax(100);
        horizontalProgressDialog.setButton("Ok", (dialogInterface, i) -> {
            dialogInterface.dismiss();
        });
        horizontalProgressDialog.show();
    }

    @Override
    public void hiddenProgressPercentage() {
        if (horizontalProgressDialog != null) {
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
        new CustomToast.Builder(getActivity().getApplicationContext(), getLayoutInflater(), mensaje)
                .setBackgroundColor(this.getResources().getColor(R.color.colorSuccess))
                .setIcon(this.getResources().getDrawable(R.drawable.ic_done_all))
                .build().show();
    }

    @Override
    public void showWarningMessage(String mensaje) {
        hiddenProgressbar();
        hiddenProgressPercentage();
        new CustomToast.Builder(getActivity().getApplicationContext(), getLayoutInflater(), mensaje).build().show();
    }

    @Override
    public void showErrorMessage(String mensaje, String mensajeDetallado) {
        hiddenProgressbar();
        hiddenProgressPercentage();

        if (erroresDetallados) {
            new CustomDialog.Builder(getActivity())
                    .setTitle(getMessage(R.string.title_error))
                    .setMessage(mensajeDetallado)
                    .setTheme(R.style.AppTheme_Dialog_Error)
                    .setIcon(R.drawable.ic_error)
                    .build().show();
        } else {
            new CustomToast.Builder(getActivity().getApplicationContext(), getLayoutInflater(), mensaje)
                    .setBackgroundColor(this.getResources().getColor(R.color.colorError))
                    .setIcon(this.getResources().getDrawable(R.drawable.ic_error))
                    .setDuration(Toast.LENGTH_LONG)
                    .build().show();
        }
    }

    @Override
    public String getMessage(int id) {
        return getResources().getString(id); //getContext().getResources().getString(...)
    }

    @Override
    public void setErroresDetallados(Boolean erroresDetallados) {
        this.erroresDetallados = erroresDetallados;
    }
}