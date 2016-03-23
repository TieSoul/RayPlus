package raytracer.scene.objects;

import raytracer.Material;
import raytracer.Texture;
import raytracer.math.*;
import raytracer.scene.IntersectionInfo;
import raytracer.scene.Object3D;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thijs on 14/02/2016.
 */
public class OpenCylinder extends Object3D {

    double radius = 1;
    double miny = 0;
    double maxy = 1;

    public OpenCylinder() {
        material = new Material(Color.WHITE);
    }

    public OpenCylinder(Color color) {
        material = new Material(color);
    }

    public OpenCylinder(Texture texture) {
        material = new Material(texture);
    }

    public OpenCylinder(Material material) {
        this.material = material;
    }

    @Override
    public IntersectionInfo intersect(Ray ray) {
        double a = ray.direction.x*ray.direction.x + ray.direction.z*ray.direction.z;
        double b = 2 * (ray.direction.x*ray.origin.x + ray.direction.z*ray.origin.z);
        double c = ray.origin.x*ray.origin.x + ray.origin.z*ray.origin.z - radius*radius;

        double D = b*b - 4*a*c;
        if (D < 0) return new IntersectionInfo(ray, this);
        double tmin = (-b - Math.sqrt(D))/(2*a);
        double tmax = (-b + Math.sqrt(D))/(2*a);

        if (tmax < 0) return new IntersectionInfo(ray, this);

        double t;
        boolean incoming;
        if (tmin > 0 && ray.getEnd(tmin).y <= maxy && ray.getEnd(tmin).y >= miny) {
            t = tmin;
            incoming = true;
        } else if (ray.getEnd(tmax).y <= maxy && ray.getEnd(tmax).y >= miny) {
            t = tmax;
            incoming = false;
        } else return new IntersectionInfo(ray, this);
        Point3d intersectionPoint = ray.getEnd(t);
        Vector3d normal = new Vector3d(intersectionPoint.x/radius, 0, intersectionPoint.z/radius);
        if (!incoming) normal = normal.scale(-1);
        return new IntersectionInfo(ray, this, normal, t, incoming);
    }

    @Override
    public List<IntersectionInfo> intersectAll(Ray ray) {
        List<IntersectionInfo> list = new ArrayList<IntersectionInfo>();
        double a = ray.direction.x*ray.direction.x + ray.direction.z*ray.direction.z;
        double b = 2 * (ray.direction.x*ray.origin.x + ray.direction.z*ray.origin.z);
        double c = ray.origin.x*ray.origin.x + ray.origin.z*ray.origin.z - radius*radius;

        double D = b*b - 4*a*c;
        if (D < 0) {
            list.add(new IntersectionInfo(ray, this));
            return list;
        }
        double tmin = (-b - Math.sqrt(D))/(2*a);
        double tmax = (-b + Math.sqrt(D))/(2*a);

        if (tmax < 0) {
            list.add(new IntersectionInfo(ray, this));
            return list;
        }

        double t;
        boolean incoming;
        if (tmin > 0) {
            t = tmin;
            incoming = true;
            Point3d intersectionPoint = ray.getEnd(t);
            Vector3d normal = new Vector3d(intersectionPoint.x/radius, 0, intersectionPoint.z/radius);
            if (intersectionPoint.y >= miny && intersectionPoint.y <= maxy) {
                list.add(new IntersectionInfo(ray, this, normal, t, incoming));
            }
        }
        t = tmax;
        incoming = false;
        Point3d intersectionPoint = ray.getEnd(t);
        Vector3d normal = new Vector3d(intersectionPoint.x/radius, 0, intersectionPoint.z/radius);
        if (intersectionPoint.y >= miny && intersectionPoint.y <= maxy) {
            list.add(new IntersectionInfo(ray, this, normal, t, incoming));
        }
        if (list.size() == 0) {
            list.add(new IntersectionInfo(ray, this));
        }
        return list;
    }

    @Override
    public Point2d getTexturePoint(Point3d point) {
        double y = (point.y - miny) / (maxy - miny);
        Vector3d hit = new Vector3d(point.x, 0, point.z).normalize();
        double longitude = Math.acos(hit.dotProduct(new Vector3d(0, 0, 1))) / (2*Math.PI);
        if (hit.dotProduct(new Vector3d(0, 1, 0).crossProduct(new Vector3d(0, 0, 1))) > 0) {
            longitude = 1 - longitude; // turn the longitude into the range [0, 1] by 'mirroring' the longitudes on one side of the sphere around 0.5
        }
        return new Point2d(longitude, y);
    }

    @Override
    public BBox getBBox() {

        return null;
    }
}
