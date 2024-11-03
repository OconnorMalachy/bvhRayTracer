import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;

public class Main {
    private static final int COEF = 40;
    private static final int WIDTH = COEF * 16;
    private static final int HEIGHT = COEF * 9;
    public static List<Tri> triangles = new ArrayList<>();
    public static void initTriangles(String fName){
        List<Vector> vertices = new ArrayList<>();
        try(BufferedReader r = new BufferedReader(new FileReader(fName))) {
            String line;
            while ((line = r.readLine()) != null) {
                line = line.trim();
                String[] parts = line.split("\\s+");
                
                if (parts.length > 0) {
                    switch (parts[0]) {
                        case "v":
                            float x = Float.parseFloat(parts[1]);
                            float y = Float.parseFloat(parts[2]);
                            float z = Float.parseFloat(parts[3]);
                            vertices.add(new Vector(x, y, z));
                            break;

                        case "f":
                            int v0Index = Integer.parseInt(parts[1].split("/")[0]) - 1;
                            int v1Index = Integer.parseInt(parts[2].split("/")[0]) - 1;
                            int v2Index = Integer.parseInt(parts[3].split("/")[0]) - 1;
                            Tri triangle = new Tri();
                            triangle.v0 = vertices.get(v0Index);
                            triangle.v1 = vertices.get(v1Index);
                            triangle.v2 = vertices.get(v2Index);
                            triangles.add(triangle);
                            break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("TRIANGLES INITIALIZED! Triangle count: " + triangles.size());
    }

    public static void render() {
            Camera camera = new Camera(new Vector(0), 250);
            BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
            long startTime = System.currentTimeMillis();
            double maxDepth = Double.NEGATIVE_INFINITY;
            for (int y = 0; y < HEIGHT; y++) {
                Color col;
                for (int x = 0; x < WIDTH; x++) {
                    Ray ray = camera.generateRay(x, y, WIDTH, HEIGHT);
                    for(Tri triangle : triangles){
                        RayTracer.intersectTri(ray, triangle);
                        col = triangle.color;
                    }
                    if (ray.t < Double.MAX_VALUE) {
                        maxDepth = Math.max(maxDepth, ray.t); 
                    }
                    double brightnessFactor = 5; // Adjust this value to increase brightness
                    int colorValue = ray.t < Double.MAX_VALUE ? (int) ((255 - (ray.t * 255 / maxDepth)) * brightnessFactor) : 0;
                    int rgbColor = (colorValue << 16) | (colorValue << 8) | colorValue; 
                    image.setRGB(x, y, rgbColor);
                }
                System.out.printf("\rScanline progress [%d / %d]", y+1, HEIGHT);

            }
            long endTime = System.currentTimeMillis();
            System.out.printf("\nRay Tracing completed in %d ms", endTime - startTime);
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
            initTriangles("Assets/dragon.obj");
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
