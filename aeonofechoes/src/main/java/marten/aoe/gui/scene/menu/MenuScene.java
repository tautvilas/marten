package marten.aoe.gui.scene.menu;

import java.awt.Font;

import marten.age.core.AgeScene;
import marten.age.core.AppInfo;
import marten.age.graphics.flat.Flatland;
import marten.age.graphics.primitives.Dimension;
import marten.age.graphics.primitives.Point;
import marten.age.graphics.text.BitmapFont;
import marten.age.graphics.text.BitmapString;
import marten.age.graphics.text.FontCache;

public abstract class MenuScene extends AgeScene {

    protected Flatland flatland = new Flatland();

    public MenuScene() {
        BitmapFont titleFont = FontCache.getFont(new Font("Arial", Font.PLAIN,
                50));

        BitmapString title = new BitmapString(titleFont, "Aeon Of Echoes");

        Dimension dApp = AppInfo.getDisplayDimension();
        Dimension dTitle = title.getDimension();
        title.setPosition(new Point(dApp.width / 2 - dTitle.width / 2,
                dApp.height / 2 + dTitle.height * 3));

        flatland.addChild(title);
    }

}
