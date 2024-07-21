import jdk.jshell.spi.ExecutionControl;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class LanguageTranslationModel {
    TreeMap<String, String> treeMapRecord;
    public TreeMap<String, String> getTreeMapRecord() {
        return treeMapRecord;
    }
    public void setTreeMapRecord(TreeMap<String, String> treeMapRecord) {
        this.treeMapRecord = treeMapRecord;
    }
    public LanguageTranslationModel(TreeMap<String, String> treeMapRecord) {
        this.treeMapRecord = treeMapRecord;
    }
    public LanguageTranslationModel() {
        treeMapRecord = new TreeMap<>();
    }
    public String addNewWord(String word, String meaning) {
        return this.treeMapRecord.put(word, meaning);
    }

    public String deleteWord(String word) {
        return treeMapRecord.remove(word);
    }

    public String getMeaning(String word) {
        return treeMapRecord.get(word);
    }
    public String getWordMeaning(String word) {
        return null;
    }

    public void printTop10Word() {
        int count = 0;
        for (Map.Entry<String, String> entry : treeMapRecord.entrySet()) {
            if (count == 10) {
                break;
            }
            String word = entry.getKey();
            String meaning = entry.getValue();
            System.out.println("Record{word: "+word + ", meaning: "+meaning+"}");
            count++;
        }

    }

    public String findMeaningOfWord(String text) {
        //throw new ExecutionControl.NotImplementedException("You need to implement this function in subclass");
        return "You need to implement this function in subclass";
    }
}
