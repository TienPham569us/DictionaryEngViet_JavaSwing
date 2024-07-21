import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class MultiSpanHeader {
    /*public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                new MultiSpanHeader().makeUI();
            }
        });
    }*/

    public void makeUI() {
        final JTable table = new JTable(5, 6);
        final JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);
        final TableColumnModel model = table.getColumnModel();
        Enumeration<TableColumn> enumColumns = model.getColumns();
        final List<TableColumn> columns = Collections.list(enumColumns);

        final JTable dummy = new JTable(0, 3);
        final JTableHeader dummyHeader = dummy.getTableHeader();
        dummyHeader.setReorderingAllowed(false);
        dummyHeader.setResizingAllowed(false);
        final TableColumnModel dummyModel = dummy.getColumnModel();
        Enumeration<TableColumn> enumDummyColumns = dummyModel.getColumns();
        final List<TableColumn> dummyColumns = Collections.list(enumDummyColumns);

        model.addColumnModelListener(new TableColumnModelListener() {

            @Override
            public void columnAdded(TableColumnModelEvent e) {
            }

            @Override
            public void columnRemoved(TableColumnModelEvent e) {
            }

            @Override
            public void columnMoved(TableColumnModelEvent e) {
            }

            @Override
            public void columnMarginChanged(ChangeEvent e) {
                dummyColumns.get(0).setWidth(columns.get(0).getWidth());
                dummyColumns.get(1).setWidth(columns.get(1).getWidth() +
                        columns.get(2).getWidth());
                dummyColumns.get(2).setWidth(columns.get(3).getWidth() +
                        columns.get(4).getWidth() + columns.get(5).getWidth());
            }

            @Override
            public void columnSelectionChanged(ListSelectionEvent e) {
            }
        });
        JScrollPane pane = new JScrollPane(table);
        pane.setPreferredSize(new Dimension(600, 200));

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(pane);
        frame.pack();

        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.add(dummyHeader);
        panel.add(header);
        pane.getColumnHeader().setView(panel);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}