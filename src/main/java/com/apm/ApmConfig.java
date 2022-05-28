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
			keyName = "toggleBehind",
			name = "Hide Behind Interfaces",
			description = "Hides graph behind interfaces i.e bank and map"
	)
	default boolean toggleBehind() {
		return false;
	}


	@ConfigItem(
			position = 4,
			keyName = "includeKeysPresses",
			name = "Include Key Presses",
			description = "Include Key Presses in APM Calculation"
	)
	default boolean includeKeyPresses() {
		return true;
	}


	@ConfigItem(
			position = 5,
			keyName = "includeMouseClicks",
			name = "Include Mouse Clicks",
			description = "Include Mouse Clicks in APM Calculation"
	)
	default boolean includeMouseClicks() {
		return true;
	}


	@Alpha
	@ConfigItem(
			position = 6,
			keyName = "GraphColor",
			name = "Graph Color",
			description = "The color of the graph"
	)
	default Color graphColor() {
		return new Color(255, 240, 0);
	}


	@Alpha
	@ConfigItem(
			position = 7,
			keyName = "TextColor",
			name = "Text Color",
			description = "The color of the text"
	)
	default Color textColor() {
		return new Color(255, 240, 0);
	}


	@Alpha
	@ConfigItem(
			position = 8,
			keyName = "OverlayBackgroundColor",
			name = "Overlay Background Color",
			description = "The background color of the overlay"
	)
	default Color overlayBackgroundColor() {
		return new Color(0, 0, 0, 100);
	}



	@Alpha
	@ConfigItem(
			position = 9,
			keyName = "OverlayBorderColor",
			name = "Overlay Border Color",
			description = "The border color of the overlay"
	)
	default Color overlayBorderColor() {
		return new Color(0, 0, 0, 70);
	}



	@Alpha
	@ConfigItem(
			position = 10,
			keyName = "graphBackgroundColor",
			name = "Graph Background Color",
			description = "The background color of the graph"
	)
	default Color graphBackgroundColor() {
		return new Color(0, 0, 0, 120);
	}



	@Alpha
	@ConfigItem(
			position = 11,
			keyName = "graphBorderColor",
			name = "Graph Border Color",
			description = "The border color of the graph"
	)
	default Color graphBorderColor() {
		return new Color(0, 0, 0, 70);
	}






	@ConfigItem(
			position = 12,
			keyName = "fontSize",
			name = "Font Size",
			description = "Default: 16"
	)
	default int fontSize() {
		return 16;
	}


	@ConfigItem(
			position = 13,
			keyName = "fontStyle",
			name = "Font Style",
			description = "Default: Regular"
	)
	default FontStyle fontStyle() {
		return FontStyle.REGULAR;
	}

	enum FontStyle {
		REGULAR(0),
		BOLD(1),
		ITALICS(2);

		private final int value;

		FontStyle(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}




}



