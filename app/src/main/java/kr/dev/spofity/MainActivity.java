package kr.dev.spofity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {


    AppCompatButton btn_login;
    EditText et_email, et_password;
    TextView tv_register;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        initUI();
        clicks();


    }

    private void initUI(){
        btn_login = findViewById(R.id.btn_login);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        tv_register = findViewById(R.id.tv_register);
    }

    private void clicks(){



        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = et_email.getText().toString();
                Intent intent = new Intent(MainActivity.this, PlayerActivity2.class );
                startActivity(intent);
                editor.putString("email",email);

                editor.putBoolean("isLoggedIn", true);
                editor.commit();



            }
        });


    }
}