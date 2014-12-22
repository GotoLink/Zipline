package zipline;

import net.minecraft.util.Vec3;

public class MyVec3D extends Vec3 {
    public MyVec3D(double d, double d1, double d2) {
        super(d, d1, d2);
    }

    @Override
    public double distanceTo(Vec3 vec3d) {
        return 1000000.0D;
    }
}