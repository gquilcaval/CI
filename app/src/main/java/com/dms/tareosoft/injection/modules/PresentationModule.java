package com.dms.tareosoft.injection.modules;

import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.repository.DataSourceLocal;
import com.dms.tareosoft.data.repository.DataSourceRemote;
import com.dms.tareosoft.domain.acceso.consulta.ConsultaAccesoInteractorImpl;
import com.dms.tareosoft.domain.acceso.consulta.IConsultaAccesoInteractor;
import com.dms.tareosoft.domain.acceso.registro.IRegistroAccesoInteractor;
import com.dms.tareosoft.domain.acceso.registro.RegistroAccesoInteractorImpl;
import com.dms.tareosoft.domain.asistencia.consulta.ConsultaAsistenciaInteractorImpl;
import com.dms.tareosoft.domain.asistencia.consulta.IConsultaAsistenciaInteractor;
import com.dms.tareosoft.domain.asistencia.empleado.AsistenciaEmpleadoInteractorImpl;
import com.dms.tareosoft.domain.asistencia.empleado.IAsistenciaEmpleadoInteractor;
import com.dms.tareosoft.domain.asistencia.registro.IRegistroAsistenciaInteractor;
import com.dms.tareosoft.domain.asistencia.registro.RegistroAsistenciaInteractorImpl;
import com.dms.tareosoft.domain.asistencia.registro.registro_definicion.IRegistroDefinicionInteractor;
import com.dms.tareosoft.domain.asistencia.registro.registro_definicion.RegistroDefinicionInteractorImpl;
import com.dms.tareosoft.domain.incidencia.ConsultaIncidenciaInteractorImpl;
import com.dms.tareosoft.domain.incidencia.IConsultaIncidenciaInteractor;
import com.dms.tareosoft.domain.incidencia.IRegistroIncidenciaInteractor;
import com.dms.tareosoft.domain.incidencia.RegistroIncidenciaInteractorImpl;
import com.dms.tareosoft.domain.login_interactor.ILoginInteractor;
import com.dms.tareosoft.domain.login_interactor.LoginInteractorImpl;
import com.dms.tareosoft.domain.search.ISearchInteractor;
import com.dms.tareosoft.domain.search.SearchInteractorImpl;
import com.dms.tareosoft.domain.settings_interactor.ISettingsInteractor;
import com.dms.tareosoft.domain.settings_interactor.SettingsInteractorImpl;
import com.dms.tareosoft.domain.sync.ISyncInteractor;
import com.dms.tareosoft.domain.sync.SyncInteractorImpl;
import com.dms.tareosoft.domain.tareo_consultar.ITareoConsultarInteractor;
import com.dms.tareosoft.domain.tareo_consultar.TareoConsultarInteractorImpl;
import com.dms.tareosoft.domain.tareo_consultar.tareo_consultar_details.ITareoConsultaDetailsInteractor;
import com.dms.tareosoft.domain.tareo_consultar.tareo_consultar_details.TareoConsultaDetailsInteractorImpl;
import com.dms.tareosoft.domain.tareo_control.ControlTareoInteractorImpl;
import com.dms.tareosoft.domain.tareo_control.IControlTareoInteractor;
import com.dms.tareosoft.domain.tareo_editar_emp.ITareoEditarEmpInteractor;
import com.dms.tareosoft.domain.tareo_editar_emp.TareoEditarEmpInteractorImpl;
import com.dms.tareosoft.domain.tareo_finalizar.FinalizadoMasivoInteractorImpl;
import com.dms.tareosoft.domain.tareo_finalizar.IFinalizadoMasivoInteractor;
import com.dms.tareosoft.domain.tareo_editar.EditarTareoInteractorImpl;
import com.dms.tareosoft.domain.tareo_editar.IEditarTareoInteractor;
import com.dms.tareosoft.domain.tareo_editar.editar_definicion.EditarDefinicionInteractorImpl;
import com.dms.tareosoft.domain.tareo_editar.editar_definicion.IEditarDefinicionInteractor;
import com.dms.tareosoft.domain.tareo_editar.editar_empleados.EditarEmpleadosInteractorImpl;
import com.dms.tareosoft.domain.tareo_editar.editar_empleados.IEditarEmpleadosInteractor;
import com.dms.tareosoft.domain.tareo_editar.editar_opciones.EditarOpcionesInteractorImpl;
import com.dms.tareosoft.domain.tareo_editar.editar_opciones.IEditarOpcionesInteractor;
import com.dms.tareosoft.domain.tareo_finalizar.finalizar_masivo.ITareoReubicarMasivoListInteractor;
import com.dms.tareosoft.domain.tareo_finalizar.finalizar_masivo.TareoReubicarMasivoListImpl;
import com.dms.tareosoft.domain.tareo_finalizar.finalizar_masivo.finalizar_masivo_por_destajo.ITareoReubicarMasivoPorDestajoInteractor;
import com.dms.tareosoft.domain.tareo_finalizar.finalizar_masivo.finalizar_masivo_por_destajo.TareoReubicarMasivoPorDestajoImpl;
import com.dms.tareosoft.domain.tareo_finalizar.finalizar_masivo.finalizar_masivo_por_destajo.finalizar_masivo_por_destajo_cantidad.ITareoReubicarMasivoPorDestajoCantidadInteractor;
import com.dms.tareosoft.domain.tareo_finalizar.finalizar_masivo.finalizar_masivo_por_destajo.finalizar_masivo_por_destajo_cantidad.TareoReubicarMasivoPorDestajoCantidadImpl;
import com.dms.tareosoft.domain.tareo_finalizar.lista_empleados.IListadoEmpleadoInteractor;
import com.dms.tareosoft.domain.tareo_finalizar.lista_empleados.ListadoEmpleadoInteractorImpl;
import com.dms.tareosoft.domain.tareo_interactor.finalizados.FinalizadosInteractorImpl;
import com.dms.tareosoft.domain.tareo_interactor.finalizados.IFinalizadosInteractor;
import com.dms.tareosoft.domain.tareo_interactor.iniciados.IIniciadosInteractor;
import com.dms.tareosoft.domain.tareo_interactor.iniciados.IniciadosInteractorImpl;
import com.dms.tareosoft.domain.tareo_nuevo.INuevoTareoInteractor;
import com.dms.tareosoft.domain.tareo_nuevo.NuevoTareoInteractorImpl;
import com.dms.tareosoft.domain.tareo_nuevo.nuevo_definicion.DefinicionInteractorImpl;
import com.dms.tareosoft.domain.tareo_nuevo.nuevo_definicion.IDefinicionInteractor;
import com.dms.tareosoft.domain.tareo_nuevo.nuevo_empleados.EmpleadosInteractorImpl;
import com.dms.tareosoft.domain.tareo_nuevo.nuevo_empleados.IEmpleadosInteractor;
import com.dms.tareosoft.domain.tareo_nuevo.nuevo_opciones.IOpcionesInteractor;
import com.dms.tareosoft.domain.tareo_nuevo.nuevo_opciones.OpcionesInteractorImpl;
import com.dms.tareosoft.domain.tareo_resultado.IResultadoInteractor;
import com.dms.tareosoft.domain.tareo_resultado.ResultadoInteractorImpl;
import com.dms.tareosoft.domain.tareo_resultado.tareo_resultado_por_tareo.IResultadoPorTareoInteractor;
import com.dms.tareosoft.domain.tareo_resultado.tareo_resultado_por_tareo.ResultadoPorTareoInteractorImpl;
import com.dms.tareosoft.domain.tareo_reubicar.IReubicarInteractor;
import com.dms.tareosoft.domain.tareo_reubicar.ReubicarInteractorImpl;
import com.dms.tareosoft.domain.tareo_reubicar.tareo_reubicar_list.ITareoReubicarListInteractor;
import com.dms.tareosoft.domain.tareo_reubicar.tareo_reubicar_list.TareoReubicarListInteractorImpl;
import com.dms.tareosoft.domain.tareo_reubicar.tareo_segun_resultado.tareo_reubicar_tipo_empleado.IReubicarTipoEmpleadoInteractor;
import com.dms.tareosoft.domain.tareo_reubicar.tareo_segun_resultado.tareo_reubicar_tipo_empleado.ReubicarTipoEmpleadoInteractorImpl;
import com.dms.tareosoft.domain.tareo_reubicar.tareo_segun_resultado.tareo_reubicar_tipo_empleado.reubicar_tipo_empleado_cantidad.IReubicarTipoEmpleadoCantidadInteractor;
import com.dms.tareosoft.domain.tareo_reubicar.tareo_segun_resultado.tareo_reubicar_tipo_empleado.reubicar_tipo_empleado_cantidad.ReubicarTipoEmpleadoCantidadInteractorImpl;
import com.dms.tareosoft.domain.tareo_reubicar.tareo_segun_resultado.tareo_reubicar_tipo_tareo.IReubicarTipoTareoInteractor;
import com.dms.tareosoft.domain.tareo_reubicar.tareo_segun_resultado.tareo_reubicar_tipo_tareo.ReubicarTipoTareoInteractorImpl;
import com.dms.tareosoft.domain.tareo_reubicar.tareo_segun_resultado.tareo_reubicar_tipo_tareo.reubicar_tipo_tareo_cantidad.IReubicarTipoTareoCantidadInteractor;
import com.dms.tareosoft.domain.tareo_reubicar.tareo_segun_resultado.tareo_reubicar_tipo_tareo.reubicar_tipo_tareo_cantidad.ReubicarTipoTareoCantidadInteractorImpl;
import com.dms.tareosoft.injection.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

