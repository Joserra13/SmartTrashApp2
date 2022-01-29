package dte.masteriot.mdp.smarttrashapp2;

import com.google.android.gms.maps.model.LatLng;

public class Item {
    private final String myDisplayText;
    private final LatLng myLocation;


    Item(String display_text, LatLng location) {
        myDisplayText = display_text;
        myLocation = location;
    }

    String getDisplayText() {
        return myDisplayText;
    }

    LatLng getLocation() {
        return myLocation;
    }

}
