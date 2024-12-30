package com.dms.tareosoft.presentation.activities.search;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dms.tareosoft.R;
import com.dms.tareosoft.annotacion.NivelConceptoTareo;
import com.dms.tareosoft.base.BaseActivity;
import com.dms.tareosoft.data.entities.ConceptoTareo;
import com.dms.tareosoft.presentation.adapter.SearchAdapter;
import com.dms.tareosoft.util.Constants;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.annotations.Nullable;

public class SearchActivity extends BaseActivity implements SearchContract.View,
        SearchView.OnQueryTextListener {

    private String TAG = SearchActivity.class.getSimpleName();

    @BindView(R.id.rv_concepto_tareo)
    RecyclerView recyclerConceptoTareo;

    @Inject
    SearchPresenter presenter;

    private int FKNivel = 0, fkPadre = 0;
    @NivelConceptoTareo
    private int concepto;
    private SearchView searchView;
    private SearchAdapter mAdapters;

    private BroadcastReceiver mScanReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String barcode = intent.getStringExtra(Constants.SCANNER_STRING);
            if (TextUtils.isEmpty(barcode)) {
                barcode = intent.getStringExtra(Constants.SCANNER_STRING_HONEY);
            }
            Log.e(TAG, "barcode: " + barcode);
            presenter.searchFilterCod(barcode);
            searchView.onActionViewExpanded();
            searchView.setQuery(barcode, false);
            searchView.clearFocus();
        }
    };

    @Override
    protected int getLayout() {
        return R.layout.activity_search;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        Toolbar toolbar = findViewById(R.id.toolbar_app);
        setToolbar(toolbar);
        Bundle parametros = this.getIntent().getExtras();
        FKNivel = parametros.getInt("fkNivel");
        fkPadre = parametros.getInt("fkPadre");
        concepto = parametros.getInt("contenido");
        Log.e(TAG, "fkNivel: " + FKNivel);
        Log.e(TAG, "fkPadre: " + fkPadre);
        Log.e(TAG, "concepto: " + concepto);

        toolbar.setTitle("selecciona");
        setupRecycler();

        presenter.setIdNivel(FKNivel);
        presenter.setIdPadre(fkPadre);
        presenter.obtenerConceptoTareoAndPadre();
        presenter.attachView(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.SCANNER_ACTION);
        this.registerReceiver(this.mScanReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        this.unregisterReceiver(this.mScanReceiver);

    }

    @Override
    public void mostrarConceptoTareo(List<ConceptoTareo> listConceptoTareo) {
        Log.e(TAG, "listConceptoTareo: " + listConceptoTareo);
        mAdapters.setData(listConceptoTareo);
        String username = presenter.getUserName();
        if (!presenter.getPerfilUser().contains("ADMINISTRADOR")) {
            if (concepto == 1) {
                for (ConceptoTareo c: listConceptoTareo) {
                    Log.d(TAG, "VALIDAR  SINC CORTE ->  " + c.getDescripcion()+ " | CON CORTE -> " + c.getDescripcion().substring(3).trim() + " | " + username.trim());
                    if (c.getDescripcion().substring(3).trim().equals(username.trim())) {
                        Intent intent = new Intent();
                        intent.putExtra("conceptoTareo", (Serializable) c);
                        intent.putExtra("concepto", concepto);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                }

            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (searchView.hasFocus()) {
                    searchView.clearFocus();
                } else {
                    onBackPressed();
                    setResult(Activity.RESULT_CANCELED, null);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.e(TAG, "newText: " + newText);
        presenter.searchFilter(newText);
        return false;
    }

    public void setToolbar(Toolbar toolbar) {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    private void setupRecycler() {
        recyclerConceptoTareo.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mAdapters = new SearchAdapter();
        mAdapters.setListener(conceptoTareo -> {
            if (conceptoTareo != null) {
                Log.e(TAG, "conceptoTareo: " + conceptoTareo);
                Intent intent = new Intent();
                intent.putExtra("conceptoTareo", (Serializable) conceptoTareo);
                intent.putExtra("concepto", concepto);
                setResult(Activity.RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(this, "sin data", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerConceptoTareo.setAdapter(mAdapters);
    }
}
