package ModelsPackage;

import java.util.ArrayList;

public class CursModel {

    private int id;
    private String nume;
    private ProfesorModel titular;
    private ArrayList<ActivitateDidacticaModel> activitati;
    private ArrayList<Double> formula;

    public CursModel(int id, String nume, ProfesorModel titular, ArrayList<ActivitateDidacticaModel> activitati) {
        this.id = id;
        this.nume = nume;
        this.titular = titular;
        this.activitati = activitati;
    }

    public CursModel(int id, ProfesorModel titular) {
        this.id = id;
        this.titular = titular;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public ProfesorModel getTitular() {
        return titular;
    }

    public void setTitular(ProfesorModel titular) {
        this.titular = titular;
    }

    public ArrayList<ActivitateDidacticaModel> getActivitati() {
        return activitati;
    }

    public void setActivitati(ArrayList<ActivitateDidacticaModel> activitati) {
        this.activitati = activitati;
    }

    public ArrayList<Double> getFormula() {
        return formula;
    }

    public void setFormula(ArrayList<Double> formula) {
        this.formula = formula;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "CursModel{" +
                "id=" + id +
                ", nume='" + nume + '\'' +
                ", titular=" + titular +
                ", activitati=" + activitati +
                ", formula=" + formula +
                '}';
    }
}
