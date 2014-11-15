package com.example.lgapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lgapp.library.DatabaseHandler;
import com.example.lgapp.library.UserFunctions;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends Activity
{
    Button btnEntrar;
    Button btnRegistrar;
    Button btnEsqueceuSenha;
    EditText edtUsuario;
    EditText edtSenha;

    // JSON Response node names
    private static String KEY_SUCCESS = "success";
    private static String KEY_NOME = "nome";
    private static String KEY_EMAIL = "email";
    private static String KEY_USUARIO = "usuario";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsuario = (EditText) findViewById(R.id.EditUsuario_Login);
        edtSenha = (EditText) findViewById(R.id.EditSenha_Login);
        btnEntrar = (Button) findViewById(R.id.ButEntrar_Login);
        btnRegistrar = (Button) findViewById(R.id.ButRegistrar_Login);
        btnEsqueceuSenha = (Button) findViewById(R.id.ButEsqueceuSenha_Login);

        // Login button Click Event
        btnEntrar.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                String usuario = edtUsuario.getText().toString();
                String senha = edtSenha.getText().toString();
                UserFunctions userFunction = new UserFunctions();
                JSONObject json = userFunction.loginUsuario(usuario, senha);
                // check for login response
                try
                {
                    if (json.getString(KEY_SUCCESS) != null)
                    {
                        String res = json.getString(KEY_SUCCESS);
                        if (Integer.parseInt(res) == 1)
                        {
                            // user successfully logged in
                            // Store user details in SQLite Database
                            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                            JSONObject json_user = json.getJSONObject("usuario");
                            // Clear all previous data in database
                            userFunction.logoutUser(getApplicationContext());
                            db.addUsuario(json.getString(KEY_USUARIO), json_user.getString(KEY_EMAIL));
                            // Launch Dashboard Screen
                            Intent menu = new Intent(getApplicationContext(), Menu.class);
                            // Close all views before launching Dashboard
                            menu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(menu);
                            // Close Login Screen
                            finish();
                        }
                        else
                        {
                            // Error in login
                            Toast.makeText(getApplication(), "Incorrect username/password", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                Intent i = new Intent(getApplicationContext(), DadosAcesso.class);
                startActivity(i);
                finish();
            }
        });

        btnEsqueceuSenha.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(), EsqueceuSenha.class);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
