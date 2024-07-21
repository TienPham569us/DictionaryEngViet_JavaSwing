import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class CellCenterRenderer extends DefaultTableCellRenderer {
    public CellCenterRenderer() {
        setHorizontalAlignment(SwingConstants.RIGHT);
        setOpaque(true);
    }
    private static final Border EMPTY_BORDER = BorderFactory.createEmptyBorder(1, 1, 1, 0);

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        ((JComponent) component).setBorder(EMPTY_BORDER);
        return component;
    }
}
