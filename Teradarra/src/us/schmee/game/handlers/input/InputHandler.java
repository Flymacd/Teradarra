package us.schmee.game.handlers.input;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import us.schmee.game.Game;
import us.schmee.game.gamestates.State;

public class InputHandler implements KeyListener {

	private Game game;
	
	public InputHandler(Game game) {
		this.game = game;
		game.addKeyListener(this);
	}
	
	public class Key {
		private int numTimesPressed = 0;
		private boolean pressed = false;
		
		public int getNumTimesPressed() {
			return numTimesPressed;
		}
		
		public boolean isPressed() {
			return pressed;
		}
		
		public void toggle(boolean isPressed) {
			pressed = isPressed;
			if (pressed) {numTimesPressed++;}
		}
	}
	
	public Key up = new Key(), down = new Key(), left = new Key(), right = new Key(), sprint = new Key(), ctrl = new Key();
	
	public void keyPressed(KeyEvent e) {
		if (game.state == State.game) {
			if (!game.displayInGameGUI) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					if (game.isMultiplayer) {
						game.displayInGameGUI = true;
					} else if (!game.isMultiplayer) {
						game.displayInGameGUI = true;
						game.state = State.paused;
					}
				}
			} else if (game.displayInGameGUI) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					game.displayInGameGUI = false;
				}
				
			}
		} else if (game.state == State.game) {
			if (game.displayInGameGUI) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					game.displayInGameGUI = false;
					if (game.state == State.paused) {
						game.state = State.game;
					}
				}
			}
		}
		
		if (game.state == State.typeMenu) {
			if (e.getKeyCode() == KeyEvent.VK_V && ctrl.isPressed()) {
				Clipboard systemClipboard = getSystemClipboard();
		        DataFlavor dataFlavor = DataFlavor.stringFlavor;

		        if (systemClipboard.isDataFlavorAvailable(dataFlavor)) {
		            Object text = "";
					try {
						text = systemClipboard.getData(dataFlavor);
					} catch (UnsupportedFlavorException | IOException e1) {
						e1.printStackTrace();
					}
		            game.typeMenu.typeString += text;
		        }
			} else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				game.typeMenu.typeString += " ";
			} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				game.mouse.typeMenuAccept();
			} else if (e.getKeyCode() == KeyEvent.VK_PERIOD) { 
				game.typeMenu.typeString += ".";
			} else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
				game.typeMenu.typeString += "";
			} else if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
				toggle(e.getKeyCode(), true);
			} else if (e.getKeyCode() != KeyEvent.VK_BACK_SPACE) {
				game.typeMenu.typeString += KeyEvent.getKeyText(e.getKeyCode());
			} else {
				game.typeMenu.removeLastChar();
			}
		}
		toggle(e.getKeyCode(), true);
	}

	public void keyReleased(KeyEvent e) {
		toggle(e.getKeyCode(), false);
	}

	public void keyTyped(KeyEvent e) {
		
	}
	
	public void toggle(int keyCode, boolean isPressed) {
		if (game.state == State.game) {
			if (keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP) {up.toggle(isPressed);}
			if (keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT) {left.toggle(isPressed);}
			if (keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN) {down.toggle(isPressed);}
			if (keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT) {right.toggle(isPressed);}
			if (keyCode == KeyEvent.VK_SHIFT) {sprint.toggle(isPressed);}
		} else if (game.state == State.typeMenu) {
			if (keyCode == KeyEvent.VK_CONTROL) {ctrl.toggle(isPressed);}
		}
	}
	
	private static Clipboard getSystemClipboard()
    {
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        Clipboard systemClipboard = defaultToolkit.getSystemClipboard();

        return systemClipboard;
    }

}
