package testProgram;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import javax.swing.JFrame;

import baseEngine.Object3D;
import baseEngine.Point3D;
import baseEngine.Polygon3D;
import isometricEngine.IsoCamera;
import isometricEngine.IsoWorld;
import polygonEngine.PolygonCamera;
import polygonEngine.PolygonWorld;
import testProgram.objects.Cube;
import voxelEngine.Particle;
import voxelEngine.Sector;

/*
 * Written By Nikolas Gaub, 8/28/2017
 * 
 * This class is used for testing successfulness of Engine3D. THIS IS NOT A JUNIT TEST SUITE!!!
 * This is simply a class used for testing recent additions to the program.
 */

public class TestViewer extends JFrame {

	private static boolean w;
	private static boolean s;
	private static boolean a;
	private static boolean d;
	private static boolean shift;
	private static boolean space;
	private static boolean running;

	private static JFrame frame;
	private static PolygonCamera cam;
	private static IsoCamera Isocam;

	public static void main(String[] args) {
		frame = new TestViewer();
		cam = new PolygonCamera();
		cam.setWorld(createWorld());
		cam.setWireFrame(false);
		cam.setPosition(new Point3D(0, 1, 0));
		
		Isocam = new IsoCamera();
		Isocam.setWorld(createIsoWorld());
		Isocam.setPosition(new Point3D(0, 1, 0));
		
		frame.add(cam, BorderLayout.CENTER);

		
		//cam.setVoxelMap(createVoxel());
		addKeyControls();

		startRefresh();
	}
	
	private static IsoWorld createIsoWorld() {
		IsoWorld world = new IsoWorld();
		//world.addObject3D(new Cube(new Point3D(1, 1, 1), 2.0));
		for (int i = 0; i < 100; i++) {
			for (int j = 0; j < 100; j++) {
				world.addObject3D(new Cube(new Point3D(i*2, 1, j*2), 2));
			}
		}
		return world;
	}

