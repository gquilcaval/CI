package com.dms.tareosoft.injection;

import com.dms.tareosoft.injection.modules.PresentationModule;
import com.dms.tareosoft.injection.scope.ActivityScope;
import com.dms.tareosoft.presentation.acceso.consulta.ConsultarAccesoFragment;
import com.dms.tareosoft.presentation.acceso.registro.RegistroAccesoFragment;
import com.dms.tareosoft.presentation.activities.login.LoginActivity;
import com.dms.tareosoft.presentation.activities.splash.SplashActivity;
import com.dms.tareosoft.presentation.asistencia.consulta.ConsultarAsistenciaFragment;
import com.dms.tareosoft.presentation.asistencia.registro.RegistroAsistenciaActivity;
import com.dms.tareosoft.presentation.asistencia.registro.definicion.AsistenciaDefinicionFragment;
import com.dms.tareosoft.presentation.asistencia.registro.empleado.AsistenciaEmpleadoFragment;
import com.dms.tareosoft.presentation.incidencia.RegistroIncidenciaFragment;
import com.dms.tareosoft.presentation.incidencia.consulta.ConsultarIncidenciaFragment;
import com.dms.tareosoft.presentation.menu.MenuActivity;
import com.dms.tareosoft.presentation.activities.search.SearchActivity;
import com.dms.tareosoft.presentation.fragments.settings.SettingActivity;
import com.dms.tareosoft.presentation.fragments.sync.SyncFragment;
import com.dms.tareosoft.presentation.tareoNew.tareoNewNotEmployer.NewTareoNotEmployerActivity;
import com.dms.tareosoft.presentation.tareoNew.tareoNewNotEmployer.definicion.DefNewTareoNotEmployerFragment;
import com.dms.tareosoft.presentation.tareoNew.tareoNewNotEmployer.opciones.OpcNewTareoNotEmployerFragment;
import com.dms.tareosoft.presentation.tareoConsulta.TareoConsultaFragment;
import com.dms.tareosoft.presentation.tareoConsulta.tareo_consulta_details.TareoConsultaDetailsActivity;
import com.dms.tareosoft.presentation.tareo_control.ListaTareoControlFragment;
import com.dms.tareosoft.presentation.tareo_control.nuevo.tab_empleado.ControlTareoFragment;
import com.dms.tareosoft.presentation.tareo_control.nuevo.tab_tareo.EmpleadoControlFragment;
import com.dms.tareosoft.presentation.tareoEditar.tareoEditAdmin.EditarTareoActivity;
import com.dms.tareosoft.presentation.tareoEditar.tareoEditAdmin.definicion.DefinicionEditarFragment;
import com.dms.tareosoft.presentation.tareoEditar.tareoEditAdmin.empleado.EmpleadoEditarFragment;
import com.dms.tareosoft.presentation.tareoEditar.tareoEditAdmin.opciones.OpcionesEditarFragment;
import com.dms.tareosoft.presentation.tareoEditar.tareoEditUser.EditarEmpleadosActivity;
import com.dms.tareosoft.presentation.tareo_finalizar.finalizar_empleado.FinalizarEmpleadoActivity;
import com.dms.tareosoft.presentation.tareo_finalizar.finalizar_masivo.FinalizarMasivoActivity;
import com.dms.tareosoft.presentation.tareo_finalizar.finalizar_masivo.tareo_reubicar_masivo_list.TareoReubicarMasivoListActivity;
import com.dms.tareosoft.presentation.tareo_finalizar.finalizar_masivo.tareo_reubicar_masivo_por_destajo.TareoReubicarMasivoPorDestajoActivity;
import com.dms.tareosoft.presentation.tareo_finalizar.finalizar_masivo.tareo_reubicar_masivo_por_destajo.tareo_reubicar_masivo_cantidad_por_destajo.TareoReubicarMasivoPorDestajoCantidadFragment;
import com.dms.tareosoft.presentation.tareo_finalizar.finalizar_masivo.tareo_reubicar_masivo_por_destajo.tareo_reubicar_masivo_finalizar_por_destajo.TareoReubicarMasivoPorDestajoFinalizarFragment;
import com.dms.tareosoft.presentation.tareo_finalizar.PorFinalizarFragment;
import com.dms.tareosoft.presentation.tareo_finalizar.finalizar_empleado.tab_empleados.TabEmpleadoFragment;
import com.dms.tareosoft.presentation.tareo_finalizar.finalizar_empleado.tab_tareo.TabTareoFragment;
import com.dms.tareosoft.presentation.tareo_principal.finalizados.FinalizadosFragment;
import com.dms.tareosoft.presentation.tareo_principal.iniciados.IniciadosFragment;
import com.dms.tareosoft.presentation.tareoNew.tareo_nuevo.NuevoTareoActivity;
import com.dms.tareosoft.presentation.tareoNew.tareo_nuevo.definicion.DefinicionFragment;
import com.dms.tareosoft.presentation.tareoNew.tareo_nuevo.empleado.EmpleadoFragment;
import com.dms.tareosoft.presentation.tareoNew.tareo_nuevo.opciones.OpcionesFragment;
import com.dms.tareosoft.presentation.tareo_resultado.ListaResultadosFragment;
import com.dms.tareosoft.presentation.tareo_resultado.resultadoPorEmpleado.ResultadoPorEmpleadoActivity;
import com.dms.tareosoft.presentation.tareo_resultado.resultadoPorTareo.ResultadoPorTareoActivity;
import com.dms.tareosoft.presentation.tareoReubicar.PagerReubicarFragment;
import com.dms.tareosoft.presentation.tareoReubicar.tabsTareo.porEmpleado.ListReubicarPorEmpleadoFragment;
import com.dms.tareosoft.presentation.tareoReubicar.tabsTareo.porTareo.ListReubicarPorTareoFragment;
import com.dms.tareosoft.presentation.tareoReubicar.tareo_reubicar_list_all_tareos.TareoReubicarListActivity;
import com.dms.tareosoft.presentation.tareoReubicar.tareo_segun_resultado.tareo_reubicar_tipo_empleado.ReubicarTipoEmpleadoActivity;
import com.dms.tareosoft.presentation.tareoReubicar.tareo_segun_resultado.tareo_reubicar_tipo_empleado.reubicar_tipo_empleado_cantidad.ReubicarTipoEmpleadoCantidadFragment;
import com.dms.tareosoft.presentation.tareoReubicar.tareo_segun_resultado.tareo_reubicar_tipo_empleado.reubicar_tipo_empleado_finalizar.ReubicarTipoEmpleadoFinalizarFragment;
import com.dms.tareosoft.presentation.tareoReubicar.tareo_segun_resultado.tareo_reubicar_tipo_tareo.ReubicarTipoTareoActivity;
import com.dms.tareosoft.presentation.tareoReubicar.tareo_segun_resultado.tareo_reubicar_tipo_tareo.reubicar_tipo_tareo_cantidad.ReubicarTipoTareoCantidadFragment;
import com.dms.tareosoft.presentation.tareoReubicar.tareo_segun_resultado.tareo_reubicar_tipo_tareo.reubicar_tipo_tareo_finalizar.ReubicarTipoTareoFinalizarFragment;

