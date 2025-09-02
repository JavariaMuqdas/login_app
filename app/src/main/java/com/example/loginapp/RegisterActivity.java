package com.example.loginapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity {

    EditText etusername, etpassword, etPassword2;
    Button btnRegister, btnGoToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etusername = findViewById(R.id.etUsername);
        etpassword = findViewById(R.id.etPassword);
        etPassword2 = findViewById(R.id.etPassword2);
        btnRegister = findViewById(R.id.btnRegister);
        btnGoToLogin = findViewById(R.id.btnGoToLogin);

        btnRegister.setOnClickListener(v -> {
            String Username = etusername.getText().toString().trim();
            String Password = etpassword.getText().toString().trim();
            String ConfirmPassword = etPassword2.getText().toString().trim();

            if (Username.isEmpty() || Password.isEmpty() || ConfirmPassword.isEmpty()) {
                Toast.makeText(this, "Please enter Username and Password", Toast.LENGTH_SHORT).show();
            } else if (!Password.equals(ConfirmPassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            } else {
                registerUser(Username, Password);
            }
        });

        btnGoToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void registerUser(String username, String password) {
        new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2/login_app/register.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                String postData = "username=" + username + "&password=" + password;
                OutputStream os = conn.getOutputStream();
                os.write(postData.getBytes());
                os.flush();
                os.close();

                InputStream is = conn.getInputStream();
                int ch;
                StringBuilder sb = new StringBuilder();
                while ((ch = is.read()) != -1) {
                    sb.append((char) ch);
                }
                is.close();

                JSONObject response = new JSONObject(sb.toString());
                boolean success = response.getBoolean("success");
                String message = response.getString("message");

                runOnUiThread(() -> {
                    Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_LONG).show();
                    if (success) {
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() ->
                        Toast.makeText(RegisterActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
            }
        }).start();
    }
}
