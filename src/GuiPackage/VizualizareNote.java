package GuiPackage;

import ConnectionPackage.MyConnection;
import ModelsPackage.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class VizualizareNote extends JFrame {
    JPanel mainPanel = new JPanel();
    JPanel titlePanel = new JPanel();
    JPanel cursPanel = new JPanel();
    JPanel labPanel = new JPanel();
    JPanel seminarPanel = new JPanel();
    JPanel mediePanel = new JPanel();
    JPanel btnPanel = new JPanel();

    JLabel lblTitle = new JLabel("Vizualizare note");
    JLabel lblCurs = new JLabel("Curs: ");
    JLabel lblLab = new JLabel("Laborator: ");
    JLabel lblSeminar = new JLabel("Seminar: ");
    JLabel lblMedie = new JLabel("Media: ");

    JTextField txtCurs = new JTextField(5);
    JTextField txtLab = new JTextField(5);
    JTextField txtSeminar = new JTextField(5);
    JTextField txtMedia = new JTextField(5);

    JButton btnBack = new JButton("Back");

    public VizualizareNote(UserModel model, CursModel curs){
        super("Introducere note");
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(450, 150);
        this.setResizable(false);
        this.setSize(500, 400);
        this.setContentPane(mainPanel);

        mainPanel.setBackground(new Color(24, 28, 28));
        titlePanel.setBackground(new Color(24, 28, 28));
        cursPanel.setBackground(new Color(24, 28, 28));
        labPanel.setBackground(new Color(24, 28, 28));
        seminarPanel.setBackground(new Color(24, 28, 28));
        mediePanel.setBackground(new Color(24, 28, 28));
        btnPanel.setBackground(new Color(24, 28, 28));

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(titlePanel);
        mainPanel.add(cursPanel);
        mainPanel.add(labPanel);
        mainPanel.add(seminarPanel);
        mainPanel.add(mediePanel);
        mainPanel.add(btnPanel);

        titlePanel.add(lblTitle);
        lblTitle.setFont(new Font("Droid Sans", Font.BOLD, 30));
        lblTitle.setForeground(new Color(160, 196, 44));

        cursPanel.add(lblCurs);
        cursPanel.add(txtCurs);
        lblCurs.setFont(new Font("Times New Roman", Font.BOLD, 20));
        lblCurs.setForeground(new Color(160, 196, 44));
        txtCurs.setFont(new Font("Times New Roman", Font.BOLD, 20));
        txtCurs.setBackground(new Color(32, 36, 36));
        txtCurs.setForeground(Color.WHITE);

        labPanel.add(lblLab);
        labPanel.add(txtLab);
        lblLab.setFont(new Font("Times New Roman", Font.BOLD, 20));
        lblLab.setForeground(new Color(160, 196, 44));
        txtLab.setFont(new Font("Times New Roman", Font.BOLD, 20));
        txtLab.setBackground(new Color(32, 36, 36));
        txtLab.setForeground(Color.WHITE);

        seminarPanel.add(lblSeminar);
        seminarPanel.add(txtSeminar);
        lblSeminar.setFont(new Font("Times New Roman", Font.BOLD, 20));
        lblSeminar.setForeground(new Color(160, 196, 44));
        txtSeminar.setFont(new Font("Times New Roman", Font.BOLD, 20));
        txtSeminar.setBackground(new Color(32, 36, 36));
        txtSeminar.setForeground(Color.WHITE);

        mediePanel.add(lblMedie);
        mediePanel.add(txtMedia);
        lblMedie.setFont(new Font("Times New Roman", Font.BOLD, 20));
        lblMedie.setForeground(new Color(160, 196, 44));
        txtMedia.setFont(new Font("Times New Roman", Font.BOLD, 20));
        txtMedia.setBackground(new Color(32, 36, 36));
        txtMedia.setForeground(Color.WHITE);

        btnPanel.add(btnBack);
        btnPanel.add(Box.createHorizontalStrut(30));

        btnBack.setFocusable(false);
        btnBack.setForeground(new Color(8, 6, 3));
        btnBack.setBackground(new Color(160, 196, 44));
        btnBack.setFont(new Font("Times New Roman", Font.BOLD, 20));

        txtCurs.setEditable(false);
        txtLab.setEditable(false);
        txtSeminar.setEditable(false);

        setValoriNote(model, curs);

        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new VizualizareCursuri(model, 5, null, null);
            }
        });

    }

    void setValoriNote(UserModel model, CursModel curs) {

        ArrayList<ActivitateDidacticaModel> activitati = MyConnection.getAllActivitatiDidacticeByIdMaterie(curs.getId());

        lblCurs.setVisible(false);
        lblLab.setVisible(false);
        lblSeminar.setVisible(false);

        txtSeminar.setVisible(false);
        txtCurs.setVisible(false);
        txtLab.setVisible(false);

        boolean hasNecules = false;

        assert activitati != null;
        for (ActivitateDidacticaModel a: activitati) {

            double nota = MyConnection.getNoteStudentByIdActivitate(model, a.getId());

            if (a.getTip() == ActivitateDidactica.CURS) {
                lblCurs.setVisible(true);
                txtCurs.setVisible(true);
                txtCurs.setText(String.valueOf(nota));
                if (nota == 0) {
                    txtCurs.setText("Necules");
                    hasNecules = true;
                }
            }else if (a.getTip() == ActivitateDidactica.SEMINAR) {
                lblSeminar.setVisible(true);
                txtSeminar.setVisible(true);
                txtSeminar.setText(String.valueOf(nota));
                if (nota == 0) {
                    txtSeminar.setText("Necules");
                    hasNecules = true;
                }
            } else if (a.getTip() == ActivitateDidactica.LABORATOR) {
                lblLab.setVisible(true);
                txtLab.setVisible(true);
                txtLab.setText(String.valueOf(nota));
                if (nota == 0) {
                    txtLab.setText("Necules");
                    hasNecules = true;
                }
            }
        }

        if (hasNecules) {
            txtMedia.setText("Necules");
        } else {
            double media = MyConnection.getMedieStudentByIdMaterie(model, curs.getId());
            txtMedia.setText(String.valueOf(media));
        }

    }

}
