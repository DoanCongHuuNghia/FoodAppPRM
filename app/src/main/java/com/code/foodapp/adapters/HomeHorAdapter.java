package com.code.foodapp.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.code.foodapp.R;
import com.code.foodapp.models.CategoryModel;
import com.code.foodapp.ui.home.HomeFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeHorAdapter extends RecyclerView.Adapter<HomeHorAdapter.ViewHolder> {

    UpdateVerticalRec updateVerticalRec;
    Activity activity;
    ArrayList<CategoryModel> list;

    boolean check = true;
    boolean select = true;
    int row_index = -1;

    public HomeHorAdapter(UpdateVerticalRec updateVerticalRec, Activity activity, ArrayList<CategoryModel> list) {
        this.updateVerticalRec = updateVerticalRec;
        this.activity = activity;
        this.list = list;
//        CategoryModel allCategory = new CategoryModel(0, "All", "/drawable/all");
//        this.list.add(0, allCategory);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_horizontal_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        CategoryModel category = list.get(position);
        holder.name.setText(list.get(position).getName());
        Picasso.get()
                .load(Uri.parse("android.resource://" + activity.getPackageName() + category.getImageUrl()))
                .into(holder.imageView);

        if (check) {
            if (category.getId() == 0) {
                // Load all products
                ((HomeFragment) updateVerticalRec).loadAllProducts();
            } else {
                ((HomeFragment) updateVerticalRec).loadProductsByCategory(category.getId());
            }
            check = false;
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                row_index = position;
                notifyDataSetChanged();

                int categoryId = list.get(position).getId();
                HomeFragment homeFragment = (HomeFragment) updateVerticalRec;
                homeFragment.setCurrentCategoryId(categoryId);

                if (category.getId() == 0) {
                    // Load all products
                    ((HomeFragment) updateVerticalRec).loadAllProducts();
                } else {
                    ((HomeFragment) updateVerticalRec).loadProductsByCategory(category.getId());
                }
            }
        });

        if (select) {
            if (position == 0) {
                holder.cardView.setBackgroundResource(R.drawable.change_bg);
                select = false;
            }
        } else {
            if (row_index == position) {
                holder.cardView.setBackgroundResource(R.drawable.change_bg);
            } else {
                holder.cardView.setBackgroundResource(R.drawable.default_bg);
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView name;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.hor_img);
            name = itemView.findViewById(R.id.hor_text);
            cardView = itemView.findViewById(R.id.cartView);
        }
    }
}
