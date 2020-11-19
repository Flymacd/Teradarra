package us.schmee.game.gfx.ui.buttons;

import us.schmee.game.Game;

public class Button {
	
	private int x, y, width, height, id;
	private String string;
	private boolean hovered;
	
	public Button(int x, int y, String string) {
		this.x = x;
		this.y = y;
		this.string = string;
		this.width = (string.length() * 8) * Game.SCALE;
		this.height = 8 * Game.SCALE;
		this.hovered = false;
	}
	
	public Button(int x, int y, String string, int id) {
		this.x = x;
		this.y = y;
		this.string = string;
		this.width = (string.length() * 8) * Game.SCALE;
		this.height = 8 * Game.SCALE;
		this.hovered = false;
		this.id = id;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean isHovered() {
		return hovered;
	}

	public void setHovered(boolean hovered) {
		this.hovered = hovered;
	}

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
