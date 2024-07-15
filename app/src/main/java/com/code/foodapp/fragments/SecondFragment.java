package com.code.foodapp.fragments;

import static com.code.foodapp.R.id.popular_hor_rec;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.code.foodapp.R;
import com.code.foodapp.adapters.PopularAdapter;
import com.code.foodapp.adapters.PopularVerAdapter;
import com.code.foodapp.connection.ConnectionClass;
import com.code.foodapp.models.ProductModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SecondFragment extends Fragment {

    List<ProductModel> productList;
    RecyclerView recyclerView;
    PopularAdapter popularAdapter;

    RecyclerView recyclerViewVer;
    PopularVerAdapter popularVerAdapter;

    ConnectionClass connectionClass;
    Connection con;

    public SecondFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_second, container, false);

        //Featured Hor RecyclerView

        recyclerView = view.findViewById(popular_hor_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

        productList = getProductsByFavourite("Popular");

        popularAdapter = new PopularAdapter(productList, getActivity());
        recyclerView.setAdapter(popularAdapter);

        //Featured Ver RecyclerView
        recyclerViewVer = view.findViewById(R.id.popular_ver_rec);
        recyclerViewVer.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        productList =  getProductsByFavourite("Popular");

        popularVerAdapter = new PopularVerAdapter(productList, getActivity(), getContext());
        recyclerViewVer.setAdapter(popularVerAdapter);

        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    public List<ProductModel> getProductsByFavourite(String Favourite) {
        connectionClass = new ConnectionClass();
        con = connectionClass.CONN();
        List<ProductModel> list = new ArrayList<>();
        if (con != null) {
            try {
                String query = "SELECT * FROM product WHERE favourite = ?";
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setString(1, Favourite);
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

                    ProductModel product = new ProductModel(id, cId, dailyMealId2, name, imageUrl, timing, rating, price, favourite, description);
                    list.add(product);
                }
                rs.close();
                stmt.close();
                con.close();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return list;
    }
}