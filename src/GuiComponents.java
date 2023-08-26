import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GuiComponents {
    private static final int FRAME_WIDTH = 500;
    private static final int FRAME_HEIGHT = 600;
    private static final Font REGULAR_FONT = new Font("Helvetica", Font.PLAIN, 14);
    private static final Font LONN_FONT = new Font("Arial", Font.BOLD, 20);

    private JFrame frame;
    private JLabel lonnOutput;
    private JTextArea textArea;

    private JTextField monthYearText;
    private JTextField dayStartEndText;
    private JButton addShiftButton;
    private JButton calculateButton;

    public GuiComponents() {
        initGui();
    }

    private void initGui() {
        frame = new JFrame("Lønnsutrekning");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setLayout(null);

        addHeaders();
        addTextFields();
        addButtons();
        addTextArea();
        addOutputLabel();

        frame.setVisible(true);
    }

    private void addHeaders() {
        addLabel("Lønnsutrekning", 0, 0, FRAME_WIDTH, 50, REGULAR_FONT, JLabel.CENTER);
        addLabel("Skriv inn måned/år MM-YYYY:", 0, 50, 250, 50, REGULAR_FONT, JLabel.CENTER);
        addLabel("Skriv inn vakt DD-HH-HH:", 250, 50, 250, 50, REGULAR_FONT, JLabel.CENTER);
    }

    private void addTextArea() {
        textArea = new JTextArea();
        textArea.setBounds(25, 200, 450, 150);
        textArea.setFont(REGULAR_FONT);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        frame.add(textArea);
    }

    private void addOutputLabel() {
        lonnOutput = new JLabel("Total lønn: ");
        lonnOutput.setBounds(25, 425, 450, 25);
        lonnOutput.setHorizontalAlignment(JLabel.CENTER);
        lonnOutput.setFont(LONN_FONT);
        frame.add(lonnOutput);
    }

    private void addLabel(String text, int x, int y, int width, int height, Font font, int alignment) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, width, height);
        label.setFont(font);
        label.setHorizontalAlignment(alignment);
        frame.add(label);
    }

    private void addTextFields() {
        monthYearText = addTextField(25, 100, 200, 25, REGULAR_FONT);
        dayStartEndText = addTextField(275, 100, 200, 25, REGULAR_FONT);
    }

    private void addButtons() {
        addShiftButton = addButton("Legg til", 25, 150, 450, 25, REGULAR_FONT, null);
        calculateButton = addButton("Beregn", 25, 375, 450, 25, REGULAR_FONT, null);
    }

    private JTextField addTextField(int x, int y, int width, int height, Font font) {
        JTextField textField = new JTextField();
        textField.setBounds(x, y, width, height);
        textField.setFont(font);
        textField.setHorizontalAlignment(JTextField.CENTER);
        frame.add(textField);
        return textField;
    }

    private JButton addButton(String text, int x, int y, int width, int height, Font font, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, height);
        button.setFont(font);
        button.setFocusable(false);
        if (actionListener != null) {
            button.addActionListener(actionListener);
        }
        frame.add(button);
        return button;
    }

    // Getter methods for the components
    public JTextField getMonthYearText() {
        return monthYearText;
    }

    public JTextField getDayStartEndText() {
        return dayStartEndText;
    }

    public JButton getAddShiftButton() {
        return addShiftButton;
    }

    public JButton getCalculateButton() {
        return calculateButton;
    }

    public JTextArea getTextArea() {
        return textArea;
    }

    public JLabel getLonnOutput() {
        return lonnOutput;
    }
}
