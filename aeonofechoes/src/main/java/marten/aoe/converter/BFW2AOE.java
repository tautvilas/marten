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
        int row = 1;
        // Reset input file
        bfwFile.close();
        bfwFile = new BufferedReader(new FileReader(args[0]));
        bfwFile.readLine();
        bfwFile.readLine();
        // Create AOE file header
        aoeFile.write("CleanData\nImport:\n    data/BFWTerrain\nMap:\n");
        aoeFile.write("    Name="+args[0].split("\\.")[0].replace(" ", "_")+"\n");
        // Tile matrix parsing
        while (row <= mapHeight) {
            currentLine = bfwFile.readLine().split(",\\s*\\d*");
            if (currentLine[0].equals(""))
                continue;
            if (mapWidth == -1)
                mapWidth = currentLine.length;
            if (mapWidth != currentLine.length)
                throw new IOException("Corrupt BFW file");
            for (int column = 1; column <= mapWidth; column++) {
                aoeFile.write("    Tile:\n");
                aoeFile.write("        X = "+column+"\n");
                aoeFile.write("        Y = "+row+"\n");
                aoeFile.write("        Terrain = ");
                // The hard part - matching BFW terrain codes with their proper names
                String terrainCode = currentLine[column - 1].trim();
                if (terrainCode.equals("Ai"))
                    aoeFile.write("ice");
                else if (terrainCode.equals("Aa"))
                    aoeFile.write("snow");
                else if (terrainCode.equals("Ww^Bw|"))
                    aoeFile.write("shallow_bridge_|");
                else if (terrainCode.equals("Ww^Bw/"))
                    aoeFile.write("shallow_bridge_/");
                else if (terrainCode.equals("WW^Bw\\"))
                    aoeFile.write("shallow_bridge_\\");
                else if (terrainCode.equals("Wo^Bw|"))
                    aoeFile.write("bridge_|");
                else if (terrainCode.equals("Wo^Bw/"))
                    aoeFile.write("bridge_/");
                else if (terrainCode.equals("Wo^Bw\\"))
                    aoeFile.write("bridge_\\");
                else if (terrainCode.equals("Ss^Bw|"))
                    aoeFile.write("swamp_bridge_|");
                else if (terrainCode.equals("Ss^Bw/"))
                    aoeFile.write("swamp_bridge_/");
                else if (terrainCode.equals("Ss^Bw\\"))
                    aoeFile.write("swamp_bridge_\\");
                else if (terrainCode.equals("Ce"))
                    aoeFile.write("encampment");
                else if (terrainCode.equals("Ch"))
                    aoeFile.write("castle");
                else if (terrainCode.equals("Cv"))
                    aoeFile.write("elven_castle");
                else if (terrainCode.equals("Cud"))
                    aoeFile.write("dwarven_castle");
                else if (terrainCode.equals("Chr"))
                    aoeFile.write("ruins");
                else if (terrainCode.equals("Chw"))
                    aoeFile.write("sunken_ruins");
                else if (terrainCode.equals("Chs"))
                    aoeFile.write("swamp_ruins");
                else if (terrainCode.equals("Ke"))
                    aoeFile.write("encampment keep");
                else if (terrainCode.equals("Kh"))
                    aoeFile.write("keep");
                else if (terrainCode.equals("Kv"))
                    aoeFile.write("elven_keep");
                else if (terrainCode.equals("Kud"))
                    aoeFile.write("dwarven_keep");
                else if (terrainCode.equals("Khr"))
                    aoeFile.write("ruined_keep");
                else if (terrainCode.equals("Khw"))
                    aoeFile.write("sunken_keep");
                else if (terrainCode.equals("Khs"))
                    aoeFile.write("swamp_keep");
                else if (terrainCode.equals("Dd^Dc"))
                    aoeFile.write("crater");
                else if (terrainCode.equals("Dd"))
                    aoeFile.write("desert");
                else if (terrainCode.equals("Dd^Dr"))
                    aoeFile.write("rubble");
                else if (terrainCode.equals("Ds"))
                    aoeFile.write("sand");
                else if (terrainCode.equals("Dd^Do"))
                    aoeFile.write("oasis");
                else if (terrainCode.equals("Aa^Fpa"))
                    aoeFile.write("snowy_forest");
                else if (terrainCode.equals("Gg^Fet"))
                    aoeFile.write("great_tree");
                else if (terrainCode.equals("Gs^Fp"))
                    aoeFile.write("forest");
                else if (terrainCode.equals("Gs^Ft"))
                    aoeFile.write("jungle");
                else if (terrainCode.equals("Gg"))
                    aoeFile.write("grassland");
                else if (terrainCode.equals("Ggf"))
                    aoeFile.write("meadow");
                else if (terrainCode.equals("Gs"))
                    aoeFile.write("savanna");
                else if (terrainCode.equals("Ha"))
                    aoeFile.write("snowy_hills");
                else if (terrainCode.equals("Hd"))
                    aoeFile.write("dunes");
                else if (terrainCode.equals("Hh"))
                    aoeFile.write("hills");
                else if (terrainCode.equals("Md"))
                    aoeFile.write("desert_mountain");
                else if (terrainCode.equals("Mm"))
                    aoeFile.write("mountain");
                else if (terrainCode.equals("Qxu"))
                    aoeFile.write("chasm");
                else if (terrainCode.equals("Ql"))
                    aoeFile.write("lava_chasm");
                else if (terrainCode.equals("Qlf"))
                    aoeFile.write("lava");
                else if (terrainCode.equals("Rd"))
                    aoeFile.write("desert_road");
                else if (terrainCode.equals("Re"))
                    aoeFile.write("dirt");
                else if (terrainCode.equals("Rr"))
                    aoeFile.write("road");
                else if (terrainCode.equals("Rp"))
                    aoeFile.write("path");
                else if (terrainCode.equals("Re^Gvs"))
                    aoeFile.write("farmland");
                else if (terrainCode.equals("Gg^Wm"))
                    aoeFile.write("windmill");
                else if (terrainCode.equals("Ss"))
                    aoeFile.write("swamp");
                else if (terrainCode.equals("Uu"))
                    aoeFile.write("cave");
                else if (terrainCode.equals("Uu^Ii"))
                    aoeFile.write("lit_cave");
                else if (terrainCode.equals("Uu^Uf"))
                    aoeFile.write("cave_fungi");
                else if (terrainCode.equals("Re^Uf"))
                    aoeFile.write("mushroom_grove");
                else if (terrainCode.equals("Uh"))
                    aoeFile.write("rocky_cave");
                else if (terrainCode.equals("Uh^Ii"))
                    aoeFile.write("lit_rocky_cave");
                else if (terrainCode.equals("Dd^Vda"))
                    aoeFile.write("desert_village");
                else if (terrainCode.equals("Dd^Vdt"))
                    aoeFile.write("desert_tents");
                else if (terrainCode.equals("Aa^Vea"))
                    aoeFile.write("snowy_elven_village");
                else if (terrainCode.equals("Gg^Ve"))
                    aoeFile.write("elven_village");
                else if (terrainCode.equals("Aa^Vha"))
                    aoeFile.write("snowy_village");
                else if (terrainCode.equals("Gg^Vh"))
                    aoeFile.write("village");
                else if (terrainCode.equals("Hh^Vhh"))
                    aoeFile.write("hill_village");
                else if (terrainCode.equals("Ha^Vhha"))
                    aoeFile.write("snowy_hill_village");
                else if (terrainCode.equals("Mm^Vhh"))
                    aoeFile.write("mountain_village");
                else if (terrainCode.equals("Gs^Vht"))
                    aoeFile.write("savanna_village");
                else if (terrainCode.equals("Uu^Vu"))
                    aoeFile.write("cave_village");
                else if (terrainCode.equals("Uu^Vud"))
                    aoeFile.write("dwarven_village");
                else if (terrainCode.equals("Ww^Vm"))
                    aoeFile.write("marine_village");
                else if (terrainCode.equals("Ss^Vhs"))
                    aoeFile.write("swamp_village");
                else if (terrainCode.equals("Ss^Vm"))
                    aoeFile.write("marine_swamp_village");
                else if (terrainCode.equals("Wo"))
                    aoeFile.write("deep_water");
                else if (terrainCode.equals("Ww"))
                    aoeFile.write("shallow_water");
                else if (terrainCode.equals("Wwf"))
                    aoeFile.write("ford");
                else if (terrainCode.equals("Wwr"))
                    aoeFile.write("reef");
                else if (terrainCode.equals("Mm^Xm"))
                    aoeFile.write("high_mountain");
                else if (terrainCode.equals("Md^Xm"))
                    aoeFile.write("high_desert_mountain");
                else if (terrainCode.equals("Xu"))
                    aoeFile.write("cave_wall");
                else if (terrainCode.equals("Xv"))
                    aoeFile.write("void");
                else {
                    System.err.println("Unknown terrain code @"+row+","+column+" : "+terrainCode);
                    aoeFile.write("void");
                }
                aoeFile.write("\n");
                // Provide map border
                if (row <= borderSize || column <= borderSize || row >= mapHeight + 1 - borderSize || column >= mapWidth + 1 - borderSize)
                    aoeFile.write("        Inaccessible\n");
            }
            row++;
        }
        aoeFile.close();
    }
}
