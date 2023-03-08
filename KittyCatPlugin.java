package com.d4;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ProjectileMoved;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import javax.inject.Inject;
import java.sql.Array;
import java.util.*;


@Slf4j
@PluginDescriptor(
	name = "KittyCatscape"
)
public class KittyCatPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private KittyCatConfig config;

	private Projectile replacementProjectile;
	private boolean isFirstProjectile = true;

	@Provides
	KittyCatConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(KittyCatConfig.class);
	}

	@Override
	protected void startUp() throws Exception
	{

	}

	@Override
	protected void shutDown() throws Exception
	{

	}

	@Subscribe
	public void onProjectileMoved(ProjectileMoved projectileMoved)
	{
		Projectile projectile = projectileMoved.getProjectile();
		int replacementGraphic = 35404;

		if (projectile.getId() == replacementGraphic) {

			// First Created Projectile nulls due to recursion
			// so just return by default
			if (isFirstProjectile) {
				isFirstProjectile = false;
				return;
			}

		} else {
			replacementProjectile = client.createProjectile(replacementGraphic,
					projectile.getFloor(),
					projectile.getX1(), projectile.getY1(),
					projectile.getHeight(),
					projectile.getStartCycle(), projectile.getEndCycle(),
					projectile.getSlope(),
					projectile.getStartHeight(), projectile.getEndHeight(),
					projectile.getInteracting(),
					projectile.getTarget().getX(), projectile.getTarget().getY());

		}
		client.getProjectiles().addLast(replacementProjectile);
		// projectile.setEndCycle(0);
	}

	@Subscribe
	public void onGameTick(GameTick gameTick) {
		catifyPlayer();
		catifySpells();
		catifyInventory();
		catifyPrayerIcons();
		catifyBuiltInWidgets();
		catifyCustomWidgets();
		catifyTrees();
	}

	private void catifyTrees()
	{
		final int tree = 10820;
		ObjectComposition objectComposition = client.getObjectDefinition(tree);
		
	}

	private void catifyPrayerIcons()
	{
		getPrayerIconWidgets().forEach(widgetInfo -> {
			if (client.getWidget(widgetInfo.getPackedId()) == null) return;

			client.getWidget(widgetInfo.getPackedId()).getChild(1).setItemQuantityMode(0);
			client.getWidget(widgetInfo.getPackedId()).getChild(1).setItemId(getRandomKitten());
		});

		// Icon & Text container underneath prayer icons
		Widget container = client.getWidget(CustomWidgetInfo.PRAYER_ICON_AND_TEXT_CONTAINER.getPackedId());

		// Icon
		container.getChild(0).setItemQuantityMode(0);
		container.getChild(0).setItemId(getRandomKitten());

		// Text
		container.setOriginalWidth(150);
		container.getChild(1).setText("Unlimited Cat Prayer");
		container.getChild(1).revalidate();
	}

	private void catifyInventory()
	{
		Widget inventory = client.getWidget(WidgetInfo.INVENTORY);
		if (inventory == null) return;

		for (Widget widget: inventory.getDynamicChildren())
		{
			widget.setItemId(getRandomKitten());
			widget.setItemQuantityMode(0);
		}
	}

	private void catifyCustomWidgets()
	{
		// Custom Widgets that don't exist in WidgetInfo
		List<Widget> customWidgets = getCustomWidgets();
		for(Widget widget: customWidgets) {
			if (customWidgets == null) continue;
			widget.setItemId(getRandomKitten());
			widget.setItemQuantityMode(0);
		}

		// Account Management Tab - Can't be bothered getting ID
		final int accountManagementTabID = 10747948;
		Widget accountManagementTab = client.getWidget(accountManagementTabID);

		if (accountManagementTab == null) return;

		accountManagementTab.setItemQuantityMode(0);
		accountManagementTab.setItemId(getRandomKitten());

		// Removes Minimap
		client.getWidget(WidgetInfo.RESIZABLE_MINIMAP_DRAW_AREA).setSpriteId(-1);
	}

	private void catifyBuiltInWidgets()
	{
		getBuiltInWidgets().forEach(widgetInfo -> {
			if (client.getWidget(widgetInfo.getPackedId()) == null) return;

			client.getWidget(widgetInfo.getPackedId()).setItemQuantityMode(0);
			client.getWidget(widgetInfo.getPackedId()).setItemId(getRandomKitten());
		});
	}

	private void catifySpells()
	{
		for(MagicWidgetInfo widget: MagicWidgetInfo.values()) {
			if (client.getWidget(widget.getPackedId()) == null) continue;

			client.getWidget(widget.getPackedId()).setItemId(getRandomKitten());
			client.getWidget(widget.getPackedId()).setItemQuantityMode(0);
		}
	}

	private void catifyPlayer()
	{
		if (client.getLocalPlayer() == null) return;

		Player player = client.getLocalPlayer();
		player.getPlayerComposition().setTransformedNpcId(getRandomNPCKitten());
	}

	private List<WidgetInfo> getPrayerIconWidgets()
	{
		return Arrays.asList(
				WidgetInfo.PRAYER_THICK_SKIN, WidgetInfo.PRAYER_BURST_OF_STRENGTH,
				WidgetInfo.PRAYER_CLARITY_OF_THOUGHT, WidgetInfo.PRAYER_ROCK_SKIN,
				WidgetInfo.PRAYER_SUPERHUMAN_STRENGTH, WidgetInfo.PRAYER_IMPROVED_REFLEXES,
				WidgetInfo.PRAYER_RAPID_RESTORE, WidgetInfo.PRAYER_RAPID_HEAL,
				WidgetInfo.PRAYER_PROTECT_ITEM, WidgetInfo.PRAYER_STEEL_SKIN,
				WidgetInfo.PRAYER_ULTIMATE_STRENGTH, WidgetInfo.PRAYER_INCREDIBLE_REFLEXES,
				WidgetInfo.PRAYER_PROTECT_FROM_MAGIC, WidgetInfo.PRAYER_PROTECT_FROM_MISSILES,
				WidgetInfo.PRAYER_PROTECT_FROM_MELEE, WidgetInfo.PRAYER_RETRIBUTION,
				WidgetInfo.PRAYER_REDEMPTION, WidgetInfo.PRAYER_SMITE,
				WidgetInfo.PRAYER_SHARP_EYE, WidgetInfo.PRAYER_MYSTIC_WILL,
				WidgetInfo.PRAYER_HAWK_EYE, WidgetInfo.PRAYER_MYSTIC_LORE,
				WidgetInfo.PRAYER_EAGLE_EYE, WidgetInfo.PRAYER_MYSTIC_MIGHT,
				WidgetInfo.PRAYER_CHIVALRY, WidgetInfo.PRAYER_PIETY,
				WidgetInfo.PRAYER_RIGOUR, WidgetInfo.PRAYER_AUGURY,
				WidgetInfo.PRAYER_PRESERVE);
	}

	private List<WidgetInfo> getBuiltInWidgets()
	{
		return Arrays.asList(
				WidgetInfo.RESIZABLE_VIEWPORT_BOTTOM_LINE_COMBAT_ICON,
				WidgetInfo.RESIZABLE_VIEWPORT_BOTTOM_LINE_INVENTORY_ICON,
				WidgetInfo.RESIZABLE_VIEWPORT_BOTTOM_LINE_STATS_ICON,
				WidgetInfo.RESIZABLE_VIEWPORT_BOTTOM_LINE_QUESTS_ICON,
				WidgetInfo.RESIZABLE_VIEWPORT_BOTTOM_LINE_EQUIPMENT_ICON,
				WidgetInfo.RESIZABLE_VIEWPORT_BOTTOM_LINE_PRAYER_ICON,
				WidgetInfo.RESIZABLE_VIEWPORT_BOTTOM_LINE_MAGIC_ICON,

				WidgetInfo.RESIZABLE_VIEWPORT_BOTTOM_LINE_FRIEND_CHAT_ICON,
				WidgetInfo.RESIZABLE_VIEWPORT_BOTTOM_LINE_FRIEND_ICON,
				WidgetInfo.RESIZABLE_VIEWPORT_BOTTOM_LINE_OPTIONS_ICON,
				WidgetInfo.RESIZABLE_VIEWPORT_BOTTOM_LINE_EMOTES_ICON,
				WidgetInfo.RESIZABLE_VIEWPORT_BOTTOM_LINE_MUSIC_ICON,

				WidgetInfo.RESIZABLE_MINIMAP_LOGOUT_BUTTON,
				WidgetInfo.MINIMAP_WORLDMAP_OPTIONS,
				WidgetInfo.MINIMAP_XP_ORB
		);
	}

	public List<Widget> getCustomWidgets()
	{
		List<Widget> widgets = new ArrayList<>();

		for (CustomWidgetInfo widgetInfo : CustomWidgetInfo.values())
		{
			if (client.getWidget(widgetInfo.getPackedId()) == null) continue;
			widgets.add(client.getWidget(widgetInfo.getPackedId()));
		}

		return widgets;
	}
	private int getRandomKitten()
	{
		List<Integer> kittens = Arrays.asList(
				ItemID.PET_KITTEN, ItemID.PET_KITTEN_1556,
				ItemID.PET_KITTEN_1557, ItemID.PET_KITTEN_1558,
				ItemID.PET_KITTEN_1559, ItemID.PET_KITTEN_1560,
				ItemID.HELLKITTEN);
		return kittens.get(new Random().nextInt(kittens.size()));
	}

	private int getRandomNPCKitten()
	{
		List<Integer> kittens = Arrays.asList(
				NpcID.KITTEN, NpcID.KITTEN_5591,
				NpcID.KITTEN_5592, NpcID.KITTEN_5593,
				NpcID.KITTEN_5594, NpcID.KITTEN_5595,
				NpcID.KITTEN_5596, NpcID.HELLKITTEN);
		return kittens.get(new Random().nextInt(kittens.size()));
	}
}