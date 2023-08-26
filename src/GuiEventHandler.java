import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GuiEventHandler implements ActionListener {
    private final GuiComponents guiComponents;
    private final ShiftLogic shiftLogic;

    public GuiEventHandler(GuiComponents guiComponents, ShiftLogic shiftLogic) {
        this.guiComponents = guiComponents;
        this.shiftLogic = shiftLogic;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == guiComponents.getAddShiftButton()) {
            handleAddShift();
        } else if (source == guiComponents.getCalculateButton()) {
            handleCalculatePay();
        }
    }

    private void handleAddShift() {
        String shiftDetails = guiComponents.getMonthYearText().getText() + "-" + guiComponents.getDayStartEndText().getText();
        shiftLogic.addShift(shiftDetails);  // Assume this method is added in ShiftLogic class
        guiComponents.getDayStartEndText().setText("");
        updateTextArea();
    }

    private void handleCalculatePay() {
        double totalPay = shiftLogic.calculateTotalPay();  // Assume this method is added in ShiftLogic class
        guiComponents.getLonnOutput().setText("Total lønn: " + totalPay);
    }

    private void updateTextArea() {
        guiComponents.getTextArea().setText("");
        for (String shift : shiftLogic.getShiftList()) {  // Assume getShiftList() method is added in ShiftLogic class
            double pay = shiftLogic.getVaktLonn(shift);
            guiComponents.getTextArea().append(shift + " // " + pay + "\n");
        }
    }
}
