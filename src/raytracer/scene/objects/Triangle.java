package raytracer.scene.objects;

import raytracer.Material;
import raytracer.math.*;
import raytracer.scene.IntersectionInfo;
import raytracer.scene.Object3D;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thijs on 14/02/2016.
 */
public class Triangle extends Object3D {

    public Point3d v1, v2, v3;
    public Vector3d normal;

    public Triangle(Point3d a, Point3d b, Point3d c) {
        v1 = a;
        v2 = b;
        v3 = c;
        normal = new Vector3d(v1, v2).crossProduct(new Vector3d(v1, v3)).normalize();
        material = new Material(Color.WHITE);
    }

    @Override
    public IntersectionInfo intersect(Ray ray) {
        double a = v1.x - v2.x, b = v1.x - v3.x, c = ray.direction.x, d = v1.x - ray.origin.x,
               e = v1.y - v2.y, f = v1.y - v3.y, g = ray.direction.y, h = v1.y - ray.origin.y,
               i = v1.z - v2.z, j = v1.z - v3.z, k = ray.direction.z, l = v1.z - ray.origin.z;
        double m = f*k - g*j, n = h*k - g*l, p = f*l - h*j;
        double q = g*i - e*k, s = e*j - f*i;
        double inv_denom = 1.0/(a*m+b*q+c*s);
        double e1 = d*m - b*n - c*p;
        double beta = e1*inv_denom;
        if (beta < 0.0) {
            return new IntersectionInfo(ray, this);
        }
        double r = e*l - h*i;
        double e2 = a*n + d*q + c*r;
        double gamma = e2*inv_denom;
        if (gamma < 0.0 || gamma + beta > 1.0) {
            return new IntersectionInfo(ray, this);
        }
        double e3 = a*p - b*r + d*s;
        double t = e3*inv_denom;
        if (t < 0) {
            return new IntersectionInfo(ray, this);
        }

        if (ray.direction.dotProduct(normal) > 0) {
            return new IntersectionInfo(ray, this, normal.scale(-1), t, true);
        }

        return new IntersectionInfo(ray, this, normal, t, true);
    }

    @Override
    public List<IntersectionInfo> intersectAll(Ray ray) {
        List<IntersectionInfo> list = new ArrayList<IntersectionInfo>();
        list.add(intersect(ray));
        return list;
    }

    @Override
    public Point2d getTexturePoint(Point3d point) {
        return new Point2d(0, 0);
    }

    @Override
    public BBox getBBox() {
        return null;
    }
}
