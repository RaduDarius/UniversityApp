package ConnectionPackage;

import ModelsPackage.ActivitateDidacticaModel;
import ModelsPackage.IntalnireModel;
import ModelsPackage.UserModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Validator {

    public static boolean validareParola(String text){
        // minim o litera mare, cifra, caracter special

        boolean upper = false;
        boolean lower = false;
        boolean digit = false;
        boolean special = false;

        for(int i = 0; i < text.length(); i ++){
            if(Character.isDigit(text.charAt(i)))
                digit = true;
            else if(Character.isLowerCase(text.charAt(i)))
                lower = true;
            else if(Character.isUpperCase(text.charAt(i)))
                upper = true;
            else
                special = true;
        }

        return upper && lower && digit && special;
    }

    public static boolean validareCNP(String cnp){
        if(cnp.length() != 13)
            return false;
        else
            for(int i = 0; i < 13; i ++)
                if(!Character.isDigit(cnp.charAt(i)))
                    return false;

        return true;
    }

    private static boolean validareEmail(String email) {

        if (email.contains("@yahoo.ro")){
            return email.charAt(email.length() - 9) == '@';
        }

        if (email.contains("@yahoo.com")) {
            return email.charAt(email.length() - 10) == '@';
        }
        if (email.contains("@gmail.com")) {
            return email.charAt(email.length() - 10) == '@';
        }
        if (email.contains("@gmail.ro")) {
            return email.charAt(email.length() - 9) == '@';
        }

        return false;
    }

    private static boolean validareTelefon(String nrTelefon) {

        if (nrTelefon.length() != 10)
            return false;

        for(int i = 0; i < 10; i ++)
            if(!Character.isDigit(nrTelefon.charAt(i)))
                return false;

        if (nrTelefon.charAt(0) != '0')
            return false;
        return nrTelefon.charAt(1) == '7';
    }

    private static boolean validareUsername(String username) {

        int idUser = MyConnection.getIdUser(username);

        return idUser == -1;
    }

    public static String validareUser(UserModel model, boolean forUpdate){

        String errorMsg = "";

        if (model.getNume().length() < 1 ||
                model.getPrenume().length() < 1 ||
                model.getCNP().length() < 1 ||
                model.getAdresa().length() < 1 ||
                model.getNrTelefon().length() < 1 ||
                model.getEmail().length() < 1 ||
                model.getIBAN().length() < 1 ||
                model.getUsername().length() < 1 ||
                model.getParola().length() < 1) {
            errorMsg = "Nu se accepta date vide";
        }else if (!validareUsername(model.getUsername()) && !forUpdate){
            errorMsg = "User deja existent";
        }else if (!validareCNP(model.getCNP())){
            errorMsg = "CNP invalid";
        }else if (!validareTelefon(model.getNrTelefon())) {
            errorMsg = "Numar de telefon invalid";
        }else if(!validareEmail(model.getEmail())){
            errorMsg = "Email invalid";
        }else if (!validareParola(model.getParola())){
            errorMsg = "Parola invalida";
        }

        return errorMsg;
    }

    public static boolean validareDate(ActivitateDidacticaModel activitate){
        boolean poate = true;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        if (now.getYear() > activitate.getDataIncepere().getAn()){
            poate = false;
            System.out.println(1);
        } else if (now.getYear() == activitate.getDataIncepere().getAn()) {
            if (now.getMonthValue() > activitate.getDataIncepere().getLuna()) {
                poate = false;
                System.out.println(2);
            }else if (now.getMonthValue() == activitate.getDataIncepere().getLuna()) {
                if (now.getDayOfMonth() > activitate.getDataIncepere().getZi()) {
                    poate = false;
                    System.out.println(3);
                }else if (now.getDayOfMonth() == activitate.getDataIncepere().getZi()) {
                    if (now.getHour() > activitate.getDataIncepere().getOra()) {
                        poate = false;
                        System.out.println(4);
                    }else if (now.getHour() == activitate.getDataIncepere().getOra()) {
                        if (now.getMinute() >= activitate.getDataIncepere().getMinute()) {
                            poate = false;
                            System.out.println(5);
                        }
                    }
                }
            }
        }

        if (activitate.getDataTerminare().getAn() < activitate.getDataIncepere().getAn()){
            poate = false;
        } else if (activitate.getDataTerminare().getAn() == activitate.getDataIncepere().getAn()) {
            if (activitate.getDataTerminare().getLuna() < activitate.getDataIncepere().getLuna()) {
                poate = false;
            }else if (activitate.getDataTerminare().getLuna() == activitate.getDataIncepere().getLuna()) {
                if (activitate.getDataTerminare().getZi() < activitate.getDataIncepere().getZi()) {
                    poate = false;
                }else if (activitate.getDataTerminare().getZi() == activitate.getDataIncepere().getZi()) {
                    if (activitate.getDataTerminare().getOra() < activitate.getDataIncepere().getOra()) {
                        poate = false;
                    }else if (activitate.getDataTerminare().getOra() == activitate.getDataIncepere().getOra()) {
                        if (activitate.getDataTerminare().getMinute() <= activitate.getDataIncepere().getMinute()) {
                            poate = false;
                        }
                    }
                }
            }
        }
        return poate;
    }
    public static boolean validareDateIntalnire(IntalnireModel intalnire) {
        boolean poate = true;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        if (now.getYear() > intalnire.getDataIncepere().getAn()){
            poate = false;
        } else if (now.getYear() == intalnire.getDataIncepere().getAn()) {
            if (now.getMonthValue() > intalnire.getDataIncepere().getLuna()) {
                poate = false;
            }else if (now.getMonthValue() == intalnire.getDataIncepere().getLuna()) {
                if (now.getDayOfMonth() > intalnire.getDataIncepere().getZi()) {
                    poate = false;
                }else if (now.getDayOfMonth() == intalnire.getDataIncepere().getZi()) {
                    if (now.getHour() > intalnire.getDataIncepere().getOra()) {
                        poate = false;
                    }else if (now.getHour() == intalnire.getDataIncepere().getOra()) {
                        if (now.getMinute() >= intalnire.getDataIncepere().getMinute()) {
                            poate = false;
                        }
                    }
                }
            }
        }

        if (intalnire.getDataTerminare().getAn() < intalnire.getDataIncepere().getAn()){
            poate = false;
        } else if (intalnire.getDataTerminare().getAn() == intalnire.getDataIncepere().getAn()) {
            if (intalnire.getDataTerminare().getLuna() < intalnire.getDataIncepere().getLuna()) {
                poate = false;
            }else if (intalnire.getDataTerminare().getLuna() == intalnire.getDataIncepere().getLuna()) {
                if (intalnire.getDataTerminare().getZi() < intalnire.getDataIncepere().getZi()) {
                    poate = false;
                }else if (intalnire.getDataTerminare().getZi() == intalnire.getDataIncepere().getZi()) {
                    if (intalnire.getDataTerminare().getOra() < intalnire.getDataIncepere().getOra()) {
                        poate = false;
                    }else if (intalnire.getDataTerminare().getOra() == intalnire.getDataIncepere().getOra()) {
                        if (intalnire.getDataTerminare().getMinute() <= intalnire.getDataIncepere().getMinute()) {
                            poate = false;
                        }
                    }
                }
            }
        }
        return poate;
    }

    public static String validareActivitate(ActivitateDidacticaModel activitate) {
        String errorMsg = "";

        if (activitate.getNume().equals("")) {
            errorMsg = "Nu se accepta campuri vide !";
        }else if (activitate.getDurataActivitate() == 0){
            errorMsg = "Durata invalida !";
        }else if (activitate.getNrMaximParticipanti() == 0) {
            errorMsg = "Numar de participanti invalid !";
        }else if (!validareDate(activitate)){
            errorMsg = "Datele introduse sunt invalide !";
        }

        return errorMsg;
    }

    public static String validareIntalnire(IntalnireModel intalnire) {
        String errorMsg = "";

        if (intalnire.getDenumire().equals("")) {
            errorMsg = "Nu se accepta campuri vide !";
        }else if (intalnire.getDurata() == 0){
            errorMsg = "Durata invalida !";
        }else if (intalnire.getNrParticipanti() == 0) {
            errorMsg = "Numar de participanti invalid !";
        }else if (!validareDateIntalnire(intalnire)){
            errorMsg = "Datele introduse sunt invalide !";
        }

        return errorMsg;
    }
}
