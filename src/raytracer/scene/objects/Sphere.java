package raytracer.scene.objects;

import raytracer.Material;
import raytracer.Texture;
import raytracer.math.*;
import raytracer.scene.IntersectionInfo;
import raytracer.scene.Object3D;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thijs on 04/10/2015.
 */
public class Sphere extends Object3D {

    public Point3d center;
    public double radius;
    public double sqrRadius;

    public Sphere(Point3d center, double radius) {
        this.center = center;
        this.radius = radius;
        this.sqrRadius = radius * radius;
        this.material = new Material(Color.WHITE);
    }

    public Sphere(Point3d center, double radius, Color color) {
        this.center = center;
        this.radius = radius;
        this.sqrRadius = radius * radius;
        this.material = new Material(color);
    }

    public Sphere(Point3d center, double radius, Texture texture) {
        this.center = center;
        this.radius = radius;
        this.sqrRadius = radius * radius;
        this.material = new Material(texture);
    }

    public Sphere(Point3d center, double radius, Material material) {
        this.center = center;
        this.radius = radius;
        this.sqrRadius = radius * radius;
        this.material = material;
    }

    //check if the ray intersects the spere
    @Override
    //actually needs to return a double
    public IntersectionInfo intersect(Ray ray) {
        //method missing

        Point3d p = ray.origin;
        Vector3d u = ray.direction;
        Vector3d v = new Vector3d(center, p);
        double t;
        Vector3d normal;
        Point3d intersection;
        boolean incoming;

        double a = ray.direction.dotProduct(ray.direction);
        double b = 2*v.dotProduct(u);
        double c = v.dotProduct(v) - sqrRadius;
        double discriminant = b*b - 4*a*c;

        //return non-hitting IntersectionInfo when there is no intersection
        if (discriminant < 0) {
            return new IntersectionInfo(ray, this);
        }

        double discSqrt = Math.sqrt(discriminant);
        double dMin = (-b - discSqrt)/(2*a);
        double dPlus = (-b + discSqrt)/(2*a);

        //if sphere is behind the ray
        if (dMin < 0 && dPlus < 0) {
            return new IntersectionInfo(ray, this);
        }

        //if origin of the ray is inside the sphere
        if (dMin < 0 && dPlus > 0){
            t = dPlus;
            intersection = ray.getEnd(t);
            normal = new Vector3d(intersection, center);
            incoming = false;
        } else {
            t = dMin;
            intersection = ray.getEnd(t);
            normal = new Vector3d(center, intersection);
            incoming = true;
        }

        return new IntersectionInfo(ray, this, normal, intersection, incoming);
    }

    @Override
    public List<IntersectionInfo> intersectAll(Ray ray) {
        List<IntersectionInfo> list = new ArrayList<IntersectionInfo>();
        Point3d p = ray.origin;
        Vector3d u = ray.direction;
        Vector3d v = new Vector3d(center, p);
        double t;
        Vector3d normal;
        Point3d intersection;
        boolean incoming;

        double a = ray.direction.dotProduct(ray.direction);
        double b = 2*v.dotProduct(u);
        double c = v.dotProduct(v) - sqrRadius;
        double discriminant = b*b - 4*a*c;

        //return non-hitting IntersectionInfo when there is no intersection
        if (discriminant < 0) {
            list.add(new IntersectionInfo(ray, this));
            return list;
        }

        double discSqrt = Math.sqrt(discriminant);
        double dMin = (-b - discSqrt)/(2*a);
        double dPlus = (-b + discSqrt)/(2*a);

        //if sphere is behind the ray
        if (dMin < 0 && dPlus < 0) {
            list.add(new IntersectionInfo(ray, this));
            return list;
        }

        if (dMin > 0) {
            t = dMin;
            intersection = ray.getEnd(t);
            normal = new Vector3d(center, intersection);
            incoming = true;
            list.add(new IntersectionInfo(ray, this, normal, intersection, incoming));
        }

        t = dPlus;
        intersection = ray.getEnd(t);
        normal = new Vector3d(intersection, center);
        incoming = false;
        list.add(new IntersectionInfo(ray, this, normal, intersection, incoming));


        return list;
    }

    @Override
    public Point2d getTexturePoint(Point3d point) {
        Vector3d hit = new Vector3d(center, point).normalize();
        double latitude = Math.acos(hit.dotProduct(new Vector3d(0, 1, 0))) / Math.PI; // latitude in [0, pi] (scaled down to [0, 1])
        double longitude = Math.acos(hit.dotProduct(new Vector3d(0, 0, 1)) / Math.sin(latitude * Math.PI)) / (2*Math.PI); // longitude in [0, pi] (scaled down to [0, 0.5])
        if (hit.dotProduct(new Vector3d(0, 1, 0).crossProduct(new Vector3d(0, 0, 1))) > 0) {
            longitude = 1 - longitude; // turn the longitude into the range [0, 1] by 'mirroring' the longitudes on one side of the sphere around 0.5
        }
        return new Point2d(longitude, latitude);
    }

    @Override
    public BBox getBBox() {
        return new BBox(center.translate(new Vector3d(-radius, -radius, -radius)), center.translate(new Vector3d(radius, radius, radius)));
    }
}
