package GuiPackage;

import ConnectionPackage.MyConnection;
import MainPackage.Conversie;
import ModelsPackage.*;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VizualizareCursuri extends JFrame {

    JPanel mainPanel = new JPanel();
    JPanel titlePanel = new JPanel();
    JPanel headerPanel = new JPanel();
    JPanel listPanel = new JPanel();
    JPanel btnPanel = new JPanel();

    JLabel lblName = new JLabel();
    JLabel lblTitle = new JLabel("Vizualizare cursuri");

    JTextField txtInput = new JTextField(12);

    JButton btnSearch = new JButton("Search");
    JButton btnBack = new JButton("Back");
    JButton btnAfis = new JButton("Afisare studenti");
    JButton btnRenuntare = new JButton("Renuntare curs");
    JButton btnDescarcare = new JButton("Descarca lista cu activitati");

    JList<String> listResults = new JList<>();

    private static ArrayList<ActivitateDidacticaModel> activitati = new ArrayList<>();
    private static ArrayList<StudentModel> studenti = new ArrayList<>();
    private static ArrayList<CursModel> cursuri = new ArrayList<>();
    private static ArrayList<UserModel> users = new ArrayList<>();
    private static ArrayList<ConversatieModel> contacts = new ArrayList<>();

    JComboBox<String> comboFilter = new JComboBox<>();

    public VizualizareCursuri(UserModel model, int fereastra, ActivitateDidacticaModel activ, UserModel user){
        super(setTitlu(fereastra));
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
        setLabelSearch(model, fereastra, user);
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

        headerPanel.add(comboFilter);
        comboFilter.setFocusable(false);
        comboFilter.setFont(new Font("Times New Roman", Font.BOLD, 18));
        comboFilter.setBackground(new Color(8, 6, 3));
        comboFilter.setForeground(new Color(160, 196, 44));

        listPanel.add(listResults);
        listResults.setListData(setList(model, fereastra, activ, user));
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
        btnPanel.add(btnAfis);
        btnPanel.add(btnRenuntare);
        btnPanel.add(btnDescarcare);

        btnBack.setFocusable(false);
        btnBack.setForeground(new Color(8, 6, 3));
        btnBack.setBackground(new Color(160, 196, 44));
        btnBack.setFont(new Font("Times New Roman", Font.BOLD, 20));
        btnPanel.add(Box.createHorizontalStrut(10));

        btnAfis.setFocusable(false);
        btnAfis.setForeground(new Color(8, 6, 3));
        btnAfis.setBackground(new Color(160, 196, 44));
        btnAfis.setFont(new Font("Times New Roman", Font.BOLD, 20));

        btnRenuntare.setFocusable(false);
        btnRenuntare.setForeground(new Color(8, 6, 3));
        btnRenuntare.setBackground(new Color(160, 196, 44));
        btnRenuntare.setFont(new Font("Times New Roman", Font.BOLD, 20));

        btnDescarcare.setFocusable(false);
        btnDescarcare.setForeground(new Color(8, 6, 3));
        btnDescarcare.setBackground(new Color(160, 196, 44));
        btnDescarcare.setFont(new Font("Times New Roman", Font.BOLD, 20));

        setDataVisible(model, fereastra);

        if (fereastra == 4 || fereastra == 2) {
            fillList(fereastra);
        }

        listScroller.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(160, 196, 44);
            }
        });

        comboFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Am selectat: " + comboFilter.getSelectedItem());

                if (Objects.equals(comboFilter.getSelectedItem(), "NONE")) {
                    listResults.setListData(setList(model, fereastra, activ, user));

                    String searchText = txtInput.getText();
                    int numOfRes = users.size();
                    String[] results = new String[numOfRes];
                    int cont = 0;

                    for (UserModel userModel : users) {
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
                    users = MyConnection.getUsersByTip(Conversie.convStringToRol((String) comboFilter.getSelectedItem()));

                    int numOfUsers = 0;
                    if (users != null) {
                        numOfUsers = users.size();
                    }

                    int contor = 0;
                    String[] results2 = new String[numOfUsers];

                    assert users != null;
                    for (UserModel u: users) {
                        results2[contor++] = u.getNume() + " " + u.getPrenume();
                    }

                    listResults.setListData(results2);

                    String searchText = txtInput.getText();
                    int numOfRes = users.size();
                    String[] results = new String[numOfRes];
                    int cont = 0;

                    for (UserModel userModel : users) {
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

            }
        });

        listResults.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

                if (fereastra == 1) {
                    String text;

                    int index = listResults.getSelectedIndex();
                    text = "Denumire: " + activitati.get(index).getNume() + "\n" +
                            "Data incepere: " + activitati.get(index).getDataIncepere().toString() + "\n" +
                            "Data terminare: " + activitati.get(index).getDataTerminare().toString() + "\n" +
                            "Durata activitatii: " + activitati.get(index).getDurataActivitate() + "h\n" +
                            "Numar maxim de participanti: " + activitati.get(index).getNrMaximParticipanti();

                    if (!activitati.get(index).getNumeProf().equals("null null"))
                        text = text + "\n" +
                                "Profesor: " + activitati.get(index).getNumeProf();
                    else
                        text = text + "\n" +
                                "nu are un profesor asignat";

                    JOptionPane.showMessageDialog(null, text, "Informatii activitate", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("icon2.png"));
                }
                else if (fereastra == -1) {

                    int index = listResults.getSelectedIndex();
                    StudentModel studentSelectat = studenti.get(index);

                    dispose();
                    new IntroducereNote(model, studentSelectat, activ);
                }
                else if (fereastra == 2) {
                    int index = listResults.getSelectedIndex();
                    if (model.getRol() == Rol.STUDENT) {

                        String numMaterie = cursuri.get(index).getNume();

                        ArrayList<CursModel> cursuriDorite = new ArrayList<>();
                        ArrayList<CursModel> cursuriEnrol = MyConnection.getAllMateriiForEnrol(model);

                        assert cursuriEnrol != null;
                        for (CursModel c: cursuriEnrol) {
                            if (c.getNume().equals(numMaterie)) {
                                cursuriDorite.add(c);
                            }
                        }

                        ArrayList<CursModel> cursuriPosibile = new ArrayList<>(cursuriDorite);

                        for (CursModel c: cursuriDorite) {
                            boolean areLoc = true;
                            for (ActivitateDidacticaModel a: c.getActivitati()) {
                                int numOfStud = MyConnection.getNumOfStudByActivitate(a.getId());
                                if (numOfStud + 1 > a.getNrMaximParticipanti()) {
                                    areLoc = false;
                                }
                            }

                            if (!areLoc) {
                                cursuriPosibile.remove(c);
                            }
                        }

                        if (cursuriPosibile.size() == 0) {
                            JOptionPane.showMessageDialog(null, "NU SE POATE INSCRIE LA CURS, NU MAI SUNT LOCURI DISPONIBILE!");
                        }
                        else {

                            // VERIFICAM DACA APARTINE INTERVALULUI, DACA DA, RETURNAM EROARE

                            ArrayList<ActivitateDidacticaModel> activitatiParticipante = MyConnection.getAllActivitatiDidacticeFromUser(model);

                            ArrayList<CursModel> cursuriDeVerificat = new ArrayList<>();

                            boolean ok = false;

                            for (CursModel c: cursuriPosibile) {
                                boolean poate = true;
                                assert activitatiParticipante != null;
                                for (ActivitateDidacticaModel ap : activitatiParticipante) {

                                    DateModel dataInepere_activitateParticipanta = ap.getDataIncepere();
                                    DateModel dataTerminare_activitateParticipanta = ap.getDataIncepere().adunare(ap.getDurataActivitate());

                                    for (ActivitateDidacticaModel ae : c.getActivitati()) {
                                        DateModel dataIncepere_activitateEnrol = ae.getDataIncepere();
                                        DateModel dataTerminare_activitateEnrol = ae.getDataIncepere().adunare(ae.getDurataActivitate());

                                        if (comparareZile(dataIncepere_activitateEnrol.getZi() % 7,
                                                dataTerminare_activitateEnrol.getZi() % 7,
                                                dataInepere_activitateParticipanta.getZi() % 7,
                                                dataTerminare_activitateParticipanta.getZi() % 7)) {

                                            if (!((dataTerminare_activitateEnrol.comparare(dataInepere_activitateParticipanta) <= 1)
                                                    || (dataTerminare_activitateParticipanta.comparare(dataIncepere_activitateEnrol) <= 1))) {

                                                poate = false;
                                            }
                                        }
                                    }
                                }

                                if (poate) {
                                    ok = true;
                                    cursuriDeVerificat.add(c);
                                }

                            }

                            if (ok) {

                                int numCursuriDeVerificat = cursuriDeVerificat.size();
                                int numMinStud = MyConnection.getNumOfStudByMaterie(cursuriDeVerificat.get(0).getId());
                                int indexCursDorit = 0;

                                for (int i = 1; i < numCursuriDeVerificat; i++) {
                                    int num = MyConnection.getNumOfStudByMaterie(cursuriDeVerificat.get(i).getId());

                                    if (num < numMinStud) {
                                        indexCursDorit = i;
                                        numMinStud = num;
                                    }
                                }

                                MyConnection.inrolareUser(model, cursuriDeVerificat.get(indexCursDorit).getId());

                            }
                            else {
                                JOptionPane.showMessageDialog(null, "NU SE POATE INSCRIE LA CURS, SE SUPRAPUNE CU ALTE ORE!");
                            }

                        }
                    }
                    else {
                        UserModel user = users.get(index);
                        dispose();
                        new VizualizareCursuri(model, -2, null, user);
                    }
                }
                else if (fereastra == -2) {
                    int index = listResults.getSelectedIndex();

                    if (user.getRol() == Rol.STUDENT) {

                        CursModel cursDorit = cursuri.get(index);

                        boolean poate = true;
                        for (ActivitateDidacticaModel a: cursDorit.getActivitati()) {
                            int numOfStud = MyConnection.getNumOfStudByActivitate(a.getId());
                            if (numOfStud + 1 > a.getNrMaximParticipanti()) {
                                poate = false;
                            }
                        }

                        ArrayList<ActivitateDidacticaModel> activitatiParticipante = MyConnection.getAllActivitatiDidacticeFromUser(user);

                        for (ActivitateDidacticaModel a: cursDorit.getActivitati()) {
                            System.out.println(a.toString());
                        }

                        assert activitatiParticipante != null;
                        for (ActivitateDidacticaModel ap : activitatiParticipante) {

                            DateModel dataInepere_activitateParticipanta = ap.getDataIncepere();
                            DateModel dataTerminare_activitateParticipanta = ap.getDataIncepere().adunare(ap.getDurataActivitate());

                            for (ActivitateDidacticaModel ae : cursDorit.getActivitati()) {
                                DateModel dataIncepere_activitateEnrol = ae.getDataIncepere();
                                DateModel dataTerminare_activitateEnrol = ae.getDataIncepere().adunare(ae.getDurataActivitate());

                                if (comparareZile(dataIncepere_activitateEnrol.getZi() % 7,
                                        dataTerminare_activitateEnrol.getZi() % 7,
                                        dataInepere_activitateParticipanta.getZi() % 7,
                                        dataTerminare_activitateParticipanta.getZi() % 7)) {

                                    if (!((dataTerminare_activitateEnrol.comparare(dataInepere_activitateParticipanta) <= 1)
                                            || (dataTerminare_activitateParticipanta.comparare(dataIncepere_activitateEnrol) <= 1))) {

                                        poate = false;
                                    }
                                }
                            }
                        }

                        if (poate) {
                            MyConnection.inrolareUser(user, cursDorit.getId());
                        }else {
                            JOptionPane.showMessageDialog(null, "NU SE POATE ADAUGA");
                        }

                    }
                    else if (user.getRol() == Rol.PROFESOR) {

                        ActivitateDidacticaModel activitateDorita = activitati.get(index);
                        boolean poate = true;

                        ArrayList<ActivitateDidacticaModel> activitatiParticipante = MyConnection.getAllActivitatiDidacticeFromUser(user);

                        DateModel dataIncepere_activitateEnrol = activitateDorita.getDataIncepere();
                        DateModel dataTerminare_activitateEnrol = activitateDorita.getDataIncepere().adunare(activitateDorita.getDurataActivitate());

                        assert activitatiParticipante != null;
                        for (ActivitateDidacticaModel ap : activitatiParticipante) {

                            DateModel dataInepere_activitateParticipanta = ap.getDataIncepere();
                            DateModel dataTerminare_activitateParticipanta = ap.getDataIncepere().adunare(ap.getDurataActivitate());

                            if (comparareZile(dataIncepere_activitateEnrol.getZi() % 7,
                                    dataTerminare_activitateEnrol.getZi() % 7,
                                    dataInepere_activitateParticipanta.getZi() % 7,
                                    dataTerminare_activitateParticipanta.getZi() % 7)) {

                                if (!(dataTerminare_activitateEnrol.comparare(dataInepere_activitateParticipanta) <= 1)
                                        && !(dataTerminare_activitateParticipanta.comparare(dataIncepere_activitateEnrol) <= 1)) {

                                    poate = false;
                                }
                            }
                        }

                        if (poate) {
                            MyConnection.inrolareUser(user, activitateDorita.getId());
                        } else {
                            JOptionPane.showMessageDialog(null, "NU SE POATE ADAUGA LA CURS");
                        }
                    }

                }
                else if (fereastra == 3) {
                        dispose();
                        new SetareFormula(model, cursuri.get(listResults.getSelectedIndex()));
                    }
                else if (fereastra == 4) {
                        dispose();
                        int index = listResults.getSelectedIndex();
                        new VizualizareDate(users.get(index), fereastra, model);
                    }
                else if (fereastra == 5) {
                    int index = listResults.getSelectedIndex();
                    CursModel cursSelectat = cursuri.get(index);

                    dispose();
                    new VizualizareNote(model, cursSelectat);
                }
                else if (fereastra == 6) {
                    int index = listResults.getSelectedIndex();
                    CursModel cursSelectat = cursuri.get(index);

                    List<ActivitateDidacticaModel> activitatiSelectate = MyConnection.getAllActivitatiDidacticeByIdMaterie(cursSelectat.getId());

                    assert activitatiSelectate != null;
                    if (activitatiSelectate.size() == 3) {
                        JOptionPane.showMessageDialog(null, "Exista deja toate activitatile didactice pentru aceasta materie !");
                    }else {
                        dispose();
                        new AdaugareActivitati(model, 1, cursSelectat, activitatiSelectate, null);
                    }
                }
                else if (fereastra == 7) {
                    dispose();
                    int index = listResults.getSelectedIndex();
                    new PrivatChat(model, contacts.get(index));
                }
                else if (fereastra == 8) {
                    int index = listResults.getSelectedIndex();
                    CursModel curs = cursuri.get(index);

                    MyConnection.renuntareStudent(model, curs);
                }
            }
        });

        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Searching " + txtInput.getText() + " ...");

                String searchText = txtInput.getText();

                if (fereastra == 1) {

                    int numOfRes = activitati.size();

                    String[] results = new String[numOfRes];
                    int cont = 0;

                    for (ActivitateDidacticaModel activitateDidacticaModel : activitati) {
                        if (activitateDidacticaModel.getNume().contains(searchText)) {
                            results[cont++] = activitateDidacticaModel.getNume() + " predat de " + activitateDidacticaModel.getNumeProf();
                        } else if (activitateDidacticaModel.getNumeProf().contains(searchText)) {
                            results[cont++] = activitateDidacticaModel.getNume() + " predat de " + activitateDidacticaModel.getNumeProf();
                        }
                    }
                    listResults.setListData(results);
                }
                else if (fereastra == -1) {

                    int numOfRes = studenti.size();
                    String[] results = new String[numOfRes];
                    int cont = 0;

                    for (StudentModel studentModel : studenti) {
                        if (studentModel.getNume().contains(searchText)) {
                            results[cont++] = studentModel.getNume() + " " + studentModel.getPrenume();
                        } else if (studentModel.getPrenume().contains(searchText)) {
                            results[cont++] = studentModel.getNume() + " " + studentModel.getPrenume();
                        } else if (searchText.contains(studentModel.getNume())) {
                            results[cont++] = studentModel.getNume() + " " + studentModel.getPrenume();
                        } else if (searchText.contains(studentModel.getPrenume())) {
                            results[cont++] = studentModel.getNume() + " " + studentModel.getPrenume();
                        }
                    }
                    listResults.setListData(results);
                }
                else if (fereastra == -2) {

                    if (user.getRol() == Rol.PROFESOR) {

                        int numOfRes = activitati.size();
                        String[] results = new String[numOfRes];
                        int cont = 0;

                        for (ActivitateDidacticaModel activitateDidacticaModel : activitati) {
                            if (activitateDidacticaModel.getNume().contains(searchText)) {
                                results[cont++] = activitateDidacticaModel.getNume();
                            } else if (searchText.contains(activitateDidacticaModel.getNume())) {
                                results[cont++] = activitateDidacticaModel.getNume();
                            }
                        }
                        listResults.setListData(results);
                    }
                    else if (user.getRol() == Rol.STUDENT) {
                        int numOfRes = cursuri.size();
                        String[] results = new String[numOfRes];
                        int cont = 0;

                        if (model.getRol() == Rol.ADMIN) {

                            for (CursModel cursModel : cursuri) {
                                if (cursModel.getNume().contains(searchText)) {
                                    results[cont++] = cursModel.getNume() + " titular fiind, " + cursModel.getTitular().getNume() + " " + cursModel.getTitular().getPrenume();
                                } else if (searchText.contains(cursModel.getNume())) {
                                    results[cont++] = cursModel.getNume() + " titular fiind, " + cursModel.getTitular().getNume() + " " + cursModel.getTitular().getPrenume();
                                } else if (searchText.contains(cursModel.getTitular().getNume())) {
                                    results[cont++] = cursModel.getNume() + " titular fiind, " + cursModel.getTitular().getNume() + " " + cursModel.getTitular().getPrenume();
                                } else if (searchText.contains(cursModel.getTitular().getPrenume())) {
                                    results[cont++] = cursModel.getNume() + " titular fiind, " + cursModel.getTitular().getNume() + " " + cursModel.getTitular().getPrenume();
                                } else if (cursModel.getTitular().getPrenume().contains(searchText)) {
                                    results[cont++] = cursModel.getNume() + " titular fiind, " + cursModel.getTitular().getNume() + " " + cursModel.getTitular().getPrenume();
                                } else if (cursModel.getTitular().getNume().contains(searchText)) {
                                    results[cont++] = cursModel.getNume() + " titular fiind, " + cursModel.getTitular().getNume() + " " + cursModel.getTitular().getPrenume();
                                }
                            }
                        }
                        listResults.setListData(results);
                    }
                }
                else if (fereastra == 2) {
                    int numOfRes = users.size();
                    String[] results = new String[numOfRes];
                    int cont = 0;

                    for (UserModel userModel : users) {
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
                else if (fereastra == 3) {
                    int numOfRes = cursuri.size();
                    String[] results = new String[numOfRes];
                    int cont = 0;

                    if (model.getRol() == Rol.ADMIN || model.getRol() == Rol.SUPER_ADMIN) {

                        for (CursModel cursModel : cursuri) {
                            if (cursModel.getNume().contains(searchText)) {
                                results[cont++] = cursModel.getNume() + " titular fiind, " + cursModel.getTitular().getNume() + " " + cursModel.getTitular().getPrenume();
                            } else if (searchText.contains(cursModel.getNume())) {
                                results[cont++] = cursModel.getNume() + " titular fiind, " + cursModel.getTitular().getNume() + " " + cursModel.getTitular().getPrenume();
                            } else if (searchText.contains(cursModel.getTitular().getNume())) {
                                results[cont++] = cursModel.getNume() + " titular fiind, " + cursModel.getTitular().getNume() + " " + cursModel.getTitular().getPrenume();
                            } else if (searchText.contains(cursModel.getTitular().getPrenume())) {
                                results[cont++] = cursModel.getNume() + " titular fiind, " + cursModel.getTitular().getNume() + " " + cursModel.getTitular().getPrenume();
                            } else if (cursModel.getTitular().getPrenume().contains(searchText)) {
                                results[cont++] = cursModel.getNume() + " titular fiind, " + cursModel.getTitular().getNume() + " " + cursModel.getTitular().getPrenume();
                            } else if (cursModel.getTitular().getNume().contains(searchText)) {
                                results[cont++] = cursModel.getNume() + " titular fiind, " + cursModel.getTitular().getNume() + " " + cursModel.getTitular().getPrenume();
                            }
                        }
                    }
                    else if (model.getRol() == Rol.PROFESOR) {
                        for (CursModel cursModel : cursuri) {
                            if (cursModel.getNume().contains(searchText)) {
                                results[cont++] = cursModel.getNume();
                            } else if (searchText.contains(cursModel.getNume())) {
                                results[cont++] = cursModel.getNume();
                            }
                        }
                    }
                    listResults.setListData(results);
                }
                else if (fereastra == 4) {
                    int numOfRes = users.size();
                    String[] results = new String[numOfRes];
                    int cont = 0;

                    for (UserModel userModel : users) {
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
                else if (fereastra == 5) {
                    int numOfRes = cursuri.size();
                    String[] results = new String[numOfRes];
                    int cont = 0;

                    if (model.getRol() == Rol.ADMIN || model.getRol() == Rol.SUPER_ADMIN) {

                        for (CursModel cursModel : cursuri) {
                            if (cursModel.getNume().contains(searchText)) {
                                results[cont++] = cursModel.getNume() + " titular fiind, " + cursModel.getTitular().getNume() + " " + cursModel.getTitular().getPrenume();
                            } else if (searchText.contains(cursModel.getNume())) {
                                results[cont++] = cursModel.getNume() + " titular fiind, " + cursModel.getTitular().getNume() + " " + cursModel.getTitular().getPrenume();
                            } else if (searchText.contains(cursModel.getTitular().getNume())) {
                                results[cont++] = cursModel.getNume() + " titular fiind, " + cursModel.getTitular().getNume() + " " + cursModel.getTitular().getPrenume();
                            } else if (searchText.contains(cursModel.getTitular().getPrenume())) {
                                results[cont++] = cursModel.getNume() + " titular fiind, " + cursModel.getTitular().getNume() + " " + cursModel.getTitular().getPrenume();
                            } else if (cursModel.getTitular().getPrenume().contains(searchText)) {
                                results[cont++] = cursModel.getNume() + " titular fiind, " + cursModel.getTitular().getNume() + " " + cursModel.getTitular().getPrenume();
                            } else if (cursModel.getTitular().getNume().contains(searchText)) {
                                results[cont++] = cursModel.getNume() + " titular fiind, " + cursModel.getTitular().getNume() + " " + cursModel.getTitular().getPrenume();
                            }
                        }
                    }
                    listResults.setListData(results);
                }
                else if (fereastra == 6) {
                    int numOfRes = cursuri.size();
                    String[] results = new String[numOfRes];
                    int cont = 0;

                    if (model.getRol() == Rol.ADMIN || model.getRol() == Rol.SUPER_ADMIN) {

                        for (CursModel cursModel : cursuri) {
                            if (cursModel.getNume().contains(searchText)) {
                                results[cont++] = cursModel.getNume() + " titular fiind, " + cursModel.getTitular().getNume() + " " + cursModel.getTitular().getPrenume();
                            } else if (searchText.contains(cursModel.getNume())) {
                                results[cont++] = cursModel.getNume() + " titular fiind, " + cursModel.getTitular().getNume() + " " + cursModel.getTitular().getPrenume();
                            } else if (searchText.contains(cursModel.getTitular().getNume())) {
                                results[cont++] = cursModel.getNume() + " titular fiind, " + cursModel.getTitular().getNume() + " " + cursModel.getTitular().getPrenume();
                            } else if (searchText.contains(cursModel.getTitular().getPrenume())) {
                                results[cont++] = cursModel.getNume() + " titular fiind, " + cursModel.getTitular().getNume() + " " + cursModel.getTitular().getPrenume();
                            } else if (cursModel.getTitular().getPrenume().contains(searchText)) {
                                results[cont++] = cursModel.getNume() + " titular fiind, " + cursModel.getTitular().getNume() + " " + cursModel.getTitular().getPrenume();
                            } else if (cursModel.getTitular().getNume().contains(searchText)) {
                                results[cont++] = cursModel.getNume() + " titular fiind, " + cursModel.getTitular().getNume() + " " + cursModel.getTitular().getPrenume();
                            }
                        }
                    } else if (model.getRol() == Rol.PROFESOR) {
                        for (CursModel cursModel : cursuri) {
                            if (cursModel.getNume().contains(searchText)) {
                                results[cont++] = cursModel.getNume();
                            } else if (searchText.contains(cursModel.getNume())) {
                                results[cont++] = cursModel.getNume();
                            }
                        }
                    }
                    listResults.setListData(results);
                }
                else if (fereastra == 7) {
                    int numOfRes = contacts.size();
                    String[] results = new String[numOfRes];
                    int cont = 0;

                    for (ConversatieModel contact : contacts) {
                        if (contact.getClass() == UserModel.class) {
                            if (((UserModel) contact).getNume().contains(searchText)) {
                                results[cont++] = ((UserModel) contact).getNume() + " " + ((UserModel) contact).getPrenume();
                            } else if (((UserModel) contact).getPrenume().contains(searchText)) {
                                results[cont++] = ((UserModel) contact).getNume() + " " + ((UserModel) contact).getPrenume();
                            } else if (searchText.contains(((UserModel) contact).getNume())) {
                                results[cont++] = ((UserModel) contact).getNume() + " " + ((UserModel) contact).getPrenume();
                            } else if (searchText.contains(((UserModel) contact).getPrenume())) {
                                results[cont++] = ((UserModel) contact).getNume() + " " + ((UserModel) contact).getPrenume();
                            }
                        } else if (contact.getClass() == GrupModel.class) {
                            if (((GrupModel) contact).getDenumire().contains(searchText)) {
                                results[cont++] = ((GrupModel) contact).getDenumire();
                            } else if (searchText.contains(((GrupModel) contact).getDenumire())) {
                                results[cont++] = ((GrupModel) contact).getDenumire();
                            }
                        }
                    }
                    listResults.setListData(results);
                }
                else if (fereastra == 8) {
                    int numOfRes = cursuri.size();
                    String[] results = new String[numOfRes];
                    int cont = 0;

                    for (CursModel cursModel : cursuri) {
                        if (cursModel.getNume().contains(searchText)) {
                            results[cont++] = cursModel.getNume() + " titular fiind, " + cursModel.getTitular().getNume() + " " + cursModel.getTitular().getPrenume();
                        } else if (searchText.contains(cursModel.getNume())) {
                            results[cont++] = cursModel.getNume() + " titular fiind, " + cursModel.getTitular().getNume() + " " + cursModel.getTitular().getPrenume();
                        } else if (searchText.contains(cursModel.getTitular().getNume())) {
                            results[cont++] = cursModel.getNume() + " titular fiind, " + cursModel.getTitular().getNume() + " " + cursModel.getTitular().getPrenume();
                        } else if (searchText.contains(cursModel.getTitular().getPrenume())) {
                            results[cont++] = cursModel.getNume() + " titular fiind, " + cursModel.getTitular().getNume() + " " + cursModel.getTitular().getPrenume();
                        } else if (cursModel.getTitular().getPrenume().contains(searchText)) {
                            results[cont++] = cursModel.getNume() + " titular fiind, " + cursModel.getTitular().getNume() + " " + cursModel.getTitular().getPrenume();
                        } else if (cursModel.getTitular().getNume().contains(searchText)) {
                            results[cont++] = cursModel.getNume() + " titular fiind, " + cursModel.getTitular().getNume() + " " + cursModel.getTitular().getPrenume();
                        }
                    }

                    listResults.setListData(results);
                }
            }
        });

        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                dispose();
                if (fereastra == -1)
                    new VizualizareCursuri(model, 1, null, null);
                else if (fereastra == -2) {
                    new VizualizareCursuri(model, 2, null, null);
                }else if (fereastra == 8) {
                    new VizualizareCursuri(model, 1, null, null);
                }
                else
                    new HomePage(model);
            }
        });

        btnAfis.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();

                int index = listResults.getSelectedIndex();

                new VizualizareCursuri(model, -1, activitati.get(index), null);
            }
        });

        btnRenuntare.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fereastra == 1) {
                    if (model.getRol() == Rol.STUDENT) {
                        dispose();
                        new VizualizareCursuri(model, 8, null, null);
                    }
                }
            }
        });

        btnDescarcare.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fereastra == 1) {
                    MyConnection.descarcareLista(activitati);
                }
            }
        });
    }

    private void fillList(int fereastra) {

        comboFilter.addItem("NONE");
        comboFilter.addItem("Student");
        comboFilter.addItem("Profesor");

        if (fereastra == 4) {
            comboFilter.addItem("Admin");
            comboFilter.addItem("Super admin");
        }

    }

    private boolean comparareZile(int ziIncepEnrol, int ziTermEnrol, int ziIncepPart, int ziTermPart) {

        if (ziIncepEnrol == ziIncepPart) {
            return true;
        }
        if (ziTermEnrol == ziIncepPart) {
            return true;
        }
        return ziTermPart == ziIncepEnrol;
    }

    private static String setTitlu(int fereastra) {

        if (fereastra == 1) {
            return "Vizualizare Activitati didactice";
        }else if (fereastra == -1) {
            return "Vizualizare Studenti";
        }else if (fereastra == 2) {
            return "Vizualizare Cursuri";
        }else if (fereastra == 4) {
            return "Vizualizare Utilizatori";
        }else if (fereastra == 3) {
            return "Setare formula";
        }else if (fereastra == 5) {
            return "Creare activitati";
        }else if (fereastra == 6) {
            return "Vizualizare note";
        }else if (fereastra == 7) {
            return "Chat";
        }else if (fereastra == 8)
            return "Renuntare curs";
        return "";
    }

    private void setLabelSearch(UserModel model, int fereastra, UserModel userModel) {

        if (fereastra == 1) {
            lblName.setText("Nume activitate didactica: ");
            lblTitle.setText("Vizualizare activitati didactice");
        }else if (fereastra == 4) {
            lblName.setText("Nume user: ");
            lblTitle.setText("Vizualizare utilizatori");
        }
        else if (fereastra == -1) {
            lblName.setText("Nume student: ");
            lblTitle.setText("Vizualizare studenti");
        }
        else if (fereastra == 2) {
            if (model.getRol() == Rol.STUDENT) {
                lblName.setText("Nume curs: ");
                lblTitle.setText("Inrolare la curs");
            }
            else {
                lblName.setText("Nume user: ");
                lblTitle.setText("Inrolare la activitati");
            }
        }
        else if (fereastra == -2) {
            if (userModel.getRol() == Rol.STUDENT) {
                lblName.setText("Nume curs: ");
                lblTitle.setText("Inrolare la curs");
            }else {
                lblName.setText("Nume activitate didactica: ");
                lblTitle.setText("Inrolare la activitate didactica");
            }
        }
        else if (fereastra == 3) {
            lblName.setText("Nume curs: ");
            lblTitle.setText("Vizualizare cursuri pentru setarea formulei");
        }
        else if (fereastra == 7) {
            lblName.setText("Nume user: ");
            lblTitle.setText("Vizualizare utilizatori pentru chat");
        }
        else if (fereastra == 5) {
            lblName.setText("Nume curs: ");
            lblTitle.setText("Vizualizare cursuri pentru vizualizarea notelor");
        }
        else if (fereastra == 6) {
            lblName.setText("Nume curs: ");
            lblTitle.setText("Vizualizare cursuri pentru crearea unei activitati");
        }
        else if (fereastra == 8) {
            lblName.setText("Nume curs: ");
            lblTitle.setText("Vizualizare cursuri pentru renuntare");
        }

    }

    private String[] setList(UserModel model, int fereastra, ActivitateDidacticaModel activ, UserModel user) {
        if (fereastra == 1) {
            if (model.getRol() == Rol.STUDENT) {
                activitati = MyConnection.getAllActivitatiDidacticeFromUser(model);

                int numOfActivities = 0;
                if (activitati != null)
                    numOfActivities = activitati.size();

                int cont = 0;
                String[] results = new String[numOfActivities];


                assert activitati != null;
                for (ActivitateDidacticaModel a : activitati) {
                    results[cont++] = a.getNume() + " predat de " + a.getNumeProf();
                }

                return results;
            }
            else if (model.getRol() == Rol.PROFESOR) {
                activitati = MyConnection.getAllActivitatiDidacticeFromUser(model);

                int numOfActivities = 0;
                if (activitati != null)
                    numOfActivities = activitati.size();

                int cont = 0;
                String[] results = new String[numOfActivities];


                assert activitati != null;
                for (ActivitateDidacticaModel a : activitati) {
                    results[cont++] = a.getNume();
                }
                return results;
            }
            else {
                activitati = MyConnection.getAllActivitatiDidactice();

                int numOfActivities = 0;
                if (activitati != null)
                    numOfActivities = activitati.size();

                int cont = 0;
                String[] results = new String[numOfActivities];


                assert activitati != null;
                for (ActivitateDidacticaModel a : activitati) {
                    if (!a.getNumeProf().equals("null null"))
                        results[cont++] = a.getNume() + " predat de " + a.getNumeProf();
                    else
                        results[cont++] = a.getNume() + " - neasignat la un profesor";
                }
                return results;
            }
        }
        else if (fereastra == -1) {
            studenti = MyConnection.getStudentByActivitateDidactica(activ.getId());
            int numOfStud = 0;
            if (studenti != null) {
                numOfStud = studenti.size();
            }

            int cont = 0;
            String[] results = new String[numOfStud];

            assert studenti != null;
            for (StudentModel s: studenti) {
                results[cont++] = s.getNume() + " " + s.getPrenume();
            }
            return results;
        }
        else if (fereastra == -2) {

            if (user.getRol() == Rol.STUDENT) {

                cursuri = MyConnection.getAllMaterii(model);

                int numOfCursuri = 0;
                if (cursuri != null) {
                    numOfCursuri = cursuri.size();
                }

                int cont = 0;
                String[] results = new String[numOfCursuri];

                assert cursuri != null;
                for (CursModel c: cursuri) {
                    results[cont++] = c.getNume() + " titular fiind, " + c.getTitular().getNume() + " " + c.getTitular().getPrenume();
                }
                return results;
            }
            else if (user.getRol() == Rol.PROFESOR) {
                activitati = MyConnection.getAllActivitatiDidactice();

                int numOfActivities = 0;
                if (activitati != null)
                    numOfActivities = activitati.size();

                int cont = 0;
                String[] results = new String[numOfActivities];


                assert activitati != null;
                for (ActivitateDidacticaModel a : activitati) {
                    if (!a.getNumeProf().equals("null null"))
                        results[cont++] = a.getNume() + " predat de " + a.getNumeProf();
                    else
                        results[cont++] = a.getNume() + " - neasignat la un profesor";
                }
                return results;

            }
        }
        else if (fereastra == 2) {

            if (model.getRol() == Rol.STUDENT) {

                ArrayList<CursModel> cursuriEnrol = MyConnection.getAllMateriiForEnrol(model);
                ArrayList<CursModel> cursuriPos = MyConnection.getAllMaterii(model);

                assert cursuriPos != null;
                for (CursModel c: cursuriPos) {
                    assert cursuriEnrol != null;
                    for (CursModel c2: cursuriEnrol) {
                        if (c2.getNume().equals(c.getNume())) {
                            cursuriEnrol.remove(c);
                        }
                    }
                }

                int numOfCursuri = 0;
                if (cursuriEnrol != null) {
                    numOfCursuri = cursuriEnrol.size();
                }

                int cont = 0;
                String[] results = new String[numOfCursuri];

                for (int i = 0; i < numOfCursuri - 1; i++){
                    if (!cursuriEnrol.get(i).getNume().equals(cursuriEnrol.get(i + 1).getNume())) {
                        results[cont++] = cursuriEnrol.get(i).getNume();
                        cursuri.add(cursuriEnrol.get(i));
                    }
                }

                assert cursuriEnrol != null;
                results[cont] = cursuriEnrol.get(numOfCursuri - 1).getNume();
                cursuri.add(cursuriEnrol.get(numOfCursuri - 1));

                return results;

            }
            else {

                users = MyConnection.getUsersByTip(Rol.PROFESOR);
                assert users != null;
                users.addAll(MyConnection.getUsersByTip(Rol.STUDENT));
                int numOfUsers = 0;
                if (users != null) {
                    numOfUsers = users.size();
                }

                int cont = 0;
                String[] results = new String[numOfUsers];

                assert users != null;
                for (UserModel u : users) {
                    results[cont++] = u.getNume() + " " + u.getPrenume();
                }

                return results;
            }
        }
        else if (fereastra == 3) {
            cursuri = MyConnection.getAllMaterii(model);

            int numOfCursuri = 0;
            if (cursuri != null) {
                numOfCursuri = cursuri.size();
            }

            int cont = 0;
            String[] results = new String[numOfCursuri];

            if (model.getRol() == Rol.ADMIN || model.getRol() == Rol.SUPER_ADMIN) {
                for (CursModel c: cursuri) {
                    results[cont++] = c.getNume() + " titular fiind, " + c.getTitular().getNume() + " " + c.getTitular().getPrenume();
                }
            }
            else if (model.getRol() == Rol.PROFESOR) {
                for (CursModel c: cursuri) {
                    results[cont++] = c.getNume();                }
            }

            return results;

        }
        else if (fereastra == 4) {
            users = MyConnection.getAllFromUser();

            int numOfUsers = 0;
            if (users != null) {
                numOfUsers = users.size();
            }

            int cont = 0;
            String[] results = new String[numOfUsers];

            assert users != null;
            for (UserModel u: users) {
                results[cont++] = u.getNume() + " " + u.getPrenume();
            }

            return results;
        }
        else if (fereastra == 5) {
            if (model.getRol() == Rol.STUDENT) {
                cursuri = MyConnection.getAllMaterii(model);

                int numOfCursuri = 0;
                if (cursuri != null) {
                    numOfCursuri = cursuri.size();
                }

                int cont = 0;
                String[] results = new String[numOfCursuri];

                assert cursuri != null;
                for (CursModel c : cursuri) {
                    results[cont++] = c.getNume() + " titular fiind, " + c.getTitular().getNume() + " " + c.getTitular().getPrenume();
                }

                return results;
            }
        }
        else if (fereastra == 6) {
            if (model.getRol() == Rol.ADMIN || model.getRol() == Rol.SUPER_ADMIN) {
                cursuri = MyConnection.getAllMaterii();

                int numOfCursuri = 0;
                if (cursuri != null) {
                    numOfCursuri = cursuri.size();
                }

                int cont = 0;
                String[] results = new String[numOfCursuri];

                assert cursuri != null;
                for (CursModel c: cursuri) {
                    results[cont++] = c.getNume() + " titular fiind, " + c.getTitular().getNume() + " " + c.getTitular().getPrenume();
                }
                return results;
            } else if (model.getRol() == Rol.PROFESOR) {
                cursuri = MyConnection.getAllMaterii(model);

                int numOfCursuri = 0;
                if (cursuri != null) {
                    numOfCursuri = cursuri.size();
                }

                int cont = 0;
                String[] results = new String[numOfCursuri];
                assert cursuri != null;
                for (CursModel c: cursuri) {
                    results[cont++] = c.getNume();                }
                return results;
            }
        }
        else if (fereastra == 7) {

            contacts.addAll(MyConnection.getAllFromUser());

            if (model.getRol() == Rol.STUDENT || model.getRol() == Rol.PROFESOR){
                contacts.addAll(MyConnection.getGrupByUser(model));
            }



            int numOfContacts = 0;
            if (contacts != null) {
                numOfContacts = contacts.size();
            }

            int cont = 0;
            String[] results = new String[numOfContacts];

            assert contacts != null;
            for (ConversatieModel c: contacts) {
                if (c.getClass() == UserModel.class) {
                    results[cont++] = ((UserModel) c).getNume() + " " + ((UserModel) c).getPrenume();
                }else {
                    results[cont++] = ((GrupModel) c).getDenumire();
                }
            }

            return results;
        }
        else if (fereastra == 8) {
            cursuri = MyConnection.getAllMaterii(model);

            int numOfCursuri = 0;
            if (cursuri != null) {
                numOfCursuri = cursuri.size();
            }

            int cont = 0;
            String[] results = new String[numOfCursuri];

            assert cursuri != null;
            for (CursModel c: cursuri) {
                results[cont++] = c.getNume() + " titular fiind, " + c.getTitular().getNume() + " " + c.getTitular().getPrenume();
            }

            return results;

        }
        return new String[]{""};
    }

    private void setDataVisible(UserModel model, int fereastra) {

        comboFilter.setVisible(false);
        btnAfis.setVisible(false);
        btnRenuntare.setVisible(true);
        btnDescarcare.setVisible(false);

        if (fereastra == 4 || fereastra == 2) {
            comboFilter.setVisible(true);
        }
        if (fereastra == 1) {
            if (model.getRol() != Rol.STUDENT) {
                btnAfis.setVisible(true);
                btnRenuntare.setVisible(false);
            }
            if (model.getRol() == Rol.STUDENT || model.getRol() == Rol.PROFESOR){
                btnDescarcare.setVisible(true);
            }
        }
        else {
            btnRenuntare.setVisible(false);
        }

    }

}
