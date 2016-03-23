package raytracer.scene.objects;

import raytracer.Material;
import raytracer.Texture;
import raytracer.math.*;
import raytracer.scene.IntersectionInfo;
import raytracer.scene.Object3D;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thijs on 11/02/2016.
 */
public class Box extends Object3D {

    public Point3d vmin, vmax;

    public Box(Point3d vmin, Point3d vmax) {
        this.vmin = vmin;
        this.vmax = vmax;
        this.material = new Material(Color.WHITE);
    }

    public Box(Point3d vmin, Point3d vmax, Color color) {
        this.vmin = vmin;
        this.vmax = vmax;
        this.material = new Material(color);
    }

    public Box(Point3d vmin, Point3d vmax, Texture texture) {
        this.vmin = vmin;
        this.vmax = vmax;
        this.material = new Material(texture);
    }

    public Box(Point3d vmin, Point3d vmax, Material material) {
        this.vmin = vmin;
        this.vmax = vmax;
        this.material = material;
    }

    @Override
    public IntersectionInfo intersect(Ray ray) {
        double tmin, tmax;
        Vector3d nmin, nmax;
        double xtmin = (vmin.x - ray.origin.x) / ray.direction.x,
                xtmax = (vmax.x - ray.origin.x) / ray.direction.x,
                ytmin = (vmin.y - ray.origin.y) / ray.direction.y,
                ytmax = (vmax.y - ray.origin.y) / ray.direction.y,
                ztmin = (vmin.z - ray.origin.z) / ray.direction.z,
                ztmax = (vmax.z - ray.origin.z) / ray.direction.z;

        if (xtmin > xtmax) {
            double temp = xtmin;
            xtmin = xtmax;
            xtmax = temp;
        }
        if (ytmin > ytmax) {
            double temp = ytmin;
            ytmin = ytmax;
            ytmax = temp;
        }
        if (ztmin > ztmax) {
            double temp = ztmin;
            ztmin = ztmax;
            ztmax = temp;
        }

        tmin = xtmin;
        nmin = new Vector3d((ray.direction.x > 0) ? -1 : 1, 0, 0);
        tmax = xtmax;
        nmax = new Vector3d((ray.direction.x > 0) ? -1 : 1, 0, 0);

        if (xtmin > ytmax || ytmin > xtmax) {
            return new IntersectionInfo(ray, this);
        }

        if (ytmin > xtmin) {
            tmin = ytmin;
            nmin = new Vector3d(0, (ray.direction.y > 0) ? -1 : 1, 0);
        }
        if (ytmax < xtmax) {
            tmax = ytmax;
            nmax = new Vector3d(0, (ray.direction.y > 0) ? -1 : 1, 0);
        }

        if (ztmin > tmax || tmin > ztmax) {
            return new IntersectionInfo(ray, this);
        }
        if (ztmin > tmin) {
            tmin = ztmin;
            nmin = new Vector3d(0, 0, (ray.direction.z > 0) ? -1 : 1);
        }
        if (ztmax < tmax) {
            tmax = ztmax;
            nmax = new Vector3d(0, 0, (ray.direction.z > 0) ? -1 : 1);
        }

        if (tmin < 0 && tmax < 0) {
            return new IntersectionInfo(ray, this);
        }

        if (tmin >= 0) {
            return new IntersectionInfo(ray, this, nmin, tmin, true);
        }
        return new IntersectionInfo(ray, this, nmax, tmax, false);
    }

    @Override
    public List<IntersectionInfo> intersectAll(Ray ray) {
        List<IntersectionInfo> list = new ArrayList<IntersectionInfo>();
        double tmin, tmax;
        Vector3d nmin, nmax;
        double xtmin = (vmin.x - ray.origin.x) / ray.direction.x,
                xtmax = (vmax.x - ray.origin.x) / ray.direction.x,
                ytmin = (vmin.y - ray.origin.y) / ray.direction.y,
                ytmax = (vmax.y - ray.origin.y) / ray.direction.y,
                ztmin = (vmin.z - ray.origin.z) / ray.direction.z,
                ztmax = (vmax.z - ray.origin.z) / ray.direction.z;

        if (xtmin > xtmax) {
            double temp = xtmin;
            xtmin = xtmax;
            xtmax = temp;
        }
        if (ytmin > ytmax) {
            double temp = ytmin;
            ytmin = ytmax;
            ytmax = temp;
        }
        if (ztmin > ztmax) {
            double temp = ztmin;
            ztmin = ztmax;
            ztmax = temp;
        }

        tmin = xtmin;
        nmin = new Vector3d((ray.direction.x > 0) ? -1 : 1, 0, 0);
        tmax = xtmax;
        nmax = new Vector3d((ray.direction.x > 0) ? -1 : 1, 0, 0);

        if (xtmin > ytmax || ytmin > xtmax) {
            list.add(new IntersectionInfo(ray, this));
            return list;
        }

        if (ytmin > xtmin) {
            tmin = ytmin;
            nmin = new Vector3d(0, (ray.direction.y > 0) ? -1 : 1, 0);
        }
        if (ytmax < xtmax) {
            tmax = ytmax;
            nmax = new Vector3d(0, (ray.direction.y > 0) ? -1 : 1, 0);
        }

        if (ztmin > tmax || tmin > ztmax) {
            list.add(new IntersectionInfo(ray, this));
            return list;
        }

        if (ztmin > tmin) {
            tmin = ztmin;
            nmin = new Vector3d(0, 0, (ray.direction.z > 0) ? -1 : 1);
        }
        if (ztmax < tmax) {
            tmax = ztmax;
            nmax = new Vector3d(0, 0, (ray.direction.z > 0) ? -1 : 1);
        }

        if (tmax < 0) {
            list.add(new IntersectionInfo(ray, this));
            return list;
        }

        if (tmin >= 0) {
            list.add(new IntersectionInfo(ray, this, nmin, tmin, true));
        }
        list.add(new IntersectionInfo(ray, this, nmax, tmax, false));
        return list;
    }

    @Override
    public Point2d getTexturePoint(Point3d point) {
        return new Point2d(0, 0); // will tinker with this later probably. Maybe. Okay, probably not.
    }

    @Override
    public BBox getBBox() {
        return new BBox(vmin, vmax);
    }
}
