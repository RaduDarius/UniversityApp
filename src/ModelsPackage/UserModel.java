package ModelsPackage;

public class UserModel extends ConversatieModel{

    private String CNP;
    private String nume;
    private String prenume;
    private String adresa;
    private String nrTelefon;
    private String email;
    private String IBAN;
    private String nrContract;
    private String username;
    private String parola;
    private Rol rol;

    public UserModel(){

    }

    public UserModel(String CNP,
                     String nume,
                     String prenume,
                     String adresa,
                     String nrTelefon,
                     String email,
                     String IBAN,
                     String nrContract,
                     String username,
                     String parola,
                     Rol rol) {
        this.CNP = CNP;
        this.nume = nume;
        this.prenume = prenume;
        this.adresa = adresa;
        this.nrTelefon = nrTelefon;
        this.email = email;
        this.IBAN = IBAN;
        this.nrContract = nrContract;
        this.username = username;
        this.parola = parola;
        this.rol = rol;
    }

    public UserModel(String username, String parola) {
        this.username = username;
        this.parola = parola;
    }

    public String getCNP() {
        return CNP;
    }
    public void setCNP(String CNP) {
        this.CNP = CNP;
    }

    public String getNume() {
        return nume;
    }
    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPrenume() {
        return prenume;
    }
    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public String getAdresa() {
        return adresa;
    }
    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getNrTelefon() {
        return nrTelefon;
    }
    public void setNrTelefon(String nrTelefon) {
        this.nrTelefon = nrTelefon;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getIBAN() {
        return IBAN;
    }
    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public String getNrContract() {
        return nrContract;
    }
    public void setNrContract(String nrContract) {
        this.nrContract = nrContract;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getParola() {
        return parola;
    }
    public void setParola(String parola) {
        this.parola = parola;
    }

    public Rol getRol() {
        return rol;
    }
    public void setRol(Rol rol) {
        this.rol = rol;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                ", CNP='" + CNP + '\'' +
                ", nume='" + nume + '\'' +
                ", prenume='" + prenume + '\'' +
                ", adresa='" + adresa + '\'' +
                ", nrTelefon='" + nrTelefon + '\'' +
                ", email='" + email + '\'' +
                ", IBAN='" + IBAN + '\'' +
                ", nrContract='" + nrContract + '\'' +
                ", username='" + username + '\'' +
                ", parola='" + parola + '\'' +
                '}';
    }
}
