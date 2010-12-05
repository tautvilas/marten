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
                aoeFile.write("        Tile:\n");
                if (terrainCode[0].equals("_") || terrainCode[0].equals("X")) {
                    aoeFile.write("            Void\n");
                }
                else if (terrainCode[0].equals("A")) {
                    aoeFile.write("            Snow\n");
                    if (terrainCode.length > 1)
                        if (terrainCode[1].equals("F"))
                            aoeFile.write("            Forest\n");
                        else if (terrainCode[1].equals("V"))
                            aoeFile.write("            Village\n");
                        else
                            System.err.println("Unknown subtype of arctic tile: "+terrainCode[1]);                    
                }
                else if (terrainCode[0].equals("C")) {
                    aoeFile.write("            City\n");
                    if (terrainCode.length > 1)
                        System.err.println("Unknown subtype of city tile: "+terrainCode[1]);
                }                    
                else if (terrainCode[0].equals("D")) {
                    aoeFile.write("            Desert\n");
                    if (terrainCode.length > 1)
                        if (terrainCode[1].equals("V"))
                            aoeFile.write("            Village\n");
                        else
                            System.err.println("Unknown subtype of desert tile: "+terrainCode[1]);
                }
                else if (terrainCode[0].equals("G")) {
                    aoeFile.write("            Grassland\n");
                    if (terrainCode.length > 1)
                        if (terrainCode[1].equals("F"))
                            aoeFile.write("            Forest\n");
                        else if (terrainCode[1].equals("V"))
                            aoeFile.write("            Village\n");
                        else if (terrainCode[1].equals("W"))
                            aoeFile.write("            Ruins\n");
                        else
                            System.err.println("Unknown subtype of grass tile: "+terrainCode[1]);                    
                }
                else if (terrainCode[0].equals("H")) {
                    aoeFile.write("            Grassland\n");
                    aoeFile.write("            Hill\n");
                    if (terrainCode.length > 1)
                        if (terrainCode[1].equals("F"))
                            aoeFile.write("            Forest\n");
                        else if (terrainCode[1].equals("V"))
                            aoeFile.write("            Village\n");
                        else
                            System.err.println("Unknown subtype of hill tile: "+terrainCode[1]);
                }
                else if (terrainCode[0].equals("K")) {
                    aoeFile.write("            City\n");
                    aoeFile.write("            HQ\n");
                    if (terrainCode.length > 1)
                        System.err.println("Unknown subtype of base tile: "+terrainCode[1]);
                }
                else if (terrainCode[0].equals("M")) {
                    aoeFile.write("            Rocks\n");
                    aoeFile.write("            Mountain\n");
                    if (terrainCode.length > 1)
                        if (terrainCode[1].equals("V"))
                            aoeFile.write("            Village\n");
                        else if (terrainCode[1].equals("X"))
                            aoeFile.write("            Impassable\n");
                        else
                            System.err.println("Unknown subtype of mountain tile: "+terrainCode[1]);                        
                }
                else if (terrainCode[0].equals("Q")) {
                    aoeFile.write("            Lava\n");
                    if (terrainCode.length > 1)                        
                        System.err.println("Unknown subtype of lava tile: "+terrainCode[1]);                        
                }
                else if (terrainCode[0].equals("R")) {
                    aoeFile.write("            Dirt\n");
                    if (terrainCode.length > 1)
                        if (terrainCode[1].equals("G"))
                            aoeFile.write("            Farmland\n");
                        else if (terrainCode[1].equals("U"))
                            aoeFile.write("            Ruins\n");
                        else
                            System.err.println("Unknown subtype of road tile: "+terrainCode[1]);                        
                }
                else if (terrainCode[0].equals("S")) {
                    aoeFile.write("            Swamp\n");
                    if (terrainCode.length > 1)
                        if (terrainCode[1].equals("B"))
                            aoeFile.write("            Bridge\n");
                        else if (terrainCode[1].equals("V"))
                            aoeFile.write("            Village\n");
                        else
                            System.err.println("Unknown subtype of swamp tile: "+terrainCode[1]);                        
                }
                else if (terrainCode[0].equals("U")) {
                    aoeFile.write("            Rocks\n");
                    if (terrainCode.length > 1)                        
                        if (terrainCode[1].equals("V"))
                            aoeFile.write("            Village\n");
                        else
                            System.err.println("Unknown subtype of cave tile: "+terrainCode[1]);
                }
                else if (terrainCode[0].equals("W")) {
                    aoeFile.write("            Water\n");
                    if (terrainCode.length > 1)
                        if (terrainCode[1].equals("B"))
                            aoeFile.write("            Bridge\n");
                        else if (terrainCode[1].equals("V"))
                            aoeFile.write("            Village\n");
                        else
                            System.err.println("Unknown subtype of water tile: "+terrainCode[1]);                        
                }
                else
                    throw new IOException("Unknown type of tile: "+terrainCode[0]);
            }
            row--;
        }
        aoeFile.close();
        bfwFile.close();
    }
}
