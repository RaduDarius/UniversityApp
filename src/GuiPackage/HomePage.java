package GuiPackage;

import MainPackage.Conversie;
import ModelsPackage.Rol;
import ModelsPackage.UserModel;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePage implements ActionListener {
    private static final int NR_OF_BUTTONS = 9;
    JFrame myFrame = new JFrame("Home Page");
    JPanel titlePanel = new JPanel();
    JPanel buttonPanel = new JPanel();
    JPanel footerPanel = new JPanel();

    JLabel titleLabel = new JLabel();
    JLabel helloLabel = new JLabel();

    JButton[] buttons = new JButton[NR_OF_BUTTONS];
    JButton logoutButton = new JButton();

    UserModel globalModel;

    public HomePage(UserModel model) {
        myFrame.setVisible(true);
        myFrame.setLocation(450, 150);
        myFrame.setSize(700, 600);
        myFrame.setResizable(false);
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.getContentPane().setBackground(new Color(24, 28, 28));
        myFrame.setLayout(new BorderLayout());

        titlePanel.setBackground(new Color(24, 28, 28));
        titlePanel.add(titleLabel);

        titleLabel.setFont(new Font("Droid Sans", Font.BOLD, 24));
        titleLabel.setText("Home Page");
        titleLabel.setForeground(new Color(160, 196, 44));

        buttonPanel.setLayout(new GridLayout(3, 3));
        buttonPanel.setBackground(new Color(173, 194, 172));

        globalModel = model;

        setButtonPanel();

        setStyle();

    }

    private void setStyle() {

        footerPanel.setBackground(new Color(24, 28, 28));
        logoutButton.addActionListener(this);
        logoutButton.setText("Log out");
        logoutButton.setForeground(Color.BLACK);
        logoutButton.setFont(new Font("Times New Roman", Font.BOLD, 22));
        logoutButton.setBackground(new Color(160, 196, 44));
        logoutButton.setFocusable(false);


        footerPanel.setLayout(new GridLayout(2, 1));
        helloLabel.setText("                               Welcome, " + globalModel.getPrenume() + " !      You are a " + Conversie.convRolToString(globalModel.getRol()));
        helloLabel.setForeground(new Color(160, 196, 44));
        helloLabel.setFont(new Font("Times New Roman", Font.BOLD, 22));
        helloLabel.setBorder(new EtchedBorder());
        footerPanel.add(helloLabel);
        footerPanel.add(logoutButton);

        myFrame.add(titlePanel, BorderLayout.NORTH);
        myFrame.add(buttonPanel, BorderLayout.CENTER);
        myFrame.add(footerPanel, BorderLayout.SOUTH);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == logoutButton) {
            myFrame.dispose();
            new Logare();
        } else if (e.getSource() == buttons[0]) {
            myFrame.dispose();
            new VizualizareDate(globalModel, 0, null);
        } else if (e.getSource() == buttons[1]) {
            myFrame.dispose();
            new VizualizareCursuri(globalModel, 1, null, null);

        } else if (e.getSource() == buttons[2]) {
            myFrame.dispose();
            //VA DESCHIDE FEREASTRA DE CHAT
            new VizualizareCursuri(globalModel, 7, null, null);
        } else if (e.getSource() == buttons[3]) {
            myFrame.dispose();
            if (globalModel.getRol() == Rol.STUDENT) {
                //FEREASTRA PENTRU INROLARE LA CURS
                new VizualizareCursuri(globalModel, 2, null, null);

            } else if (globalModel.getRol() == Rol.PROFESOR) {

                // SETARE FORMULA
                new VizualizareCursuri(globalModel, 3, null, null);

            } else if (globalModel.getRol() == Rol.ADMIN || globalModel.getRol() == Rol.SUPER_ADMIN) {
                //FEREASTRA DE INROLARE PROF/STUDENT LA CURS
                new VizualizareCursuri(globalModel, 2, null, null);
            }
        }else if (e.getSource() == buttons[4]) {
            if (globalModel.getRol() == Rol.ADMIN || globalModel.getRol() == Rol.SUPER_ADMIN) {
                myFrame.dispose();
                // SETARE FORMULA
                new VizualizareCursuri(globalModel, 3, null, null);
            } else {
                myFrame.dispose();
                new Program(globalModel);
            }
        }else if (e.getSource() == buttons[5]) {
            if (globalModel.getRol() == Rol.ADMIN || globalModel.getRol() == Rol.SUPER_ADMIN) {
                myFrame.dispose();
                //CAUTARE UTILIZATOR
                new VizualizareCursuri(globalModel, 4, null, null);
            }else if (globalModel.getRol() == Rol.STUDENT) {
                myFrame.dispose();
                //VIZUALIZARE NOTE
                new VizualizareCursuri(globalModel, 5, null, null);
            } else if (globalModel.getRol() == Rol.PROFESOR) {
                myFrame.dispose();
                //CREARE ACTIVITATE
                new VizualizareCursuri(globalModel, 6, null, null);
            }

        } else if (e.getSource() == buttons[6]) {
            if (globalModel.getRol() == Rol.ADMIN || globalModel.getRol() == Rol.SUPER_ADMIN) {
                myFrame.dispose();
                //CREARE ACTIVITATE
                new VizualizareCursuri(globalModel, 6, null, null);
            }
        }else if (e.getSource() == buttons[7]) {
            if (globalModel.getRol() == Rol.ADMIN || globalModel.getRol() == Rol.SUPER_ADMIN) {
                myFrame.dispose();
                new RegisterUser(globalModel);
            }

        }
    }
    private void setButtonPanel(){

        for (int i = 0; i < NR_OF_BUTTONS; i++) {
            buttons[i] = new JButton();
            buttonPanel.add(buttons[i]);
            buttons[i].setFont(new Font("Times New Roman", Font.BOLD, 22));
            buttons[i].setFocusable(false);
            buttons[i].addActionListener(this);
            buttons[i].setBackground(new Color(80,84,84));
            buttons[i].setForeground(new Color(160, 196, 44));
        }

        buttons[0].setText("Date personale");
        buttons[1].setText("Informatii cursuri");
        buttons[2].setText("Chat");

        if (globalModel.getRol() == Rol.STUDENT){
            buttons[3].setText("Inrolare la curs");
            buttons[4].setText("Program");
            buttons[5].setText("Vizualizare note");
        }

        if (globalModel.getRol() == Rol.PROFESOR){
            buttons[3].setText("Setare formula");
            buttons[4].setText("Program");
            buttons[5].setText("Creare activitate");
        }

        if (globalModel.getRol() == Rol.ADMIN || globalModel.getRol() == Rol.SUPER_ADMIN){
            buttons[3].setText("Inrolare la curs");
            buttons[4].setText("Setare formula");
            buttons[5].setText("Cautare utilizator");
            buttons[6].setText("Creare activitate");
            buttons[7].setText("Inregistrare User");
        }
    }
}
