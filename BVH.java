import java.util.stream.IntStream;
public class BVH{
    BVHNode[] bvhNode;
    public Tri[] tris;
    public int[] triIdx;
    int rootNodeIdx = 0, nodesUsed = 1;

    public BVH(Tri[] triangles, int N){
        tris = triangles;
        triIdx = new int[N];
        bvhNode = IntStream.range(0, N * 2).mapToObj(i -> new BVHNode()).toArray(BVHNode[]::new);
        for(int i = 0; i < N; i++){
            triIdx[i] = i;
            tris[i].centroid = Tri.calcCentroid(tris[i]);
        }
        BVHNode root = bvhNode[rootNodeIdx];
        root.leftFirst = 0;
        root.triCount = N;

        updateNodeBounds(rootNodeIdx);
        subdivide(rootNodeIdx);
    }
    private void updateNodeBounds(int nodeIdx){
        BVHNode node = bvhNode[nodeIdx];
        node.aabbMin = new Vector(Double.MAX_VALUE);
        node.aabbMax = new Vector(Double.MIN_VALUE);
        
        int first = node.leftFirst;
        for(int i =0; i < node.triCount; i++){
            int leafTriIdx = triIdx[first + i];
            Tri leafTri = tris[leafTriIdx];
            node.aabbMin = Vector.min(node.aabbMin, leafTri.v0);
            node.aabbMin = Vector.min(node.aabbMin, leafTri.v1);
            node.aabbMin = Vector.min(node.aabbMin, leafTri.v2);
        
            node.aabbMax = Vector.max(node.aabbMax, leafTri.v0);
            node.aabbMax = Vector.max(node.aabbMax, leafTri.v1);
            node.aabbMax = Vector.max(node.aabbMax, leafTri.v2);        
        }
    }
    private void subdivide(int nodeIdx){
        BVHNode node = bvhNode[nodeIdx];
        if(node.triCount <= 2){return;}
        
        int bestAxis = -1;
        double bestPos = 0;
        double bestCost = Double.MAX_VALUE;
        for(int axis = 0 ; axis<3; axis++){
            for(int i = 0; i < node.triCount; i++){
                Tri triangle = tris[triIdx[node.leftFirst + i]];
                double candidatePos = triangle.centroid.el(axis);
                double cost = EvaluateSAH(node, axis, candidatePos);
                if(cost < bestCost){
                    bestPos = candidatePos;
                    bestAxis = axis;
                    bestCost = cost;
                }
            }
            System.out.printf("DONE\n");
        }
        int axis = bestAxis;
        double splitPos = bestPos;
        int i = node.leftFirst;
        int j = i + node.triCount-1;
        while(i <= j){
            if(tris[triIdx[i]].centroid.el(axis) < splitPos){i++;}
            else{swap(triIdx[i], triIdx[j--]);}
        }
        int leftCount = i - node.leftFirst;
        if(leftCount == 0 || leftCount == node.triCount){
            return;
        }
        int leftChildIdx = nodesUsed++;
        int rightChildIdx = nodesUsed++;
        bvhNode[leftChildIdx].leftFirst = node.leftFirst;
        bvhNode[leftChildIdx].triCount = leftCount;
        bvhNode[rightChildIdx].leftFirst = i;
        bvhNode[rightChildIdx].triCount = node.triCount - leftCount;
        node.leftFirst = leftChildIdx;
        node.triCount = 0;

        updateNodeBounds(leftChildIdx);
        updateNodeBounds(rightChildIdx);

        subdivide(leftChildIdx);
        subdivide(rightChildIdx);
    }
    private void swap(int a, int b) {
        int temp = triIdx[a];
        triIdx[a] = triIdx[b];
        triIdx[b] = temp;
    }
    public void intersectBVH(Ray r, int nodeIdx){
        BVHNode node = bvhNode[nodeIdx];
        if(!RayTracer.intersectAABB(r, node.aabbMin, node.aabbMax)){return;}
        if(node.isLeaf()){
            for(int i = 0; i < node.triCount; i++){
                RayTracer.intersectTri(r, tris[triIdx[node.leftFirst+i]]);
            }
        }else{
            intersectBVH(r, node.leftFirst);
            intersectBVH(r, node.leftFirst + 1);
        }
    }
    private double EvaluateSAH(BVHNode node, int axis, double pos){
        AABB leftBox = new AABB();
        AABB rightBox = new AABB();

        int leftCount = 0;
        int rightCount = 0;
        for(int i = 0; i < node.triCount; i++){
            Tri triangle = tris[triIdx[node.leftFirst + i]];
            if(triangle.centroid.el(axis) < pos){
                leftCount++;
                leftBox.grow(triangle.v0);
                leftBox.grow(triangle.v1);
                leftBox.grow(triangle.v2);
            }else{
                rightCount++;
                rightBox.grow(triangle.v0);
                rightBox.grow(triangle.v1);
                rightBox.grow(triangle.v2);
            }
        }
        double cost = leftCount * leftBox.area() + rightCount*rightBox.area();
        return cost > 0 ? cost : Double.MAX_VALUE;
    }
}
