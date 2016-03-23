package raytracer.samplers;

import raytracer.math.Point2d;
import raytracer.math.Point3d;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Thijs on 22/11/2015.
 */
public abstract class Sampler {
    public int numSamples;
    public int numSets;
    protected int jump = 0;
    protected int count = 0;
    protected Random random = new Random();
    public ArrayList<Point2d> samples = new ArrayList<Point2d>();
    public ArrayList<Point3d> hemisphereSamples = new ArrayList<Point3d>();
    public Point2d sampleUnitSquare() {
        if (count == numSamples) {
            count = 0;
            jump = random.nextInt(numSets);
        }

        return samples.get(jump * numSamples + (count++));
    }
    public Point3d sampleUnitHemisphere() {
        if (count == numSamples) {
            count = 0;
            jump = random.nextInt(numSets);
        }

        return hemisphereSamples.get(jump * numSamples + (count++));
    }
    public void mapToHemisphere(double e) {
        for (Point2d sample : samples) {
            double cos_phi = Math.cos(2*Math.PI*(sample.x+0.5));
            double sin_phi = Math.sin(2*Math.PI*(sample.x+0.5));
            double cos_theta = Math.pow(1d - (sample.y+0.5), 1.0/(e+1.0));
            double sin_theta = Math.sqrt(1d - cos_theta*cos_theta);
            double pu = sin_theta*cos_phi;
            double pv = sin_theta*sin_phi;
            double pw = cos_theta;
            if (Double.isNaN(pu)) {
                System.out.println(1.0-cos_theta*cos_theta);
            }
            hemisphereSamples.add(new Point3d(pu, pv, pw));
        }
    }
    public abstract void generateSamples();
}
