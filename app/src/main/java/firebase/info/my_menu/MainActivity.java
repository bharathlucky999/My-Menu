package firebase.info.my_menu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {

    private FirebaseAuth fire;
    private TextView email, password, Register, forgot;
    private Button Submit;
    String user_email, user_password;
    ProgressDialog pd;
    Intent i, intent;
    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = (TextView) findViewById(R.id.editText);
        password = (TextView) findViewById(R.id.editText2);

        Register = (TextView) findViewById(R.id.register);
        Submit = (Button) findViewById(R.id.login);
        forgot = (TextView) findViewById(R.id.forgot_password);


        Register.setOnClickListener(this);
        Submit.setOnClickListener(this);
        forgot.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.login) {
            user_email = email.getText().toString();
            user_password = password.getText().toString();
            fire = FirebaseAuth.getInstance();
            fire.signInWithEmailAndPassword(user_email, user_password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Login Successfull", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Not Success", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

            // intent = new Intent(this,dashboard.class);
        }




        else if (id == R.id.register) {
            i = new Intent(MainActivity.this, register.class);
            startActivity(i);
             this.overridePendingTransition(R.anim.rightswipe, android.R.anim.fade_out);

        }

        else if (id == R.id.forgot_password) {
            Intent intent_fpwd = new Intent(MainActivity.this,forgotpwd.class);
            startActivity(intent_fpwd);
            //this.overridePendingTransition(R.anim.leftswipe, android.R.anim.fade_out);
        }
    }
}