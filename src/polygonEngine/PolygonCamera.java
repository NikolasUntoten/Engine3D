package polygonEngine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JPanel;

import baseEngine.Camera;
import baseEngine.Object3D;
import baseEngine.Point3D;
import baseEngine.Polygon3D;
import voxelEngine.Particle;
import voxelEngine.Sector;

/*
 * Written By Nikolas Gaub, 8/28/2017
 * 
 * Camera is an extension of JPanel that renders objects from a 3 Dimensional space onto a 2 dimensional view.
 */

public class PolygonCamera extends Camera {
	
	private final static int QUALITY = 8000;
	
	//determines whether objects are drawn with texture or as wireframes
	private boolean wireFrameMode;
	
	//list of current visible polygons
	private List<Polygon3D> visiblePolygons;
	
	//Default constructor, sets position and angle to 0
	public PolygonCamera() {
		super();
		//this.setBounds(0, 0, 500, 500);
		world = new PolygonWorld();
		visiblePolygons = new ArrayList<Polygon3D>();
		wireFrameMode = true;
	}
	
	public void setWireFrame(boolean bool) {
		wireFrameMode = bool;
	}
	
	public void toggleWireFrame() {
		wireFrameMode = !wireFrameMode;
	}
	
	public boolean getWireFrameMode() {
		return wireFrameMode;
	}
	
	public void setWorld(PolygonWorld newWorld) {
		world = newWorld;
	}
	
	public PolygonWorld getWorld() {
		return (PolygonWorld) world;
	}
	
	//number of times paintComponent has been called, reset after 100.
	private int cycleCount;
	
	//renders all 3d objects and paints them on the panel. (currently wire frame)
	@Override
	public void paintComponent(Graphics g) {
		if (cycleCount >= 0) {
			cycleCount = 0;
			update();
		}
		
		for (Object3D object : ((PolygonWorld) world).getObjects()) {
			if (wireFrameMode) {
				renderWireframeObject(g, object, position.copy());
			} else {
				renderTexturedObject(g, object, position.copy());
			}
		}
		
		if (overlay != null) {
			g.drawImage(overlay, 0, 0, null);
		}
		cycleCount++;
	}
	
	/*
	 * long[] nsPerOp = new long[3];
			long lastNS = System.nanoTime();
			nsPerOp[0] = System.nanoTime() - lastNS;
			lastNS = System.nanoTime();
			System.out.println(Arrays.toString(nsPerOp));
			//THIS IS THE ROUTINE FOR TESTING THE TIME AN OPERATION TAKES
	 */
	
	//updates list of visible polygons
	private void update() {
		List<Polygon3D> polys = ((PolygonWorld) world).getPolys(position);
		TextureRenderer.sortByDistance(position.copy(), polys);
		
		visiblePolygons = new ArrayList<Polygon3D>();
		int index = polys.size() - 1;
		int count = 0;
		while (count < QUALITY && index >= 0){
			if (withinScreen(polys.get(index), position)) {
				visiblePolygons.add(0, polys.get(index));
				count++;
			}
			index--;
		}
	}
	
	//renders a single 3d object to a 2d panel, with texture
	private void renderTexturedObject(Graphics g, Object3D object, Point3D location) {
		g.setColor(Color.BLACK);
		long[] nsPerOp = new long[3];
		
		for (Polygon3D poly : visiblePolygons) {
			
			
			long lastNS = System.nanoTime();

			Point3D[] points = poly.getPoints();
			Point[] drawPoints = new Point[points.length];
			
			nsPerOp[0] += System.nanoTime() - lastNS;
			lastNS = System.nanoTime();

			for (int i = 0; i < points.length; i++) {

				drawPoints[i] = getDrawingCoordinates(getRelativeCoordinates(points[i], location), location);
			}
			
			nsPerOp[1] += System.nanoTime() - lastNS;
			lastNS = System.nanoTime();

			Polygon drawPoly = getDrawingPoly(drawPoints, location);
			TextureRenderer.render(g, drawPoly, poly);
			
			nsPerOp[2] += System.nanoTime() - lastNS;
			lastNS = System.nanoTime();
			
		}
		//System.out.println(Arrays.toString(nsPerOp));
	}
	
	//helper method that determines whether or not a given polygon will appear on screen
	private boolean withinScreen(Polygon3D poly, Point3D location) {
		
		Point3D point = getRelativeCoordinates(poly.getPoint(0), location);
		int tolerance = (int) (100 - point.distance(location));
		Point drawPoint = getDrawingCoordinates(point, location);
		//TODO this will not work with large objects.
		return drawPoint.x > -tolerance && drawPoint.x < this.getWidth() + tolerance
				&& drawPoint.y > -tolerance && drawPoint.y < this.getHeight() + tolerance;
	}
	
	// renders all objects currently in world to a 2d panel, with texture
	private void renderTexturedWorld(Graphics g, Point3D location) {
		g.setColor(Color.BLACK);
		
		for (Polygon3D poly : visiblePolygons) {
			
			Point3D[] points = poly.getPoints();
			Point[] drawPoints = new Point[points.length];

			for (int i = 0; i < points.length; i++) {
				Point3D point = points[i];
				point = getRelativeCoordinates(point, location);
				drawPoints[i] = getDrawingCoordinates(point, location);
			}
			
			Polygon drawPoly = getDrawingPoly(drawPoints, location);
			TextureRenderer.render(g, drawPoly, poly);
			
		}
	}
	
	//turns a set of 2d points into a 2d polygon
	private Polygon getDrawingPoly(Point[] drawPoints, Point3D location) {
		int[] xPoints = new int[drawPoints.length];
		int[] yPoints = new int[drawPoints.length];
		for (int i = 0; i < drawPoints.length; i++) {
			xPoints[i] = drawPoints[i].x;
			yPoints[i] = drawPoints[i].y;
		}
		return new Polygon(xPoints, yPoints, drawPoints.length);
	}
	
	//renders a single 3d object to a 2d panel, in wireframe
	private void renderWireframeObject(Graphics g, Object3D object, Point3D location) {
		List<Point3D[]> lines = object.getAllLines();
		for (Point3D[] line : lines) {
			renderLine(g, line[0], line[1], location);
		}
	}
}
