package dte.masteriot.mdp.smarttrashapp2;

import com.google.android.gms.maps.model.LatLng;

public class Item {
    private final String myDisplayText;
    private final LatLng myLocation;
    private int orgLevel = 0;
    private int plaLevel = 0;
    private int papLevel = 0;
    private int glaLevel = 0;
    private float temperature = 0;
    private float humidity = 0;
    private float acc_XAXIS = 0;
    private float acc_YAXIS = 0;
    private float acc_ZAXIS = 0;

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

    public int getOrgLevel(){return orgLevel;}
    public int getPlaLevel(){return plaLevel;}
    public int getPapLevel(){return papLevel;}
    public int getGlaLevel(){return glaLevel;}


    public float getTemp(){return temperature;}
    public float getHum(){return humidity;}


    public float getX(){return acc_XAXIS;}
    public float getY(){return acc_YAXIS;}
    public float getZ(){return acc_ZAXIS;}
}
