package GuiPackage;

import ConnectionPackage.MyConnection;
import ConnectionPackage.Validator;
import MainPackage.Conversie;
import ModelsPackage.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class RegisterUser extends JFrame {
    private JTextField textPrenume;
    private JTextField textCNP;
    private JTextField textAdresa;
    private JTextField textTelefon;
    private JTextField textEmail;
    private JTextField textIBAN;
    private JTextField textNrContract;
    private JPasswordField textParola;
    private JComboBox<String> comboTipUtilizator;
    private JLabel lblcreareUtilizator;
    private JPanel myPanel;
    private JTextField textUsername;
    private JButton submitButton;
    private JLabel labelNume;
    private JTextField textNume;
    private JLabel labelPrenume;
    private JLabel labelCNP;
    private JLabel labelAdresa;
    private JLabel labelEmail;
    private JLabel labelIBAN;
    private JLabel labelNrContract;
    private JLabel labelTipUtilizator;
    private JLabel labelUsername;
    private JLabel labelParola;
    private JLabel labelTelefon;
    private JTextField textAnStudiu;
    private JLabel labelAnStudiu;
    private JLabel labelDepartament;
    private JTextField textDepartament;
    private JTextField textNumarOre;
    private JLabel labelNumarOre;
    private JButton cancelButton;
    private JComboBox<String> comboDepartament;
    private JLabel labelNrMinOre;
    private JTextField textNrMinOre;
    private JLabel labelNrMaxOre;
    private JTextField textNrMaxOre;
    private JLabel iconUtilizator;

    public RegisterUser(UserModel model){
        setContentPane(myPanel);
        setSize(700, 600);
        setLocation(450, 150);
        setVisible(true);
        setTitle("Inregistrare utilizator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        fillLists(model);

        modificaRoluri();

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String cnp = textCNP.getText();
                String nume = textNume.getText();
                String prenume = textPrenume.getText();
                String adresa = textAdresa.getText();
                String nrTelefon = textTelefon.getText();
                String email = textEmail.getText();
                String iban = textIBAN.getText();
                String nrContract = textNrContract.getText();
                String username = textUsername.getText();
                String parola = textParola.getText();

                if(Objects.equals(comboTipUtilizator.getSelectedItem(), "STUDENT")){

                    int anStudiu, oreSustinute;
                    try {
                        anStudiu = Integer.parseInt(textAnStudiu.getText());
                    }catch (Exception exception){
                        JOptionPane.showMessageDialog(null, "An de studiu invalid");
                        return;
                    }
                    try {
                        oreSustinute = Integer.parseInt(textNumarOre.getText());
                    }catch (Exception exception){
                        JOptionPane.showMessageDialog(null, "numar de ore invalid");
                        return;
                    }

                    StudentModel studentModel = new StudentModel(cnp, nume, prenume, adresa, nrTelefon, email, iban, nrContract, username, parola, Rol.STUDENT, anStudiu, oreSustinute);

                    String errorMsg = Validator.validareUser(studentModel, false);
                    if (!errorMsg.equals("")){
                        JOptionPane.showMessageDialog(null, errorMsg);
                        return;
                    }

                    MyConnection.saveStudent(studentModel);

                }
                else if (Objects.equals(comboTipUtilizator.getSelectedItem(), "PROFESOR")){

                    int minOre, maxOre;
                    try{
                        minOre = Integer.parseInt(textNrMinOre.getText());
                        maxOre = Integer.parseInt(textNrMaxOre.getText());
                    }catch (Exception exception){
                        JOptionPane.showMessageDialog(null, "numar de ore invalid");
                        return;
                    }

                    Departament departament = Conversie.convStringToDepartament((String) Objects.requireNonNull(comboDepartament.getSelectedItem()));

                    ProfesorModel profesorModel = new ProfesorModel(cnp, nume, prenume, adresa, nrTelefon, email, iban, nrContract, username, parola, Rol.PROFESOR, minOre, maxOre, departament);

                    String errorMsg = Validator.validareUser(profesorModel, false);
                    if (!errorMsg.equals("")){
                        JOptionPane.showMessageDialog(null, errorMsg);
                        return;
                    }

                    MyConnection.saveProfesor(profesorModel);
                }
                else if (Objects.equals(comboTipUtilizator.getSelectedItem(), "ADMIN")){
                    UserModel userModel = new UserModel(cnp, nume, prenume, adresa, nrTelefon, email, iban, nrContract, username, parola, Rol.ADMIN);

                    String errorMsg = Validator.validareUser(userModel, false);
                    if (!errorMsg.equals("")){
                        JOptionPane.showMessageDialog(null, errorMsg);
                        return;
                    }

                    MyConnection.saveUser(userModel);
                }
            }
        });
        comboTipUtilizator.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modificaRoluri();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new HomePage(model);
            }
        });
    }

    private void fillLists(UserModel model) {

        comboTipUtilizator.addItem("STUDENT");
        comboTipUtilizator.addItem("PROFESOR");
        if (model.getRol() == Rol.SUPER_ADMIN) {
            comboTipUtilizator.addItem("ADMIN");
        }

        comboDepartament.addItem("CALCULATOARE");
        comboDepartament.addItem("AUTOMATICA");
        comboDepartament.addItem("MATEMATICA");
    }

    private void modificaRoluri(){

        if(Objects.equals(comboTipUtilizator.getSelectedItem(), "STUDENT")){
            //setam elementele specifice studentilor sa fie vizibile si editabile
            labelAnStudiu.setVisible(true);
            textAnStudiu.setVisible(true);
            textAnStudiu.setEditable(true);

            labelNumarOre.setVisible(true);
            textNumarOre.setVisible(true);
            textNumarOre.setEditable(true);

            //setam elementele specifice profesorilor sa fie invizibile si needitabile
            labelNrMaxOre.setVisible(false);
            textNrMaxOre.setVisible(false);
            textNrMaxOre.setEnabled(false);

            labelNrMinOre.setVisible(false);
            textNrMinOre.setVisible(false);
            textNrMinOre.setEditable(false);

            labelDepartament.setVisible(false);
            comboDepartament.setVisible(false);
            comboDepartament.setEditable(false);

            textNrMaxOre.setText("");
            textNrMinOre.setText("");
        }
        else if (Objects.equals(comboTipUtilizator.getSelectedItem(), "PROFESOR")){

            //setam elementele specifice profesorilor sa fie vizibile si editabile
            labelNrMaxOre.setVisible(true);
            textNrMaxOre.setVisible(true);
            textNrMaxOre.setEnabled(true);

            labelNrMinOre.setVisible(true);
            textNrMinOre.setVisible(true);
            textNrMinOre.setEditable(true);

            labelDepartament.setVisible(true);
            comboDepartament.setVisible(true);

            //setam elementele specifice profesorilor sa fie invizibile si needitabile
            labelAnStudiu.setVisible(false);
            textAnStudiu.setVisible(false);
            textAnStudiu.setEditable(false);

            labelNumarOre.setVisible(false);
            textNumarOre.setVisible(false);
            textNumarOre.setEditable(false);

        }
        else if (Objects.equals(comboTipUtilizator.getSelectedItem(), "ADMIN")){
            //setam elementele specifice profesorilor sa fie vizibile si editabile
            labelNrMaxOre.setVisible(false);
            textNrMaxOre.setVisible(false);
            textNrMaxOre.setEnabled(false);

            labelNrMinOre.setVisible(false);
            textNrMinOre.setVisible(false);
            textNrMinOre.setEditable(false);

            labelDepartament.setVisible(false);
            comboDepartament.setVisible(false);

            //setam elementele specifice studentilor sa fie vizibile si editabile
            labelAnStudiu.setVisible(false);
            textAnStudiu.setVisible(false);
            textAnStudiu.setEditable(false);

            labelNumarOre.setVisible(false);
            textNumarOre.setVisible(false);
            textNumarOre.setEditable(false);
        }

    }
}
