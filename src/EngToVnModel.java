import java.util.ArrayList;
import java.util.TreeMap;

public class EngToVnModel extends LanguageTranslationModel{
    public EngToVnModel() {
        super();
    }

    public EngToVnModel(TreeMap<String, String> treeMapRecord) {
        super(treeMapRecord);
    }

    @Override
    public String findMeaningOfWord(String text) {

        String meaning = null;

        meaning = getMeaning(text);
        //System.out.println(meaning);
        if (meaning == null) {

           meaning = "Not found meaning of word";
        }
        return meaning;
    }

}
