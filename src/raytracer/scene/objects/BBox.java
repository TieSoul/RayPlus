package raytracer.scene.objects;

import raytracer.math.Point3d;
import raytracer.math.Ray;

/**
 * Created by Thijs on 14/02/2016.
 */
public class BBox {
    
    public Point3d vmin, vmax;
    public BBox(Point3d vmin, Point3d vmax) {
        this.vmin = vmin;
        this.vmax = vmax;
    }
    
    public boolean hit(Ray ray) {
        double tmin, tmax;
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
        tmax = xtmax;

        if (xtmin > ytmax || ytmin > xtmax) {
            return false;
        }

        if (ytmin > xtmin) {
            tmin = ytmin;
        }
        if (ytmax < xtmax) {
            tmax = ytmax;
        }

        if (ztmin > tmax || tmin > ztmax) {
            return false;
        }
        if (ztmin > tmin) {
            tmin = ztmin;
        }
        if (ztmax < tmax) {
            tmax = ztmax;
        }

        return !(tmin < 0 && tmax < 0);

    }
}
