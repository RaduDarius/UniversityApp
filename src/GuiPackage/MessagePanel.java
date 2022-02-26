package GuiPackage;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class MessagePanel extends JPanel {
    JTextArea textArea = new JTextArea(3, 5);

    public MessagePanel(String text, String sender, String date){
        TitledBorder title = new TitledBorder("Sent by " + sender + "     " + date);
        title.setTitleColor(Color.WHITE);
        textArea.setBorder(title);
        this.add(textArea);
        textArea.setText(text);
        textArea.setFont(new Font("Times New Roman", Font.BOLD, 20));
        textArea.setPreferredSize(new Dimension(250, 5));
        textArea.setEditable(false);
        textArea.setForeground(new Color(160, 196, 44));
        textArea.setBackground(new Color(24, 28, 28));
        this.setBackground(new Color(24, 28, 28));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        

    }
}
