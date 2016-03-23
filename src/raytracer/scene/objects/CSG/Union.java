package raytracer.scene.objects.CSG;

import raytracer.math.Point2d;
import raytracer.math.Point3d;
import raytracer.math.Ray;
import raytracer.scene.IntersectionInfo;
import raytracer.scene.Object3D;
import raytracer.scene.objects.BBox;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Thijs on 12/02/2016.
 */
public class Union extends Object3D {

    public Object3D obj1;
    public Object3D obj2;

    @Override
    public IntersectionInfo intersect(Ray ray) {
        IntersectionInfo i1 = obj1.intersect(ray);
        IntersectionInfo i2 = obj2.intersect(ray);
        if (!i2.hit) return i1;
        if (i1.t > i2.t || !i1.hit) return i2;
        return i1;
    }

    @Override
    public List<IntersectionInfo> intersectAll(Ray ray) {
        List<IntersectionInfo> list = new ArrayList<IntersectionInfo>();
        List<IntersectionInfo> i1 = obj1.intersectAll(ray);
        List<IntersectionInfo> i2 = obj2.intersectAll(ray);
        if (!i1.get(0).hit) return i2;
        if (!i2.get(0).hit) return i1;
        i1.sort(new Comparator<IntersectionInfo>() {
            @Override
            public int compare(IntersectionInfo o1, IntersectionInfo o2) {
                if (o1.t > o2.t) return 1;
                if (o2.t > o1.t) return -1;
                if (o1.incoming && !o2.incoming) return 1;
                if (o2.incoming && !o1.incoming) return -1;
                return 0;
            }
        });
        i2.sort(new Comparator<IntersectionInfo>() {
            @Override
            public int compare(IntersectionInfo o1, IntersectionInfo o2) {
                if (o1.t > o2.t) return 1;
                if (o2.t > o1.t) return -1;
                if (o1.incoming && !o2.incoming) return 1;
                if (o2.incoming && !o1.incoming) return -1;
                return 0;
            }
        });
        boolean obj1status = !i1.get(0).incoming;
        boolean obj2status = !i2.get(0).incoming;
        boolean overallstatus = obj1status || obj2status;
        for (IntersectionInfo intersect : i1) {
            if (i2.size() == 0) break;
            IntersectionInfo intersect2 = i2.get(0);
            while (intersect2.t <= intersect.t) {
                obj2status = intersect2.incoming;
                if ((obj1status || obj2status) && !overallstatus) {
                    overallstatus = true;
                    list.add(intersect);
                }
                if (!obj1status && !obj2status && overallstatus) {
                    overallstatus = false;
                    list.add(intersect);
                }
                i2.remove(intersect2);
                if (i2.size() == 0) {
                    obj2status = false;
                    break;
                }
                intersect2 = i2.get(0);
            }
            obj1status = intersect.incoming;
            if ((obj1status || obj2status) && !overallstatus) {
                overallstatus = true;
                list.add(intersect);
            }
            if (!obj1status && !obj2status && overallstatus) {
                overallstatus = false;
                list.add(intersect);
            }
        }
        if (list.isEmpty()) {
            list.add(new IntersectionInfo(ray, this));
        }
        return list;
    }

    @Override
    public Point2d getTexturePoint(Point3d point) {
        return new Point2d(0,0); // u wot m8
    }

    @Override
    public BBox getBBox() {
        return null;
    }

    public Union(Object3D o1, Object3D o2) {
        this.obj1 = o1;
        this.obj2 = o2;
    }
}
