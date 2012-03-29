package marten.testing.tests;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import marten.aoe.data.type.UnitType;
import marten.aoe.data.units.Units;
import marten.aoe.dto.PlayerDTO;
import marten.aoe.dto.PointDTO;
import marten.aoe.dto.TileDTO;
import marten.aoe.dto.UnitDTO;
import marten.testing.ConsoleTest;

public class DtoSizeTest implements ConsoleTest {

    @Override
    public void run() {
        PointDTO point = new PointDTO(100, 100);
        PlayerDTO player = new PlayerDTO(0, "bastard", 5, 2, 2);
        UnitDTO unit = new UnitDTO(0, Units.Worker, player, 40, 50, 5, 10, false, UnitType.GROUND);
        @SuppressWarnings("deprecation")
        TileDTO tile = new TileDTO("River", point, unit);
        @SuppressWarnings("deprecation")
        TileDTO tileWoUnit = new TileDTO("River", point, null);
        System.out.println("Point DTO size: " + this.getSize(point));
        System.out.println("Player DTO size: " + this.getSize(player));
        System.out.println("Unit DTO size: " + this.getSize(unit));
        System.out.println("Tile with unit DTO size: " + this.getSize(tile));
        System.out.println("Tile without unit DTO size: " + this.getSize(tileWoUnit));
    }

    private int getSize(Serializable object) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream;
        try {
            objectStream = new ObjectOutputStream(byteStream);
            objectStream.writeObject(object);
            objectStream.flush();
            objectStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return byteStream.size();
    }

}
