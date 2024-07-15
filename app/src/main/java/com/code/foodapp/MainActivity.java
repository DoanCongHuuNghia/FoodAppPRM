package com.code.foodapp;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.code.foodapp.R;
import com.code.foodapp.activities.LoginActivity;
import com.code.foodapp.receiver.MyBluetoothReceiver;
import com.code.foodapp.receiver.MyWifiReceiver;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.code.foodapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private TextView txtUserFullName;
    private TextView txtUserEmail;

    private MyBluetoothReceiver bluetoothReceiver;
    private MyWifiReceiver wifiReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bluetoothReceiver = new MyBluetoothReceiver();
        wifiReceiver = new MyWifiReceiver();

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_daily_meal, R.id.nav_favourite, R.id.nav_my_cart, R.id.nav_maps, R.id.nav_chat)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        setupNavigationHeader(navigationView, navController);

        Intent intent = getIntent();
        if (intent != null) {
            String navigateTo = intent.getStringExtra("navigate_to");
            if ("my_cart".equals(navigateTo)) {
                navController.navigate(R.id.nav_my_cart);
            }
        }
    }

    private void setupNavigationHeader(NavigationView navigationView, NavController navController) {
        // Inflate header layout
        navigationView.inflateHeaderView(R.layout.nav_header_main);
        // Get header views
        txtUserFullName = navigationView.getHeaderView(0).findViewById(R.id.text_fullname);
        txtUserEmail = navigationView.getHeaderView(0).findViewById(R.id.text_email);

        // Retrieve user session data
        SharedPreferences sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
        String userFullName = sharedPreferences.getString("user_fullname", "");
        String userEmail = sharedPreferences.getString("user_email", "");

        // Set user data to navigation header
        txtUserFullName.setText(userFullName);
        txtUserEmail.setText(userEmail);

        Bundle bundle = new Bundle();
        bundle.putString("userFullName", userFullName);
        navController.navigate(R.id.nav_home, bundle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return false;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void logout(View view) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().clear().apply();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
        startActivity(intent);
        finish();
    }

    public String getUserFullName() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
        return sharedPreferences.getString("user_fullname", "");
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(bluetoothReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(bluetoothReceiver);
        unregisterReceiver(wifiReceiver);
    }
}