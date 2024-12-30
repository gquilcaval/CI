package com.dms.tareosoft.presentation.acceso.consulta;

import android.app.DatePickerDialog;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.fragment.app.FragmentManager;

import com.dms.tareosoft.base.BasePresenter;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.domain.acceso.consulta.IConsultaAccesoInteractor;
import com.dms.tareosoft.domain.incidencia.IConsultaIncidenciaInteractor;
import com.dms.tareosoft.domain.tareo_nuevo.nuevo_empleados.IEmpleadosInteractor;
import com.dms.tareosoft.presentation.dialog.DatePickerFragment;
import com.dms.tareosoft.presentation.incidencia.consulta.IConsultaIncidenciaContract;
import com.dms.tareosoft.util.Constants;
import com.dms.tareosoft.util.DateUtil;

import java.util.Calendar;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;

public class ConsultarAccesoPresenter extends BasePresenter<IConsultaAccesoContract.View>
        implements IConsultaAccesoContract.Presenter {
    private final String TAG = ConsultarAccesoPresenter.class.getSimpleName();

    @Inject
    PreferenceManager preferenceManager;
    @Inject
    IConsultaAccesoInteractor interactor;
    @Inject
    IEmpleadosInteractor interactorEmple;
    @Inject
    @Named("executor_thread")
    Scheduler ExecutorThread;
    @Inject
    @Named("ui_thread")
    Scheduler UiThread;

    private Calendar calendar;

    @Inject
    public ConsultarAccesoPresenter() {
        calendar = Calendar.getInstance(Locale.getDefault());
    }

    @Override
    public void searchAccesosByDate(String fch_ini, String fch_fin) {

        getCompositeDisposable().add(interactor.findAccesoByDate(fch_ini, fch_fin)
                .subscribeOn(ExecutorThread)
                .observeOn(UiThread)
                .subscribe(accesos -> {
                    Log.d(TAG, "searchAccesosByDate -> " + accesos);
                    if (isViewAttached()) {
                        getView().hiddenProgressbar();
                        getView().displayAccesos(accesos);
                    }
                }, error -> {
                    getView().hiddenProgressbar();
                    getView().showErrorMessage("Error al realizar la consulta.", error.getMessage());
                }));
    }

    @Override
    public void showDatePickerDialog(FragmentManager fragment, EditText editText, Long dateTime, boolean search) {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Log.e(TAG, "year: " + year + ", month: " + month + ", dayOfMonth: " + dayOfMonth);
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String date = DateUtil.dateToStringFormat(calendar.getTime(),
                                Constants.F_DATE_LECTURA);
                      //  setFechaInicio(DateUtil.dateToStringFormat(calendar.getTime(), Constants.F_DATE_TAREO));
                        Log.e(TAG, "date: " + date);
                        editText.setText(date);
                    }
                }, dateTime);
        newFragment.show(fragment, DatePickerFragment.TAG);
    }

}
