package marten.aoe.converter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public final class BFW2AOE {

    /** Reads a "Battle for Wesnoth" map file and converts it into "Aeons of Echoes" map.
     * @param args strings with input file and output file respectively
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.err.println("Usage:\nBFW2AOE <bfw_map> <aoe_map>");
            System.exit(1);
        }
        BufferedReader bfwFile = new BufferedReader(new FileReader(args[0]));
        BufferedWriter aoeFile = new BufferedWriter(new FileWriter(args[1]));
        // Header parsing
        String[] currentLine = bfwFile.readLine().split("=");
        if (currentLine.length != 2 || (currentLine[0].trim().equals("usage") && !currentLine[1].trim().equals("map")) || (!(currentLine[0].trim().equals("border_size")) && !(currentLine[0].trim().equals("usage"))))
            throw new IOException("Input file is not a BFW map");
        currentLine = bfwFile.readLine().split("=");
        if (currentLine.length != 2 || (currentLine[0].trim().equals("usage") && !currentLine[1].trim().equals("map")) || (!(currentLine[0].trim().equals("border_size")) && !(currentLine[0].trim().equals("usage"))))
            throw new IOException("Input file is not a BFW map");
        // Determine map size
        int mapHeight = 0;
        String rawLine = bfwFile.readLine();
        while (rawLine != null) {
            if (!rawLine.matches("\\s*"))
                mapHeight++;
            rawLine = bfwFile.readLine();
        }
        int mapWidth = -1;            
        int row = mapHeight;
        // Reset input file
        bfwFile.close();
        bfwFile = new BufferedReader(new FileReader(args[0]));
        bfwFile.readLine();
        bfwFile.readLine();
        // Create AOE file header
        aoeFile.write("Map:\n");
        // Tile matrix parsing
        while (row > 0) {
            currentLine = bfwFile.readLine().split(",\\s*\\d*");
            if (currentLine[0].equals(""))
                continue;
            if (mapWidth == -1)
                mapWidth = currentLine.length;
            if (mapWidth != currentLine.length)
                throw new IOException("Corrupt BFW file");
            aoeFile.write("    Row:\n");
            for (int column = 1; column <= mapWidth; column++) {
                // The hard part - matching BFW terrain codes with their proper names
                String[] terrainCodes = currentLine[column - 1].trim().split("[a-zI\\^]*");
                int terrainCodeLength = 0;
                for (String terrainCodeLetter : terrainCodes)
                    if (!terrainCodeLetter.matches("\\s*"))
                        terrainCodeLength++;
                String[] terrainCode = new String[terrainCodeLength];
                terrainCodeLength = 0;
                for (String terrainCodeLetter : terrainCodes)
                    if (!terrainCodeLetter.matches("\\s*")) {
                        terrainCode[terrainCodeLength] = terrainCodeLetter;
                        terrainCodeLength++;
                    }
                aoeFile.write("        ");
                if (terrainCode[0].equals("_") || terrainCode[0].equals("X"))
                    aoeFile.write("Void");
                else if (terrainCode[0].equals("A"))
                    if (terrainCode.length < 2)
                        aoeFile.write("Snow");
                    else if (terrainCode[1].equals("F"))
                        aoeFile.write("SnowForest");
                    else if (terrainCode[1].equals("V"))
                        aoeFile.write("SnowVillage");
                    else {
                        System.err.println("Unkwown subtype of arctic tile: "+terrainCode[1]);
                        aoeFile.write("Snow");
                    }
                else if (terrainCode[0].equals("C"))
                    if (terrainCode.length < 2)
                        aoeFile.write("City");
                    else {                        
                        System.err.println("Unknown subtype of city tile: "+terrainCode[1]);
                        aoeFile.write("City");
                    }
                else if (terrainCode[0].equals("D"))
                    if (terrainCode.length < 2)
                        aoeFile.write("Desert");
                    else if (terrainCode[1].equals("V"))
                        aoeFile.write("DesertVillage");
                    else {
                        System.err.println("Unknown subtype of desert tile: "+terrainCode[1]);
                        aoeFile.write("Desert");
                    }
                else if (terrainCode[0].equals("G"))
                    if (terrainCode.length < 2)
                        aoeFile.write("Field");
                    else if (terrainCode[1].equals("F"))
                        aoeFile.write("Forest");
                    else if (terrainCode[1].equals("V"))
                        aoeFile.write("Village");
                    else if (terrainCode[1].equals("W"))
                        aoeFile.write("Structure");
                    else {
                        System.err.println("Unknown subtype of grass tile: "+terrainCode[1]);
                        aoeFile.write("Field");
                    }
                else if (terrainCode[0].equals("H"))
                    if (terrainCode.length < 2)
                        aoeFile.write("Hill");
                    else if (terrainCode[1].equals("F"))
                        aoeFile.write("HillForest");
                    else if (terrainCode[1].equals("V"))
                        aoeFile.write("HillVillage");
                    else {
                        System.err.println("Unknown subtype of hill tile: "+terrainCode[1]);
                        aoeFile.write("Hill");
                    }
                else if (terrainCode[0].equals("K"))
                    if (terrainCode.length < 2)
                        aoeFile.write("Headquarters");
                    else {
                        System.err.println("Unknown subtype of base tile: "+terrainCode[1]);
                        aoeFile.write("Headquarters");
                    }
                else if (terrainCode[0].equals("M"))
                    if (terrainCode.length < 2)
                        aoeFile.write("Mountain");
                    else if (terrainCode[1].equals("V"))
                        aoeFile.write("MountainVillage");
                    else if (terrainCode[1].equals("X"))
                        aoeFile.write("ImpassableMountain");
                    else {
                        System.err.println("Unknown subtype of mountain tile: "+terrainCode[1]);
                        aoeFile.write("Mountain");
                    }
                else if (terrainCode[0].equals("Q"))
                    if (terrainCode.length < 2)
                        aoeFile.write("Lava");
                    else {
                        System.err.println("Unknown subtype of lava tile: "+terrainCode[1]);
                        aoeFile.write("Lava");
                    }
                else if (terrainCode[0].equals("R"))
                    if (terrainCode.length < 2)
                        aoeFile.write("Road");
                    else if (terrainCode[1].equals("G"))
                        aoeFile.write("Farm");
                    else if (terrainCode[1].equals("U"))
                        aoeFile.write("Ruins");
                    else {
                        System.err.println("Unknown subtype of road tile: "+terrainCode[1]);
                        aoeFile.write("Road");
                    }
                else if (terrainCode[0].equals("S"))
                    if (terrainCode.length < 2)
                        aoeFile.write("Swamp");
                    else if (terrainCode[1].equals("B"))
                        aoeFile.write("SwampBridge");
                    else if (terrainCode[1].equals("V"))
                        aoeFile.write("SwampVillage");
                    else {
                        System.err.println("Unknown subtype of swamp tile: "+terrainCode[1]);
                        aoeFile.write("Swamp");
                    }
                else if (terrainCode[0].equals("U"))
                    if (terrainCode.length < 2)
                        aoeFile.write("Cave");
                    else if (terrainCode[1].equals("V"))
                        aoeFile.write("CaveVillage");
                    else {
                        System.err.println("Unknown subtype of cave tile: "+terrainCode[1]);
                        aoeFile.write("Cave");
                    }
                else if (terrainCode[0].equals("W"))
                    if (terrainCode.length < 2)
                        aoeFile.write("Water");
                    else if (terrainCode[1].equals("B"))
                        aoeFile.write("Bridge");
                    else if (terrainCode[1].equals("V"))
                        aoeFile.write("WaterVillage");
                    else {
                        System.err.println("Unknown subtype of water tile: "+terrainCode[1]);
                        aoeFile.write("Water");
                    }
                else
                    throw new IOException("Unknown type of tile: "+terrainCode[0]);
                aoeFile.write("\n");
            }
            row--;
        }
        aoeFile.close();
        bfwFile.close();
    }
}
