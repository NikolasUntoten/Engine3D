package polygonEngine;

import java.util.ArrayList;
import java.util.List;

import baseEngine.Object3D;
import baseEngine.Point3D;
import baseEngine.Polygon3D;
import baseEngine.World;

/*
 * Written By Nikolas Gaub, 8/28/2017
 * 
 * The World object stores data about 3 Dimensional objects in a 3 Dimensional space.
 */

public class PolygonWorld implements World {
	
	private List<Object3D> objects;
	private List<Polygon3D> polygons;
	
	public PolygonWorld() {
		objects = new ArrayList<Object3D>();
		polygons = new ArrayList<Polygon3D>();
		sorted = false;
	}
	
	public void addObject3D(Object3D object) {
		objects.add(object);
		polygons.addAll(object.getPolygons());
	}
	
	public void removeObject3D(Object3D object) {
		objects.remove(object);
		for (Polygon3D p : object.getPolygons()) {
			polygons.remove(p);
		}
	}
	
	public List<Object3D> getObjects() {
		return objects;
	}
	
	public Object3D getObject(int index) {
		if (index < 0 || index >= objects.size()) throw new ArrayIndexOutOfBoundsException();
		return objects.get(index);
	}
	
	private boolean sorted;
	public List<Polygon3D> getPolys(Point3D location) {
		if (!sorted) {
			TextureRenderer.sortByDistance(location, polygons);
			sorted = true;
		} else {
			bubbleSort(polygons, location);
		}
		return polygons;
	}
	
	public void bubbleSort(List<Polygon3D> polys, Point3D location) {
		int changes;
		do {
			changes = 0;
			for (int i = 0; i < polys.size() - 1; i++) {
				int comp = compare(polys.get(i), polys.get(i+1), location);
				if (comp > 0) {
					Polygon3D temp = polys.get(i);
					polys.set(i, polys.get(i+1));
					polys.set(i+1, temp);
				}
			}
		} while (changes != 0);
	}
	
	//negative if p1 farther, positive if p1 closer
	private int compare(Polygon3D p1, Polygon3D p2, Point3D location) {
		Point3D point1 = TextureRenderer.nearest(location, p1);
		Point3D point2 = TextureRenderer.nearest(location, p2);
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
}
