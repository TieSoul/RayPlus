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
public class Intersection extends Object3D {

    public Object3D obj1;
    public Object3D obj2;

    @Override
    public IntersectionInfo intersect(Ray ray) {
        List<IntersectionInfo> i = intersectAll(ray);
        i.sort(new Comparator<IntersectionInfo>() {
            @Override
            public int compare(IntersectionInfo o1, IntersectionInfo o2) {
                if (o1.t < o2.t) return -1;
                if (o1.t > o2.t) return 1;
                return 0;
            }
        });
        return i.get(0);
    }

    @Override
    public List<IntersectionInfo> intersectAll(Ray ray) {
        List<IntersectionInfo> list = new ArrayList<IntersectionInfo>();
        List<IntersectionInfo> i1 = obj1.intersectAll(ray);
        List<IntersectionInfo> i2 = obj2.intersectAll(ray);
        if (!i1.get(0).hit) {
            list.add(i1.get(0));
            return list;
        }
        if (!i2.get(0).hit) {
            list.add(i2.get(0));
            return list;
        }
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
        boolean overallstatus = obj1status && obj2status;
        for (IntersectionInfo intersect : i2) {
            if (i1.size() == 0) break;
            IntersectionInfo intersect2 = i1.get(0);
            while (intersect2.t <= intersect.t) {
                obj1status = intersect2.incoming;
                if (obj1status && obj2status && !overallstatus) {
                    overallstatus = true;
                    list.add(intersect2);
                }
                if ((!obj1status || !obj2status) && overallstatus) {
                    overallstatus = false;
                    list.add(intersect2);
                }
                i1.remove(intersect2);
                if (i1.size() == 0) {
                    obj1status = false;
                    overallstatus = false;
                    break;
                }
                intersect2 = i1.get(0);
            }
            obj2status = intersect.incoming;
            if (obj1status && obj2status && !overallstatus) {
                overallstatus = true;
                list.add(intersect);
            }
            if ((!obj1status || !obj2status) && overallstatus) {
                overallstatus = false;
                list.add(intersect);
            }
        }
        if (list.size() == 0) {
            list.add(new IntersectionInfo(ray, this));
        }
        return list;
    }

    @Override
    public Point2d getTexturePoint(Point3d point) {
        return null;
    }

    @Override
    public BBox getBBox() {
        return null;
    }

    public Intersection(Object3D o1, Object3D o2) {
        this.obj1 = o1;
        this.obj2 = o2;
    }
}
