package isometricEngine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.List;

import baseEngine.Camera;
import baseEngine.Object3D;
import baseEngine.Point3D;
import baseEngine.Polygon3D;
import baseEngine.World;
import polygonEngine.TextureRenderer;

public class IsoCamera extends Camera {

	private static final int SCALE = 300;
	private int width;
	private int height;

	public IsoCamera() {
		super();
		position = new Point3D(0, 0, 0);
		world = new IsoWorld();
		width = 200;
		height = 200;
	}
	
	@Override
	public void setWorld(World newWorld) {
		world = newWorld;
		List<Object3D> objects = ((IsoWorld) world).getObjects();
		sortObjectsByDistance(objects);
	}
	
	@Override
	public void changePosition(double deltaX, double deltaY, double deltaZ) {
		position.x += deltaX;
		position.y += deltaY;
		position.z += deltaZ;
		if (position.y < 0) position.y = 0;
	}
	
	@Override
	public void setPosition(Point3D newPosition) {
		position = newPosition;
		if (position.y < 0) position.y = 0;
	}

	@Override
	public void paintComponent(Graphics g) {
		BufferedImage screen = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics imG = screen.getGraphics();
		List<Object3D> objects = ((IsoWorld) world).getObjects();
		for (Object3D object : objects) {
			sortByDistance(object.getPolygons());
			for (Polygon3D poly : object.getPolygons()) {
				fillPoly(imG, poly);
			}
		}
		g.drawImage(screen, 0, 0, getWidth(), getHeight(), null);
		g.drawImage(overlay, 0, 0, null);
	}
	
	private void sortObjectsByDistance(List<Object3D> objects) {
		objects.sort(new Comparator<Object3D>() {
			@Override
			public int compare(Object3D o1, Object3D o2) {
				Point3D o1Closest = closest(o1.getPolygons());
				Point3D o2Closest = closest(o2.getPolygons());
				if (o1Closest.x + o1Closest.z < o2Closest.x + o2Closest.z) return 1;
				return -1;
			}
		});
	}
	
	private void drawPoly(Graphics g, Polygon3D poly) {
		for (Point3D[] line : poly.getLines()) {
			drawLine(g, line[0], line[1]);
		}
	}

	private void fillPoly(Graphics g, Polygon3D poly) {
		Point[] points = new Point[poly.getPoints().length];
		int i = 0;
		for (Point3D p : poly.getPoints()) {
			points[i] = getDrawingCoord(p);
			i++;
		}

		Polygon drawPoly = getDrawingPoly(points);
		g.setColor(Color.GRAY);
		g.fillPolygon(drawPoly);
		g.setColor(Color.BLACK);
		g.drawPolygon(drawPoly);
	}

	// turns a set of 2d points into a 2d polygon
	private Polygon getDrawingPoly(Point[] drawPoints) {
		int[] xPoints = new int[drawPoints.length];
		int[] yPoints = new int[drawPoints.length];
		for (int i = 0; i < drawPoints.length; i++) {
			xPoints[i] = drawPoints[i].x;
			yPoints[i] = drawPoints[i].y;
		}
		return new Polygon(xPoints, yPoints, drawPoints.length);
	}

	private void drawLine(Graphics g, Point3D p1, Point3D p2) {
		Point p1Draw = getDrawingCoord(p1);
		Point p2Draw = getDrawingCoord(p2);
		g.drawLine(p1Draw.x, p1Draw.y, p2Draw.x, p2Draw.y);
	}

	private Point getDrawingCoord(Point3D point) {
		double x = 0;
		double y = 0;
		// y -= point.y;
		// x += point.x;
		// x -= point.z;
		// y -= point.x;
		// y -= point.z;

		y -= point.y;
		x += point.x;
		y -= point.x;
		x -= point.z;
		y -= point.z;
		x -= position.x;
		y -= position.z;
		
		//System.out.println(position.y);
		
		double scale = SCALE / (position.y/5);
		return new Point((int) (x * scale) + width / 2, (int) (y * scale) + height / 2);
	}
	
	private void sortByDistance(List<Polygon3D> polys) {
		sortByDistance(closest(polys), polys);
	}
	
	private Point3D closest(List<Polygon3D> polys) {
		Point3D closest = new Point3D(Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
		for (Polygon3D poly : polys) {
			for (Point3D p : poly.getPoints()) {
				if (p.x < closest.x) closest.x = p.x;
				if (p.y > closest.y) closest.y = p.y;
				if (p.z < closest.z) closest.z = p.z;
			}
		}
		return closest;
	}
	
	private static void sortByDistance(Point3D location, List<Polygon3D> polys) {
		polys.sort(new Comparator<Polygon3D>() {
			@Override
			public int compare(Polygon3D p1, Polygon3D p2) {
				Point3D point1 = nearest(location, p1);
				Point3D point2 = nearest(location, p2);
				if (point1.equals(point2)) {
					point1 = TextureRenderer.average(location, p1);
					point2 = TextureRenderer.average(location, p2);
				}
				double p1Dist = point1.distance(location);
				double p2Dist = point2.distance(location);
				//System.out.println(dist);
				if (p1Dist == p2Dist) return 0;
				return p1Dist < p2Dist ? 1 : -1;
			}
		});
	}
	
	private static Point3D nearest(Point3D loc, Polygon3D p) {
		Point3D[] points = p.getPoints();
		Point3D nearest = points[0];
		double dist = nearest.distance(loc);
		for (Point3D point : points) {
			double temp = loc.distance(point);
			if (temp < dist) {
				nearest = point;
				dist = temp;
			}
		}
		return nearest;
	}
	
	private static Point3D average(Point3D loc, Polygon3D p) {
		double xSum = 0;
		double ySum = 0;
		double zSum = 0;
		
		Point3D[] points = p.getPoints();
		
		for (Point3D point : points) {
			xSum += point.x;
			ySum += point.y;
			zSum += point.z;
		}
		
		return new Point3D(xSum/points.length, ySum/points.length, zSum/points.length);
	}

}
