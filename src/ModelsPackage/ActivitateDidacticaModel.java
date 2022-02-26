package ModelsPackage;

public class ActivitateDidacticaModel {

    private int id;
    private ActivitateDidactica tip;
    private String nume;
    private DateModel dataIncepere;
    private DateModel dataTerminare;
    private int nrMaximParticipanti;
    private int durataActivitate;
    private String numeProf;

    public ActivitateDidacticaModel(int id, ActivitateDidactica tip, String nume, DateModel dataIncepere, DateModel dataTerminare, int nrMaximParticipanti, int durataActivitate, String numeProf) {
        this.id = id;
        this.tip = tip;
        this.nume = nume;
        this.dataIncepere = dataIncepere;
        this.dataTerminare = dataTerminare;
        this.nrMaximParticipanti = nrMaximParticipanti;
        this.durataActivitate = durataActivitate;
        this.numeProf = numeProf;
    }

    public ActivitateDidactica getTip() {
        return tip;
    }

    public void setTip(ActivitateDidactica tip) {
        this.tip = tip;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public DateModel getDataIncepere() {
        return dataIncepere;
    }

    public void setDataIncepere(DateModel dataIncepere) {
        this.dataIncepere = dataIncepere;
    }

    public DateModel getDataTerminare() {
        return dataTerminare;
    }

    public void setDataTerminare(DateModel dataTerminare) {
        this.dataTerminare = dataTerminare;
    }

    public int getNrMaximParticipanti() {
        return nrMaximParticipanti;
    }

    public void setNrMaximParticipanti(int nrMaximParticipanti) {
        this.nrMaximParticipanti = nrMaximParticipanti;
    }

    public int getDurataActivitate() {
        return durataActivitate;
    }

    public void setDurataActivitate(int durataActivitate) {
        this.durataActivitate = durataActivitate;
    }

    public String getNumeProf() {
        return numeProf;
    }

    public void setNumeProf(String numeProf) {
        this.numeProf = numeProf;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ActivitateDidacticaModel{" +
                "tip=" + tip +
                ", nume='" + nume + '\'' +
                ", dataIncepere=" + dataIncepere.toString() +
                ", dataTerminare=" + dataTerminare.toString() +
                ", nrMaximParticipanti=" + nrMaximParticipanti +
                ", durataActivitate=" + durataActivitate +
                ", numeProf='" + numeProf + '\'' +
                '}';
    }
}
