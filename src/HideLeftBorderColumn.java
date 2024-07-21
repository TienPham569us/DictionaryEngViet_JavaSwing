import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;

public class HideLeftBorderColumn {
    private static class CustomCellRenderer extends DefaultTableCellRenderer {
        private static final Border EMPTY_BORDER = BorderFactory.createEmptyBorder(1, 1, 1, 1);

        public CustomCellRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            ((JComponent) component).setBorder(EMPTY_BORDER);

            return component;
        }
    }
}