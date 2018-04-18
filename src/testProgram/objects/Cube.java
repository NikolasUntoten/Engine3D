package testProgram.objects;

import baseEngine.Object3D;
import baseEngine.Point3D;
import baseEngine.Polygon3D;

public class Cube extends Object3D {
	
	public Cube(Point3D center, double size) {
		super();
		Point3D ooo = new Point3D(center.x - size/2, center.y - size/2, center.z - size/2);
		Point3D ooi = new Point3D(center.x - size/2, center.y - size/2, center.z + size/2);
		Point3D oio = new Point3D(center.x - size/2, center.y + size/2, center.z - size/2);
		Point3D oii = new Point3D(center.x - size/2, center.y + size/2, center.z + size/2);
		Point3D ioo = new Point3D(center.x + size/2, center.y - size/2, center.z - size/2);
		Point3D ioi = new Point3D(center.x + size/2, center.y - size/2, center.z + size/2);
		Point3D iio = new Point3D(center.x + size/2, center.y + size/2, center.z - size/2);
		Point3D iii = new Point3D(center.x + size/2, center.y + size/2, center.z + size/2);
		
		addPolygon(new Polygon3D(ooo, ooi, oii, oio));
		addPolygon(new Polygon3D(ooo, ooi, ioi, ioo));
		addPolygon(new Polygon3D(ooo, oio, iio, ioo));
		addPolygon(new Polygon3D(iii, iio, ioo, ioi));
		addPolygon(new Polygon3D(iii, iio, oio, oii));
		addPolygon(new Polygon3D(iii, ioi, ooi, oii));
	}
}
