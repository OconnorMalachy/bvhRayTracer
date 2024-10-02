public class RayTracer{
    
    public static boolean intersectAABB(Ray r, Vector bmin, Vector bmax){

        double tx1 = (bmin.x - r.O.x) / r.D.x;
        double tx2 = (bmax.x - r.O.x) / r.D.x;
        double tmin = Math.min(tx1, tx2);
        double tmax = Math.max(tx1, tx2);

        double ty1 = (bmin.y - r.O.y) / r.D.y;
        double ty2 = (bmax.y - r.O.y) / r.D.y;
        tmin = Math.max(tmin, Math.min(ty1, ty2));
        tmax = Math.min(tmax, Math.max(ty1, ty2));
        
        double tz1 = (bmin.z - r.O.z) / r.D.z;
        double tz2 = (bmax.z - r.O.z) / r.D.z;
        tmin = Math.max(tmin, Math.min(tz1, tz2));
        tmax = Math.min(tmax, Math.max(tz1, tz2));

        return tmax >= tmin && tmin < r.t && tmax > 0;
    }
    // Moller-Trumbore algo
    public static void intersectTri(Ray r, Tri tri){
        Vector edge1 = tri.v1.sub(tri.v0);
        Vector edge2 = tri.v2.sub(tri.v0);
        Vector h = r.D.cross(edge2);
        double a =  edge1.dot(h);

        if(a > -0.0001 && a < 0.0001){return;}
        
        double f = 1.0/a;
        Vector s = r.O.sub(tri.v0);
        double u = f * s.dot(h);
        
        if(u < 0.0 || u > 1.0){return;}
        
        Vector q = s.cross(edge1);
        double v = f * r.D.dot(q);

        if(v < 0.0 || u + v > 1.0){return;}
        
        double t = f * edge2.dot(q);
        
        if(t > 0.0001 && t < r.t){
            r.t = t;
        }
    }
}
