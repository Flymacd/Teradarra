package us.schmee.game.gfx;

import java.awt.image.BufferedImage;



public class SpriteSheet {
	
	public String path;
	public int width, height;
	
	public int[] pixels;
	
	public SpriteSheet(BufferedImage image) {
		
		if (image == null) { return; }
		
		this.width = image.getWidth();
		this.height = image.getHeight();
		
		pixels = image.getRGB(0, 0, width, height, null, 0, width);
		
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = (pixels[i] & 0xff) / 64;
		}
	}
	
}
