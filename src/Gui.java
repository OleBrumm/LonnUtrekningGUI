import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;


public class Gui implements ActionListener {

    JFrame frame;
    JTextField textField;
    JLabel label;
    JButton button;
    JButton calculate;
    JTextArea textArea;
    ArrayList<String> list = new ArrayList<String>();

    Font myFont = new Font("Arial", Font.BOLD, 15);
    Font lonnFont = new Font("Arial", Font.BOLD, 20);

    double timelonn = 163.53, ekstra1 = 110, ekstra2 = 55;

    Gui() {

        frame = new JFrame("Lønnsutrekning");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 600);
        frame.setLayout(null);

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


        frame.setVisible(true);

    }

    public static void main(String[] args) {
        Gui gui = new Gui();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            list.add(textField.getText());
            textField.setText("");
            textArea.setText("");
            for (String s : list) {
                textArea.append(s + " // " + getVaktLonn(s) + "\n");
            }
        }
        if (e.getSource() == calculate) {
            double total = 0;
            for (String s : list) {
                total += getVaktLonn(s);
            }
            label.setText("Lønn: " + total);
        }
    }

    public double getVaktLonn(String s) {
        double total = 0;
        String[] parts = s.split("-");
        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);
        int startHour = Integer.parseInt(parts[3]);
        int startMinute = Integer.parseInt(parts[4]);
        int endHour = Integer.parseInt(parts[5]);
        int endMinute = Integer.parseInt(parts[6]);

        LocalDate date = LocalDate.of(year, month, day);
        DayOfWeek dayOfWeekDow = date.getDayOfWeek();
        int dayOfWeek = dayOfWeekDow.getValue();

        Calendar start = Calendar.getInstance();
        start.set(year, month, day, startHour, startMinute);
        Calendar end = Calendar.getInstance();
        end.set(year, month, day, endHour, endMinute);
        long diff = end.getTimeInMillis() - start.getTimeInMillis();
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffSeconds = diff / 1000 % 60;
        double diffTotal = diffHours + (diffMinutes / 60) + (diffSeconds / 3600);
        double diffTotalRounded = Math.round(diffTotal * 100.0) / 100.0;

        if (dayOfWeek == 6) {
            for (int i = startHour; i < endHour; i++) {
                if (i < 15) {
                    total += timelonn;
                } else if (i < 18) {
                    total += timelonn + ekstra2;
                } else {
                    total += timelonn + ekstra1;
                }
            }
            total -= (timelonn/2 + ekstra1/2);
        }
        else if (dayOfWeek == 7) {
            for (int i = startHour; i < endHour; i++) {
                total += timelonn + ekstra1;
            }
            total -= (timelonn/2 + ekstra1/2);
        }
        else {
            total += timelonn * diffTotalRounded;
            total -= timelonn/2;
        }
        System.out.println(dayOfWeek);
        return Math.round(total * 100.0) / 100.0;
    }
}