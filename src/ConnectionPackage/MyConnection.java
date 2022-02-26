package ConnectionPackage;
import MainPackage.Conversie;
import ModelsPackage.*;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MyConnection {

    private final static String USERNAME = "root";
    private final static String PASSWORD = "1234";
    private final static String DB_NAME = "websitefacultate";
    private final static String CONNECTION_LINK = "jdbc:mysql://localhost:3306/";

    private static Connection connection;

    public static Connection getConnection() {
        return connection;
    }
    public static void setConnection(Connection connection) {
        MyConnection.connection = connection;
    }

    public static void createConnection(){

        try {
            connection = DriverManager.getConnection(CONNECTION_LINK + DB_NAME, USERNAME, PASSWORD);

            System.out.println("Conexiune realizata cu succes");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public static void saveUser(UserModel model){

        //Aflam id-ul rolului pentru al adauga in tabela
        int idRol = getIdRol(Conversie.convRolToString(model.getRol()));

        try {
            CallableStatement cstmt = connection.prepareCall("{call sp_InsertUser(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");

            cstmt.setString(1, model.getNume());
            cstmt.setString(2, model.getPrenume());
            cstmt.setString(3, model.getCNP());
            cstmt.setString(4, model.getAdresa());
            cstmt.setString(5, model.getNrTelefon());
            cstmt.setString(6, model.getEmail());
            cstmt.setString(7, model.getIBAN());
            cstmt.setString(8, model.getNrContract());
            cstmt.setString(9, model.getUsername());
            cstmt.setString(10, model.getParola());
            cstmt.setInt(11, idRol);

            cstmt.execute();
            JOptionPane.showMessageDialog(null, "User inregistrat cu succes !");

        }catch (SQLException e){
            e.printStackTrace();
        }

    }
    public static void saveStudent(StudentModel model) {

        //Salvam atributele specifice userilor in tabela user
        saveUser(model);

        //Calculam id-ul user-ului
        int id = getIdUser(model.getUsername());

        if (id == -1){
            JOptionPane.showMessageDialog(null, "Nu s-a putut salva studentul !");
            return;
        }

        // Inseram atributele specifice studentului
        try {
            CallableStatement cstmt = connection.prepareCall("{call sp_InsertStudent(?, ?, ?)}");

            cstmt.setInt(1, id);
            cstmt.setInt(2, model.getAnStudiu());
            cstmt.setInt(3, model.getOreSustinute());

            cstmt.execute();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static void saveProfesor(ProfesorModel model) {

        //Salvam atributele specifice userilor in tabela user
        saveUser(model);

        //Calculam id-ul user-ului
        //Calculam id-ul departamentului
        int idDepartament = getIdDepartament(Conversie.convDepartamentToString(model.getDepartament()));
        int id = getIdUser(model.getUsername());

        if (idDepartament == -1){
            JOptionPane.showMessageDialog(null, "Nu s-a putut salva profesorul. Departament invalid !");
            return;
        }

        if (id == -1){
            JOptionPane.showMessageDialog(null, "Nu s-a putut salva profesorul !");
            return;
        }

        // Inseram atributele specifice profesorului
        try {
            CallableStatement cstmt = connection.prepareCall("{call sp_InsertProfesor(?, ?, ?, ?)}");

            cstmt.setInt(1, id);
            cstmt.setInt(2, model.getMinOre());
            cstmt.setInt(3, model.getMaxOre());
            cstmt.setInt(4, idDepartament);

            cstmt.execute();

        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public static void saveFormulaMaterie(CursModel cursModel) {

        try {
            CallableStatement cstm = connection.prepareCall("{call p_save_formula(?, ?, ?, ?)}");

            cstm.setInt(1, cursModel.getId());
            cstm.setDouble(2, cursModel.getFormula().get(0));
            cstm.setDouble(3, cursModel.getFormula().get(1));
            cstm.setDouble(4, cursModel.getFormula().get(2));
            cstm.execute();

        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void saveMesaj(MesajPrivatModel msg) {

        try {

            CallableStatement cstm = connection.prepareCall("{call p_insert_mesaj_privat(?,?,?,?,?)}");

            int idFrom = getIdUser(msg.getFromSend().getUsername());
            int idTo;
            if (msg.getToSend().getClass() == UserModel.class) {
                idTo = getIdUser(((UserModel)msg.getToSend()).getUsername());
                cstm.setInt(5, 0);
            }else {
                idTo = getIdGrup(((GrupModel)msg.getToSend()).getProfesor(), ((GrupModel)msg.getToSend()).getCurs());
                cstm.setInt(5, 1);
            }
            cstm.setInt(1, idFrom);
            cstm.setInt(2, idTo);
            cstm.setString(3, msg.getText());
            cstm.setString(4, msg.getDataTrimitere());

            cstm.execute();

        }catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Mesaj netrimis !");
        }

    }
    public static void saveActivitate(ActivitateDidacticaModel activitate, CursModel cursModel) {

        try {
            CallableStatement cstm = connection.prepareCall("{call p_insert_activitate_didactica(?,?,?,?,?,?,?)}");

            cstm.setString(1, Conversie.convActivitateDidacticaToString(activitate.getTip()));
            cstm.setString(2, activitate.getNume());
            cstm.setString(3, Conversie.convDateModelToString(activitate.getDataIncepere()));
            cstm.setString(4, Conversie.convDateModelToString(activitate.getDataTerminare()));
            cstm.setInt(5, activitate.getNrMaximParticipanti());
            cstm.setInt(6, activitate.getDurataActivitate());
            cstm.setInt(7, cursModel.getId());

            cstm.execute();

            JOptionPane.showMessageDialog(null, "Activitate adaugata cu succes !");

        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void saveNota(StudentModel studNotat, ActivitateDidacticaModel activ, double nota) {

        try {

            CallableStatement cstm = connection.prepareCall("{call p_insert_nota(?,?,?,?)}");

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime now = LocalDateTime.now();

            cstm.setDouble(1, nota);
            cstm.setString(2, dtf.format(now));
            cstm.setInt(3, getIdUser(studNotat.getUsername()));
            cstm.setInt(4, activ.getId());

            cstm.execute();

        }catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static boolean saveGrup(GrupModel grup) {

        try {

            CallableStatement cstm = connection.prepareCall("{call p_insert_grup(?,?,?,?)}");

            cstm.setInt(1, grup.getCurs().getId());
            cstm.setInt(2, getIdUser(grup.getProfesor().getUsername()));
            cstm.setString(3, grup.getDescriere());
            cstm.setString(4, grup.getDenumire());

            cstm.execute();

            JOptionPane.showMessageDialog(null, "Grup creat cu succes !");

            return true;

        }catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public static void saveIntalnire(IntalnireModel intalnire) {

        try {

            CallableStatement cstm = connection.prepareCall("{call p_insert_intalnire(?,?,?,?,?,?,?)}");

            cstm.setString(1, intalnire.getDenumire());
            cstm.setInt(2, intalnire.getNrParticipanti());
            cstm.setInt(3, intalnire.getDurata());
            cstm.setString(4, Conversie.convDateModelToString(intalnire.getDataIncepere()));
            cstm.setString(5, Conversie.convDateModelToString(intalnire.getDataTerminare()));
            cstm.setInt(6, getIdUser(intalnire.getCreator().getUsername()));
            cstm.setInt(7, intalnire.getGrup().getId());

            cstm.execute();

            JOptionPane.showMessageDialog(null, "Intalnire planificata cu succes !");

        }catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void saveStudentIntalnire(UserModel model, IntalnireModel intalnire) {

        try {

            CallableStatement cstm = connection.prepareCall("{call p_insert_studentiintalnire(?,?,?)}");

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime now = LocalDateTime.now();

            System.out.println(getIdIntalnire(intalnire));

            cstm.setInt(1, getIdIntalnire(intalnire));
            cstm.setInt(2, getIdUser(model.getUsername()));
            cstm.setString(3, dtf.format(now));

            cstm.execute();

        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void inrolareUser(UserModel model, int id) {

        try {

            CallableStatement cstm = connection.prepareCall("{}");

            if (model.getRol() == Rol.STUDENT) {
                cstm = connection.prepareCall("{call p_insert_inscrierestudent(?, ?, ?)}");
            }
            else if (model.getRol() == Rol.PROFESOR) {
                cstm = connection.prepareCall("{call p_insert_didactic(?, ?, ?)}");
            }

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime now = LocalDateTime.now();

            cstm.setString(1, dtf.format(now));
            cstm.setInt(2, id);
            cstm.setInt(3, getIdUser(model.getUsername()));

            cstm.execute();

            JOptionPane.showMessageDialog(null, "INROLAT CU SUCCES !");


        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void inrolareStudentGrup(UserModel model, GrupModel grup) {

        try {

            CallableStatement cstm = connection.prepareCall("{call p_insert_student_grupe(?,?,?)}");

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime now = LocalDateTime.now();

            cstm.setString(1, dtf.format(now));
            cstm.setInt(2, grup.getId());
            cstm.setInt(3, getIdUser(model.getUsername()));

            cstm.execute();

        }catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static UserModel findUser(UserModel model) {

        String string = "SELECT * FROM user u WHERE u.username = " + (char)34 + model.getUsername() + (char)34
                + " AND u.parola = " + (char)34 + model.getParola() + (char)34 + ";";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(string);

            //return resultSet != null;
            if (resultSet.next()){

                int idRol = resultSet.getInt("idRol");

                ArrayList<RolModel> rolModels = getAllFromRol();

                Rol rol = null;

                assert rolModels != null;
                for (RolModel r: rolModels) {
                    if (r.getId() == idRol) {
                        rol = Conversie.convStringToRol(r.getNume());
                    }
                }

                return new UserModel(
                        resultSet.getString("CNP"),
                        resultSet.getString("nume"),
                        resultSet.getString("prenume"),
                        resultSet.getString("adresa"),
                        resultSet.getString("nr_telefon"),
                        resultSet.getString("email"),
                        resultSet.getString("IBAN"),
                        resultSet.getString("nr_contract"),
                        resultSet.getString("username"),
                        resultSet.getString("parola"),
                        rol
                );
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
    public static StudentModel findStudent(UserModel model){

        String string = "SELECT * FROM student " +
                "INNER JOIN user u on student.id = u.id " +
                "WHERE u.username = " + (char)34 + model.getUsername() + (char)34 + ";";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(string);

            //return resultSet != null;
            if (resultSet.next()){

                return new StudentModel(
                        model.getCNP(),
                        model.getNume(),
                        model.getPrenume(),
                        model.getAdresa(),
                        model.getNrTelefon(),
                        model.getEmail(),
                        model.getIBAN(),
                        model.getNrContract(),
                        model.getUsername(),
                        model.getParola(),
                        model.getRol(),
                        resultSet.getInt("an_studiu"),
                        resultSet.getInt("ore_sustinute"));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
    public static ProfesorModel findProfesor(UserModel model){

        String string = "SELECT * FROM profesori " +
                "INNER JOIN user u on profesori.id = u.id " +
                "WHERE u.username = " + (char)34 + model.getUsername() + (char)34 + ";";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(string);

            //return resultSet != null;
            if (resultSet.next()){

                int idDepartament = resultSet.getInt("idDepartament");

                ArrayList<DepartamentModel> depModels = getAllFromDepartament();
                Departament departament = null;

                if (depModels == null) {
                    JOptionPane.showMessageDialog(null, "Erroare la gasirea utilizatorului");
                    return null;
                }

                for (DepartamentModel d: depModels) {
                    if (d.getId() == idDepartament) {
                        departament = Conversie.convStringToDepartament(d.getNume());
                    }
                }

                return new ProfesorModel(
                        model.getCNP(),
                        model.getNume(),
                        model.getPrenume(),
                        model.getAdresa(),
                        model.getNrTelefon(),
                        model.getEmail(),
                        model.getIBAN(),
                        model.getNrContract(),
                        model.getUsername(),
                        model.getParola(),
                        model.getRol(),
                        resultSet.getInt("minOre"),
                        resultSet.getInt("maxOre"),
                        departament);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static void updateUser(UserModel model, UserModel newModel){

        try {
            CallableStatement cstmt = connection.prepareCall("{call p_update_user(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");

            int idUser = getIdUser(model.getUsername());

            cstmt.setInt(1, idUser);
            cstmt.setString(2, newModel.getAdresa());
            cstmt.setString(3, newModel.getCNP());
            cstmt.setString(4, newModel.getEmail());
            cstmt.setString(5, newModel.getIBAN());
            cstmt.setInt(6, getIdRol(Conversie.convRolToString(newModel.getRol())));
            cstmt.setString(7, newModel.getNrContract());
            cstmt.setString(8, newModel.getNrTelefon());
            cstmt.setString(9, newModel.getNume());
            cstmt.setString(10, newModel.getParola());
            cstmt.setString(11, newModel.getPrenume());
            cstmt.setString(12, newModel.getUsername());

            cstmt.execute();

        }catch (SQLException e){
            e.printStackTrace();
        }

    }
    public static void updateProfesor(UserModel model, ProfesorModel newModel){

        updateUser(model, newModel);

        try {
            CallableStatement cstmt = connection.prepareCall("{call p_update_profesori(?, ?, ?, ?)}");

            int idUser = getIdUser(model.getUsername());
            int idDepartament = getIdDepartament(Conversie.convDepartamentToString(newModel.getDepartament()));

            cstmt.setInt(1, idUser);
            cstmt.setInt(2, idDepartament);
            cstmt.setInt(3, newModel.getMaxOre());
            cstmt.setInt(4, newModel.getMinOre());

            cstmt.execute();

        }catch (SQLException e){
            e.printStackTrace();
        }

    }
    public static void updateStudent(UserModel model, StudentModel newModel){

        updateUser(model, newModel);

        try {

            CallableStatement cstmt = connection.prepareCall("{call p_update_student(?, ?, ?)}");

            int idUser = getIdUser(model.getUsername());

            cstmt.setInt(1, idUser);
            cstmt.setInt(2, newModel.getAnStudiu());
            cstmt.setInt(3, newModel.getOreSustinute());

            cstmt.execute();

        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    public static ArrayList<ActivitateDidacticaModel> getAllActivitatiDidacticeFromUser(UserModel model) {

        try {
            CallableStatement cstmt = connection.prepareCall("{call p_afisare_activitati_didactice(?)}");

            ArrayList<ActivitateDidacticaModel> activitati = new ArrayList<>();

            cstmt.setString(1, model.getUsername());
            ResultSet resultSet = cstmt.executeQuery();

            while (resultSet.next()) {
                activitati.add(new ActivitateDidacticaModel(
                        resultSet.getInt("id"),
                        Conversie.convStringToActivitateDidactica(resultSet.getString("tip")),
                        resultSet.getString("denumire"),
                        Conversie.convStringToDateModel(resultSet.getString("data_incepere")),
                        Conversie.convStringToDateModel(resultSet.getString("data_sfarsit")),
                        resultSet.getInt("nr_maxim"),
                        resultSet.getInt("durata_activitate"),
                        resultSet.getString("nume") + " " + resultSet.getString("prenume")
                ));
            }

            return activitati;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    public static ArrayList<ActivitateDidacticaModel> getAllActivitatiDidactice() {
        try {
            CallableStatement cstmt = connection.prepareCall("{call p_afisare_toate_activitatile_didactice()}");

            ArrayList<ActivitateDidacticaModel> activitati = new ArrayList<>();

            ResultSet resultSet = cstmt.executeQuery();

            while (resultSet.next()) {
                activitati.add(new ActivitateDidacticaModel(
                        resultSet.getInt("id"),
                        Conversie.convStringToActivitateDidactica(resultSet.getString("tip")),
                        resultSet.getString("denumire"),
                        Conversie.convStringToDateModel(resultSet.getString("data_incepere")),
                        Conversie.convStringToDateModel(resultSet.getString("data_sfarsit")),
                        resultSet.getInt("nr_maxim"),
                        resultSet.getInt("durata_activitate"),
                        resultSet.getString("nume") + " " + resultSet.getString("prenume")
                ));
            }

            return activitati;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    public static ArrayList<ActivitateDidacticaModel> getAllActivitatiDidacticeByIdMaterie(int idMaterie) {
        try {

            CallableStatement cstm = connection.prepareCall("{call p_afisare_activitate_didactica_by_id_materie(?)}");
            cstm.setInt(1, idMaterie);
            ResultSet resultSet = cstm.executeQuery();

            ArrayList<ActivitateDidacticaModel> activitati = new ArrayList<>();

            while (resultSet.next()) {
                activitati.add(new ActivitateDidacticaModel(
                        resultSet.getInt("id"),
                        Conversie.convStringToActivitateDidactica(resultSet.getString("tip")),
                        resultSet.getString("denumire"),
                        Conversie.convStringToDateModel(resultSet.getString("data_incepere")),
                        Conversie.convStringToDateModel(resultSet.getString("data_sfarsit")),
                        resultSet.getInt("nr_maxim"),
                        resultSet.getInt("durata_activitate"), ""
                ));
            }

            return activitati;

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static ArrayList<ActivitateDidacticaModel> getActivitatiByDate(UserModel model, String now) {

        try {

            if (now != null) {
                CallableStatement cstm = connection.prepareCall("{call p_getActivitatiByDate(?,?)}");

                cstm.setString(1, model.getUsername());
                cstm.setString(2, now);

                ResultSet resultSet = cstm.executeQuery();

                ArrayList<ActivitateDidacticaModel> activities = new ArrayList<>();

                while (resultSet.next()) {

                    activities.add(new ActivitateDidacticaModel(
                            resultSet.getInt("id"),
                            Conversie.convStringToActivitateDidactica(resultSet.getString("tip")),
                            resultSet.getString("denumire"),
                            Conversie.convStringToDateModel(resultSet.getString("data_incepere")),
                            Conversie.convStringToDateModel(resultSet.getString("data_sfarsit")),
                            resultSet.getInt("nr_maxim"),
                            resultSet.getInt("durata_activitate"),
                            null
                    ));

                }

                return activities;
            }
            else {
                CallableStatement cstm = connection.prepareCall("{call p_getActivitatiFromZi(?)}");

                cstm.setString(1, model.getUsername());

                ResultSet resultSet = cstm.executeQuery();

                ArrayList<ActivitateDidacticaModel> activities = new ArrayList<>();

                while (resultSet.next()) {

                    DateModel dateModel = Conversie.convStringToDateModel(resultSet.getString("data_incepere"));

                    ActivitateDidacticaModel activitate = new ActivitateDidacticaModel(
                            resultSet.getInt("id"),
                            Conversie.convStringToActivitateDidactica(resultSet.getString("tip")),
                            resultSet.getString("denumire"),
                            Conversie.convStringToDateModel(resultSet.getString("dataCurenta")),
                            Conversie.convStringToDateModel(resultSet.getString("data_sfarsit")),
                            resultSet.getInt("nr_maxim"),
                            resultSet.getInt("durata_activitate"),
                            null
                    );
                    activitate.getDataIncepere().setOra(dateModel.getOra());

                    activities.add(activitate);

                }

                return activities;
            }



        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static double getNoteStudentByIdActivitate(UserModel model, int idActivitate) {

        try {

            CallableStatement cstm = connection.prepareCall("{call p_afisare_note_student_by_id_activitate(?,?)}");

            cstm.setInt(1, getIdUser(model.getUsername()));
            cstm.setInt(2, idActivitate);

            ResultSet resultSet = cstm.executeQuery();

            if (resultSet.next()) {
                return resultSet.getDouble("valoare");
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public static double getMedieStudentByIdMaterie(UserModel model, int idMaterie) {

        try {
            CallableStatement cstm = connection.prepareCall("{call p_afisare_medie_student_by_id_materie(?,?)}");

            cstm.setInt(1, getIdUser(model.getUsername()));
            cstm.setInt(2, idMaterie);

            ResultSet resultSet = cstm.executeQuery();

            if (resultSet.next()) {
                return resultSet.getDouble("valoare");
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static ArrayList<ConversatieModel> getConversationsFromUser(UserModel model) {

        try {
            CallableStatement cstm = connection.prepareCall("{call p_afisare_conversatii(?)}");

            cstm.setString(1, model.getUsername());

            ResultSet resultSet = cstm.executeQuery();

            ArrayList<ConversatieModel> users = new ArrayList<>();

            while (resultSet.next()) {

                int idRol = resultSet.getInt("idRol");

                ArrayList<RolModel> rolModels = getAllFromRol();

                Rol rol = null;

                assert rolModels != null;
                for (RolModel r: rolModels) {
                    if (r.getId() == idRol) {
                        rol = Conversie.convStringToRol(r.getNume());
                    }
                }

                users.add(new UserModel(
                        resultSet.getString("CNP"),
                        resultSet.getString("nume"),
                        resultSet.getString("prenume"),
                        resultSet.getString("adresa"),
                        resultSet.getString("nr_telefon"),
                        resultSet.getString("email"),
                        resultSet.getString("IBAN"),
                        resultSet.getString("nr_contract"),
                        resultSet.getString("username"),
                        resultSet.getString("parola"),
                        rol
                ));
            }

            return users;

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static ArrayList<MesajPrivatModel> getMesajeFromUser(UserModel modelFrom, ConversatieModel modelTo) {

        try {

            CallableStatement cstm = connection.prepareCall("{call get_all_mesaje_by_id(?,?,?)}");

            cstm.setInt(1, getIdUser(modelFrom.getUsername()));
            if (modelTo.getClass() == UserModel.class) {
                cstm.setInt(2, getIdUser(((UserModel)modelTo).getUsername()));
                cstm.setInt(3, 0);

                ResultSet resultSet = cstm.executeQuery();

                ArrayList<MesajPrivatModel> mesaje = new ArrayList<>();

                int idFrom = getIdUser(modelFrom.getUsername());

                while (resultSet.next()) {

                    int id = resultSet.getInt("idFrom");
                    if (id == idFrom) {
                        mesaje.add(new MesajPrivatModel(
                                modelTo,
                                modelFrom,
                                resultSet.getString("text"),
                                resultSet.getString("dataTrimitere")));
                    } else {
                        mesaje.add(new MesajPrivatModel(
                                modelFrom,
                                (UserModel) modelTo,
                                resultSet.getString("text"),
                                resultSet.getString("dataTrimitere")));
                    }
                }
                return mesaje;
            }
            else {
                cstm.setInt(2, ((GrupModel)modelTo).getId());
                cstm.setInt(3, 1);

                ResultSet resultSet = cstm.executeQuery();

                ArrayList<MesajPrivatModel> mesaje = new ArrayList<>();

                while (resultSet.next()) {

                    int idRol = resultSet.getInt("idRol");

                    ArrayList<RolModel> rolModels = getAllFromRol();

                    Rol rol = null;

                    assert rolModels != null;
                    for (RolModel r: rolModels) {
                        if (r.getId() == idRol) {
                            rol = Conversie.convStringToRol(r.getNume());
                        }
                    }

                    mesaje.add(new MesajPrivatModel(
                        modelTo,
                        new UserModel(
                                resultSet.getString("CNP"),
                                resultSet.getString("nume"),
                                resultSet.getString("prenume"),
                                resultSet.getString("adresa"),
                                resultSet.getString("nr_telefon"),
                                resultSet.getString("email"),
                                resultSet.getString("IBAN"),
                                resultSet.getString("nr_contract"),
                                resultSet.getString("username"),
                                resultSet.getString("parola"),
                                rol),
                        resultSet.getString("textMesaj"),
                        resultSet.getString("dataTrimitere")
                    ));
                }
                return mesaje;
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<GrupModel> getGrupByUser(UserModel model) {
        try {

            CallableStatement cstm = connection.prepareCall("{call p_get_grup_by_user(?)}");

            cstm.setString(1, model.getUsername());

            ResultSet resultSet = cstm.executeQuery();

            ArrayList<GrupModel> grupuri = new ArrayList<>();

            while (resultSet.next()) {
                grupuri.add(new GrupModel(
                        resultSet.getInt("id"),
                        getMaterieById(resultSet.getInt("idMaterie")),
                        getUserById(resultSet.getInt("idProf")),
                        resultSet.getString("descriere"),
                        resultSet.getString("denumire")
                ));
            }
            return grupuri;
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static ArrayList<IntalnireModel> getMeetingByGrup(UserModel model, GrupModel toSend) {
        try {

            CallableStatement cstm = connection.prepareCall("{call p_afisare_intalniri_from_grup(?,?)}");

            cstm.setInt(1, toSend.getId());
            cstm.setString(2, model.getUsername());
            ResultSet resultSet = cstm.executeQuery();

            ArrayList<IntalnireModel> meetings = new ArrayList<>();

            while (resultSet.next()) {

                int idRol = resultSet.getInt("idRol");

                ArrayList<RolModel> rolModels = getAllFromRol();

                Rol rol = null;

                assert rolModels != null;
                for (RolModel r : rolModels) {
                    if (r.getId() == idRol) {
                        rol = Conversie.convStringToRol(r.getNume());
                    }
                }

                meetings.add(new IntalnireModel(
                        resultSet.getString("titlu"),
                        resultSet.getInt("nr_participanti"),
                        resultSet.getInt("durata"),
                        Conversie.convStringToDateModel(resultSet.getString("dataIncepere")),
                        Conversie.convStringToDateModel(resultSet.getString("dataExpirare")),
                        new UserModel(
                                resultSet.getString("CNP"),
                                resultSet.getString("nume"),
                                resultSet.getString("prenume"),
                                resultSet.getString("adresa"),
                                resultSet.getString("nr_telefon"),
                                resultSet.getString("email"),
                                resultSet.getString("IBAN"),
                                resultSet.getString("nr_contract"),
                                resultSet.getString("username"),
                                resultSet.getString("parola"),
                                rol
                        ),
                        toSend
                ));
            }

            return meetings;

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }
    private static ArrayList<IntalnireModel> getAllMeetings() {

        try {

            CallableStatement cstm = connection.prepareCall("{call p_get_meetings()}");

            ResultSet resultSet = cstm.executeQuery();

            ArrayList<IntalnireModel> meetings = new ArrayList<>();

            while (resultSet.next()) {

                IntalnireModel intalnireModel = new IntalnireModel(
                        resultSet.getString("titlu"),
                        resultSet.getInt("nr_participanti"),
                        resultSet.getInt("durata"),
                        Conversie.convStringToDateModel(resultSet.getString("dataIncepere")),
                        Conversie.convStringToDateModel(resultSet.getString("dataExpirare")),
                        new UserModel(
                                resultSet.getString("username"),
                                resultSet.getString("parola")),
                        new GrupModel(resultSet.getInt("g.id"),
                                new CursModel(resultSet.getInt("idMaterie"), new ProfesorModel(resultSet.getString("u2.username"), "")), new ProfesorModel(resultSet.getString("u2.username"), ""),
                                resultSet.getString("descriere"),
                                resultSet.getString("denumire")));

                intalnireModel.setId(resultSet.getInt("id"));

                meetings.add(intalnireModel);

            }

            return meetings;

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<DateModel> getDateFromUser(UserModel model) {

        try {

            CallableStatement cstm = connection.prepareCall("{call p_get_date_by_user(?)}");
            cstm.setString(1, model.getUsername());

            ResultSet resultSet = cstm.executeQuery();

            ArrayList<DateModel> dateModels = new ArrayList<>();

            while (resultSet.next()) {

                dateModels.add(Conversie.convStringToDateModel(resultSet.getString("dataCurenta")));

            }

            return dateModels;

        }catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ArrayList<CursModel> getAllMateriiForEnrol(UserModel model) {

        try {
            CallableStatement cstm = connection.prepareCall("{call p_afisare_materii_not_stud(?)}");

            cstm.setString(1, model.getUsername());
            ResultSet resultSet = cstm.executeQuery();

            ArrayList<CursModel> cursuri = new ArrayList<>();

            while (resultSet.next()) {

                int idDepartament = resultSet.getInt("p.idDepartament");

                ArrayList<DepartamentModel> depModels = getAllFromDepartament();
                Departament departament = null;

                assert depModels != null;
                for (DepartamentModel d : depModels) {
                    if (d.getId() == idDepartament) {
                        departament = Conversie.convStringToDepartament(d.getNume());
                    }
                }
                int idRol = resultSet.getInt("idRol");

                ArrayList<RolModel> rolModels = getAllFromRol();

                Rol rol = null;

                assert rolModels != null;
                for (RolModel r : rolModels) {
                    if (r.getId() == idRol) {
                        rol = Conversie.convStringToRol(r.getNume());
                    }
                }

                int idMaterie = resultSet.getInt("m2.id");

                cursuri.add(new CursModel(
                        resultSet.getInt("m2.id"),
                        resultSet.getString("m2.nume"),
                        new ProfesorModel(
                                resultSet.getString("CNP"),
                                resultSet.getString("u2.nume"),
                                resultSet.getString("prenume"),
                                resultSet.getString("adresa"),
                                resultSet.getString("nr_telefon"),
                                resultSet.getString("email"),
                                resultSet.getString("IBAN"),
                                resultSet.getString("nr_contract"),
                                resultSet.getString("username"),
                                resultSet.getString("parola"),
                                rol,
                                resultSet.getInt("minOre"),
                                resultSet.getInt("maxOre"),
                                departament
                        ),
                        getAllActivitatiDidacticeByIdMaterie(idMaterie)
                ));
            }
            return cursuri;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    public static ArrayList<CursModel> getAllMaterii(UserModel model) {
        try {

            CallableStatement cstm = connection.prepareCall("{call p_afisare_materii(?)}");

            cstm.setString(1, model.getUsername());
            ResultSet resultSet = cstm.executeQuery();

            ArrayList<CursModel> cursuri = new ArrayList<>();

            while (resultSet.next()) {

                int idDepartament = resultSet.getInt("p.idDepartament");

                ArrayList<DepartamentModel> depModels = getAllFromDepartament();
                Departament departament = null;

                assert depModels != null;
                for (DepartamentModel d : depModels) {
                    if (d.getId() == idDepartament) {
                        departament = Conversie.convStringToDepartament(d.getNume());
                    }
                }
                int idRol = resultSet.getInt("idRol");

                ArrayList<RolModel> rolModels = getAllFromRol();

                Rol rol = null;

                assert rolModels != null;
                for (RolModel r : rolModels) {
                    if (r.getId() == idRol) {
                        rol = Conversie.convStringToRol(r.getNume());
                    }
                }

                int idMaterie = resultSet.getInt("m.id");

                cursuri.add(new CursModel(
                        resultSet.getInt("m.id"),
                        resultSet.getString("m.nume"),
                        new ProfesorModel(
                                resultSet.getString("CNP"),
                                resultSet.getString("u.nume"),
                                resultSet.getString("prenume"),
                                resultSet.getString("adresa"),
                                resultSet.getString("nr_telefon"),
                                resultSet.getString("email"),
                                resultSet.getString("IBAN"),
                                resultSet.getString("nr_contract"),
                                resultSet.getString("username"),
                                resultSet.getString("parola"),
                                rol,
                                resultSet.getInt("minOre"),
                                resultSet.getInt("maxOre"),
                                departament
                        ),
                        getAllActivitatiDidacticeByIdMaterie(idMaterie)
                ));
            }
            return cursuri;

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static ArrayList<CursModel> getAllMaterii() {
        try {

            CallableStatement cstm = connection.prepareCall("{call p_afisare_toate_materiile()}");

            ResultSet resultSet = cstm.executeQuery();

            ArrayList<CursModel> cursuri = new ArrayList<>();

            while (resultSet.next()) {

                int idDepartament = resultSet.getInt("p.idDepartament");

                ArrayList<DepartamentModel> depModels = getAllFromDepartament();
                Departament departament = null;

                assert depModels != null;
                for (DepartamentModel d : depModels) {
                    if (d.getId() == idDepartament) {
                        departament = Conversie.convStringToDepartament(d.getNume());
                    }
                }
                int idRol = resultSet.getInt("idRol");

                ArrayList<RolModel> rolModels = getAllFromRol();

                Rol rol = null;

                assert rolModels != null;
                for (RolModel r : rolModels) {
                    if (r.getId() == idRol) {
                        rol = Conversie.convStringToRol(r.getNume());
                    }
                }

                int idMaterie = resultSet.getInt("m.id");

                cursuri.add(new CursModel(
                        resultSet.getInt("m.id"),
                        resultSet.getString("m.nume"),
                        new ProfesorModel(
                                resultSet.getString("CNP"),
                                resultSet.getString("u.nume"),
                                resultSet.getString("prenume"),
                                resultSet.getString("adresa"),
                                resultSet.getString("nr_telefon"),
                                resultSet.getString("email"),
                                resultSet.getString("IBAN"),
                                resultSet.getString("nr_contract"),
                                resultSet.getString("username"),
                                resultSet.getString("parola"),
                                rol,
                                resultSet.getInt("minOre"),
                                resultSet.getInt("maxOre"),
                                departament
                        ),
                        getAllActivitatiDidacticeByIdMaterie(idMaterie)
                ));
            }
            return cursuri;

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    private static CursModel getMaterieById(int idMaterie) {

        try {

            CallableStatement cstm = connection.prepareCall("{call get_materie_by_id(?)}");

            cstm.setInt(1, idMaterie);
            ResultSet resultSet = cstm.executeQuery();

            if (resultSet.next()) {
                int idDepartament = resultSet.getInt("p.idDepartament");

                ArrayList<DepartamentModel> depModels = getAllFromDepartament();
                Departament departament = null;

                assert depModels != null;
                for (DepartamentModel d : depModels) {
                    if (d.getId() == idDepartament) {
                        departament = Conversie.convStringToDepartament(d.getNume());
                    }
                }
                int idRol = resultSet.getInt("idRol");

                ArrayList<RolModel> rolModels = getAllFromRol();

                Rol rol = null;

                assert rolModels != null;
                for (RolModel r : rolModels) {
                    if (r.getId() == idRol) {
                        rol = Conversie.convStringToRol(r.getNume());
                    }
                }

                return new CursModel(
                        resultSet.getInt("m.id"),
                        resultSet.getString("m.nume"),
                        new ProfesorModel(
                                resultSet.getString("CNP"),
                                resultSet.getString("u.nume"),
                                resultSet.getString("prenume"),
                                resultSet.getString("adresa"),
                                resultSet.getString("nr_telefon"),
                                resultSet.getString("email"),
                                resultSet.getString("IBAN"),
                                resultSet.getString("nr_contract"),
                                resultSet.getString("username"),
                                resultSet.getString("parola"),
                                rol,
                                resultSet.getInt("minOre"),
                                resultSet.getInt("maxOre"),
                                departament
                        ),
                        getAllActivitatiDidacticeByIdMaterie(idMaterie)
                );
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<UserModel> getUsersByTip(Rol rol) {

        try {

            CallableStatement cstm = connection.prepareCall("{call p_cautare_dupa_tip(?)}");

            cstm.setString(1, Conversie.convRolToString(rol));

            ResultSet resultSet = cstm.executeQuery();

            ArrayList<UserModel> users = new ArrayList<>();

            while (resultSet.next()) {
                users.add(
                        new UserModel(
                                resultSet.getString("CNP"),
                                resultSet.getString("nume"),
                                resultSet.getString("prenume"),
                                resultSet.getString("adresa"),
                                resultSet.getString("nr_telefon"),
                                resultSet.getString("email"),
                                resultSet.getString("IBAN"),
                                resultSet.getString("nr_contract"),
                                resultSet.getString("username"),
                                resultSet.getString("parola"),
                                rol
                        ));
            }
            return users;

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static ArrayList<StudentModel> getStudentByActivitateDidactica(int id) {
        try {
            CallableStatement cstm = connection.prepareCall("{call p_afisare_studenti_by_activitate_didactica(?)}");

            cstm.setInt(1, id);
            ResultSet resultSet = cstm.executeQuery();

            ArrayList<StudentModel> studenti = new ArrayList<>();

            while (resultSet.next()) {

                int idRol = resultSet.getInt("idRol");

                ArrayList<RolModel> rolModels = getAllFromRol();

                Rol rol = null;

                assert rolModels != null;
                for (RolModel r: rolModels) {
                    if (r.getId() == idRol) {
                        rol = Conversie.convStringToRol(r.getNume());
                    }
                }

                studenti.add(new StudentModel(
                        resultSet.getString("CNP"),
                        resultSet.getString("nume"),
                        resultSet.getString("prenume"),
                        resultSet.getString("adresa"),
                        resultSet.getString("nr_telefon"),
                        resultSet.getString("email"),
                        resultSet.getString("IBAN"),
                        resultSet.getString("nr_contract"),
                        resultSet.getString("username"),
                        resultSet.getString("parola"),
                        rol,
                        resultSet.getInt("an_studiu"),
                        resultSet.getInt("ore_sustinute")
                ));
            }
            return studenti;

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    private static ProfesorModel getUserById(int idProf) {

        try {

            CallableStatement cstm = connection.prepareCall("{call get_profesor_by_id(?)}");

            cstm.setInt(1, idProf);
            ResultSet resultSet = cstm.executeQuery();

            if (resultSet.next()){

                int idRol = resultSet.getInt("idRol");

                ArrayList<RolModel> rolModels = getAllFromRol();

                Rol rol = null;

                assert rolModels != null;
                for (RolModel r: rolModels) {
                    if (r.getId() == idRol) {
                        rol = Conversie.convStringToRol(r.getNume());
                    }
                }

                int idDepartament = resultSet.getInt("idDepartament");

                ArrayList<DepartamentModel> depModels = getAllFromDepartament();
                Departament departament = null;

                if (depModels == null) {
                    JOptionPane.showMessageDialog(null, "Erroare la gasirea utilizatorului");
                    return null;
                }

                for (DepartamentModel d: depModels) {
                    if (d.getId() == idDepartament) {
                        departament = Conversie.convStringToDepartament(d.getNume());
                    }
                }

                return new ProfesorModel(
                        resultSet.getString("CNP"),
                        resultSet.getString("nume"),
                        resultSet.getString("prenume"),
                        resultSet.getString("adresa"),
                        resultSet.getString("nr_telefon"),
                        resultSet.getString("email"),
                        resultSet.getString("IBAN"),
                        resultSet.getString("nr_contract"),
                        resultSet.getString("username"),
                        resultSet.getString("parola"),
                        rol,
                        resultSet.getInt("minOre"),
                        resultSet.getInt("maxOre"),
                        departament
                );
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static ArrayList<UserModel> getAllUsersFromGrup(GrupModel grupModel) {

        try {

            CallableStatement cstm = connection.prepareCall("{call p_afisare_membrii_grup(?)}");

            cstm.setInt(1, grupModel.getId());

            ArrayList<UserModel> membrii = new ArrayList<>();

            ResultSet resultSet = cstm.executeQuery();

            while (resultSet.next()) {

                int idRol = resultSet.getInt("idRol");

                ArrayList<RolModel> rolModels = getAllFromRol();

                Rol rol = null;

                assert rolModels != null;
                for (RolModel r : rolModels) {
                    if (r.getId() == idRol) {
                        rol = Conversie.convStringToRol(r.getNume());
                    }
                }

                membrii.add(new UserModel(
                        resultSet.getString("CNP"),
                        resultSet.getString("nume"),
                        resultSet.getString("prenume"),
                        resultSet.getString("adresa"),
                        resultSet.getString("nr_telefon"),
                        resultSet.getString("email"),
                        resultSet.getString("IBAN"),
                        resultSet.getString("nr_contract"),
                        resultSet.getString("username"),
                        resultSet.getString("parola"),
                        rol
                ));
            }

            return membrii;

        }catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    public static ArrayList<UserModel> getSugestii(GrupModel grupModel) {

        try {

            CallableStatement cstm = connection.prepareCall("{call p_get_sugestii(?)}");

            cstm.setInt(1, grupModel.getCurs().getId());

            ResultSet resultSet = cstm.executeQuery();

            ArrayList<UserModel> sugestii = new ArrayList<>();

            while (resultSet.next()) {
                int idRol = resultSet.getInt("idRol");

                ArrayList<RolModel> rolModels = getAllFromRol();

                Rol rol = null;

                assert rolModels != null;
                for (RolModel r: rolModels) {
                    if (r.getId() == idRol) {
                        rol = Conversie.convStringToRol(r.getNume());
                    }
                }

                sugestii.add(new UserModel(
                        resultSet.getString("CNP"),
                        resultSet.getString("nume"),
                        resultSet.getString("prenume"),
                        resultSet.getString("adresa"),
                        resultSet.getString("nr_telefon"),
                        resultSet.getString("email"),
                        resultSet.getString("IBAN"),
                        resultSet.getString("nr_contract"),
                        resultSet.getString("username"),
                        resultSet.getString("parola"),
                        rol));
            }

            return sugestii;

        }catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static int getIdDepartament(String departament) {

        String string = "SELECT id FROM departament WHERE denumire = " + (char)34 + departament + (char)34 + ";";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(string);

            if (resultSet.next())
                return resultSet.getInt("id");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return -1;
    }
    public static int getIdUser(String username){

        String string = "SELECT id FROM user WHERE username = " + (char)34 + username + (char)34 + ";";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(string);

            if (resultSet.next())
                return resultSet.getInt("id");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return -1;

    }
    private static int getIdRol(String rol){

        String string = "SELECT id FROM rol WHERE nume = " + (char)34 + rol + (char)34 + ";";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(string);

            if (resultSet.next())
                return resultSet.getInt("id");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return -1;


    }
    public static int getIdGrup(ProfesorModel model, CursModel cursModel) {
        try {

            CallableStatement cstm = connection.prepareCall("{call p_get_id_grup(?,?)}");

            cstm.setInt(1, cursModel.getId());
            cstm.setInt(2, getIdUser(model.getUsername()));

            ResultSet resultSet = cstm.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("id");
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    private static int getIdIntalnire(IntalnireModel intalnire) {
        try {

            CallableStatement cstm = connection.prepareCall("{call p_get_id_intalnire(?,?,?,?,?,?,?)}");

            cstm.setString(1, intalnire.getDenumire());
            cstm.setInt(2, intalnire.getNrParticipanti());
            cstm.setInt(3, intalnire.getDurata());
            cstm.setString(4, Conversie.convDateModelToString(intalnire.getDataIncepere()));
            cstm.setString(5, Conversie.convDateModelToString(intalnire.getDataTerminare()));
            cstm.setInt(6, getIdUser(intalnire.getCreator().getUsername()));
            cstm.setInt(7, intalnire.getGrup().getId());

            ResultSet resultSet = cstm.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("id");
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getNumOfStudByActivitate(int id) {
        try {

            CallableStatement cstm = connection.prepareCall("{call p_num_activitati(?)}");

            cstm.setInt(1, id);
            ResultSet resultSet = cstm.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("num");
            }else
                return 0;

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public static int getNumOfStudByMaterie(int id) {
        try {

            CallableStatement cstm = connection.prepareCall("{call p_num_materii(?)}");

            cstm.setInt(1, id);
            ResultSet resultSet = cstm.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("num");
            }else
                return 0;

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    private static int getNumOfStudByIntalnire(int id) {
        try {

            CallableStatement cstm = connection.prepareCall("{call p_num_stud_intalnire(?)}");

            cstm.setInt(1, id);

            ResultSet resultSet = cstm.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("num");
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    private static ArrayList<DepartamentModel> getAllFromDepartament() {
        String string = "Select * FROM departament";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(string);

            ArrayList<DepartamentModel> depModels = new ArrayList<>();

            while (resultSet.next()) {
                depModels.add(new DepartamentModel(resultSet.getInt("id"), resultSet.getString("denumire")));
            }

            return depModels;

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    private static ArrayList<RolModel> getAllFromRol() {

        String string = "SELECT * FROM rol";

        try {

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(string);

            ArrayList<RolModel> rolModels = new ArrayList<>();

            while (resultSet.next()) {
                rolModels.add(new RolModel(resultSet.getInt("id"), resultSet.getString("nume")));
            }
            return rolModels;

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static ArrayList<UserModel> getAllFromUser() {
        String string = "SELECT * FROM user";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(string);

            ArrayList<UserModel> users = new ArrayList<>();
            while (resultSet.next()) {

                int idRol = resultSet.getInt("idRol");
                Rol rol = null;
                ArrayList<RolModel> rolModels = getAllFromRol();
                assert rolModels != null;
                for (RolModel r: rolModels) {
                    if (idRol == r.getId()) {
                        rol = Conversie.convStringToRol(r.getNume());
                    }
                }

                users.add(new UserModel(
                        resultSet.getString("CNP"),
                        resultSet.getString("nume"),
                        resultSet.getString("prenume"),
                        resultSet.getString("adresa"),
                        resultSet.getString("nr_telefon"),
                        resultSet.getString("email"),
                        resultSet.getString("IBAN"),
                        resultSet.getString("nr_contract"),
                        resultSet.getString("username"),
                        resultSet.getString("parola"),
                        rol));
            }

            return users;
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static ArrayList<ProfesorModel> getAllProfesori() {

        try {

            CallableStatement cstm = connection.prepareCall("{call p_afisare_profi()}");

            ResultSet resultSet = cstm.executeQuery();

            ArrayList<ProfesorModel> profi = new ArrayList<>();

            while (resultSet.next()) {

                int idDepartament = resultSet.getInt("idDepartament");

                ArrayList<DepartamentModel> depModels = getAllFromDepartament();
                Departament departament = null;

                if (depModels == null) {
                    JOptionPane.showMessageDialog(null, "Erroare la gasirea utilizatorului");
                    return null;
                }

                for (DepartamentModel d: depModels) {
                    if (d.getId() == idDepartament) {
                        departament = Conversie.convStringToDepartament(d.getNume());
                    }
                }

                int idRol = resultSet.getInt("idRol");

                ArrayList<RolModel> rolModels = getAllFromRol();

                Rol rol = null;

                assert rolModels != null;
                for (RolModel r: rolModels) {
                    if (r.getId() == idRol) {
                        rol = Conversie.convStringToRol(r.getNume());
                    }
                }

                profi.add(new ProfesorModel(
                        resultSet.getString("CNP"),
                        resultSet.getString("nume"),
                        resultSet.getString("prenume"),
                        resultSet.getString("adresa"),
                        resultSet.getString("nr_telefon"),
                        resultSet.getString("email"),
                        resultSet.getString("IBAN"),
                        resultSet.getString("nr_contract"),
                        resultSet.getString("username"),
                        resultSet.getString("parola"),
                        rol,
                        resultSet.getInt("minOre"),
                        resultSet.getInt("maxOre"),
                        departament));
            }

            return profi;

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static ArrayList<GrupModel> getAllGrups(UserModel model) {

        try {

            CallableStatement cstm = connection.prepareCall("{call p_afisare_grupuri(?)}");

            cstm.setString(1, model.getUsername());

            ResultSet resultSet = cstm.executeQuery();

            ArrayList<GrupModel> grups = new ArrayList<>();

            while (resultSet.next()) {
                grups.add(new GrupModel(
                        resultSet.getInt("g.id"),
                        null, null,
                        resultSet.getString("descriere"),
                        resultSet.getString("denumire")
                ));
            }

            return grups;

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    public static void validareIntalniri() {

        ArrayList<IntalnireModel> meetings = getAllMeetings();


        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        DateModel acm = Conversie.convStringToDateModel(dtf.format(now));


        assert meetings != null;
        for (IntalnireModel i: meetings) {
            DateModel dateModel = i.getDataTerminare();

            if (acm.comparareTot(dateModel) >= 1) {
                int num = getNumOfStudByIntalnire(i.getId());
                if (num < i.getNrParticipanti()) {
                    saveMesaj(new MesajPrivatModel(
                            i.getGrup(),
                            i.getCreator(),
                            "Intalnirea programata pe data de " + i.getDataIncepere().toString() + " se suspenda !",
                            dtf.format(now)));
                    deleteIntalnire(i);
                }
            }
        }

    }

    public static void renuntareStudent(UserModel model, CursModel curs) {

        try {

            CallableStatement cstm = connection.prepareCall("{call p_delete_inscrierestudent(?,?)}");

            cstm.setInt(1, getIdUser(model.getUsername()));
            cstm.setInt(2, curs.getId());

            cstm.execute();

            JOptionPane.showMessageDialog(null, "Ai renuntat cu succes !");

        }catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static void deleteIntalnire(IntalnireModel i) {

        try {

            CallableStatement cstm = connection.prepareCall("{call p_delete_intalnire(?)}");
            CallableStatement cstm2 = connection.prepareCall("call p_delete_studentiintalnire(?)");

            cstm.setInt(1, i.getId());
            cstm2.setInt(1, i.getId());

            cstm2.execute();
            cstm.execute();

        }catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void renuntareGrup(UserModel model, GrupModel grupModel) {
        try {

            CallableStatement cstm = connection.prepareCall("{call p_delete_studenti_intalnire(?,?)}");

            cstm.setInt(1, getIdUser(model.getUsername()));
            cstm.setInt(2, grupModel.getId());

            cstm.execute();

        }catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void descarcareProgram(ArrayList<ActivitateDidacticaModel> activitati) {

        try {
            FileOutputStream file  = new FileOutputStream("program.txt");

            PrintStream fout = new PrintStream(file);

            for (ActivitateDidacticaModel a: activitati) {
                fout.println(a.getNume() + " de la ora " +
                        a.getDataIncepere().getOra() + ":" +
                        a.getDataIncepere().getMinute() + " si dureaza " +
                        a.getDurataActivitate() + "h");
            }

            JOptionPane.showMessageDialog(null, "Lista a fost descarcata in fisierul program.txt!");

        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static void descarcareProgramAll(ArrayList<ActivitateDidacticaModel> activitati) {

        try {
            FileOutputStream file  = new FileOutputStream("program_tot.txt");

            PrintStream fout = new PrintStream(file);

            for (ActivitateDidacticaModel a: activitati) {
                fout.println(a.getNume() + " in data de " +
                        a.getDataIncepere().toString() + " de la ora " +
                        a.getDataIncepere().getOra() + ":" +
                        a.getDataIncepere().getMinute() + " si dureaza " +
                        a.getDurataActivitate() + "h");
            }

            JOptionPane.showMessageDialog(null, "Lista a fost descarcata in fisierul program_tot.txt!");

        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static void descarcareLista(ArrayList<ActivitateDidacticaModel> activitati) {

        try {
            FileOutputStream file  = new FileOutputStream("activitati.txt");

            PrintStream fout = new PrintStream(file);

            for (ActivitateDidacticaModel a: activitati) {
                fout.println(a.getNume());
            }

            JOptionPane.showMessageDialog(null, "Lista a fost descarcata in fisierul activitati.txt!");

        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
