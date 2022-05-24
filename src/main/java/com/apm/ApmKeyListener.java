package com.apm;

import net.runelite.client.input.KeyListener;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;

public class ApmKeyListener implements KeyListener {
    public final ApmPlugin plugin;
    public final ApmConfig config;

    ApmKeyListener(ApmPlugin plugin, ApmConfig config) throws FileNotFoundException {
        this.plugin = plugin;
        this.config = config;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (config.includeKeyPresses()) {
            plugin.totalInputCount++;
            plugin.inputCountSecond++;
        }
    }
}
