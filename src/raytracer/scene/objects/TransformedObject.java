package raytracer.scene.objects;

import raytracer.math.Matrix;
import raytracer.math.Point2d;
import raytracer.math.Point3d;
import raytracer.math.Ray;
import raytracer.scene.IntersectionInfo;
import raytracer.scene.Object3D;

import java.util.List;

/**
 * Created by Thijs on 11/02/2016.
 */
public class TransformedObject extends Object3D {
    public Object3D originalObject;
    public Matrix transform;
    public Matrix inverse;

    public TransformedObject(Object3D obj, Matrix transform) {
        originalObject = obj;
        material = obj.material;
        this.transform = transform;
        inverse = transform.inverse();
    }

    @Override
    public TransformedObject transform(Matrix m) {
        return new TransformedObject(originalObject, transform.mult(m));
    }

    @Override
    public IntersectionInfo intersect(Ray ray) {
        IntersectionInfo intersect = originalObject.intersect(ray.transform(inverse));
        intersect.object = this;
        intersect.ray = ray;
        if (intersect.hit) {
            intersect.point = intersect.point.transform(transform);
            intersect.t = ray.origin.getDistance(intersect.point);
            intersect.normal = intersect.normal.transform(inverse.transpose()).normalize();
        }
        return intersect;
    }

    @Override
    public List<IntersectionInfo> intersectAll(Ray ray) {
        List<IntersectionInfo> intersects = originalObject.intersectAll(ray.transform(inverse));
        for (IntersectionInfo intersect : intersects) {
            intersect.object = this;
            intersect.ray = ray;
            if (intersect.hit) {
                intersect.point = intersect.point.transform(transform);
                intersect.t = ray.origin.getDistance(intersect.point);
                intersect.normal = intersect.normal.transform(inverse.transpose()).normalize();
            }
        }
        return intersects;
    }

    @Override
    public Point2d getTexturePoint(Point3d point) {
        return originalObject.getTexturePoint(point.transform(inverse));
    }

    @Override
    public BBox getBBox() {
        return null;
    }
}
