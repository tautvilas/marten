package marten.aoe.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import marten.age.graphics.SceneGraphBranch;
import marten.age.graphics.SceneGraphChild;
import marten.age.graphics.primitives.Point;
import marten.aoe.dto.PointDTO;
import marten.aoe.dto.TileDTO;
import marten.aoe.dto.UnitDTO;
import marten.aoe.gui.widget.UnitWidget;

import org.apache.log4j.Logger;

public class UnitDrawer {
    private HashMap<Integer, UnitData> units = new HashMap<Integer, UnitDrawer.UnitData>();

    private SceneGraphBranch<SceneGraphChild> context;

    private static org.apache.log4j.Logger log = Logger
            .getLogger(UnitDrawer.class);

    public UnitDrawer(SceneGraphBranch<SceneGraphChild> context) {
        this.context = context;
    }

    public void updateTile(TileDTO oldTile, TileDTO tile,
            Point tileDisplayCoordinates) {
        UnitDTO unitDTO = tile.getUnit();
//        UnitDTO oldUnit = null;
//        if (oldTile != null) {
//            oldUnit = oldTile.getUnit();
//        }
        PointDTO coords = tile.getCoordinates();
        if (unitDTO != null) {
            int id = unitDTO.getId();
            if (!this.units.containsKey(id)) {
                UnitWidget unit = new UnitWidget(tile.getUnit());
                unit.setPosition(new Point(tileDisplayCoordinates.x + 64 / 2
                        - unit.getDimension().width / 2,
                        tileDisplayCoordinates.y + 64 / 2
                                - unit.getDimension().height / 2));
                this.context.addChild(unit);
                this.units.put(tile.getUnit().getId(), new UnitData(unit,
                        coords));
                log.info("Unit " + id + " entered " + coords);
            } else {
                UnitData unit = this.units.get(id);
                unit.path.add(new Point(tileDisplayCoordinates.x + 64 / 2
                        - unit.widget.getDimension().width / 2,
                        tileDisplayCoordinates.y + 64 / 2
                                - unit.widget.getDimension().height / 2));
                unit.widget.update(unitDTO);
                unit.position = coords;
                log.info("Unit " + id + " moved to " + coords);
            }
        }/* else if (oldUnit != null) {
            log.info("Deleting unit");
            UnitData unit = this.units.get(oldUnit.getId());
            if (unit.position.equals(coords)) {
                this.context.removeChild(unit.widget);
                this.units.remove(oldUnit.getId());
            }
        } */
    }

    public void animate(int speed) {
        for (UnitData unit : this.units.values()) {
            if  (!unit.path.isEmpty()) {
                Point nextPos = unit.path.get(0);
                Point pos = unit.widget.getPosition();
                if (nextPos.approximatelyEquals(pos, speed)) {
                    unit.widget.setPosition(nextPos);
                    unit.path.remove(0);
                } else {
                    int dx = Integer.signum((int)(nextPos.x - pos.x));
                    int dy = Integer.signum((int)(nextPos.y - pos.y));
                    Point newPos = new Point(pos.x + dx * speed, pos.y + dy * speed);
                    unit.widget.setPosition(newPos);
                }
            }
        }
    }

    private class UnitData {
        public UnitWidget widget;
        public List<Point> path = new ArrayList<Point>();
        public PointDTO position;

        public UnitData(UnitWidget widget, PointDTO position) {
            this.widget = widget;
            this.position = position;
        }
    }
}
