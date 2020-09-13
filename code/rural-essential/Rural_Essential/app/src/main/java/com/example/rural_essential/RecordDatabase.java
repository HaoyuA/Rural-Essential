package com.example.rural_essential;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.rural_essential.ui.model.Record;

@Database(entities = {Record.class}, version = 1, exportSchema = false)
public abstract class RecordDatabase extends RoomDatabase {
    public abstract RecordDao recordDao();
    private static RecordDatabase INSTANCE;
    public static synchronized RecordDatabase getDatabase(final Context context) {

        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), RecordDatabase.class, "record")
                           .fallbackToDestructiveMigration()
                           //.addCallback(roomCallback)
                           .build();
                }

        return INSTANCE;

        }

//     private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
//         @Override
//         public void onCreate(@NonNull SupportSQLiteDatabase db) {
//             super.onCreate(db);
//             new PopulateDbAsyncTask(INSTANCE).execute();
//         }
//     };
//
//    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void>{
//        private RecordDao recordDao;
//
//        private PopulateDbAsyncTask(RecordDatabase db){
//            recordDao = db.recordDao();
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            String a = "a";
//            recordDao.insert(new Record(
//
//            ));
//            return null;
//        }
//    }


}
