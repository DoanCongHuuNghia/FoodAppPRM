package com.code.foodapp.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.code.foodapp.MainActivity;
import com.code.foodapp.R;
import com.code.foodapp.activities.BillingActivity;
import com.code.foodapp.activities.LoginActivity;
import com.code.foodapp.adapters.CartAdapter;
import com.code.foodapp.connection.ConnectionClass;
import com.code.foodapp.models.CartModel;
import com.code.foodapp.models.DailyMealModel;

import net.sourceforge.jtds.jdbc.DateTime;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MyCartFragment extends Fragment {

    List<CartModel> list;
    CartAdapter cartAdapter;
    RecyclerView recyclerView;
    TextView totalTextView;
    ConnectionClass connectionClass;
    private Button btnOrder;
    Connection con;

    public MyCartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_cart, container, false);

        recyclerView = view.findViewById(R.id.cart_rec);
        totalTextView = view.findViewById(R.id.my_cart_total);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        int userId = LoginActivity.getUserIdFromSharedPreferences(getContext());
        list = getCartUser(userId);

        cartAdapter = new CartAdapter(list, getActivity());
        recyclerView.setAdapter(cartAdapter);

        double totalPrice = cartAdapter.calculateTotalPrice();
        totalTextView.setText(String.format("$%.1f", totalPrice));

        btnOrder = view.findViewById(R.id.btn_order);

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder orderDetails = new StringBuilder();
                StringBuilder orderPrices = new StringBuilder();
                for (CartModel cart : list) {
                    orderDetails.append(cart.getQuantity())
                            .append(" x ")
                            .append(cart.getName())
                            .append("\n")
                            .append("\n");
                    orderPrices.append(String.format("$%.1f", Double.parseDouble(cart.getPrice())))
                            .append("\n")
                            .append("\n");
                }
                Intent intent = new Intent(requireContext(), BillingActivity.class);
                intent.putExtra("orderDetails", orderDetails.toString());
                intent.putExtra("orderPrices", orderPrices.toString());
                intent.putExtra("totalAmount", totalPrice);
                startActivity(intent);
            }
        });

        return view;
    }

    private List<CartModel> getCartUser(int userId) {
        connectionClass = new ConnectionClass();
        con = connectionClass.CONN();
        List<CartModel> listCart = new ArrayList<>();
        if (con != null) {
            try {
                String query = "SELECT * FROM ShoppingCart WHERE user_id = ?";
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    int id = rs.getInt("id");
                    int pId = rs.getInt("product_id");
                    int uId = rs.getInt("user_id");
                    int quantity = rs.getInt("quantity");
                    String price = rs.getString("price");
                    Date createdAt = rs.getDate("created_at");

                    String productQuery = "SELECT * FROM product WHERE id = ?";
                    PreparedStatement productStmt = con.prepareStatement(productQuery);
                    productStmt.setInt(1, pId);
                    ResultSet productRs = productStmt.executeQuery();
                    if (productRs.next()) {
                        String image = productRs.getString("image_url");
                        String name = productRs.getString("name");
                        String rating = productRs.getString("rating");

                        CartModel cartModel = new CartModel(id, pId, uId, image, name, quantity, price, rating, createdAt);
                        listCart.add(cartModel);
                    }
                    productRs.close();
                    productStmt.close();
                }

                rs.close();
                stmt.close();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return listCart;
    }

}