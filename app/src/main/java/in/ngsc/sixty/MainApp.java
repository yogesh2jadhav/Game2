package in.ngsc.sixty;

import android.app.Application;

import in.ngsc.sixty.database.DbManager;

public class MainApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        createDatabase();
    }

    private void createDatabase() {
        DbManager.getInstance(this);

    }
}
