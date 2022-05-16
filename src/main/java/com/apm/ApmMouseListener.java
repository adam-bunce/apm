package com.apm;

import net.runelite.client.input.MouseListener;

import javax.inject.Inject;
import java.awt.event.MouseEvent;

public class ApmMouseListener implements MouseListener {
    @Inject
    ApmPlugin plugin;


    // sometimes this counts as two inputs when right-clicking
    // might just be that spamming in league made it mad sensitive, looks fine if
    // i click properly
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
        plugin.totalInputCount++;
        plugin.inputCountSecond++;
        System.out.println(mouseEvent.getButton());
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
