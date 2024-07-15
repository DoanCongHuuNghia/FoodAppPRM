package com.code.foodapp.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.code.foodapp.R;
import com.code.foodapp.activities.LoginActivity;
import com.code.foodapp.connection.ConnectionClass;
import com.code.foodapp.models.ProductModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class HomeVerAdapter extends RecyclerView.Adapter<HomeVerAdapter.ViewHolder> {

    private BottomSheetDialog bottomSheetDialog;
    private Activity activity;
    private Context context;
    private ArrayList<ProductModel> list;
    private ConnectionClass connectionClass;
    private Connection con;

    public HomeVerAdapter(Context context, Activity activity, ArrayList<ProductModel> list) {
        this.context = context;
        this.activity = activity;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_vertical_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductModel product = list.get(position);
        holder.name.setText(product.getName());
        Picasso.get().load(Uri.parse("android.resource://" + activity.getPackageName() + product.getImage())).into(holder.imageView);
        holder.timing.setText(product.getTiming());
        holder.rating.setText(product.getRating());
        holder.price.setText(product.getPrice());

        holder.itemView.setOnClickListener(v -> showBottomSheetDialog(product));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name, timing, rating, price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ver_img);
            name = itemView.findViewById(R.id.name);
            timing = itemView.findViewById(R.id.timming);
            rating = itemView.findViewById(R.id.rating);
            price = itemView.findViewById(R.id.price);
        }
    }

    private void showBottomSheetDialog(ProductModel product) {
        bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetTheme);
        View sheetView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_layout, null);

        ImageView bottomImg = sheetView.findViewById(R.id.bottom_img);
        TextView bottomName = sheetView.findViewById(R.id.bottom_name);
        TextView bottomDescription = sheetView.findViewById(R.id.bottom_description);
        TextView bottomRating = sheetView.findViewById(R.id.bottom_rating);
        TextView bottomTiming = sheetView.findViewById(R.id.bottom_timing);
        TextView bottomPrice = sheetView.findViewById(R.id.bottom_price);

        bottomName.setText(product.getName());
        bottomDescription.setText(product.getDescription());
        bottomRating.setText(product.getRating());
        bottomTiming.setText(product.getTiming());
        bottomPrice.setText(product.getPrice());
        Picasso.get().load(Uri.parse("android.resource://" + activity.getPackageName() + product.getImage())).into(bottomImg);

        sheetView.findViewById(R.id.add_to_cart).setOnClickListener(v -> {
            int userId = LoginActivity.getUserIdFromSharedPreferences(context);
            if (isProductInCart(product)) {
                updateCart(product.getId(), userId, 1);
            } else {
                addToCart(product, userId);
            }
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
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

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(ArrayList<ProductModel> filteredList) {
        list.clear();
        list.addAll(filteredList);
        notifyDataSetChanged();
    }
}
