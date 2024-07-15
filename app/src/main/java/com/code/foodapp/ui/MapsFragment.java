package com.code.foodapp.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.code.foodapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private final int FINE_PERMISSION_CODE = 1;
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private SearchView searchView;

    public MapsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

//        searchView = view.findViewById(R.id.map_search);
//
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());
//
//        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
//        } else {
//            getLastLocation();
//        }
//
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                String location = searchView.getQuery().toString().trim();
//                List<Address> addressList = null;
//                if (!location.isEmpty()) {
//                    Geocoder geocoder = new Geocoder(requireContext());
//                    try {
//                        addressList = geocoder.getFromLocationName(location, 1);
//                    } catch (IOException e) {
//                        Toast.makeText(requireContext(), "Không thể tìm kiếm địa điểm này", Toast.LENGTH_SHORT).show();
//                        e.printStackTrace();
//                    }
//                    if (addressList != null && !addressList.isEmpty()) {
//                        Address address = addressList.get(0);
//                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
//                        mMap.addMarker(new MarkerOptions().position(latLng).title(location));
//                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
//                    } else {
//                        Toast.makeText(requireContext(), "Không tìm thấy địa điểm này", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(requireContext(), "Vui lòng nhập địa điểm cần tìm", Toast.LENGTH_SHORT).show();
//                }
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.map, mapFragment)
                    .commit();
            mapFragment.getMapAsync(this);
        }

        return view;
    }

//    private void performSearch() {
//        String location = searchView.getQuery().toString().trim();
//
//        if (!location.isEmpty()) {
//            Geocoder geocoder = new Geocoder(requireContext());
//            try {
//                List<Address> addressList = geocoder.getFromLocationName(location, 1);
//
//                if (addressList != null && !addressList.isEmpty()) {
//                    Address address = addressList.get(0);
//                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
//                    mMap.addMarker(new MarkerOptions().position(latLng).title(location));
//                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
//                } else {
//                    Toast.makeText(requireContext(), "Không tìm thấy địa điểm này", Toast.LENGTH_SHORT).show();
//                }
//            } catch (IOException e) {
//                Toast.makeText(requireContext(), "Không thể tìm kiếm địa điểm này", Toast.LENGTH_SHORT).show();
//                e.printStackTrace();
//            }
//        } else {
//            Toast.makeText(requireContext(), "Vui lòng nhập địa điểm cần tìm", Toast.LENGTH_SHORT).show();
//        }
//    }

//    private void getLastLocation() {
//        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
//            return;
//        }
//        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
//            @Override
//            public void onSuccess(Location location) {
//                if (location != null) {
//                    currentLocation = location;
//                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//                    mMap.addMarker(new MarkerOptions().position(latLng).title("Vị trí hiện tại"));
//                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
//                } else {
//                    Toast.makeText(requireContext(), "Không thể lấy vị trí hiện tại", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng fptCityDaNang = new LatLng(16.001224, 108.251953); // Tọa độ của Khu đô thị FPT City
        mMap.addMarker(new MarkerOptions().position(fptCityDaNang).title("Khu đô thị FPT City, Ngũ Hành Sơn, Đà Nẵng"));
        float zoomLevel = 10.0f;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(fptCityDaNang, zoomLevel));

        // Cấu hình các cài đặt của bản đồ
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == FINE_PERMISSION_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                getLastLocation();
//            } else {
//                Toast.makeText(requireContext(), "Quyền truy cập vị trí bị từ chối", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
}
