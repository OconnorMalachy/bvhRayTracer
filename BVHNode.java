public class BVHNode{
    public Vector aabbMin, aabbMax;
    public int leftNode, firstTriIdx, triCount;
    public boolean isLeaf(){return triCount > 0;}
}
