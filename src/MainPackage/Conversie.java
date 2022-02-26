package MainPackage;

import ModelsPackage.ActivitateDidactica;
import ModelsPackage.DateModel;
import ModelsPackage.Departament;
import ModelsPackage.Rol;

import java.util.Objects;

public class Conversie {

    public static String convDepartamentToString(Departament departament){

        if (departament == Departament.AUTOMATICA){
            return "Automatica";
        }else if (departament == Departament.CALCULATOARE) {
            return "Calculatoare";
        }
        return "Matematica";
    }
    public static String convRolToString(Rol rol) {

        if (rol == Rol.STUDENT) {
            return "Student";
        } else if (rol == Rol.PROFESOR) {
            return "Profesor";
        } else if (rol == Rol.ADMIN) {
            return "Admin";
        }
        return "Super admin";
    }

    public static Departament convStringToDepartament(String string){

        if (Objects.equals(string, "Automatica")){
            return Departament.AUTOMATICA;
        }else if (Objects.equals(string, "Calculatoare")) {
            return Departament.CALCULATOARE;
        }
        return Departament.MATEMATICA;
    }

    public static Rol convStringToRol(String string){

        if (Objects.equals(string, "Profesor")){
            return Rol.PROFESOR;
        }else if (Objects.equals(string, "Student")) {
            return Rol.STUDENT;
        }else if (Objects.equals(string, "Admin")){
            return Rol.ADMIN;
        }
        return Rol.SUPER_ADMIN;
    }

    public static ActivitateDidactica convStringToActivitateDidactica(String string) {
        if (Objects.equals(string, "LABORATOR"))
            return ActivitateDidactica.LABORATOR;
        else if (Objects.equals(string, "SEMINAR"))
            return ActivitateDidactica.SEMINAR;
        return ActivitateDidactica.CURS;
    }

    public static String convActivitateDidacticaToString(ActivitateDidactica activitate) {
        if (activitate == ActivitateDidactica.CURS) {
            return "CURS";
        }else if (activitate == ActivitateDidactica.SEMINAR) {
            return "SEMINAR";
        }
        return "LABORATOR";
    }

    public static DateModel convStringToDateModel(String string) {

        int[] ints = new int[6];
        int cont = 0;

        for (String val: string.split("-")) {
            for (String val2: val.split(" ")) {
                for (String val3: val2.split(":")) {
                    ints[cont++] = Integer.parseInt(val3);
                }
            }
        }

        return new DateModel(ints[0], ints[1], ints[2], ints[3], ints[4]);
    }


    public static String convDateModelToString(DateModel model) {

        return model.getAn() + "-" +
                model.getLuna() + "-" +
                model.getZi() + " " +
                model.getOra() + ":" +
                model.getMinute() + ":" +
                "0";

    }

}
