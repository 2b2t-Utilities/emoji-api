package me.tigermouthbear.emojimod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Emojis {
	private static final Minecraft MC = Minecraft.getMinecraft();
	private static final String VERSION_URL = "https://raw.githubusercontent.com/Tigermouthbear/emoji-mod-resources/master/version.json";
	private static final String ZIP_URL = "https://github.com/Tigermouthbear/emoji-mod-resources/archive/master.zip";
	private static final String FOLDER = "emoji";
	private static final File LOCAL_VERSION = new File(FOLDER + File.separator + "version.json");
	private static final Map<String, ResourceLocation> EMOJI_MAP = new HashMap<>();

	public static void load() throws IOException {
		if(!LOCAL_VERSION.exists()) update_emojis();
		else {
			// load version info
			JSONObject globalVer = new JSONObject(new JSONTokener(new URL(VERSION_URL).openStream()));
			JSONObject localVer = new JSONObject(new JSONTokener(new FileInputStream(LOCAL_VERSION)));

			// make sure current version is latest
			if(!globalVer.has("version")) update_emojis();
			else {
				if(globalVer.getInt("version") != localVer.getInt("version")) update_emojis();
			}
		}

		File[] emojis = new File("emoji").listFiles(file -> file.isFile() && file.getName().toLowerCase().endsWith(".png"));
		for(File emoji: emojis) addEmoji(emoji);
	}

	private static void update_emojis() throws IOException {
		ZipInputStream zip = new ZipInputStream(new URL(ZIP_URL).openStream());
		ZipEntry entry = zip.getNextEntry();
		// iterates over entries in the zip file
		while(entry != null) {
			String filePath = FOLDER + File.separator + entry.getName().substring(entry.getName().indexOf("/"));
			if(!entry.isDirectory()) {
				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
				byte[] bytesIn = new byte[4096];
				int read;
				while((read = zip.read(bytesIn)) != -1) {
					bos.write(bytesIn, 0, read);
				}
				bos.close();
			}
			zip.closeEntry();
			entry = zip.getNextEntry();
		}
		zip.close();

		EmojiMod.log.info("Updated local emojis");
	}

	private static void addEmoji(File file) {
		DynamicTexture dynamicTexture;
		try {
			BufferedImage image = ImageIO.read(file);
			dynamicTexture = new DynamicTexture(image);
			dynamicTexture.loadTexture(MC.getResourceManager());
		} catch(Exception ignored) {
			return;
		}

		EMOJI_MAP.put(file.getName().replaceAll(".png", ""), MC.getTextureManager().getDynamicTextureLocation(file.getName().replaceAll(".png", ""), dynamicTexture));
	}

	public static ResourceLocation getEmoji(Emoji emoji) {
		return EMOJI_MAP.get(emoji.getName());
	}

	public static boolean isEmoji(String name) {
		return EMOJI_MAP.containsKey(name);
	}
}