@Module
public class PresentationModule {

    @ActivityScope
    @Provides
    ILoginInteractor provideSyncInteractor(DataSourceLocal dataSourceLocal,
                                           DataSourceRemote dataSourceRemote,
                                           PreferenceManager preferenceManager) {
        return new LoginInteractorImpl(dataSourceLocal, dataSourceRemote, preferenceManager);
    }

    @ActivityScope
    @Provides
    ISettingsInteractor provideSettingsInteractor(DataSourceLocal dataSourceLocal,
                                                  DataSourceRemote dataSourceRemote,
                                                  PreferenceManager preferenceManager) {
        return new SettingsInteractorImpl(dataSourceLocal, dataSourceRemote, preferenceManager);
    }

    @ActivityScope
    @Provides
    IIniciadosInteractor provideIniciadosInteractor(DataSourceLocal dataSourceLocal,
                                                    DataSourceRemote dataSourceRemote,
                                                    PreferenceManager preferenceManager) {
        return new IniciadosInteractorImpl(dataSourceLocal, dataSourceRemote, preferenceManager);
    }

    @ActivityScope
    @Provides
    IFinalizadosInteractor provideFinalizadosInteractor(DataSourceLocal dataSourceLocal,
                                                        DataSourceRemote dataSourceRemote,
                                                        PreferenceManager preferenceManager) {
        return new FinalizadosInteractorImpl(dataSourceLocal, dataSourceRemote, preferenceManager);
    }

