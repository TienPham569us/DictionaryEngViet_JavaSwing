import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordHelper {
    public static String unicodeToASCII(String s) {
        String s1 = Normalizer.normalize(s, Normalizer.Form.NFKD);
        String regex =
                "[\\p{InCombiningDiacriticalMarks}\\p{IsLm}\\p{IsSk}]+";
        String s2 = null;
        try {
            s2 = new String(s1.replaceAll(regex, "").getBytes("ascii"),
                    "ascii");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
        return s2;
    }
    public static Map<String, Integer> countDuplicates(List<String> words) {
        Map<String, Integer> wordCounts = new HashMap<>();

        for (String word : words) {
            wordCounts.put(word, wordCounts.getOrDefault(word, 0) + 1);
        }

        return wordCounts;
    }
}
