package in.ngsc.sixty.database;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * The Class InsertCommand.
 */
public class InsertCommand extends ExecuteQuery {

    /**
     * The _table name.
     */
    private String _tableName;

    /**
     * The _content values.
     */
    private ContentValues _contentValues;

    /**
     * Instantiates a new insert command.
     *
     * @param tableName the table name
     */
    public InsertCommand(String tableName) {
        _tableName = tableName;
        _contentValues = new ContentValues();
    }

    /**
     * Instantiates a new insert command.
     *
     * @param tableName     the table name
     * @param contentValues the content values
     */
    public InsertCommand(String tableName, ContentValues contentValues) {
        _tableName = tableName;
        _contentValues = contentValues;
    }

    /**
     * Insert.
     *
     * @param column the column
     * @param value  the value
     */
    public void insert(String column, String value) {
		/*if(value!= null && _tableName.equals(QueryBuilder.TABLE_UPLOAD) ) {
			_contentValues.put(column, DatabaseUtils.sqlEscapeString(value));
		}
		else */
        {
            _contentValues.put(column, value);
        }
    }

    /**
     * Insert.
     *
     * @param column the column
     * @param value  the value
     */
    public void insert(String column, int value) {
        _contentValues.put(column, value);
    }

    /**
     * Insert.
     *
     * @param column the column
     * @param value  the value
     */
    public void insert(String column, long value) {
        _contentValues.put(column, value);
    }

    /**
     * Insert.
     *
     * @param column the column
     * @param value  the value
     */
    public void insert(String column, float value) {
        _contentValues.put(column, value);
    }

    /**
     * Insert.
     *
     * @param column the column
     * @param value  the value
     */
    public void insert(String column, double value) {
        _contentValues.put(column, value);
    }

    /**
     * Insert.
     *
     * @param column the column
     * @param value  the value
     */
    public void insert(String column, boolean value) {
        _contentValues.put(column, value);
    }

    /* (non-Javadoc)
     * @see com.ril.jio.jiosdk.database.ExecuteQuery#executeQuery(SQLiteDatabase)
     */
    long executeQuery(SQLiteDatabase db) {
        //return db.insert(_tableName, null, _contentValues);
        return db.insertWithOnConflict(_tableName, null, _contentValues, SQLiteDatabase.CONFLICT_REPLACE);
    }
}
