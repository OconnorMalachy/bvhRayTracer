public class AABB{
    public Vector bmin = new Vector(Double.MAX_VALUE);
    public Vector bmax = new Vector(Double.MIN_VALUE);
    AABB(){}
    void grow(Vector p){
        bmin = Vector.min(bmin, p);
        bmax = Vector.max(bmax, p);
    }
    double area(){
        Vector e = bmax.sub(bmin);
        return e.x * e.y + e.y * e.z + e.z * e.x;
    }
}
