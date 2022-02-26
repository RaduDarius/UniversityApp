package GuiPackage;

import ConnectionPackage.MyConnection;
import ModelsPackage.*;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class PrivatChat extends JFrame {
    JPanel mainPanel = new JPanel();
    JPanel titlePanel = new JPanel();
    JPanel convPanel = new JPanel();
    JPanel typingPanel = new JPanel();
    JPanel footerPanel = new JPanel();
    JPanel messagesPanel = new JPanel();
    JPanel buttonsPanel = new JPanel();

    JLabel lblTitle = new JLabel("Private chat");
    JTextArea typingArea = new JTextArea(5, 10);

    JTextArea messagesArea = new JTextArea(50, 61);
    JList<String> conversations = new JList<>();
    JButton btnSend = new JButton("Send");
    JButton btnNewConv = new JButton("Start a new conversation");
    JButton btnNewGroup = new JButton("Create a new group");
    JButton btnCreateMeet = new JButton("Create a meeting");
    JButton btnBack = new JButton("Back");
    JButton btnInrolareMeet = new JButton("Inrolare Meet");
    JButton btnInrolareGrup = new JButton("Inrolare Grup");
    JButton btnMembrii = new JButton("Vizualizare membrii din grup");
    JButton btnLeaveGrup = new JButton("Parasire grup");

    private ArrayList<ConversatieModel> users = new ArrayList<>();

    private ArrayList<MessagePanel> msgPanels = new ArrayList<>();

    private static ConversatieModel toSend;

    public PrivatChat(UserModel model, ConversatieModel toSendModel){
        super("Private chat");
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(450, 100);
        this.setResizable(false);
        this.setSize(900, 700);
        this.setContentPane(mainPanel);
        this.getContentPane().setBackground(new Color(32, 36, 36));

        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(32, 36, 36));
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        titlePanel.add(lblTitle);
        titlePanel.setBackground(new Color(32, 36, 36));
        lblTitle.setFont(new Font("Droid Sans", Font.BOLD, 30));
        lblTitle.setForeground(new Color(160, 196, 44));

        messagesPanel.setLayout(new GridLayout(100, 1));
        mainPanel.add(messagesPanel, BorderLayout.CENTER);
        messagesPanel.setBackground(new Color(24, 28, 28));
        messagesArea.setBackground(new Color(24, 28, 28));
        messagesArea.setForeground(new Color(160, 196, 44));
        messagesArea.setForeground(new Color(160, 196, 44));

        setToSend(toSendModel);

        mainPanel.add(convPanel, BorderLayout.WEST);
        convPanel.add(conversations);
        convPanel.setBackground(new Color(24, 28, 28));
        conversations.setListData(setList(model)); //AICI AM SETAT
        conversations.setPreferredSize(new Dimension(180, 1500));
        conversations.setBackground(new Color(24, 28, 28));
        conversations.setForeground(new Color(160, 196, 44));
        conversations.setFont(new Font("Times New Roman", Font.BOLD, 18));
        conversations.setBorder(new EtchedBorder());

        btnNewConv.setFocusable(false);
        btnNewConv.setForeground(new Color(8, 6, 3));
        btnNewConv.setBackground(new Color(160, 196, 44));
        btnNewConv.setFont(new Font("Times New Roman", Font.BOLD, 20));

        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        typingPanel.add(typingArea);
        typingArea.setPreferredSize(new Dimension(800, 20));
        typingArea.setBackground(new Color(62, 73, 73));
        typingArea.setForeground(Color.WHITE);
        typingArea.setFont(new Font("Consolas", Font.BOLD, 20));
        typingArea.setBorder(new EtchedBorder());
        typingPanel.add(btnSend);
        typingPanel.setBackground(new Color(8, 6, 3));
        typingArea.setWrapStyleWord(true);
        typingArea.setLineWrap(true);

        btnSend.setFocusable(false);
        btnSend.setForeground(new Color(8, 6, 3));
        btnSend.setBackground(new Color(160, 196, 44));
        btnSend.setFont(new Font("Times New Roman", Font.BOLD, 20));

        messagesPanel.add(messagesArea);
        messagesArea.setPreferredSize(new Dimension(600, 50));
        messagesArea.setBackground(new Color(24, 28, 28));
        messagesArea.setForeground(Color.WHITE);
        messagesArea.setFont(new Font("Consolas", Font.BOLD, 20));
        messagesArea.setEditable(false);

        messagesPanel.setPreferredSize(new Dimension(700, 6000));
        JScrollPane scrollPane = new JScrollPane(messagesPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        messagesPanel.setAutoscrolls(true);
        scrollPane.setPreferredSize(new Dimension(700, 600));
        mainPanel.add(scrollPane, BorderLayout.EAST);

        JScrollPane listScroller = new JScrollPane();
        listScroller.setViewportView(conversations);
        conversations.setLayoutOrientation(JList.VERTICAL);
        convPanel.add(listScroller);
        listScroller.getVerticalScrollBar().setBackground(Color.BLACK);
        listScroller.setPreferredSize(new Dimension(180, 900));

        footerPanel.setLayout(new GridLayout(2, 1));
        footerPanel.add(typingPanel);
        typingPanel.setBackground(new Color(24, 28, 28));

        buttonsPanel.add(btnBack);
        buttonsPanel.add(btnNewGroup);
        buttonsPanel.add(btnNewConv);
        buttonsPanel.add(btnCreateMeet);
        buttonsPanel.add(btnInrolareGrup);
        buttonsPanel.add(btnInrolareMeet);
        buttonsPanel.add(btnMembrii);
        buttonsPanel.add(btnLeaveGrup);

        btnNewGroup.setFocusable(false);
        btnNewGroup.setForeground(new Color(8, 6, 3));
        btnNewGroup.setBackground(new Color(160, 196, 44));
        btnNewGroup.setFont(new Font("Times New Roman", Font.BOLD, 20));

        btnCreateMeet.setFocusable(false);
        btnCreateMeet.setForeground(new Color(8, 6, 3));
        btnCreateMeet.setBackground(new Color(160, 196, 44));
        btnCreateMeet.setFont(new Font("Times New Roman", Font.BOLD, 20));

        btnBack.setFocusable(false);
        btnBack.setForeground(new Color(8, 6, 3));
        btnBack.setBackground(new Color(160, 196, 44));
        btnBack.setFont(new Font("Times New Roman", Font.BOLD, 20));

        btnInrolareGrup.setFocusable(false);
        btnInrolareGrup.setForeground(new Color(8, 6, 3));
        btnInrolareGrup.setBackground(new Color(160, 196, 44));
        btnInrolareGrup.setFont(new Font("Times New Roman", Font.BOLD, 20));

        btnInrolareMeet.setFocusable(false);
        btnInrolareMeet.setForeground(new Color(8, 6, 3));
        btnInrolareMeet.setBackground(new Color(160, 196, 44));
        btnInrolareMeet.setFont(new Font("Times New Roman", Font.BOLD, 20));

        btnMembrii.setFocusable(false);
        btnMembrii.setForeground(new Color(8, 6, 3));
        btnMembrii.setBackground(new Color(160, 196, 44));
        btnMembrii.setFont(new Font("Times New Roman", Font.BOLD, 20));

        btnLeaveGrup.setFocusable(false);
        btnLeaveGrup.setForeground(new Color(8, 6, 3));
        btnLeaveGrup.setBackground(new Color(160, 196, 44));
        btnLeaveGrup.setFont(new Font("Times New Roman", Font.BOLD, 20));

        footerPanel.add(buttonsPanel);
        footerPanel.setBackground(new Color(24, 28, 28));
        buttonsPanel.setBackground(new Color(24, 28, 28));

        if (toSend.getClass() == UserModel.class)
            loadMesaje(model);
        setDataVisible(model);

        typingArea.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(typingArea.getText().length() >= 355){
                    typingArea.setEditable(false);
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyChar() == 8)
                    typingArea.setEditable(true);
                else if(e.getKeyChar() == '\n')
                    btnSend.doClick();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyChar() == '\n')
                    typingArea.setText("");
            }
        });

        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                messagesArea.append("\n");
                String text = typingArea.getText();
                String sender = model.getNume() + " " + model.getPrenume();

                typingArea.setText("");

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm-ss");
                LocalDateTime now = LocalDateTime.now();
                String date = dtf.format(now);
                if (!text.isBlank()) {

                    MesajPrivatModel msg = new MesajPrivatModel(toSend, model, text, date);


                    MyConnection.saveMesaj(msg);


                    MessagePanel newMsgPanel = new MessagePanel(text, sender, date);
                    msgPanels.add(newMsgPanel);
                    messagesPanel.add(newMsgPanel, BorderLayout.SOUTH);
                }

            }
        });

        conversations.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

                int index = conversations.getSelectedIndex();
                setToSend(users.get(index));
                setDataVisible(model);

                for (MessagePanel m: msgPanels) {
                    messagesPanel.remove(m);
                    m.remove(0);
                }

                msgPanels = new ArrayList<>();
                loadMesaje(model);
            }
        });

        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new VizualizareCursuri(model, 7, null, null);
            }
        });

        btnNewConv.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new VizualizareCursuri(model, 7, null, null);
            }
        });

        btnNewGroup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new CreateGroup(model, toSend);
            }
        });

        btnCreateMeet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new AdaugareActivitati(model, 2, null, null, (GrupModel)toSend);
            }
        });

        btnInrolareGrup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //INROLAREA LA GRUPURI PENTRU STUDENTI
                dispose();
                new InrolareGrup(model, toSend, 1, null);
            }
        });

        btnInrolareMeet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //INROLAREA LA MEET-UL PENTRU STUDENTI
                dispose();
                //System.out.println(toSendModel);
                new InrolareGrup(model, toSend, 2, null);
            }
        });

        btnMembrii.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                //fereastra 3 inseamna ca afisam membrii
                new InrolareGrup(model, toSend, 3, null);
            }
        });

        btnLeaveGrup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: SA VINA RAZVAN SA MI FACA TRIGGERE

                MyConnection.renuntareGrup(model, (GrupModel) toSend);

                dispose();
                new VizualizareCursuri(model, 7, null, null);

            }
        });
    }

    private void loadMesaje(UserModel model) {

        ArrayList<MesajPrivatModel> mesaje = MyConnection.getMesajeFromUser(model, toSend);

        assert mesaje != null;
        for (MesajPrivatModel m: mesaje) {

            String text = m.getText();
            String sender = m.getFromSend().getNume() + " " + m.getFromSend().getPrenume();
            String date = m.getDataTrimitere();

            MessagePanel newMsgPanel = new MessagePanel(text, sender, date);
            msgPanels.add(newMsgPanel);
            messagesPanel.add(newMsgPanel, BorderLayout.SOUTH);
        }
    }

    private void setToSend(ConversatieModel toSendModel) {

        if (toSendModel.getClass() == UserModel.class) {
            toSend = toSendModel;
            messagesArea.setForeground(new Color(160, 196, 44));
            messagesArea.setText("Send message to " + ((UserModel)toSend).getNume() + " " + ((UserModel)toSend).getPrenume());
        }else {
            toSend = toSendModel;
            messagesArea.setForeground(new Color(160, 196, 44));
            messagesArea.setText("Sending a message in " + ((GrupModel)toSend).getDenumire());
        }
    }

    private String[] setList(UserModel model) {

        users = MyConnection.getConversationsFromUser(model);
        assert users != null;

        ArrayList<GrupModel> grupuri = MyConnection.getGrupByUser(model);

        users.addAll(grupuri);

        boolean found = false;

        for (ConversatieModel u: users) {
            if (u.getClass() == UserModel.class && toSend.getClass() == UserModel.class) {
                if (((UserModel)u).getUsername().equals(((UserModel)toSend).getUsername())) {
                    found = true;
                }
            } else if (u.getClass() == GrupModel.class && toSend.getClass() == GrupModel.class){
                if (((GrupModel)u).getId() == ((GrupModel)toSend).getId()) {
                    found = true;
                }
            }
        }

        if (!found) {
            users.add(toSend);
        }

        int numOfUsers = 0;
        numOfUsers = users.size();

        int cont = 0;
        String[] results = new String[numOfUsers];

        for (ConversatieModel u: users) {
            if (u.getClass() == UserModel.class) {
                results[cont++] = ((UserModel) u).getNume() + " " + ((UserModel) u).getPrenume();
            }else {
                results[cont++] = ((GrupModel) u).getDenumire();
            }
        }
        return results;

    }

    private void setDataVisible(UserModel model) {
        if (model.getRol() != Rol.STUDENT) {
            btnNewGroup.setVisible(false);
            btnCreateMeet.setVisible(false);
            btnInrolareGrup.setVisible(false);
            btnInrolareMeet.setVisible(false);
            btnMembrii.setVisible(false);
            btnLeaveGrup.setVisible(false);
        }
        else if (toSend.getClass() == UserModel.class){
            btnCreateMeet.setVisible(false);
            btnInrolareMeet.setVisible(false);
            btnMembrii.setVisible(false);
            btnLeaveGrup.setVisible(false);
        }
        else if (toSend.getClass() == GrupModel.class) {
            btnCreateMeet.setVisible(true);
            btnInrolareMeet.setVisible(true);
            btnMembrii.setVisible(true);
            btnLeaveGrup.setVisible(true);
        }
    }
}
