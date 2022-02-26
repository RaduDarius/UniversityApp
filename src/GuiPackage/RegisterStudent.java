package GuiPackage;

import ConnectionPackage.MyConnection;
import ConnectionPackage.Validator;
import ModelsPackage.Rol;
import ModelsPackage.StudentModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterStudent extends JFrame{
    private JLabel lblcreareUtilizator;
    private JLabel labelUsername;
    private JTextField textUsername;
    private JLabel labelNume;
    private JTextField textNume;
    private JLabel labelPrenume;
    private JTextField textPrenume;
    private JLabel labelCNP;
    private JTextField textCNP;
    private JLabel labelAdresa;
    private JTextField textAdresa;
    private JLabel labelAnStudiu;
    private JTextField textAnStudiu;
    private JLabel labelParola;
    private JPasswordField textParola;
    private JLabel labelEmail;
    private JTextField textEmail;
    private JLabel labelIBAN;
    private JTextField textIBAN;
    private JLabel labelNrContract;
    private JTextField textNrContract;
    private JLabel labelTelefon;
    private JLabel labelNumarOre;
    private JTextField textTelefon;
    private JTextField textNumarOre;
    private JButton submitButton;
    private JPanel inregStudent;
    private JButton buttonBack;

    public RegisterStudent(){
        setContentPane(inregStudent);
        setSize(700, 600);
        setLocation(450, 150);
        setVisible(true);
        setTitle("Inregistrare student");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

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

                StudentModel model = new StudentModel(cnp, nume, prenume, adresa, nrTelefon, email, iban, nrContract, username, parola, Rol.STUDENT, anStudiu, oreSustinute);

                String errorMsg = Validator.validareUser(model, false);
                if (!errorMsg.equals("")){
                    JOptionPane.showMessageDialog(null, errorMsg);
                    return;
                }

                MyConnection.saveStudent(model);

                dispose();
                new Logare();
            }
        });
        buttonBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                dispose();
                new Logare();

            }
        });
    }
}
