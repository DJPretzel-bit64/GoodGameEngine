package engine;

import engine.entity.Menu;
import engine.entity.*;
import engine.physics.DefaultPhysicsEngine;
import engine.physics.PhysicsEngine;
import engine.utility.Input;
import engine.utility.Vec2;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Engine extends Canvas {

	private 		final	Input				input;
	private			final	JFrame				frame = new JFrame();

	private			final	boolean				separateWindow;
	public	static			int					width;
	public	static			int					height;
	private					int					initialWidth;
	private					int					initialHeight;
	private					String				title;
	private					Color				bgColor;
	private					int					tps;
	public	static			int					scale;
	private					int					initialScale;
	private					boolean				dynamicScale;
	public	static			int					numLayers;
	private					String				entitiesLocation;
	private					String				cameraEntityName;
	private					String				physicsEngineName;
	public	static			boolean				debug;
	public	static			boolean				overview;
	public	static			String				layout;
	public	static			String				jarPath = "";

	private					boolean				running;
	private					boolean				updateFinished;
	private					boolean				linux;
	private					double				sps;
	private	static	final	ArrayList<Entity>	entities = new ArrayList<>();
	private	static	final	ArrayList<Entity>	addList = new ArrayList<>();
	private static	final	ArrayList<Entity>	removeList = new ArrayList<>();
	public	static			Vec2				cameraPos = new Vec2();
	private					PhysicsEngine		physicsEngine;


	public Engine(boolean separateWindow) {

		if(isRunningFromJAR())
			jarPath = new File(Engine.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParent().replace("%20", " ") + "/";
		Toolkit.getDefaultToolkit().setDynamicLayout(false);
		String os = System.getProperty("os.name").toLowerCase();
		if(os.contains("nix") || os.contains("nux"))
			linux = true;

		this.separateWindow = separateWindow;
		loadProperties();
		loadEntities();
		loadPhysicsEngine();

		input = new Input();
		this.setPreferredSize(new Dimension(width, height));
		this.addKeyListener(input);
		this.addMouseListener(input);
		this.addMouseMotionListener(input);

		if(separateWindow) {
			frame.setTitle(title);
			frame.addKeyListener(input);
			frame.addMouseListener(input);
			frame.addMouseMotionListener(input);
			frame.add(this);
			frame.pack();
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);

			start();
		}
		else
			frame.dispose();
	}

	private void loadProperties() {
		try {
			Properties properties = new Properties();
			properties.load(new FileReader(jarPath + "engine/config.properties"));

			width = initialWidth = Integer.parseInt(properties.getProperty("width", "800"));
			height = initialHeight = Integer.parseInt(properties.getProperty("height", "600"));
			title = properties.getProperty("title", "Engine");
			String bgColorS = properties.getProperty("bg_color", "34, 37, 41");
			tps = Integer.parseInt(properties.getProperty("tps", "60"));
			scale = initialScale = Integer.parseInt(properties.getProperty("scale", "4"));
			dynamicScale = Boolean.parseBoolean(properties.getProperty("dynamic_scale", "true"));
			numLayers = Integer.parseInt(properties.getProperty("num_layers", "10"));
			entitiesLocation = properties.getProperty("entities", "game/entities");
			physicsEngineName = properties.getProperty("physics_engine", "default");
			debug = Boolean.parseBoolean(properties.getProperty("debug", "false"));
			overview = Boolean.parseBoolean(properties.getProperty("overview", "false"));
			layout = properties.getProperty("layout", "qwerty");

			String[] colors = bgColorS.split(",");
			bgColor = new Color(Integer.parseInt(colors[0]), Integer.parseInt(colors[1]), Integer.parseInt(colors[2]));
		} catch(IOException e) {
			System.out.println("Could not find config.properties");
		}
	}

	private void loadEntities() {
		Path folderPath = Paths.get(jarPath + entitiesLocation);
		try(DirectoryStream<Path> directoryStream = Files.newDirectoryStream(folderPath)) {
			for(Path file : directoryStream) {
				if(Files.isRegularFile(file)) {
					Properties properties = new Properties();
					properties.load(new FileInputStream(file.toString()));

					Entity entity;
					String type = properties.getProperty("type", "null");
					double posX = Double.parseDouble(properties.getProperty("pos_x", "0"));
					double posY = Double.parseDouble(properties.getProperty("pos_y", "0"));
					double sizeX = Double.parseDouble(properties.getProperty("size_x", "16"));
					double sizeY = Double.parseDouble(properties.getProperty("size_y", "16"));
					String textureLocation = jarPath + properties.getProperty("texture", "engine/res/missing.png");
					String code = properties.getProperty("code");
					String collidesWithString = properties.getProperty("collides_with", "");
					int layer = Integer.parseInt(properties.getProperty("layer", "-1"));
					boolean checkCollisions = Boolean.parseBoolean(properties.getProperty("check_collisions", "true"));
					boolean initiateAutomatically = Boolean.parseBoolean(properties.getProperty("initiate_automatically", "true"));
					String[] collidesWithArray = collidesWithString.split(",");
					ArrayList<String> collidesWith = new ArrayList<>(Arrays.asList(collidesWithArray));
					Vec2 pos = new Vec2(posX, posY);
					Vec2 size = new Vec2(sizeX, sizeY);
					BufferedImage texture;
					try {
						 texture = ImageIO.read(new File(textureLocation));
					} catch(IOException e) {
						System.out.println("Could not find " + textureLocation + " texture: " + e);
						texture = ImageIO.read(new File(jarPath + "/engine/res/missing.png"));
					}

					switch(type) {
						case "BasicEntity" -> {
							if(code == null)
								entity = new BasicEntity(pos, size, texture, collidesWith, layer, checkCollisions, initiateAutomatically);
							else {
								code = code.replace('/', '.');
								Class<?> entityClass = loadClass(code);
								entity = (BasicEntity) entityClass
										.getDeclaredConstructor(Vec2.class, Vec2.class, BufferedImage.class, ArrayList.class, int.class, boolean.class, boolean.class)
										.newInstance(pos, size, texture, collidesWith, layer, checkCollisions, initiateAutomatically);
							}
						}
						case "TopDown" -> {
							double speed = Double.parseDouble(properties.getProperty("speed", "100"));
							if(code == null)
								entity = new TopDown(pos, size, texture, collidesWith, layer, checkCollisions, initiateAutomatically, speed);
							else {
								code = code.replace('/', '.');
								Class<?> entityClass = loadClass(code);
								entity = (TopDown) entityClass
										.getDeclaredConstructor(Vec2.class, Vec2.class, BufferedImage.class, ArrayList.class, int.class, boolean.class, boolean.class, double.class)
										.newInstance(pos, size, texture, collidesWith, layer, checkCollisions, initiateAutomatically, speed);
							}
						}
						case "Platformer" -> {
							double speed = Double.parseDouble(properties.getProperty("speed", "100"));
							double jumpSpeed = Double.parseDouble(properties.getProperty("jump_speed", "150"));
							double gravity = Double.parseDouble(properties.getProperty("gravity", "400"));
							if(code == null)
								entity = new Platformer(pos, size, texture, collidesWith, layer, checkCollisions, initiateAutomatically, speed, jumpSpeed, gravity);
							else {
								code = code.replace('/', '.');
								Class<?> entityClass = loadClass(code);
								entity = (Platformer) entityClass
										.getDeclaredConstructor(Vec2.class, Vec2.class, BufferedImage.class, ArrayList.class, int.class, boolean.class, boolean.class, double.class, double.class, double.class)
										.newInstance(pos, size, texture, collidesWith, layer, checkCollisions, initiateAutomatically, speed, jumpSpeed, gravity);
							}
						}
						case "World" -> {
							String worldCSVFile = jarPath + properties.getProperty("world_csv_file");
							int tileSize = Integer.parseInt(properties.getProperty("tile_size", "16"));
							File worldCSV = new File(worldCSVFile);
							if(code == null)
								entity = new World(texture, checkCollisions, initiateAutomatically, worldCSV, tileSize, layer);
							else {
								code = code.replace('/', '.');
								Class<?> entityClass = loadClass(code);
								entity = (World) entityClass
										.getDeclaredConstructor(BufferedImage.class, boolean.class, boolean.class, File.class, int.class, int.class)
										.newInstance(texture, checkCollisions, initiateAutomatically, worldCSV, tileSize, layer);
							}
						}
						case "Menu" -> {
							if(code == null)
								entity = new Menu(pos, size, texture, initiateAutomatically);
							else {
								code = code.replace('/', '.');
								Class<?> entityClass = loadClass(code);
								entity = (Menu) entityClass
										.getDeclaredConstructor(Vec2.class, Vec2.class, BufferedImage.class, boolean.class)
										.newInstance(pos, size, texture, initiateAutomatically);
							}
						}
						default -> {
							assert code != null;
							code = code.replace('/', '.');
							Class<?> entityClass = loadClass(code);
							entity = (Entity) entityClass
									.getDeclaredConstructor()
									.newInstance();
						}
					}
					entities.add(entity);

					if(properties.getProperty("follow", "false").equals("true"))
						cameraEntityName = entity.toString();
				}
			}

			for(Entity entity : entities)
				if(entity.shouldInitiate())
					entity.init();
		} catch(Exception e) {
			JFrame frame = new JFrame("Failed");
			frame.setVisible(true);
			frame.add(new JLabel(e + e.getMessage()));
			System.out.println("Could not load folder " + entitiesLocation + ". Cause: " + e);
			e.printStackTrace();
		}
	}

	private void loadPhysicsEngine() {
		if(physicsEngineName.equals("default")) {
			physicsEngine = new DefaultPhysicsEngine();
		}
		else {
			try {
				physicsEngineName = physicsEngineName.replace('/', '.');
				Class<?> entityClass = loadClass(physicsEngineName);
				physicsEngine = (PhysicsEngine) entityClass
						.getDeclaredConstructor()
						.newInstance();
			} catch(Exception e) {
				System.out.println("Error loading PhysicsClass: " + e);
			}
		}
	}

	public synchronized void start() {
		running = true;
		Thread renderThread = new Thread(render);
		Thread updateThread = new Thread(update);
		renderThread.start();
		updateThread.start();
	}

	Runnable render = new Runnable() {
		@Override
		public void run() {
			long timer = System.currentTimeMillis();
			int frames = 0;

			while(running) {
				if(updateFinished) {
					try {
						render();
						updateFinished = false;
					} catch(ConcurrentModificationException ignored){}
					frames++;
				}
				if (System.currentTimeMillis() - timer >= 1000) {
					timer += 1000;
					if(separateWindow)
						frame.setTitle(title + " | " + frames + " fps" + " | " + Math.round(sps * tps) + " tps");
					frames = 0;
					sps = 0;
				}
			}
		}
	};

	Runnable update = new Runnable() {
		@Override
		public void run() {
			long lastTime = System.nanoTime();

			while(running) {
				long now = System.nanoTime();
				double delta = (now - lastTime) / 1_000_000_000.;
				if(delta >= 1./tps) {
					updateFinished = false;
					update(delta);
					lastTime = now;
					sps += 1./tps;
					updateFinished = true;
				}
			}
		}
	};

	private void update(double delta) {
		width = getWidth();
		height = getHeight();

		if(dynamicScale) {
			double ratio = Math.min((double)width / initialWidth, (double)height / initialHeight);
			scale = Math.max((int)(ratio * initialScale), 1);
		}

		physicsEngine.checkCollisions(entities);
		for(Entity entity : entities) {
			if(entity.isInitiated()) {
				if(entity.toString().equals(cameraEntityName))
					cameraPos = new Vec2(entity.getPos());
				entity.update(delta, input);
			}
		}

		updateEntityList();
	}

	private void render() throws ConcurrentModificationException {
		if(linux)
			Toolkit.getDefaultToolkit().sync();

		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(2);
			return;
		}
		Graphics g = bs.getDrawGraphics();

		g.setColor(bgColor);
		g.fillRect(0, 0, width, height);

		for(Entity entity : entities)
			if(entity.getLayer() == -1 && entity.isInitiated())
				entity.render(g);

		for(int i = 0; i < numLayers; i++)
			for(Entity entity : entities)
				if(entity.getLayer() == i && entity.isInitiated())
					entity.render(g);

		g.dispose();
		bs.show();
	}

	private Class<?> loadClass(String className) throws Exception {
		URLClassLoader classLoader = new URLClassLoader(new URL[]{new File("").toURI().toURL()});
		return Class.forName(className, true, classLoader);
	}

	public static ArrayList<Entity> getEntities() {
		return new ArrayList<>(entities);
	}

	public static Entity getEntity(String entityName) {
		for(Entity entity : entities) {
			if(entity.getClass().getName().equals(entityName))
				return entity;
		}
		return null;
	}

	private void updateEntityList() {
		entities.addAll(addList);
		entities.removeAll(removeList);
		addList.clear();
		removeList.clear();
	}

	public static void addEntity(Entity entity) {
		addList.add(entity);
	}

	public static void addEntities(ArrayList<Entity> entities) {
		addList.addAll(entities);
	}

	public static void removeEntity(Entity entity) {
		removeList.add(entity);
	}

	public static void removeEntities(ArrayList<Entity> entities) {
		removeList.addAll(entities);
	}

	private boolean isRunningFromJAR() {
		String className = Engine.class.getName().replace('.', '/');
		String classJar = Objects.requireNonNull(Engine.class.getResource("/" + className + ".class")).toString();
		return classJar.startsWith("jar:");
	}

	public static void main(String[] args) {
		new Engine(true);
	}
}
