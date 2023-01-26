import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;


public class Gui implements ActionListener {

    // GUI components
    JFrame frame;
    JTextField textField;
    JLabel label;
    JButton button;
    JButton calculate;
    JTextArea textArea;

    // List of shift strings
    ArrayList<String> list = new ArrayList<>();

    // Fonts for GUI
    Font myFont = new Font("Arial", Font.BOLD, 15);
    Font lonnFont = new Font("Arial", Font.BOLD, 20);

    // Variables for calculating pay
    double timelonn = 163.53;
    double extra1 = 110;
    double extra2 = 55;

    Gui() {

        // Create GUI
        frame = new JFrame("Lønnsutrekning");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 600);
        frame.setLayout(null);


        // Create components
        label = new JLabel("Lønnsutrekning");
        label.setBounds(0, 0, 500, 50);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setFont(myFont);
        frame.add(label);

        label = new JLabel("Skriv inn vakter DD-MM-YYYY-HH-MM-HH-MM:");
        label.setBounds(0, 50, 500, 50);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setFont(myFont);
        frame.add(label);

        textField = new JTextField();
        textField.setBounds(25, 100, 450, 25);
        textField.setFont(myFont);
        textField.setHorizontalAlignment(JTextField.CENTER);
        frame.add(textField);

        button = new JButton("Legg til");
        button.setBounds(25, 150, 450, 25);
        button.setFont(myFont);
        button.setFocusable(false);
        button.addActionListener(this);
        frame.add(button);

        textArea = new JTextArea();
        textArea.setBounds(25, 200, 450, 150);
        textArea.setFont(myFont);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        frame.add(textArea);

        calculate = new JButton("Beregn");
        calculate.setBounds(25, 375, 450, 25);
        calculate.setFont(myFont);
        calculate.setFocusable(false);
        calculate.addActionListener(this);
        frame.add(calculate);

        label = new JLabel("Lønn:");
        label.setBounds(25, 425, 450, 25);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setFont(lonnFont);
        frame.add(label);

        // Make GUI visible
        frame.setVisible(true);

    }

    // Main method
    public static void main(String[] args) {
        new Gui();
    }

    // Action listener
    @Override
    public void actionPerformed(ActionEvent e) {

        // Add shift to list
        if (e.getSource() == button) {
            list.add(textField.getText());
            textField.setText("");
            textArea.setText("");
            for (String s : list) {
                textArea.append(s + " // " + getVaktLonn(s) + "\n");
            }
        }

        // Calculate pay
        if (e.getSource() == calculate) {
            double total = 0;
            for (String s : list) {
                total += getVaktLonn(s);
            }
            label.setText("Lønn: " + total);
        }
    }

    // Calculate pay for shift
    public double getVaktLonn(String s) {
        double total = 0;
        String[] parts = s.split("-");
        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);
        int startHour = Integer.parseInt(parts[3]);
        int endHour = Integer.parseInt(parts[4]);

        LocalDate date = LocalDate.of(year, month, day);
        DayOfWeek dayOfWeekDow = date.getDayOfWeek();
        int dayOfWeek = dayOfWeekDow.getValue();

        double diffTotalRounded = getDiffTotalRounded(year, month, day, startHour, endHour);
        total += getTotalForWeekday(dayOfWeek, diffTotalRounded, startHour, endHour);
        return Math.round(total * 100.0) / 100.0;
    }

    // Calculate time difference
    private double getDiffTotalRounded(int year, int month, int day, int startHour, int endHour) {
        Calendar start = Calendar.getInstance();
        start.set(year, month, day, startHour, 0);
        Calendar end = Calendar.getInstance();
        end.set(year, month, day, endHour, 0);
        long diff = end.getTimeInMillis() - start.getTimeInMillis();
        long diffHours = diff / (60 * 60 * 1000) % 24;
        return Math.round(diffHours * 100.0) / 100.0;
    }

    // Calculate pay for weekday
    private double getTotalForWeekday(int dayOfWeek, double diffTotalRounded, int startHour, int endHour) {
        double total = 0;
        if (dayOfWeek == 6) {
            for (int i = startHour; i < endHour; i++) {
                if (i < 15) {
                    total += timelonn;
                } else if (i < 18) {
                    total += timelonn + extra2;
                } else {
                    total += timelonn + extra1;
                }
            }
            total -= (timelonn / 2 + extra1 / 2);
        } else if (dayOfWeek == 7) {
            for (int i = startHour; i < endHour; i++) {
                total += timelonn + extra1;
            }
            total -= (timelonn / 2 + extra1 / 2);
        } else {
            total += timelonn * diffTotalRounded;
            total -= timelonn / 2;
        }
        return total;
    }
}