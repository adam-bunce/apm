package com.apm;

import net.runelite.api.Client;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.components.LayoutableRenderableEntity;


import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.TitleComponent;

import javax.inject.Inject;
import java.awt.*;


import java.awt.*;
import java.util.LinkedList;

public class ApmOver1ay extends OverlayPanel {

    private final Client client;
    private final ApmPlugin plugin;
    private final ApmConfig config;


    @Inject
    public ApmOver1ay(ApmPlugin plugin, ApmConfig config, Client client) {
        this.plugin = plugin;
        this.config = config;
        this.client = client;

        setLayer(OverlayLayer.ABOVE_WIDGETS);
        setPosition(OverlayPosition.BOTTOM_LEFT);
    }




    LayoutableRenderableEntity apmGraph = new LayoutableRenderableEntity() {

        @Override
        public Dimension render(Graphics2D graphics) {
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

            int overlayWidth, overlayHeight;                // width and height of the entire overlay
            try {
                overlayWidth = getPreferredSize().width;
                overlayHeight = getPreferredSize().height;
            } catch (NullPointerException e) {
                overlayWidth = 180;                         // Default settings for first time
                overlayHeight = 60;
                ApmOver1ay.this.setPreferredSize(new Dimension(overlayWidth, overlayHeight));
            }

            int width, height, tempX;                       // width and height of the graph
            int marginGraphHeight = 0;  // pingGraphConfig.fontSize();
            int marginGraphWidth = 0;

            //background rect
            graphics.setColor(new Color(0, 0, 0, 100));
            graphics.fillRect(0, 0, overlayWidth, overlayHeight);

            //outside border
            graphics.setColor(new Color(0, 0, 0, 120));
            graphics.drawRect(0, 0, overlayWidth, overlayHeight);


                width = overlayWidth;                       // set graph width to whole plugin width
                height = overlayHeight - marginGraphHeight; // remove the extra height


                //inside border
                graphics.setColor(new Color(0,0,0,120));
                int x2 =  marginGraphWidth - 1;
                graphics.drawRect(x2, marginGraphHeight + 1, width, height);

                //inside rect
                graphics.setColor(new Color (0,0,0,100));
                graphics.fillRect(x2, marginGraphHeight + 1, width, height);

                //Font Settings
                graphics.setColor(new Color(255, 240, 0));
                String fontName = "Runescape Small";           // Default name if the font name is empty


                Font userFont = new Font(fontName, Font.PLAIN, 16);


                graphics.setFont(userFont);

                String rightLabel = "APM: " + plugin.currentApm;

                //Right label
                int strWidth = graphics.getFontMetrics().stringWidth(rightLabel);
                graphics.drawString(rightLabel, overlayWidth - strWidth - marginGraphWidth, marginGraphHeight - 1);

                //Left label
                String leftLabel = "Inputs: " + plugin.totalInputCount;
                graphics.drawString(leftLabel, marginGraphWidth, marginGraphHeight - 1);


                String centerLabel = "Time: " + plugin.seconds;
                strWidth = graphics.getFontMetrics().stringWidth(centerLabel);
                graphics.drawString(centerLabel, marginGraphWidth + strWidth, marginGraphHeight - 1);






            LinkedList<Integer> data;
            data = plugin.getPastMinuteInputs();

            int dataStart = (data.size() > overlayWidth) ? (data.size() - overlayWidth) : 0;


            int maxValue;
            int minValue;
                maxValue = plugin.getMax();
                minValue = plugin.getMin();


            // change maxPing to 100, prevents div by 0 in-case of error
            if (maxValue <= 0) {
                maxValue = 100;
            }

            int tempMax = maxValue;
                double round = maxValue > 50 ? 50 : 10; // round up to nearest 50ms if > 50 else 10ms
                maxValue = (int) (Math.ceil((double) tempMax / round) * round);

                if ((maxValue - tempMax) <= (0.2 * maxValue)) {
                    maxValue += round; // increase the max value to move the graph away from the top
                }


            if (maxValue == minValue) {
                maxValue++;
                minValue--;
            }


            if (config.chartType().getValue().equals("Bar")) {
                graphics.setColor(new Color(255, 240, 0));

                int x_spacing = overlayWidth / 60;


                int x_pos = 0;
                for (int i = dataStart; i < data.size(); i++) {
                    int y = data.get(i);

                    y = overlayHeight - ( ( height - (((height - 2) * (y - minValue)) / (maxValue - minValue) + 1)));


                    tempX = ((width) * (i - dataStart) / (data.size() - dataStart));
                    graphics.fillRect(tempX, overlayHeight -  y, x_spacing, y);

                    x_pos+=x_spacing;
                }

            }
            else {
                //drawing line graph
                graphics.setColor(new Color(255, 240, 0));
                int oldX, oldY = oldX = -1;

                for (int x = dataStart; x < data.size(); x++) {
                    int y = data.get(x);

                    y = y < 0 ? maxValue - 1 : y; // change a "timed out" to spike rather than drop

                    //((limitMax - limitMin) * (valueIn - baseMin) / (baseMax - baseMin)) + limitMin;
                    //scale the x and y values to fit to the plugin
                    y = height - (((height - 2) * (y - minValue)) / (maxValue - minValue) + 1);

                    tempX = ((width) * (x - dataStart) / (data.size() - dataStart));

//                    y += marginGraphHeight;

                    tempX += marginGraphWidth;
//                    tempX -= marginGraphWidth;
//                    y -= marginGraphHeight;

                    if (y >= 0) {
                        graphics.drawLine(tempX, y, tempX, y);
                    }
                    if (oldX != -1 && y >= 0) {
                        graphics.drawLine(oldX, oldY, tempX, y);
                    }

                    oldX = tempX;
                    oldY = y;
                }
            }

            return new Dimension(overlayWidth - 8, overlayHeight - 8);
        }


        @Override
        public Rectangle getBounds() {
            return null;
        }

        @Override
        public void setPreferredLocation(Point position) {

        }

        @Override
        public void setPreferredSize(Dimension dimension) {

        }
    };


    @Override
    public Dimension render(Graphics2D graphics) {
        panelComponent.render(graphics);
        panelComponent.getChildren().add(apmGraph);
        panelComponent.setBackgroundColor(new Color(0, 0, 0, 0));
        return super.render(graphics);
    }
}
