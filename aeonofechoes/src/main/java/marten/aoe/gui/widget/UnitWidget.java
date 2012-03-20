package marten.aoe.gui.widget;

import java.awt.Font;

import marten.age.graphics.appearance.Color;
import marten.age.graphics.layout.BoxedObject;
import marten.age.graphics.primitives.Dimension;
import marten.age.graphics.primitives.Point;
import marten.age.graphics.text.BitmapFont;
import marten.age.graphics.text.BitmapString;
import marten.age.graphics.text.FontCache;
import marten.age.graphics.transform.TranslationGroup;
import marten.age.widget.Widget;
import marten.aoe.data.type.UnitType;
import marten.aoe.dto.UnitDTO;

public class UnitWidget extends TranslationGroup implements Widget, BoxedObject {

    private BitmapString representation;
    private BitmapString movePoints;
    private BitmapString hp;
    // private UnitDTO unit;
    private final BitmapFont font = FontCache.getFont(new Font("Courier New",
            Font.BOLD, 20));
    private final BitmapFont smallFont = FontCache.getFont(new Font(
            "Courier New", Font.BOLD, 14));
    private final BitmapFont smallThinFont = FontCache.getFont(new Font(
            "Courier New", Font.PLAIN, 14));

    public void update(UnitDTO dto) {
        this.updateMovePoints(dto);
        this.updateHealthPoints(dto);
    }

    private void updateMovePoints(UnitDTO dto) {
        float cp = dto.getCurrentMovementPoints();
        float mp = dto.getMaximumMovementPoints();
        if (cp == 0) {
            this.movePoints.setColor(new Color(1, 0, 0));
        } else {
            this.movePoints.setColor(new Color(1 - cp / mp, cp / mp, 0));
        }
        this.movePoints.setContent(Integer.toString(dto.getCurrentMovementPoints()));
    }

    private void updateHealthPoints(UnitDTO dto) {
        float chp = dto.getCurrentHitPoints();
        float mhp = dto.getMaximumHitPoints();
        if (chp == 0) {
            this.hp.setColor(new Color(1, 0, 0));
        } else {
            this.hp.setColor(new Color(1 - chp / mhp, chp / mhp, 0));
        }
        this.hp.setContent(Integer.toString(dto.getCurrentHitPoints()));
    }

    public UnitWidget(UnitDTO unit) {
        // this.unit = unit;
        this.representation = new BitmapString(font, unit.getName().getTitle().charAt(0)
                + "", new Color(0.0, 1.0, unit.getOwner().getTeam()));
        this.movePoints = new BitmapString(smallThinFont, unit
                .getCurrentMovementPoints() + "");
        this.hp = new BitmapString(smallFont, unit
                .getCurrentHitPoints() + "");
        this.movePoints.setPosition(new Point(-10, 10));
        this.hp.setPosition(new Point(-10, -10));
        this.addChild(representation);
        if (unit.getType() != UnitType.BUILDING) {
            this.addChild(movePoints);
        }
        this.addChild(hp);
        this.updateHealthPoints(unit);
        this.updateMovePoints(unit);
    }

    @Override
    public Dimension getDimension() {
        return this.representation.getDimension();
    }
}
