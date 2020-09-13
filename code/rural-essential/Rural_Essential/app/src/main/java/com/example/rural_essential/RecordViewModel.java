package com.example.rural_essential;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.rural_essential.ui.model.Record;

import java.util.List;

// Used to access data in room database
public class RecordViewModel extends AndroidViewModel {
    private RecordRepository repository;
    private LiveData<List<Record>> allRecords;

    public RecordViewModel(@NonNull Application application) {
        super(application);
        repository = new RecordRepository(application);
        allRecords = repository.getAllRecords();
    }

    public void insert(Record record){
        repository.insert(record);
    }

    public void update(Record record){
        repository.update(record);
    }
    public void delete(Record record){
        repository.delete(record);
    }
    public void deleteAllNotes(){
        repository.deleteAllRecords();
    }

    public LiveData<List<Record>> getAllRecords(){
        return allRecords;
    }
}
