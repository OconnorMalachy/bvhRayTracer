public class Camera{
    public Vector position;
    public Vector p0, p1, p2;
    public Camera(){
        position = new Vector(0, 0, -18);
        p0 = new Vector(-1, 1, -15);
        p1 = new Vector(1, 1, -15);
        p2 = new Vector(-1, -1, -15);
    }
    public Ray generateRay(int x, int y, int imageWidth, int imageHeight){
        double u = x / (double) imageWidth;
        double v = y / (double) imageHeight;
        Vector pixelPos = p0.add(p1.sub(p0).mul(u)).add(p2.sub(p0).mul(v));
        Vector direction = pixelPos.sub(position).normalize();
        return new Ray(position, direction);
    }
}
