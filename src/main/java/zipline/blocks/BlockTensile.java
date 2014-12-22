package zipline.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import zipline.ModModel;
import zipline.MyVec3D;
import zipline.mod_zipline;

import java.util.List;
import java.util.Random;

public class BlockTensile extends Block implements ITensile {
    public static float swingPosition = 0.0F;
    public static float swingAmplitude = 0.0F;
    public static float swingDecay = 0.9F;
    public double[] crossPieces = {0.5D};

    public static double[] heights = {0.125D, 0.375D, 0.625D, 1.125D};
    public double pixelsWide;
    public double pixelsHigh;
    public double boundingWidth;
    public double boundingHeight;

    public BlockTensile(Material material) {
        super(material);
        this.pixelsWide = 2.0D;
        this.pixelsHigh = 2.0D;
        this.boundingWidth = 0.25D;
        this.boundingHeight = 0.25D;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean canPlaceBlockAt(World world, int i, int j, int k) {
        return super.canPlaceBlockAt(world, i, j, k) && !world.getBlock(i, j, k).getMaterial().isLiquid();
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getRenderType() {
        return mod_zipline.proxy.getRopeRenderType();
    }

    @Override
    public int tickRate(World world) {
        return 3;
    }

    @Override
    public void updateTick(World world, int i, int j, int k, Random random) {
        if (tryToFall(world, i, j, k) && tryToLift(world, i, j, k)) {
            updateMetadata(world, i, j, k);
        }
    }

    @Override
    public void onBlockPlacedBy(World world, int i, int j, int k, EntityLivingBase entityliving, ItemStack itemStack) {
        int l = MathHelper.floor_double(entityliving.rotationYaw * 4.0F / 360.0F + 0.5D) & 0x3;
        int i1 = 0;
        if (l == 0 || l == 2) {
            i1 = 1;
        }
        world.setBlockMetadataWithNotify(i, j, k, i1, 3);
        onBlockRemoval(world, i, j, k);
    }

    @Override
    public void onBlockClicked(World world, int i, int j, int k, EntityPlayer entityplayer) {
        updateTick(world, i, j, k, null);
        world.scheduleBlockUpdate(i, j, k, this, tickRate(world));
    }

    @Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int side, float vecX, float vecY, float vecZ) {
        if (world.isRemote) {
            return true;
        }
        if (entityplayer.ridingEntity != null) {
            entityplayer.mountEntity(entityplayer.ridingEntity);
            return true;
        }

        return false;
    }

    @Override
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z) {
        onBlockRemoval(world, x, y, z);
        return super.removedByPlayer(world, player, x, y, z);
    }

    public void onBlockRemoval(World world, int i, int j, int k) {
        world.notifyBlockChange(i, j + 1, k, this);
        world.notifyBlockChange(i, j, k, this);
        world.notifyBlockChange(i, j - 1, k, this);
    }

    @Override
    public void onNeighborBlockChange(World world, int i, int j, int k, Block l) {
        world.scheduleBlockUpdate(i, j, k, this, tickRate(world));
    }

