package com.code.foodapp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.code.foodapp.MainActivity;
import com.code.foodapp.R;
import com.code.foodapp.adapters.DetailedDailyMealAdapter;
import com.code.foodapp.connection.ConnectionClass;
import com.code.foodapp.models.ProductModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DetailDailyMealActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<ProductModel> productList;
    DetailedDailyMealAdapter detailedDailyMealAdapter;
    ImageView imageView;
    ConnectionClass connectionClass;
    Connection con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_daily_meal);

        FloatingActionButton fabShoppingCart = findViewById(R.id.fab_shopping_cart);
        fabShoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailDailyMealActivity.this, MainActivity.class);
                intent.putExtra("navigate_to", "my_cart");
                startActivity(intent);
            }
        });


        int dailyMealId = getIntent().getIntExtra("type", 0);
        String dailyMealName = getIntent().getStringExtra("name");

        recyclerView = findViewById(R.id.detailed_rec);
        imageView = findViewById(R.id.detailed_image);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productList = new ArrayList<>();
        detailedDailyMealAdapter = new DetailedDailyMealAdapter(productList, this, this);
        recyclerView.setAdapter(detailedDailyMealAdapter);

        switch (dailyMealId) {
            case 1:
                imageView.setImageResource(R.drawable.breakfast);
                getProductsByDailyMealId(dailyMealId);
                break;
            case 2:
                imageView.setImageResource(R.drawable.lunch);
                getProductsByDailyMealId(dailyMealId);
                break;
            case 3:
                imageView.setImageResource(R.drawable.dinner);
                getProductsByDailyMealId(dailyMealId);
                break;
            case 4:
                imageView.setImageResource(R.drawable.sweets);
                getProductsByDailyMealId(dailyMealId);
                break;
            case 5:
                imageView.setImageResource(R.drawable.coffe);
                getProductsByDailyMealId(dailyMealId);
                break;
            default:
                break;
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(dailyMealName);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void getProductsByDailyMealId(int dailyMealId) {
        productList.clear();
        connectionClass = new ConnectionClass();
        con = connectionClass.CONN();

        if (con != null) {
            try {
                String query = "SELECT * FROM product WHERE daily_meal_id = ?";
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setInt(1, dailyMealId);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    int id = rs.getInt("id");
                    int cId = rs.getInt("category_id");
                    int dailyMealId2 = rs.getInt("daily_meal_id");
                    String name = rs.getString("name");
                    String description = rs.getString("description");
                    String imageUrl = rs.getString("image_url");
                    String timing = rs.getString("timing");
                    String rating = rs.getString("rating");
                    String price = rs.getString("price");
                    String favourite = rs.getString("favourite");

                    // Create ProductModel object and add to list
                    ProductModel product = new ProductModel(id, cId, dailyMealId2, name, imageUrl, timing, rating, price, favourite, description);
                    productList.add(product);
                }
                rs.close();
                stmt.close();
                con.close();
                detailedDailyMealAdapter.notifyDataSetChanged();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
