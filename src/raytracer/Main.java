package raytracer;

import raytracer.math.Color;
import raytracer.math.Point3d;
import raytracer.math.Vector3d;
import raytracer.samplers.RegularSampler;
import raytracer.scene.Camera;
import raytracer.scene.Object3D;
import raytracer.scene.Scene;
import raytracer.scene.cameras.OrthographicCamera;
import raytracer.scene.cameras.PerspectiveCamera;
import raytracer.scene.lights.AmbientLight;
import raytracer.scene.lights.Light;
import raytracer.scene.lights.PointLight;
import raytracer.scene.objects.*;
import raytracer.scene.objects.Box;
import raytracer.tracers.RayTracer;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by Thijs on 04/10/2015.
 */
public class Main {

    public static int IMAGE_WIDTH = 400;
    public static int IMAGE_HEIGHT = 400;
    public static double PIXEL_SIZE = 0.006125;
    public static double FPS = 30;
    public static double GIF_LENGTH = 2;

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        BufferedImage image = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
        frame.getContentPane().add(new JLabel(new ImageIcon(image)));
        frame.setSize(image.getHeight() + frame.getSize().height, image.getWidth() + frame.getSize().width);
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        try {
            ImageOutputStream output = new FileImageOutputStream(new File("anim.gif"));
            GifSequenceWriter writer = new GifSequenceWriter(output, image.getType(), (int)(1000/FPS), true);
            for (int i = 0; i < FPS*GIF_LENGTH; i++) {
                Scene scene = constructScene(i/(FPS*GIF_LENGTH));
                scene.render(image, frame);
                writer.writeToSequence(image);
            }
            writer.close();
            output.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        File outFile = new File("image.png");
    }
    public static Scene constructScene(double gifProgress) {
        ArrayList<Object3D> objects = new ArrayList<Object3D>();
        Object3D sphere = new Sphere(new Point3d(0, 0.5, 0), 0.5);
        sphere.material.reflectionCoefficient = 0.1;
        sphere.material.diffuseCoefficient = sphere.material.ambientCoefficient = 0;
        sphere.material.specularCoefficient = 1.0;
        sphere.material.specularExponent = 200;
        sphere.material.transparencyCoefficient = 0.9;
        sphere.material.refractionIndex = 1.52;
        Object3D sphere2 = new Sphere(new Point3d(-2, 0.5, -1), 0.5, Color.RED);
        Object3D plane = new Plane(new Point3d(0, 0, 0), new Vector3d(0, 1, 0), new Texture(new File("checkerboard.png")));
        objects.add(sphere);
        objects.add(sphere2);
        objects.add(plane);
        Camera camera = new PerspectiveCamera(PIXEL_SIZE, IMAGE_WIDTH, IMAGE_HEIGHT, new Point3d(Math.cos(gifProgress*Math.PI*2), Math.sin(gifProgress*Math.PI)+0.5, Math.sin(gifProgress*Math.PI*2)), 1.5, new Point3d(0, 0.5, 0), new Vector3d(0, 1, 0.01));
        Tracer tracer = new RayTracer();
        ArrayList<Light> lights = new ArrayList<Light>();
        lights.add(new AmbientLight());
        Point3d lightPoint = new Point3d(-3, 2, 0);
        lights.add(new PointLight(lightPoint, 10));
        Scene scene = new Scene(objects, lights, camera, tracer);
        scene.sampler = new RegularSampler(16);
        return scene;
    }
}
