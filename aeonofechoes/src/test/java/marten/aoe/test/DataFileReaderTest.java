package marten.aoe.test;

import java.io.IOException;

import marten.aoe.loader.DataFileReader;
import marten.aoe.loader.DataTree;

public final class DataFileReaderTest {

    public static void main(String[] args) throws IOException {
        System.out.println("Reading test file");
        DataTree data = DataFileReader.read("data/test");
        System.out.println("Testing data integrity");        
        assert data.value().equals("FILE");
        assert data.branches().get(0).value().equals("singleton");
        assert data.branches().get(1).value().equals("list");
        assert data.branches().get(1).branches().get(0).value().equals("KEYVALUE");
        assert data.branches().get(1).branches().get(0).branches().get(0).equals("key");
        assert data.branches().get(1).branches().get(0).branches().get(0).equals("value");
        assert data.branches().get(2).value().equals("ident");
        System.out.println("Test successfully passed");
    }

}
