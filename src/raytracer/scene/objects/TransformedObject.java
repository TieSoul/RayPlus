package raytracer.scene.objects;

import raytracer.math.*;
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
        BBox origBBox = originalObject.getBBox();
        double minX, maxX, minY, maxY, minZ, maxZ;
        minX = minY = minZ = Double.POSITIVE_INFINITY;
        maxX = maxY = maxZ = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < 7; i++) {
            int x = i&1;
            int y = (i>>1)&1;
            int z = (i>>2)&1; // SKERPLES HELPED WITH THIS.
            Point3d point = origBBox.vmin;
            point = point.translate(new Vector3d(x*(origBBox.vmax.x - origBBox.vmin.x),
                                                 y*(origBBox.vmax.y - origBBox.vmin.y),
                                                 z*(origBBox.vmax.z - origBBox.vmin.z)));
            point = point.transform(transform);
            if (point.x < minX) minX = point.x;
            if (point.x > maxX) maxX = point.x;
            if (point.y < minY) minY = point.y;
            if (point.y > maxY) maxY = point.y;
            if (point.z < minZ) minZ = point.z;
            if (point.z > maxZ) maxZ = point.z;
        }
        return new BBox(new Point3d(minX, minY, minZ), new Point3d(maxX, maxY, maxZ));
    }
}
