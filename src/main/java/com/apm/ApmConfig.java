package com.apm;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("apm")
public interface ApmConfig extends Config {
	@ConfigItem(
		keyName = "includeKeysPresses",
		name = "Include Key Presses",
		description = "Include Key Presses in APM Calculation"
	)
	default boolean includeKeyPresses() {
		return true;
	}

	@ConfigItem(
			keyName = "includeMouseClicks",
			name = "Include Mouse Clicks",
			description = "Include Mouse Clicks in APM Calculation"
	)
	default boolean includeMouseClicks() {
		return true;
	}

	@ConfigItem(
			position = 2,
			keyName = "chartType",
			name = "Chart Type",
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
}
