package th.ac.kku.charoenkitsupat.chanyanood.managementforsugarapp;

/**
 * Created by Panya on 23/7/2560.
 */

public class EmployeeModel {
    private String id, password;

    EmployeeModel(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }
}
