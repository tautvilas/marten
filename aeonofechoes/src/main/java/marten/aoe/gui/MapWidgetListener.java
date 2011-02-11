package marten.aoe.gui;

import marten.aoe.dto.PointDTO;

public interface MapWidgetListener {
    public void moveUnit(PointDTO from, PointDTO to);
}
