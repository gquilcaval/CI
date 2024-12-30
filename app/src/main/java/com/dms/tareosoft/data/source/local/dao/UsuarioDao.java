package com.dms.tareosoft.data.source.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.dms.tareosoft.data.entities.Usuario;
import com.dms.tareosoft.data.models.UsuarioPerfil;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface UsuarioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[]insertAll(List<Usuario> entities);

    @Query("DELETE FROM Usuario")
    void deleteAll();

    @Query("SELECT * FROM Usuario " +
            "INNER JOIN Empleado ON Empleado.id = Usuario.idEmpleado " +
            "INNER JOIN Perfil ON Perfil.idPerfil = Usuario.fkPerfil  " +
            "WHERE  usuario = :username and  clave = :password LIMIT 1")
    Maybe<UsuarioPerfil> validUser(String username, String password);
}
