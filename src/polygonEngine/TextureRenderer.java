package polygonEngine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.List;

import baseEngine.Point3D;
import baseEngine.Polygon3D;

public class TextureRenderer {
	
	public static void render(Graphics g, Polygon drawPoly, Polygon3D realPoly) {
		BufferedImage texture = realPoly.getTexture();
		if (texture == null) {
			Color c = realPoly.getFill();
			g.setColor(c);
			g.fillPolygon(drawPoly);
			g.setColor(Color.black);
			g.drawPolygon(drawPoly);
		}
	}
	
	public static void sortByDistance(Point3D location, List<Polygon3D> polys) {
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
	
	public static Point3D nearest(Point3D loc, Polygon3D p) {
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
	
	public static Point3D average(Point3D loc, Polygon3D p) {
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
