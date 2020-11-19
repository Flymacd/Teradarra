package us.schmee.game;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;

import javax.swing.JFrame;

import us.schmee.game.entites.mobs.Player;
import us.schmee.game.entites.mobs.PlayerMP;
import us.schmee.game.files.ReadObjects;
import us.schmee.game.files.SaveObjects;
import us.schmee.game.files.settings.Settings;
import us.schmee.game.gamestates.State;
import us.schmee.game.gfx.Colors;
import us.schmee.game.gfx.Font;
import us.schmee.game.gfx.ImageLoader;
import us.schmee.game.gfx.Screen;
import us.schmee.game.gfx.SpriteSheet;
import us.schmee.game.gfx.ui.CharCreationMenu;
import us.schmee.game.gfx.ui.HUD;
import us.schmee.game.gfx.ui.InGameGUI;
import us.schmee.game.gfx.ui.LoadGameSingleplayer;
import us.schmee.game.gfx.ui.MainMenuGUI;
import us.schmee.game.gfx.ui.MultiplayerMenu;
import us.schmee.game.gfx.ui.SettingsMenu;
import us.schmee.game.gfx.ui.TypeMenu;
import us.schmee.game.handlers.input.InputHandler;
import us.schmee.game.handlers.input.Mouse;
import us.schmee.game.handlers.window.WindowHandler;
import us.schmee.game.level.Level;
import us.schmee.game.net.GameClient;
import us.schmee.game.net.GameServer;
import us.schmee.game.net.packets.Packet00Login;

@SuppressWarnings("serial")
public class Game extends Canvas implements Runnable {

	public static final int WIDTH = 280, HEIGHT = WIDTH / 14 * 9, SCALE = 5;
	public static final String NAME = "Teradarra";
	public static Game game;
	
	public JFrame frame;
	
	public boolean running = false, firstRun = true, gettingSettingsName = false;
	
	
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	private int[] colors = new int[6 * 6 * 6];
	
	// Game States
	public State state = State.mainmenu;
	
	//private SpriteSheet spriteSheet = new SpriteSheet("/sprite_sheet.png");
	public Screen screen;
	public InputHandler input;
	public Mouse mouse;
	public WindowHandler windowHandler;
	public Level level;
	public Player player;
	
	public int xOffset = 0, yOffset = 0;
	
	// Tick Count
	public int tickCount = 0;
	
	// Files
	public SaveObjects saveObjects;
	public ReadObjects readObjects;
	
	// Settings
	public Settings settings;
	public File[] settingsFiles;
	public String settingsFile = "";
	
	// Saves
	public File[] loadGameFiles;
	
	// GUI
	public MainMenuGUI mainMenu;
	public MultiplayerMenu mpMenu;
	public SettingsMenu sMenu;
	public InGameGUI igGUI;
	public CharCreationMenu ccMenu;
	public HUD hud;
	public TypeMenu typeMenu;
	public LoadGameSingleplayer lgs;
	
	public int hosting = 0;
	
	public boolean displayInGameGUI = false;
	
	// Multiplayer
	public GameClient client;
	public GameServer server;
	public boolean isMultiplayer = false;
	
	public Game() {
		setMinimumSize(new Dimension(WIDTH * SCALE,HEIGHT * SCALE));
		setMaximumSize(new Dimension(WIDTH * SCALE,HEIGHT * SCALE));
		setPreferredSize(new Dimension(WIDTH * SCALE,HEIGHT * SCALE));
		
		frame = new JFrame(NAME);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		
		frame.add(this, BorderLayout.CENTER);
		frame.pack();
		
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setFocusable(true);
		
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

		// Create a new blank cursor.
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
		    cursorImg, new Point(0, 0), "blank cursor");

		// Set the blank cursor to the JFrame.
		frame.getContentPane().setCursor(blankCursor);
		
