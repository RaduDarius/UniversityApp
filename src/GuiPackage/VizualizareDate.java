package GuiPackage;

import ConnectionPackage.MyConnection;
import ConnectionPackage.Validator;
import MainPackage.Conversie;
import ModelsPackage.ProfesorModel;
import ModelsPackage.Rol;
import ModelsPackage.StudentModel;
import ModelsPackage.UserModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class VizualizareDate extends JFrame{
    private JLabel lblcreareUtilizator;
    private JLabel labelUsername;
    private JLabel labelNume;
    private JLabel labelPrenume;
    private JLabel labelCNP;
    private JLabel labelAdresa;
    private JLabel labelTipUtilizator;
    private JLabel labelAnStudiu;
    private JLabel labelNrMinOre;
    private JButton backButton;
    private JLabel labelParola;
    private JLabel labelEmail;
    private JLabel labelIBAN;
    private JLabel labelNrContract;
    private JLabel labelTelefon;
    private JLabel labelNumarOre;
    private JLabel labelDepartament;
    private JLabel labelNrMaxOre;
    private JPanel vizualizareDatePanel;
    private JTextField textUsername;
    private JTextField textNume;
    private JTextField textPrenume;
    private JTextField textCNP;
    private JTextField textAdresa;
    private JTextField textTipUtilizator;
    private JTextField textAnStudiu;
    private JTextField textNrMinOre;
    private JTextField textParola;
    private JTextField textEmail;
    private JTextField textIBAN;
    private JTextField textNrContract;
    private JTextField textNrTel;
    private JTextField textNrOre;
    private JTextField textDepartament;
    private JTextField textNrMaxOre;
    private JButton editButton;
    private JButton saveButton;

    boolean apasat = false;

    public VizualizareDate(UserModel model, int fereastra, UserModel modelFrom){
        setContentPane(vizualizareDatePanel);
        setVisible(true);
        setLocation(450, 150);
        setSize(600, 600);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        textTipUtilizator.setEditable(false);

        setEditDate(false);

        setDataVisible(model, modelFrom, fereastra);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                if (fereastra == 4)
                    new VizualizareCursuri(modelFrom, fereastra, null, null);
                else
                    new HomePage(model);
            }
        });
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (fereastra == 4) {

                    if (!apasat) {
                        apasat = true;
                        setEditDate(true);
                    }

                }
            }
        });
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                setEditDate(false);

                if (model.getRol() == Rol.STUDENT){

                    apasat = false;

                    StudentModel student = new StudentModel(
                            textCNP.getText(),
                            textNume.getText(),
                            textPrenume.getText(),
                            textAdresa.getText(),
                            textNrTel.getText(),
                            textEmail.getText(),
                            textIBAN.getText(),
                            textNrContract.getText(),
                            textUsername.getText(),
                            textParola.getText(),
                            Rol.STUDENT,
                            Integer.parseInt(textAnStudiu.getText()),
                            Integer.parseInt(textNrOre.getText()));

                    String errorMsg = Validator.validareUser(student, true);
                    if (!errorMsg.equals("")){
                        JOptionPane.showMessageDialog(null, errorMsg);
                        return;
                    }

                    MyConnection.updateStudent(model, student);

                    JOptionPane.showMessageDialog(null, "Date schimbate cu succes");

                }
                else if (model.getRol() == Rol.PROFESOR) {

                    apasat = false;

                    ProfesorModel profesor = new ProfesorModel(
                            textCNP.getText(),
                            textNume.getText(),
                            textPrenume.getText(),
                            textAdresa.getText(),
                            textNrTel.getText(),
                            textEmail.getText(),
                            textIBAN.getText(),
                            textNrContract.getText(),
                            textUsername.getText(),
                            textParola.getText(),
                            Rol.PROFESOR,
                            Integer.parseInt(textNrMinOre.getText()),
                            Integer.parseInt(textNrMaxOre.getText()),
                            Conversie.convStringToDepartament(textDepartament.getText())
                            );

                    String errorMsg = Validator.validareUser(profesor, true);
                    if (!errorMsg.equals("")){
                        JOptionPane.showMessageDialog(null, errorMsg);
                        return;
                    }

                    MyConnection.updateProfesor(model, profesor);

                    JOptionPane.showMessageDialog(null, "Date schimbate cu succes");

                }
                else if (modelFrom.getRol() == Rol.SUPER_ADMIN && !Objects.equals(modelFrom.getUsername(), model.getUsername())){

                    apasat = false;

                    UserModel user = new UserModel(
                            textCNP.getText(),
                            textNume.getText(),
                            textPrenume.getText(),
                            textAdresa.getText(),
                            textNrTel.getText(),
                            textEmail.getText(),
                            textIBAN.getText(),
                            textNrContract.getText(),
                            textUsername.getText(),
                            textParola.getText(),
                            Conversie.convStringToRol(textTipUtilizator.getText())
                    );

                    String errorMsg = Validator.validareUser(user, true);
                    if (!errorMsg.equals("")){
                        JOptionPane.showMessageDialog(null, errorMsg);
                        return;
                    }

                    MyConnection.updateUser(model, user);

                    JOptionPane.showMessageDialog(null, "Date schimbate cu succes");

                }

            }
        });
    }

    private void setDataVisible(UserModel model, UserModel modelFrom, int fereastra) {

        textNume.setText(model.getNume());
        textPrenume.setText(model.getPrenume());
        textCNP.setText(model.getCNP());
        textAdresa.setText(model.getAdresa());
        textEmail.setText(model.getEmail());
        textIBAN.setText(model.getIBAN());
        textNrTel.setText(model.getNrTelefon());
        textUsername.setText(model.getUsername());
        textParola.setText(model.getParola());
        textNrContract.setText(model.getNrContract());
        textTipUtilizator.setText(Conversie.convRolToString(model.getRol()));

        if (model.getRol() == Rol.STUDENT){

            labelNrMaxOre.setVisible(false);
            textNrMaxOre.setVisible(false);

            labelNrMinOre.setVisible(false);
            textNrMinOre.setVisible(false);

            labelDepartament.setVisible(false);
            textDepartament.setVisible(false);

            labelAnStudiu.setVisible(true);
            textAnStudiu.setVisible(true);

            labelNumarOre.setVisible(true);
            textNrOre.setVisible(true);

            StudentModel newModel = MyConnection.findStudent(model);

            if (newModel == null){
                JOptionPane.showMessageDialog(null, "User incorect !");
                return;
            }
            textNrOre.setText(String.valueOf(newModel.getOreSustinute()));
            textAnStudiu.setText(String.valueOf(newModel.getAnStudiu()));

            editButton.setVisible(false);
            saveButton.setVisible(false);

        }
        else if (model.getRol() == Rol.PROFESOR){

            labelNrMaxOre.setVisible(true);
            textNrMaxOre.setVisible(true);

            labelNrMinOre.setVisible(true);
            textNrMinOre.setVisible(true);

            labelDepartament.setVisible(true);
            textDepartament.setVisible(true);

            labelAnStudiu.setVisible(false);
            textAnStudiu.setVisible(false);

            labelNumarOre.setVisible(false);
            textNrOre.setVisible(false);

            ProfesorModel newModel = MyConnection.findProfesor(model);

            if (newModel == null){
                JOptionPane.showMessageDialog(null, "User incorect !");
                return;
            }
            textNrMinOre.setText(String.valueOf(newModel.getMinOre()));
            textNrMaxOre.setText(String.valueOf(newModel.getMaxOre()));
            textDepartament.setText(Conversie.convDepartamentToString(newModel.getDepartament()));

            editButton.setVisible(false);
            saveButton.setVisible(false);
        }
        else {
            labelNrMaxOre.setVisible(false);
            textNrMaxOre.setVisible(false);

            labelNrMinOre.setVisible(false);
            textNrMinOre.setVisible(false);

            labelDepartament.setVisible(false);
            textDepartament.setVisible(false);

            labelAnStudiu.setVisible(false);
            textAnStudiu.setVisible(false);

            labelNumarOre.setVisible(false);
            textNrOre.setVisible(false);

            editButton.setVisible(false);
            saveButton.setVisible(false);
        }

        if (fereastra == 4) {
            editButton.setVisible(true);
            saveButton.setVisible(true);
            if (Objects.equals(model.getUsername(), modelFrom.getUsername())) {
                editButton.setVisible(false);
                saveButton.setVisible(false);
            }
        }

    }

    private void setEditDate(boolean editDate){
        textUsername.setEditable(editDate);
        textAnStudiu.setEditable(editDate);
        textAdresa.setEditable(editDate);
        textNrMinOre.setEditable(editDate);
        textCNP.setEditable(editDate);
        textDepartament.setEditable(editDate);
        textIBAN.setEditable(editDate);
        textNrContract.setEditable(editDate);
        textNrMaxOre.setEditable(editDate);
        textEmail.setEditable(editDate);
        textNume.setEditable(editDate);
        textPrenume.setEditable(editDate);
        textParola.setEditable(editDate);
        textNrTel.setEditable(editDate);
        textNrOre.setEditable(editDate);
    }
}
