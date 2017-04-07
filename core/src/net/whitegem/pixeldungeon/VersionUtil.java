package net.whitegem.pixeldungeon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Game;
import com.watabou.pixeldungeon.Assets;
import com.watabou.pixeldungeon.scenes.PixelScene;
import com.watabou.pixeldungeon.ui.Icons;
import com.watabou.pixeldungeon.windows.WndError;
import com.watabou.pixeldungeon.windows.WndTitledMessage;
import com.watabou.utils.BitmapCache;

/**
 * Created by Carl-Station on 01/25/15.
 */
public class VersionUtil
{
    public static boolean shown = false;

    public static int checkState = 0; // 0 = unchecked, 1 = checking, 2 = checkFailed, 3 = checkPassed

    public static void check()
    {
        if (shown)
        {
            return;
        }

        if (checkState == 5)
        {
            Game.scene().add(new WndTitledMessage(Icons.ALERT.get(), "Information", "You are using the latest beta version."));
            shown = true;
            return;
        }

        if (checkState == 0)
        {
            checkGithubIsLatest();
        }
        else
        {
            switch (checkState)
            {
                case 1: // checking
                    break;
                case 2: // failed
                    Game.scene().add(new WndError("This version of Chinese translation has expired.  You'll be switched to the English version."));
                    setEng();
                    shown = true;
                    break;
                case 3: // failed
                    Game.scene().add(new WndError("Internet access failed.  You'll be switched to the English version."));
                    setEng();
                    shown = true;
                    break;
                case 4: // cancelled
                    Game.scene().add(new WndError("Internet access cancelled.  You'll be switched to the English version."));
                    setEng();
                    shown = true;
                    break;
            }
        }
    }

    private static void setEng()
    {
        LanguageFactory.INSTANCE.setLanguage("en");
        // 3x5 (6)
        PixelScene.font1x = BitmapText.Font.colorMarked(
                BitmapCache.get(Assets.FONTS1X), 0x00000000, BitmapText.Font.LATIN_FULL);
        PixelScene.font1x.baseLine = 6;
        PixelScene.font1x.tracking = -1;

        // 5x8 (10)
        PixelScene.font15x = BitmapText.Font.colorMarked(
                BitmapCache.get(Assets.FONTS15X), 12, 0x00000000, BitmapText.Font.LATIN_FULL);
        PixelScene.font15x.baseLine = 9;
        PixelScene.font15x.tracking = -1;

        // 6x10 (12)
        PixelScene.font2x = BitmapText.Font.colorMarked(
                BitmapCache.get(Assets.FONTS2X), 14, 0x00000000, BitmapText.Font.LATIN_FULL);
        PixelScene.font2x.baseLine = 11;
        PixelScene.font2x.tracking = -1;

        // 7x12 (15)
        PixelScene.font25x = BitmapText.Font.colorMarked(
                BitmapCache.get(Assets.FONTS25X), 17, 0x00000000, BitmapText.Font.LATIN_FULL);
        PixelScene.font25x.baseLine = 13;
        PixelScene.font25x.tracking = -1;

        // 9x15 (18)
        PixelScene.font3x = BitmapText.Font.colorMarked(
                BitmapCache.get(Assets.FONTS3X), 22, 0x00000000, BitmapText.Font.LATIN_FULL);
        PixelScene.font3x.baseLine = 17;
        PixelScene.font3x.tracking = -2;
    }

    private static void checkGithubIsLatest()
    {
        checkState = 1;

        String version = Game.version;
        if (version.equals("???"))
        {
            checkState = 5;
            return;
        }

        Net.HttpRequest httpGet = new Net.HttpRequest(Net.HttpMethods.GET);
        httpGet.setUrl("https://api.github.com/repos/WiseClock/localized-pixel-dungeon-gdx/releases");

        Gdx.net.sendHttpRequest(httpGet, new Net.HttpResponseListener()
        {
            public void handleHttpResponse(Net.HttpResponse httpResponse)
            {
                String result = httpResponse.getResultAsString();
                if (result == null)
                {
                    checkState = 3;
                    return;
                }
                JsonValue root = new JsonReader().parse(result);
                String versionNewest = root.get(0).getString("tag_name");
                if (Game.version.equals(versionNewest))
                {
                    checkState = 5;
                    return;
                }
                else
                {
                    checkState = 2;
                    return;
                }
            }
            public void failed(Throwable t)
            {
                checkState = 3;
            }
            public void cancelled()
            {
                checkState = 4;
            }
        });
    }
}