		frame.requestFocus();
	}
	
	public void init() {
		game = this;
		int index = 0;
		for (int r = 0; r < 6; r++) {
			for (int g = 0; g < 6; g++) {
				for (int b = 0; b < 6; b++) {
					int rr = (r * 255/5);
					int gg = (g * 255/5);
					int bb = (b * 255/5);
					
					colors[index++] = rr << 16 | gg << 8 | bb;
				}
			}
		}
		
		screen = new Screen(WIDTH, HEIGHT, new SpriteSheet(ImageLoader.loadImage("/spritesheets/sprite_sheet.png")));
		mpMenu = new MultiplayerMenu(this);
		sMenu = new SettingsMenu(this);
		igGUI = new InGameGUI(this);
		hud = new HUD(this);
		lgs = new LoadGameSingleplayer(this);
		typeMenu = new TypeMenu(this);
		saveObjects = new SaveObjects(this);
		readObjects = new ReadObjects(this);
		ccMenu = new CharCreationMenu(this);
		input = new InputHandler(this);
		settings = new Settings(this);
		
		readObjects.loadFirstRun(System.getProperty("user.dir") + "/saves/fr.sav");
		
		if (firstRun) {
			File dir = new File("saves");
			dir.mkdir();
			dir = new File("saves/settings");
			dir.mkdir();
			saveObjects.newSaveFirstRun(System.getProperty("user.dir") + "/saves/fr.sav", false);
		} else {
			settingsFiles = readObjects.finder(System.getProperty("user.dir") + "/saves/settings/");
			if (settingsFiles.length > 0) {
				settingsFile = settingsFiles[0].getName();
				settings.load(settingsFile);
			}
		}
	}
	
	public synchronized void start() {
		running = true;
		new Thread(this).start();
		
		level = new Level("/levels/menu_bg.png");
		
		mouse = new Mouse(this);
		mainMenu = new MainMenuGUI(this);
		
	}
	
	public synchronized void stop() {
		running = false;
	}
	
	public void run() {
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000D/60D;
		int ticks = 0;
		int frames = 0;
		long lastTimer = System.currentTimeMillis();
		double delta = 0;
		
		init();
		
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / nsPerTick;
			lastTime = now;
			boolean shouldRender = true;
			
			while (delta >= 1) {
				ticks++;
				tick();
				delta -= 1;
				shouldRender = true;
			}
			
			try { 
				Thread.sleep(2);;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (shouldRender) {
				frames++;
				render();
			}
			
			if (System.currentTimeMillis() - lastTimer > 1000) {
				if (settings.isShowFps()) {
					frame.setTitle(NAME + " | FPS: " + frames + " | UPS: " + ticks); 
				} else {
					frame.setTitle(NAME);
				}
				lastTimer = System.currentTimeMillis();
				frames = 0;
				ticks = 0;
			}
		}
	}
	
	public void checkForSaveFiles() {
		if (!firstRun) {
			loadGameFiles = readObjects.finder(System.getProperty("user.dir") + "/saves/singleplayer/");
		}
	}
	
	public void load(State state, Level level) {
		this.level = level;
		this.state = state;
	}
	
	
	public void singlePlayer() {
		newGame("");
//		state = State.loadgame;
	}
	
	public void newGame(String username) {
		state = State.loading;
		level = new Level("/levels/Maptest001.png");
		player = new Player(level, 2266 * 8, 836 * 8, input, username, level.getIdNewPlayer());
		level.addEntity(player);
		isMultiplayer = false;
		state = State.game;
	}
	
	public void loadGame(File file) {
		state = State.loading;
		level = new Level("/levels/Maptest001.png");
		player = readObjects.loadSingleplayerGame(file.getAbsolutePath());
		level.addEntity(player);
		isMultiplayer = false;
		state = State.game;
	}
	
	public void multiPlayer() {		
		isMultiplayer = true;
		state = State.multiplayer;
	}
	
	public void createCharacter(int color, String name, String ip) {
		if (hosting == 1) {
			hostMultiplayer(color, name);
		} else {
			joinMultiplayer(color, name, ip);
		}
	}
	
	public void hostMultiplayer(int color, String name) {
		level = new Level("/levels/Maptest001.png");
		server = new GameServer(this);
		server.start();
		//client = new GameClient(this, JOptionPane.showInputDialog(this, "Please Enter an IP Address"));
		client = new GameClient(this, "127.0.0.1");
		client.start();
		windowHandler = new WindowHandler(this);
		player = new PlayerMP(level, 2266 * 8, 836 * 8, input, name, null, -1, level.getIdNewPlayer());
		frame.setTitle(NAME + " | " + player.getUsername());
		player.setColor(Colors.get(-1, 111, color, 543));
		Packet00Login loginPacket = new Packet00Login(player.getUsername(), player.x, player.y, player.getColor(), 0);
		if (server != null) {
			server.addConnection((PlayerMP) player, loginPacket);
		}
		loginPacket.writeData(client);
		level.addEntity(player);
		state = State.game;
	}
	
	public void joinMultiplayer(int color, String name, String ip) {
		level = new Level("/levels/Maptest001.png");
		client = new GameClient(this, ip);
		//client = new GameClient(this, "127.0.0.1");
		client.start();
		windowHandler = new WindowHandler(this);
		player = new PlayerMP(level, 2266 * 8, 836 * 8, input, name, null, -1, 0);
		player.setColor(Colors.get(-1, 111, color, 543));
		Packet00Login loginPacket = new Packet00Login(player.getUsername(), player.x, player.y, player.getColor(), 0);
		if (server != null) {
			server.addConnection((PlayerMP) player, loginPacket);
		}
		loginPacket.writeData(client);
		level.addEntity(player);
		state = State.game;
	}
	
	public void settings() {
		state = State.settings;
	}
	
	public void quit() {
		System.exit(0);
	}
	
	public void tick() {
		mouse.tick();
		level.tick();
		
		tickCount++;
	}
	
	public void mainMenu() {
		game.state = State.loading;
		game.displayInGameGUI = false;
		level.entities.removeAll(level.entities);
		player = null;
		level = new Level("/levels/menu_bg.png");
		game.state = State.mainmenu;
	}
	
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.black);
		
		xOffset = screen.xOffset;
		yOffset = screen.yOffset;
		
		for (int y = 0; y < screen.height; y++) {
			for (int x = 0; x < screen.width; x++) {
				int colorCode = screen.pixels[x + y * screen.width];
				if (colorCode < 255) pixels[x + y * WIDTH] = colors[colorCode];
			}
		}
		
		if (state == State.mainmenu) {
			level.renderTiles(screen, screen.width / 2, screen.width / 2);
			mainMenu.render(screen);
		} else if (state == State.game) {
			xOffset = player.x - screen.width / 2;
			yOffset = player.y - screen.height / 2;
			
			level.renderTiles(screen, xOffset, yOffset);
			level.renderEntities(screen);
			hud.render(screen, xOffset, yOffset);
			
		} else if (state == State.multiplayer) {
			level.renderTiles(screen, screen.width / 2, screen.width / 2);
			mpMenu.render(screen);
		} else if (state == State.settings) {
			level.renderTiles(screen, screen.width / 2, screen.width / 2);
			sMenu.render(screen);
		} else if (state == State.paused) {
			xOffset = player.x - screen.width / 2;
			yOffset = player.y - screen.height / 2;
			
			level.renderTiles(screen, xOffset, yOffset);
			level.renderEntities(screen);
			hud.render(screen, xOffset, yOffset);
			
			
			Font.render("PAUSED", screen, screen.xOffset + screen.width / 2 - (("PAUSED").length() - 1) / 2 * 11, screen.yOffset + 15, Colors.get(-1, -1, -1, 555), 1);
		} else if (state == State.createchar) {
			level.renderTiles(screen, screen.width / 2, screen.width / 2);
			level.renderEntities(screen);
			ccMenu.render(screen);
		} else if (state == State.typeMenu) {
			level.renderTiles(screen, xOffset, yOffset);
			if (gettingSettingsName) {
				typeMenu.displayText = "Enter a File Name";
			} else if (game.hosting == 1 || game.mouse.typeMenuAccepts < 1 && !gettingSettingsName) {
				typeMenu.displayText = "Enter a Username";
			} else if (game.hosting == 0 && game.mouse.typeMenuAccepts > 0 && !gettingSettingsName) {
				typeMenu.displayText = "Enter an IP Address";
			}
			typeMenu.render(screen);
		} else if (state == State.loadgame) {
			level.renderTiles(screen, xOffset, yOffset);
			lgs.render(screen);
		}
		
		if (displayInGameGUI) {
			igGUI.render(screen, xOffset, yOffset);
		}
		
		mouse.render(screen, xOffset, yOffset);
		
		
		
		g.fillRect(0, 0, getWidth(), getHeight());
		
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		
		g.dispose();
		bs.show();
	}
	
	public static void main(String[] args) {
		new Game().start();
	}
	
}
