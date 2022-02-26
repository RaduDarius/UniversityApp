package GuiPackage;

import ConnectionPackage.MyConnection;
import ModelsPackage.ActivitateDidacticaModel;
import ModelsPackage.UserModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Program extends JFrame {
    JPanel mainPanel = new JPanel();
    JPanel titlePanel = new JPanel();
    JPanel activitiesPanel = new JPanel();
    JPanel buttonPanel = new JPanel();

    JLabel lblTitle = new JLabel("Calendar activitati");

    JButton btnBack = new JButton("Back");
    JButton btnDownload = new JButton("Descarcare activitati");
    JButton btnDownloadAll = new JButton("Descarca toate activitatile");

    ArrayList<ActivitateDidacticaModel> activitati = new ArrayList<>();

    public Program(UserModel model){
        super("Calendar");
        this.setVisible(true);
        this.setLocation(450, 150);
        this.setSize(850, 600);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.getContentPane().setBackground(new Color(24, 28, 28));

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(titlePanel);
        titlePanel.setBackground(new Color(24, 28, 28));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 30, 10));
        titlePanel.add(lblTitle);
        lblTitle.setFont(new Font("Droid Sans", Font.BOLD, 24));
        lblTitle.setForeground(new Color(160, 196, 44));

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();

        int day = now.getDayOfWeek().getValue();

        String[] days = { "Luni", "Marti", "Miercuri", "Joi", "Vineri", "Sambata", "Duminica"};

        String[] columnNames = { days[day - 1] };
        String[][] data =  setData(model, dtf.format(now));
        JTable activities = new JTable(data, columnNames);
        activitiesPanel.add(activities);

        mainPanel.add(activitiesPanel);
        activitiesPanel.setLayout(new BorderLayout());
        activitiesPanel.add(activities.getTableHeader(), BorderLayout.NORTH);
        activitiesPanel.add(activities, BorderLayout.CENTER);
        activitiesPanel.setBackground(new Color(24, 28, 28));
        activities.setFont(new Font("Times New Roman", Font.PLAIN, 25));
        activities.setDefaultEditor(Object.class, null);
        activities.getColumnModel().getColumn(0).setPreferredWidth(120);
        activities.setBackground(new Color(24, 28, 28));
        activities.setForeground(new Color(160, 196, 44));
        activities.setRowHeight(50);

        activities.getTableHeader().setFont(new Font("Times New Roman", Font.BOLD, 25));
        activities.getTableHeader().setBackground(new Color(24, 28, 28));
        activities.getTableHeader().setForeground(new Color(160, 196, 44));

        buttonPanel.add(btnBack);
        buttonPanel.add(Box.createHorizontalStrut(50));
        buttonPanel.add(btnDownload);
        buttonPanel.add(btnDownloadAll);
        mainPanel.add(buttonPanel);

        btnBack.setFocusable(false);
        btnBack.setForeground(new Color(8, 6, 3));
        btnBack.setBackground(new Color(160, 196, 44));
        btnBack.setFont(new Font("Times New Roman", Font.BOLD, 20));

        btnDownload.setFocusable(false);
        btnDownload.setForeground(new Color(8, 6, 3));
        btnDownload.setBackground(new Color(160, 196, 44));
        btnDownload.setFont(new Font("Times New Roman", Font.BOLD, 20));

        btnDownloadAll.setFocusable(false);
        btnDownloadAll.setForeground(new Color(8, 6, 3));
        btnDownloadAll.setBackground(new Color(160, 196, 44));
        btnDownloadAll.setFont(new Font("Times New Roman", Font.BOLD, 20));

        buttonPanel.setBackground(new Color(24, 28, 28));

        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new HomePage(model);
            }
        });

        btnDownload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MyConnection.descarcareProgram(activitati);
            }
        });

        btnDownloadAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                ArrayList<ActivitateDidacticaModel> aList = MyConnection.getActivitatiByDate(model, null);

                assert aList != null;
                MyConnection.descarcareProgramAll(aList);
            }
        });

    }

    private String[][] setData(UserModel model, String now) {

        activitati = MyConnection.getActivitatiByDate(model, now);

        int numOfActiv = 0;
        if (activitati != null) {
            numOfActiv = activitati.size();
        }

        String[][] results = new String[numOfActiv][1];
        int cont = 0;

        assert activitati != null;
        for (ActivitateDidacticaModel a: activitati) {
            results[cont++][0] = a.getNume() + " de la ora " +
                    a.getDataIncepere().getOra() + ":" +
                    a.getDataIncepere().getMinute() + " si dureaza " +
                    a.getDurataActivitate() + "h";
        }
        return results;
    }
}
