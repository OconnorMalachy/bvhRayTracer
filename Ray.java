public class Ray{
    public Vector origin, direction;
    public double t;
    public Ray(Vector origin, Vector direction){
        this.origin = origin;
        this.direction = direction;
        t = Double.MAX_VALUE;
    }
}
