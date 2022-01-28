package dte.masteriot.mdp.smarttrashapp2;

import com.google.android.gms.maps.model.LatLng;

public class Item {
    private final String myDisplayText;
    private final LatLng myLocation;
    private final String mydate;


    Item(String display_text, LatLng location, String date) {
        myDisplayText = display_text;
        myLocation = location;
        mydate = date;
    }

    String getDisplayText() {
        return myDisplayText;
    }

    LatLng getLocation() {
        return myLocation;
    }

    String getDate(){return mydate;}

}
