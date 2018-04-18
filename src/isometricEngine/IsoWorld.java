package isometricEngine;

import java.util.ArrayList;
import java.util.List;

import baseEngine.Object3D;
import baseEngine.World;

public class IsoWorld implements World {
	private List<Object3D> objects;
	
	public IsoWorld() {
		objects = new ArrayList<Object3D>();
	}
	
	public void addObject3D(Object3D object) {
		objects.add(object);
	}
	
	public void removeObject3D(Object3D object) {
		objects.remove(object);
	}
	
	public List<Object3D> getObjects() {
		return objects;
	}
	
	public Object3D getObject(int index) {
		if (index < 0 || index >= objects.size()) throw new ArrayIndexOutOfBoundsException();
		return objects.get(index);
	}
}
