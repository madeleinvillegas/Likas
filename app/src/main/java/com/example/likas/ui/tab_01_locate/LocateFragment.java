package com.example.likas.ui.tab_01_locate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.likas.FacilityPopupActivity;
import com.example.likas.R;
import com.example.likas.classes.Facility;
import com.example.likas.databinding.Tab01LocateBinding;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.CopyrightOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocateFragment extends Fragment {
    private Tab01LocateBinding binding;
    private SharedPreferences mPrefs;
    private MapView mMapView;
    private Context context;
    private MyLocationNewOverlay mLocationOverlay;

    private static final String PREFS_NAME = "org.andnav.osm.prefs";
    private static final String PREFS_TILE_SOURCE = "tilesource";
    private static final String PREFS_LATITUDE_STRING = "latitudeString";
    private static final String PREFS_LONGITUDE_STRING = "longitudeString";
    private static final String PREFS_ZOOM_LEVEL_DOUBLE = "zoomLevelDouble";
    private static final String DB_URL = "https://likas-a4330-default-rtdb.asia-southeast1.firebasedatabase.app/";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = this.getActivity();
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context));

        binding = Tab01LocateBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final DisplayMetrics dm = requireContext().getResources().getDisplayMetrics();

        mMapView = binding.map;
        mMapView.setTileSource(TileSourceFactory.MAPNIK);
        mPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        //Copyright Overlay
        CopyrightOverlay mCopyrightOverlay = new CopyrightOverlay(context);
        mMapView.getOverlays().add(mCopyrightOverlay);

        // Map Scale
        ScaleBarOverlay mScaleBarOverlay = new ScaleBarOverlay(mMapView);
        mScaleBarOverlay.setCentred(true);
        mScaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10);
        mMapView.getOverlays().add(mScaleBarOverlay);

        // Needed for Pinch Zooms
        mMapView.setMultiTouchControls(true);
        mMapView.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.NEVER);

        // Scales Tiles to the Current Screen's DPI, Helps with Readability of Labels
        mMapView.setTilesScaledToDpi(true);

        //My Location
        mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(context), mMapView);
        mLocationOverlay.enableMyLocation();
        mMapView.getOverlays().add(mLocationOverlay);

        // Dropdown
        setupDropdown();

        // Overlay Markers
        setupOverlay();

        // Init Database
        // initFacilities();

        // Nearest Facility Button
        binding.nearestFacility.setOnClickListener(view1 -> nearestButton());

        // Locate Me Button
        binding.locateMe.setOnClickListener(view1 -> {
            GeoPoint myLocation = mLocationOverlay.getMyLocation();
            if (myLocation != null) {
                double latitude = mLocationOverlay.getMyLocation().getLatitude();
                double longitude = mLocationOverlay.getMyLocation().getLongitude();
                mMapView.getController().animateTo(new GeoPoint(latitude, longitude), 17.0, 500L);
            } else {
                Toast.makeText(context, "Location Services if Off or Not Working", Toast.LENGTH_SHORT).show();
            }
        });

        // The Rest of This is Restoring the Last Map Location the User Looked at
        final float zoomLevel = mPrefs.getFloat(PREFS_ZOOM_LEVEL_DOUBLE, 17.0F);
        mMapView.getController().setZoom(zoomLevel);
        final String latitudeString = mPrefs.getString(PREFS_LATITUDE_STRING, "14.565208778193965");
        final String longitudeString = mPrefs.getString(PREFS_LONGITUDE_STRING, "120.99362663173709");
        final double latitude = Double.parseDouble(latitudeString);
        final double longitude = Double.parseDouble(longitudeString);
        mMapView.setExpectedCenter(new GeoPoint(latitude, longitude));

        setHasOptionsMenu(true);
    }

    private void nearestButton() {
        if (mLocationOverlay.getMyLocation() == null) return;

        double myLat = mLocationOverlay.getMyLocation().getLatitude();
        double myLong = mLocationOverlay.getMyLocation().getLongitude();

        Double shortest = null;
        Double nearLat = null;
        Double nearLong = null;

        List<Overlay> ovs = mMapView.getOverlays();
        for (Overlay ov : ovs) {
            if (ov instanceof Marker) {
                double markLat = ((Marker) ov).getPosition().getLatitude();
                double markLong = ((Marker) ov).getPosition().getLongitude();
                double gap = Math.sqrt(Math.pow(markLat - myLat, 2) + Math.pow(markLong - myLong, 2));
                if ((shortest == null) || (gap < shortest)) {
                    shortest = gap;
                    nearLat = markLat;
                    nearLong = markLong;
                }
                Log.e("NEAREST", String.valueOf(gap));
            }
        }
        if (shortest != null)
            mMapView.getController().animateTo(new GeoPoint(nearLat, nearLong), 17.0, 500L);
    }

    private void initFacilities() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance(DB_URL).getReference();
        String lst = getResources().getString(R.string.csv_facilities).trim();
        Log.e("GAT", "Hello");

        for (String place : lst.split("\n")) {
            String[] place_info = place.split("~");
            String key = mDatabase.child("facilities").push().getKey();
            Facility facility = new Facility(place_info[0], place_info[1], place_info[2], place_info[3], place_info[4], place_info[5]);
            Log.e("GAT", facility.toString());

            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("facilities/" + key, facility);

            mDatabase.updateChildren(childUpdates);
        }
    }

    private void setupOverlay() {
        // Your Items
        DatabaseReference mDatabase = FirebaseDatabase.getInstance(DB_URL).getReference();

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                try {
                    double lat = Double.parseDouble(String.valueOf(snapshot.child("latitude").getValue()));
                    double longitude = Double.parseDouble(String.valueOf(snapshot.child("longitude").getValue()));
                    String name = String.valueOf(snapshot.child("name").getValue());
                    int slotsTaken = Integer.parseInt(String.valueOf(snapshot.child("slotsTaken").getValue()));
                    int slotsMax = Integer.parseInt(String.valueOf(snapshot.child("slotsMax").getValue()));
                    String type = String.valueOf(snapshot.child("type").getValue());

                    Marker marker = new Marker(mMapView);
                    marker.setPosition(new GeoPoint(lat, longitude));
                    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
                    marker.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.black_pin, null));
                    marker.setTitle(name);

                    marker.setOnMarkerClickListener((marker1, mapView) -> {
                        Intent intent = new Intent(getContext(), FacilityPopupActivity.class);
                        intent.putExtra("lat", lat);
                        intent.putExtra("longitude", longitude);
                        intent.putExtra("name", name);
                        intent.putExtra("slotsTaken", slotsTaken);
                        intent.putExtra("slotsMax", slotsMax);
                        intent.putExtra("type", type);
                        startActivity(intent);
                        return false;
                    });

                    mMapView.getOverlays().add(marker);
                } catch (NullPointerException e) {
                    // None
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // None
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                // None
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // None
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // None
            }
        };

        mDatabase.child("facilities").addChildEventListener(childEventListener);
    }

    private void setupDropdown() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.facility_types,
                R.layout.facility_item);
        Spinner spinner = binding.facilityDropdown;
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String str = spinner.getSelectedItem().toString();
                Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Nothing
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        final String tileSourceName = mPrefs.getString(PREFS_TILE_SOURCE, TileSourceFactory.DEFAULT_TILE_SOURCE.name());
        try {
            final ITileSource tileSource = TileSourceFactory.getTileSource(tileSourceName);
            mMapView.setTileSource(tileSource);
        } catch (final IllegalArgumentException e) {
            mMapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
        }

        mMapView.onResume();
    }

    @Override
    public void onPause() {
        // Save the Current Location
        final SharedPreferences.Editor edit = mPrefs.edit();
        edit.putString(PREFS_TILE_SOURCE, mMapView.getTileProvider().getTileSource().name());
        edit.putString(PREFS_LATITUDE_STRING, String.valueOf(mMapView.getMapCenter().getLatitude()));
        edit.putString(PREFS_LONGITUDE_STRING, String.valueOf(mMapView.getMapCenter().getLongitude()));
        edit.putFloat(PREFS_ZOOM_LEVEL_DOUBLE, (float) mMapView.getZoomLevelDouble());
        edit.apply();

        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mMapView.onDetach();
    }
}