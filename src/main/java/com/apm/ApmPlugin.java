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
import net.runelite.client.task.Schedule;
import net.runelite.client.ui.overlay.OverlayManager;

import java.time.temporal.ChronoUnit;
import java.util.LinkedList;

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
	ApmOver1ay overlay;

	@Inject
	private KeyManager keyManager;

	@Inject
	private ApmKeyListener keyListener;

	@Inject
	private MouseManager mouseManager;

	@Inject
	private ApmMouseListener mouseListener;

	public int totalInputCount, startMinutes, seconds, inputCountSecond;
	public long startMs;

	@Getter
	private final LinkedList<Integer> pastMinuteInputs = new LinkedList<>();

	private final int numCells = 60;

	public int currentApm = 0;

	@Override
	protected void startUp() throws Exception {
		overlayManager.add(overlay);


		pastMinuteInputs.clear();


		keyManager.registerKeyListener(keyListener);
		mouseManager.registerMouseListener(mouseListener);
		inputCountSecond = 0;
		totalInputCount = 0;
		startMinutes = -1;
		startMs = 0L;
		log.info("APM started!");
	}

	@Override
	protected void shutDown() throws Exception {
		overlayManager.remove(overlay);
		log.info("APM stopped!");
	}

	// on game enter/start
	@Subscribe
	public void onWidgetClosed(WidgetClosed widgetClosed) {
		if (widgetClosed.getGroupId() == WidgetID.LOGIN_CLICK_TO_PLAY_GROUP_ID){
			inputCountSecond = 0;
			totalInputCount = 0;
			startMinutes = -1;
			startMs = 0L;
			pastMinuteInputs.clear();

			for (int i = 0; i < numCells; i++) pastMinuteInputs.add(0);

		}
	}

	// the graph starts rolling out after 75 seconds not 70 for some reason
	@Schedule(
			period = 1000,
			unit = ChronoUnit.MILLIS
	)
	public void doEverySecond() {

		pastMinuteInputs.add(inputCountSecond);
		pastMinuteInputs.remove();

		// INFO keylistener is disabled while logging in, thats why the # of inputs
		// is always 4 (click sign in, click password box, click login, click play)

		inputCountSecond = 0;
		int hold = 0;
		for (Integer integer : pastMinuteInputs) {
			hold += integer;
		}
		currentApm = hold;
	}

	public Integer getMax() {
		Integer currentMax = 0;
		for (Integer integer : pastMinuteInputs) {
			if (integer > currentMax) {
				currentMax = integer;
			}
		}
		return currentMax;
	}

	public Integer getMin(){
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
