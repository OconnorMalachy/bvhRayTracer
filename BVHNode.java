public class BVHNode{
    public Vector aabbMin, aabbMax;
    public int leftFirst, triCount;
    public boolean isLeaf(){return triCount > 0;}
}