    @ActivityScope
    @Provides
    INuevoTareoInteractor provideNuevoTareoInteractor(DataSourceLocal dataSourceLocal,
                                                      DataSourceRemote dataSourceRemote,
                                                      PreferenceManager preferenceManager) {
        return new NuevoTareoInteractorImpl(dataSourceLocal, dataSourceRemote, preferenceManager);
    }

    @ActivityScope
    @Provides
    IDefinicionInteractor provideDefinicionInteractor(DataSourceLocal dataSourceLocal,
                                                      DataSourceRemote dataSourceRemote,
                                                      PreferenceManager preferenceManager) {
        return new DefinicionInteractorImpl(dataSourceLocal, dataSourceRemote, preferenceManager);
    }

    @ActivityScope
    @Provides
    IEmpleadosInteractor provideEmpleadosInteractor(DataSourceLocal dataSourceLocal,
                                                    DataSourceRemote dataSourceRemote,
                                                    PreferenceManager preferenceManager) {
        return new EmpleadosInteractorImpl(dataSourceLocal, dataSourceRemote, preferenceManager);
    }

    @ActivityScope
    @Provides
    IOpcionesInteractor provideOpcionesInteractor(DataSourceLocal dataSourceLocal,
                                                  DataSourceRemote dataSourceRemote,
                                                  PreferenceManager preferenceManager) {
        return new OpcionesInteractorImpl(dataSourceLocal, dataSourceRemote, preferenceManager);
    }


