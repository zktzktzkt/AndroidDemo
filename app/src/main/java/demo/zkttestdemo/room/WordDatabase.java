package demo.zkttestdemo.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

/**
 * Created by Lenovo on 20/11/09.
 * Description: 
 */
@Database(entities = {Word.class}, version = 1, exportSchema = true)
public abstract class WordDatabase extends RoomDatabase {

    private static WordDatabase INSTANCE;

    public static synchronized WordDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), WordDatabase.class, "word_database")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration() //仅供测试用。每次升级都会清空之前的数据
                    //.addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .build();
        }
        return INSTANCE;
    }

    public abstract WordDao getWordDao();

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE Word ADD COLUMN chinese_meaning_111 TEXT");
        }
    };

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE Word ADD COLUMN chinese_meaning_222 TEXT");
        }
    };

}
