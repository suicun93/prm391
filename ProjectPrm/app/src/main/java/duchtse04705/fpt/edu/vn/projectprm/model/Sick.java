package duchtse04705.fpt.edu.vn.projectprm.model;

import java.util.ArrayList;

/**
 * Created by huy_td on 7/16/18.
 */

public class Sick {
    private String nameSick;
    private String[] listSymptom;
    private String[] list_drugtreatment;
    private boolean isHasBeen;

    public Sick(String nameSick, String[] listSymptom, String[] list_drugtreatment) {
        this.nameSick = nameSick;
        this.listSymptom = listSymptom;
        this.list_drugtreatment = list_drugtreatment;
        isHasBeen=true;
    }

    public String getNameSick() {
        return nameSick;
    }

    public boolean isHasBeen() {
        return isHasBeen;
    }

    public void setHasBeen(boolean hasBeen) {
        isHasBeen = hasBeen;
    }

    public void setNameSick(String nameSick) {
        this.nameSick = nameSick;
    }

    public String[] getListSymptom() {
        return listSymptom;
    }

    public void setListSymptom(String[] listSymptom) {
        this.listSymptom = listSymptom;
    }

    public String[] getList_drugtreatment() {
        return list_drugtreatment;
    }

    public void setList_drugtreatment(String[] list_drugtreatment) {
        this.list_drugtreatment = list_drugtreatment;
    }
}
