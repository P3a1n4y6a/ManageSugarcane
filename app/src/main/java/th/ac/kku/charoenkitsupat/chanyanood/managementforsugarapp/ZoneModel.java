package th.ac.kku.charoenkitsupat.chanyanood.managementforsugarapp;

/**
 * Created by Panya on 18/7/2560.
 */

public class ZoneModel {
    private String CONTRACTOR_NO, zoneId, companyName;
    private boolean onMap;
    private double lat, lng;


    ZoneModel(String CONTRACTOR_NO, double lat, double lng, String zoneId, String companyName) {
        this.CONTRACTOR_NO = CONTRACTOR_NO;
        this.lat = lat;
        this.lng = lng;
        this.zoneId = zoneId;
        this.companyName = companyName;
        this.onMap = false;
    }

    public String getCONTRACTOR_NO() {
        return CONTRACTOR_NO;
    }

    public String getZoneId() {
        return zoneId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setOnMap(boolean onMap) {
        this.onMap = onMap;
    }

    public boolean isOnMap() {
        return onMap;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}
