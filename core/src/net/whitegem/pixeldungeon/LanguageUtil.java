package net.whitegem.pixeldungeon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.watabou.noosa.BitmapText;
import com.watabou.pixeldungeon.scenes.PixelScene;
import com.watabou.utils.BitmapCache;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Carl-Station on 01/22/15.
 */
public class LanguageUtil
{
    private static String code = "en";
    private static HashMap<String, FontSettings> fonts = new HashMap<String, FontSettings>();

    private static class FontSettings
    {
        protected String fnt;
        protected String png;
        protected float baseline = -1;
        protected int scale = -1;
        public FontSettings(String line)
        {
            String[] pieces = line.split(",");
            float b = -1;
            int s = 1;
            String f = "";
            String p = "";
            switch (pieces.length)
            {
                case 4:
                    p = pieces[3];
                    s = Integer.parseInt(pieces[2]);
                case 2:
                    b = Float.parseFloat(pieces[1]); // baseline
                case 1:
                    f = pieces[0]; // filename
            }
            fnt = "translation/" + f + ".fnt";
            png = p.equals("") ? "translation/" + f + ".png" : "translation/" + p + ".png";
            baseline = b;
            scale = s;
        }
        public float getBaseline(float def)
        {
            return baseline == -1 ? def : baseline;
        }
    }
    private static void getCode()
    {
        fonts.clear();
        if (Gdx.files.internal("translation/language.settings").exists())
        {
            FileHandle file = Gdx.files.internal("translation/language.settings");
            BufferedReader reader = new BufferedReader(file.reader("UTF8"));
            ArrayList<String> lines = new ArrayList<>();
            try
            {
                String line = reader.readLine();
                while (line != null)
                {
                    if (!line.trim().equals("") && !line.trim().startsWith("#"))
                    {
                        lines.add(line.trim());
                    }
                    line = reader.readLine();
                }
            } catch (IOException ioe)
            {
            }

            HashMap<String, String> settings = new HashMap<String, String>();
            if (lines.size() > 0)
            {
                for (String line : lines)
                {
                    String[] pieces = line.split("=");
                    if (pieces.length == 2)
                    {
                        settings.put(pieces[0].toLowerCase(), pieces[1]);
                    }
                }

                if (settings.containsKey("lang") && settings.containsKey("font1x") && settings.containsKey("font15x")
                        && settings.containsKey("font2x") && settings.containsKey("font25x") && settings.containsKey("font3x"))
                {
                    code = settings.get("lang");
                    fonts.put("font1x", new FontSettings(settings.get("font1x")));
                    fonts.put("font15x", new FontSettings(settings.get("font15x")));
                    fonts.put("font2x", new FontSettings(settings.get("font2x")));
                    fonts.put("font25x", new FontSettings(settings.get("font25x")));
                    fonts.put("font3x", new FontSettings(settings.get("font3x")));
                }
            }
        }
    }
    public static void setLanguage()
    {
        getCode();
        LanguageFactory.INSTANCE.setLanguage(code);

        if (!code.equals("en"))
        {
            // 3x5 (6)
            PixelScene.font1x = BitmapText.Font.colorMarked(
                    BitmapCache.get(fonts.get("font1x").png), 0x00000000, BitmapText.Font.LATIN_FULL, fonts.get("font1x").fnt, fonts.get("font1x").scale);
            PixelScene.font1x.baseLine = fonts.get("font1x").getBaseline(7);
            PixelScene.font1x.tracking = -1;

            // 5x8 (10)
            PixelScene.font15x = BitmapText.Font.colorMarked(
                    BitmapCache.get(fonts.get("font15x").png), 0x00000000, BitmapText.Font.LATIN_FULL, fonts.get("font15x").fnt, fonts.get("font15x").scale);
            PixelScene.font15x.baseLine = fonts.get("font15x").getBaseline(10);
            PixelScene.font15x.tracking = -1;

            // 6x10 (12)
            PixelScene.font2x = BitmapText.Font.colorMarked(
                    BitmapCache.get(fonts.get("font2x").png), 0x00000000, BitmapText.Font.LATIN_FULL, fonts.get("font2x").fnt, fonts.get("font2x").scale);
            PixelScene.font2x.baseLine = fonts.get("font2x").getBaseline(12);
            PixelScene.font2x.tracking = -1;

            // 7x12 (15)
            PixelScene.font25x = BitmapText.Font.colorMarked(
                    BitmapCache.get(fonts.get("font25x").png), 0x00000000, BitmapText.Font.LATIN_FULL, fonts.get("font25x").fnt, fonts.get("font25x").scale);
            PixelScene.font25x.baseLine = fonts.get("font25x").getBaseline(18);
            PixelScene.font25x.tracking = -1;

            // 9x15 (18)
            PixelScene.font3x = BitmapText.Font.colorMarked(
                    BitmapCache.get(fonts.get("font3x").png), 0x00000000, BitmapText.Font.LATIN_FULL, fonts.get("font3x").fnt, fonts.get("font3x").scale);
            PixelScene.font3x.baseLine = fonts.get("font3x").getBaseline(22);
            PixelScene.font3x.tracking = -1;
        }
    }
}
