package marten.aoe.proposal.maps;

import java.io.IOException;

import marten.aoe.proposal.dto.PointDTO;
import marten.aoe.proposal.engine.Player;
import marten.aoe.proposal.engine.SimpleMap;

public final class Freelands extends SimpleMap {

    public Freelands() throws IOException {
        super ("2pFreelands", 39, 26);
    }
    
    @Override public int getPlayerLimit() {
        return 2;
    }

    @Override public PointDTO getStartingPosition(Player player) {
        if (player.getTeam() == 1) {
            return new PointDTO(18,3);
        }
        if (player.getTeam() == 2) {
            return new PointDTO(18,20);
        }
        return null;
    }

    @Override public void onTurnOver() {       
    }

}
