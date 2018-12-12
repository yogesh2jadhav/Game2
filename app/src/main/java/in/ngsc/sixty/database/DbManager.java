package in.ngsc.sixty.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;

public class DbManager extends SQLiteOpenHelper {

    private final static String DbName = "myAppDb";
    private final static int DbVersion = 1;
    /**
     * Database Instance *
     */
    private static volatile DbManager sInstance = null;

    /**
     * SqliteDatabase Object *
     */
    private static SQLiteDatabase sSqliteDatabase;


    private Context mContext;

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS TutorialsPoint(correctAnswers INT,wrongAnswer INT, createdDate DATE);");
        Date currentDate = new Date();
        db.execSQL("INSERT INTO TutorialsPoint VALUES(0,0,"+currentDate.getTime()+");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        int versionCounter = oldVersion;
        while (versionCounter <= newVersion) {
            switch (versionCounter) {
                case 2:
                    //Add updates here per version.

                    break;
            }
            versionCounter++;
        }
    }

    private DbManager(Context context) {
        super(context, DbName, null, DbVersion);
    }

    public static synchronized DbManager getInstance(Context context) throws SQLiteException {
        if (sInstance == null) {

            sInstance = new DbManager(context);
            sSqliteDatabase = getWriteableDatabse();
            if(sSqliteDatabase!=null) {
                sSqliteDatabase.enableWriteAheadLogging();
            }
        }
        if (sSqliteDatabase == null) {
            sSqliteDatabase = getWriteableDatabse();
            if(sSqliteDatabase!=null) {
                sSqliteDatabase.enableWriteAheadLogging();
            }
        }

        return sInstance;
    }

    private static SQLiteDatabase getWriteableDatabse() {
        return sInstance.getWritableDatabase();
    }

    public synchronized long executeQuery(ArrayList<? extends ExecuteQuery> commands) {
        long value = -1;

        try {
            sSqliteDatabase.beginTransactionNonExclusive();
            try {
                value = 1;//Jalpesh : in case we need to check for success : else it is alway returning -1 in case of batch transactions this is not the real value .
                for (ExecuteQuery item : commands) {
                    if (item.executeQuery(sSqliteDatabase) == 0) {
                        value = -1;
                    }
                }
                sSqliteDatabase.setTransactionSuccessful();

            } finally {
                sSqliteDatabase.endTransaction();
            }

        } catch (SQLiteException ex) {
            ex.printStackTrace();
            return -1;
        }

        return value;
    }

    /**
     * Executes a Database command.
     *
     * @param query command to execute
     * @return result id return value
     */
    public synchronized long executeQuery(ExecuteQuery query) {

        long value = -1;
        try {
            value = query.executeQuery(sSqliteDatabase);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return value;

    }

    synchronized ISelectCommand fillData(String query, ISelectCommand selectCommand, boolean dontClose) {
        Cursor cursor = null;
        try {
            cursor = sSqliteDatabase.rawQuery(query, null);
            selectCommand.fillData(cursor);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null && !dontClose) {
                cursor.close();
            }
        }
        return selectCommand;
    }

    synchronized Cursor fillData(String query) {
        Cursor cursor = null;
        try {
            cursor = sSqliteDatabase.rawQuery(query, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return cursor;
    }

    synchronized ISelectCommand fillData(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, ISelectCommand selectCommand) {
        Cursor cursor = null;
        try {
            cursor = sSqliteDatabase.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
            selectCommand.fillData(cursor);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return selectCommand;

    }


}
