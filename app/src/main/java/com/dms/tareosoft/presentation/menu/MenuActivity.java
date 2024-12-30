package com.dms.tareosoft.presentation.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.dms.tareosoft.R;
import com.dms.tareosoft.base.BaseActivity;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.presentation.acceso.consulta.ConsultarAccesoFragment;
import com.dms.tareosoft.presentation.acceso.registro.RegistroAccesoFragment;
import com.dms.tareosoft.presentation.activities.login.LoginActivity;
import com.dms.tareosoft.presentation.asistencia.consulta.ConsultarAsistenciaFragment;
import com.dms.tareosoft.presentation.asistencia.registro.RegistroAsistenciaActivity;
import com.dms.tareosoft.presentation.fragments.settings.SettingActivity;
import com.dms.tareosoft.presentation.fragments.sync.SyncFragment;
import com.dms.tareosoft.presentation.incidencia.RegistroIncidenciaFragment;
import com.dms.tareosoft.presentation.incidencia.consulta.ConsultarIncidenciaFragment;
import com.dms.tareosoft.presentation.tareoConsulta.TareoConsultaFragment;
import com.dms.tareosoft.presentation.tareo_control.ListaTareoControlFragment;
import com.dms.tareosoft.presentation.tareo_finalizar.PorFinalizarFragment;
import com.dms.tareosoft.presentation.tareo_principal.ListaTareoFragment;
import com.dms.tareosoft.presentation.tareo_resultado.ListaResultadosFragment;
import com.dms.tareosoft.presentation.tareoReubicar.PagerReubicarFragment;
import com.dms.tareosoft.util.Constants;
import com.dms.tareosoft.util.CustomDialog;
import com.google.android.material.navigation.NavigationView;

import javax.inject.Inject;

import butterknife.BindView;

public class MenuActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.toolbar_app)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.nav_view)
    NavigationView navView;

    @Inject
    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("AQUI","onCreate");
        getActivityComponent().inject(this);

        Toolbar toolbar = findViewById(R.id.toolbar_app);
        setSupportActionBar(toolbar);

        setupNavigation();
        validarMenu();
        addFragment(new ListaTareoFragment());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.INTENT_SETTINGS) {
                reiniciarActivity();
            }
        }
    }

    private void reiniciarActivity() {
        Log.d("AQUI","reiniciarActivity");
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    private void validarMenu() {
        if (!preferenceManager.isAdmin()) {
            Menu nav_Menu = navView.getMenu();
            nav_Menu.findItem(R.id.menu_config).setVisible(false);
        }

        if (!preferenceManager.getMostrarModuloAsistencia()) {
            Menu menuNav=navView.getMenu();
            menuNav.findItem(R.id.menu_registro_asistencia).setVisible(false);
            menuNav.findItem(R.id.menu_consulta_asistencia).setVisible(false);
        }

        if (!preferenceManager.getMostrarModuloIncidencia()) {
            Menu menuNav=navView.getMenu();
            menuNav.findItem(R.id.menu_registro_incidencia).setVisible(false);
            menuNav.findItem(R.id.menu_consulta_incidencia).setVisible(false);
        }

        if (!preferenceManager.getMostrarModuloAcceso()) {
            Menu menuNav=navView.getMenu();
            menuNav.findItem(R.id.menu_registro_acceso).setVisible(false);
            menuNav.findItem(R.id.menu_consulta_acceso).setVisible(false);
        }
    }

    private void setupNavigation() {
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        setupHeaderView();
    }

    private void setupHeaderView() {
        navView.setNavigationItemSelectedListener(this);

        View hView = navView.getHeaderView(0); //Se debe obtener la vista del Header
        TextView tvUsuario = hView.findViewById(R.id.tv_username);
        TextView tvPerfil = hView.findViewById(R.id.tv_perfil);

        tvUsuario.setText(preferenceManager.getNombreUsuario());
        tvPerfil.setText(preferenceManager.getNombrePerfil());
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void onBackPressed() {
    }

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, MenuActivity.class);
        return intent;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menu_tareo) {
            addFragment(new ListaTareoFragment());
        } else if (id == R.id.menu_fin_tareo) {
            addFragment(new PorFinalizarFragment());
        } else if (id == R.id.menu_verificacion) {
            addFragment(new ListaTareoControlFragment());
        } else if (id == R.id.menu_resultado) {
            addFragment(new ListaResultadosFragment());
        } else if (id == R.id.menu_sync) {
            addFragment(new SyncFragment());
        } else if (id == R.id.menu_reubicar) {
            addFragment(new PagerReubicarFragment());
        } else if (id == R.id.menu_consulta) {
            addFragment(new TareoConsultaFragment());
        } else if (id == R.id.menu_close) {
            openLogoutDialog();
        } else if (id == R.id.menu_config) {
            startActivityForResult(new Intent(MenuActivity.this,
                    SettingActivity.class), Constants.INTENT_SETTINGS);
        } else if (id == R.id.menu_registro_asistencia) {
            addFragment(new RegistroAsistenciaActivity());
        } else if (id == R.id.menu_consulta_asistencia) {
            addFragment(new ConsultarAsistenciaFragment());
        }
        else if (id == R.id.menu_registro_incidencia) {
            addFragment(new RegistroIncidenciaFragment());
        }
        else if (id == R.id.menu_consulta_incidencia) {
            addFragment(new ConsultarIncidenciaFragment());
        }
        else if (id == R.id.menu_registro_acceso) {
            addFragment(new RegistroAccesoFragment());
        }
        else if (id == R.id.menu_consulta_acceso) {
            addFragment(new ConsultarAccesoFragment());
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void addFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_main, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    //.addToBackStack(null)
                    .commit();
        }
    }

    private void openLogoutDialog() {
        new CustomDialog.Builder(MenuActivity.this)
                .setTitle(getString(R.string.dialog_logout_title))
                .setMessage(getString(R.string.dialog_logout_message))
                .setPositiveButtonLabel(getString(R.string.label_yes))
                .setNegativeButtonLabel(getString(R.string.label_no))
                .setPositiveButtonlistener(() -> {
                    preferenceManager.removeUser();
                    startActivity(LoginActivity.newInstance(getApplicationContext()));
                    finish();
                })
                .build().show();
    }

}