public class BVH{
    BVHNode[] bvhNode;
    public Tri[] tris;
    public int[] triIdx;
    int rootNodeIdx = 0, nodesUsed = 1;
    public BVH(Tri[] triangles, int N){
        tris = triangles;
        triIdx = new int[N];
        bvhNode = new BVHNode[N*2 - 1];
        for (int i = 0; i < bvhNode.length; i++) {
            bvhNode[i] = new BVHNode(); // Initialize each node
        }
        for(int i = 0; i < N; i++){
            triIdx[i] = i;
            tris[i].centroid = (tris[i].v0.add(tris[i].v1).add(tris[i].v2)).mul(0.3333);
        }
        BVHNode root = bvhNode[rootNodeIdx];
        root.leftNode = 0;
        root.firstTriIdx =0; 
        root.triCount = N;

        updateNodeBounds(rootNodeIdx);
        subdivide(rootNodeIdx);
    }
    public void subdivide(int nodeIdx){
        BVHNode node = bvhNode[nodeIdx];
        if(node.triCount <= 2){return;}

        Vector extent = node.aabbMax.sub(node.aabbMin);
        int axis = 0;
        if(extent.y > extent.x){axis = 1;}
        if(extent.z > extent.el(axis)){axis = 2;}
        double splitPos = node.aabbMin.el(axis) + extent.el(axis) * 0.5;
        
        int i = node.firstTriIdx;
        int j = i + node.triCount - 1;
        while(i <= j){
            if(tris[i].centroid.el(axis) < splitPos){i++;}
            else{swap(triIdx[i], triIdx[j--]);}
        }

        int leftCount = i - node.firstTriIdx;
        if(leftCount == 0 || leftCount == node.triCount){return;}
        int leftChildIdx = nodesUsed++;
        int rightChildIdx = nodesUsed++;

        bvhNode[leftChildIdx].firstTriIdx = node.firstTriIdx;  // Updated to firstTriIdx
        bvhNode[leftChildIdx].triCount = leftCount;  // Updated to triCount
        bvhNode[rightChildIdx].firstTriIdx = i;  // Updated to firstTriIdx
        bvhNode[rightChildIdx].triCount = node.triCount - leftCount;  // Updated to triCount
        node.leftNode = leftChildIdx;  // Updated to leftNode
        node.triCount = 0;  // Reset triCount

        updateNodeBounds(leftChildIdx);
        updateNodeBounds(rightChildIdx);

        subdivide(leftChildIdx);
        subdivide(rightChildIdx);
    }
    public void updateNodeBounds(int nodeIdx){
        BVHNode node = bvhNode[nodeIdx];
        node.aabbMin = new Vector(1e30f);
        node.aabbMax = new Vector(-1e30f);

        for(int i =0; i < node.triCount; i++){
            Tri leafTri = tris[triIdx[node.firstTriIdx + i]];
            node.aabbMin = Vector.min(node.aabbMin, leafTri.v0);
            node.aabbMin = Vector.min(node.aabbMin, leafTri.v1);
            node.aabbMin = Vector.min(node.aabbMin, leafTri.v2);
        
            node.aabbMax = Vector.max(node.aabbMax, leafTri.v0);
            node.aabbMax = Vector.max(node.aabbMax, leafTri.v1);
            node.aabbMax = Vector.max(node.aabbMax, leafTri.v2);        }
    }
    public void swap(int a, int b) {
        int temp = triIdx[a];
        triIdx[a] = triIdx[b];
        triIdx[b] = temp;
    }
}
