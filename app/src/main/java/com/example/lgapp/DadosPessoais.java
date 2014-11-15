package com.example.lgapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lgapp.library.UserFunctions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DadosPessoais extends Activity
{
    Button btnConcluir;
    Button btnVoltar;
    EditText edtNome;
    EditText edtDataNasc;
    EditText edtAltura;
    CheckBox chFutebol, chFutsal, chVolei, chBasquete, chTenis;
    JSONObject JSONesportes;

    // JSON Response node names
    private static String KEY_SUCCESS = "success";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados_pessoais);
        // Importing all assets like buttons, text fields
        edtNome = (EditText) findViewById(R.id.EditNome_Cadastro);
        edtDataNasc = (EditText) findViewById(R.id.EditDataNasc_Cadastro);
        edtAltura = (EditText) findViewById(R.id.EditAltura_Cadastro);
        btnConcluir = (Button) findViewById(R.id.ButConcluir_Cadastro);
        btnVoltar = (Button) findViewById(R.id.ButVoltar_Cadastro);
        chFutebol = (CheckBox) findViewById(R.id.CheckFutebol_Cadastro);
        chFutsal = (CheckBox) findViewById(R.id.CheckFutsal_Cadastro);
        chVolei = (CheckBox) findViewById(R.id.CheckVolei_Cadastro);
        chBasquete = (CheckBox) findViewById(R.id.CheckBasquete_Cadastro);
        chTenis = (CheckBox) findViewById(R.id.CheckTenis_Cadastro);
        // Register Button Click event
        btnConcluir.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                ArrayList esportes = new ArrayList();
                JSONesportes = new JSONObject();
                if (chFutebol.isChecked())
                {
                    esportes.add("Futebol");
                }
                if (chFutsal.isChecked())
                {
                    esportes.add("Futsal");
                }
                if (chBasquete.isChecked())
                {
                    esportes.add("Basquete");
                }
                if (chVolei.isChecked())
                {
                    esportes.add("Volei");
                }
                if (chTenis.isChecked())
                {
                    esportes.add("Tenis");
                }
                for (int i = 0; i < esportes.size(); i++)
                {
                    try
                    {
                        JSONesportes.put("esporte", esportes.get(i));
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                new ProcessCadastro().execute();
            }
        });
        // Link to Login Screen
        btnVoltar.setOnClickListener(new View.OnClickListener()
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

    class ProcessCadastro extends AsyncTask<JSONObject, JSONObject, JSONObject>
    {
        String nome = edtNome.getText().toString();
        String datanasc = edtDataNasc.getText().toString();
        String altura = edtAltura.getText().toString();

        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pDialog = new ProgressDialog(DadosPessoais.this);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected JSONObject doInBackground(JSONObject... args)
        {
            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.FinalizaCadastro(nome, datanasc, altura, JSONesportes);
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
                        Intent dashboard = new Intent(getApplicationContext(), Menu.class);
                        // Close all views before launching Dashboard
                        dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
