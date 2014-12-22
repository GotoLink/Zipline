package zipline;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;

import java.util.ArrayList;

public class ModModel {
    public ArrayList<double[]> vertices;
    public int x;
    public int y;
    public int z;
    public double u;
    public double v;

    public ModModel(int i, int j, int k, IIcon l) {
        setPosition(i, j, k);
        setTexture(l);
        this.vertices = new ArrayList<double[]>();
    }

    public void setPosition(int i, int j, int k) {
        this.x = i;
        this.y = j;
        this.z = k;
    }

    public void setTexture(IIcon i) {
        this.u = i.getMinU();
        this.v = i.getMinV();
    }

    public void addVertexWithUV(double d, double d1, double d2, double d3, double d4) {
        this.vertices.add(new double[]{d, d1, d2, d3, d4});
    }

    public void addVertexWithUV(double d, double d1, double d2, int i, double d3, double d4) {
        double d5 = 0.0D;
        double d6 = 0.0D;
        switch ((i & 0x38) >> 3) {
            default:
                d5 = d;
                break;
            case 1:
                d5 = d1;
                break;
            case 2:
                d5 = d2;
                break;
            case 3:
                d5 = 1.0D - d;
                break;
            case 4:
                d5 = 1.0D - d1;
                break;
            case 5:
                d5 = 1.0D - d2;
        }

        switch (i & 0x7) {
            default:
                d6 = d;
                break;
            case 1:
                d6 = d1;
                break;
            case 2:
                d6 = d2;
                break;
            case 3:
                d6 = 1.0D - d;
                break;
            case 4:
                d6 = 1.0D - d1;
                break;
            case 5:
                d6 = 1.0D - d2;
        }

        addVertexWithUV(d, d1, d2, Math.max(0.0D, Math.min(1.0D, d5 + d3)), Math.max(0.0D, Math.min(1.0D, d6 + d4)));
    }

    public void render(boolean flag) {
        render(flag, false, false);
    }

    public void render(boolean flag, boolean flag1) {
        render(flag, flag1, false);
    }

    public void render(boolean flag, boolean flag1, boolean flag2) {
        Tessellator tessellator = Tessellator.instance;
        int i = flag1 ? -1 : 1;
        int j = flag2 ? -1 : 1;
        int k = flag1 ? 1 : 0;
        int l = flag2 ? 1 : 0;
        if (!flag) {
            for (double[] ad : this.vertices) {
                tessellator.addVertexWithUV(this.x + ad[0], this.y + ad[1], this.z + ad[2], this.u + (ad[3] * i + k) / (3 * 16.0D), this.v + (ad[4] * j + l) / (3 * 16.0D));
            }
        } else {
            for (int j1 = this.vertices.size() - 1; j1 >= 0; j1--) {
                double[] ad1 = this.vertices.get(j1);
                tessellator.addVertexWithUV(this.x + ad1[0], this.y + ad1[1], this.z + ad1[2], this.u + (ad1[3] * i + k) / (3 * 16.0D), this.v + (ad1[4] * j + l) / (3 * 16.0D));
            }
        }
    }
}