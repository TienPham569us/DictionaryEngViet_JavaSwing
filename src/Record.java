public class Record {
    private String word;
    private String meaning;

    public Record(String word, String meaning) {
        this.word = word;
        this.meaning = meaning;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getWord() {
        return word;
    }

    public String getMeaning() {
        return meaning;
    }

    @Override
    public String toString() {
        return "Record{word: "+word+", meaning: "+meaning+"}";
    }
}
