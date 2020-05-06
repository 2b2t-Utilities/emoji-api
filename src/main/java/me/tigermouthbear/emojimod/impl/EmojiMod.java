package me.tigermouthbear.emojimod.impl;

import me.tigermouthbear.emojimod.api.Emojis;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

/**
 * @author Tigermouthbear
 * 5/5/20
 */

@Mod(modid = EmojiMod.MODID, name = EmojiMod.NAME, version = EmojiMod.VERSION)
public class EmojiMod {
	public static final String MODID = "emojimod";
	public static final String NAME = "Emoji Mod";
	public static final String VERSION = "1.0";

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		try {
			Emojis.load();
		} catch(Exception ignored) {
		}
	}
}
