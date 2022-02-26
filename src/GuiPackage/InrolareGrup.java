package GuiPackage;

import ConnectionPackage.MyConnection;
import ModelsPackage.*;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class InrolareGrup extends JFrame {

    JPanel mainPanel = new JPanel();
    JPanel titlePanel = new JPanel();
    JPanel headerPanel = new JPanel();
    JPanel listPanel = new JPanel();
    JPanel btnPanel = new JPanel();

    JLabel lblName = new JLabel();
    JLabel lblTitle = new JLabel();

    JTextField txtInput = new JTextField(12);

    JButton btnSearch = new JButton("Search");
    JButton btnBack = new JButton("Back");
    JButton btnSubmit = new JButton("Submit");

    JList<String> listResults = new JList<>();

    private ArrayList<GrupModel> grups = new ArrayList<>();
    private ArrayList<IntalnireModel> meetings = new ArrayList<>();
    private ArrayList<UserModel> membrii = new ArrayList<>();
    private ArrayList<UserModel> sugestii = new ArrayList<>();

    public InrolareGrup(UserModel model, ConversatieModel toSend, int fereastra, GrupModel grupModel) {
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(450, 150);
        this.setResizable(false);
        this.setSize(700, 600);
        this.setContentPane(mainPanel);

        headerPanel.setBackground(new Color(24, 28, 28));
        titlePanel.setBackground(new Color(24, 28, 28));
        mainPanel.setBackground(new Color(24, 28, 28));
        listPanel.setBackground(new Color(24, 28, 28));
        btnPanel.setBackground(new Color(24, 28, 28));

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(titlePanel);
        mainPanel.add(headerPanel);
        mainPanel.add(listPanel);
        mainPanel.add(btnPanel);

        titlePanel.add(lblTitle);
        lblTitle.setFont(new Font("Droid Sans", Font.BOLD, 30));
        lblTitle.setForeground(new Color(160, 196, 44));

        headerPanel.add(lblName);
        lblName.setFont(new Font("Times New Roman", Font.BOLD, 20));
        lblName.setForeground(new Color(160, 196, 44));
        headerPanel.add(Box.createHorizontalStrut(10));

        headerPanel.add(txtInput);
        txtInput.setFont(new Font("Times New Roman", Font.BOLD, 20));
        txtInput.setBackground(new Color(32, 36, 36));
        txtInput.setForeground(Color.WHITE);
        headerPanel.add(Box.createHorizontalStrut(10));

        headerPanel.add(btnSearch);
        btnSearch.setFocusable(false);
        btnSearch.setForeground(new Color(8, 6, 3));
        btnSearch.setBackground(new Color(160, 196, 44));
        btnSearch.setFont(new Font("Times New Roman", Font.BOLD, 20));
        headerPanel.add(Box.createHorizontalStrut(10));

        listPanel.add(listResults);
        listResults.setListData(setList(model, toSend, fereastra, grupModel));
        listResults.setPreferredSize(new Dimension(600, 600));
        listResults.setBackground(new Color(24, 28, 28));
        listResults.setForeground(new Color(160, 196, 44));
        listResults.setFont(new Font("Times New Roman", Font.BOLD, 18));
        listResults.setBorder(new EtchedBorder());

        JScrollPane listScroller = new JScrollPane();
        listScroller.setViewportView(listResults);
        listResults.setLayoutOrientation(JList.VERTICAL);
        listPanel.add(listScroller);
        listScroller.getVerticalScrollBar().setBackground(Color.BLACK);

        btnPanel.add(btnBack);
        btnPanel.add(btnSubmit);

        btnBack.setFocusable(false);
        btnBack.setForeground(new Color(8, 6, 3));
        btnBack.setBackground(new Color(160, 196, 44));
        btnBack.setFont(new Font("Times New Roman", Font.BOLD, 20));

        btnSubmit.setFocusable(false);
        btnSubmit.setForeground(new Color(8, 6, 3));
        btnSubmit.setBackground(new Color(160, 196, 44));
        btnSubmit.setFont(new Font("Times New Roman", Font.BOLD, 20));

        btnPanel.add(Box.createHorizontalStrut(10));

        setTitle(fereastra);
        setDataVisible(fereastra);

        listScroller.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(160, 196, 44);
            }
        });

        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();

                if (fereastra == 4) {
                    new CreateGroup(model, toSend);
                }else {
                    new PrivatChat(model, toSend);
                }
            }
        });

        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Searching " + txtInput.getText() + " ...");

                String searchText = txtInput.getText();

                if (fereastra == 1) {
                    int numOfRes = grups.size();

                    String[] results = new String[numOfRes];
                    int cont = 0;

                    for (GrupModel grup : grups) {

                        if (grup.getDenumire().contains(searchText)) {
                            results[cont++] = grup.getDenumire();
                        } else if (searchText.contains(grup.getDenumire())) {
                            results[cont++] = grup.getDenumire();
                        }

                    }
                    listResults.setListData(results);
                }
                else if (fereastra == 2) {
                    int numOfRes = meetings.size();

                    String[] results = new String[numOfRes];
                    int cont = 0;

                    for (IntalnireModel meeting : meetings) {

                        if (meeting.getDenumire().contains(searchText)) {
                            results[cont++] = meeting.getDenumire();
                        } else if (searchText.contains(meeting.getDenumire())) {
                            results[cont++] = meeting.getDenumire();
                        }

                    }
                    listResults.setListData(results);
                }
                else if (fereastra == 3) {
                    int numOfRes = membrii.size();
                    String[] results = new String[numOfRes];
                    int cont = 0;

                    for (UserModel userModel : membrii) {
                        if (userModel.getNume().contains(searchText)) {
                            results[cont++] = userModel.getNume() + " " + userModel.getPrenume();
                        } else if (userModel.getPrenume().contains(searchText)) {
                            results[cont++] = userModel.getNume() + " " + userModel.getPrenume();
                        } else if (searchText.contains(userModel.getNume())) {
                            results[cont++] = userModel.getNume() + " " + userModel.getPrenume();
                        } else if (searchText.contains(userModel.getPrenume())) {
                            results[cont++] = userModel.getNume() + " " + userModel.getPrenume();
                        }
                    }
                    listResults.setListData(results);
                }
                else if (fereastra == 4) {
                    int numOfRes = sugestii.size();
                    String[] results = new String[numOfRes];
                    int cont = 0;

                    for (UserModel userModel : sugestii) {
                        if (userModel.getNume().contains(searchText)) {
                            results[cont++] = userModel.getNume() + " " + userModel.getPrenume();
                        } else if (userModel.getPrenume().contains(searchText)) {
                            results[cont++] = userModel.getNume() + " " + userModel.getPrenume();
                        } else if (searchText.contains(userModel.getNume())) {
                            results[cont++] = userModel.getNume() + " " + userModel.getPrenume();
                        } else if (searchText.contains(userModel.getPrenume())) {
                            results[cont++] = userModel.getNume() + " " + userModel.getPrenume();
                        }
                    }
                    listResults.setListData(results);
                }
                else {
                    listResults.setListData(new String[]{});
                }
            }
        });

        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int index = listResults.getSelectedIndex();

                if (fereastra == 1) {
                    GrupModel grup = grups.get(index);
                    System.out.println(grup.getId());
                    MyConnection.inrolareStudentGrup(model, grup);
                    JOptionPane.showMessageDialog(null, "Inrolat cu succes la grup !");
                }
                else if (fereastra == 2) {
                    IntalnireModel meet = meetings.get(index);
                    MyConnection.saveStudentIntalnire(model, meet);
                    JOptionPane.showMessageDialog(null, "Inrolat cu succes la intalnire !");
                }
                else if (fereastra == 4) {

                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm-ss");
                    LocalDateTime now = LocalDateTime.now();
                    String date = dtf.format(now);

                    if (index == -1) {
                        for (UserModel u: sugestii) {
                            MyConnection.saveMesaj(new MesajPrivatModel(
                                    u,
                                    model,
                                    "Buna, am creat un grup pentru studiu la disciplina " +
                                            "\n" + grupModel.getCurs().getNume() + " esti invitat sa ni te alaturi oricand !",
                                    date));
                        }

                        JOptionPane.showMessageDialog(null, "Mesaje trimise cu succes !");

                    }else {

                        MyConnection.saveMesaj(new MesajPrivatModel(
                                sugestii.get(index),
                                model,
                                "Buna, am creat un grup pentru studiu la disciplina " +
                                        "\n" + grupModel.getCurs().getNume() + " esti invitat sa ni te alaturi oricand !",
                                date));
                        JOptionPane.showMessageDialog(null, "Mesaj trimis cu succes !");
                    }
                }
            }
        });

    }

    private void setDataVisible(int fereastra) {

        if (fereastra == 3) {
            btnSubmit.setVisible(false);
        }
        else if (fereastra == 4) {
            btnSubmit.setText("Invita in grup");
        }

    }

    private String[] setList(UserModel model, ConversatieModel toSend, int fereastra, GrupModel grupModel) {

        if (fereastra == 1) {
            grups = MyConnection.getAllGrups(model);

            int numOfGrups = 0;
            if (grups != null) {
                numOfGrups = grups.size();
            }

            String[] results = new String[numOfGrups];
            int cont = 0;

            assert grups != null;
            for (GrupModel g: grups) {
                results[cont++] = g.getDenumire();
            }

            return results;
        }
        else if (fereastra == 2) {
            meetings = MyConnection.getMeetingByGrup(model, (GrupModel)toSend);

            int numOfMeetings = 0;
            if (meetings != null) {
                numOfMeetings = meetings.size();
            }

            String[] results = new String[numOfMeetings];
            int cont = 0;

            assert meetings != null;
            for (IntalnireModel m: meetings) {
                results[cont++] = m.getDenumire();
            }

            return results;

        }
        else if (fereastra == 3) {
            membrii = MyConnection.getAllUsersFromGrup((GrupModel) toSend);

            int numOfMembers = 0;
            if (membrii != null) {
                numOfMembers = membrii.size();
            }

            String[] results = new String[numOfMembers];
            int cont = 0;

            assert membrii != null;
            for (UserModel u: membrii) {
                results[cont++] = u.getNume() + " " + u.getPrenume();
            }

            return results;
        }
        else if (fereastra == 4) {
            sugestii = MyConnection.getSugestii(grupModel);

            int numOfSugestii = 0;
            if (sugestii != null) {
                numOfSugestii = sugestii.size();
            }

            String[] results = new String[numOfSugestii];
            int cont = 0;

            assert sugestii != null;
            for (UserModel u: sugestii) {
                results[cont++] = u.getNume() + " " + u.getPrenume();
            }

            return results;
        }
        return new String[]{""};
    }

    private void setTitle(int fereastra) {

        if (fereastra == 1) {
            lblTitle.setText("Vizualizare grupuri");
        }
        else if (fereastra == 2) {
            lblTitle.setText("Vizualizare intalniri");
        }
        else if (fereastra == 3) {
            lblTitle.setText("Vizualizare participanti");
        }
        else if (fereastra == 4) {
            lblTitle.setText("Vizualizare sugestii");
        }

    }

}