	private static PolygonWorld createWorld() {
		PolygonWorld world = new PolygonWorld();
		//world.addObject3D(new Cube(new Point3D(0, 0, 10), Sector.MINIMUM_SIZE));
		double[][] map = new double[200][200];
		double seedX = 0;//Math.random() * 1000;
		double seedY = 0;//Math.random() * 1000;
		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map[x].length; y++) {
				
				map[x][y] = 3*Math.sin(0.008*x) + 2*Math.sin(y) + 7*Math.sin(0.3*x) + Math.sin(0.9*y) + 4*Math.sin(0.08*x) + 5*Math.sin(0.07*y);//SimplexNoise.noise(seedX + x/50.0, seedY + y/50.0) * 25;
			}
		}
		Object3D ground = new Object3D();
		for (int x = 0; x < map.length - 1; x++) {
			for (int y = 0; y < map[x].length - 1; y++) {
				Point3D tl = new Point3D(x, map[x][y], y);
				Point3D tr = new Point3D(x+1, map[x+1][y], y);
				Point3D br = new Point3D(x+1, map[x+1][y+1], y+1);
				Point3D bl = new Point3D(x, map[x][y+1], y+1);
				ground.addPolygon(new Polygon3D(tl, tr, br, bl));
			}
		}
		world.addObject3D(ground);
		return world;
	}
	
	private static Sector createVoxel() {
		double voxelSize = Sector.MINIMUM_SIZE * Math.pow(2, 10);
		Sector voxel = new Sector(new Point3D(-voxelSize/2, -voxelSize/2, -voxelSize/2), voxelSize);
		
		for (int i = 0; i < 10; i++) {
			voxel.addParticle(new Particle(new Point3D(num()-2.5, num()-2.5, 7.5+num()), Particle.BASIC_PARTICLE));
		}
		voxel.addParticle(new Particle(new Point3D(0, 0, 10), Particle.BASIC_PARTICLE));
		
		return voxel;
	}
	
	private static double num() { return Math.pow(Math.random()*5, 1);}

	public TestViewer() {
		super("Engine 3D");

		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setVisible(true);
		this.setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor c = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "whoosh");
		getContentPane().setCursor(c);
	}

	public static void addKeyControls() {
		frame.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_W:
					w = true;
					break;
				case KeyEvent.VK_S:
					s = true;
					break;
				case KeyEvent.VK_A:
					a = true;
					break;
				case KeyEvent.VK_D:
					d = true;
					break;
				case KeyEvent.VK_SHIFT:
					shift = true;
					break;
				case KeyEvent.VK_SPACE:
					space = true;
					break;
				case KeyEvent.VK_ESCAPE:
					running = false;
					
					break;
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_W:
					w = false;
					break;
				case KeyEvent.VK_S:
					s = false;
					break;
				case KeyEvent.VK_A:
					a = false;
					break;
				case KeyEvent.VK_D:
					d = false;
					break;
				case KeyEvent.VK_SHIFT:
					shift = false;
					break;
				case KeyEvent.VK_SPACE:
					space = false;
					break;
				}
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
			}

		});
		
		new Thread() {
			@Override
			public void run() {
				running = true;
				while (running) {
					move();
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.exit(0);
			}
		}.start();
	}

	private static int fps;
	
	// continuously refreshes JFrame until program ends.
	public static void startRefresh() {
		new Thread() {
			@Override
			public void run() {
				running = true;
				while (running) {
					frame.validate();
					frame.repaint();

					checkFPS();
					BufferedImage image = new BufferedImage(100, 50, BufferedImage.TYPE_INT_ARGB);
					Graphics g = image.getGraphics();
					g.setColor(Color.black);
					g.drawString("FPS:" + fps, 10, 50);
					cam.setOverlay(image);
					//System.out.println(fps);
					
					checkMouse();
					
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.exit(0);
			}
		}.start();
	}
	
	private static long lastCycle = 0L;
	private static int frames;
	
	private static void checkFPS() {
		
		long current = System.nanoTime();
		frames++;
		
		if (current - lastCycle > 1000000000) {
			lastCycle = current;
			fps = frames;
			frames = 0;
		}
	}
	
	private static final double SPEED = .2;
	
	public static void move() {
		double camAngle = cam.getAngleXZ();
		double cos = Math.cos(camAngle);
		double sin = Math.sin(camAngle);
		if (w) {
			cam.changePosition(-1*SPEED*sin, 0, SPEED*cos);
		}
		if (s) {
			cam.changePosition(SPEED*sin, 0, -1*SPEED*cos);
		}
		if (a) {
			cam.changePosition(-1*SPEED*cos, 0, -1*SPEED*sin);
		}
		if (d) {
			cam.changePosition(SPEED*cos, 0, SPEED*sin);
		}
		if (shift) {
			cam.changePosition(0, SPEED, 0);
		}
		if (space) {
			cam.changePosition(0, -1*SPEED, 0);
		}
	}
	
	public static void move2() {
		if (w) {
			cam.changePosition(0, 0, -SPEED);
		}
		if (s) {
			cam.changePosition(0, 0, SPEED);
		}
		if (a) {
			cam.changePosition(-SPEED, 0, 0);
		}
		if (d) {
			cam.changePosition(SPEED, 0, 0);
		}
		if (shift) {
			cam.changePosition(0, -SPEED, 0);
		}
		if (space) {
			cam.changePosition(0, SPEED, 0);
		}
	}

	private static final double MOUSE_SPEED = 0.06;
	
	public static void checkMouse() {
		Point e = frame.getMousePosition();
		//System.out.println(e);
		if (e == null) return;
		int dx = e.x - frame.getWidth()/2 - 9;
		int dy = e.y - frame.getHeight()/2 - 9;
		cam.changeAngleXZ(-1 * dx * Math.PI / 180.0 * MOUSE_SPEED);
		cam.changeAngleYZ(dy * Math.PI / 180.0 * MOUSE_SPEED);

		if (dx != 0 || dy != 0) {

			Robot r = null;
			try {
				r = new Robot();
			} catch (AWTException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			r.mouseMove(frame.getWidth() / 2, frame.getHeight() / 2);
		}
	}
}