import dagger.Component;

@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = PresentationModule.class)
public interface ActivityComponent {

    void inject(LoginActivity activity);

    void inject(SettingActivity activity);

    void inject(MenuActivity activity);

    void inject(NuevoTareoActivity activity);

    void inject(EditarTareoActivity activity);

    void inject(FinalizarMasivoActivity activity);

    void inject(ResultadoPorEmpleadoActivity activity);

    void inject(FinalizarEmpleadoActivity activity);

    void inject(EditarEmpleadosActivity activity);

    void inject(ReubicarTipoTareoActivity activity);

    void inject(ReubicarTipoEmpleadoActivity activity);

    void inject(TareoReubicarListActivity activity);

    void inject(TareoReubicarMasivoListActivity activity);

    void inject(TareoReubicarMasivoPorDestajoActivity activity);

    void inject(SearchActivity activity);

    void inject(TareoConsultaDetailsActivity activity);

    void inject(ResultadoPorTareoActivity activity);

    void inject(NewTareoNotEmployerActivity activity);


    void inject(SyncFragment fragment);

    void inject(IniciadosFragment fragment);

    void inject(FinalizadosFragment fragment);

    void inject(DefinicionFragment fragment);

    void inject(EmpleadoFragment fragment);

    void inject(OpcionesFragment fragment);

    void inject(DefinicionEditarFragment fragment);

    void inject(EmpleadoEditarFragment fragment);

    void inject(OpcionesEditarFragment fragment);

    void inject(PorFinalizarFragment fragment);

    void inject(ListaResultadosFragment fragment);

    void inject(ListaTareoControlFragment fragment);

    void inject(TabEmpleadoFragment fragment);

    void inject(TabTareoFragment fragment);

    void inject(EmpleadoControlFragment fragment);

    void inject(ControlTareoFragment fragment);

    void inject(PagerReubicarFragment fragment);

    void inject(ReubicarTipoTareoFinalizarFragment fragment);

    void inject(ReubicarTipoTareoCantidadFragment fragment);

    void inject(ReubicarTipoEmpleadoFinalizarFragment fragment);

    void inject(ReubicarTipoEmpleadoCantidadFragment fragment);

    void inject(TareoReubicarMasivoPorDestajoCantidadFragment fragment);

    void inject(TareoReubicarMasivoPorDestajoFinalizarFragment fragment);

    void inject(TareoConsultaFragment fragment);

    void inject(ListReubicarPorEmpleadoFragment fragment);

    void inject(ListReubicarPorTareoFragment fragment);

    void inject(OpcNewTareoNotEmployerFragment fragment);

    void inject(DefNewTareoNotEmployerFragment fragment);

    void inject(ConsultarAsistenciaFragment fragment);

    void inject(RegistroAsistenciaActivity fragment);

    void inject(AsistenciaEmpleadoFragment fragment);

    void inject(AsistenciaDefinicionFragment fragment);

    void inject(SplashActivity activity);

    void inject(RegistroIncidenciaFragment fragment);

    void inject(ConsultarIncidenciaFragment fragment);

    void inject(RegistroAccesoFragment fragment);

    void inject(ConsultarAccesoFragment fragment);

}
