import org.jdatepicker.DateModel;
import org.jdatepicker.JDatePanel;
import org.jdatepicker.JDatePicker;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class CreateUI {
    private JFrame frame;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JPanel panelTranslation;
    private JPanel panelAddingWord;
    private JPanel panelFavoritesWord;
    public CreateUI() {
        initBackend();

        JFrame.setDefaultLookAndFeelDecorated(true);
        frame = new JFrame("Dictionary");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        WindowListener windowListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int result = showConfirmationDialog(frame);
                if (result == JOptionPane.YES_OPTION) {
                    FileUtility.saveXMLFile(EnToVnFilePath, modelEnToVn.getTreeMapRecord());
                    FileUtility.saveXMLFile(VnToEnFilePath, modelVnToEn.getTreeMapRecord());

                    FileUtility.saveXMLFile(favoritesEnToVnFilePath,modelFavoritesEnToVn.getTreeMapRecord());
                    FileUtility.saveXMLFile(favoritesVnToEnFilePath,modelFavoritesVnToEn.getTreeMapRecord());
                    frame.dispose();
                } else if (result == JOptionPane.NO_OPTION) {
                    frame.dispose();
                }
            }
        };
        initMainPanel();

       // frame.setPreferredSize(new Dimension(300,400));
        frame.setContentPane(mainPanel);
        frame.addWindowListener(windowListener);
        frame.pack();
        frame.setVisible(true);
    }
    private static int showConfirmationDialog(JFrame frame) {
        String message = "Do you want to save changes?";
        String title = "Save Changes";
        int optionType = JOptionPane.YES_NO_CANCEL_OPTION;
        int messageType = JOptionPane.QUESTION_MESSAGE;
        Icon icon = null;

        return JOptionPane.showOptionDialog(frame, message, title, optionType, messageType, icon, null, null);
    }
    String rootFolder;
    LanguageTranslationModel modelVnToEn;
    LanguageTranslationModel modelEnToVn;
    LanguageTranslationModel modelFavoritesVnToEn;
    LanguageTranslationModel modelFavoritesEnToVn;
    private String historyFilePath;
    private String favoritesVnToEnFilePath;
    private String favoritesEnToVnFilePath;
    private String VnToEnFilePath;
    private String EnToVnFilePath;
    private void initBackend() {
        rootFolder = FileUtility.getRootProjectDirectory();
        historyFilePath = rootFolder+"/Data/history.txt";
        favoritesVnToEnFilePath = rootFolder + "/Data/Favorites_Viet_Anh.xml";
        favoritesEnToVnFilePath = rootFolder + "/Data/Favorites_Anh_Viet.xml";
        VnToEnFilePath = rootFolder + "/Data/Viet_Anh.xml";
        EnToVnFilePath = rootFolder + "/Data/Anh_Viet.xml";


        modelVnToEn = new VnToEngModel();
        modelVnToEn.setTreeMapRecord(FileUtility.loadXMLFile(rootFolder + "/Data/Viet_Anh.xml"));

        modelEnToVn = new EngToVnModel();
        modelEnToVn.setTreeMapRecord(FileUtility.loadXMLFile(rootFolder + "/Data/Anh_Viet.xml"));

        modelFavoritesEnToVn = new EngToVnModel();
        modelFavoritesEnToVn.setTreeMapRecord(FileUtility.loadXMLFile(favoritesEnToVnFilePath));

        modelFavoritesVnToEn = new VnToEngModel();
        modelFavoritesVnToEn.setTreeMapRecord(FileUtility.loadXMLFile(favoritesVnToEnFilePath));


        //modelFavoritesEnToVn.addNewWord("okay","ừ");

    }
    private JPanel panelStatisticalWord;
    private JPanel panelListWord;
    private void initMainPanel() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(Color.WHITE);

        initPanelTranslation();
        initPanelAddingWord();
        initPanelFavoritesWord();
        initPanelStatisticalWord();
        initPanelListWord();

        mainPanel.add(panelTranslation,"panelTranslation");
        mainPanel.add(panelAddingWord,"panelAddingWord");
        mainPanel.add(panelFavoritesWord, "panelFavoritesWord");
        mainPanel.add(panelStatisticalWord, "panelStatisticalWord");
        mainPanel.add(panelListWord,"panelListWord");
        cardLayout.show(mainPanel,"panelTranslation");

    }
    private void initPanelListWord() {
        panelListWord = new JPanel();
        panelListWord.setLayout(new BorderLayout());

        initPanelReturn();
        panelListWord.add(panelReturn,BorderLayout.PAGE_START);

        initPanelTableListWord();
        panelTableListWord.setMinimumSize(new Dimension(500,500));
        panelListWord.add(panelTableListWord, BorderLayout.CENTER);

        initPanelOperationListWord();
        panelListWord.add(panelOperationListWord, BorderLayout.LINE_START);

    }
    private JPanel panelOperationListWord;
    private JComboBox comboBoxListWordType;
    private JButton buttonDeleteWordInList;
    private JTextField textFieldWordInList;
    private JTextArea textAreaMeaningInList;
    private JButton buttonSearchInList;
    private void clearWordDisplay() {
        textFieldWordInList.setText("");
        textAreaMeaningInList.setText("");
    }
    private void searchStringInTable(String searchString, JTable table) {
        int rowCount = table.getRowCount();
        int column = 0; // Column to search (0 for the first column)

        for (int row = 0; row < rowCount; row++) {
            String cellValue = table.getValueAt(row, column).toString();
            if (cellValue.equals(searchString)) {
                //System.out.println("find");
                table.setRowSelectionInterval(row, row);
                Rectangle rect = table.getCellRect(row, 0, true);
                table.scrollRectToVisible(rect);
                break;
            }
        }
        //System.out.println("not find");
    }
    private void initPanelOperationListWord() {
        panelOperationListWord = new JPanel();
        panelOperationListWord.setLayout(new BoxLayout(panelOperationListWord,BoxLayout.Y_AXIS));

        buttonDeleteWordInList = new JButton("Delete word");
        buttonDeleteWordInList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = comboBoxListWordType.getSelectedIndex();

                if (selectedIndex==0 && tableEnToVn.getSelectedRow()!=-1) {
                    int selectedRow = tableEnToVn.getSelectedRow();
                    String word = (String) tableEnToVn.getValueAt(selectedRow,0);
                    tableModelEnToVn.removeRow(selectedRow);
                    tableModelEnToVn.fireTableDataChanged();

                    String result1 = modelEnToVn.deleteWord(word);
                    String temp = modelFavoritesEnToVn.deleteWord(word);

                   // System.out.println("buttonDeleteWord: "+temp);
                    if (temp !=null) {
                        searchAndDeleteInTable(tableFavoritesWordEnToVnAsc,tableModelFavoritesEnToVn, word);
                        tableModelFavoritesEnToVn.fireTableDataChanged();
                    }

                    if (result1!=null) {
                        JOptionPane.showMessageDialog(frame, "Delete word successfully!");
                        clearWordDisplay();
                        clearInput();
                        clearOutput();
                    } else {
                        JOptionPane.showMessageDialog(frame, "Cannot find the word. Delete fail!");
                    }

                } else if (selectedIndex==1 && tableVnToEn.getSelectedRow()!=-1) {
                    int selectedRow = tableVnToEn.getSelectedRow();
                    //System.out.println("selectedRow: "+selectedRow);
                    String word = (String) tableVnToEn.getValueAt(selectedRow,0);
                    tableModelVnToEn.removeRow(selectedRow);
                    tableModelVnToEn.fireTableDataChanged();

                    String result2 = modelVnToEn.deleteWord(word);
                    String temp = modelFavoritesVnToEn.deleteWord(word);

                    if (temp !=null) {
                        searchAndDeleteInTable(tableFavoritesWordVnToEnAsc,tableModelFavoritesVnToEn, word);
                        tableModelFavoritesVnToEn.fireTableDataChanged();
                    }

                    if (result2!=null) {
                        JOptionPane.showMessageDialog(frame, "Delete word successfully!");
                        clearWordDisplay();
                        clearInput();
                        clearOutput();
                    } else {
                        JOptionPane.showMessageDialog(frame, "Cannot find the word. Delete fail!");
                    }
                } else {

                    JOptionPane.showMessageDialog(frame, "Cannot find the word. Delete fail!");
                }
            }
        });
        //buttonDeleteWordInList.setEnabled(false);

        initComboBoxListWordType();
        textFieldWordInList = new JTextField();
        textFieldWordInList.setMaximumSize(new Dimension(300,70));
        textFieldWordInList.setBackground(Color.WHITE);
        buttonSearchInList = new JButton("Search");
        buttonSearchInList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = comboBoxListWordType.getSelectedIndex();

                if (index == 0) {
                    String word = textFieldWordInList.getText();
                    //String meaning = modelEnToVn.findMeaningOfWord(word);
                    String meaning = modelEnToVn.getMeaning(word);
                    if (meaning==null) {
                        meaning="Cannot find the meaning of this word";
                    } else {
                        searchStringInTable(word,tableEnToVn);
                        FileUtility.saveSearchToHistory(word,historyFilePath);
                    }
                    // System.out.println(meaning);
                    textAreaMeaningInList.setText(meaning);
                } else if (index == 1) {
                    String word = textFieldWordInList.getText();
                    //String meaning = modelVnToEn.findMeaningOfWord(word);
                    String meaning = modelVnToEn.getMeaning(word);
                    if (meaning==null) {
                        meaning="Cannot find the meaning of this word";
                    } else {
                        searchStringInTable(word,tableVnToEn);
                        FileUtility.saveSearchToHistory(word,historyFilePath);
                    }
                    // System.out.println(meaning);
                    textAreaMeaningInList.setText(meaning);
                }
            }
        });
        //textFieldWordInList.setEditable(false);
        textAreaMeaningInList = new JTextArea();
        textAreaMeaningInList.setEditable(false);

        comboBoxListWordType.setMaximumSize(new Dimension(300,70));

        panelOperationListWord.add(comboBoxListWordType);
        panelOperationListWord.add(buttonAddWord);
        panelOperationListWord.add(buttonDeleteWordInList);
        panelOperationListWord.add(textFieldWordInList);
        panelOperationListWord.add(buttonSearchInList);
        panelOperationListWord.add(new JScrollPane(textAreaMeaningInList));
    }
    private void initComboBoxListWordType() {
        String[] type = new String[]{"English To Vietnamese","Vietnamese To English"};
        comboBoxListWordType = new JComboBox(type);
        comboBoxListWordType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = comboBoxListWordType.getSelectedIndex();
                if (index==0) {
                    cardLayoutListWord.show(panelTableListWordDetail,"tableEnToVn");
                } else if (index==1) {
                    cardLayoutListWord.show(panelTableListWordDetail,"tableVnToEn");
                }
            }
        });
    }
    private JPanel panelTableListWord;
    private CardLayout cardLayoutListWord;
    private JTable tableEnToVn;
    private JTable tableVnToEn;
    private DefaultTableModel tableModelEnToVn;
    private DefaultTableModel tableModelVnToEn;
    private JPanel panelTableListWordDetail;
    private void initPanelTableListWord() {
        panelTableListWord = new JPanel();
        panelTableListWordDetail = new JPanel();
        cardLayoutListWord = new CardLayout();
        panelTableListWordDetail.setLayout(cardLayoutListWord);
        panelTableListWord.setLayout(new BoxLayout(panelTableListWord,BoxLayout.Y_AXIS));

        tableModelEnToVn = initTableFavoritesWordAsc(modelEnToVn.getTreeMapRecord());
        tableModelVnToEn = initTableFavoritesWordAsc(modelVnToEn.getTreeMapRecord());

        tableEnToVn = new JTable(tableModelEnToVn);
        tableVnToEn = new JTable(tableModelVnToEn);

        tableEnToVn.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int index = tableEnToVn.getSelectedRow();

                /*boolean selected = tableEnToVn.getSelectedRow() !=-1;
                buttonDeleteWordInList.setEnabled(selected);*/

                if (index != -1) {
                    textFieldWordInList.setText(
                            (String)tableEnToVn.getValueAt(index, 0)
                    );
                    textAreaMeaningInList.setText(
                            (String)tableEnToVn.getValueAt(index,1)
                    );
                }
            }
        });
        tableVnToEn.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int index = tableVnToEn.getSelectedRow();

                /*boolean selected = tableVnToEn.getSelectedRow() !=-1;
                buttonDeleteWordInList.setEnabled(selected);*/

                if (index != -1) {
                    textFieldWordInList.setText(
                            (String)tableVnToEn.getValueAt(index, 0)
                    );
                    textAreaMeaningInList.setText(
                            (String)tableVnToEn.getValueAt(index,1)
                    );
                }
            }
        });

        panelTableListWordDetail.add(new JScrollPane(tableEnToVn),"tableEnToVn");
        panelTableListWordDetail.add(new JScrollPane(tableVnToEn),"tableVnToEn");

        cardLayoutListWord.show(panelTableListWordDetail,"tableEnToVn");

        panelTableListWord.add(new JLabel("DICTIONARY"));
        panelTableListWord.add(panelTableListWordDetail);

    }
    private JPanel panelChooseDate;
    private JPanel panelTableStatisticalWord;
    private void initPanelStatisticalWord() {
        panelStatisticalWord = new JPanel();
        panelStatisticalWord.setLayout(new BoxLayout(panelStatisticalWord, BoxLayout.Y_AXIS));

        initPanelReturn();
        panelStatisticalWord.add(panelReturn);

        initPanelChooseDate();
        panelStatisticalWord.add(panelChooseDate);

        initPanelTableStatisticalWord();
        panelStatisticalWord.add(panelTableStatisticalWord);
    }

    private JTable tableStatisticalWord;
    private DefaultTableModel tableModelStatisticalWord;
    private JScrollPane scrollPaneTableStatisticalWord;
    private void initPanelTableStatisticalWord() {
        panelTableStatisticalWord = new JPanel();
        tableModelStatisticalWord = new DefaultTableModel();
        tableStatisticalWord = new JTable(tableModelStatisticalWord);
        scrollPaneTableStatisticalWord = new JScrollPane(tableStatisticalWord);
        panelTableStatisticalWord.add(scrollPaneTableStatisticalWord);


    }
   // private JDatePicker datePickerStart;
    private JDatePickerImpl datePickerStart;
    private JDatePickerImpl datePickerEnd;
    private String datePattern = "yyyy-MM-dd";
    private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);
    private DateLabelFormatter dateLabelFormatter;
    private JButton buttonStartStatistic;
    //private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);
    private void initPanelChooseDate() {
        panelChooseDate = new JPanel();
        panelChooseDate.setLayout(new BoxLayout(panelChooseDate, BoxLayout.X_AXIS));

        dateLabelFormatter = new DateLabelFormatter();
        Properties properties = new Properties();
        properties.put("text.today", "Today");
        properties.put("text.month", "Month");
        properties.put("text.year", "Year");

        UtilDateModel modelStartDate = new UtilDateModel();
        JDatePanelImpl datePanelStart = new JDatePanelImpl(modelStartDate,properties);
        datePickerStart = new JDatePickerImpl(datePanelStart, dateLabelFormatter);

        UtilDateModel modelEndDate = new UtilDateModel();
        JDatePanelImpl datePanelEnd = new JDatePanelImpl(modelEndDate, properties);
        datePickerEnd = new JDatePickerImpl(datePanelEnd, dateLabelFormatter);

        buttonStartStatistic = new JButton("Start Statistic");
        buttonStartStatistic.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Date dateStart = (Date) datePickerStart.getModel().getValue();
                Date dateEnd = (Date) datePickerEnd.getModel().getValue();

                if (dateStart==null || dateEnd == null ) {
                    JOptionPane.showMessageDialog(frame, "Please choose both start date and end date!");
                } else if (dateStart.after(dateEnd)) {
                    JOptionPane.showMessageDialog(frame, "Start date must before end date!");
                } else {
                   // System.out.println("Start: " + dateFormatter.format(dateStart));

                    List<String> statisticalWords = FileUtility.getSearchWordsBetweenDates(
                            dateFormatter.format(dateStart),
                            dateFormatter.format(dateEnd),
                            historyFilePath);
                    Map<String, Integer> wordCounts = WordHelper.countDuplicates(statisticalWords);

                    /*for (Map.Entry<String, Integer> entry : wordCounts.entrySet()) {
                        String word = entry.getKey();
                        int count = entry.getValue();
                        System.out.println(word + " : " + count);
                    }*/

                    tableModelStatisticalWord = initTableModelStatisticalWord(
                                                        wordCounts,
                                                        dateFormatter.format(dateStart),
                                                        dateFormatter.format(dateEnd));
                    tableModelStatisticalWord.fireTableStructureChanged();
                    tableModelStatisticalWord.fireTableDataChanged();
                    //panelTableStatisticalWord.remove(scrollPaneTableStatisticalWord);
                    tableStatisticalWord = new JTable(tableModelStatisticalWord);
                    tableStatisticalWord.getColumnModel().getColumn(1).setCellRenderer(new CellCenterRenderer());

                    scrollPaneTableStatisticalWord.setViewportView(tableStatisticalWord);
                    //panelTableStatisticalWord.add(scrollPaneTableStatisticalWord);
                }

            }
        });

        panelChooseDate.add(new JLabel("Start date: "));
        panelChooseDate.add(datePickerStart);
        panelChooseDate.add(new JLabel("End date: "));
        panelChooseDate.add(datePickerEnd);
        panelChooseDate.add(buttonStartStatistic);
    }
    private JPanel panelInput;
    private JPanel panelOutput;
    private JPanel panelChangeModel;
    private JPanel panelWordOperation;
    private void initPanelTranslation() {
        panelTranslation = new JPanel();
        panelTranslation.setLayout(new BorderLayout());

        initPanelInputOutput();
        initPanelWordOperation();


        panelTranslation.add(panelInputOutput, BorderLayout.CENTER);
        panelTranslation.add(panelWordOperation, BorderLayout.LINE_START);

    }
    private JButton buttonAddWord;
    private JButton buttonDeleteWord;
    private JButton buttonStatisticWord;
    private JButton buttonFavoritesWord;
    private JButton buttonAddToFavoritesDictionary;
    private JComboBox comboBoxTranslationDictionaryType;
    public static void printTable(JTable table) {
        TableModel model = table.getModel();
        int rowCount = model.getRowCount();
        int columnCount = model.getColumnCount();

        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                Object value = model.getValueAt(row, column);
                System.out.print(value + "\t");
            }
            System.out.println();
        }
    }
    private void sortEnToVnAsc() {
        sorterEnToVn.setComparator(0,Comparator.naturalOrder());
        sorterEnToVn.sort();
    }
    private void sortEnToVnDsc() {
        sorterEnToVn.setComparator(0,Comparator.reverseOrder());
        sorterEnToVn.sort();
    }
    private void sortVnToEnAsc() {
        sorterVnToEn.setComparator(0,new AsciiComparator(true));
        //System.out.println("sort");
        sorterVnToEn.sort();
    }
    private void sortVnToEnDsc() {
        sorterVnToEn.setComparator(0,new AsciiComparator(false));

        sorterVnToEn.sort();
    }
    private void searchAndDeleteInTable(JTable table, DefaultTableModel model, String word) {
        int rowCount = table.getRowCount();

        for (int i=0;i< rowCount;i++) {
            String cellValue = (String) table.getValueAt(i,0);
            //System.out.println(i+": "+cellValue);

            if (cellValue.equals(word)) {
                //System.out.println("Find searchAndDeleteInTable ");
                model.removeRow(i);
            }
        }
    }
    private void initPanelWordOperation() {
        panelWordOperation = new JPanel();
        panelWordOperation.setLayout(new GridLayout(0,1));

        buttonAddWord = new JButton("Add Word");
        buttonAddWord.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel,"panelAddingWord");
            }
        });

        buttonDeleteWord = new JButton("Management Dictionary");
        buttonDeleteWord.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                cardLayout.show(mainPanel,"panelListWord");
                /*String word = textFieldInput.getText();


                int selectedIndex = comboBoxTranslationDictionaryType.getSelectedIndex();

                if (selectedIndex==0) {
                    String result1 = modelEnToVn.deleteWord(word);
                    String temp = modelFavoritesEnToVn.deleteWord(word);

                    System.out.println("buttonDeleteWord: "+temp);
                    if (temp !=null) {
                        searchAndDeleteInTable(tableFavoritesWordEnToVnAsc,tableModelFavoritesEnToVn, word);
                        tableModelFavoritesEnToVn.fireTableDataChanged();
                    }

                    if (result1!=null) {
                        JOptionPane.showMessageDialog(frame, "Delete word successfully!");
                        clearInput();
                        clearOutput();
                    } else {
                        JOptionPane.showMessageDialog(frame, "Cannot find the word. Delete fail!");
                    }

                } else if (selectedIndex==1) {

                    String result2 = modelVnToEn.deleteWord(word);
                    String temp = modelFavoritesVnToEn.deleteWord(word);

                    if (temp !=null) {
                        searchAndDeleteInTable(tableFavoritesWordVnToEnAsc,tableModelFavoritesVnToEn, word);
                        tableModelFavoritesVnToEn.fireTableDataChanged();
                    }

                    if (result2!=null) {
                        JOptionPane.showMessageDialog(frame, "Delete word successfully!");
                        clearInput();
                        clearOutput();
                    } else {
                        JOptionPane.showMessageDialog(frame, "Cannot find the word. Delete fail!");
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Cannot find the word. Delete fail!");
                }*/

            }
        });

        buttonStatisticWord = new JButton("Statistic Word");
        buttonStatisticWord.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "panelStatisticalWord");
            }
        });

        buttonFavoritesWord = new JButton("Favorites Word");
        buttonFavoritesWord.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortEnToVnAsc();
                sortVnToEnAsc();
                tableModelFavoritesEnToVn.fireTableDataChanged();
                tableModelFavoritesVnToEn.fireTableDataChanged();
                //JTableHeader header = tableEnToVn.getTableHeader();

                cardLayout.show(mainPanel, "panelFavoritesWord");
            }
        });

        buttonAddToFavoritesDictionary = new JButton("Add to favorites");
        buttonAddToFavoritesDictionary.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (comboBoxTranslationDictionaryType.getSelectedIndex()==0) {
                    String input = textFieldInput.getText();
                    String meaning = modelEnToVn.getMeaning(input);
                    if (meaning!=null) {
                        String result = modelFavoritesEnToVn.addNewWord(input,meaning);
                        //System.out.println("buttonAddToFavoritesDictionary: "+result);
                        if (result==null) {
                            tableModelFavoritesEnToVn.addRow(new Object[]{input, meaning});
                            tableModelFavoritesEnToVn.fireTableDataChanged();
                        }

                        JOptionPane.showMessageDialog(frame,"Add word to favorites EN to VN Dictionary successfully!");
                    } else {
                        JOptionPane.showMessageDialog(frame,"Error! Cannot find meaning of word!");
                    }

                } else if (comboBoxTranslationDictionaryType.getSelectedIndex()==1) {
                    String input = textFieldInput.getText();
                    String meaning = modelVnToEn.getMeaning(input);
                    if (meaning!=null) {
                        String result = modelFavoritesVnToEn.addNewWord(input,meaning);

                        if (result==null) {
                            tableModelFavoritesVnToEn.addRow(new Object[]{input, meaning});
                            tableModelFavoritesVnToEn.fireTableDataChanged();
                        }

                        JOptionPane.showMessageDialog(frame,"Add word to favorites VN to EN Dictionary successfully!");
                    } else {
                        JOptionPane.showMessageDialog(frame,"Error! Cannot find meaning of word!");
                    }
                } else {
                    JOptionPane.showMessageDialog(frame,"Error! Please choose the language type!");
                }
            }
        });

        panelWordOperation.add(buttonAddWord);
        panelWordOperation.add(buttonDeleteWord);
        panelWordOperation.add(buttonStatisticWord);
        panelWordOperation.add(buttonAddToFavoritesDictionary);
        panelWordOperation.add(buttonFavoritesWord);
    }
    private void initComboBoxDictionary(JComboBox comboBox) {
        String[] type = new String[]{"English To Vietnamese","Vietnamese To English"};
        comboBox = new JComboBox(type);
    }
    private void clearInput() {
        textFieldInput.setText("");
    }
    private void clearOutput() {
        textAreaOutput.setText("");
    }
    private JPanel panelInputOutput;
    private void initPanelInputOutput() {
        panelInputOutput = new JPanel();

        //panelInputOutput.setLayout(new BoxLayout(panelInputOutput, BoxLayout.Y_AXIS));

        panelInputOutput.setLayout(new GridLayout(0,1));
        //panelInputOutput.setLayout(new BoxLayout(panelInputOutput,BoxLayout.Y_AXIS));
        initPanelInput();
        initPanelOutput();
        initPanelChangeModel();

        panelChangeModel.setMaximumSize(new Dimension(1000,200));
        panelInputOutput.add(panelInput);
        panelInputOutput.add(panelChangeModel);
        panelInputOutput.add(panelOutput);
    }
    private JTextField textFieldInput;
    private JLabel labelInput;
    private void initPanelInput() {
        panelInput = new JPanel();
        panelInput.setLayout(new BoxLayout(panelInput, BoxLayout.Y_AXIS));

        labelInput = new JLabel("Input Word");
        textFieldInput = new JTextField();

        panelInput.add(labelInput);
        panelInput.add(textFieldInput);
    }
    private JTextField textFieldOutput;
    private JLabel labelOutput;
    private JTextArea textAreaOutput;
    private void initPanelOutput() {
        panelOutput = new JPanel();
        panelOutput.setLayout(new BoxLayout(panelOutput, BoxLayout.Y_AXIS));

        labelOutput = new JLabel("Output Word");
        textFieldOutput = new JTextField();
        textFieldOutput.setEditable(false);

        textAreaOutput = new JTextArea();
        textAreaOutput.setEditable(false);
        //textAreaOutput.setPreferredSize(new Dimension(200,300));

        panelOutput.add(labelOutput);
        panelOutput.add(new JScrollPane(textAreaOutput));
    }
    private JButton buttonEnToVn;
    private JButton buttonVnToEn;
    private JButton buttonTranslate;
    private void initPanelChangeModel() {
        panelChangeModel = new JPanel();
        //panelChangeModel.setLayout(new BoxLayout(panelChangeModel, BoxLayout.X_AXIS));
        panelChangeModel.setLayout(new FlowLayout(FlowLayout.CENTER));
        //panelChangeModel.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonEnToVn = new JButton("Translate English To Vietnamese");
        buttonEnToVn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String word = textFieldInput.getText();
                //String meaning = modelEnToVn.findMeaningOfWord(word);
                String meaning = modelEnToVn.getMeaning(word);
                if (meaning==null) {
                    meaning="Cannot find the meaning of this word";
                } else {
                    FileUtility.saveSearchToHistory(word,historyFilePath);
                }
               // System.out.println(meaning);
                textAreaOutput.setText(meaning);
            }
        });
        buttonVnToEn = new JButton("Translate Vietnamese To English");
        buttonVnToEn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String word = textFieldInput.getText();
                //String meaning = modelVnToEn.findMeaningOfWord(word);
                String meaning = modelVnToEn.getMeaning(word);
                if (meaning==null) {
                    meaning="Cannot find the meaning of this word";
                } else {
                    FileUtility.saveSearchToHistory(word,historyFilePath);
                }
                // System.out.println(meaning);
                textAreaOutput.setText(meaning);
            }
        });
        //initComboBoxDictionary(comboBoxTranslationDictionaryType);
        buttonTranslate = new JButton("Translate");
        buttonTranslate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = comboBoxTranslationDictionaryType.getSelectedIndex();

                if (index == 0) {
                    String word = textFieldInput.getText();
                    //String meaning = modelEnToVn.findMeaningOfWord(word);
                    String meaning = modelEnToVn.getMeaning(word);
                    if (meaning==null) {
                        meaning="Cannot find the meaning of this word";
                    } else {
                        FileUtility.saveSearchToHistory(word,historyFilePath);
                    }
                    // System.out.println(meaning);
                    textAreaOutput.setText(meaning);
                } else if (index == 1) {
                    String word = textFieldInput.getText();
                    //String meaning = modelVnToEn.findMeaningOfWord(word);
                    String meaning = modelVnToEn.getMeaning(word);
                    if (meaning==null) {
                        meaning="Cannot find the meaning of this word";
                    } else {
                        FileUtility.saveSearchToHistory(word,historyFilePath);
                    }
                    // System.out.println(meaning);
                    textAreaOutput.setText(meaning);
                }
            }
        });
        initComboBoxTranslationDictionaryType();
        panelChangeModel.add(comboBoxTranslationDictionaryType);
        //buttonEnToVn.setAlignmentX(Component.CENTER_ALIGNMENT);
        //buttonVnToEn.setAlignmentX(Component.CENTER_ALIGNMENT);


        //panelChangeModel.add(buttonEnToVn);
        panelChangeModel.add(Box.createRigidArea(new Dimension(20, 20)));
        panelChangeModel.add(buttonTranslate);

    }
    private void initComboBoxTranslationDictionaryType() {
        String[] type = new String[]{"English To Vietnamese","Vietnamese To English"};
        comboBoxTranslationDictionaryType = new JComboBox(type);
    }
    private JPanel panelReturn;
    private JPanel panelAddingWordDetail;
    private void initPanelAddingWord() {
        panelAddingWord = new JPanel();
        panelAddingWord.setLayout(new BoxLayout(panelAddingWord, BoxLayout.Y_AXIS));

        initPanelReturn();
        panelReturn.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelReturn.setAlignmentY(Component.TOP_ALIGNMENT);

        initPanelAddingWordDetail();
        initPanelSaveCancel();

        panelAddingWord.add(panelReturn);
        panelAddingWord.add(panelAddingWordDetail);
        panelAddingWord.add(panelSaveCancel);
    }
    private JTextArea textAreaNewWord;
    private JTextArea textAreaNewMeaning;
    private JLabel labelNewWord;
    private JLabel labelNewMeaning;
    private JComboBox comboBoxWordType;
    private JComboBox comboBoxMeaningType;
    private void initPanelAddingWordDetail() {
        panelAddingWordDetail = new JPanel();
        panelAddingWordDetail.setLayout(new GridLayout(0,2));

        initComboBoxWordType();
        initComboBoxMeaningType();

        labelNewWord = new JLabel("Word:");
        labelNewMeaning = new JLabel("Meaning:");
        textAreaNewWord = new JTextArea();
        setHint(textAreaNewWord,"Enter the word here.");
        textAreaNewMeaning = new JTextArea();
        setHint(textAreaNewMeaning,"Enter the meaning here.");

        panelAddingWordDetail.add(comboBoxWordType);
        panelAddingWordDetail.add(comboBoxMeaningType);
        panelAddingWordDetail.add(labelNewWord);
        panelAddingWordDetail.add(labelNewMeaning);
        panelAddingWordDetail.add(textAreaNewWord);
        panelAddingWordDetail.add(textAreaNewMeaning);
    }
    private JButton buttonCancel;
    private JButton buttonSave;
    private JPanel panelSaveCancel;
    private void initPanelSaveCancel() {
        panelSaveCancel = new JPanel();
        panelSaveCancel.setBackground(Color.WHITE);
        panelSaveCancel.setLayout(new BoxLayout(panelSaveCancel,BoxLayout.X_AXIS));

        buttonSave = getButtonSave();
        buttonCancel = getButtonCancel();

        panelSaveCancel.add(buttonSave);
        panelSaveCancel.add(buttonCancel);
    }
    private void clearForm() {
        textAreaNewWord.setText("");
        textAreaNewMeaning.setText("");
    }
    private JButton getButtonCancel() {
        JButton buttonCancel = new JButton("Cancel");
        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
                cardLayout.show(mainPanel,"panelTranslation");
            }
        });
        return buttonCancel;
    }
    private JButton getButtonSave() {
        //System.out.println("row: " +selectedRowTable);
        JButton buttonSave = new JButton("Save");
        buttonSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

               String word = textAreaNewWord.getText();
               String meaning = textAreaNewMeaning.getText();
               if (comboBoxWordType.getSelectedIndex()==0 && comboBoxWordType.getSelectedIndex()==0) {
                   modelEnToVn.addNewWord(word, meaning);
                   modelVnToEn.addNewWord(meaning, word);
                   JOptionPane.showMessageDialog(frame, "New word has been added to dictionary successfully!");
                   clearForm();
               } else if (comboBoxWordType.getSelectedIndex()==1 && comboBoxWordType.getSelectedIndex()==1) {
                   modelEnToVn.addNewWord(meaning, word);
                   modelVnToEn.addNewWord(word, meaning);
                   JOptionPane.showMessageDialog(frame, "New word has been added to dictionary successfully!");
                   clearForm();
               } else {
                   JOptionPane.showMessageDialog(frame, "Invalid input. Please choose language of word different language of meaning");
               }

                //cardLayout.show(mainPanel, "panelTranslation");

            }

        });
        return buttonSave;
    }
    public static void setHint(JTextArea textArea, String hint) {
        Font originalFont = textArea.getFont();
        Font italicFont = originalFont.deriveFont(Font.ITALIC);

        textArea.setText(hint);
        textArea.setForeground(Color.GRAY);
        textArea.setFont(italicFont);

        textArea.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (textArea.getText().equals(hint)) {
                    textArea.setText("");
                    textArea.setForeground(Color.BLACK);
                    textArea.setFont(originalFont);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (textArea.getText().isEmpty()) {
                    textArea.setText(hint);
                    textArea.setForeground(Color.GRAY);
                    textArea.setFont(italicFont);
                }
            }
        });
    }
    private void initComboBoxWordType() {
        String[] type = new String[]{"English","Vietnamese"};
        comboBoxWordType = new JComboBox(type);
        comboBoxWordType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = comboBoxWordType.getSelectedIndex();

                if (index == 0 ) {
                    comboBoxMeaningType.setSelectedIndex(0);
                } else if (index == 1) {
                    comboBoxMeaningType.setSelectedIndex(1);
                }
            }
        });
    }
    private void initComboBoxMeaningType() {
        String[] type = new String[]{"Vietnamese","English"};
        comboBoxMeaningType = new JComboBox(type);
        comboBoxMeaningType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = comboBoxMeaningType.getSelectedIndex();

                if (index == 0 ) {
                    comboBoxWordType.setSelectedIndex(0);
                } else if (index == 1) {
                    comboBoxWordType.setSelectedIndex(1);
                }
            }
        });
    }
    private void initPanelReturn() {
        panelReturn = new JPanel();
        panelReturn.setLayout(new BoxLayout(panelReturn, BoxLayout.X_AXIS));
        panelReturn.setBackground(Color.WHITE);
        JButton buttonReturn = new JButton("Return");
        buttonReturn.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonReturn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show (mainPanel,"panelTranslation");
            }
        });
        panelReturn.add(buttonReturn);
    }
    private JButton buttonDislike;
    private JPanel panelFavoritesWordOperation;
    private void initPanelFavoritesWord() {
        panelFavoritesWord = new JPanel();
        //panelFavoritesWord.setLayout(new BoxLayout(panelFavoritesWord,BoxLayout.Y_AXIS));
        panelFavoritesWord.setLayout(new BorderLayout());

        initPanelReturn();
        panelFavoritesWord.add(panelReturn,BorderLayout.PAGE_START);

        initPanelFavoritesWordOperation();
        panelFavoritesWord.add(panelFavoritesWordOperation,BorderLayout.LINE_START);


        initPanelTableFavoritesWord();
        panelFavoritesWord.add(panelTableFavoritesWord, BorderLayout.CENTER);
    }
    private JTextField textFieldFavoritesWord;
    private JTextArea textAreaFavoritesMeaning;
    private void clearFavoritesTextField() {
        textFieldFavoritesWord.setText("");
        textAreaFavoritesMeaning.setText("");
    }
    private void initPanelFavoritesWordOperation() {
        panelFavoritesWordOperation = new JPanel();
        panelFavoritesWordOperation.setLayout(new BoxLayout(panelFavoritesWordOperation,BoxLayout.Y_AXIS));

        initPanelSortDictionary();
        panelFavoritesWordOperation.add(panelSortDictionary);
        panelSortDictionary.setMaximumSize(new Dimension(300,70));

        buttonDislike = new JButton("Dislike");
        buttonDislike.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = comboBoxDictionaryType.getSelectedIndex();

                if (index==0) {
                    int row = tableFavoritesWordEnToVnAsc.getSelectedRow();
                    // System.out.println("row:"+row);
                    if (row!=-1) {
                        modelFavoritesEnToVn.deleteWord(
                                (String) tableFavoritesWordEnToVnAsc.getValueAt(row,0)
                        );
                        //tableFavoritesWordEnToVnAsc.removeRowSelectionInterval(row, row);
                        int selectedRowModel = tableFavoritesWordEnToVnAsc.convertRowIndexToModel(row);
                        tableModelFavoritesEnToVn.removeRow(selectedRowModel);
                        tableModelFavoritesEnToVn.fireTableDataChanged();
                        ((RowSorter) tableFavoritesWordEnToVnAsc.getRowSorter()).modelStructureChanged();
                        clearFavoritesTextField();
                    }
                }   else if (index == 1) {
                    int row = tableFavoritesWordVnToEnAsc.getSelectedRow();
                    //System.out.println("row:"+row);
                    if (row!=-1) {
                        modelFavoritesVnToEn.deleteWord(
                                (String) tableFavoritesWordVnToEnAsc.getValueAt(row,0)
                        );
                        int selectedRowModel = tableFavoritesWordVnToEnAsc.convertRowIndexToModel(row);
                        //tableFavoritesWordVnToEnAsc.removeRowSelectionInterval(row,row);

                        tableModelFavoritesVnToEn.removeRow(selectedRowModel);
                        tableModelFavoritesVnToEn.fireTableDataChanged();
                        ((RowSorter) tableFavoritesWordVnToEnAsc.getRowSorter()).modelStructureChanged();
                        clearFavoritesTextField();
                    }
                } else {
                    JOptionPane.showMessageDialog(frame,"There are some errors occurred!");
                }
            }
        });
        panelFavoritesWordOperation.add(buttonDislike);

        textFieldFavoritesWord = new JTextField();
        textFieldFavoritesWord.setEditable(false);
        textFieldFavoritesWord.setMaximumSize(new Dimension(300,70));
        panelFavoritesWordOperation.add(textFieldFavoritesWord);

        textAreaFavoritesMeaning = new JTextArea();
        textAreaFavoritesMeaning.setEditable(false);
        panelFavoritesWordOperation.add(new JScrollPane(textAreaFavoritesMeaning));
    }

    private JPanel panelTableFavoritesWord;
    private CardLayout cardLayoutFavoritesWord;
    TableRowSorter<DefaultTableModel> sorterEnToVn;
    TableRowSorter<DefaultTableModel> sorterVnToEn;
    private JTable tableFavoritesWordEnToVnAsc;
    private JTable tableFavoritesWordEnToVnDsc;
    private JTable tableFavoritesWordVnToEnAsc;
    private JTable tableFavoritesWordVnToEnDsc;
    private DefaultTableModel tableModelFavoritesEnToVn;
    private DefaultTableModel tableModelFavoritesVnToEn;
    private void initPanelTableFavoritesWord() {
        panelTableFavoritesWord = new JPanel();
        cardLayoutFavoritesWord= new CardLayout();
        panelTableFavoritesWord.setLayout(cardLayoutFavoritesWord);


        tableModelFavoritesEnToVn = initTableFavoritesWordAsc( modelFavoritesEnToVn.getTreeMapRecord());
        tableFavoritesWordEnToVnAsc = new JTable(tableModelFavoritesEnToVn);
        sorterEnToVn = new TableRowSorter<>(tableModelFavoritesEnToVn);
        tableFavoritesWordEnToVnAsc.setRowSorter(sorterEnToVn);
        tableFavoritesWordEnToVnAsc.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int index = tableFavoritesWordEnToVnAsc.getSelectedRow();

                if (index!=-1) {
                    textFieldFavoritesWord.setText(
                            (String)tableFavoritesWordEnToVnAsc.getValueAt(index,0)
                    );
                    textAreaFavoritesMeaning.setText(
                            (String)tableFavoritesWordEnToVnAsc.getValueAt(index,1)
                    );
                }
            }
        });

        tableModelFavoritesVnToEn = initTableFavoritesWordAsc(modelFavoritesVnToEn.getTreeMapRecord());
        tableFavoritesWordVnToEnAsc = new JTable(tableModelFavoritesVnToEn);
        sorterVnToEn = new TableRowSorter<>(tableModelFavoritesVnToEn);
        tableFavoritesWordVnToEnAsc.setRowSorter(sorterVnToEn);
        tableFavoritesWordVnToEnAsc.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int index = tableFavoritesWordVnToEnAsc.getSelectedRow();

                if (index!=-1) {
                    textFieldFavoritesWord.setText(
                            (String)tableFavoritesWordVnToEnAsc.getValueAt(index,0)
                    );
                    textAreaFavoritesMeaning.setText(
                            (String)tableFavoritesWordVnToEnAsc.getValueAt(index,1)
                    );
                }
            }
        });

        panelTableFavoritesWord.add(new JScrollPane(tableFavoritesWordEnToVnAsc),"tableFavoritesWordEnToVnAsc");
        panelTableFavoritesWord.add(new JScrollPane(tableFavoritesWordVnToEnAsc),"tableFavoritesWordVnToEnAsc");

        cardLayoutFavoritesWord.show(panelTableFavoritesWord,"tableFavoritesWordEnToVnAsc");
    }
    private DefaultTableModel initTableFavoritesWordAsc(TreeMap<String, String> treeMap) {
        ArrayList<Map.Entry<String, String>> sortedAscending = new ArrayList<>(treeMap.entrySet());
        sortedAscending.sort(Comparator.comparing(Map.Entry::getKey));

        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make all cells non-editable
                return false;
            }
        };
        model.addColumn("Word");
        model.addColumn("Meaning");

        for(Map.Entry<String, String> entry : sortedAscending) {
            model.addRow(
                    new Object[]{entry.getKey(), entry.getValue()}
            );
        }
        //table.setModel(model);
        //table = new JTable(model);
        return model;
    }
    private DefaultTableModel initTableModelStatisticalWord(Map<String, Integer> mapWords, String startDate, String endDate) {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Word");
        model.addColumn("Start:" +startDate);
        model.addColumn("End: " +endDate);

        for (Map.Entry<String, Integer> entry : mapWords.entrySet()) {
            model.addRow(
                    new Object[]{entry.getKey(),entry.getValue()+" time(s)",""}
            );
            //System.out.println(entry.getKey()+" - " +entry.getValue());
        }

        return model;
    }
    private DefaultTableModel initTableFavoritesWordDsc(JTable table, TreeMap<String, String> treeMap) {
        ArrayList<Map.Entry<String, String>> sortedAscending = new ArrayList<>(treeMap.entrySet());
        sortedAscending.sort(Comparator.comparing(Map.Entry::getKey, Comparator.reverseOrder()));

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Word");
        model.addColumn("Meaning");

        for(Map.Entry<String, String> entry : sortedAscending) {
            model.addRow(
                    new Object[]{entry.getKey(), entry.getValue()}
            );
        }

        table = new JTable(model);

        return model;
    }
    private JPanel panelSortDictionary;
    private JComboBox comboBoxSortOrder;
    private JComboBox comboBoxDictionaryType;
    private JLabel labelSortOrder;
    private JLabel labelDictionaryType;
    private void initPanelSortDictionary() {
        panelSortDictionary = new JPanel();
        panelSortDictionary.setLayout(new BoxLayout(panelSortDictionary, BoxLayout.X_AXIS));

        initComboBoxSortOrder();
        comboBoxSortOrder.setAlignmentX(0.5f);

        intiComboBoxDictionaryType();
        comboBoxDictionaryType.setAlignmentX(0.5f);

        labelSortOrder = new JLabel("Order by: ");
        labelSortOrder.setAlignmentX(0.5f);
        labelDictionaryType = new JLabel("Dictionary: ");
        labelDictionaryType.setAlignmentX(0.5f);

        panelSortDictionary.add(labelDictionaryType);
        panelSortDictionary.add(comboBoxDictionaryType);
        /*panelSortDictionary.add(labelSortOrder);
        panelSortDictionary.add(comboBoxSortOrder);*/

    }
    private void initComboBoxSortOrder() {
        String[] order = new String[]{"A-Z","Z-A"};
        comboBoxSortOrder = new JComboBox(order);
        comboBoxSortOrder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = comboBoxSortOrder.getSelectedIndex();

               // System.out.println("index: "+selectedIndex);
                if (selectedIndex==1) {
                    sortEnToVnDsc();
                    sortVnToEnDsc();
                } else if (selectedIndex==0) {
                    sortEnToVnAsc();
                    sortVnToEnAsc();
                }
            }
        });

    }
    private void intiComboBoxDictionaryType() {
        String[] type = new String[]{"English To Vietnamese","Vietnamese To English"};
        comboBoxDictionaryType = new JComboBox(type);
        comboBoxDictionaryType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = comboBoxDictionaryType.getSelectedIndex();

                if (selectedIndex==0) {
                    cardLayoutFavoritesWord.show(panelTableFavoritesWord,"tableFavoritesWordEnToVnAsc");
                } else if (selectedIndex==1) {
                    cardLayoutFavoritesWord.show(panelTableFavoritesWord,"tableFavoritesWordVnToEnAsc");
                }
            }
        });
    }
}