    @ActivityScope
    @Provides
    IEditarTareoInteractor provideEditarTareoInteractor(DataSourceLocal dataSourceLocal,
                                                        DataSourceRemote dataSourceRemote,
                                                        PreferenceManager preferenceManager) {
        return new EditarTareoInteractorImpl(dataSourceLocal, dataSourceRemote, preferenceManager);
    }

    @ActivityScope
    @Provides
    IEditarDefinicionInteractor provideEditarDefinicionInteractor(DataSourceLocal dataSourceLocal,
                                                                  DataSourceRemote dataSourceRemote,
                                                                  PreferenceManager preferenceManager) {
        return new EditarDefinicionInteractorImpl(dataSourceLocal, dataSourceRemote, preferenceManager);
    }

    @ActivityScope
    @Provides
    IEditarEmpleadosInteractor provideEditarEmpleadosInteractor(DataSourceLocal dataSourceLocal,
                                                                DataSourceRemote dataSourceRemote,
                                                                PreferenceManager preferenceManager) {
        return new EditarEmpleadosInteractorImpl(dataSourceLocal, dataSourceRemote, preferenceManager);
    }

    @ActivityScope
    @Provides
    IEditarOpcionesInteractor provideEditarOpcionesInteractor(DataSourceLocal dataSourceLocal,
                                                              DataSourceRemote dataSourceRemote,
                                                              PreferenceManager preferenceManager) {
        return new EditarOpcionesInteractorImpl(dataSourceLocal, dataSourceRemote, preferenceManager);
    }

    @ActivityScope
    @Provides
    IFinalizadoMasivoInteractor provideFinalizadoMasivoInteractor(DataSourceLocal dataSourceLocal,
                                                                  DataSourceRemote dataSourceRemote,
                                                                  PreferenceManager preferenceManager) {
        return new FinalizadoMasivoInteractorImpl(dataSourceLocal, dataSourceRemote, preferenceManager);
    }

    @ActivityScope
    @Provides
    IResultadoInteractor provideResultadoInteractor(DataSourceLocal dataSourceLocal,
                                                    DataSourceRemote dataSourceRemote,
                                                    PreferenceManager preferenceManager) {
        return new ResultadoInteractorImpl(dataSourceLocal, dataSourceRemote, preferenceManager);
    }

    @ActivityScope
    @Provides
    IControlTareoInteractor provideControlTareoInteractor(DataSourceLocal dataSourceLocal,
                                                          DataSourceRemote dataSourceRemote,
                                                          PreferenceManager preferenceManager) {
        return new ControlTareoInteractorImpl(dataSourceLocal, dataSourceRemote, preferenceManager);
    }

    /* Finalizar tareo x empleado */
    @ActivityScope
    @Provides
    IListadoEmpleadoInteractor provideListadoEmpleadoInteractorImpl(DataSourceLocal dataSourceLocal,
                                                                    DataSourceRemote dataSourceRemote,
                                                                    PreferenceManager preferenceManager) {
        return new ListadoEmpleadoInteractorImpl(dataSourceLocal, dataSourceRemote, preferenceManager);
    }

    @ActivityScope
    @Provides
    ISyncInteractor provideSyncInteractorImpl(DataSourceLocal dataSourceLocal,
                                              DataSourceRemote dataSourceRemote,
                                              PreferenceManager preferenceManager) {
        return new SyncInteractorImpl(dataSourceLocal, dataSourceRemote, preferenceManager);
    }

    @ActivityScope
    @Provides
    ITareoEditarEmpInteractor provideITareoEditarEmpInteractorImpl(DataSourceLocal dataSourceLocal,
                                                                   DataSourceRemote dataSourceRemote,
                                                                   PreferenceManager preferenceManager) {
        return new TareoEditarEmpInteractorImpl(dataSourceLocal, dataSourceRemote, preferenceManager);
    }

    @ActivityScope
    @Provides
    IReubicarInteractor provideIReubicarInteractor(DataSourceLocal dataSourceLocal,
                                                   DataSourceRemote dataSourceRemote,
                                                   PreferenceManager preferenceManager) {
        return new ReubicarInteractorImpl(dataSourceLocal, dataSourceRemote, preferenceManager);
    }

