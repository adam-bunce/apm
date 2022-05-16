package com.apm;

import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.TitleComponent;

import javax.inject.Inject;
import java.awt.*;

public class ApmOverlay extends OverlayPanel {

    private final ApmPlugin plugin;

    @Inject
    public ApmOverlay(ApmPlugin plugin) {
        this.plugin = plugin;
        setLayer(OverlayLayer.ABOVE_WIDGETS);
        setPosition(OverlayPosition.TOP_CENTER);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        final FontMetrics fontMetrics = graphics.getFontMetrics();
        panelComponent.setPreferredSize(new Dimension(100, 0));
        panelComponent.getChildren().add(TitleComponent.builder()
                .text("Time In Game: " + plugin.seconds)
                .build());
        panelComponent.getChildren().add(TitleComponent.builder()
                .text("Inputs: " + plugin.totalInputCount)
                .build());
        panelComponent.getChildren().add(TitleComponent.builder()
                .text("Current APM: " + plugin.currentApm)
                .build());

        // TODO possibly add a toggle here so i can see actions per 5 mintues or something like that for very afk tasks, would need to modify
        //  the linked list size and then instead of 60 make it 60 * # of user configured minutes
        //  first 60 seconds will make the apm get cranked stupid high b/c division by fractions if this isn't here
       if (plugin.seconds < 60)  {
           panelComponent.getChildren().add(TitleComponent.builder()
                   .text("Session APM: " + plugin.totalInputCount)
                   .build());
       }
       else {
           panelComponent.getChildren().add(TitleComponent.builder()
                   .text("Session APM: " + (int) (plugin.totalInputCount / (plugin.seconds / 60.0)))
                   .build());
       }



       // TODO make all of the panel components be built into the manual rendering w/ lines
       //   rip code from the ping grapher plugin, also need to make sure that the session #'s reset properly (check discord for the pic)

        int lastX = 0;
        int lastY = 0;
        for (Integer integer : plugin.getPastMinuteInputs()){
            // lastX = integer;
            lastX+=11;

            // graphics.drawLine(lastX * 10, lastY * 10, integer * 10 , lastY * 10 +1);
            if (integer > 0) {
                graphics.setColor(new Color(166, 232, 230));
                graphics.fillRect(lastX, 20, 10, integer * 50);
            }
            else {
                graphics.setColor(new Color(166, 232, 230));
                graphics.fillRect(lastX, 20, 10, 10);
            }

        }

//        graphics.setColor(new Color(215, 13, 13));
//        graphics.fillRect(0, 150,200,300);
        // graphics.setColor(new Color(215, 13, 13));
        // graphics.fillRect(x, marginGraphHeight + 1, width, height);

        return super.render(graphics);
    }
}
