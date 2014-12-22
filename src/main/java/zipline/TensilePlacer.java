package zipline;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

public class TensilePlacer {
    public int metadata;
    public int[][] path;
    public int path_index;
    public int startX;
    public int startY;
    public int startZ;
    public int currentX;
    public int currentY;
    public int currentZ;
    public int endX;
    public int endY;
    public int endZ;

    public TensilePlacer(int i, int j, int k, int l, int i1, int j1) {
        this.startX = (this.currentX = i);
        this.startY = (this.currentY = j);
        this.startZ = (this.currentZ = k);
        this.endX = l;
        this.endY = i1;
        this.endZ = j1;
        int k1 = this.endX - this.startX;
        int l1 = this.endY - this.startY;
        int i2 = this.endZ - this.startZ;
        int j2 = Math.abs(k1);
        int k2 = Math.abs(l1);
        int l2 = Math.abs(i2);
        int i3 = j2 <= l2 ? 0 : 1;
        boolean flag = l1 > 0;
        int j3 = j2 <= l2 ? 1 : 0;
        this.metadata = (j2 <= l2 ? 1 : 0);
        int k3 = k1 <= 0 ? -i3 : i3;
        int l3 = i2 <= 0 ? -j3 : j3;
        int i4 = i3 != 1 ? l2 : j2;
        this.path = new int[2][];
        this.path_index = 0;
        if (flag) {
            if (k2 > i4) {
                addPath(0, 1, 0, k2 - i4 + 1);
                addPath(k3, 1, l3, i4 + 1);
            } else {
                addPath(k3, 1, l3, k2 + 1);
                addPath(k3, 0, l3, i4 - k2 + 1);
            }
        } else if (k2 > i4) {
            addPath(k3, -1, l3, i4 + 1);
            addPath(0, -1, 0, k2 - i4 + 1);
        } else {
            addPath(k3, 0, l3, i4 - k2 + 1);
            addPath(k3, -1, l3, k2 + 1);
        }
        this.path_index = 0;
    }

    public void addPath(int i, int j, int k, int l) {
        this.path[this.path_index] = new int[4];
        this.path[this.path_index][0] = i;
        this.path[this.path_index][1] = j;
        this.path[this.path_index][2] = k;
        this.path[this.path_index][3] = l;
        this.path_index += 1;
    }

    public boolean canPlace(World world, int i, int j, int k) {
        Material material = world.getBlock(this.currentX, this.currentY, this.currentZ).getMaterial();
        return (material == Material.air) || (material == Material.leaves) || (material == Material.plants);
    }

    public int place(World world, Block i, int j) {
        for (; this.path_index < this.path.length && this.path[this.path_index] != null && j > 0; this.path_index += 1) {
            int[] ai = this.path[this.path_index];
            for (int k = 0; k < ai[3] && j >= 0; j--) {
                if (canPlace(world, this.currentX, this.currentY, this.currentZ)) {
                    if (!world.isAirBlock(this.currentX, this.currentY, this.currentZ)) {
                        world.func_147480_a(this.currentX, this.currentY, this.currentZ, true);
                    }
                } else {
                    return j;
                }
                int l = this.metadata;
                if (ai[1] != 0) {
                    l |= ((ai[0] == 0) && (ai[2] == 0) ? 12 : 8);
                }
                world.setBlock(this.currentX, this.currentY, this.currentZ, i, l, 3);
                this.currentX += ai[0];
                this.currentY += ai[1];
                this.currentZ += ai[2];
                k++;
            }

        }

        return j;
    }
}