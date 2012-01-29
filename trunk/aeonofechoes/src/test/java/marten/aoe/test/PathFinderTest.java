package marten.aoe.test;

import junit.framework.Assert;
import marten.aoe.dto.PointDTO;
import marten.aoe.dto.TileDTO;
import marten.aoe.engine.Action;
import marten.aoe.engine.Engine;
import marten.aoe.engine.EngineListener;
import marten.aoe.engine.GlobalEvent;
import marten.aoe.engine.LocalEvent;
import marten.aoe.engine.core.PlayerDTO;

import org.junit.Test;

public class PathFinderTest {
    @Test
    public void adjacentTileTest() {
        PlayerDTO[] players = {new PlayerDTO(0, "player")};
        Engine engine = new Engine("Freelands", players);
        engine.addListener(new EngineListener() {
            private int index = 0;
            private final PointDTO[] expectedPath = new PointDTO[] {
                    new PointDTO(13, 6), // unit creation
                    new PointDTO(13, 7)  // unit movement
            };
            @Override public void onGlobalEvent(GlobalEvent event) {}
            @Override public void onLocalEvent(LocalEvent event, TileDTO tileDTO) {
                if (event == LocalEvent.UNIT_ENTRY) {
                    Assert.assertEquals(tileDTO.getCoordinates(), this.expectedPath[this.index]);
                    this.index++;
                }
            }}, players[0]);
        engine.getMap().spawnUnit(players[0], "Dwarf", new PointDTO(13, 6));
        engine.performAction(players[0], new PointDTO(13, 6), Action.FIRST, new PointDTO(13, 7));
    }
}
