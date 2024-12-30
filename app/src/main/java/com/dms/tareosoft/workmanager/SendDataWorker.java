package com.dms.tareosoft.workmanager;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.dms.tareosoft.App;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.entities.Acceso;
import com.dms.tareosoft.data.entities.Incidencia;
import com.dms.tareosoft.data.entities.Marcacion;
import com.dms.tareosoft.data.repository.DataSourceLocal;
import com.dms.tareosoft.data.repository.DataSourceRemote;
import com.dms.tareosoft.injection.DaggerApplicationComponent;
import com.dms.tareosoft.injection.modules.ContextModule;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Giancarlos Quilca
 */
public class SendDataWorker extends Worker {
    public static final String TAG = SendDataWorker.class.getSimpleName();

    private CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    DataSourceLocal dataSourceLocal;

    @Inject
    DataSourceRemote dataSourceRemoto;

    @Inject
    PreferenceManager preferenceManager;

    public SendDataWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        DaggerApplicationComponent
                .builder()
                .contextModule(new ContextModule(App.get(context)))
                .build()
                .inject(this);

    }

    @NonNull
    @Override
    public Result doWork() {
        starSendData();
        if (!isStopped())
            return Result.failure();
        return Result.success();
    }

    @Override
    public void onStopped() {
        super.onStopped();
        Log.i(TAG, "onStopped");
        cleanDispose();
    }

    public CompositeDisposable getCompositeDisposable() {
        if (disposables == null) {
            disposables = new CompositeDisposable();
        }
        return disposables;
    }

    private void cleanDispose() {
        Log.i(TAG, "cleanDispose");
        if (disposables != null) {
            disposables.dispose();
        }
    }

    private void starSendData() {
       obtenerIncidenciasLocal();
       obtenerMarcacionesAsistenciaLocal();
       obtenerMarcacionesAccesoLocal();
    }

    /** MODULO MARCACION INCIDENCIA ***/

    private  void obtenerIncidenciasLocal() {
        dataSourceLocal.obtenerIncidentesPendientesEnviar()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {

                            for (Incidencia marcacion: result) {
                                Log.e(TAG, "result list -> " + marcacion);
                                enviarIncidencia(marcacion);
                            }

                        }, throwable -> {

                            Log.e(TAG, "obtenerIncidenciasLocal throwable: " + throwable);
                            Log.e(TAG, "obtenerIncidenciasLocal throwable: " + throwable.toString());
                            Log.e(TAG, "obtenerIncidenciasLocal throwable: " + throwable.getMessage());
                            Log.e(TAG, "obtenerIncidenciasLocal throwable: " + throwable.getLocalizedMessage());

                        }
                );
    }

    private void enviarIncidencia(Incidencia incidencia) {
        dataSourceRemoto.registrarIncidencia(incidencia)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(respuesta -> {
                    Log.d(TAG, "ENVIAR idIncidencia = "+incidencia.getInt_MarcaInsidencia()+ " response -> " + respuesta);
                    if (respuesta == 1) {
                        actualizarIncidencia(incidencia.getInt_MarcaInsidencia());
                    }

                }, throwable -> {
                    Log.e(TAG, "enviarIncidencia throwable: " + throwable);
                    Log.e(TAG, "enviarIncidencia throwable: " + throwable.toString());
                    Log.e(TAG, "enviarIncidencia throwable: " + throwable.getMessage());
                    Log.e(TAG, "enviarIncidencia throwable: " + throwable.getLocalizedMessage());
                });
    }

    private void actualizarIncidencia(int idIncidencia) {
        dataSourceLocal.updateEnvioIncidencia(idIncidencia)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( e -> {
                            Log.d(TAG, "actualizado -> " + e);
                        }, throwable -> {
                            Log.e(TAG, "actualizarIncidencia throwable: " + throwable);
                            Log.e(TAG, "actualizarIncidencia throwable: " + throwable.toString());
                            Log.e(TAG, "actualizarIncidencia throwable: " + throwable.getMessage());
                            Log.e(TAG, "actualizarIncidencia throwable: " + throwable.getLocalizedMessage());

                        }
                );
    }


    /** MODULO MARCACION ASISTENCIA ***/

    private  void obtenerMarcacionesAsistenciaLocal() {
        dataSourceLocal.obtenerAsistenciasPendientes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {

                            for (Marcacion marcacion: result) {
                                Log.e(TAG, "result list -> " + marcacion);
                                enviarMarcacionAsistencia(marcacion);
                            }

                        }, throwable -> {

                            Log.e(TAG, "obtenerMarcacionesAsistenciaLocal throwable: " + throwable);
                            Log.e(TAG, "obtenerMarcacionesAsistenciaLocal throwable: " + throwable.toString());
                            Log.e(TAG, "obtenerMarcacionesAsistenciaLocal throwable: " + throwable.getMessage());
                            Log.e(TAG, "obtenerMarcacionesAsistenciaLocal throwable: " + throwable.getLocalizedMessage());

                        }
                );
    }

    private void enviarMarcacionAsistencia(Marcacion marcacion) {
        dataSourceRemoto.registrarMarcacion(marcacion)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(respuesta -> {
                    Log.d(TAG, "ENVIAR idMarcacionAsistencia = "+marcacion.getIdMarcacion()+ " response -> " + respuesta);
                    if (respuesta == 1) {
                        actualizarMarcacionAsistencia(marcacion.getIdMarcacion());
                    }

                }, throwable -> {
                    Log.e(TAG, "enviarMarcacionAsistencia throwable: " + throwable);
                    Log.e(TAG, "enviarMarcacionAsistencia throwable: " + throwable.toString());
                    Log.e(TAG, "enviarMarcacionAsistencia throwable: " + throwable.getMessage());
                    Log.e(TAG, "enviarMarcacionAsistencia throwable: " + throwable.getLocalizedMessage());
                });
    }

    private void actualizarMarcacionAsistencia(int idMarcacion) {
        dataSourceLocal.updateEnvioMarcacion(idMarcacion)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( e -> {
                            Log.d(TAG, "actualizado -> " + e);
                        }, throwable -> {
                            Log.e(TAG, "actualizarMarcacionAsistencia throwable: " + throwable);
                            Log.e(TAG, "actualizarMarcacionAsistencia throwable: " + throwable.toString());
                            Log.e(TAG, "actualizarMarcacionAsistencia throwable: " + throwable.getMessage());
                            Log.e(TAG, "actualizarMarcacionAsistencia throwable: " + throwable.getLocalizedMessage());

                        }
                );
    }

    /** MODULO MARCACION ACCESO ***/

    private  void obtenerMarcacionesAccesoLocal() {
        dataSourceLocal.obtenerAccesosPendientesEnviar()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {

                            for (Acceso acceso: result) {
                                Log.e(TAG, "result list -> " + acceso);
                                enviarMarcacionAcceso(acceso);
                            }

                        }, throwable -> {

                            Log.e(TAG, "obtenerMarcacionesAccesoLocal throwable: " + throwable);
                            Log.e(TAG, "obtenerMarcacionesAccesoLocal throwable: " + throwable.toString());
                            Log.e(TAG, "obtenerMarcacionesAccesoLocal throwable: " + throwable.getMessage());
                            Log.e(TAG, "obtenerMarcacionesAccesoLocal throwable: " + throwable.getLocalizedMessage());

                        }
                );
    }

    private void enviarMarcacionAcceso(Acceso acceso) {
        Log.d(TAG, "enviarMarcacionAcceso" + acceso);
        dataSourceRemoto.registrarAcceso(acceso)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(respuesta -> {
                    Log.d(TAG, "ENVIAR id = "+acceso.getId()+ " response -> " + respuesta);
                    try {
                        if (respuesta.getSuccess() == true) {
                            actualizarMarcacionAcceso(acceso.getId());
                        }
                    } catch (NullPointerException e) {
                        Log.e(TAG, "enviarMarcacionAcceso throwable: NULL");
                    }


                }, throwable -> {
                    Log.e(TAG, "enviarMarcacionAcceso throwable: " + throwable);
                    Log.e(TAG, "enviarMarcacionAcceso throwable: " + throwable.toString());
                    Log.e(TAG, "enviarMarcacionAcceso throwable: " + throwable.getMessage());
                    Log.e(TAG, "enviarMarcacionAcceso throwable: " + throwable.getLocalizedMessage());
                });
    }

    private void actualizarMarcacionAcceso(int id) {
        dataSourceLocal.updateEnvioMarcacion(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( e -> {
                            Log.d(TAG, "actualizado -> " + e);
                        }, throwable -> {
                            Log.e(TAG, "actualizarMarcacionAcceso throwable: " + throwable);
                            Log.e(TAG, "actualizarMarcacionAcceso throwable: " + throwable.toString());
                            Log.e(TAG, "actualizarMarcacionAcceso throwable: " + throwable.getMessage());
                            Log.e(TAG, "actualizarMarcacionAcceso throwable: " + throwable.getLocalizedMessage());

                        }
                );
    }
}
