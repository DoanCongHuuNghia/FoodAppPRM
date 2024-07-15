package com.code.foodapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.code.foodapp.MainActivity;
import com.code.foodapp.R;
import com.code.foodapp.connection.ConnectionClass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegistrationActivity extends AppCompatActivity {


    ConnectionClass connectionClass = new ConnectionClass();
    Connection con;
    String str;
    EditText edtFullname, edtEmail, edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        edtFullname = findViewById(R.id.register_fullname);
        edtEmail = findViewById(R.id.register_email);
        edtPassword = findViewById(R.id.register_password);
    }

    public void login(View view) {
        startActivity( new Intent(RegistrationActivity.this, LoginActivity.class));
    }

    public void register(View view) {
        String fullname = edtFullname.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (fullname.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            con = connectionClass.CONN();
            if (con != null) {
                try {
                    String checkQuery = "SELECT COUNT(*) AS count FROM [dbo].[user] WHERE email = ?";
                    PreparedStatement checkStatement = con.prepareStatement(checkQuery);
                    checkStatement.setString(1, email);
                    ResultSet resultSet = checkStatement.executeQuery();
                    resultSet.next();
                    int count = resultSet.getInt("count");

                    if (count > 0) {
                        str = "Email already exists";
                        runOnUiThread(() -> Toast.makeText(this, str, Toast.LENGTH_SHORT).show());
                    } else {
                        String insertQuery = "INSERT INTO [dbo].[user] (fullname, email, password, createAt, updateAt) VALUES (?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";
                        PreparedStatement insertStatement = con.prepareStatement(insertQuery);
                        insertStatement.setString(1, fullname);
                        insertStatement.setString(2, email);
                        insertStatement.setString(3, password);
                        insertStatement.executeUpdate();

                        str = "Registration successful";
                        runOnUiThread(() -> {
                            Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                        });
                    }

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                str = "Error: Unable to connect to database";
                runOnUiThread(() -> Toast.makeText(this, str, Toast.LENGTH_SHORT).show());
            }
        });
    }
}