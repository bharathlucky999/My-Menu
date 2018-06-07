package firebase.info.my_menu;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Random;


public class register extends AppCompatActivity implements View.OnClickListener {

    private EditText Name,Email,Password,Confirm_Password;
    private Button Confirm;
    private TextView Login;
    private ProgressBar pb;
    Intent intent;
    int pwd_flag=0;
    AlertDialog.Builder dialog;
    String pwd,cpwd,name,email,phone;
    ProgressDialog pd;
    private FirebaseAuth fire;
    byte[] encrypted;

    // Encrypt
    private BigInteger p;
    private BigInteger q;
    private BigInteger N;
    private BigInteger phi;
    private BigInteger e;
    private BigInteger d;
    private int bitlength = 1024;
    private Random r;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Name = (EditText)findViewById(R.id.editText3);
        Email = (EditText)findViewById(R.id.editText4);
        Password = (EditText)findViewById(R.id.editText6);
        Confirm_Password = (EditText)findViewById(R.id.editText7);

        Confirm = (Button)findViewById(R.id.signup);
        Login = (TextView)findViewById(R.id.login_signup);

        Confirm.setOnClickListener(this);
        Login.setOnClickListener(this);



        pb = (ProgressBar)findViewById(R.id.pb);
        pb.setProgress(0);

        Password.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void afterTextChanged(Editable s) {

                strengthcalc();

            }
        });

        dialog = new AlertDialog.Builder(this);
        dialog.setCancelable(false);
        dialog.setTitle("Your Menu Getting Ready");
        dialog.setMessage("Successfully Registered" );
        dialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                startActivity(intent);
                finish();
            }
        });


    }


    public boolean validateEmail( String v_email )
    {
        return v_email.matches( "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+" );
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.signup){
            pwd = Password.getText().toString();
            cpwd = Confirm_Password.getText().toString();
            name = Name.getText().toString();
            email = Email.getText().toString();
            fire = FirebaseAuth.getInstance();

            if(pwd.equals(cpwd) && validateEmail(email) && pwd_flag==0 && (phone.length()==10 )&& !name.equals("") ){
                fire.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful())
                        {
                            Toast.makeText(register.this,"Registered Susscusfully",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(register.this,"Something Wrong. Please Try again ",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                intent = new Intent(this,MainActivity.class);
            }

            else{
                if(pwd_flag!=0){
                    Toast.makeText(getApplicationContext(),"Password should contain atleast 1 Special Character, 1 Capital Letter and a Number",Toast.LENGTH_LONG).show();
                }
                else if(name.equals("")){
                    Toast.makeText(getApplicationContext(), "Invalid Name", Toast.LENGTH_LONG).show();
                }

                else if(!validateEmail(email)){
                    Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_LONG).show();
                }
                else if(!pwd.equals(cpwd))
                    Toast.makeText(getApplicationContext(), "Passwords Don't Match", Toast.LENGTH_LONG).show();

            }

        }

        else if(id == R.id.login_signup){
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
            this.overridePendingTransition(R.anim.leftswipe, android.R.anim.fade_out);

            finish();
        }
    }

    void strengthcalc() {

        pwd = Password.getText().toString();
        int Upper_Count = 0, count = 0, Length_Count = 0, Number_Count = 0, SpecialChar_Count = 0;
        int string_length = pwd.length();

        if (string_length >= 8)
            Length_Count++;

        for (int i = 0; i < string_length; i++) {
            if (pwd.charAt(i) >= 65 && pwd.charAt(i) <= 91) {
                Upper_Count++;
            }
            if (pwd.charAt(i) >= 48 && pwd.charAt(i) <= 57) {
                Number_Count++;
            }

            if ((pwd.charAt(i) >= 33 && pwd.charAt(i) <= 44) || (pwd.charAt(i) == 64)) {
                SpecialChar_Count++;
            }
        }

        count = Upper_Count + Length_Count + Number_Count + SpecialChar_Count;

        if (Length_Count < 1 || Upper_Count < 1 || Number_Count < 1 || SpecialChar_Count < 1) {
            pb.setProgress(1);
            pwd_flag = 1;
        } else if (count < 5) {
            pwd_flag = 0;
            pb.setProgress(30);
        } else if (count < 6) {
            pwd_flag = 0;
            pb.setProgress(60);

        } else if (count >= 6) {
            pwd_flag = 0;
            pb.setProgress(100);
        }

    }

}