package ModelsPackage;

public class StudentModel extends UserModel{

    private int anStudiu;
    private int oreSustinute;

    public StudentModel(String CNP, String nume, String prenume, String adresa, String nrTelefon, String email, String IBAN, String nrContract, String username, String parola, Rol rol, int anStudiu, int oreSustinute) {
        super(CNP, nume, prenume, adresa, nrTelefon, email, IBAN, nrContract, username, parola, rol);
        this.anStudiu = anStudiu;
        this.oreSustinute = oreSustinute;
    }

    public int getAnStudiu() {
        return anStudiu;
    }

    public void setAnStudiu(int anStudiu) {
        this.anStudiu = anStudiu;
    }

    public int getOreSustinute() {
        return oreSustinute;
    }

    public void setOreSustinute(int oreSustinute) {
        this.oreSustinute = oreSustinute;
    }
}
