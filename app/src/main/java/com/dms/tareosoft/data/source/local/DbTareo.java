package com.dms.tareosoft.data.source.local;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.dms.tareosoft.data.entities.Acceso;
import com.dms.tareosoft.data.entities.BonoModel;
import com.dms.tareosoft.data.entities.ClaseTareo;
import com.dms.tareosoft.data.entities.ClaveDinamicaModel;
import com.dms.tareosoft.data.entities.ConceptoTareo;
import com.dms.tareosoft.data.entities.CultivoModel;
import com.dms.tareosoft.data.entities.DetalleTareo;
import com.dms.tareosoft.data.entities.DetalleTareoControl;
import com.dms.tareosoft.data.entities.Empleado;
import com.dms.tareosoft.data.entities.GrupoModel;
import com.dms.tareosoft.data.entities.Incidencia;
import com.dms.tareosoft.data.entities.Marcacion;
import com.dms.tareosoft.data.entities.NivelTareo;
import com.dms.tareosoft.data.entities.ParametrosModel;
import com.dms.tareosoft.data.entities.ParcelaModel;
import com.dms.tareosoft.data.entities.Perfil;
import com.dms.tareosoft.data.entities.ResultadoPorTareo;
import com.dms.tareosoft.data.entities.ResultadoTareo;
import com.dms.tareosoft.data.entities.Tareo;
import com.dms.tareosoft.data.entities.Turno;
import com.dms.tareosoft.data.entities.UnidadMedida;
import com.dms.tareosoft.data.entities.Usuario;
import com.dms.tareosoft.data.source.local.dao.AccesoDao;
import com.dms.tareosoft.data.source.local.dao.BonoDao;
import com.dms.tareosoft.data.source.local.dao.ClaseTareoDao;
import com.dms.tareosoft.data.source.local.dao.ClaveDinamicaDao;
import com.dms.tareosoft.data.source.local.dao.ConceptosTareoDao;
import com.dms.tareosoft.data.source.local.dao.CultivoDao;
import com.dms.tareosoft.data.source.local.dao.DetalleTareoControlDao;
import com.dms.tareosoft.data.source.local.dao.DetalleTareoDao;
import com.dms.tareosoft.data.source.local.dao.EmpleadosDao;
import com.dms.tareosoft.data.source.local.dao.GrupoDao;
import com.dms.tareosoft.data.source.local.dao.IncidenciaDao;
import com.dms.tareosoft.data.source.local.dao.MarcacionDao;
import com.dms.tareosoft.data.source.local.dao.NivelesTareoDao;
import com.dms.tareosoft.data.source.local.dao.ParametrosDao;
import com.dms.tareosoft.data.source.local.dao.ParcelaDao;
import com.dms.tareosoft.data.source.local.dao.PerfilDao;
import com.dms.tareosoft.data.source.local.dao.ResultadoPorTareoDao;
import com.dms.tareosoft.data.source.local.dao.ResultadoTareoDao;
import com.dms.tareosoft.data.source.local.dao.TareoDao;
import com.dms.tareosoft.data.source.local.dao.TurnoDao;
import com.dms.tareosoft.data.source.local.dao.UnidadMedidaDao;
import com.dms.tareosoft.data.source.local.dao.UsuarioDao;

@Database(entities = {Usuario.class,
        UnidadMedida.class,
        Turno.class,
        Tareo.class,
        ResultadoTareo.class,
        ResultadoPorTareo.class,
        ParcelaModel.class,
        ParametrosModel.class,
        NivelTareo.class,
        GrupoModel.class,
        Empleado.class,
        DetalleTareo.class,
        CultivoModel.class,
        ConceptoTareo.class,
        ClaveDinamicaModel.class,
        ClaseTareo.class,
        BonoModel.class,
        Perfil.class,
        DetalleTareoControl.class,
        Marcacion.class,
        Incidencia.class,
        Acceso.class}, version = 4, exportSchema = false)
public abstract class DbTareo extends RoomDatabase {

    public abstract UsuarioDao usuarioDao();

    public abstract UnidadMedidaDao unidadMedidaDao();

    public abstract TurnoDao turnoDao();

    public abstract ClaseTareoDao claseTareoDao();

    public abstract PerfilDao perfilDao();

    public abstract TareoDao tareoDao();

    public abstract DetalleTareoDao detalleTareoDao();

    public abstract ParcelaDao parcelaDao();

    public abstract ParametrosDao parametrosDao();

    public abstract NivelesTareoDao nivelesTareoDao();

    public abstract GrupoDao grupoDao();

    public abstract EmpleadosDao empleadosDao();

    public abstract CultivoDao cultivoDao();

    public abstract ConceptosTareoDao conceptosTareoDao();

    public abstract ClaveDinamicaDao claveDinamicaDao();

    public abstract ResultadoTareoDao resultadoTareoDao();

    public abstract ResultadoPorTareoDao resultadoPorTareoDao();

    public abstract BonoDao bonoDao();

    public abstract DetalleTareoControlDao detalleTareoControlDao();

    public abstract MarcacionDao marcacionDao();

    public abstract IncidenciaDao incidenciaDao();

    public abstract AccesoDao accesoDao();

    private static volatile DbTareo INSTANCE;

    public static DbTareo getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (DbTareo.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            DbTareo.class, "DB_TAREO.db")
                            .addCallback(sRoomDatabaseCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static Callback sRoomDatabaseCallback =
            new Callback() {
                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                }
            };
}
