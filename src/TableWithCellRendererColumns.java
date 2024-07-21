import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class TableWithCellRendererColumns {

    private static class MyTableModel extends AbstractTableModel {
        private final String[] columnNames = {"Col 0", "Column 1", "Column 2"};

        private final Object[][] data = {
                {"Header 1", "Data 1", ""},
                {"Header 2", "Data 3", ""}
        };

        @Override
        public int getRowCount() {
            return data.length;
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        @Override
        public String getColumnName(int col) {
            return columnNames[col];
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            return false;
        }
    }

    private static class HeaderRenderer extends DefaultTableCellRenderer {
        public HeaderRenderer() {
            setHorizontalAlignment(SwingConstants.RIGHT);
            setOpaque(true);
            setForeground(Color.WHITE);
            //setBackground(Color.LIGHT_GRAY);
            //setForeground(Color.BLACK);
        }
        private static final Border EMPTY_BORDER = BorderFactory.createEmptyBorder(1, 0, 1, 1);

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            ((JComponent) component).setBorder(EMPTY_BORDER);
            return component;
        }
    }

}