package com.code.foodapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.code.foodapp.MainActivity;
import com.code.foodapp.R;
import com.code.foodapp.connection.ConnectionClass;
import com.code.foodapp.models.UserModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private ConnectionClass connectionClass;
    private Connection con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.login_email);
        edtPassword = findViewById(R.id.login_password);
        connectionClass = new ConnectionClass();

    }

    public void register(View view) {
        startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
    }

    public void login(View view) {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        authenticateUser(email, password);
    }

    private void authenticateUser(String email, String password) {

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            con = connectionClass.CONN();
            try {
                String query = "SELECT * FROM [dbo].[user] WHERE email = ? AND password = ?";
                PreparedStatement preparedStatement = con.prepareStatement(query);
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, password);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    // Retrieve user details
                    int id = resultSet.getInt("id");
                    String fullname = resultSet.getString("fullname");
                    String address = resultSet.getString("address");
                    String phone = resultSet.getString("phone");
                    boolean isAdmin = resultSet.getBoolean("admin");
                    String createdAt = resultSet.getString("createAt");
                    String updatedAt = resultSet.getString("updateAt");

                    // Create UserModel object
                    UserModel user = new UserModel(id, fullname, email, address, phone, isAdmin, createdAt, updatedAt);

                    // Save user data (for session management or other purposes)
                    saveUserSession(user);

                    runOnUiThread(() -> {
                        Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show());
                }
            } catch (SQLException e) {
                runOnUiThread(() -> Toast.makeText(LoginActivity.this, "SQL Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void saveUserSession(UserModel user) {
        // Example: Save user to SharedPreferences for session management
        SharedPreferences sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("user_id", user.getId());
        editor.putString("user_fullname", user.getFullname());
        editor.putString("user_email", user.getEmail());
        editor.putString("user_phone", user.getEmail());
        editor.putString("user_address", user.getEmail());
        editor.putString("user_isAdmin", user.getEmail());
        editor.apply();
    }

    public static int getUserIdFromSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_session", MODE_PRIVATE);
        return sharedPreferences.getInt("user_id", -1); // -1 or any default value you want
    }

}
