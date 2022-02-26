package GuiPackage;

import ConnectionPackage.MyConnection;
import ConnectionPackage.Validator;
import MainPackage.Conversie;
import ModelsPackage.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AdaugareActivitati extends JFrame{
    private JLabel lblAdaugareUtilizator;
    private JTextField txtDenumire;
    private JComboBox<String> comboTip;
    private JButton btnBack;
    private JPanel mainPanel;
    private JButton btnSubmit;
    private JLabel lblActivitate;
    private JLabel lblDataIncepere;
    private JTextField txtDataIncepere;
    private JLabel lblDataFinalizare;
    private JLabel lblNrMax;
    private JTextField txtNrMax;
    private JSpinner sprDataIncepereLuna;
    private JSpinner sprDataIncepereAn;
    private JSpinner sprDataIncepereZi;
    private JPanel dataInceperePanel;
    private JPanel dataTerminarePanel;
    private JSpinner sprDataTerminareLuna;
    private JSpinner sprDataTerminareAn;
    private JSpinner sprDataTerminareZi;
    private JSpinner sprIncepereOra;
    private JSpinner sprIncepereMinute;
    private JSpinner sprTerminareOra;
    private JSpinner sprTerminareMinute;
    private JLabel lblZiI;
    private JLabel lblLunaI;
    private JLabel lblLunaT;
    private JLabel lblZiT;
    private JLabel lblAnI;
    private JLabel lblAnT;
    private JLabel lblOreI;
    private JLabel lblOreT;
    private JLabel lblMinuteI;
    private JLabel lblMinuteT;
    private JLabel lblDenumire;
    private JLabel lblDurata;
    private JTextField txtDurata;

    private static final String[] tipActivitate = new String[] {"CURS", "SEMINAR", "LABORATOR"};

    public AdaugareActivitati(UserModel model, int fereastra,
                              CursModel cursModel,
                              List<ActivitateDidacticaModel> activitati,
                              GrupModel toSend){
        setContentPane(mainPanel);
        setVisible(true);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 550);
        setLocation(450, 100);

        setSpinner();
        if (fereastra == 1) {
            setList(activitati);
        }
        else {
            comboTip.setVisible(false);
            lblActivitate.setVisible(false);
            lblAdaugareUtilizator.setText("Creare intalnire");
        }


        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                if (fereastra == 1) {
                    new VizualizareCursuri(model, 6, null, null);
                }else if (fereastra == 2) {
                    new PrivatChat(model, toSend);
                }
            }
        });
        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fereastra == 1) {
                    try {
                        int nrMaxim = Integer.parseInt(txtNrMax.getText());
                        int durata = Integer.parseInt(txtDurata.getText());

                        ActivitateDidacticaModel activitate = new ActivitateDidacticaModel(
                                0,
                                Conversie.convStringToActivitateDidactica((String) comboTip.getSelectedItem()),
                                txtDenumire.getText(),
                                new DateModel((Integer) sprDataIncepereAn.getValue(), (Integer) sprDataIncepereLuna.getValue(), (Integer) sprDataIncepereZi.getValue(), (Integer) sprIncepereOra.getValue(), (Integer) sprIncepereMinute.getValue()),
                                new DateModel((Integer) sprDataTerminareAn.getValue(), (Integer) sprDataTerminareLuna.getValue(), (Integer) sprDataTerminareZi.getValue(), (Integer) sprTerminareOra.getValue(), (Integer) sprTerminareMinute.getValue()),
                                nrMaxim,
                                durata,
                                ""
                        );

                        String errorMsg = Validator.validareActivitate(activitate);

                        if (!errorMsg.equals("")) {
                            JOptionPane.showMessageDialog(null, errorMsg);
                        } else {
                            MyConnection.saveActivitate(activitate, cursModel);
                            dispose();
                            new VizualizareCursuri(model, 6, null, null);
                        }

                    } catch (Exception exception) {
                        JOptionPane.showMessageDialog(null, "Date invalide !");
                    }

                }
                else if (fereastra == 2) {
                    //CREARE MEET

                    try {
                        int nrMaxim = Integer.parseInt(txtNrMax.getText());
                        int durata = Integer.parseInt(txtDurata.getText());

                        IntalnireModel intalnire = new IntalnireModel(
                                txtDenumire.getText(),
                                nrMaxim,
                                durata,
                                new DateModel((Integer) sprDataIncepereAn.getValue(), (Integer) sprDataIncepereLuna.getValue(), (Integer) sprDataIncepereZi.getValue(), (Integer) sprIncepereOra.getValue(), (Integer) sprIncepereMinute.getValue()),
                                new DateModel((Integer) sprDataTerminareAn.getValue(), (Integer) sprDataTerminareLuna.getValue(), (Integer) sprDataTerminareZi.getValue(), (Integer) sprTerminareOra.getValue(), (Integer) sprTerminareMinute.getValue()),
                                model,
                                toSend
                        );

                        String errorMsg = Validator.validareIntalnire(intalnire);
                        if (!errorMsg.equals("")) {
                            JOptionPane.showMessageDialog(null, errorMsg);
                            return;
                        }

                        //System.out.println(intalnire.toString());

                        MyConnection.saveIntalnire(intalnire);
                        MyConnection.saveStudentIntalnire(model, intalnire);

                        dispose();
                        new PrivatChat(model, toSend);

                    } catch (Exception exception) {
                        JOptionPane.showMessageDialog(null, "Date invalide !");
                    }
                }
            }
        });
        sprDataIncepereLuna.addChangeListener(e -> {
            int numZile = findZiMaxima(sprDataIncepereLuna.getValue());

            SpinnerModel spinnerModel = new SpinnerNumberModel(1, 1, numZile, 1);

            sprDataIncepereZi.setModel(spinnerModel);
        });
        sprDataTerminareLuna.addChangeListener(e -> {
            int numZile = findZiMaxima(sprDataTerminareLuna.getValue());

            SpinnerModel spinnerModel = new SpinnerNumberModel(1, 1, numZile, 1);

            sprDataTerminareZi.setModel(spinnerModel);
        });

        if (fereastra == 2) {
            setSugestii(model, toSend);
        }
    }

    private void setSugestii(UserModel model, GrupModel grup) {

        ArrayList<UserModel> users = MyConnection.getAllUsersFromGrup(grup);

        ArrayList<DateModel> dateModels = new ArrayList<>();

        assert users != null;
        for (UserModel u: users) {
            dateModels.addAll(MyConnection.getDateFromUser(model));
        }

        int[] ani = new int[]{0,0,0};
        int[] luni = new int[13];
        for (int i = 0; i < 13; i++){
            luni[i] = 0;
        }
        int[] zile = new int[32];
        for (int i = 0; i < 32; i++){
            zile[i] = 0;
        }
        int[] ore = new int[25];
        for (int i = 0; i < 25; i++){
            ore[i] = 0;
        }

        for (DateModel d: dateModels) {
            ani[d.getAn() % 10] = 1;
            luni[d.getLuna()] = 1;
            zile[d.getZi()] = 1;
            ore[d.getOra()] = 1;
        }

        ArrayList<Integer> maxZileLuna = new ArrayList<>();
        maxZileLuna.add(31);
        maxZileLuna.add(28);
        maxZileLuna.add(31);
        maxZileLuna.add(30);
        maxZileLuna.add(31);
        maxZileLuna.add(30);
        maxZileLuna.add(31);
        maxZileLuna.add(31);
        maxZileLuna.add(30);
        maxZileLuna.add(31);
        maxZileLuna.add(30);
        maxZileLuna.add(31);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        DateModel now = Conversie.convStringToDateModel(dtf.format(LocalDateTime.now()));

        StringBuilder dates = new StringBuilder();

        int numOfSuggestions = 3;

        int cont = 0;
        while (cont < numOfSuggestions) {

            for (int i = 1; i <= 2; i++) {

                for (int j = 1; j <= 12; j++) {

                    for (int k = 1; k <= maxZileLuna.get(j - 1); k++) {

                        for (int o = 8; o <= 20; o++) {

                            if (ani[i] + luni[j] + zile[k] + ore[o] != 4) {

                                DateModel data = new DateModel(2020 + i, j, k, o, 0);

                                if (data.comparareTot(now) == 2) {

                                    dates.append("\n").append(data.toString());

                                    //System.out.println(data.toString());
                                    cont++;
                                }
                                if (cont == numOfSuggestions)
                                    break;
                            }

                        }
                        if (cont == numOfSuggestions)
                            break;

                    }
                    if (cont == numOfSuggestions)
                        break;

                }
                if (cont == numOfSuggestions)
                    break;

            }
        }

        JOptionPane.showMessageDialog(null, "Sugestii de date pentru inceperea intalnirii: " + dates);

    }

    private int findZiMaxima(Object value) {

        if (value.equals(2))
            return 28;

        if (value.equals(1) || value.equals(3)
                || value.equals(5) || value.equals(7)
                || value.equals(8) || value.equals(10)
                || value.equals(12)) {
            return 31;
        }
        return 30;
    }

    private void setSpinner() {
        SpinnerModel spinnerModelLunaI = new SpinnerNumberModel(1, 1, 12, 1);
        SpinnerModel spinnerModelLunaT = new SpinnerNumberModel(1, 1, 12, 1);
        sprDataIncepereLuna.setModel(spinnerModelLunaI);
        sprDataTerminareLuna.setModel(spinnerModelLunaT);

        SpinnerModel spinnerModelAnI = new SpinnerNumberModel(2022, 2022, 2080, 1);
        SpinnerModel spinnerModelAnT = new SpinnerNumberModel(2022, 2022, 2080, 1);
        sprDataIncepereAn.setModel(spinnerModelAnI);
        sprDataTerminareAn.setModel(spinnerModelAnT);

        SpinnerModel spinnerModelOreI = new SpinnerNumberModel(1, 0, 23, 1);
        SpinnerModel spinnerModelOreT = new SpinnerNumberModel(1, 0, 23, 1);

        sprIncepereOra.setModel(spinnerModelOreI);
        sprTerminareOra.setModel(spinnerModelOreT);

        SpinnerModel spinnerModelMinI = new SpinnerNumberModel(0, 0, 59, 1);
        SpinnerModel spinnerModelminT = new SpinnerNumberModel(0, 0, 59, 1);

        sprIncepereMinute.setModel(spinnerModelMinI);
        sprTerminareMinute.setModel(spinnerModelminT);

        int ziMaxima = findZiMaxima(sprDataIncepereLuna.getValue());

        SpinnerModel spinnerModelZiI = new SpinnerNumberModel(1, 1, ziMaxima, 1);
        sprDataIncepereZi.setModel(spinnerModelZiI);

        ziMaxima = findZiMaxima(sprDataTerminareLuna.getValue());

        SpinnerModel spinnerModelZiT = new SpinnerNumberModel(1, 1, ziMaxima, 1);
        sprDataTerminareZi.setModel(spinnerModelZiT);

        sprDataIncepereZi.setBackground(new Color(24, 28, 28));
        sprDataIncepereZi.getEditor().getComponent(0).setBackground(new Color(24, 28, 28));
        sprDataIncepereZi.getEditor().getComponent(0).setForeground(new Color(240, 240, 240));
        sprDataIncepereZi.getComponent(0).setBackground(new Color(24, 28, 28));
        sprDataIncepereZi.getComponent(1).setBackground(new Color(24, 28, 28));

        sprDataIncepereLuna.setBackground(new Color(24, 28, 28));
        sprDataIncepereLuna.getEditor().getComponent(0).setBackground(new Color(24, 28, 28));
        sprDataIncepereLuna.getEditor().getComponent(0).setForeground(new Color(240, 240, 240));
        sprDataIncepereLuna.getComponent(0).setBackground(new Color(24, 28, 28));
        sprDataIncepereLuna.getComponent(1).setBackground(new Color(24, 28, 28));

        sprDataIncepereAn.setBackground(new Color(24, 28, 28));
        sprDataIncepereAn.getEditor().getComponent(0).setBackground(new Color(24, 28, 28));
        sprDataIncepereAn.getEditor().getComponent(0).setForeground(new Color(240, 240, 240));
        sprDataIncepereAn.getComponent(0).setBackground(new Color(24, 28, 28));
        sprDataIncepereAn.getComponent(1).setBackground(new Color(24, 28, 28));

        sprDataTerminareZi.setBackground(new Color(24, 28, 28));
        sprDataTerminareZi.getEditor().getComponent(0).setBackground(new Color(24, 28, 28));
        sprDataTerminareZi.getEditor().getComponent(0).setForeground(new Color(240, 240, 240));
        sprDataTerminareZi.getComponent(0).setBackground(new Color(24, 28, 28));
        sprDataTerminareZi.getComponent(1).setBackground(new Color(24, 28, 28));

        sprDataTerminareLuna.setBackground(new Color(24, 28, 28));
        sprDataTerminareLuna.getEditor().getComponent(0).setBackground(new Color(24, 28, 28));
        sprDataTerminareLuna.getEditor().getComponent(0).setForeground(new Color(240, 240, 240));
        sprDataTerminareLuna.getComponent(0).setBackground(new Color(24, 28, 28));
        sprDataTerminareLuna.getComponent(1).setBackground(new Color(24, 28, 28));


        sprDataTerminareAn.setBackground(new Color(24, 28, 28));
        sprDataTerminareAn.getEditor().getComponent(0).setBackground(new Color(24, 28, 28));
        sprDataTerminareAn.getEditor().getComponent(0).setForeground(new Color(240, 240, 240));
        sprDataTerminareAn.getComponent(0).setBackground(new Color(24, 28, 28));
        sprDataTerminareAn.getComponent(1).setBackground(new Color(24, 28, 28));

        sprIncepereMinute.setBackground(new Color(24, 28, 28));
        sprIncepereMinute.getEditor().getComponent(0).setBackground(new Color(24, 28, 28));
        sprIncepereMinute.getEditor().getComponent(0).setForeground(new Color(240, 240, 240));
        sprIncepereMinute.getComponent(0).setBackground(new Color(24, 28, 28));
        sprIncepereMinute.getComponent(1).setBackground(new Color(24, 28, 28));

        sprIncepereOra.setBackground(new Color(24, 28, 28));
        sprIncepereOra.getEditor().getComponent(0).setBackground(new Color(24, 28, 28));
        sprIncepereOra.getEditor().getComponent(0).setForeground(new Color(240, 240, 240));
        sprIncepereOra.getComponent(0).setBackground(new Color(24, 28, 28));
        sprIncepereOra.getComponent(1).setBackground(new Color(24, 28, 28));

        sprTerminareOra.setBackground(new Color(24, 28, 28));
        sprTerminareOra.getEditor().getComponent(0).setBackground(new Color(24, 28, 28));
        sprTerminareOra.getEditor().getComponent(0).setForeground(new Color(240, 240, 240));
        sprTerminareOra.getComponent(0).setBackground(new Color(24, 28, 28));
        sprTerminareOra.getComponent(1).setBackground(new Color(24, 28, 28));


        sprTerminareMinute.setBackground(new Color(24, 28, 28));
        sprTerminareMinute.getEditor().getComponent(0).setBackground(new Color(24, 28, 28));
        sprTerminareMinute.getEditor().getComponent(0).setForeground(new Color(240, 240, 240));
        sprTerminareMinute.getComponent(0).setBackground(new Color(24, 28, 28));
        sprTerminareMinute.getComponent(1).setBackground(new Color(24, 28, 28));
    }

    private void setList(List<ActivitateDidacticaModel> activitati) {

        boolean curs = true;
        boolean seminar = true;
        boolean lab = true;

        for (ActivitateDidacticaModel a: activitati) {
            if (a.getTip() == ActivitateDidactica.CURS) {
                curs = false;
            }else if (a.getTip() == ActivitateDidactica.SEMINAR) {
                seminar = false;
            }else if (a.getTip() == ActivitateDidactica.LABORATOR) {
                lab = false;
            }
        }

        if (curs)
            comboTip.addItem(tipActivitate[0]);
        if (seminar)
            comboTip.addItem(tipActivitate[1]);
        if (lab)
            comboTip.addItem(tipActivitate[2]);
    }
}
