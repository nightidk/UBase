package com.example.universitybase;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;


public class Session {
    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public Session(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
    }

    public void setActionStudent(String student) {
        editor.putString("student", student);
        editor.apply();
    }
    public void setActionFaculty(String faculty) {
        editor.putString("faculty", faculty);
        editor.apply();
    }
    public void setActionDepartament(String departament) {
        editor.putString("departament", departament);
        editor.apply();
    }

    public void setDate(String date) {
        editor.putString("date", date);
        editor.apply();
    }

    public String getAction(String name) {
        String string = sharedPreferences.getString(name, "");
        return string;
    }

    public void setAuth(String login, String password) {
        editor.putString("login", login);
        editor.putString("password", password);
        editor.apply();
        MainActivity.setLogin(login);
    }

    public void logout() {
        editor.clear();
        editor.apply();
        MainActivity.deleteLogin();
    }


    public boolean getAuthStatus() {
        //Log.d("test", sharedPreferences.toString());
        String login = sharedPreferences.getString("login", "");
        String password = sharedPreferences.getString("password", "");

        if (login.length() == 0 || password.length() == 0) {
            return false;
        }

        MainActivity.setLogin(login);
        return true;
    }
}
