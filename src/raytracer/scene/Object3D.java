package raytracer.scene;

import raytracer.Material;
import raytracer.math.*;
import raytracer.scene.objects.BBox;
import raytracer.scene.objects.TransformedObject;

import java.util.List;

public abstract class Object3D {
    public abstract IntersectionInfo intersect(Ray ray);
    public abstract List<IntersectionInfo> intersectAll(Ray ray);
    public Material material;
    public abstract Point2d getTexturePoint(Point3d point);
    public abstract BBox getBBox();
    public TransformedObject transform(Matrix m) {
        return new TransformedObject(this, m);
    }

    public TransformedObject scale(Vector3d scaleFactor) {
        return transform(Matrix.from_scale(scaleFactor));
    }
}
