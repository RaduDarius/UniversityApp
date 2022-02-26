package ModelsPackage;

public class IntalnireModel {

    private int id;
    private String denumire;
    private int nrParticipanti;
    private int durata;
    private DateModel dataIncepere;
    private DateModel dataTerminare;
    private UserModel creator;
    private GrupModel grup;

    public IntalnireModel(String denumire, int nrParticipanti, int durata, DateModel dataIncepere, DateModel dataTerminare, UserModel creator, GrupModel grup) {
        this.denumire = denumire;
        this.nrParticipanti = nrParticipanti;
        this.durata = durata;
        this.dataIncepere = dataIncepere;
        this.dataTerminare = dataTerminare;
        this.creator = creator;
        this.grup = grup;
    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public int getNrParticipanti() {
        return nrParticipanti;
    }

    public void setNrParticipanti(int nrParticipanti) {
        this.nrParticipanti = nrParticipanti;
    }

    public int getDurata() {
        return durata;
    }

    public void setDurata(int durata) {
        this.durata = durata;
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

    public UserModel getCreator() {
        return creator;
    }

    public void setCreator(UserModel creator) {
        this.creator = creator;
    }

    public GrupModel getGrup() {
        return grup;
    }

    public void setGrup(GrupModel grup) {
        this.grup = grup;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "IntalnireModel{" +
                "id=" + id +
                ", denumire='" + denumire + '\'' +
                ", nrParticipanti=" + nrParticipanti +
                ", durata=" + durata +
                ", dataIncepere=" + dataIncepere +
                ", dataTerminare=" + dataTerminare +
                ", creator=" + creator +
                ", grup=" + grup +
                '}';
    }
}
