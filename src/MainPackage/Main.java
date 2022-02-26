package MainPackage;

import ConnectionPackage.MyConnection;
import GuiPackage.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {

    public static void main(String[] args) {

        MyConnection.createConnection();

        MyConnection.validareIntalniri();

        new Logare();

    }
}
