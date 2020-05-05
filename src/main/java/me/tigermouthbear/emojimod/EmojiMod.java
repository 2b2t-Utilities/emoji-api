package me.tigermouthbear.emojimod;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(modid = EmojiMod.MODID, name = EmojiMod.NAME, version = EmojiMod.VERSION)
public class EmojiMod {
	public static final String MODID = "emojimod";
	public static final String NAME = "Emoji Mod";
	public static final String VERSION = "1.0";
	public static final Logger log = LogManager.getLogger(NAME);

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		File dir = new File("emoji");
		if(!dir.exists()) dir.mkdir();
		try {
			Emojis.load();
		} catch(Exception ignored) {  }
	}
}
