package raytracer.math;

/**
 * Created by Thijs on 04/10/2015.
 */
public class Point3d {
    public double x, y, z;

    public Point3d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getDistance(Point3d point) {
        return new Vector3d(this, point).getLength();
    }

    public Point3d translate(Vector3d vector) {
        return new Point3d(x + vector.x, y+vector.y, z+vector.z);
    }

    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }

    public Point3d transform(Matrix m) {
        double vx = x*m.m[0][0] + y*m.m[0][1] + z*m.m[0][2] + m.m[0][3];
        double vy = x*m.m[1][0] + y*m.m[1][1] + z*m.m[1][2] + m.m[1][3];
        double vz = x*m.m[2][0] + y*m.m[2][1] + z*m.m[2][2] + m.m[2][3];
        double vw = x*m.m[3][0] + y*m.m[3][1] + z*m.m[3][2] + m.m[3][3];
        if (vw != 1 && vw != 0) {
            vx /= vw;
            vy /= vw;
            vz /= vw;
        }
        return new Point3d(vx, vy, vz);
    }
}
