import java.util.ArrayList;

public class LanguageTranslationModelArrayList {
    ArrayList<Record> arrayListRecord;
    public ArrayList<Record> getArrayListRecord() {
        return arrayListRecord;
    }

    public void setArrayListRecord(ArrayList<Record> arrayListRecord) {
        this.arrayListRecord = arrayListRecord;
    }

    public LanguageTranslationModelArrayList(ArrayList<Record> arrayListRecord) {
        this.arrayListRecord = arrayListRecord;
    }

    public LanguageTranslationModelArrayList() {
        arrayListRecord = null;
    }

    public void addNewWord(Record record) {
        this.arrayListRecord.add(record);
    }

    public void deleteWord(Record record) {
        this.arrayListRecord.remove(record);
    }

    public Record getWord(int index) {
        return arrayListRecord.get(index);
    }

    public void printTop10Word() {
        for (int i=0; i<10;i++) {
            System.out.println(getWord(i).toString());
        }
    }

    public String findMeaningOfWord(String text) {
        //throw new ExecutionControl.NotImplementedException("You need to implement this function in subclass");
        return "You need to implement this function in subclass";
    }
}
