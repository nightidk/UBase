package com.example.universitybase;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.net.URLEncoder;
import java.util.Objects;

import android.util.Log;
import android.os.AsyncTask;

public class ServerRequests extends AsyncTask<String, String, String> {

    private HashMap<String, String> params;

    public ServerRequests() {
        params = new HashMap<String, String>();
    }


    private StringBuilder paramsToStringBuilderLogin(String login, String password) {
        params.put("login", login);
        params.put("password", password);

        StringBuilder sbParams = new StringBuilder();
        int i = 0;
        for (String key : params.keySet()) {
            try {
                if (i != 0)
                    sbParams.append("&");
                sbParams.append(key).append("=");
                sbParams.append((String)(URLEncoder.encode(params.get(key), "UTF-8")));
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
            i++;
        }
        params.clear();

        return sbParams;
    }

    private StringBuilder paramsToStringBuilderRegistration(String login, String password, String name) {
        params.put("login", login);
        params.put("password", password);
        params.put("name", name);

        StringBuilder sbParams = new StringBuilder();
        int i = 0;
        for (String key : params.keySet()) {
            try {
                if (i != 0)
                    sbParams.append("&");
                sbParams.append(key).append("=");
                sbParams.append((String)(URLEncoder.encode(params.get(key), "UTF-8")));
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
            i++;
        }
        params.clear();

        return sbParams;
    }

    private StringBuilder paramsToStringBuilderDepartamentsGet(String name) {
        params.put("name", name);

        StringBuilder sbParams = new StringBuilder();
        int i = 0;
        for (String key : params.keySet()) {
            try {
                if (i != 0)
                    sbParams.append("&");
                sbParams.append(key).append("=");
                sbParams.append((String)(URLEncoder.encode(params.get(key), "UTF-8")));
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
            i++;
        }
        params.clear();

        return sbParams;
    }

    private StringBuilder paramsToStringBuilderDepartamentsGetAll() {

        StringBuilder sbParams = new StringBuilder();
        int i = 0;
        for (String key : params.keySet()) {
            try {
                if (i != 0)
                    sbParams.append("&");
                sbParams.append(key).append("=");
                sbParams.append((String)(URLEncoder.encode(params.get(key), "UTF-8")));
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
            i++;
        }
        params.clear();

        return sbParams;
    }

    private StringBuilder paramsToStringBuilderDepartamentsAdd(String faculty, String departament) {
        params.put("faculty", faculty);
        params.put("departament", departament);

        StringBuilder sbParams = new StringBuilder();
        int i = 0;
        for (String key : params.keySet()) {
            try {
                if (i != 0)
                    sbParams.append("&");
                sbParams.append(key).append("=");
                sbParams.append((String)(URLEncoder.encode(params.get(key), "UTF-8")));
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
            i++;
        }
        params.clear();

        return sbParams;
    }

    private StringBuilder paramsToStringBuilderDepartamentsDelete(String id) {
        params.put("id", id);

        StringBuilder sbParams = new StringBuilder();
        int i = 0;
        for (String key : params.keySet()) {
            try {
                if (i != 0)
                    sbParams.append("&");
                sbParams.append(key).append("=");
                sbParams.append((String)(URLEncoder.encode(params.get(key), "UTF-8")));
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
            i++;
        }
        params.clear();

        return sbParams;
    }

    private StringBuilder paramsToStringBuilderDepartamentsEdit(String id, String newname) {
        params.put("id", String.valueOf(id));
        params.put("newname", newname);

        StringBuilder sbParams = new StringBuilder();
        int i = 0;
        for (String key : params.keySet()) {
            try {
                if (i != 0)
                    sbParams.append("&");
                sbParams.append(key).append("=");
                sbParams.append((String)(URLEncoder.encode(params.get(key), "UTF-8")));
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
            i++;
        }
        params.clear();

        return sbParams;
    }

    private StringBuilder paramsToStringBuilderStudentsGet(String name) {
        params.put("name", name);

        StringBuilder sbParams = new StringBuilder();
        int i = 0;
        for (String key : params.keySet()) {
            try {
                if (i != 0)
                    sbParams.append("&");
                sbParams.append(key).append("=");
                sbParams.append((String)(URLEncoder.encode(params.get(key), "UTF-8")));
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
            i++;
        }
        params.clear();

        return sbParams;
    }

    private StringBuilder paramsToStringBuilderStudentsGetAll() {

        StringBuilder sbParams = new StringBuilder();
        int i = 0;
        for (String key : params.keySet()) {
            try {
                if (i != 0)
                    sbParams.append("&");
                sbParams.append(key).append("=");
                sbParams.append((String)(URLEncoder.encode(params.get(key), "UTF-8")));
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
            i++;
        }
        params.clear();

        return sbParams;
    }

    private StringBuilder paramsToStringBuilderStudentsAdd(String name, String faculty, String departament, String date) {
        params.put("name", name);
        params.put("faculty", faculty);
        params.put("departament", departament);
        params.put("date", date);

        StringBuilder sbParams = new StringBuilder();
        int i = 0;
        for (String key : params.keySet()) {
            try {
                if (i != 0)
                    sbParams.append("&");
                sbParams.append(key).append("=");
                sbParams.append((String)(URLEncoder.encode(params.get(key), "UTF-8")));
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
            i++;
        }
        params.clear();

        return sbParams;
    }

    private StringBuilder paramsToStringBuilderStudentsDelete(String id) {
        params.put("id", id);

        StringBuilder sbParams = new StringBuilder();
        int i = 0;
        for (String key : params.keySet()) {
            try {
                if (i != 0)
                    sbParams.append("&");
                sbParams.append(key).append("=");
                sbParams.append((String)(URLEncoder.encode(params.get(key), "UTF-8")));
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
            i++;
        }
        params.clear();

        return sbParams;
    }

    private StringBuilder paramsToStringBuilderStudentsCheckDate(String id, String date) {
        params.put("id", id);
        params.put("date", date);

        StringBuilder sbParams = new StringBuilder();
        int i = 0;
        for (String key : params.keySet()) {
            try {
                if (i != 0)
                    sbParams.append("&");
                sbParams.append(key).append("=");
                sbParams.append((String)(URLEncoder.encode(params.get(key), "UTF-8")));
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
            i++;
        }
        params.clear();

        return sbParams;
    }

    private StringBuilder paramsToStringBuilderStudentsEditDate(String param, String id, String date) {
        params.put("param", param);
        params.put("id", id);
        params.put("date", date);

        StringBuilder sbParams = new StringBuilder();
        int i = 0;
        for (String key : params.keySet()) {
            try {
                if (i != 0)
                    sbParams.append("&");
                sbParams.append(key).append("=");
                sbParams.append((String)(URLEncoder.encode(params.get(key), "UTF-8")));
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
            i++;
        }
        params.clear();

        return sbParams;
    }

    private StringBuilder paramsToStringBuilderStudentsEdit(String id, String name, String date) {
        params.put("id", id);
        params.put("newname", name);
        params.put("newdate", date);

        StringBuilder sbParams = new StringBuilder();
        int i = 0;
        for (String key : params.keySet()) {
            try {
                if (i != 0)
                    sbParams.append("&");
                sbParams.append(key).append("=");
                sbParams.append((String)(URLEncoder.encode(params.get(key), "UTF-8")));
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
            i++;
        }
        params.clear();

        return sbParams;
    }

    private StringBuilder paramsToStringBuilderFacultyGet() {

        StringBuilder sbParams = new StringBuilder();
        int i = 0;
        for (String key : params.keySet()) {
            try {
                if (i != 0)
                    sbParams.append("&");
                sbParams.append(key).append("=");
                sbParams.append((String)(URLEncoder.encode(params.get(key), "UTF-8")));
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
            i++;
        }
        params.clear();

        return sbParams;
    }

    private StringBuilder paramsToStringBuilderFacultyAdd(String name) {
        params.put("name", name);

        StringBuilder sbParams = new StringBuilder();
        int i = 0;
        for (String key : params.keySet()) {
            try {
                if (i != 0)
                    sbParams.append("&");
                sbParams.append(key).append("=");
                sbParams.append((String)(URLEncoder.encode(params.get(key), "UTF-8")));
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
            i++;
        }
        params.clear();

        return sbParams;
    }

    private StringBuilder paramsToStringBuilderFacultyDelete(String id) {
        params.put("id", id);

        StringBuilder sbParams = new StringBuilder();
        int i = 0;
        for (String key : params.keySet()) {
            try {
                if (i != 0)
                    sbParams.append("&");
                sbParams.append(key).append("=");
                sbParams.append((String)(URLEncoder.encode(params.get(key), "UTF-8")));
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
            i++;
        }
        params.clear();

        return sbParams;
    }

    private StringBuilder paramsToStringBuilderFacultyEdit(String id, String name) {
        params.put("id", id);
        params.put("newname", name);

        StringBuilder sbParams = new StringBuilder();
        int i = 0;
        for (String key : params.keySet()) {
            try {
                if (i != 0)
                    sbParams.append("&");
                sbParams.append(key).append("=");
                sbParams.append((String)(URLEncoder.encode(params.get(key), "UTF-8")));
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
            i++;
        }
        params.clear();

        return sbParams;
    }

    ////////////////////////////// Попытка подключения к серверу ///////////////////////////////////
    private HttpURLConnection getConnection(String path) {
        try {
            String url = "http://185.251.90.153/api/" + path;
            URL urlObj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("Authorization", "hxoTR9Z5t7feByHoBvh3L2joe7LxrLH1DxeTaMYJ");

            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);

            conn.connect();
            return conn;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////// Отправка POST запроса на сервер //////////////////////////////////
    private void sendPost(HttpURLConnection conn, StringBuilder sbParams) {
        try{
            if (MainActivity.getLogin() != null)
                sbParams.append("&login=").append(MainActivity.getLogin());

            String paramsString = sbParams.toString();

            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(paramsString);
            wr.flush();
            wr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////// Получение ответа от сервера /////////////////////////////////////
    private String getReceive(HttpURLConnection conn) {
        StringBuilder result = new StringBuilder();
        try {
            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            Log.d("test", "result from server: " + result.toString());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return result.toString();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    protected String doInBackground(String... params) {
        String path = params[0];
        //Log.e("path", path);
        StringBuilder sbParams = null;
        if (Objects.equals(path, "login")) {
            String login = params[1];
            String password = params[2];
            sbParams = paramsToStringBuilderLogin(login, password);
        } else if (Objects.equals(path, "register")) {
            String login = params[1];
            String password = params[2];
            String name = params[3];
            sbParams = paramsToStringBuilderRegistration(login, password, name);
        } else if (Objects.equals(path, "departament/get")) {
            String name = params[1];
            sbParams = paramsToStringBuilderDepartamentsGet(name);
        } else if (Objects.equals(path, "departaments/get")) {
            sbParams = paramsToStringBuilderDepartamentsGetAll();
        } else if (Objects.equals(path, "departament/add")) {
            String faculty = params[1];
            String departament = params[2];
            sbParams = paramsToStringBuilderDepartamentsAdd(faculty, departament);
        } else if (Objects.equals(path, "departament/edit")) {
            String id = params[1];
            String newname = params[2];
            sbParams = paramsToStringBuilderDepartamentsEdit(String.valueOf(id), newname);
        } else if (Objects.equals(path, "departament/delete")) {
            String id = params[1];
            sbParams = paramsToStringBuilderDepartamentsDelete(String.valueOf(id));
        } else if (Objects.equals(path, "faculty/get")) {
            sbParams = paramsToStringBuilderFacultyGet();
        } else if (Objects.equals(path, "faculty/add")) {
            String name = params[1];
            sbParams = paramsToStringBuilderFacultyAdd(name);
        } else if (Objects.equals(path, "faculty/edit")) {
            String id = params[1];
            String name = params[2];
            sbParams = paramsToStringBuilderFacultyEdit(id, name);
        } else if (Objects.equals(path, "faculty/delete")) {
            String id = params[1];
            sbParams = paramsToStringBuilderFacultyDelete(id);
        } else if (Objects.equals(path, "students/get")) {
            String name = params[1];
            sbParams = paramsToStringBuilderStudentsGet(name);
        } else if (Objects.equals(path, "students/getAll")){
            sbParams = paramsToStringBuilderStudentsGetAll();
        } else if (Objects.equals(path, "student/add")) {
            String name = params[1];
            String faculty = params[2];
            String departament = params[3];
            String date = params[4];
            sbParams = paramsToStringBuilderStudentsAdd(name, faculty, departament, date);
        } else if (Objects.equals(path, "student/edit")) {
            String id = params[1];
            String name = params[2];
            String date = params[3];
            sbParams = paramsToStringBuilderStudentsEdit(id, name, date);
        } else if (Objects.equals(path, "student/delete")) {
            String id = params[1];
            sbParams = paramsToStringBuilderStudentsDelete(id);
        } else if (Objects.equals(path, "student/get")) {
            String name = params[1];
            sbParams = paramsToStringBuilderStudentsGet(name);
        } else if (Objects.equals(path, "student/editDates")) {
            String param = params[1];
            String id = params[2];
            String date = params[3];
            sbParams = paramsToStringBuilderStudentsEditDate(param, id, date);
        } else if (Objects.equals(path, "student/checkDate")) {
            String id = params[1];
            String date = params[2];
            sbParams = paramsToStringBuilderStudentsCheckDate(id, date);
        }

        HttpURLConnection conn = getConnection(path);

        sendPost(conn, sbParams);

        return getReceive(conn);
    }

}
