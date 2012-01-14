package marten.aoe.gui;

import marten.aoe.dto.PointDTO;

public interface MapWidgetListener {
    public void performAction(PointDTO from, PointDTO to);
}
