package com.code.foodapp.ui.dailymeal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.code.foodapp.R;
import com.code.foodapp.adapters.DailyMealAdapter;
import com.code.foodapp.connection.ConnectionClass;
import com.code.foodapp.models.DailyMealModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DailyMealFragment extends Fragment {

    RecyclerView recyclerView;
    List<DailyMealModel> dailyMealModers;
    DailyMealAdapter dailyMealAdapter;

    ConnectionClass connectionClass;
    Connection con;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_daily_meal, container, false);

        recyclerView = root.findViewById(R.id.daily_meal_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dailyMealModers = new ArrayList<>();

        loadDailyMeals();

        dailyMealAdapter = new DailyMealAdapter(getContext(), getActivity(), dailyMealModers);
        recyclerView.setAdapter(dailyMealAdapter);

        return root;
    }

    private void loadDailyMeals() {
        connectionClass = new ConnectionClass();
        con = connectionClass.CONN();
        if (con != null) {
            try {
                String query = "SELECT * FROM daily_meal";
                PreparedStatement stmt = con.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String image = rs.getString("image");
                    String name = rs.getString("name");
                    String discount = rs.getString("discount");
                    String description = rs.getString("description");
                    String type = rs.getString("type");

                    DailyMealModel dailyMeal = new DailyMealModel(id, image, name, discount, description, type);
                    dailyMealModers.add(dailyMeal);
                }

                rs.close();
                stmt.close();
                con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
