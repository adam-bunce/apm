package com.apm;

import net.runelite.client.input.MouseListener;

import javax.inject.Inject;
import java.awt.event.MouseEvent;

public class ApmMouseListener implements MouseListener {
    @Inject
    ApmPlugin plugin;

    @Inject
    ApmConfig config;

    @Override
    public MouseEvent mouseClicked(MouseEvent mouseEvent) {
        return mouseEvent;
    }

    @Override
    public MouseEvent mousePressed(MouseEvent mouseEvent) {
        return mouseEvent;
    }

    @Override
    public MouseEvent mouseReleased(MouseEvent mouseEvent) {
        if (config.includeMouseClicks()) {
            plugin.totalInputCount++;
            plugin.inputCountSecond++;
        }
        return mouseEvent;
    }

    @Override
    public MouseEvent mouseEntered(MouseEvent mouseEvent) {
        return mouseEvent;
    }

    @Override
    public MouseEvent mouseExited(MouseEvent mouseEvent) {
        return mouseEvent;
    }

    @Override
    public MouseEvent mouseDragged(MouseEvent mouseEvent) {
        return mouseEvent;
    }

    @Override
    public MouseEvent mouseMoved(MouseEvent mouseEvent) {
        return mouseEvent;
    }
}
