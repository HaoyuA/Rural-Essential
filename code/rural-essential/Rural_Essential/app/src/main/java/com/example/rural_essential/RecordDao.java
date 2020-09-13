package com.example.rural_essential;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.rural_essential.ui.model.Record;

import java.util.List;

@Dao
public interface RecordDao {

    @Query("SELECT * FROM record")
    LiveData<List<Record>> getAll();

    @Query("SELECT * FROM record WHERE recordid = :recordid LIMIT 1")
    Record findByID(int recordid);

    @Query("SELECT count(*)  FROM record")
    int numOfRecords();

    @Insert
    void insertAll(Record... records);

    @Insert
    long insert(Record record);

    @Delete
    void delete(Record record);

    @Update
    void update(Record... records);

    @Query("DELETE FROM record")
    void deleteAll();
}
