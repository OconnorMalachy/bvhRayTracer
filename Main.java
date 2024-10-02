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
    private static final int WIDTH = 256;
    private static final int HEIGHT = 256;//12,582
    private static int TRIANGLE_COUNT = 12582;
    public static Tri[] triangles = new Tri[TRIANGLE_COUNT];
    private static BVH bvh;
    public static void initTriangles(String fName){
        try(BufferedReader r = new BufferedReader(new FileReader(fName))){
            for(int t = 0; t < TRIANGLE_COUNT; t++){
            String line = r.readLine().trim();
            String[] values = line.split(" ");
            try{
                float[] coords = new float[9];
                for(int i = 0; i < 9; i++){coords[i] = Float.parseFloat(values[i]);}
                triangles[t] = new Tri();
                triangles[t].v0 = new Vector(coords[0], coords[1], coords[2]);
                triangles[t].v1 = new Vector(coords[3], coords[4], coords[5]);
                triangles[t].v2 = new Vector(coords[6], coords[7], coords[8]);
            }catch (NumberFormatException e) {
                    System.err.println("Error parsing line " + (t + 1) + ": " + line);
                    e.printStackTrace();
            }
          }
        } catch(Exception e){ e.printStackTrace();}
        System.out.println("TRIANGLES INITIALIZED!");
    }
    public static void initTrianglesFromObj(String fName) {
        try (BufferedReader r = new BufferedReader(new FileReader(fName))) {
            String line;
            List<Vector> vertices = new ArrayList<>();
            List<Tri> tempTriangles = new ArrayList<>();
            
            while ((line = r.readLine()) != null) {
                line = line.trim();
                String[] values = line.split(" ");
                
                if (values[0].equals("v")) {
                    float x = Float.parseFloat(values[1]);
                    float y = Float.parseFloat(values[2]);
                    float z = Float.parseFloat(values[3]);
                    vertices.add(new Vector(x, y, z));
                }
                else if (values[0].equals("f")) {
                    int v0Idx = Integer.parseInt(values[1].split("/")[0]) - 1; 
                    int v1Idx = Integer.parseInt(values[2].split("/")[0]) - 1;
                    int v2Idx = Integer.parseInt(values[3].split("/")[0]) - 1;

                    Tri triangle = new Tri();
                    triangle.v0 = vertices.get(v0Idx);
                    triangle.v1 = vertices.get(v1Idx);
                    triangle.v2 = vertices.get(v2Idx);
                    tempTriangles.add(triangle);
                }
            }
            TRIANGLE_COUNT = tempTriangles.size();
            triangles = new Tri[TRIANGLE_COUNT];
            for (int i = 0; i < tempTriangles.size(); i++) {
                triangles[i] = tempTriangles.get(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("TRIANGLES INITIALIZED FROM OBJ!");
    }


    public static void buildBVH() {
        bvh = new BVH(triangles, TRIANGLE_COUNT); // Initialize BVH with triangles
    }
    public static void render() {
        Camera camera = new Camera();
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        long startTime = System.currentTimeMillis();
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Ray ray = camera.generateRay(x, y, WIDTH, HEIGHT);
                bvh.intersectBVH(ray, bvh.rootNodeIdx);

                int colorValue = ray.t < Double.MAX_VALUE ? (500 - (int) (ray.t * 42)) : 0;
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
        //initTrianglesFromObj("assets/dragon.obj");
        initTriangles("assets/unity.tri");
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
