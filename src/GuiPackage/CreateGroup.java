package GuiPackage;

import ConnectionPackage.MyConnection;
import ModelsPackage.*;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class CreateGroup extends JFrame{
    private JLabel lblAdaugareUtilizator;
    private JTextField txtDenumire;
    private JTextArea txtDescriere;
    private JList<String> listProf;
    private JList<String> listCurs;
    private JButton createButton;
    private JButton backButton;
    private JLabel lblDescriere;
    private JLabel lblDenumire;
    private JPanel btnPanel;
    private JPanel mainPanel;
    private JScrollPane scrollPane1;
    private JScrollPane scrollPane2;
    private final JPanel denumirePanel = new JPanel();
    private final JPanel descrierePanel = new JPanel();

    private static ArrayList<ProfesorModel> profi = new ArrayList<>();
    private static ArrayList<CursModel> cursuri = new ArrayList<>();

    public CreateGroup(UserModel model, ConversatieModel toSend) {

        setContentPane(mainPanel);
        setVisible(true);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(900, 650);
        setLocation(450, 100);

        listCurs.setPreferredSize(new Dimension(600, 1000));
        listCurs.setBackground(new Color(24, 28, 28));
        listCurs.setForeground(new Color(160, 196, 44));
        listCurs.setFont(new Font("Times New Roman", Font.BOLD, 18));
        listCurs.setBorder(new EtchedBorder());


        listProf.setPreferredSize(new Dimension(600, 1000));
        listProf.setBackground(new Color(24, 28, 28));
        listProf.setForeground(new Color(160, 196, 44));
        listProf.setFont(new Font("Times New Roman", Font.BOLD, 18));
        listProf.setBorder(new EtchedBorder());


        listProf.setListData(setListProfi());
        listCurs.setListData(setListCursuri());

        txtDenumire.setFont(new Font("Times New Roman", Font.BOLD, 20));
        txtDenumire.setBackground(new Color(32, 36, 36));
        txtDenumire.setForeground(Color.WHITE);

        txtDescriere.setFont(new Font("Times New Roman", Font.BOLD, 20));
        txtDescriere.setBackground(new Color(32, 36, 36));
        txtDescriere.setForeground(Color.WHITE);
        txtDescriere.setSize(new Dimension(500, 200));


        scrollPane1.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(160, 196, 44);
            }
        });

        scrollPane2.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(160, 196, 44);
            }
        });

        scrollPane1.getVerticalScrollBar().setBackground(Color.BLACK);
        scrollPane2.getVerticalScrollBar().setBackground(Color.BLACK);

        scrollPane1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new PrivatChat(model, toSend);
            }
        });

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int indexProfi = listProf.getSelectedIndex();
                int indexCurs = listCurs.getSelectedIndex();

                String denumire = txtDenumire.getText();
                if (denumire.isBlank()){
                    JOptionPane.showMessageDialog(null, "Nu se accepta denumire vida !");
                    return;
                }

                GrupModel grup = new GrupModel(0, cursuri.get(indexCurs), profi.get(indexProfi), txtDescriere.getText(), denumire);

                boolean creat = MyConnection.saveGrup(grup);

                if (creat) {

                    grup.setId(MyConnection.getIdGrup(grup.getProfesor(), grup.getCurs()));

                    MyConnection.inrolareStudentGrup(model, grup);

                    dispose();

                    new InrolareGrup(model, toSend, 4, grup);
                }
            }
        });
    }

    private String[] setListCursuri() {

        cursuri = MyConnection.getAllMaterii();

        int numOfCursuri = 0;
        if (cursuri != null) {
            numOfCursuri = cursuri.size();
        }

        String[] results = new String[numOfCursuri];
        int cont = 0;

        assert cursuri != null;
        for (CursModel c: cursuri) {
            results[cont++] = c.getNume() + " titular fiind, " + c.getTitular().getNume() + " " + c.getTitular().getPrenume();
        }

        return results;
    }

    private String[] setListProfi() {

        profi = MyConnection.getAllProfesori();

        int numOfProfi = 0;
        if (profi != null) {
            numOfProfi = profi.size();
        }

        String[] results = new String[numOfProfi];
        int cont = 0;

        assert profi != null;
        for (ProfesorModel p: profi) {
            results[cont++] = p.getNume() + " " + p.getPrenume();
        }
        return results;
    }

}
