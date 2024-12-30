package com.dms.tareosoft.presentation.tareoNew.tareoNewNotEmployer.opciones;

import android.view.View;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.dms.tareosoft.R;
import com.dms.tareosoft.annotacion.TypeResultado;
import com.dms.tareosoft.annotacion.TypeTareo;
import com.dms.tareosoft.base.BaseFragment;
import com.dms.tareosoft.data.entities.Turno;
import com.dms.tareosoft.data.entities.UnidadMedida;
import com.dms.tareosoft.domain.DateTimeManager;
import com.dms.tareosoft.presentation.adapter.SpinTurnoAdapter;
import com.dms.tareosoft.presentation.adapter.SpinUnidadMedidaAdapter;
import com.dms.tareosoft.presentation.models.OpcionesTareo;
import com.dms.tareosoft.util.SpinnerListener;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class OpcNewTareoNotEmployerFragment extends BaseFragment implements IOpcNewTareoNotEmployerContract.View,
        View.OnClickListener {

    String TAG = OpcNewTareoNotEmployerFragment.class.getSimpleName();

    @BindView(R.id.et_fechaIniTareo)
    TextInputEditText etFechaIniTareo;
    @BindView(R.id.et_horaIniTareo)
    TextInputEditText etHoraInicioTareo;
    @BindView(R.id.sp_unidMedidaTareo)
    Spinner spUnidadMedida;
    @BindView(R.id.sp_turnoTareo)
    Spinner spTurno;
    @BindView(R.id.tv_unidad_tareo)
    TextView tvUnidadTareo;
    @BindView(R.id.rb_jornal)
    RadioButton rbJornal;
    @BindView(R.id.rb_destajo)
    RadioButton rbDestajo;
    @BindView(R.id.rb_porTareo)
    RadioButton rbPorTareo;
    @BindView(R.id.rb_porEmpleado)
    RadioButton rbPorEmpleado;

    @Inject
    OpcNewTareoNotEmployerPresenter presenter;
    @Inject
    DateTimeManager appDateTime;

    private SpinUnidadMedidaAdapter adapterUnidadMedida;

    private SpinTurnoAdapter adapterTurnos;

    @Override
    protected int getLayout() {
        return R.layout.fragment_nuevo_tareo_opciones;
    }

    @Override
    protected void setupVariables() {
        getActivityComponent().inject(this);
    }

    @Override
    protected void initEvents() {
        etFechaIniTareo.setOnClickListener(this);
        etHoraInicioTareo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_fechaIniTareo:
                presenter.showDatePickerDialog(getFragmentManager(), etFechaIniTareo, null);
                break;
            case R.id.et_horaIniTareo:
                presenter.showTimePickerDialog(getFragmentManager(), etHoraInicioTareo);
                break;
        }
    }

    @Override
    protected void setupView() {
        getActivityComponent().inject(this);
        presenter.attachView(this);

        setAdapters();
        presenter.obtenerUnidadesMedidas();
        presenter.obtenerTurnos();

        rbPorEmpleado.setChecked(true);
        rbPorTareo.setChecked(false);

        rbJornal.setChecked(true);
    }

    private void setAdapters() {
        etFechaIniTareo.setText(presenter.getFechaInicio());
        etHoraInicioTareo.setText(presenter.getHoraInicio());

        etFechaIniTareo.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                presenter.showDatePickerDialog(getFragmentManager(), etFechaIniTareo, null);
            }
        });

        etHoraInicioTareo.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                presenter.showTimePickerDialog(getFragmentManager(), etHoraInicioTareo);
            }
        });
        /*campos.setHoraInicio(mTimeTareo);*/
        spUnidadMedida.setOnItemSelectedListener((SpinnerListener) pos -> {
            UnidadMedida item = adapterUnidadMedida.getItem(pos);
            presenter.setCodigoUnidadMedida(item.getId());
        });

        spTurno.setOnItemSelectedListener((SpinnerListener) pos -> {
            Turno item = adapterTurnos.getItem(pos);
            presenter.setCodigoTurno(item.getId());
        });

        //tipo de tareo
        rbJornal.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                presenter.setTipoTareo(TypeTareo.TIPO_TAREO_JORNAL);
                rbJornal.setButtonDrawable(R.drawable.ic_action_checked);
                rbJornal.setChecked(true);

                rbDestajo.setButtonDrawable(R.drawable.ic_action_unchecked);
                rbDestajo.setChecked(false);

                rbPorTareo.setVisibility(View.VISIBLE);
            }
        });

        rbDestajo.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                presenter.setTipoTareo(TypeTareo.TIPO_TAREO_DESTAJO);
                rbDestajo.setButtonDrawable(R.drawable.ic_action_checked);
                rbDestajo.setChecked(true);

                rbJornal.setButtonDrawable(R.drawable.ic_action_unchecked);
                rbJornal.setChecked(false);

                rbPorTareo.setVisibility(View.GONE);
            }
        });

        //tipo de resultado
        rbPorEmpleado.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                presenter.setTipoResultado(TypeResultado.TIPO_RESULTADO_POR_EMPLEADO);
                rbPorEmpleado.setButtonDrawable(R.drawable.ic_action_checked);
                rbPorEmpleado.setChecked(true);

                rbPorTareo.setButtonDrawable(R.drawable.ic_action_unchecked);
                rbPorTareo.setChecked(false);

                rbDestajo.setVisibility(View.VISIBLE);
                rbPorTareo.setVisibility(View.VISIBLE);
            }
        });

        rbPorTareo.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                presenter.setTipoResultado(TypeResultado.TIPO_RESULTADO_POR_TAREO);
                rbPorTareo.setButtonDrawable(R.drawable.ic_action_checked);
                rbPorTareo.setChecked(true);

                rbPorEmpleado.setButtonDrawable(R.drawable.ic_action_unchecked);
                rbPorEmpleado.setChecked(false);

                presenter.setTipoTareo(TypeTareo.TIPO_TAREO_JORNAL);
                rbJornal.setButtonDrawable(R.drawable.ic_action_checked);
                rbJornal.setChecked(true);

                rbDestajo.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void listarUnidadMedida(List<UnidadMedida> resultado, int seleccion) {
        adapterUnidadMedida = new SpinUnidadMedidaAdapter(getActivity().getApplicationContext(),
                R.layout.spinner_dropdown, resultado);
        spUnidadMedida.setAdapter(adapterUnidadMedida);

        adapterUnidadMedida.notifyDataSetChanged();
        presenter.setCodigoUnidadMedida(seleccion);
        spUnidadMedida.setSelection(seleccion);
    }

    @Override
    public void llenarSpinnerTurno(List<Turno> resultado, int seleccion) {
        adapterTurnos = new SpinTurnoAdapter(getActivity().getApplicationContext(),
                R.layout.spinner_dropdown, resultado);
        spTurno.setAdapter(adapterTurnos);
        adapterTurnos.notifyDataSetChanged();
        presenter.setCodigoTurno(seleccion);
        spTurno.setSelection(seleccion);
        presenter.selectTurnoSegunHora(spTurno);
    }

    @Override
    public OpcionesTareo obtenerCampos() {
        return presenter.getOpcionesTareo();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
