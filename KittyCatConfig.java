package com.d4;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("KittyCat")
public interface KittyCatConfig extends Config
{
	@ConfigItem(
			keyName = "noConfigs",
			name = "Why you need config? Is just Kitty Cats.",
			description = "NO CONFIGS! JUST KITTY CAT MODE"
	)
	default void noConfigs() {}
}
