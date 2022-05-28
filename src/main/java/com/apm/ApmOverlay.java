package com.apm;

import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.components.LayoutableRenderableEntity;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

import javax.inject.Inject;
import java.awt.*;

import java.util.LinkedList;

public class ApmOverlay extends OverlayPanel {

    private final ApmPlugin plugin;
    private final ApmConfig config;


    @Inject
    public ApmOverlay(ApmPlugin plugin, ApmConfig config) {
        this.plugin = plugin;
        this.config = config;

        setPosition(OverlayPosition.TOP_LEFT);
    }

    LayoutableRenderableEntity apmGraph = new LayoutableRenderableEntity() {

        @Override
        public Dimension render(Graphics2D graphics) {
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

            if (config.toggleBehind()) {
                setLayer(OverlayLayer.ABOVE_SCENE);
            } else {
                setLayer(OverlayLayer.ABOVE_WIDGETS);
            }

            int overlayWidth, overlayHeight;                // width and height of the entire overlay
            try {
                overlayWidth = getPreferredSize().width;
                overlayHeight = getPreferredSize().height;
            } catch (NullPointerException e) {
                overlayWidth = 180;                         // Default settings for first time
                overlayHeight = 60;
                ApmOverlay.this.setPreferredSize(new Dimension(overlayWidth, overlayHeight));
            }

            int width, height, tempX;                       // width and height of the graph

            int marginGraphHeight = config.fontSize();
            int marginGraphWidth = 10;

            if (config.hideMargin()){
                width = overlayWidth;                       // set graph width to whole plugin width
                height = overlayHeight - marginGraphHeight; // remove the extra height
            } else {
                width = overlayWidth - marginGraphWidth * 2;
                height = overlayHeight - (marginGraphHeight + 15);
            }

            if (config.hideGraph()) {
                width = 0;
                height = 0;
                overlayHeight = config.fontSize();
            }

            //background rect
            graphics.setColor(config.overlayBackgroundColor());
            graphics.fillRect(0, 0, overlayWidth, overlayHeight);

            //outside border
            graphics.setColor(config.overlayBorderColor());
            graphics.drawRect(0, 0, overlayWidth, overlayHeight);

            //inside border
            graphics.setColor(config.graphBorderColor());
            int val = config.hideMargin() ? 0 : marginGraphWidth - 1;
            graphics.drawRect(val, marginGraphHeight + 1, width, height);

            //inside rect
            graphics.setColor(config.graphBackgroundColor());
            graphics.fillRect(val, marginGraphHeight + 1, width, height);

            graphics.setColor(config.textColor());
            String fontName = "Runescape Small";
            Font userFont = new Font(fontName, config.fontStyle().getValue(), config.fontSize());
            graphics.setFont(userFont);

            //Right label
            String rightLabel = "APM: " + plugin.currentApm;
            int strWidth = graphics.getFontMetrics().stringWidth(rightLabel);
            graphics.drawString(rightLabel, overlayWidth - strWidth - marginGraphWidth, marginGraphHeight - 1);


            // Left Label
            String leftLabel = "?";
            if (plugin.seconds < 60)  {
                leftLabel = ("Session APM: " + plugin.currentApm);
            }
            else {
                leftLabel = "Session APM: " + (int) (plugin.totalInputCount / (plugin.seconds / 60.0));
            }
            graphics.drawString(leftLabel, marginGraphWidth, marginGraphHeight - 1);

            LinkedList<Integer> data;
            data = plugin.getPastMinuteInputs();

            int dataStart = (data.size() > overlayWidth) ? (data.size() - overlayWidth) : 0;

            int maxValue;
            int minValue;
                maxValue = plugin.max;
                minValue = plugin.min;

            // change max inputs to 100, prevents div by 0 in-case of error
            if (maxValue <= 0) {
                maxValue = 10;
            }

            int tempMax = maxValue;
                double round = maxValue > 50 ? 50 : 10;
                maxValue = (int) (Math.ceil((double) tempMax / round) * round);

                if ((maxValue - tempMax) <= (0.2 * maxValue)) {
                    maxValue += round; // increase the max value to move the graph away from the top
                }

            if (maxValue == minValue) {
                maxValue++;
                minValue--;
            }

            if (!config.hideGraph()) {
                // Drawing bar graph
                if (config.chartType().getValue().equals("Bar")) {
                    graphics.setColor(config.graphColor());

                    for (int i = dataStart; i < data.size(); i++) {
                        int y = data.get(i);

                        int barWidth;
                        if (!config.hideMargin()) {
                            barWidth = (overlayWidth - marginGraphWidth) / 60;
                        } else {
                            barWidth = (overlayWidth / 60);
                        }
                        y = y < 0 ? maxValue - 1 : y;
                        y = height - (((height - 2) * (y - minValue)) / (maxValue - minValue) + 1);
                        tempX = ((width) * (i - dataStart) / (data.size() - dataStart));
                        y += marginGraphHeight;


                        if (!config.hideMargin()) {
                            tempX += marginGraphWidth;
                        }

                        graphics.setColor(config.graphColor());

                        if (!config.hideMargin()) {
                            graphics.drawRect(tempX, y, barWidth, overlayHeight - marginGraphHeight - y);
                            graphics.fillRect(tempX, y, barWidth, overlayHeight - marginGraphHeight - y);
                        } else {
                            graphics.drawRect(tempX, y, barWidth, overlayHeight - y);
                            graphics.fillRect(tempX, y, barWidth, overlayHeight - y);
                        }

                    }
                // Drawing line graph
                } else {
                    graphics.setColor(config.graphColor());
                    int oldX, oldY = oldX = -1;

                    for (int x = dataStart; x < data.size(); x++) {
                        int y = data.get(x);

                        y = y < 0 ? maxValue - 1 : y;
                        y = height - (((height - 2) * (y - minValue)) / (maxValue - minValue) + 1);
                        tempX = ((width) * (x - dataStart) / (data.size() - dataStart));
                        y += marginGraphHeight;

                        if (!config.hideMargin()) {
                            tempX += marginGraphWidth;
                        }
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
