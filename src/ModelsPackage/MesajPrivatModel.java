package ModelsPackage;

public class MesajPrivatModel {

    private ConversatieModel toSend;
    private UserModel fromSend;
    private String text;
    private String dataTrimitere;

    public MesajPrivatModel(ConversatieModel toSend, UserModel fromSend, String text, String dataTrimitere) {
        this.toSend = toSend;
        this.fromSend = fromSend;
        this.text = text;
        this.dataTrimitere = dataTrimitere;
    }

    public ConversatieModel getToSend() {
        return toSend;
    }

    public void setToSend(UserModel toSend) {
        this.toSend = toSend;
    }

    public UserModel getFromSend() {
        return fromSend;
    }

    public void setFromSend(UserModel fromSend) {
        this.fromSend = fromSend;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDataTrimitere() {
        return dataTrimitere;
    }

    public void setDataTrimitere(String dataTrimitere) {
        this.dataTrimitere = dataTrimitere;
    }

    @Override
    public String toString() {
        return "MesajPrivatModel{" +
                "toSend=" + toSend +
                ", fromSend=" + fromSend +
                ", text='" + text + '\'' +
                ", dataTrimitere='" + dataTrimitere + '\'' +
                '}';
    }
}
