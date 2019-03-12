package android.example.myapplication;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);
        //setContentView(R.layout.layout_login);
        //setContentView(R.layout.activity_main);
    }

    public void toLoginScreen(View v){
        new pageUpdate().execute(R.layout.layout_login);
    }
    public void toRegisterScreen(View v){
        new pageUpdate().execute(R.layout.layout_register);
    }
    public void Logout(View v){
        new pageUpdate().execute(R.layout.layout_login);
    }

    public void signUp(View v){
        EditText name_Text = (EditText) findViewById(R.id.name);
        String name = new String(name_Text.getText().toString());
        EditText email_Text = (EditText) findViewById(R.id.email);
        String email = new String(email_Text.getText().toString());
        EditText password_Text = (EditText) findViewById(R.id.password);
        String password = new String(password_Text.getText().toString());
        if(TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            Toast.makeText(MainActivity.this,"The content cannot be empty",Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            SharedPreferences sf = getSharedPreferences("demo",Context.MODE_PRIVATE);
            SharedPreferences.Editor mEditor = sf.edit();
            mEditor.putString("name",name);
            mEditor.putString("email",email);
            mEditor.putString("password",password);
            mEditor.apply();
            Toast.makeText(MainActivity.this,"Registered successfully !",Toast.LENGTH_SHORT).show();
        }
    }

    public void Login(View v){
        SharedPreferences sf = getSharedPreferences("demo",Context.MODE_PRIVATE);
        String email = sf.getString("email","");
        String password = sf.getString("password","");
        EditText email_Text = (EditText) findViewById(R.id.input_email);
        String input_email = new String(email_Text.getText().toString());
        EditText password_Text = (EditText) findViewById(R.id.input_password);
        String input_password = new String(password_Text.getText().toString());
        if(email.equals(input_email) && password.equals(input_password)){
            new pageUpdate().execute(R.layout.activity_main);
        }
        else{
            Toast.makeText(MainActivity.this,"Account or password error!",Toast.LENGTH_SHORT).show();
        }
    }

    //use AsynchTask
    private class pageUpdate extends AsyncTask<Integer, Void, Integer> {

        @Override
        protected Integer doInBackground(Integer ... integers) {
            return integers[0];
        }

        @Override
        protected void onPostExecute(Integer temp) {
            setContentView(temp);
        }
    }
}
