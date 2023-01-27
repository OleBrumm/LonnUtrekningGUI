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
    JLabel header;
    JLabel monthYearHeader;
    JLabel dayStartEndHeader;
    JTextField monthYearText;
    JTextField dayStartEndText;
    JButton addShiftButton;
    JButton calculateButton;
    JTextArea textArea;
    JLabel lonnOutput;

    // List of shift strings
    ArrayList<String> list = new ArrayList<>();

    // Fonts for GUI
    Font regularFont = new Font("Helvetica", Font.PLAIN, 14);
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
        header = new JLabel("Lønnsutrekning");
        header.setBounds(0, 0, 500, 50);
        header.setHorizontalAlignment(JLabel.CENTER);
        header.setFont(regularFont);
        frame.add(header);

        monthYearHeader = new JLabel("Skriv inn måned/år MM-YYYY:");
        monthYearHeader.setBounds(0, 50, 250, 50);
        monthYearHeader.setHorizontalAlignment(JLabel.CENTER);
        monthYearHeader.setFont(regularFont);
        frame.add(monthYearHeader);

        dayStartEndHeader = new JLabel("Skriv inn vakt DD-HH-HH:");
        dayStartEndHeader.setBounds(250, 50, 250, 50);
        dayStartEndHeader.setHorizontalAlignment(JLabel.CENTER);
        dayStartEndHeader.setFont(regularFont);
        frame.add(dayStartEndHeader);

        monthYearText = new JTextField();
        monthYearText.setBounds(25, 100, 200, 25);
        monthYearText.setFont(regularFont);
        monthYearText.setHorizontalAlignment(JTextField.CENTER);
        frame.add(monthYearText);

        dayStartEndText = new JTextField();
        dayStartEndText.setBounds(275, 100, 200, 25);
        dayStartEndText.setFont(regularFont);
        dayStartEndText.setHorizontalAlignment(JTextField.CENTER);
        frame.add(dayStartEndText);

        addShiftButton = new JButton("Legg til");
        addShiftButton.setBounds(25, 150, 450, 25);
        addShiftButton.setFont(regularFont);
        addShiftButton.setFocusable(false);
        addShiftButton.addActionListener(this);
        frame.add(addShiftButton);

        textArea = new JTextArea();
        textArea.setBounds(25, 200, 450, 150);
        textArea.setFont(regularFont);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        frame.add(textArea);

        calculateButton = new JButton("Beregn");
        calculateButton.setBounds(25, 375, 450, 25);
        calculateButton.setFont(regularFont);
        calculateButton.setFocusable(false);
        calculateButton.addActionListener(this);
        frame.add(calculateButton);

        lonnOutput = new JLabel("Total lønn: ");
        lonnOutput.setBounds(25, 425, 450, 25);
        lonnOutput.setHorizontalAlignment(JLabel.CENTER);
        lonnOutput.setFont(lonnFont);
        frame.add(lonnOutput);

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
        if (e.getSource() == addShiftButton) {
            list.add(monthYearText.getText() + "-" + dayStartEndText.getText());
            dayStartEndText.setText("");
            textArea.setText("");
            for (String s : list) {
                textArea.append(s + " // " + getVaktLonn(s) + "\n");
            }
        }

        // Calculate pay
        if (e.getSource() == calculateButton) {
            double total = 0;
            for (String s : list) {
                total += getVaktLonn(s);
            }
            lonnOutput.setText("Total lønn: " + total);
        }
    }

    // Calculate pay for shift
    public double getVaktLonn(String s) {
        double total = 0;
        String[] parts = s.split("-");
        int month = Integer.parseInt(parts[0]);
        int year = Integer.parseInt(parts[1]);
        int day = Integer.parseInt(parts[2]);
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
            for (int i = startHour; i <= endHour; i++) {
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