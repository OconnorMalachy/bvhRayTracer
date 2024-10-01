public class RayTracer{

    // Moller-Trumbore algo
    public static void intersectTriangle(Ray r, Tri tri){
        Vector edge1 = tri.v1.sub(tri.v0);
        Vector edge2 = tri.v2.sub(tri.v0);
        Vector h = r.direction.cross(edge2);
        double a =  edge1.dot(h);

        if(a > -0.0001 && a < 0.0001){return;}
        
        double f = 1.0/a;
        Vector s = r.origin.sub(tri.v0);
        double u = f * s.dot(h);
        
        if(u < 0.0 || u > 1.0){return;}
        
        Vector q = s.cross(edge1);
        double v = f * r.direction.dot(q);

        if(v < 0.0 || u + v > 1.0){return;}
        
        double t = f * edge2.dot(q);
        
        if(t > 0.0001 && t < r.t){
            r.t = t;
        }
    }
    public static void intersectBVH(Ray ray, BVH bvh, int nodeIdx) {
        BVHNode node = bvh.bvhNode[nodeIdx];
        if (!intersectAABB(ray, node.aabbMin, node.aabbMax)) {
            return; // Ray misses the AABB
        }

        if (node.isLeaf()) {
            // If it's a leaf node, intersect the ray with the triangles
            for (int i = 0; i < node.triCount; i++) {
                intersectTriangle(ray, bvh.tris[bvh.triIdx[node.firstTriIdx + i]]);
            }
        } else {
            // Recurse into child nodes
            intersectBVH(ray, bvh, node.leftNode);
            intersectBVH(ray, bvh, node.leftNode + 1); // Right child
        }
    }

    public static boolean intersectAABB(Ray ray, Vector bmin, Vector bmax) {
        double tx1 = (bmin.x - ray.origin.x) / ray.direction.x;
        double tx2 = (bmax.x - ray.origin.x) / ray.direction.x;
        double tmin = Math.min(tx1, tx2);
        double tmax = Math.max(tx1, tx2);

        double ty1 = (bmin.y - ray.origin.y) / ray.direction.y;
        double ty2 = (bmax.y - ray.origin.y) / ray.direction.y;
        tmin = Math.max(tmin, Math.min(ty1, ty2));
        tmax = Math.min(tmax, Math.max(ty1, ty2));

        double tz1 = (bmin.z - ray.origin.z) / ray.direction.z;
        double tz2 = (bmax.z - ray.origin.z) / ray.direction.z;
        tmin = Math.max(tmin, Math.min(tz1, tz2));
        tmax = Math.min(tmax, Math.max(tz1, tz2));

        return tmax >= tmin && tmin < ray.t && tmax > 0; // Check if ray intersects AABB
    }
}
