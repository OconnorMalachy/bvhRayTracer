public class Camera {
    public Vector position;
    public Vector p0, p1, p2;
    public Vector target; // Target point to look at

    public Camera(Vector target, double distanceToTarget) {
        this.target = target;

        // Set the camera's position a certain distance from the target along the Z-axis
        position = target.add(new Vector(distanceToTarget * 10, 0, -distanceToTarget));

        // Define the width and height of the viewing plane based on the field of view
        double planeWidth = distanceToTarget;
        double planeHeight = distanceToTarget;

        p0 = target.add(new Vector(-planeWidth / 2, planeHeight / 2, 0));
        p1 = target.add(new Vector(planeWidth / 2, planeHeight / 2, 0));
        p2 = target.add(new Vector(-planeWidth / 2, -planeHeight / 2, 0));
    }

    public Ray generateRay(int x, int y, int imageWidth, int imageHeight) {
        double u = x / (double) imageWidth;
        double v = y / (double) imageHeight;
        Vector pixelPos = p0.add(p1.sub(p0).mul(u)).add(p2.sub(p0).mul(v));
        Vector direction = pixelPos.sub(position).normalize();
        return new Ray(position, direction);
    }
}

