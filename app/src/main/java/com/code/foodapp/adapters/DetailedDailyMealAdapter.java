package com.code.foodapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.code.foodapp.R;
import com.code.foodapp.activities.LoginActivity;
import com.code.foodapp.connection.ConnectionClass;
import com.code.foodapp.models.ProductModel;
import com.squareup.picasso.Picasso;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DetailedDailyMealAdapter extends RecyclerView.Adapter<DetailedDailyMealAdapter.ViewHolder> {

    List<ProductModel> list;
    Activity activity;

    Context context;

    ConnectionClass connectionClass;
    Connection con;

    public DetailedDailyMealAdapter(List<ProductModel> list, Activity activity, Context context) {
        this.list = list;
        this.activity = activity;
        this.context = context;
    }


    @NonNull
    @Override
    public DetailedDailyMealAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.detailed_daily_meal_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull DetailedDailyMealAdapter.ViewHolder holder, int position) {
        ProductModel product = list.get(position);
        Picasso.get().load(Uri.parse("android.resource://" + activity.getPackageName() + product.getImage())).into(holder.imageView);
        holder.name.setText(list.get(position).getName());
        holder.description.setText(list.get(position).getDescription());
        holder.timing.setText(list.get(position).getTiming());
        holder.rating.setText(list.get(position).getRating());
        holder.price.setText(list.get(position).getPrice());
        holder.itemView.findViewById(R.id.daily_meal_add_to_cart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int userId = LoginActivity.getUserIdFromSharedPreferences(context);
                if (isProductInCart(product)) {
                    updateCart(product.getId(), userId, 1);
                } else {
                    addToCart(product, userId);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name,price,description,rating,timing;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.detailed_img);
            price = itemView.findViewById(R.id.detailed_price);
            description = itemView.findViewById(R.id.detailed_description);
            rating = itemView.findViewById(R.id.detailed_rating);
            name = itemView.findViewById(R.id.detailed_name);
            timing = itemView.findViewById(R.id.detailed_timing);
        }
    }

    private void addToCart(ProductModel product, int userId) {
        connectionClass = new ConnectionClass();
        con = connectionClass.CONN();

        if (con != null) {
            try {
                String query = "INSERT INTO ShoppingCart (product_id, user_id, quantity, price, created_at) VALUES (?, ?, ?, ?, GETDATE())";
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setInt(1, product.getId());
                stmt.setInt(2, userId);
                stmt.setInt(3, 1);
                stmt.setDouble(4, Double.parseDouble(product.getPrice()));

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    Toast.makeText(context, "Thêm " + product.getName() + " vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Thêm vào giỏ hàng thất bại", Toast.LENGTH_SHORT).show();
                }

                stmt.close();
                con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private boolean isProductInCart(ProductModel product) {
        boolean isInCart = false;
        int userId = LoginActivity.getUserIdFromSharedPreferences(context);
        connectionClass = new ConnectionClass();
        con = connectionClass.CONN();

        if (con != null) {
            try {
                String query = "SELECT COUNT(*) AS count FROM ShoppingCart WHERE product_id = ? AND user_id = ?";
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setInt(1, product.getId());
                stmt.setInt(2, userId);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    int count = rs.getInt("count");
                    isInCart = count > 0;
                }

                stmt.close();
                con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return isInCart;
    }

    private void updateCart(int productId, int userId, int newQuantity) {
        connectionClass = new ConnectionClass();
        con = connectionClass.CONN();

        if (con != null) {
            try {
                String updateQuery = "UPDATE ShoppingCart SET quantity = quantity + ? WHERE product_id = ? AND user_id = ?";
                PreparedStatement updateStmt = con.prepareStatement(updateQuery);
                updateStmt.setInt(1, newQuantity);
                updateStmt.setInt(2, productId);
                updateStmt.setInt(3, userId);

                int rowsAffected = updateStmt.executeUpdate();
                if (rowsAffected > 0) {
                    Toast.makeText(context, "Cập nhật giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Cập nhật giỏ hàng thất bại", Toast.LENGTH_SHORT).show();
                }
                updateStmt.close();
                con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
