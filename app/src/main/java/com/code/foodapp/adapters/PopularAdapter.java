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
import com.code.foodapp.models.ProductModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.ViewHolder> {

    List<ProductModel> list;

    Activity activity;

    public PopularAdapter(List<ProductModel> list, Activity activity) {
        this.list = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public PopularAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_hor_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PopularAdapter.ViewHolder holder, int position) {

        ProductModel product = list.get(position);
        Picasso.get().load(Uri.parse("android.resource://" + activity.getPackageName() + product.getImage())).into(holder.imageView);
        holder.name.setText(list.get(position).getName());
        holder.desc.setText(list.get(position).getDescription());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name, desc;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.favourite_img);
            name = itemView.findViewById(R.id.favourite_name);
            desc = itemView.findViewById(R.id.favourite_des);
        }
    }
}
