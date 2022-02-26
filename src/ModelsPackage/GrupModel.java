package ModelsPackage;

public class GrupModel extends ConversatieModel{

    private int id;
    private CursModel curs;
    private ProfesorModel profesor;
    private String descriere;
    private String denumire;

    public GrupModel(int id, CursModel curs, ProfesorModel profesor, String descriere, String denumire) {
        this.id = id;
        this.curs = curs;
        this.profesor = profesor;
        this.descriere = descriere;
        this.denumire = denumire;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CursModel getCurs() {
        return curs;
    }

    public void setCurs(CursModel curs) {
        this.curs = curs;
    }

    public ProfesorModel getProfesor() {
        return profesor;
    }

    public void setProfesor(ProfesorModel profesor) {
        this.profesor = profesor;
    }

    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }
}
