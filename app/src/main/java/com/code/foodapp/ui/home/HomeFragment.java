package com.code.foodapp.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.code.foodapp.R;
import com.code.foodapp.adapters.HomeHorAdapter;
import com.code.foodapp.adapters.HomeVerAdapter;
import com.code.foodapp.adapters.UpdateVerticalRec;
import com.code.foodapp.connection.ConnectionClass;
import com.code.foodapp.models.CategoryModel;
import com.code.foodapp.models.ProductModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class HomeFragment extends Fragment implements UpdateVerticalRec {

    RecyclerView homeHorizontalRec, homeVerticalRec;
    ArrayList<CategoryModel> categoryList;
    HomeHorAdapter categoryAdapter;
    ArrayList<ProductModel> productList;
    HomeVerAdapter homeVerAdapter;
    EditText homeSearch;
    ConnectionClass connectionClass;
    Connection con;

    private TextView txtUserFullName;
    private int currentCategoryId = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        homeHorizontalRec = root.findViewById(R.id.home_hor_rec);
        homeVerticalRec = root.findViewById(R.id.home_ver_rec);
        txtUserFullName = root.findViewById(R.id.fragment_home_welcome);
        homeSearch = root.findViewById(R.id.home_search);

        Bundle args = getArguments();
        if (args != null) {
            String userFullName = args.getString("userFullName", "");
            txtUserFullName.setText("Hello " + userFullName);
        }

        // Horizontal RecyclerView
        categoryList = new ArrayList<>();
        categoryAdapter = new HomeHorAdapter(this, getActivity(), categoryList);
        homeHorizontalRec.setAdapter(categoryAdapter);
        homeHorizontalRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        homeHorizontalRec.setHasFixedSize(true);
        homeHorizontalRec.setNestedScrollingEnabled(false);
        loadCategories();

        // Vertical RecyclerView
        productList = new ArrayList<>();
        homeVerAdapter = new HomeVerAdapter(getContext(), getActivity(), productList);
        homeVerticalRec.setAdapter(homeVerAdapter);
        homeVerticalRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        homeSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProducts(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadCategories();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadCategories() {
        categoryList.clear();

        CategoryModel allCategory = new CategoryModel(0, "All", "/drawable/all");
        categoryList.add(allCategory);

        connectionClass = new ConnectionClass();
        con = connectionClass.CONN();
        if (con != null) {
            try {
                String query = "SELECT * FROM categories";
                PreparedStatement stmt = con.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String imageUrl = rs.getString("image_url");

                    CategoryModel category = new CategoryModel(id, name, imageUrl);
                    categoryList.add(category);
                }

                categoryAdapter.notifyDataSetChanged();

                rs.close();
                stmt.close();
                con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void loadProductsByCategory(int categoryId) {
        productList.clear();
        connectionClass = new ConnectionClass();
        con = connectionClass.CONN();
        if (con != null) {
            try {
                String query = "SELECT * FROM product WHERE category_id = ?";
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setInt(1, categoryId);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    int id = rs.getInt("id");
                    int cId = rs.getInt("category_id");
                    int dailyMealId = rs.getInt("daily_meal_id");
                    String name = rs.getString("name");
                    String description = rs.getString("description");
                    String image = rs.getString("image_url");
                    String timing = rs.getString("timing");
                    String rating = rs.getString("rating");
                    String price = rs.getString("price");
                    String favourite = rs.getString("favourite");

                    ProductModel product = new ProductModel(id, cId, dailyMealId, name, image, timing, rating, price, favourite, description);
                    productList.add(product);
                }

                homeVerAdapter.notifyDataSetChanged();

                rs.close();
                stmt.close();
                con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void filterProducts(String searchText) {
        ArrayList<ProductModel> filteredList = new ArrayList<>();
        if (searchText.isEmpty()) {
            if (currentCategoryId == 0) {
                loadAllProducts();
            } else {
                loadProductsByCategory(currentCategoryId);
            }
        } else {
            for (ProductModel product : productList) {
                if (product.getName().toLowerCase().contains(searchText.toLowerCase())) {
                    filteredList.add(product);
                }
            }
            homeVerAdapter.filterList(filteredList);
        }
    }

    @Override
    public void callBack(int position, ArrayList<ProductModel> list) {
        homeVerAdapter = new HomeVerAdapter(getContext(), getActivity(), list);
        homeVerticalRec.setAdapter(homeVerAdapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void loadAllProducts() {
        productList.clear();
        connectionClass = new ConnectionClass();
        con = connectionClass.CONN();
        if (con != null) {
            try {
                String query = "SELECT * FROM product";
                PreparedStatement stmt = con.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    int id = rs.getInt("id");
                    int cId = rs.getInt("category_id");
                    int dailyMealId = rs.getInt("daily_meal_id");
                    String name = rs.getString("name");
                    String description = rs.getString("description");
                    String image = rs.getString("image_url");
                    String timing = rs.getString("timing");
                    String rating = rs.getString("rating");
                    String price = rs.getString("price");
                    String favourite = rs.getString("favourite");

                    ProductModel product = new ProductModel(id, cId, dailyMealId, name, image, timing, rating, price, favourite, description);
                    productList.add(product);
                }

                homeVerAdapter.notifyDataSetChanged();

                rs.close();
                stmt.close();
                con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void setCurrentCategoryId(int categoryId) {
        this.currentCategoryId = categoryId;
    }

}
