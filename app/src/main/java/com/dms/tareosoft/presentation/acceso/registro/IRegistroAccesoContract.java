package com.dms.tareosoft.presentation.acceso.registro;

import com.dms.tareosoft.base.IBaseContract;
import com.dms.tareosoft.data.entities.Acceso;
import com.dms.tareosoft.data.entities.Incidencia;
import com.dms.tareosoft.data.entities.NivelTareo;

import java.util.List;

/**
 * Created by Giancarlos Quilca
 */
public interface IRegistroAccesoContract {
    interface View extends IBaseContract.View {
        void listarClaseTareos(List<String> lista, int posicion);

        void actualizarVistaNiveles(List<NivelTareo> listaNivel);

        boolean hasTareo();

        void viewResult(Acceso acceso);
    }

    interface Presenter extends IBaseContract.Presenter<IRegistroAccesoContract.View> {
        void registroAcceso(Acceso acceso);

        void obtenerClasesTareo();

        void setClaseTareo(int pos);

        void setNivel1(String idConceptoTareo);

        void setNivel2(String actividad);

        void setNivel3(String msg);

        Incidencia obtenerIncidencia();

        String getPerfilUser();
    }
}
