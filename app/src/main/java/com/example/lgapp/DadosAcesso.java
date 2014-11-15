package com.example.lgapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lgapp.library.DatabaseHandler;
import com.example.lgapp.library.UserFunctions;

import org.json.JSONException;
import org.json.JSONObject;

public class DadosAcesso extends Activity
{
    Button btnAvancar;
    Button btnLogin;
    EditText edtUsuario;
    EditText edtEmail;
    EditText edtSenha;
    EditText edtSenhaNov;

    // JSON Response node names
    private static String KEY_SUCCESS = "success";
    private static String KEY_USUARIO = "usuario";
    private static String KEY_EMAIL = "email";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados_acesso);
        // Importing all assets like buttons, text fields
        edtUsuario = (EditText) findViewById(R.id.EditUsuario_Cadastro);
        edtEmail = (EditText) findViewById(R.id.EditEmail_Cadastro);
        edtSenha = (EditText) findViewById(R.id.EditSenha_Cadastro);
        edtSenhaNov = (EditText) findViewById(R.id.EditSenhaNov_Cadastro);
        btnAvancar = (Button) findViewById(R.id.ButAvancar_Cadastro);
        btnLogin = (Button) findViewById(R.id.ButLogin_Cadastro);
        // Register Button Click event
        btnAvancar.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                String usuario = edtUsuario.getText().toString();
                String email = edtEmail.getText().toString();
                String senha = edtSenha.getText().toString();
                String senhanov = edtSenhaNov.getText().toString();
                if (senhanov.equals(senha))
                {
                    new ProcessLogin().execute();
                }
            }
        });
        // Link to Login Screen
        btnLogin.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                Intent i = new Intent(getApplicationContext(), Login.class);
                startActivity(i);
                // Close Registration View
                finish();
            }
        });
    }

    class ProcessLogin extends AsyncTask<JSONObject, JSONObject, JSONObject>
    {
        String usuario = edtUsuario.getText().toString();
        String email = edtEmail.getText().toString();
        String senha = edtSenha.getText().toString();

        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pDialog = new ProgressDialog(DadosAcesso.this);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected JSONObject doInBackground(JSONObject... args)
        {
            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.VerificaDadosAcesso(usuario, email, senha);
            return json;
        }

        protected void onPostExecute(JSONObject json)
        {
            try
            {
                if (json.getString(KEY_SUCCESS) != null)
                {
                    String res = json.getString(KEY_SUCCESS);
                    if (Integer.parseInt(res) == 1)
                    {
                        // user successfully registred
                        // Store user details in SQLite Database
                        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                        db.addUsuario(usuario, email);
                        // Launch Dashboard Screen
                        Intent dashboard = new Intent(getApplicationContext(), DadosPessoais.class);
                        // Close all views before launching Dashboard
                        dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pDialog.dismiss();
                        startActivity(dashboard);
                        // Close Registration Screen
                        finish();
                    }
                    else
                    {
                        // Error in registration
                        Toast.makeText(getApplication(), "Ocorreu um erro no cadastro!", Toast.LENGTH_LONG).show();
                    }
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}
