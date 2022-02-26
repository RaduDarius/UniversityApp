package GuiPackage;

import ConnectionPackage.MyConnection;
import ModelsPackage.ActivitateDidactica;
import ModelsPackage.ActivitateDidacticaModel;
import ModelsPackage.CursModel;
import ModelsPackage.UserModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class SetareFormula extends JFrame {
    JPanel mainPanel = new JPanel();
    JPanel titlePanel = new JPanel();
    JPanel cursPanel = new JPanel();
    JPanel labPanel = new JPanel();
    JPanel seminarPanel = new JPanel();
    JPanel btnPanel = new JPanel();
    
    JLabel lblTitle = new JLabel("Setare formula calcul");
    JLabel lblCurs = new JLabel("Curs: ");
    JLabel lblLab = new JLabel("Laborator: ");
    JLabel lblSeminar = new JLabel("Seminar: ");

    JTextField txtCurs = new JTextField(5);
    JTextField txtLab = new JTextField(5);
    JTextField txtSeminar = new JTextField(5);
    
    JButton btnBack = new JButton("Back");
    JButton btnSave = new JButton("Save");
    private int curs = 0;
    private int lab = 0;
    private int seminar = 0;

    public SetareFormula(UserModel model, CursModel cursModel){
        super("Setare formula");
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
        btnPanel.setBackground(new Color(24, 28, 28));
       
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(titlePanel);
        mainPanel.add(cursPanel);
        mainPanel.add(labPanel);
        mainPanel.add(seminarPanel);
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

        txtCurs.setText("0");
        txtSeminar.setText("0");
        txtLab.setText("0");

        txtCurs.setVisible(false);
        lblCurs.setVisible(false);
        txtSeminar.setVisible(false);
        lblSeminar.setVisible(false);
        txtLab.setVisible(false);
        lblLab.setVisible(false);
        setDataVisible(cursModel);

        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new VizualizareCursuri(model, 3, null, null);
            }
        });

        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int total = 0;
                try{
                    curs = Integer.parseInt(txtCurs.getText());
                    lab = Integer.parseInt(txtLab.getText());
                    seminar = Integer.parseInt(txtSeminar.getText());

                    total += curs + lab + seminar;
                    if(total != 100){
                        JOptionPane.showMessageDialog(null, "Suma procentelor trebuie sa fie 100!", "Error", JOptionPane.ERROR_MESSAGE, new ImageIcon("icon2.png"));
                    }
                    else{
                        System.out.println("Date corecte, salvate!");
                        JOptionPane.showMessageDialog(null, "Date salvate cu succes !", "INFO", JOptionPane.ERROR_MESSAGE, new ImageIcon("icon2.png"));

                        ArrayList<Double> formula = new ArrayList<>();
                        formula.add(curs/100.0);
                        formula.add(seminar/100.0);
                        formula.add(lab/100.0);

                        cursModel.setFormula(formula);

                        MyConnection.saveFormulaMaterie(cursModel);

                    }

                }catch(NumberFormatException ex){
                    JOptionPane.showMessageDialog(null, "Ai introdus caractere invalide!", "Error", JOptionPane.ERROR_MESSAGE, new ImageIcon("icon2.png"));
                }
            }
        });

    }

    private void setDataVisible(CursModel cursModel) {
        for (ActivitateDidacticaModel a: cursModel.getActivitati()) {
            if (a.getTip() == ActivitateDidactica.CURS) {
                txtCurs.setVisible(true);
                lblCurs.setVisible(true);
            }else if (a.getTip() == ActivitateDidactica.SEMINAR) {
                txtSeminar.setVisible(true);
                lblSeminar.setVisible(true);
            }else if (a.getTip() == ActivitateDidactica.LABORATOR) {
                txtLab.setVisible(true);
                lblLab.setVisible(true);
            }
        }
    }

}
