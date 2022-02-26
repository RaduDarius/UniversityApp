package ModelsPackage;

public class ProfesorModel extends UserModel{

    private int minOre;
    private int maxOre;
    private Departament departament;

    public ProfesorModel(String CNP, String nume, String prenume, String adresa, String nrTelefon, String email, String IBAN, String nrContract, String username, String parola, Rol rol, int minOre, int maxOre, Departament departament) {
        super(CNP, nume, prenume, adresa, nrTelefon, email, IBAN, nrContract, username, parola, rol);
        this.minOre = minOre;
        this.maxOre = maxOre;
        this.departament = departament;
    }

    public ProfesorModel(String username, String parola) {
        super(username, parola);
    }

    public int getMinOre() {
        return minOre;
    }

    public void setMinOre(int minOre) {
        this.minOre = minOre;
    }

    public int getMaxOre() {
        return maxOre;
    }

    public void setMaxOre(int maxOre) {
        this.maxOre = maxOre;
    }

    public Departament getDepartament() {
        return departament;
    }

    public void setDepartament(Departament departament) {
        this.departament = departament;
    }
}
