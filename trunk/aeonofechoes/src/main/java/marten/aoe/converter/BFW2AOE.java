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
        int borderSize = 0;
        // Header parsing
        String[] currentLine = bfwFile.readLine().split("=");
        if (currentLine.length != 2 || (currentLine[0].trim().equals("usage") && !currentLine[1].trim().equals("map")) || (!(currentLine[0].trim().equals("border_size")) && !(currentLine[0].trim().equals("usage"))))
            throw new IOException("Input file is not a BFW map");
        if (currentLine[0].trim().equals("border_size"))
            borderSize = Integer.parseInt(currentLine[1]);
        currentLine = bfwFile.readLine().split("=");
        if (currentLine.length != 2 || (currentLine[0].trim().equals("usage") && !currentLine[1].trim().equals("map")) || (!(currentLine[0].trim().equals("border_size")) && !(currentLine[0].trim().equals("usage"))))
            throw new IOException("Input file is not a BFW map");
        if (currentLine[0].trim().equals("border_size"))
            borderSize = Integer.parseInt(currentLine[1]);
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
        aoeFile.write("CleanData\nImport:\n    data/terrain/BasicTerrain\nMap:\n");
        String[] mapPath = args[0].split("/"); 
        aoeFile.write("    Name="+mapPath[mapPath.length-1].split("\\.")[0].replace(" ", "_")+"\n");
        // Tile matrix parsing
        while (row > 0) {
            currentLine = bfwFile.readLine().split(",\\s*\\d*");
            if (currentLine[0].equals(""))
                continue;
            if (mapWidth == -1)
                mapWidth = currentLine.length;
            if (mapWidth != currentLine.length)
                throw new IOException("Corrupt BFW file");
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
                if (terrainCode[0].equals("_") || terrainCode[0].equals("X"))
                    continue;
                aoeFile.write("    Tile:\n");
                aoeFile.write("        X = "+column+"\n");
                aoeFile.write("        Y = "+row+"\n");
                aoeFile.write("        Terrain = ");                
                if (terrainCode[0].equals("A"))
                    if (terrainCode.length < 2)
                        aoeFile.write("arctic");
                    else if (terrainCode[1].equals("F"))
                        aoeFile.write("arctic_forest");
                    else if (terrainCode[1].equals("V"))
                        aoeFile.write("arctic_village");
                    else {
                        System.err.println("Unkwown subtype of arctic tile: "+terrainCode[1]);
                        aoeFile.write("arctic");
                    }
                else if (terrainCode[0].equals("C"))
                    if (terrainCode.length < 2)
                        aoeFile.write("city");
                    else {                        
                        System.err.println("Unknown subtype of city tile: "+terrainCode[1]);
                        aoeFile.write("city");
                    }
                else if (terrainCode[0].equals("D"))
                    if (terrainCode.length < 2)
                        aoeFile.write("desert");
                    else if (terrainCode[1].equals("V"))
                        aoeFile.write("desert_village");
                    else {
                        System.err.println("Unknown subtype of desert tile: "+terrainCode[1]);
                        aoeFile.write("desert");
                    }
                else if (terrainCode[0].equals("G"))
                    if (terrainCode.length < 2)
                        aoeFile.write("grass");
                    else if (terrainCode[1].equals("F"))
                        aoeFile.write("forest");
                    else if (terrainCode[1].equals("V"))
                        aoeFile.write("village");
                    else if (terrainCode[1].equals("W"))
                        aoeFile.write("structure");
                    else {
                        System.err.println("Unknown subtype of grass tile: "+terrainCode[1]);
                        aoeFile.write("grass");
                    }
                else if (terrainCode[0].equals("H"))
                    if (terrainCode.length < 2)
                        aoeFile.write("hill");
                    else if (terrainCode[1].equals("F"))
                        aoeFile.write("hilly_forest");
                    else if (terrainCode[1].equals("V"))
                        aoeFile.write("hill_village");
                    else {
                        System.err.println("Unknown subtype of hill tile: "+terrainCode[1]);
                        aoeFile.write("hill");
                    }
                else if (terrainCode[0].equals("K"))
                    if (terrainCode.length < 2)
                        aoeFile.write("base");
                    else {
                        System.err.println("Unknown subtype of base tile: "+terrainCode[1]);
                        aoeFile.write("base");
                    }
                else if (terrainCode[0].equals("M"))
                    if (terrainCode.length < 2)
                        aoeFile.write("mountain");
                    else if (terrainCode[1].equals("V"))
                        aoeFile.write("mountain_village");
                    else if (terrainCode[1].equals("X"))
                        aoeFile.write("high_mountain");
                    else {
                        System.err.println("Unknown subtype of mountain tile: "+terrainCode[1]);
                        aoeFile.write("mountain");
                    }
                else if (terrainCode[0].equals("Q"))
                    if (terrainCode.length < 2)
                        aoeFile.write("lava");
                    else {
                        System.err.println("Unknown subtype of lava tile: "+terrainCode[1]);
                        aoeFile.write("lava");
                    }
                else if (terrainCode[0].equals("R"))
                    if (terrainCode.length < 2)
                        aoeFile.write("road");
                    else if (terrainCode[1].equals("G"))
                        aoeFile.write("farmland");
                    else if (terrainCode[1].equals("U"))
                        aoeFile.write("rubble");
                    else {
                        System.err.println("Unknown subtype of road tile: "+terrainCode[1]);
                        aoeFile.write("road");
                    }
                else if (terrainCode[0].equals("S"))
                    if (terrainCode.length < 2)
                        aoeFile.write("swamp");
                    else if (terrainCode[1].equals("B"))
                        aoeFile.write("swamp_bridge");
                    else if (terrainCode[1].equals("V"))
                        aoeFile.write("swamp_village");
                    else {
                        System.err.println("Unknown subtype of swamp tile: "+terrainCode[1]);
                        aoeFile.write("swamp");
                    }
                else if (terrainCode[0].equals("U"))
                    if (terrainCode.length < 2)
                        aoeFile.write("cave");
                    else if (terrainCode[1].equals("V"))
                        aoeFile.write("cave_village");
                    else {
                        System.err.println("Unknown subtype of cave tile: "+terrainCode[1]);
                        aoeFile.write("cave");
                    }
                else if (terrainCode[0].equals("W"))
                    if (terrainCode.length < 2)
                        aoeFile.write("water");
                    else if (terrainCode[1].equals("B"))
                        aoeFile.write("bridge");
                    else if (terrainCode[1].equals("V"))
                        aoeFile.write("water_village");
                    else {
                        System.err.println("Unknown subtype of water tile: "+terrainCode[1]);
                        aoeFile.write("water");
                    }
                else
                    throw new IOException("Unknown type of tile: "+terrainCode[0]);
                aoeFile.write("\n");
                // Provide map border
                if (row <= borderSize || column <= borderSize || row >= mapHeight + 1 - borderSize || column >= mapWidth + 1 - borderSize)
                    aoeFile.write("        Inaccessible\n");
            }
            row--;
        }
        aoeFile.close();
    }
}