    @Override
    public void addCollisionBoxesToList(World world, int i, int j, int k, AxisAlignedBB axisalignedbb, List arraylist, Entity entity) {
        int l = world.getBlockMetadata(i, j, k);
        int i1 = l & 0x3;
        float f = (float) (0.5D - this.boundingWidth / 2.0D);
        float f1 = (float) (0.5D + this.boundingWidth / 2.0D);
        float f2 = 1.0F;
        if ((l & 0xC) == 8) {
            float f3 = (float) (0.5D - this.boundingHeight / 2.0D);
            float f4 = (float) (0.5D + this.boundingHeight / 2.0D);
            boolean flag = isTensile(world, i, j - 1, k);
            if (i1 == 0) {
                setBlockBounds(flag ? f3 : 0.0F, 0.0F, f, 0.5F, 0.5F, f1);
                super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, entity);
                setBlockBounds(0.5F, 0.0F, f, 1.0F, f2, f1);
                super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, entity);
            } else if (i1 == 1) {
                setBlockBounds(f, 0.0F, flag ? f3 : 0.0F, f1, 0.5F, 0.5F);
                super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, entity);
                setBlockBounds(f, 0.0F, 0.5F, f1, f2, 1.0F);
                super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, entity);
            } else if (i1 == 2) {
                setBlockBounds(0.0F, 0.0F, f, 0.5F, f2, f1);
                super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, entity);
                setBlockBounds(0.5F, 0.0F, f, flag ? f4 : 1.0F, 0.5F, f1);
                super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, entity);
            } else if (i1 == 3) {
                setBlockBounds(f, 0.0F, 0.0F, f1, f2, 0.5F);
                super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, entity);
                setBlockBounds(f, 0.0F, 0.5F, f1, 0.5F, flag ? f4 : 1.0F);
                super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, entity);
            }
            setBlockBoundsBasedOnState(world, i, j, k);
        } else {
            setBlockBoundsBasedOnState(world, i, j, k);
            super.addCollisionBoxesToList(world, i, j, k, axisalignedbb, arraylist, entity);
        }
    }

    @Override
    public MovingObjectPosition collisionRayTrace(World world, int i, int j, int k, Vec3 vec3d, Vec3 vec3d1) {
        MovingObjectPosition movingobjectposition = super.collisionRayTrace(world, i, j, k, vec3d, vec3d1);
        if (movingobjectposition != null) {
            movingobjectposition.hitVec = new MyVec3D(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
        }
        return movingobjectposition;
    }

    public void updateMetadata(World world, int i, int j, int k) {
        int l = world.getBlockMetadata(i, j, k);
        int i1 = l;
        if (isTensile(world, i, j + 1, k)) {
            i1 = 0xC | l & 0x3;
        } else {
            int j1 = l & 0x3;
            boolean flag = (j1 & 0x1) == 0;
            int k1 = flag ? 1 : 0;
            int l1 = flag ? 0 : 1;
            boolean flag1 = isTensile(world, i + k1, j + 1, k + l1);
            boolean flag2 = isTensile(world, i - k1, j + 1, k - l1);
            boolean flag3 = isTensile(world, i + k1, j, k + l1);
            boolean flag4 = isTensile(world, i - k1, j, k - l1);
            if ((flag1) || (flag2)) {
                if (((flag1) && (flag4)) || ((flag2) && (flag3)) || ((flag1) && (flag2))) {
                    i1 |= 6;
                    i1 &= -9;
                } else {
                    i1 |= 8;
                    i1 &= -5;
                    if (flag2) {
                        i1 |= 2;
                    } else
                        i1 &= -3;
                }
            } else {
                int i2 = getTensileMetadata(world, i + k1, j, k + l1, isTensile(world, i + k1, j + 1, k + l1) ? 6 : 0);
                int j2 = getTensileMetadata(world, i - k1, j, k - l1, isTensile(world, i - k1, j + 1, k - l1) ? 6 : 0);
                int k2 = Math.max(i2 & 0x6, j2 & 0x6) >> 1;
                int l2 = Math.max(k2 - 1, 0);
                i1 = i1 & 0xFFFFFFF9 | l2 << 1;
                i1 &= -9;
            }
        }
        if (i1 == l) {
            return;
        }

        world.setBlockMetadataWithNotify(i, j, k, i1, 2);
        onBlockRemoval(world, i, j, k);
        world.markBlockRangeForRenderUpdate(i, j, k, i, j, k);
        world.scheduleBlockUpdate(i, j, k, this, tickRate(world));
    }

    public boolean tryToLift(World world, int i, int j, int k) {
        int l = world.getBlockMetadata(i, j, k);
        boolean flag = (l & 0x1) == 0;
        int i1 = flag ? 1 : 0;
        int j1 = flag ? 0 : 1;
        int k1 = getTensileMetadata(world, i - i1, j, k - j1, isTensile(world, i - i1, j + 1, k - j1) ? 6 : 0);
        int l1 = getTensileMetadata(world, i + i1, j, k + j1, isTensile(world, i + i1, j + 1, k + j1) ? 6 : 0);
        if (((k1 & 0x8) == 8 || (k1 & 0xE) == 6) && ((l1 & 0x8) == 8 || (l1 & 0xE) == 6) && (!world.isAirBlock(i - i1, j, k - j1) || !world.isAirBlock(i + i1, j, k + j1)) && (isTensile(world, i - i1, j + 1, k - j1) || isTensile(world, i + i1, j + 1, k + j1))) {
            if (!world.isAirBlock(i, j + 1, k)) {
                world.setBlockToAir(i, j, k);
                dropBlockAsItem(world, i, j, k, l, 1);
                return false;
            }

            world.setBlockToAir(i, j, k);
            world.setBlock(i, j + 1, k, this);
            int j2 = flag ? 0 : 1;
            world.setBlockMetadataWithNotify(i, j + 1, k, j2, 2);
            world.scheduleBlockUpdate(i, j + 1, k, this, tickRate(world));
            return false;
        }

        return true;
    }

    public boolean tryToFall(World world, int i, int j, int k) {
        if (isTensile(world, i, j + 1, k)) {
            return true;
        }
        if (j <= 0 || !canFall(world, i, j, k)) {
            return true;
        }
        int l = world.getBlockMetadata(i, j, k);
        boolean flag = (l & 0x1) == 0;
        int i1 = flag ? 1 : 0;
        int j1 = flag ? 0 : 1;
        byte byte0 = 0;
        if (isTensile(world, i + i1, j + 1, k + j1)) {
            if (((!world.isAirBlock(i + i1, j, k + j1)) && (!isTensile(world, i + i1, j, k + j1))) || (isTensile(world, i - i1, j + 1, k - j1)) || (isTensile(world, i - i1, j, k - j1)) || (isTensile(world, i - i1, j - 1, k - j1))) {
                return true;
            }
            byte0 = 1;
        }
        if (isTensile(world, i - i1, j + 1, k - j1)) {
            if (((!world.isAirBlock(i - i1, j, k - j1)) && (!isTensile(world, i - i1, j, k - j1))) || (isTensile(world, i + i1, j + 1, k + j1)) || (isTensile(world, i + i1, j, k + j1)) || (isTensile(world, i + i1, j - 1, k + j1))) {
                return true;
            }
            byte0 = -1;
        }
        if (isTensile(world, i + i1, j, k + j1) && isTensile(world, i + i1 * 2, j + 1, k + j1 * 2) && (isTensile(world, i - i1, j - 1, k - j1))) {
            return true;
        }
        if (isTensile(world, i - i1, j, k - j1) && (isTensile(world, i - i1 * 2, j + 1, k - j1 * 2)) && (isTensile(world, i + i1, j - 1, k + j1))) {
            return true;
        }
        int k1 = getTensileMetadata(world, i + i1, j, k + j1, -1);
        int l1 = getTensileMetadata(world, i - i1, j, k - j1, -1);
        if ((k1 != -1) && (l1 != -1)) {
            if ((k1 & 0x6) != 0) {
                return true;
            }
            if ((l1 & 0x6) != 0) {
                return true;
            }
            if (!world.isAirBlock(i + i1, j - 1, k + j1) || !world.isAirBlock(i - i1, j - 1, k - j1)) {
                return true;
            }
        }
        int i2 = world.getBlockMetadata(i, j, k);
        if (byte0 != 0) {
            if (!world.isAirBlock(i + i1 * byte0, j, k + j1 * byte0)) {
                if (world.isAirBlock(i + i1 * byte0, j - 1, k + j1 * byte0)) {
                    world.setBlockToAir(i, j, k);
                    i += i1 * byte0;
                    k += j1 * byte0;
                    world.setBlock(i, j - 1, k, this);
                    world.setBlockMetadataWithNotify(i, j - 1, k, i2 | 0xC, 2);
                    updateMetadata(world, i, j - 1, k);
                    world.scheduleBlockUpdate(i, j - 1, k, this, tickRate(world));
                    return false;
                }
            } else {
                world.setBlockToAir(i, j, k);
                i += i1 * byte0;
                k += j1 * byte0;
                world.setBlock(i, j, k, this);
                world.setBlockMetadataWithNotify(i, j, k, i2 | 0xC, 2);
                updateMetadata(world, i, j, k);
                world.scheduleBlockUpdate(i, j, k, this, tickRate(world));
                return false;
            }
        }
        if (world.getBlock(i, j - 1, k).getMaterial() == Material.air) {
            world.setBlockToAir(i, j, k);
            world.setBlock(i, j - 1, k, this);
            int j2 = (i2 & 0x8) != 0 ? i2 : i2 | 0x6;
            world.setBlockMetadataWithNotify(i, j - 1, k, j2, 2);
            world.scheduleBlockUpdate(i, j - 1, k, this, tickRate(world));
            return false;
        }
        if (!isTensile(world, i, j - 1, k)) {
            world.setBlockToAir(i, j, k);
            dropBlockAsItem(world, i, j, k, i2, 0);
            return false;
        }

        world.setBlockToAir(i, j, k);
        world.getBlock(i, j - 1, k).dropBlockAsItem(world, i, j - 1, k, world.getBlockMetadata(i, j - 1, k), 0);
        world.setBlock(i, j - 1, k, this);
        int k2 = (i2 & 0xC) != 0 ? i2 : i2 | 0x6;
        world.setBlockMetadataWithNotify(i, j - 1, k, k2, 2);
        world.scheduleBlockUpdate(i, j - 1, k, this, tickRate(world));
        return false;
    }

    public static boolean canFall(World world, int i, int j, int k) {
        Material material = world.getBlock(i, j - 1, k).getMaterial();
        return (material == Material.air) || (isTensile(world, i, j - 1, k)) || (material == Material.water) || (material == Material.lava) || (material == Material.fire);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k) {
        int l = iblockaccess.getBlockMetadata(i, j, k);
        int i1 = l & 0x3;
        float f = (float) (0.5D - this.boundingWidth / 2.0D);
        float f1 = (float) (0.5D + this.boundingWidth / 2.0D);
        float f2 = (float) (0.5D - this.boundingHeight / 2.0D);
        float f3 = (float) (0.5D + this.boundingHeight / 2.0D);
        float f4 = 1.0F;
        if ((l & 0xC) == 12) {
            boolean flag = (l & 0x1) == 1;
            if (flag) {
                setBlockBounds(f, 0.0F, f2, f1, 1.0F, f3);
            } else {
                setBlockBounds(f2, 0.0F, f, f3, 1.0F, f1);
            }
        } else if ((l & 0xC) == 8) {
            boolean flag1 = isTensile(iblockaccess, i, j - 1, k);
            if (i1 == 0) {
                setBlockBounds(flag1 ? f2 : 0.0F, 0.0F, f, 1.0F, f4, f1);
            } else if (i1 == 1) {
                setBlockBounds(f, 0.0F, flag1 ? f2 : 0.0F, f1, f4, 1.0F);
            } else if (i1 == 2) {
                setBlockBounds(0.0F, 0.0F, f, flag1 ? f3 : 1.0F, f4, f1);
            } else if (i1 == 3) {
                setBlockBounds(f, 0.0F, 0.0F, f1, f4, flag1 ? f3 : 1.0F);
            }
        } else {
            int j1 = l & 0x1;
            int k1 = (l & 0x6) >> 1;
            float f5 = k1 != 3 ? 0.125F : k1 != 2 ? 0.0F : 0.5F;
            float f6 = k1 != 3 ? 0.5F : k1 != 2 ? 0.25F : k1 != 0 ? 0.5F : 1.0F;
            if (j1 == 0) {
                setBlockBounds(0.0F, f5, f, 1.0F, f6, f1);
            } else
                setBlockBounds(f, f5, 0.0F, f1, f6, 1.0F);
        }
    }

    @SideOnly(Side.CLIENT)
    public void drawTensile(int i, int j, int k, double d, double d1, double d2, double d3, double d4, double d5, IIcon l, int i1, int j1, int k1, double d6) {
        ModModel modmodel = new ModModel(i, j, k, l);
        double d7 = d6 * (i1 != 0 ? 0 : 1);
        double d8 = d6 * (i1 != 1 ? 0 : 1);
        double d9 = d6 * (i1 != 2 ? 0 : 1);
        double d10 = d6 * (j1 != 0 ? 0 : 1);
        double d11 = d6 * (j1 != 1 ? 0 : 1);
        double d12 = d6 * (j1 != 2 ? 0 : 1);
        double d13 = 0.0D;
        double d14 = 0.0D;
        if (d1 > 1.0D && d4 > 1.0D && ((k1 & 0x1) == 1)) {
            d13 = -0.5D;
        }
        modmodel.addVertexWithUV(d + d7, d1 + d8, d2 + d9, k1, d13, d14);
        modmodel.addVertexWithUV(d - d7, d1 - d8, d2 - d9, k1, d13, d14);
        modmodel.addVertexWithUV(d3 - d10, d4 - d11, d5 - d12, k1, d13, d14);
        modmodel.addVertexWithUV(d3 + d10, d4 + d11, d5 + d12, k1, d13, d14);
        modmodel.render(false);
        modmodel.render(true, true);
    }

    public static int getTensileMetadata(IBlockAccess iblockaccess, int i, int j, int k) {
        return getTensileMetadata(iblockaccess, i, j, k, -1);
    }

    public static int getTensileMetadata(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        if (!(iblockaccess.getBlock(i, j, k) instanceof ITensile)) {
            return l;
        }

        return iblockaccess.getBlockMetadata(i, j, k);
    }

    public static boolean isTensile(IBlockAccess iblockaccess, int i, int j, int k) {
        return iblockaccess.getBlock(i, j, k) instanceof ITensile;
    }

    @SideOnly(Side.CLIENT)
    public void render(int i, int j, int k, int l, IBlockAccess iblockaccess, RenderBlocks renderblocks) {
        double[] ad = heights;
        IIcon i1 = getIcon(0, i);
        if (renderblocks.overrideBlockTexture != null) {
            i1 = renderblocks.overrideBlockTexture;
        }
        boolean flag = (i & 0x1) == 0;
        if ((i & 0xC) == 12) {
            boolean flag1 = isTensile(iblockaccess, flag ? j + 1 : j, k - 1, flag ? l : l + 1);
            boolean flag2 = isTensile(iblockaccess, flag ? j - 1 : j, k - 1, flag ? l : l - 1);
            double d = flag2 ? 0.0D : flag1 ? 1.0D : 0.5D;
            byte byte0 = (byte) (flag ? 0 : 2);
            byte byte2 = (!flag1) && (!flag2) ? byte0 : 1;
            byte byte4 = (byte) (flag ? 2 : 0);
            double d1 = (!flag1) && (!flag2) ? 0.0D : ad[0];
            drawTensile(j, k, l, 0.5D, 1.0D, 0.5D, flag ? d : 0.5D, d1, flag ? 0.5D : d, i1, byte4, byte4, flag ? 17 : 1, this.pixelsWide / 32.0D);
            for (double crossPiece : this.crossPieces) {
                drawTensile(j, k, l, flag ? 0.5D : crossPiece, 1.0D, flag ? crossPiece : 0.5D, flag ? d : crossPiece, d1, flag ? crossPiece : d, i1, byte0, byte2, flag ? 1 : 17, this.pixelsHigh / 32.0D);
            }

            return;
        }
        int j1 = flag ? 1 : 0;
        int k1 = flag ? 0 : 1;
        int l1 = ((isTensile(iblockaccess, j + j1, k + 1, l + k1) ? 6 : getTensileMetadata(iblockaccess, j + j1, k, l + k1, 0)) & 0x6) >> 1;
        int i2 = ((isTensile(iblockaccess, j - j1, k + 1, l - k1) ? 6 : getTensileMetadata(iblockaccess, j - j1, k, l - k1, 0)) & 0x6) >> 1;
        byte byte1 = (byte) (flag ? 2 : 0);
        byte byte3 = 1;
        byte byte5 = 1;
        double d2 = 0.0D;
        double d3 = 1.0D;
        boolean flag3 = false;
        if ((i & 0x8) == 0) {
            if (l1 <= i2) {
                i2 = (i & 0x6) >> 1;
            }
            if (l1 >= i2) {
                l1 = (i & 0x6) >> 1;
            }
            if ((l1 == 3) && (i2 == 3)) {
                if (isTensile(iblockaccess, j + j1, k + 1, l + k1)) {
                    i2 = 2;
                } else
                    l1 = 2;
            }
        } else {
            byte byte6 = (byte) (l1 >= i2 ? -1 : 1);
            if ((isTensile(iblockaccess, j, k - 1, l)) && (!isTensile(iblockaccess, j + j1 * byte6, k - 1, l + k1 * byte6))) {
                ad = ad.clone();
                ad[0] = 0.0D;
                if (l1 < i2) {
                    l1 = 0;
                    d3 = 0.5D;
                    flag3 = true;
                    byte5 = (byte) (flag ? 0 : 2);
                }
                if (l1 > i2) {
                    d2 = 0.5D;
                    i2 = 0;
                    flag3 = true;
                    byte3 = (byte) (flag ? 0 : 2);
                }
            }
        }
        drawTensile(j, k, l, flag ? d2 : 0.5D, ad[i2], flag ? 0.5D : d2, flag ? d3 : 0.5D, ad[l1], flag ? 0.5D : d3, i1, byte1, byte1, flag ? 16 : flag3 ? 1 : flag ? 17 : 2, this.pixelsWide / 32.0D);
        for (double crossPiece : this.crossPieces) {
            drawTensile(j, k, l, flag ? d2 : crossPiece, ad[i2], flag ? crossPiece : d2, flag ? d3 : crossPiece, ad[l1], flag ? crossPiece : d3, i1, byte3, byte5, flag ? 8 : flag3 ? 17 : flag ? 1 : 10, this.pixelsHigh / 32.0D);
        }
    }

    @SideOnly(Side.CLIENT)
    public void modifyCamera(Entity entity, int j, int k, int l, float f) {
        World world = entity.worldObj;
        int i1 = world.getBlockMetadata(j, k, l);
        float f1 = 25.0F;
        swingDecay = 0.9F;
        if ((i1 & 0x8) == 0 || ((i1 & 0xC) == 8 && !isTensile(world, j, k - 1, l))) {
            if (entity.boundingBox.minY > k + 0.1D && world.getBlock(j, k - 1, l) == Blocks.air && !isTensile(world, j, k - 1, l)) {
                if (entity.onGround) {
                    f1 = 3.0F;
                    swingDecay = 0.94F;
                }
            } else {
                swingAmplitude = 0.0F;
            }
        } else {
            swingAmplitude = 0.0F;
        }
        boolean flag = (i1 & 0x1) == 0;
        float f2 = (float) Math.sin(System.currentTimeMillis() / 250.0D) * 11.0F * swingAmplitude;
        swingPosition += (f2 - swingPosition) / f1;
        double d = (entity.rotationYaw + (flag ? 0.0F : 90.0F)) / 180.0D * Math.PI;
        GL11.glRotatef(swingPosition, (float) Math.cos(d), -(float) Math.sin(d), (float) Math.sin(d));
    }
}