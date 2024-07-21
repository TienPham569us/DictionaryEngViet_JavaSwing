import java.util.Comparator;

public class AsciiComparator implements Comparator<String> {
    private final boolean ascending;

    public AsciiComparator(boolean ascending) {
        this.ascending = ascending;
    }

    @Override
    public int compare(String s1, String s2) {
        String str1 = WordHelper.unicodeToASCII(s1);
        String str2 = WordHelper.unicodeToASCII(s2);
        int result =  str1.compareToIgnoreCase(str2);
        return ascending ? result : -result;
    }
}
