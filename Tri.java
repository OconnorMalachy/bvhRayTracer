public class Tri{

    public Vector v0, v1, v2;
    public Vector centroid;
    public Tri(){}
    public Tri(Vector vertex0,Vector vertex1,Vector vertex2){
        v0 = vertex0;
        v1 = vertex1;
        v2 = vertex2;
    }
    public static Vector calcCentroid(Tri t){
        double cX = (t.v0.x + t.v1.x + t.v2.x) / 3.0;
        double cY = (t.v0.y + t.v1.y + t.v2.y) / 3.0;
        double cZ = (t.v0.z + t.v1.z + t.v2.z) / 3.0;
        return new Vector(cX, cY, cZ);
    }
}
