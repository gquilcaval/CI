package com.dms.tareosoft.data.source.local;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;


import com.dms.tareosoft.data.entities.Tareo;

import io.reactivex.Completable;
import io.reactivex.Single;

import java.util.ArrayList;
import java.util.List;

public interface BaseDao<T> {

    /**
     * Insert an object in the database.
     *
     * @param entity the object to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertVoid(T entity);

    /**
     * Insert an object in the database.
     *
     * @param entity the object to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(T entity);

    /**
     * Insert an array of objects in the database.
     *
     * @param entities the objects to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllVoid(List<T> entities);

    /**
     * Insert an array of objects in the database.
     *
     * @param entities the objects to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(List<T> entities);

    /**
     * Update an object from the database.
     *
     * @param entity the object to be updated
     */
    @Update
    Completable update(T entity);

    /**
     * Delete an object from the database
     *
     * @param entity the object to be deleted
     */
    @Delete
    Single<Integer> delete(T entity);
}
