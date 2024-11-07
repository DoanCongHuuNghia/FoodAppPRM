package com.code.foodapp.adapters;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.code.foodapp.R;
import com.code.foodapp.models.CartModel;
import com.code.foodapp.models.ProductModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    List<CartModel> list;

    Activity activity;

    public CartAdapter(List<CartModel> list, Activity activity) {
        this.list = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_cart_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        CartModel cart = list.get(position);
        Picasso.get().load(Uri.parse("android.resource://" + activity.getPackageName() + cart.getImage())).into(holder.imageView);
        holder.name.setText(list.get(position).getName());
        Double price = Double.parseDouble(list.get(position).getPrice()) * list.get(position).getQuantity();
        holder.price.setText(String.valueOf(price));
        holder.rating.setText(list.get(position).getRating());
        holder.quantity.setText(String.valueOf(cart.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name, price, rating, quantity;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.my_cart_img);
            name = itemView.findViewById(R.id.my_cart_name);
            price = itemView.findViewById(R.id.my_cart_price);
            rating = itemView.findViewById(R.id.my_cart_rating);
            quantity = itemView.findViewById(R.id.my_cart_quantity);
        }
    }

    public double calculateTotalPrice() {
        double totalPrice = 0;
        for (CartModel cart : list) {
            double itemPrice = Double.parseDouble(cart.getPrice()) * cart.getQuantity();
            totalPrice += itemPrice;
        }
        return totalPrice;
    }
}
