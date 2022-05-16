package com.apm;

import net.runelite.client.input.KeyListener;

import javax.inject.Inject;
import java.awt.event.KeyEvent;

public class ApmKeyListener implements KeyListener {
    @Inject
    ApmPlugin plugin;


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        plugin.totalInputCount++;
        plugin.inputCountSecond++;
    }
}
