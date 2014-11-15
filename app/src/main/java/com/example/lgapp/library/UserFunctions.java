package com.example.lgapp.library;

import android.content.Context;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinicius on 11/14/2014.
 */
public class UserFunctions
{

    private JSONParser jsonParser;

    private static String loginURL = "http://lgapp.besaba.com/LGApp/";
    private static String registerURL = "http://lgapp.besaba.com/LGApp/";

    private static String login_tag = "login";
    private static String verifica_dados_acesso_tag = "dados_acesso";
    private static String cadastro_tag = "cadastrar";

    // constructor
    public UserFunctions()
    {
        jsonParser = new JSONParser();
    }

    public JSONObject loginUsuario(String usuario, String senha)
    {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("usuario", usuario));
        params.add(new BasicNameValuePair("senha", senha));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        return json;
    }

    public JSONObject VerificaDadosAcesso(String usuario, String email, String senha)
    {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", verifica_dados_acesso_tag));
        params.add(new BasicNameValuePair("usuario", usuario));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("senha", senha));
        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
        // return json
        return json;
    }

    public JSONObject FinalizaCadastro(String nome, String datanasc, String altura, JSONObject esportes)
    {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", cadastro_tag));
        params.add(new BasicNameValuePair("nome", nome));
        params.add(new BasicNameValuePair("datanasc", datanasc));
        params.add(new BasicNameValuePair("altura", altura));
        params.add(new BasicNameValuePair("esportes", String.valueOf(esportes)));
        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
        // return json
        return json;
    }

    /**
     * Function get Login status
     */
    public boolean isUserLoggedIn(Context context)
    {
        DatabaseHandler db = new DatabaseHandler(context);
        int count = db.getRowCount();
        if (count > 0)
        {
            // user logged in
            return true;
        }
        return false;
    }

    /**
     * Function to logout user
     * Reset Database
     */
    public boolean logoutUser(Context context)
    {
        DatabaseHandler db = new DatabaseHandler(context);
        db.resetTables();
        return true;
    }
}