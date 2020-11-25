package dev.tigr.emojiapi;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.compress.utils.Charsets;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author Tigermouthbear
 * 5/5/20
 */

public class Emojis {
	private static final Minecraft MC = Minecraft.getMinecraft();
	private static final String VERSION_URL = "https://raw.githubusercontent.com/2b2t-Utilities/emojis/master/version.json";
	private static final String ZIP_URL = "https://github.com/2b2t-Utilities/emojis/archive/master.zip";
	private static final String FOLDER = "emoji";
	private static final File LOCAL_VERSION = new File(FOLDER + File.separator + "version.json");
	private static final Map<String, ResourceLocation> EMOJI_MAP = new HashMap<>();

	static {
		load();
	}

	private static void load() {
		File dir = new File("emoji");
		if(!dir.exists()) dir.mkdir();

		try {
			if(!LOCAL_VERSION.exists()) update_emojis();
			else {
				// load version info
				JsonObject globalVer = read(new URL(VERSION_URL).openStream());
				JsonObject localVer = read(new FileInputStream(LOCAL_VERSION));

				// make sure current version is latest
				if(!globalVer.has("version")) update_emojis();
				else {
					if(globalVer.get("version").getAsInt() != localVer.get("version").getAsInt()) update_emojis();
				}
			}
		} catch(Exception ignored) {  }

		try {
			Files.list(new File(FOLDER).toPath()).filter(path -> path.endsWith(".png")).forEach(path -> {
				try {
					addEmoji(path);
				} catch(Exception ignored) {  }
			});
		} catch(IOException e) {  }
	}

	private static JsonObject read(InputStream stream) {
		Gson gson = new Gson();
		JsonObject jsonObject = null;

		try {
			String json = IOUtils.toString(stream, Charsets.UTF_8);
			jsonObject = gson.fromJson(json, JsonObject.class);
		} catch(IOException e) {
			e.printStackTrace();
		}

		return jsonObject;
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
	}

	private static void addEmoji(Path path) {
		File file = path.toFile();
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
