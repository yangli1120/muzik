package crazysheep.io.materialmusic.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.Field;

/**
 * base prefs
 *
 * Created by crazysheep on 15/12/18.
 */
public class BasePrefs {

    public static String PREFS_NAME = "material_music";

    private Context mContext;
    private SharedPreferences mSharedPrefs;

    public BasePrefs(Context context) {
        mContext = context;

        Class<? extends BasePrefs> clazz = getClass();
        try {
            Field field = clazz.getField("PREFS_NAME");

            mSharedPrefs = mContext.getSharedPreferences((String)field.get(null),
                    Context.MODE_PRIVATE);
        } catch (NoSuchFieldException nsfe) {
            nsfe.printStackTrace();

            throw new RuntimeException("can not find PREFS_NAME field");
        } catch (IllegalAccessException iae) {
            iae.printStackTrace();

            throw new RuntimeException("PREFS_NAME field is NOT string");
        }
    }

    public void clear() {
        mSharedPrefs.edit().clear().apply();
    }

    public String getString(String key, String defaultValue) {
        return mSharedPrefs.getString(key, defaultValue);
    }

    public void setString(String key, String value) {
        mSharedPrefs.edit().putString(key, value).apply();
    }

    public int getInt(String key, int defaultValue) {
        return mSharedPrefs.getInt(key, defaultValue);
    }

    public void setInt(String key, int value) {
        mSharedPrefs.edit().putInt(key, value).apply();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return mSharedPrefs.getBoolean(key, defaultValue);
    }

    public void setBoolean(String key, boolean value) {
        mSharedPrefs.edit().putBoolean(key, value).apply();
    }

    public void setLong(String key, long value) {
        mSharedPrefs.edit().putLong(key, value).apply();
    }

    public long getLong(String key, long defaultValue) {
        return mSharedPrefs.getLong(key, defaultValue);
    }

}
