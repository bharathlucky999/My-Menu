package firebase.info.my_menu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;




public class forgotpwd extends AppCompatActivity implements View.OnClickListener{

    private EditText email,phone;
    private Button retrieve,cancel;
    String mail,phne;
    ProgressDialog pd;
    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpwd);

        email = (EditText)findViewById(R.id.c_password);
        phone = (EditText)findViewById(R.id.r_phone);

        retrieve = (Button)findViewById(R.id.retrieve);
        retrieve.setOnClickListener(this);

        cancel = (Button)findViewById(R.id.cancel_fp);
        cancel.setOnClickListener(this);

        //Animation Left = AnimationUtils.loadAnimation(forgotpwd.this, R.anim.leftswipe);
       // retrieve.startAnimation(Left);
        //cancel.startAnimation(Left);
    }

    @Override
    public void onClick(View v) {

        if(v.getId()== R.id.retrieve) {

            mail = email.getText().toString();
            phne = phone.getText().toString();

            new retrivepwd().execute();
            i = new Intent(this, MainActivity.class);
        }
        else if(v.getId()==R.id.cancel_fp){
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            this.overridePendingTransition(R.anim.rightswipe, android.R.anim.fade_out);

            finish();
        }
    }

    class retrivepwd extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String url_s = "https://bacteriological-bus.000webhostapp.com/emailtest.php?email="+ URLEncoder.encode(mail,"UTF-8")+"&phone="+URLEncoder.encode(phne,"UTF-8");
                URL url = new URL(url_s);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }

        }

        protected void onPostExecute(String response) {

            pd.dismiss();
            if (response == null) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            } else  if(response.contains("Updated")){
                Toast.makeText(getApplicationContext(), "Email Sent", Toast.LENGTH_LONG).show();
                startActivity(i);


            }
            else if(response.contains("Invalid Details")){
                Toast.makeText(getApplicationContext(), "Please Check your Details", Toast.LENGTH_LONG).show();

            }
            else{
                Toast.makeText(getApplicationContext(),"Please try after sometime",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd= ProgressDialog.show(forgotpwd.this,"","Please Wait",false);
        }
    }
}