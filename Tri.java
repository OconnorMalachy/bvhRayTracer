public class Tri{
    public Vector v0, v1, v2;
    public Vector centroid;
    public Tri(Vector vertex0,Vector vertex1,Vector vertex2){
        v0 = vertex0;
        v1 = vertex1;
        v2 = vertex2;
        centroid = vertex0.add(vertex1).add(vertex2).div(3);
    }
}
