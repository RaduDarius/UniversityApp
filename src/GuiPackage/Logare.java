package GuiPackage;

import ConnectionPackage.MyConnection;
import ModelsPackage.Rol;
import ModelsPackage.UserModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Logare extends JFrame {
    private JLabel lblcreareUtilizator;
    private JLabel labelUsername;
    private JTextField textUsername;
    private JButton loginButton;
    private JButton studentButton;
    private JPanel panelLogare;
    private JPasswordField textParola;

    public Logare(){
        setContentPane(panelLogare);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocation(450, 150);
        setResizable(false);

        studentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new RegisterStudent();
            }
        });
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserModel model = new UserModel(textUsername.getText(), textParola.getText());

                model = MyConnection.findUser(model);
                if (model == null){
                    JOptionPane.showMessageDialog(null, "Date incorecte !");
                    return;
                }

                System.out.println("Logare cu succes !");
                if (model.getRol() == Rol.ADMIN){
                    System.out.println("Bun venit domnule admin");
                }
                else if (model.getRol() == Rol.SUPER_ADMIN) {
                    System.out.println("Bun venit domnule super admin");
                }
                dispose();
                new HomePage(model);
            }
        });
    }


}
