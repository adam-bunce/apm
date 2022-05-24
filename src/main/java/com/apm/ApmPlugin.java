package com.apm;

import com.google.inject.Provides;

import javax.inject.Inject;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import net.runelite.api.Client;
import net.runelite.api.events.WidgetClosed;
import net.runelite.api.widgets.WidgetID;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.input.KeyManager;
import net.runelite.client.input.MouseManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import java.util.LinkedList;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


@Slf4j
@PluginDescriptor(
	name = "APM"
)
public class ApmPlugin extends Plugin {
	@Inject
	private Client client;

	@Inject
	private ApmConfig config;

	@Inject
	OverlayManager overlayManager;

	@Inject
	ApmOverlay overlay;

	@Inject
	private KeyManager keyManager;

	private ApmKeyListener keyListener;

	@Inject
	private MouseManager mouseManager;

	private ApmMouseListener mouseListener;

	public int totalInputCount, seconds, inputCountSecond;

	@Getter
	private final LinkedList<Integer> pastMinuteInputs = new LinkedList<>();

	private final int numCells = 60;

	public int currentApm = 0;
	public int max, min = 0;

	@Inject
	private ScheduledExecutorService executorService;

	private ScheduledFuture updateChartFuture;

	@Override
	protected void startUp() throws Exception {
		pastMinuteInputs.clear();
		for (int i = 0; i < numCells; i++) pastMinuteInputs.add(0);

		overlayManager.add(overlay);

		// Tried using @Schedule but it was off by ~15 seconds for some reason?
		updateChartFuture = executorService.scheduleAtFixedRate(this::updateChart, 0, 1, TimeUnit.SECONDS);

		keyListener = new ApmKeyListener(this, config);
		keyManager.registerKeyListener(keyListener);
		mouseListener = new ApmMouseListener(this, config);
		mouseManager.registerMouseListener(mouseListener);

		inputCountSecond = 0;
		totalInputCount = 0;
		seconds = 0;


		log.info("APM started!");
	}

	@Override
	protected void shutDown() throws Exception {
		overlayManager.remove(overlay);

		updateChartFuture.cancel(true);
		updateChartFuture = null;

		keyManager.unregisterKeyListener(keyListener);
		keyListener = null;
		mouseManager.unregisterMouseListener(mouseListener);
		mouseListener = null;

		pastMinuteInputs.clear();
		log.info("APM stopped!");
	}

	// on game enter/start
	@Subscribe
	public void onWidgetClosed(WidgetClosed widgetClosed) {
		if (widgetClosed.getGroupId() == WidgetID.LOGIN_CLICK_TO_PLAY_GROUP_ID){
			inputCountSecond = 0;
			totalInputCount = 0;
			seconds = 0;

			pastMinuteInputs.clear();
			for (int i = 0; i < numCells; i++) pastMinuteInputs.add(0);

		}
	}

	public void updateChart() {
		pastMinuteInputs.add(inputCountSecond);
		pastMinuteInputs.remove();

		seconds++;
		inputCountSecond = 0;
		int hold = 0;

		for (Integer integer : pastMinuteInputs) {
			hold += integer;
		}

		currentApm = hold;
		max = getMax();
		min = getMin();
	}

	private Integer getMax() {
		Integer currentMax = 0;
			for (Integer integer : pastMinuteInputs) {
				if (integer > currentMax) {
					currentMax = integer;
				}
			}
		return currentMax;
	}

	private Integer getMin(){
		Integer currentMin = 0;
		for (Integer integer : pastMinuteInputs) {
			if (integer < currentMin) {
				currentMin = integer;
			}
		}
		return currentMin;
	}

	@Provides
	ApmConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(ApmConfig.class);
	}
}
