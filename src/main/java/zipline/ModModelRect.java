package zipline;

import net.minecraft.client.renderer.Tessellator;

public class ModModelRect {
    public int type;
    public double x1;
    public double y1;
    public double z1;
    public double x2;
    public double y2;
    public double z2;
    public double u1;
    public double v1;
    public double u2;
    public double v2;
    public boolean twosides;
    public boolean mirror;

    public ModModelRect(int i, double d, double d1, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, boolean flag, boolean flag1) {
        this.type = i;
        this.x1 = d;
        this.y1 = d1;
        this.z1 = d2;
        this.x2 = d3;
        this.y2 = d4;
        this.z2 = d5;
        this.u1 = d6;
        this.v1 = d7;
        this.u2 = d8;
        this.v2 = d9;
        this.twosides = flag;
        this.mirror = flag1;
    }

    public void render(Tessellator tessellator, double d, double d1, double d2, int i) {
        switch (this.type) {
            case 0:
                renderRectX(tessellator, this, d, d1, d2, i);
                break;
            case 1:
                renderRectY(tessellator, this, d, d1, d2, i);
                break;
            case 2:
                renderRectZ(tessellator, this, d, d1, d2, i);
        }
    }

    public static void renderRectX(Tessellator tessellator, ModModelRect modmodelrect, double d, double d1, double d2, int i) {
        int j = (i & 0xF) << 4;
        int k = i & 0xF0;
        double d3 = (modmodelrect.u1 * 16.0D + j) / 256.0D;
        double d4 = (modmodelrect.u2 * 16.0D + j) / 256.0D;
        double d5 = (modmodelrect.v1 * 16.0D + k) / 256.0D;
        double d6 = (modmodelrect.v2 * 16.0D + k) / 256.0D;
        tessellator.addVertexWithUV(d + modmodelrect.x1, d1 + modmodelrect.y1, d2 + modmodelrect.z1, d3, d5);
        tessellator.addVertexWithUV(d + modmodelrect.x1, d1 + modmodelrect.y2, d2 + modmodelrect.z2, d3, d6);
        tessellator.addVertexWithUV(d + modmodelrect.x2, d1 + modmodelrect.y2, d2 + modmodelrect.z2, d4, d6);
        tessellator.addVertexWithUV(d + modmodelrect.x2, d1 + modmodelrect.y1, d2 + modmodelrect.z1, d4, d5);
        if (modmodelrect.twosides) {
            tessellator.addVertexWithUV(d + modmodelrect.x1, d1 + modmodelrect.y1, d2 + modmodelrect.z1, modmodelrect.mirror ? d4 : d3, d5);
            tessellator.addVertexWithUV(d + modmodelrect.x2, d1 + modmodelrect.y1, d2 + modmodelrect.z1, modmodelrect.mirror ? d3 : d4, d5);
            tessellator.addVertexWithUV(d + modmodelrect.x2, d1 + modmodelrect.y2, d2 + modmodelrect.z2, modmodelrect.mirror ? d3 : d4, d6);
            tessellator.addVertexWithUV(d + modmodelrect.x1, d1 + modmodelrect.y2, d2 + modmodelrect.z2, modmodelrect.mirror ? d4 : d3, d6);
        }
    }

    public static void renderRectY(Tessellator tessellator, ModModelRect modmodelrect, double d, double d1, double d2, int i) {
        int j = (i & 0xF) << 4;
        int k = i & 0xF0;
        double d3 = (modmodelrect.u1 * 16.0D + j) / 256.0D;
        double d4 = (modmodelrect.u2 * 16.0D + j) / 256.0D;
        double d5 = (modmodelrect.v1 * 16.0D + k) / 256.0D;
        double d6 = (modmodelrect.v2 * 16.0D + k) / 256.0D;
        tessellator.addVertexWithUV(d + modmodelrect.x1, d1 + modmodelrect.y1, d2 + modmodelrect.z1, d3, d5);
        tessellator.addVertexWithUV(d + modmodelrect.x1, d1 + modmodelrect.y2, d2 + modmodelrect.z1, d3, d6);
        tessellator.addVertexWithUV(d + modmodelrect.x2, d1 + modmodelrect.y2, d2 + modmodelrect.z2, d4, d6);
        tessellator.addVertexWithUV(d + modmodelrect.x2, d1 + modmodelrect.y1, d2 + modmodelrect.z2, d4, d5);
        if (modmodelrect.twosides) {
            tessellator.addVertexWithUV(d + modmodelrect.x1, d1 + modmodelrect.y1, d2 + modmodelrect.z1, modmodelrect.mirror ? d4 : d3, d5);
            tessellator.addVertexWithUV(d + modmodelrect.x2, d1 + modmodelrect.y1, d2 + modmodelrect.z2, modmodelrect.mirror ? d3 : d4, d5);
            tessellator.addVertexWithUV(d + modmodelrect.x2, d1 + modmodelrect.y2, d2 + modmodelrect.z2, modmodelrect.mirror ? d3 : d4, d6);
            tessellator.addVertexWithUV(d + modmodelrect.x1, d1 + modmodelrect.y2, d2 + modmodelrect.z1, modmodelrect.mirror ? d4 : d3, d6);
        }
    }

    public static void renderRectZ(Tessellator tessellator, ModModelRect modmodelrect, double d, double d1, double d2, int i) {
        int j = (i & 0xF) << 4;
        int k = i & 0xF0;
        double d3 = (modmodelrect.u1 * 16.0D + j) / 256.0D;
        double d4 = (modmodelrect.u2 * 16.0D + j) / 256.0D;
        double d5 = (modmodelrect.v1 * 16.0D + k) / 256.0D;
        double d6 = (modmodelrect.v2 * 16.0D + k) / 256.0D;
        tessellator.addVertexWithUV(d + modmodelrect.x1, d1 + modmodelrect.y1, d2 + modmodelrect.z1, d3, d5);
        tessellator.addVertexWithUV(d + modmodelrect.x1, d1 + modmodelrect.y1, d2 + modmodelrect.z2, d4, d5);
        tessellator.addVertexWithUV(d + modmodelrect.x2, d1 + modmodelrect.y2, d2 + modmodelrect.z2, d4, d6);
        tessellator.addVertexWithUV(d + modmodelrect.x2, d1 + modmodelrect.y2, d2 + modmodelrect.z1, d3, d6);
        if (modmodelrect.twosides) {
            tessellator.addVertexWithUV(d + modmodelrect.x1, d1 + modmodelrect.y1, d2 + modmodelrect.z1, modmodelrect.mirror ? d4 : d3, d5);
            tessellator.addVertexWithUV(d + modmodelrect.x2, d1 + modmodelrect.y2, d2 + modmodelrect.z1, modmodelrect.mirror ? d4 : d3, d6);
            tessellator.addVertexWithUV(d + modmodelrect.x2, d1 + modmodelrect.y2, d2 + modmodelrect.z2, modmodelrect.mirror ? d3 : d4, d6);
            tessellator.addVertexWithUV(d + modmodelrect.x1, d1 + modmodelrect.y1, d2 + modmodelrect.z2, modmodelrect.mirror ? d3 : d4, d5);
        }
    }

    public String toString() {
        return "" + this.type + ',' + this.x1 + ',' + this.y1 + ',' + this.z1 + ',' + this.x2 + ',' + this.y2 + ',' + this.z2 + ',' + this.u1 + ',' + this.v1 + ',' + this.u2 + ',' + this.v2 + ',' + this.twosides + ',' + this.mirror;
    }
}