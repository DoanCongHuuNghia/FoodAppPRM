package com.code.foodapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.code.foodapp.MainActivity;
import com.code.foodapp.R;
import com.code.foodapp.connection.ConnectionClass;
import com.code.foodapp.ui.home.HomeFragment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BillingActivity extends AppCompatActivity {

    private TextView tvTotalAmount;
    private EditText etCardNumber, etCardExpiry, etCardCVV;
    private Button btnPayNow;
    private Context context;
    ConnectionClass connectionClass;
    Connection con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);

        context = this;

        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        etCardNumber = findViewById(R.id.etCardNumber);
        etCardExpiry = findViewById(R.id.etCardExpiry);
        etCardCVV = findViewById(R.id.etCardCVV);
        btnPayNow = findViewById(R.id.btnPayNow);

        // Get order details from Intent
        String orderDetails = getIntent().getStringExtra("orderDetails");
        String orderPrices = getIntent().getStringExtra("orderPrices");
        double totalAmount = getIntent().getDoubleExtra("totalAmount", 0.0);

        // Set order details to TextViews
        TextView tvItemDetails = findViewById(R.id.tvItemDetails);
        TextView tvSubTotal = findViewById(R.id.tvSubTotal);
        tvItemDetails.setText(orderDetails);
        tvSubTotal.setText(orderPrices);
        tvTotalAmount.setText("Total: $" + totalAmount);

        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    String cardNumber = etCardNumber.getText().toString().trim();
                    String cardExpiry = etCardExpiry.getText().toString().trim();
                    String cardCVV = etCardCVV.getText().toString().trim();
                    Toast.makeText(context, "Payment Successful!", Toast.LENGTH_SHORT).show();
                    int userId = LoginActivity.getUserIdFromSharedPreferences(context);
                    deleteCart(userId);
                    startActivity(new Intent(BillingActivity.this, MainActivity.class));
                }
            }
        });
    }

    private boolean validateInputs() {
        String cardNumber = etCardNumber.getText().toString().trim();
        String cardExpiry = etCardExpiry.getText().toString().trim();
        String cardCVV = etCardCVV.getText().toString().trim();

        if (TextUtils.isEmpty(cardNumber)) {
            etCardNumber.setError("Card number is required");
            return false;
        }

        if (cardNumber.length() != 16 || !TextUtils.isDigitsOnly(cardNumber)) {
            etCardNumber.setError("Enter a valid 16-digit card number");
            return false;
        }

        if (TextUtils.isEmpty(cardExpiry)) {
            etCardExpiry.setError("Expiry date is required");
            return false;
        }

        if (!cardExpiry.matches("(0[1-9]|1[0-2])/[0-9]{2}")) {
            etCardExpiry.setError("Enter a valid expiry date (MM/YY)");
            return false;
        }

        if (TextUtils.isEmpty(cardCVV)) {
            etCardCVV.setError("CVV is required");
            return false;
        }

        if (cardCVV.length() != 3 || !TextUtils.isDigitsOnly(cardCVV)) {
            etCardCVV.setError("Enter a valid 3-digit CVV");
            return false;
        }

        return true;
    }

    private void deleteCart(int userId) {
        connectionClass = new ConnectionClass();
        con = connectionClass.CONN();

        if (con != null) {
            try {
                String deleteQuery = "DELETE FROM ShoppingCart WHERE user_id = ?";
                PreparedStatement deleteStmt = con.prepareStatement(deleteQuery);
                deleteStmt.setInt(1, userId);
                deleteStmt.executeUpdate();
                deleteStmt.close();
                con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
