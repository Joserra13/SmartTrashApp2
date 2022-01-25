package dte.masteriot.mdp.smarttrashapp2;

import com.google.android.gms.maps.model.LatLng;

public class Item {
    private final String myDisplayText;
    private final LatLng myLocation;
    private final int orgLevel;
    private final int plaLevel;
    private final int papLevel;
    private final int glaLevel;
    private final float temperature;
    private final float humidity;
    private final float acc_XAXIS;
    private final float acc_YAXIS;
    private final float acc_ZAXIS;

    Item(String display_text, LatLng location, int organic, int plastic, int glass, int paper, float temp, float hum, float x_Axis, float y_Axis, float z_Axis) {
        myDisplayText = display_text;
        myLocation = location;
        orgLevel = organic;
        plaLevel = plastic;
        papLevel = paper;
        glaLevel = glass;
        temperature = temp;
        humidity = hum;
        acc_XAXIS = x_Axis;
        acc_YAXIS = y_Axis;
        acc_ZAXIS = z_Axis;
    }

    String getDisplayText() {
        return myDisplayText;
    }

    LatLng getLocation() {
        return myLocation;
    }

    int getOrgLevel(){return orgLevel;}
    int getPlaLevel(){return plaLevel;}
    int getPapLevel(){return papLevel;}
    int getGlaLevel(){return glaLevel;}

    float getTemp(){return temperature;}
    float getHum(){return humidity;}


    float getX(){return acc_XAXIS;}
    float getY(){return acc_YAXIS;}
    float getZ(){return acc_ZAXIS;}
}
