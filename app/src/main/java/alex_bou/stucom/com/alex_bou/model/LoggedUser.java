package alex_bou.stucom.com.alex_bou.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;

public class LoggedUser extends User {

    private static LoggedUser instance;
    private String token ;
    private String email;
    private transient boolean updated = false;

    private LoggedUser() {}

    public static LoggedUser getInstance() {
        if (instance == null)
            instance = new LoggedUser();
        return instance;
    }

    public static void setInstance(LoggedUser instance) {
        LoggedUser.instance = instance;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        Log.d("infoDebug","token set as: " + token);
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

    public void saveToPrefs(Context context) {
        Context applicationContext = context.getApplicationContext();
        SharedPreferences.Editor editorPrefs = applicationContext.getSharedPreferences(applicationContext.getPackageName(),MODE_PRIVATE).edit();
        Gson gson = new Gson();
        editorPrefs.putString("user",gson.toJson(this));
        editorPrefs.apply();
    }

    public void loadFromPrefs(Context context) {
        Context applicationContext = context.getApplicationContext();
        String user = applicationContext.getSharedPreferences(applicationContext.getPackageName(),MODE_PRIVATE).getString("user",null);
        instance = new Gson().fromJson(user, LoggedUser.class);
    }
}