    @ActivityScope
    @Provides
    ITareoReubicarListInteractor provideITareoReubicarListInteractor(DataSourceLocal dataSourceLocal,
                                                                     DataSourceRemote dataSourceRemote,
                                                                     PreferenceManager preferenceManager) {
        return new TareoReubicarListInteractorImpl(dataSourceLocal, dataSourceRemote, preferenceManager);
    }

    @ActivityScope
    @Provides
    ITareoReubicarMasivoListInteractor provideITareoReubicarMasivoListInterator(DataSourceLocal dataSourceLocal,
                                                                                DataSourceRemote dataSourceRemote,
                                                                                PreferenceManager preferenceManager) {
        return new TareoReubicarMasivoListImpl(dataSourceLocal, dataSourceRemote, preferenceManager);
    }

    @ActivityScope
    @Provides
    ITareoReubicarMasivoPorDestajoInteractor provideITareoReubicarMasivoPorDestajoInterator(DataSourceLocal dataSourceLocal,
                                                                                            DataSourceRemote dataSourceRemote,
                                                                                            PreferenceManager preferenceManager) {
        return new TareoReubicarMasivoPorDestajoImpl(dataSourceLocal, dataSourceRemote, preferenceManager);
    }

    @ActivityScope
    @Provides
    ITareoReubicarMasivoPorDestajoCantidadInteractor provideITareoReubicarMasivoPorDestajoCantidadInteractor(DataSourceLocal dataSourceLocal,
                                                                                                             DataSourceRemote dataSourceRemote,
                                                                                                             PreferenceManager preferenceManager) {
        return new TareoReubicarMasivoPorDestajoCantidadImpl(dataSourceLocal, dataSourceRemote, preferenceManager);
    }

    @ActivityScope
    @Provides
    ISearchInteractor provideISearchInteractor(DataSourceLocal dataSourceLocal,
                                               DataSourceRemote dataSourceRemote,
                                               PreferenceManager preferenceManager) {
        return new SearchInteractorImpl(dataSourceLocal, dataSourceRemote, preferenceManager);
    }

    @ActivityScope
    @Provides
    ITareoConsultarInteractor provideITareoConsultarInteractor(DataSourceLocal dataSourceLocal,
                                                               DataSourceRemote dataSourceRemote,
                                                               PreferenceManager preferenceManager) {
        return new TareoConsultarInteractorImpl(dataSourceLocal, dataSourceRemote, preferenceManager);
    }

    @ActivityScope
    @Provides
    ITareoConsultaDetailsInteractor provideITareoConsultaIniciadoInteractor(DataSourceLocal dataSourceLocal,
                                                                            DataSourceRemote dataSourceRemote,
                                                                            PreferenceManager preferenceManager) {
        return new TareoConsultaDetailsInteractorImpl(dataSourceLocal, dataSourceRemote, preferenceManager);
    }

    @ActivityScope
    @Provides
    IResultadoPorTareoInteractor provideIResultadoPorTareoInteractor(DataSourceLocal dataSourceLocal,
                                                                     DataSourceRemote dataSourceRemote,
                                                                     PreferenceManager preferenceManager) {
        return new ResultadoPorTareoInteractorImpl(dataSourceLocal, dataSourceRemote, preferenceManager);
    }

    @ActivityScope
    @Provides
    IReubicarTipoEmpleadoCantidadInteractor provideIReubicarTipoEmpleadoCantidadInteractor(DataSourceLocal dataSourceLocal,
                                                                                           DataSourceRemote dataSourceRemote,
                                                                                           PreferenceManager preferenceManager) {
        return new ReubicarTipoEmpleadoCantidadInteractorImpl(dataSourceLocal, dataSourceRemote, preferenceManager);
    }

    @ActivityScope
    @Provides
    IReubicarTipoEmpleadoInteractor provideIReubicarTipoEmpleadoInteractor(DataSourceLocal dataSourceLocal,
                                                                           DataSourceRemote dataSourceRemote,
                                                                           PreferenceManager preferenceManager) {
        return new ReubicarTipoEmpleadoInteractorImpl(dataSourceLocal, dataSourceRemote, preferenceManager);
    }

