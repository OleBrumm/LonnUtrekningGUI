import javax.swing.*;

public class Gui {
    public static void main(String[] args) {
        GuiComponents guiComponents = new GuiComponents();
        ShiftLogic shiftLogic = new ShiftLogic();
        GuiEventHandler handler = new GuiEventHandler(guiComponents, shiftLogic);

        guiComponents.getAddShiftButton().addActionListener(handler);
        guiComponents.getCalculateButton().addActionListener(handler);

    }
}
