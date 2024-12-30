package com.dms.tareosoft.domain.settings_interactor;

import com.dms.tareosoft.data.entities.ClaseTareo;
import com.dms.tareosoft.data.entities.ConceptoTareo;
import com.dms.tareosoft.data.entities.DetalleTareo;
import com.dms.tareosoft.data.entities.Empleado;
import com.dms.tareosoft.data.entities.NivelTareo;
import com.dms.tareosoft.data.entities.Perfil;
import com.dms.tareosoft.data.entities.Tareo;
import com.dms.tareosoft.data.entities.Turno;
import com.dms.tareosoft.data.entities.UnidadMedida;
import com.dms.tareosoft.data.entities.Usuario;
import com.dms.tareosoft.domain.models.RespuestaGeneral;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Call;

public interface ISettingsInteractor {

    Single<RespuestaGeneral> validarConexion();

    Call<String> obtenerFechaHora();

    /*Batch*/
    Observable<List<Usuario>> cargarUsuarios();

    Observable<List<Perfil>> cargarPerfiles();

    Observable<List<NivelTareo>> cargarNiveles();

    Observable<List<ClaseTareo>> cargarClases();

    Observable<List<ConceptoTareo>> cargarConceptos();

    Observable<List<Tareo>> cargarTareosAbiertos(int usuario);

    Observable<List<DetalleTareo>> cargarDetalleTareosAbiertos(int usuario);

    Observable<List<UnidadMedida>> cargarUnidadMedida();

    Observable<List<Turno>> cargarTurnos();

    Observable<List<Empleado>> cargarEmpleados();

    /*Linea*/
    Observable<List<ClaseTareo>> listarClaseTareo();

    Single<List<Turno>> listarTurnos();

    Single<List<UnidadMedida>> listarUnidadMedidas();
}
