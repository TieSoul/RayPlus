package raytracer.scene.objects;

import raytracer.Material;
import raytracer.math.Point2d;
import raytracer.math.Point3d;
import raytracer.math.Ray;
import raytracer.scene.IntersectionInfo;
import raytracer.scene.Object3D;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thijs on 14/02/2016.
 */
public class Compound extends Object3D {

    List<Object3D> objects = new ArrayList<Object3D>();
    BBox bbox;

    public void addObject(Object3D object) {
        objects.add(object);
        computeBBox();
    }

    public void setMaterial(Material material) {
        for (Object3D object : objects) {
            object.material = material;
        }
    }

    @Override
    public IntersectionInfo intersect(Ray ray) {
        if (!bbox.hit(ray)) return new IntersectionInfo(ray, this);
        IntersectionInfo minintersect = new IntersectionInfo(ray, this);
        for (Object3D object : objects) {
            IntersectionInfo intersect = object.intersect(ray);
            if (intersect.hit && (intersect.t < minintersect.t || !minintersect.hit)) {
                minintersect = intersect;
            }
        }
        return minintersect;
    }

    @Override
    public List<IntersectionInfo> intersectAll(Ray ray) {
        List<IntersectionInfo> list = new ArrayList<IntersectionInfo>();
        for (Object3D object : objects) {
            list.addAll(object.intersectAll(ray));
        }
        return list;
    }

    public void computeBBox() {
        double xmin, ymin, zmin, xmax, ymax, zmax;
        xmin = ymin = zmin = Double.POSITIVE_INFINITY;
        xmax = ymax = zmax = Double.NEGATIVE_INFINITY;
        for (Object3D object : objects) {
            BBox b = object.getBBox();
            if (b != null) {
                if (b.vmin.x < xmin) xmin = b.vmin.x;
                if (b.vmin.y < ymin) ymin = b.vmin.y;
                if (b.vmin.z < zmin) zmin = b.vmin.z;
                if (b.vmax.x > xmax) xmax = b.vmax.x;
                if (b.vmax.y > ymax) ymax = b.vmax.y;
                if (b.vmax.z > zmax) zmax = b.vmax.z;
            }
        }
        bbox = new BBox(new Point3d(xmin, ymin, zmin), new Point3d(xmax, ymax, zmax));
    }

    @Override
    public Point2d getTexturePoint(Point3d point) {
        return new Point2d(0, 0);
    }

    @Override
    public BBox getBBox() {
        return bbox;
    }
}
