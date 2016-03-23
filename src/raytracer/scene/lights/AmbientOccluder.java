package raytracer.scene.lights;

import raytracer.math.Color;
import raytracer.samplers.RegularSampler;
import raytracer.samplers.Sampler;

/**
 * Created by Thijs on 13/02/2016.
 */
public class AmbientOccluder extends Light {
    public Sampler sampler = new RegularSampler(1024);
    public AmbientOccluder() {
        color = Color.WHITE;
    }
    public AmbientOccluder(Color color) {
        this.color = color;
    }
}
