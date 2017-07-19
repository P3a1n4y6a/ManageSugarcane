package th.ac.kku.charoenkitsupat.chanyanood.managementforsugarapp;

/**
 * Created by Panya on 8/6/2560.
 */

public class Information {
    private String zoneId;
    private boolean onMap;
    private double lat, lng;

    Information (double lat, double lng, String zoneId) {
        this.lat = lat;
        this.lng = lng;
        this.zoneId = zoneId;
    }

    public void setOnMap(boolean onMap) {
        this.onMap = onMap;
    }

    public boolean getOnMap() {
        return onMap;
    }

    public String getZoneId() {
        return zoneId;
    }

    public double getLatitude() {
        return lat;
    }

    public double getLongitude() {
        return lng;
    }
}
