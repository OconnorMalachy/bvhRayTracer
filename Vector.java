public class Vector{
    public double x, y, z;
    public Vector(double xyz){
        x = xyz;
        y = xyz;
        z = xyz;
    }
    public Vector(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public double el(int idx){
        if(idx == 0){return x;}
        if(idx == 1){return y;}
        return z;
    }
    public Vector add(Vector other){
        return new Vector(x + other.x, y + other.y, z + other.z);
    }
    public Vector sub(Vector other){
        return new Vector(x - other.x, y - other.y, z - other.z);
    }
    public Vector mul(double scalar) {
        return new Vector(x * scalar, y * scalar, z * scalar);
    }
    public Vector div(double scalar) {
        return new Vector(x / scalar, y / scalar, z / scalar);
    }
    public Vector cross(Vector other){
        return new Vector(
            y * other.z - z * other.y,
            z * other.x - x * other.z,
            x * other.y - y * other.x
        );
    }
    public double dot(Vector other){
        return x * other.x + y * other.y + z * other.z;
    }
    public Vector normalize(){
        double length = (double) Math.sqrt(x*x + y*y + z*z);
        return new Vector(x/length, y/length, z/length);
    }
    public static Vector min(Vector a, Vector b) {
        return new Vector(Math.min(a.x, b.x), Math.min(a.y, b.y), Math.min(a.z, b.z));
    }
    public static Vector max(Vector a, Vector b) {
        return new Vector(Math.max(a.x, b.x), Math.max(a.y, b.y), Math.max(a.z, b.z));
    }
  
}
