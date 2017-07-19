package th.ac.kku.charoenkitsupat.chanyanood.managementforsugarapp;

/**
 * Created by Panya on 10/7/2560.
 */

public class FarmerModel {
    private String id, fullname, email, address, sub_district, district, province, zip_code, tel;
    private String CONTRACTOR_NO, cane_company, district_chief, district_chief_tel;

    FarmerModel (String id, String fullname) {
        this.id = id;
        this.fullname = fullname;
    }

    FarmerModel (String id, String fullname, String email, String address, String sub_district,
                 String district, String province, String zip_code, String tel, String CONTRACTOR_NO,
                 String cane_company, String district_chief, String district_chief_tel) {
        this.id = id;
        this.fullname = fullname;
        this.email = email;
        this.address = address;
        this.sub_district = sub_district;
        this.district = district;
        this.province = province;
        this.zip_code = zip_code;
        this.tel = tel;
        this.CONTRACTOR_NO = CONTRACTOR_NO;
        this.cane_company = cane_company;
        this.district_chief = district_chief;
        this.district_chief_tel = district_chief_tel;
    }

    public String getCONTRACTOR_NO() {
        return CONTRACTOR_NO;
    }

    public String getId() {
        return id;
    }

    public String getFullname() {
        return fullname;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getSubDistrict() {
        return sub_district;
    }

    public String getDistrict() {
        return district;
    }

    public String getProvince() {
        return province;
    }

    public String getZipCode() {
        return zip_code;
    }

    public String getTel() {
        return tel;
    }

    public String getCaneCompany() {
        return cane_company;
    }

    public String getDistrictChief() {
        return district_chief;
    }

    public String getDistrictChiefTel() {
        return district_chief_tel;
    }
}
