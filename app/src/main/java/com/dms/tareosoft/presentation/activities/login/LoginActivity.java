package com.dms.tareosoft.presentation.activities.login;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.dms.tareosoft.BuildConfig;
import com.dms.tareosoft.R;
import com.dms.tareosoft.base.BaseActivity;
import com.dms.tareosoft.presentation.TimeChangeBroadcastReceiver;
import com.dms.tareosoft.presentation.menu.MenuActivity;
import com.dms.tareosoft.presentation.fragments.settings.SettingActivity;
import com.google.android.material.button.MaterialButton;

import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements ILoginContract.View {

    @BindView(R.id.txt_usuario)
    EditText user_etxt;
    @BindView(R.id.txt_clave)
    EditText pass_etxt;
    @BindView(R.id.login_button)
    MaterialButton btnLogin;
    @BindView(R.id.tv_apk_version)
    TextView tvApkVersion;
    @Inject
    LoginPresenter presenter;

    IntentFilter s_intentFilter;

    TimeChangeBroadcastReceiver m_timeChangedReceiver;
    private MenuItem menuItemSetting;
    private MenuItem menuSetting;
    private Menu mMenu;
    Menu optionsMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);

        Toolbar toolbar = findViewById(R.id.toolbar_login);
        setSupportActionBar(toolbar);

        tvApkVersion.setText("v"+BuildConfig.VERSION_NAME);
        setTitle("");
        presenter.attachView(this);

        s_intentFilter = new IntentFilter();
        s_intentFilter.addAction(Intent.ACTION_TIME_CHANGED);

        m_timeChangedReceiver = new TimeChangeBroadcastReceiver();

        presenter.validarViewOrNotMenu();


    }

    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login, menu);
        if (presenter.validaDescargaExitosa()) {
            menuSetting = menu.findItem(R.id.action_settings).setVisible(false);// cambio momentaneo
        } else  {
            menuSetting = menu.findItem(R.id.action_settings).setVisible(true);// cambio momentaneo
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                menuItemSetting = item;
                menuItemSetting.setEnabled(true);
                startActivity(SettingActivity.newInstance(getApplicationContext()));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.login_button)
    public void onLoginClick() {
        presenter.login(user_etxt.getText().toString().trim(), pass_etxt.getText().toString().trim());
    }

    @Override
    public void goToMainMenu() {
        startActivity(MenuActivity.newInstance(getApplicationContext()));
        finish();
    }

    @Override
    public void bloqueoLogin() {
        btnLogin.setEnabled(false);
    }

    @Override
    public void activarLogin() {
        btnLogin.setEnabled(true);
    }

    @Override
    public void viewMenuConfig(boolean viewMenu) {
        if (menuSetting != null) {
            //menuSetting.setVisible(viewMenu); desabilitado momentaneo
        }
    }

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.validarModoTrabajo();
        if (menuItemSetting != null ) {
            Log.d("aqui", "onresume");
            if (presenter.validaDescargaExitosa()) {
                menuItemSetting.setVisible(false);
                menuSetting.setEnabled(false);
            } else {
                menuItemSetting.setVisible(true);
                menuItemSetting.setEnabled(true);
            }
        }
        registerReceiver(m_timeChangedReceiver, s_intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(m_timeChangedReceiver);
    }


}
