package th.ac.kku.charoenkitsupat.chanyanood.managementforsugarapp;

/**
 * Created by Panya on 20/7/2560.
 */

public class PlantModel {
    private String id, farmer_id, output, qc;
    private String farmer_name, address, sub_district, district, province, zip_code;
    private String start_survey_date, survey_status, explorer_name, explorer_tel;

    PlantModel (String id, String start_survey_date, String survey_status, String output, String qc) {
        this.id = id;
        this.start_survey_date = start_survey_date;
        this.survey_status = survey_status;
        this.output = output;
        this.qc = qc;
    }

    public String getId() {
        return id;
    }

    public String getStart_survey_date() {
        return start_survey_date;
    }

    public String getSurvey_status() {
        return survey_status;
    }

    public String getOutput() {
        return output;
    }

    public String getQc() {
        return qc;
    }
}
