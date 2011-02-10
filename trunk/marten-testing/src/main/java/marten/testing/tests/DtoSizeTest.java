package marten.testing.tests;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import marten.aoe.dto.PointDTO;
import marten.aoe.dto.TileDTO;
import marten.aoe.dto.UnitDTO;
import marten.testing.ConsoleTest;

public class DtoSizeTest implements ConsoleTest {

    @Override
    public void run() {
        PointDTO point = new PointDTO(100, 100);
        UnitDTO unit = new UnitDTO("dwarf");
        TileDTO tile = new TileDTO("River", point, unit, true);
        System.out.println("Point DTO size: " + getSize(point));
        System.out.println("Unit DTO size: " + getSize(unit));
        System.out.println("Tile DTO size: " + getSize(tile));
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
