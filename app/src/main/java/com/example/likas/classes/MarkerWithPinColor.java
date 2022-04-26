package com.example.likas.classes;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class MarkerWithPinColor extends Marker {
    public boolean isRed;

    public MarkerWithPinColor(MapView mapView, boolean isRed) {
        super(mapView);
        this.isRed = isRed;
    }
}