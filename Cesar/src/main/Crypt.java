package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Crypt {
    public static Character[] letters = {
            'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 
            'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я', '.', ',', '"', '\'', ':', '-', '!', '?', ' '
    };

    public static List<Character> lettersList = Arrays.asList(letters);
    
    public static String encrypt(String source, int key) {
        List<Character> result = new ArrayList<>();

        for (char chr : source.toCharArray()) {
            if (lettersList.contains(chr)) {
                int index = lettersList.indexOf(chr);
                result.add(lettersList.get((index + key) % letters.length));
            } else {
                result.add(chr);
            }
        }

        return toString(result);
    }

    public static String uncrypt(String source, int key) {
        List<Character> result = new ArrayList<>();

        for (char chr : source.toCharArray()) {
            if (lettersList.contains(chr)) {
                int index = lettersList.indexOf(chr);
                result.add(lettersList.get((index - key < 0 ? letters.length - (key - index) : index - key)));
            } else {
                result.add(chr);
            }
        }

        return toString(result);
    }

    static String toString(List<Character> list) {
        StringBuilder br = new StringBuilder(list.size());
        for (Character c : list) {
            br.append(c);
        }
        return br.toString();
    }

    public static int checkResult(String source) {
        int issues = 0;

        if (source.indexOf(' ') == -1 && source.length() >= 25) {
            return 1000;
        }

        String[] parts = source.split(" ");
        for (String word : parts) {
            if (word.length() > 25) issues++;
        }

        int singleQuotesCount = 0, doubleQuotesCount = 0;

        for (int i = 0; i < source.length() - 1; i++) {
            if (source.charAt(i) == ',' && source.charAt(i + 1) != ' ') {
                issues++;
            }

            if (source.charAt(i) == '?' && (source.charAt(i + 1) != ' ' && source.charAt(i + 1) != '?')) {
                issues++;
            }

            if (source.charAt(i) == '!' && (source.charAt(i + 1) != ' ' && source.charAt(i + 1) != '!')) {
                issues++;
            }

            if (source.charAt(i) == '\'') {
                singleQuotesCount++;
            }

            if (source.charAt(i) == '"') {
                doubleQuotesCount++;
            }
        }

        if (singleQuotesCount % 2 != 0) issues += 5;
        if (doubleQuotesCount % 2 != 0) issues += 5;

        return issues;
    }
}
