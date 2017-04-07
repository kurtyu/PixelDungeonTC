package net.whitegem.pixeldungeon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * 儲存翻譯字典的HashMap
 */
public class Translator
{
    private String language;
    private HashMap<String, String> translation;

    public Translator(String language)
    {
        this.language = language;
        translation = new HashMap<>();
        // THIS FILE MUST BE UTF8 WITHOUT BOM
        FileHandle file = Gdx.files.internal("translation/" + language + ".txt");
        BufferedReader reader = new BufferedReader(file.reader("UTF8"));
        ArrayList<String> lines = new ArrayList<>();
        try
        {
            String line = reader.readLine();
            while (line != null)
            {
                // 過濾掉註解#
                if (!line.trim().equals("") && !line.trim().startsWith("#"))
                {
                    lines.add(line.trim());
                }
                line = reader.readLine();
            }
            reader.close();
        }
        catch (IOException ioe)
        {
        }

        // 檢查：翻譯ㄧ定要成對出現
        if (lines.size() % 2 != 0)
        {
            Gdx.app.log("Translator", "ERROR READING FILE" + " translation/" + language + ".txt", new Exception("Lines of original texts and translated texts in the translation file do not match."));
        }

        for (int i = 0; i < lines.size(); i += 2)
        {
            String trans = lines.get(i + 1);
            Gdx.app.log("Translator", lines.get(i).toLowerCase() + " => 加入翻譯 => " + trans + "\n");
            translation.put(lines.get(i).toLowerCase(), trans);
        }
    }

    private static boolean isChinese(char c)
    {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION)
        {
            return true;
        }
        return false;
    }

    public String[] splitWords(String paragraph)
    {
        if (language.equals("en"))
        {
            String[] pre = Pattern.compile("\\s+").split(paragraph);
            ArrayList<String> tmp = new ArrayList<String>();
            for (String s : pre)
            {
                tmp.add(s);
                tmp.add(" ");
            }
            return tmp.toArray(new String[tmp.size()]);
        }

        ArrayList<String> words = new ArrayList<String>();
        int start = 0;
        int end = 1;
        while (start != paragraph.length())
        {
            String str = paragraph.substring(start, end);
            if (str.length() == 1 && (isChinese(str.charAt(0)) || str.equals(" ")))
            {
                words.add(str);
                start++;
                end++;
            }
            else
            {
                if (end == paragraph.length())
                {
                    words.add(str);//.trim());
                    start = end;
                    end++;
                }
                else
                {
                    String endStr = paragraph.substring(end - 1, end);
                    String endStrNext = paragraph.substring(end, end + 1);
                    if (endStr.equals(" ") || (endStr.matches("\\p{P}") && !endStrNext.matches("[0-9]")) || (endStr.matches("[0-9]") && (!endStrNext.matches("[0-9]") && !endStrNext.matches("\\p{P}"))))
                    {
                        words.add(str);
                        start = end;
                    }
                    end++;
                }
            }
        }
        return words.toArray(new String[words.size()]);
        // return Pattern.compile("\\s+").split(paragraph);
    }

    public boolean hasKey(String key)
    {
        return translation.containsKey(key.toLowerCase());
    }

    public String translate(String originalText)
    {
        if (hasKey(originalText))
        {
            return translation.get(originalText.toLowerCase());
        }
        return originalText;
    }
}
