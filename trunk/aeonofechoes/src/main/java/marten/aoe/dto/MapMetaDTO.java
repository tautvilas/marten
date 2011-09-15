package marten.aoe.dto;

import java.io.Serializable;

import java.util.List;

public final class MapMetaDTO implements Serializable {
    private static final long serialVersionUID = -4492413422461219935L;
    private final String name;
    private final int width;
    private final int height;
    private final int numberOfPlayers;
    private final List<PointDTO> startingPositions;
    public MapMetaDTO(String name, int width, int height, int numberOfPlayers, List<PointDTO> startingPositions) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.numberOfPlayers = numberOfPlayers;
        this.startingPositions = startingPositions;
    }
    public String getName() {
        return this.name;
    }
    public int getWidth() {
        return this.width;
    }
    public int getHeight() {
        return this.height;
    }
    public int getNumberOfPlayers() {
        return this.numberOfPlayers;
    }
    public List<PointDTO> getStartingPositions () {
        return this.startingPositions;
    }
}
