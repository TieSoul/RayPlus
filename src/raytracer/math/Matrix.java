package raytracer.math;

public class Matrix {
    public double[][] m = new double[4][4];

    public Matrix() {
        m[0][0] = 1d;
        m[1][1] = 1d;
        m[2][2] = 1d;
        m[3][3] = 1d;
    }

    public Matrix mult(Matrix m1) {
        Matrix new_matrix = new Matrix();
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m.length; j++) {
                new_matrix.m[i][j] = 0;
                for (int n = 0; n < m.length; n++) {
                    new_matrix.m[i][j] += m[i][n] * m1.m[n][j];
                }
            }
        }
        return new_matrix;
    }
    public Matrix mult(double d) {
        Matrix new_matrix = new Matrix();
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[i].length; j++) {
                new_matrix.m[i][j] = m[i][j] * d;
            }
        }
        return new_matrix;
    }
    public Matrix transpose() {
        Matrix transpose = new Matrix();
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[i].length; j++) {
                transpose.m[j][i] = m[i][j];
            }
        }
        return transpose;
    }
    // inefficient inverting technique.
    public Matrix inverse() {
        double[] inv = new double[16];
        double[] m = new double[16];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                m[i*4+j] = this.m[i][j];
            }
        }
        inv[0] = m[5]  * m[10] * m[15] -
                m[5]  * m[11] * m[14] -
                m[9]  * m[6]  * m[15] +
                m[9]  * m[7]  * m[14] +
                m[13] * m[6]  * m[11] -
                m[13] * m[7]  * m[10];

        inv[4] = -m[4]  * m[10] * m[15] +
                m[4]  * m[11] * m[14] +
                m[8]  * m[6]  * m[15] -
                m[8]  * m[7]  * m[14] -
                m[12] * m[6]  * m[11] +
                m[12] * m[7]  * m[10];

        inv[8] = m[4]  * m[9] * m[15] -
                m[4]  * m[11] * m[13] -
                m[8]  * m[5] * m[15] +
                m[8]  * m[7] * m[13] +
                m[12] * m[5] * m[11] -
                m[12] * m[7] * m[9];

        inv[12] = -m[4]  * m[9] * m[14] +
                m[4]  * m[10] * m[13] +
                m[8]  * m[5] * m[14] -
                m[8]  * m[6] * m[13] -
                m[12] * m[5] * m[10] +
                m[12] * m[6] * m[9];

        inv[1] = -m[1]  * m[10] * m[15] +
                m[1]  * m[11] * m[14] +
                m[9]  * m[2] * m[15] -
                m[9]  * m[3] * m[14] -
                m[13] * m[2] * m[11] +
                m[13] * m[3] * m[10];

        inv[5] = m[0]  * m[10] * m[15] -
                m[0]  * m[11] * m[14] -
                m[8]  * m[2] * m[15] +
                m[8]  * m[3] * m[14] +
                m[12] * m[2] * m[11] -
                m[12] * m[3] * m[10];

        inv[9] = -m[0]  * m[9] * m[15] +
                m[0]  * m[11] * m[13] +
                m[8]  * m[1] * m[15] -
                m[8]  * m[3] * m[13] -
                m[12] * m[1] * m[11] +
                m[12] * m[3] * m[9];

        inv[13] = m[0]  * m[9] * m[14] -
                m[0]  * m[10] * m[13] -
                m[8]  * m[1] * m[14] +
                m[8]  * m[2] * m[13] +
                m[12] * m[1] * m[10] -
                m[12] * m[2] * m[9];

        inv[2] = m[1]  * m[6] * m[15] -
                m[1]  * m[7] * m[14] -
                m[5]  * m[2] * m[15] +
                m[5]  * m[3] * m[14] +
                m[13] * m[2] * m[7] -
                m[13] * m[3] * m[6];

        inv[6] = -m[0]  * m[6] * m[15] +
                m[0]  * m[7] * m[14] +
                m[4]  * m[2] * m[15] -
                m[4]  * m[3] * m[14] -
                m[12] * m[2] * m[7] +
                m[12] * m[3] * m[6];

        inv[10] = m[0]  * m[5] * m[15] -
                m[0]  * m[7] * m[13] -
                m[4]  * m[1] * m[15] +
                m[4]  * m[3] * m[13] +
                m[12] * m[1] * m[7] -
                m[12] * m[3] * m[5];

        inv[14] = -m[0]  * m[5] * m[14] +
                m[0]  * m[6] * m[13] +
                m[4]  * m[1] * m[14] -
                m[4]  * m[2] * m[13] -
                m[12] * m[1] * m[6] +
                m[12] * m[2] * m[5];

        inv[3] = -m[1] * m[6] * m[11] +
                m[1] * m[7] * m[10] +
                m[5] * m[2] * m[11] -
                m[5] * m[3] * m[10] -
                m[9] * m[2] * m[7] +
                m[9] * m[3] * m[6];

        inv[7] = m[0] * m[6] * m[11] -
                m[0] * m[7] * m[10] -
                m[4] * m[2] * m[11] +
                m[4] * m[3] * m[10] +
                m[8] * m[2] * m[7] -
                m[8] * m[3] * m[6];

        inv[11] = -m[0] * m[5] * m[11] +
                m[0] * m[7] * m[9] +
                m[4] * m[1] * m[11] -
                m[4] * m[3] * m[9] -
                m[8] * m[1] * m[7] +
                m[8] * m[3] * m[5];

        inv[15] = m[0] * m[5] * m[10] -
                m[0] * m[6] * m[9] -
                m[4] * m[1] * m[10] +
                m[4] * m[2] * m[9] +
                m[8] * m[1] * m[6] -
                m[8] * m[2] * m[5];

        double det = m[0] * inv[0] + m[1] * inv[4] + m[2] * inv[8] + m[3] * inv[12];
        det = 1.0 / det;

        Matrix inverse = new Matrix();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                inverse.m[i][j] = det * inv[i*4+j];
            }
        }
        return inverse;
    }
    public Matrix scale(Vector3d scaleFactor) {
        Matrix new_matrix = new Matrix();
        new_matrix.m[0][0] = scaleFactor.x;
        new_matrix.m[1][1] = scaleFactor.y;
        new_matrix.m[2][2] = scaleFactor.z;
        return new_matrix.mult(this);
    }

    public Matrix translate(Vector3d translation) {
        Matrix new_matrix = new Matrix();
        new_matrix.m[0][3] = translation.x;
        new_matrix.m[1][3] = translation.y;
        new_matrix.m[2][3] = translation.z;
        return new_matrix.mult(this);
    }

    public Matrix rotx(double angle) {
        Matrix new_matrix = new Matrix();
        new_matrix.m[1][1] = Math.cos(angle);
        new_matrix.m[1][2] = -Math.sin(angle);
        new_matrix.m[2][1] = Math.sin(angle);
        new_matrix.m[2][2] = Math.cos(angle);
        return new_matrix.mult(this);
    }

    public Matrix roty(double angle) {
        Matrix new_matrix = new Matrix();
        new_matrix.m[0][0] = Math.cos(angle);
        new_matrix.m[0][2] = Math.sin(angle);
        new_matrix.m[2][0] = -Math.sin(angle);
        new_matrix.m[2][2] = Math.cos(angle);
        return new_matrix.mult(this);
    }

    public Matrix rotz(double angle) {
        Matrix new_matrix = new Matrix();
        new_matrix.m[0][0] = Math.cos(angle);
        new_matrix.m[0][1] = -Math.sin(angle);
        new_matrix.m[1][0] = Math.sin(angle);
        new_matrix.m[1][1] = Math.cos(angle);
        return new_matrix.mult(this);
    }

    public Matrix rotate(Vector3d axis, double angle) {
        Matrix new_matrix = new Matrix();
        new_matrix.m[0][0] = axis.x*axis.x + (1 - axis.x*axis.x)*Math.cos(angle);
        new_matrix.m[0][1] = axis.x*axis.y*(1-Math.cos(angle)) - axis.z*Math.sin(angle);
        new_matrix.m[0][2] = axis.x*axis.z*(1-Math.cos(angle)) + axis.y*Math.sin(angle);
        new_matrix.m[1][0] = axis.x*axis.y*(1-Math.cos(angle)) + axis.z*Math.sin(angle);
        new_matrix.m[1][1] = axis.y*axis.y + (1-axis.y*axis.y)*Math.cos(angle);
        new_matrix.m[1][2] = axis.y*axis.z*(1-Math.cos(angle)) - axis.x*Math.sin(angle);
        new_matrix.m[2][0] = axis.x*axis.z*(1-Math.cos(angle)) - axis.y*Math.sin(angle);
        new_matrix.m[2][1] = axis.y*axis.z*(1-Math.cos(angle)) + axis.x*Math.sin(angle);
        new_matrix.m[2][2] = axis.z*axis.z + (1-axis.z*axis.z)*Math.cos(angle);
        return new_matrix.mult(this);
    }

    public static Matrix from_scale(Vector3d scaleFactor) {
        return new Matrix().scale(scaleFactor);
    }

    public static Matrix from_translation(Vector3d translation) {
        return new Matrix().translate(translation);
    }

    public static Matrix from_rotation(Vector3d axis, double angle) {
        return new Matrix().rotate(axis, angle);
    }

    public String toString() {
        Double[][] ds = new Double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                ds[i][j] = m[i][j];
            }
        }
        String str = "";
        for (int i = 0; i < 4; i++) {
            str += String.format("[ %f %f %f %f ]\n", ds[i]);
        }
        return str;
    }
}
