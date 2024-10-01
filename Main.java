import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class Main {

    private static final int WIDTH = 1280;
    private static final int HEIGHT = (WIDTH * 9 ) / 16;
    private static final int TRIANGLE_COUNT = 200;
    private static Tri[] triangles = new Tri[TRIANGLE_COUNT];
    private static BVH bvh;

    public static void initTriangles() {
        for (int i = 0; i < TRIANGLE_COUNT; i++) {
            Vector r0 = new Vector(Math.random(), Math.random(), Math.random()).mul(9).sub(new Vector(5.0));
            Vector r1 = new Vector(Math.random(), Math.random(), Math.random());
            Vector r2 = new Vector(Math.random(), Math.random(), Math.random());
            triangles[i] = new Tri(r0, r0.add(r1), r0.add(r2));
        }
    }

    public static void buildBVH() {
        bvh = new BVH(triangles, TRIANGLE_COUNT); // Initialize BVH with triangles
    }
    public static void render() {
        Camera camera = new Camera();
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Ray ray = camera.generateRay(x, y, WIDTH, HEIGHT);
                
                RayTracer.intersectBVH(ray, bvh, bvh.rootNodeIdx);

                int colorValue = ray.t < Double.MAX_VALUE ? 255 : 0;  
                int rgbColor = (colorValue << 16) | (colorValue << 8) | colorValue; 
                image.setRGB(x, y, rgbColor);
            }
            System.out.printf("\rScanline progress [%d / %d]", y+1, HEIGHT);

        }

        try {
            File outputfile = new File("rendered_image.png");
            ImageIO.write(image, "png", outputfile);
            System.out.println("\nImage saved as rendered_image.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
        new displayImage("rendered_image.png");
    }

    public static void main(String[] args) {
        initTriangles();
        buildBVH();
        render();
    }
}
class displayImage extends JFrame{
    private BufferedImage img;
    public displayImage(String filePath){
        try{img = ImageIO.read(new File(filePath));}
        catch(Exception e){
            e.printStackTrace();
        }
    
        setTitle("RAY TRACED RESULTS");
        setSize(img.getWidth(), img.getHeight());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (img != null) {
            g.drawImage(img, 0, 0, null);
        }
    }
}