    @ActivityScope
    @Provides
    IReubicarTipoTareoCantidadInteractor provideIReubicarTipoTareoCantidadInteractor(DataSourceLocal dataSourceLocal,
                                                                                     DataSourceRemote dataSourceRemote,
                                                                                     PreferenceManager preferenceManager) {
        return new ReubicarTipoTareoCantidadInteractorImpl(dataSourceLocal, dataSourceRemote, preferenceManager);
    }

    @ActivityScope
    @Provides
    IReubicarTipoTareoInteractor provideIReubicarTipoTareoInteractor(DataSourceLocal dataSourceLocal,
                                                                     DataSourceRemote dataSourceRemote,
                                                                     PreferenceManager preferenceManager) {
        return new ReubicarTipoTareoInteractorImpl(dataSourceLocal, dataSourceRemote, preferenceManager);
    }

    @ActivityScope
    @Provides
    IRegistroAsistenciaInteractor provideIRegistroAsistenciaInteractorr(DataSourceLocal dataSourceLocal,
                                                                      DataSourceRemote dataSourceRemote,
                                                                      PreferenceManager preferenceManager) {
        return new RegistroAsistenciaInteractorImpl(dataSourceLocal, dataSourceRemote, preferenceManager);
    }

    @ActivityScope
    @Provides
    IRegistroDefinicionInteractor provideRegistroDefinicionInteractorr(DataSourceLocal dataSourceLocal,
                                                                       DataSourceRemote dataSourceRemote,
                                                                       PreferenceManager preferenceManager) {
        return new RegistroDefinicionInteractorImpl(dataSourceLocal, dataSourceRemote, preferenceManager);
    }

    @ActivityScope
    @Provides
    IAsistenciaEmpleadoInteractor provideAsistenciaEmpleadoInteractor(DataSourceLocal dataSourceLocal,
                                                                       DataSourceRemote dataSourceRemote,
                                                                       PreferenceManager preferenceManager) {
        return new AsistenciaEmpleadoInteractorImpl(dataSourceLocal, dataSourceRemote, preferenceManager);
    }

    @ActivityScope
    @Provides
    IConsultaAsistenciaInteractor provideConsultaAsistenciaEmpleadoInteractor(DataSourceLocal dataSourceLocal,
                                                                              DataSourceRemote dataSourceRemote,
                                                                              PreferenceManager preferenceManager) {
        return new ConsultaAsistenciaInteractorImpl(dataSourceLocal, dataSourceRemote, preferenceManager);
    }

    @ActivityScope
    @Provides
    IRegistroIncidenciaInteractor provideRegistroIncidenciaInteractor(DataSourceLocal dataSourceLocal,
                                                                              DataSourceRemote dataSourceRemote,
                                                                              PreferenceManager preferenceManager) {
        return new RegistroIncidenciaInteractorImpl(dataSourceLocal, dataSourceRemote, preferenceManager);
    }

    @ActivityScope
    @Provides
    IConsultaIncidenciaInteractor provideConsultaIncidenciaInteractor(DataSourceLocal dataSourceLocal,
                                                                      DataSourceRemote dataSourceRemote,
                                                                      PreferenceManager preferenceManager) {
        return new ConsultaIncidenciaInteractorImpl(dataSourceLocal, dataSourceRemote, preferenceManager);
    }

    @ActivityScope
    @Provides
    IRegistroAccesoInteractor provideRegistroAccesoInteractor(DataSourceLocal dataSourceLocal,
                                                              DataSourceRemote dataSourceRemote,
                                                              PreferenceManager preferenceManager) {
        return new RegistroAccesoInteractorImpl(dataSourceLocal, dataSourceRemote, preferenceManager);
    }


    @ActivityScope
    @Provides
    IConsultaAccesoInteractor provideConsultaAccesoInteractor(DataSourceLocal dataSourceLocal,
                                                                  DataSourceRemote dataSourceRemote,
                                                                  PreferenceManager preferenceManager) {
        return new ConsultaAccesoInteractorImpl(dataSourceLocal, dataSourceRemote, preferenceManager);
    }
}