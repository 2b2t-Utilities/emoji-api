package me.tigermouthbear.emojimod.impl.mixin;

import me.tigermouthbear.emojimod.api.Emoji;
import me.tigermouthbear.emojimod.api.Emojis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * @author Tigermouthbear
 * 5/5/20
 */

@Mixin(FontRenderer.class)
public abstract class MixinFontRenderer {
	@Shadow
	private boolean randomStyle;
	@Shadow
	private boolean boldStyle;
	@Shadow
	private boolean strikethroughStyle;
	@Shadow
	private boolean underlineStyle;
	@Shadow
	private boolean italicStyle;
	@Shadow
	private boolean unicodeFlag;

	@Shadow
	private int textColor;

	@Shadow
	private float red;
	@Shadow
	private float green;
	@Shadow
	private float blue;
	@Shadow
	private float alpha;

	@Shadow
	protected float posX;
	@Shadow
	protected float posY;

	@Shadow
	public Random fontRandom;

	@Final
	@Shadow
	private int[] colorCode;

	@Shadow
	protected abstract void setColor(float r, float g, float b, float a);

	@Shadow
	public abstract int getCharWidth(char character);

	@Shadow
	protected abstract float renderChar(char ch, boolean italic);

	@Shadow
	protected abstract void doDraw(float f);

	@Shadow
	public abstract int getStringWidth(String text);

	/**
	 * @author Tigermouthbear
	 */
	@Overwrite
	private void renderStringAtPos(String text, boolean shadow) {
		int size = getStringWidth("  ");
		List<Emoji> emojis = new ArrayList<>();

		for(String possile : text.split(":")) {
			if(Emojis.isEmoji(possile)) emojis.add(new Emoji(possile));
		}

		for(Emoji emoji : emojis) {
			if(!shadow) {
				int index = text.indexOf(":" + emoji.getName() + ":");
				if(index == -1) continue;
				int x = getStringWidth(text.substring(0, index));

				Minecraft.getMinecraft().getTextureManager().bindTexture(Emojis.getEmoji(emoji));
				GlStateManager.color(1, 1, 1, alpha);
				Gui.drawScaledCustomSizeModalRect((int) (posX + x), (int) posY, 0, 0, size, size, size, size, size, size);
			}

			text = text.replaceFirst(":" + emoji.getName() + ":", "  ");
		}

		for(int i = 0; i < text.length(); ++i) {
			char c0 = text.charAt(i);

			if(c0 == 167 && i + 1 < text.length()) {
				int i1 = "0123456789abcdefklmnor".indexOf(String.valueOf(text.charAt(i + 1)).toLowerCase(Locale.ROOT).charAt(0));

				if(i1 < 16) {
					this.randomStyle = false;
					this.boldStyle = false;
					this.strikethroughStyle = false;
					this.underlineStyle = false;
					this.italicStyle = false;

					if(i1 < 0 || i1 > 15) {
						i1 = 15;
					}

					if(shadow) {
						i1 += 16;
					}

					int j1 = this.colorCode[i1];
					this.textColor = j1;
					setColor((float) (j1 >> 16) / 255.0F, (float) (j1 >> 8 & 255) / 255.0F, (float) (j1 & 255) / 255.0F, this.alpha);
				} else if(i1 == 16) {
					this.randomStyle = true;
				} else if(i1 == 17) {
					this.boldStyle = true;
				} else if(i1 == 18) {
					this.strikethroughStyle = true;
				} else if(i1 == 19) {
					this.underlineStyle = true;
				} else if(i1 == 20) {
					this.italicStyle = true;
				} else if(i1 == 21) {
					this.randomStyle = false;
					this.boldStyle = false;
					this.strikethroughStyle = false;
					this.underlineStyle = false;
					this.italicStyle = false;
					setColor(this.red, this.blue, this.green, this.alpha);
				}

				++i;
			} else {
				int j = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".indexOf(c0);

				if(this.randomStyle && j != -1) {
					int k = this.getCharWidth(c0);
					char c1;

					while(true) {
						j = this.fontRandom.nextInt("\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".length());
						c1 = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".charAt(j);

						if(k == this.getCharWidth(c1)) {
							break;
						}
					}

					c0 = c1;
				}

				float f1 = j == -1 || this.unicodeFlag ? 0.5f: 1f;
				boolean flag = (c0 == 0 || j == -1 || this.unicodeFlag) && shadow;

				if(flag) {
					this.posX -= f1;
					this.posY -= f1;
				}

				float f = this.renderChar(c0, this.italicStyle);

				if(flag) {
					this.posX += f1;
					this.posY += f1;
				}

				if(this.boldStyle) {
					this.posX += f1;

					if(flag) {
						this.posX -= f1;
						this.posY -= f1;
					}

					this.renderChar(c0, this.italicStyle);
					this.posX -= f1;

					if(flag) {
						this.posX += f1;
						this.posY += f1;
					}

					++f;
				}
				doDraw(f);
			}
		}
	}
}
