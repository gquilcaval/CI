package com.dms.tareosoft.presentation.tareo_principal.iniciados;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dms.tareosoft.R;
import com.dms.tareosoft.annotacion.TypeView;
import com.dms.tareosoft.base.BaseFragment;
import com.dms.tareosoft.presentation.adapter.TareoGroupAdapter;
import com.dms.tareosoft.presentation.models.GroupTareoRow;
import com.dms.tareosoft.presentation.models.TareoRow;
import com.dms.tareosoft.presentation.tareoEditar.tareoEditAdmin.EditarTareoActivity;
import com.dms.tareosoft.presentation.tareoEditar.tareoEditUser.EditarEmpleadosActivity;
import com.dms.tareosoft.presentation.tareoNew.tareo_nuevo.NuevoTareoActivity;
import com.dms.tareosoft.util.UtilsMethods;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class IniciadosFragment extends BaseFragment
        implements IIniciadosContract.View {

    String TAG = IniciadosFragment.class.getSimpleName();

    @Inject
    IniciadosPresenter presenter;
    @Inject
    Context context;
    @BindView(R.id.rv_tareos)
    RecyclerView rvTareos;
    @BindView(R.id.tv_totalTareosIniciados)
    TextView tvTotalIniciados;
    @BindView(R.id.fab_nuevoTareo)
    FloatingActionButton fab_nuevoTareo;

    private TareoGroupAdapter adapterTareo;
    private List<GroupTareoRow> listaTareos;

    public IniciadosFragment() {
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_tareos_inciados;
    }

    @Override
    protected void setupVariables() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void setupView() {
        rvTareos.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        listaTareos = new ArrayList<>();
        adapterTareo = new TareoGroupAdapter(listaTareos, getActivity().getApplicationContext());
        adapterTareo.setListener((view, item, position, longPress) -> {
            if (!longPress)
                viewMenu(view, item, position);
        });
        rvTareos.setAdapter(adapterTareo);
        presenter.attachView(this);
    }

    @OnClick(R.id.fab_nuevoTareo)
    public void submit(View view) {
        fab_nuevoTareo.setEnabled(false);
        startActivity(NuevoTareoActivity.newInstance(getContext()));
    }

    @Override
    public void mostrarTareos(List<TareoRow> lista) {
        listaTareos.clear();
        HashMap<String, List<TareoRow>> groupedHashMap = groupDataIntoHashMap(lista);
        List<GroupTareoRow> list = new ArrayList<>();
        for (String date : groupedHashMap.keySet()) {
            GroupTareoRow dateItem = new GroupTareoRow();
            dateItem.setNivel1(date);
            dateItem.setTypeView(TypeView.HEADER);
            list.add(dateItem);
            for (TareoRow pojoOfJsonArray : groupedHashMap.get(date)) {
                GroupTareoRow generalItem = new GroupTareoRow();
                generalItem.setData(pojoOfJsonArray);
                generalItem.setTypeView(TypeView.BODY);
                list.add(generalItem);
            }
        }

        listaTareos.addAll(list);
        adapterTareo.notifyDataSetChanged();
        tvTotalIniciados.setText(getString(R.string.total_ini_x,
                lista.size()));
    }

    private HashMap<String, List<TareoRow>> groupDataIntoHashMap(List<TareoRow> listOfPojosOfJsonArray) {
        HashMap<String, List<TareoRow>> groupedHashMap = new HashMap<>();
        for (TareoRow pojoOfJsonArray : listOfPojosOfJsonArray) {
            String hashMapKey = pojoOfJsonArray.getConcepto1();
            if (groupedHashMap.containsKey(hashMapKey)) {
                groupedHashMap.get(hashMapKey).add(pojoOfJsonArray);
            } else {
                List<TareoRow> list = new ArrayList<>();
                list.add(pojoOfJsonArray);
                groupedHashMap.put(hashMapKey, list);
            }
        }
        return groupedHashMap;
    }

    @Override
    public void onResume() {
        super.onResume();
        fab_nuevoTareo.setEnabled(true);
        presenter.obtenerTareosIniciados();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    private void viewMenu(View view, TareoRow item, int position) {
        PopupMenu popup = new PopupMenu(getContext(), view);
        popup.inflate(R.menu.popup_menu);
        /*if (presenter.isAdmin()) {
            popup.getMenu().findItem(R.id.elim_menu).setVisible(true);
        } else {
            popup.getMenu().findItem(R.id.elim_menu).setVisible(false);
        }*/
        popup.setOnMenuItemClickListener(menuItem -> {
            Log.e(TAG, "allTareoRow: " + item);
            switch (menuItem.getItemId()) {
                case R.id.elim_menu:
                    if (item.getCantProducida() > 0) {
                        mostrarMensajeEliminar(item, "El tareo seleccionado contiene resultados\n" +
                                "¿Esta seguro de eliminar el tareo iniciado?");
                    } else {
                        mostrarMensajeEliminar(item, "¿Esta seguro de eliminar el tareo iniciado?");
                    }
                    return true;
                case R.id.edit_menu:
                    if (presenter.isAdmin()) {
                        startActivity(EditarTareoActivity.newInstance(getContext(), item.getCodigo()));
                    } else {
                        startActivity(EditarEmpleadosActivity.newInstance(getContext(), item.getCodigo()));
                    }
                    return true;
            }
            return false;
        });
        popup.show();
    }

    private void mostrarMensajeEliminar(TareoRow item, String mensaje) {
        UtilsMethods.showConfirmDialog(getActivity(), "Confirmar",
                mensaje,
                () -> {
                    presenter.deleteTareo(item.getCodigo());
                },
                () -> {
                /*adapterTareo.undoDelete(position);
                presenter.obtenerTareosIniciados();*/
                }).show();
        presenter.obtenerTareosIniciados();
    }
}
