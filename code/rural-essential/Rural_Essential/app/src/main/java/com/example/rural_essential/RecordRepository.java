package com.example.rural_essential;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.rural_essential.ui.model.Record;

import java.util.List;

// Used to directly access data of local zoom database
public class RecordRepository {
    private RecordDao recordDao;
    private LiveData<List<Record>> allRecords;

    public RecordRepository(Application application){
        RecordDatabase database = RecordDatabase.getDatabase(application);
        recordDao = database.recordDao();
        new PopulateDbAsyncTask(database).execute();
        allRecords = recordDao.getAll();
    }

    public void insert(Record record){
        new InsertRecordAsyncTask(recordDao).execute(record);
    }

    public void update(Record record){
        new UpdateRecordAsyncTask(recordDao).execute(record);
    }

    public void delete(Record record){
        new DeleteRecordAsyncTask(recordDao).execute(record);
    }

    public void deleteAllRecords(){
        new InsertRecordAsyncTask(recordDao).execute();
    }

    public LiveData<List<Record>> getAllRecords(){
        return allRecords;
    }

    private static class InsertRecordAsyncTask extends AsyncTask<Record,Void,Void> {
        private RecordDao recordDao;

        private InsertRecordAsyncTask(RecordDao recordDao){
            this.recordDao = recordDao;
        }

        @Override
        protected Void doInBackground(Record... records) {
            recordDao.insert(records[0]);
            return null;
        }
    }

    private static class UpdateRecordAsyncTask extends AsyncTask<Record,Void,Void> {
        private RecordDao recordDao;

        private UpdateRecordAsyncTask(RecordDao recordDao){
            this.recordDao = recordDao;
        }

        @Override
        protected Void doInBackground(Record... records) {
            recordDao.update(records[0]);
            return null;
        }
    }

    private static class DeleteRecordAsyncTask extends AsyncTask<Record,Void,Void> {
        private RecordDao recordDao;

        private DeleteRecordAsyncTask(RecordDao recordDao){
            this.recordDao = recordDao;
        }

        @Override
        protected Void doInBackground(Record... records) {
            recordDao.delete(records[0]);
            return null;
        }
    }

    private static class DeleteAllRecordAsyncTask extends AsyncTask<Void,Void,Void> {
        private RecordDao recordDao;

        private DeleteAllRecordAsyncTask(RecordDao recordDao){
            this.recordDao = recordDao;
        }

        @Override
        protected Void doInBackground(Void... Voids) {
            recordDao.deleteAll();
            return null;
        }
    }

        private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void>{
        private RecordDao recordDao;

        private PopulateDbAsyncTask(RecordDatabase db){
            recordDao = db.recordDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
//            int numOfRecord = recordDao.numOfRecords();
//            if(numOfRecord == 2){
//                Record record = new Record();
//                recordDao.insert(record);
//                recordDao.insert(record);
//            }
            return null;
        }
    }
}
