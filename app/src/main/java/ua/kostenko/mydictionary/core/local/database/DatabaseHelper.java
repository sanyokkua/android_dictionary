package ua.kostenko.mydictionary.core.local.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import ua.kostenko.mydictionary.core.local.database.dao.UnitDao;
import ua.kostenko.mydictionary.core.local.database.dao.UserDao;
import ua.kostenko.mydictionary.core.local.database.domain.Unit;

public final class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String TAG = DatabaseHelper.class.getSimpleName();
    private static final String DB_NAME = "app.db";
    private static final int DB_VERSION = 1;

    private UnitDao unitDao;
    private UserDao userDao;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(@NonNull final SQLiteDatabase database, @NonNull final ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Unit.class);
        } catch (SQLException e) {
            Log.e(TAG, "Error with table creation", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(@NonNull final SQLiteDatabase database, @NonNull final ConnectionSource connectionSource,
                          final int oldVersion, final int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Unit.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            Log.e(TAG, "Error with table upgrade", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        super.close();
        unitDao = null;
        userDao = null;
    }

    @NonNull
    public UnitDao getUnitDao() {
        if (unitDao == null) {
            //TODO: create dao
            throw new RuntimeException("Not implemented");
        }
        return unitDao;
    }

    @NonNull
    public UserDao getUserDao() {
        if (userDao == null) {
            //TODO: create dao
            throw new RuntimeException("Not implemented");
        }
        return userDao;
    }
}
