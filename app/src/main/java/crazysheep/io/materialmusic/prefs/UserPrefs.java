package crazysheep.io.materialmusic.prefs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import crazysheep.io.materialmusic.bean.LoginDto;

/**
 * prefs for user
 *
 * Created by crazysheep on 15/12/18.
 */
public class UserPrefs extends BasePrefs {

    public static String PREFS_NAME = "user_prefs";

    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_USER_TOKEN = "user_token";
    public static final String KEY_USER_EXPIRE = "user_expire";
    public static final String KEY_USER_NAME = "user_name";
    public static final String KEY_USER_EMAIL = "user_email";

    public static final long ILLEGAL_USER_ID = -1;

    public UserPrefs(Context context) {
        super(context);
    }

    public boolean checkLogin() {
        return getUserId() > 0 && !TextUtils.isEmpty(getUserName())
                && !TextUtils.isEmpty(getEmail())
                && !TextUtils.isEmpty(getToken())
                && !TextUtils.isEmpty(getExpire());
    }

    public void saveLoginData(@NonNull LoginDto loginDto) {
        setUserId(loginDto.user_id);
        setUserName(loginDto.user_name);
        setToken(loginDto.token);
        setEmail(loginDto.email);
        setExpire(loginDto.expire);
    }

    public void setUserId(long userId) {
        setLong(KEY_USER_ID, userId);
    }

    public long getUserId() {
        return getLong(KEY_USER_ID, ILLEGAL_USER_ID);
    }

    public void setToken(String token) {
        setString(KEY_USER_TOKEN, token);
    }

    public String getToken() {
        return getString(KEY_USER_TOKEN, null);
    }

    public void setExpire(String expire) {
        setString(KEY_USER_EXPIRE, expire);
    }

    public String getExpire() {
        return getString(KEY_USER_EXPIRE, null);
    }

    public void setUserName(String name) {
        setString(KEY_USER_NAME, name);
    }

    public String getUserName() {
        return getString(KEY_USER_NAME, null);
    }

    public void setEmail(String email) {
        setString(KEY_USER_EMAIL, email);
    }

    public String getEmail() {
        return getString(KEY_USER_EMAIL, null);
    }

}
