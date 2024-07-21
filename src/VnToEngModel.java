import java.util.ArrayList;
import java.util.TreeMap;

public class VnToEngModel extends LanguageTranslationModel {

    public VnToEngModel(TreeMap<String, String> treeMapRecord) {
        super(treeMapRecord);
    }

    public VnToEngModel() {
        super();
    }

    @Override
    public String findMeaningOfWord(String text) {
        String meaning=null;

        /*System.out.println(
                WordHelper.unicodeToASCII(
                        text.toLowerCase()
                )
        );
        for (int i=0;i<10;i++) {
            System.out.println(
                    WordHelper.unicodeToASCII(
                            arrayListRecord.get(i).getWord().toLowerCase()
                    )
            );
        }*/
        /*for (int i=0; i<arrayListRecord.size(); i++) {
            String currentWord = arrayListRecord.get(i).getWord();

            if (WordHelper.unicodeToASCII(currentWord).toLowerCase().startsWith(
                    WordHelper.unicodeToASCII(text).toLowerCase()
            )) {
                meaning = arrayListRecord.get(i).getMeaning();
                break;
            }

        }*/

        meaning = getMeaning(text);
        //System.out.println(meaning);
        if (meaning==null) {
            meaning = "Không tìm thấy nghĩa của từ";
        }
        return meaning;
    }
}
