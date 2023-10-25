package com.example.my1;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback, SearchView.OnQueryTextListener {

    private GoogleMap mMap;
    private SearchView mSearchView;
    private FusedLocationProviderClient mFusedLocationClient;
    private Location mLastKnownLocation;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onDestroy() {
        mMap.clear();
        super.onDestroy();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        // Get the SearchView and set the OnQueryTextListener
        mSearchView = view.findViewById(R.id.search_view);
        mSearchView.setOnQueryTextListener(this);

        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map_container);
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Request permission to access the user's location
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                mLastKnownLocation = location;
                                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                            }
                        }
                    });
        } else {
            // Request permission to access the user's location
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // Perform the search functionality here
        // You can use the Geocoder class to get the location coordinates
        // and move the map's camera to the searched location
        Geocoder geocoder = new Geocoder(getActivity());
        try {
            List<Address> addresses = geocoder.getFromLocationName(query, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                mMap.addMarker(new MarkerOptions().position(latLng).title(query));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            } else {
                // Handle case where no results were found
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // Handle the text change event here
        // You can update the search suggestions based on the new text
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                    mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
                    mFusedLocationClient.getLastLocation()
                            .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    // Got last known location. In some rare situations this can be null.
                                    if (location != null) {
                                        mLastKnownLocation = location;
                                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                                    }
                                }
                            });
                }
            } else {
                // Permission denied
            }
        }
    }
}