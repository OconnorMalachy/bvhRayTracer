public class Ray{
    public Vector O, D;
    public double t;
    public Ray(Vector origin, Vector direction){
        O = origin;
        D = direction;
        t = Double.MAX_VALUE;
    }
}
