package engine;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

public class Engine extends Canvas implements Runnable {

	private 		final	Input				input;
	private			final	JFrame				frame = new JFrame();

	private			final	boolean				separateWindow;
	public	static			int					width;
	public	static			int					height;
	private					String				title;
	private					Color				bgColor;
	private					int					tps;
	public	static			int					scale;
	private					String				entitiesLocation;
	private					String				cameraEntityName;
	private					String				physicsEngineName;
	public	static			boolean				debug;

	private					boolean				running;
	private	static	final	ArrayList<Entity>	entities = new ArrayList<>();
	public	static			Vec2				cameraPos = new Vec2();
	private					PhysicsEngine		physicsEngine;


	public Engine(boolean separateWindow) {

		// I don't know what this does, but it stops the flickering
		Toolkit.getDefaultToolkit().setDynamicLayout(false);

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
			properties.load(new FileReader("engine/config.properties"));

			width = Integer.parseInt(properties.getProperty("width", "800"));
			height = Integer.parseInt(properties.getProperty("height", "600"));
			title = properties.getProperty("title", "Engine");
			String bgColorS = properties.getProperty("bg_color", "34, 37, 41");
			tps = Integer.parseInt(properties.getProperty("tps", "60"));
			scale = Integer.parseInt(properties.getProperty("scale", "4"));
			entitiesLocation = properties.getProperty("entities", "game/entities");
			physicsEngineName = properties.getProperty("physics_engine", "default");
			debug = Boolean.parseBoolean(properties.getProperty("debug", "false"));

			String[] colors = bgColorS.split(",");
			bgColor = new Color(Integer.parseInt(colors[0]), Integer.parseInt(colors[1]), Integer.parseInt(colors[2]));
		} catch(IOException e) {
			System.out.println("Could not find config.properties");
		}
	}

	private void loadEntities() {
		Path folderPath = Paths.get(entitiesLocation);
		try(DirectoryStream<Path> directoryStream = Files.newDirectoryStream(folderPath)) {
			for(Path file : directoryStream) {
				if(Files.isRegularFile(file)) {
					Properties properties = new Properties();
					properties.load(new FileInputStream(file.toString()));

					Entity entity;
					String type = properties.getProperty("type");
					double posX = Double.parseDouble(properties.getProperty("pos_x", "0"));
					double posY = Double.parseDouble(properties.getProperty("pos_y", "0"));
					double sizeX = Double.parseDouble(properties.getProperty("size_x", "16"));
					double sizeY = Double.parseDouble(properties.getProperty("size_y", "16"));
					String textureLocation = properties.getProperty("texture",
							"engine/res/missing.png");
					String code = properties.getProperty("code");
					String collidesWithString = properties.getProperty("collides_with", "");
					String[] collidesWithArray = collidesWithString.split(",");
					ArrayList<String> collidesWith = new ArrayList<>(Arrays.asList(collidesWithArray));
					Vec2 pos = new Vec2(posX, posY);
					Vec2 size = new Vec2(sizeX, sizeY);
					BufferedImage texture;
					try {
						 texture = ImageIO.read(new File(textureLocation));
					} catch(IOException e) {
						System.out.println("Could not find " + textureLocation + " texture: " + e);
						texture = ImageIO.read(new File("engine/res/missing.png"));
					}

					switch (type) {
						case "BasicEntity" -> {
							if(code == null)
								entity = new BasicEntity(pos, size, texture, collidesWith);
							else {
								code = code.replace('/', '.');
								Class<?> entityClass = loadClass(code);
								entity = (BasicEntity) entityClass
										.getDeclaredConstructor(Vec2.class, Vec2.class, BufferedImage.class, ArrayList.class)
										.newInstance(pos, size, texture, collidesWith);
							}
						}
						case "TopDown" -> {
							double speed = Double.parseDouble(properties.getProperty("speed", "100"));
							if(code == null)
								entity = new TopDown(pos, size, texture, collidesWith, speed);
							else {
								code = code.replace('/', '.');
								Class<?> entityClass = loadClass(code);
								entity = (TopDown) entityClass
										.getDeclaredConstructor(Vec2.class, Vec2.class, BufferedImage.class, ArrayList.class, double.class)
										.newInstance(pos, size, texture, collidesWith, speed);
							}
						}
						case "Platformer" -> {
							double speed = Double.parseDouble(properties.getProperty("speed", "100"));
							double jumpSpeed = Double.parseDouble(properties.getProperty("jump_speed", "150"));
							double gravity = Double.parseDouble(properties.getProperty("gravity", "400"));
							if(code == null)
								entity = new Platformer(pos, size, texture, collidesWith, speed, jumpSpeed, gravity);
							else {
								code = code.replace('/', '.');
								Class<?> entityClass = loadClass(code);
								entity = (Platformer) entityClass
										.getDeclaredConstructor(Vec2.class, Vec2.class, BufferedImage.class, ArrayList.class, double.class, double.class, double.class)
										.newInstance(pos, size, texture, collidesWith, speed, jumpSpeed, gravity);
							}
						}
						case "World" -> {
							String worldCSVFile = properties.getProperty("world_csv_file");
							int tileSize = Integer.parseInt(properties.getProperty("tile_size", "16"));
							File worldCSV = new File(worldCSVFile);
							if(code == null)
								entity = new World(texture, worldCSV, tileSize);
							else {
								code = code.replace('/', '.');
								Class<?> entityClass = loadClass(code);
								entity = (World) entityClass
										.getDeclaredConstructor(BufferedImage.class, File.class, int.class)
										.newInstance(texture, worldCSV, tileSize);
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
		} catch(Exception e) {
			System.out.println("Could not load folder " + entitiesLocation + ". Cause: " + e);
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
		Thread engineThread = new Thread(this);
		engineThread.start();
	}

	@Override
	public void run() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		int frames = 0;
		double sps = 0;

		while(running) {
			long now = System.nanoTime();
			double delta = (now - lastTime) / 1_000_000_000.;
			if(delta >= 1./tps) {
				update(delta);
				lastTime = now;
				sps += 1. / tps;
			}
			render();
			frames ++;
			render();
			if (System.currentTimeMillis() - timer >= 1000) {
				timer += 1000;
				if(separateWindow)
					frame.setTitle(title + " | " + frames + " fps" + " | " + Math.round(sps * tps) + " tps");
				frames = 0;
				sps = 0;
			}
		}
	}

	private void update(double delta) {
		width = getWidth();
		height = getHeight();
		physicsEngine.checkCollisions(entities);
		for(Entity entity : entities) {
			entity.update(delta, input);
			if(entity.toString().equals(cameraEntityName)) {
				cameraPos = new Vec2(entity.getPos());
			}
		}
	}

	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(2);
			return;
		}
		Graphics g = bs.getDrawGraphics();

		g.setColor(bgColor);
		g.fillRect(0, 0, width, height);

		for(Entity entity : entities)
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

	public static void main(String[] args) {
		new Engine(true);
	}
}
