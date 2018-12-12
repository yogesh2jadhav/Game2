package in.ngsc.sixty.database;

import android.database.Cursor;

/**
 * The Interface ISelectCommand.
 */
public interface ISelectCommand {

    /**
     * Fill data.
     *
     * @param cursor the cursor
     */
    void fillData(Cursor cursor);
}
