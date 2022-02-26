package GuiPackage;

import ConnectionPackage.MyConnection;
import ModelsPackage.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class IntroducereNote extends JFrame {
    JPanel mainPanel = new JPanel();
    JPanel titlePanel = new JPanel();
    JPanel notaPanel = new JPanel();
    JPanel btnPanel = new JPanel();

    JLabel lblTitle = new JLabel("Introducere note");
    JLabel lblNota = new JLabel("Nota: ");

    JTextField txtNota = new JTextField(5);

    JButton btnBack = new JButton("Back");
    JButton btnSave = new JButton("Save");

    public IntroducereNote(UserModel model, StudentModel studNotat, ActivitateDidacticaModel activ){
        super("Introducere note");
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(450, 150);
        this.setResizable(false);
        this.setSize(500, 400);
        this.setContentPane(mainPanel);

        mainPanel.setBackground(new Color(24, 28, 28));
        titlePanel.setBackground(new Color(24, 28, 28));
        notaPanel.setBackground(new Color(24, 28, 28));
        btnPanel.setBackground(new Color(24, 28, 28));

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(titlePanel);
        mainPanel.add(notaPanel);
        mainPanel.add(btnPanel);

        titlePanel.add(lblTitle);
        lblTitle.setFont(new Font("Droid Sans", Font.BOLD, 30));
        lblTitle.setForeground(new Color(160, 196, 44));

        notaPanel.add(lblNota);
        notaPanel.add(txtNota);
        lblNota.setFont(new Font("Times New Roman", Font.BOLD, 20));
        lblNota.setForeground(new Color(160, 196, 44));
        txtNota.setFont(new Font("Times New Roman", Font.BOLD, 20));
        txtNota.setBackground(new Color(32, 36, 36));
        txtNota.setForeground(Color.WHITE);

        btnPanel.add(btnBack);
        btnPanel.add(Box.createHorizontalStrut(30));
        btnPanel.add(btnSave);

        btnBack.setFocusable(false);
        btnBack.setForeground(new Color(8, 6, 3));
        btnBack.setBackground(new Color(160, 196, 44));
        btnBack.setFont(new Font("Times New Roman", Font.BOLD, 20));


        btnSave.setFocusable(false);
        btnSave.setForeground(new Color(8, 6, 3));
        btnSave.setBackground(new Color(160, 196, 44));
        btnSave.setFont(new Font("Times New Roman", Font.BOLD, 20));

        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new VizualizareCursuri(model, -1, activ, null);
            }
        });

        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    double nota = Double.parseDouble(txtNota.getText());

                    if (nota > 10) {
                        JOptionPane.showMessageDialog(null, "Nota introdusa nu este valida !");
                    }else {

                        MyConnection.saveNota(studNotat, activ, nota);
                        JOptionPane.showMessageDialog(null, "Nota atribuita cu succes !");
                    }

                }catch(NumberFormatException ex){
                    JOptionPane.showMessageDialog(null, "Ai introdus caractere invalide!", "Error", JOptionPane.ERROR_MESSAGE, new ImageIcon("icon2.png"));
                }

            }
        });

    }
}
