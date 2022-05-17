package com.apm;

import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

import java.awt.*;

@ConfigGroup("apm")
public interface ApmConfig extends Config {

	@ConfigItem(
			position = 0,
			keyName = "chartType",
			name = "Graph Type",
			description = "Chart type that displays the last minute's APM"
	)
	default chartType chartType() {
		return chartType.Bar;
	}
	enum chartType {
		Bar("Bar"),
		Line("Line");

		private final String value;
		chartType(String graph) {
			this.value = graph;
		}

		public String getValue() {
			return value;
		}
	}


	@ConfigItem(
			position = 1,
			keyName = "HideGraph",
			name = "Hide Graph",
			description = "Hide the Input Graph"
	)
	default boolean hideGraph() {
		return false;
	}


	@ConfigItem(
			position = 2,
			keyName = "HideMargin",
			name = "Hide Bottom and Side Margins",
			description = "Remove the margins that surround the graph"
	)
	default boolean hideMargin() {
		return true;
	}


	@ConfigItem(
			position = 3,
			keyName = "includeKeysPresses",
			name = "Include Key Presses",
			description = "Include Key Presses in APM Calculation"
	)
	default boolean includeKeyPresses() {
		return true;
	}


	@ConfigItem(
			position = 4,
			keyName = "includeMouseClicks",
			name = "Include Mouse Clicks",
			description = "Include Mouse Clicks in APM Calculation"
	)
	default boolean includeMouseClicks() {
		return true;
	}


	@Alpha
	@ConfigItem(
			position = 5,
			keyName = "GraphColor",
			name = "Graph Color",
			description = "The color of the graph"
	)
	default Color graphColor() {
		return new Color(255, 240, 0);
	}


	@Alpha
	@ConfigItem(
			position = 6,
			keyName = "TextColor",
			name = "Text Color",
			description = "The color of the text"
	)
	default Color textColor() {
		return new Color(255, 240, 0);
	}

}