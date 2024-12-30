package com.dms.tareosoft.presentation.asistencia.registro.definicion;

import com.dms.tareosoft.base.IBaseContract;
import com.dms.tareosoft.data.entities.Marcacion;
import com.dms.tareosoft.data.entities.NivelTareo;

import java.util.List;

public interface IAsistenciaDefinicionContract {

    interface View extends IBaseContract.View {
        void listarClaseTareos(List<String> lista, int posicion);

        void actualizarVistaNiveles(List<NivelTareo> listaNivel);

        boolean hasTareo();
    }

    interface Presenter extends IBaseContract.Presenter<View> {

        void obtenerClasesTareo();

        void setClaseTareo(int pos);

        void setNivel1(String idConceptoTareo);

        void setNivel2(String idConceptoTareo);

        Marcacion obtenerTareo();

        String getPerfilUser();
    }
}
