package zipline.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import zipline.EntityHandlebar;
import zipline.items.IHandlebar;
import zipline.mod_zipline;

import java.util.List;

public class BlockRope extends BlockTensile implements IZipline {
    public BlockRope(Material material) {
        super(material);
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k, Entity entity) {
        if (entity instanceof EntityItem) {
            return null;
        }
        int l = world.getBlockMetadata(i, j, k);
        if ((l & 0xC) == 12) {
            return null;
        }
        if (((l & 0xC) == 8) && (isTensile(world, i, j - 1, k))) {
            return null;
        }
        if ((entity instanceof IHandlebar)) {
            return null;
        }
        if ((entity != null) && (entity.ridingEntity instanceof IHandlebar)) {
            return null;
        }

        return getCollisionBoundingBoxFromPool(world, i, j, k);
    }

    @Override
    public void onEntityWalking(World world, int i, int j, int k, Entity entity) {
        handleCollision(world, i, j, k, entity, true);
    }

    @Override
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB mask, List list, Entity entity) {
        AxisAlignedBB axisalignedbb1 = this.getCollisionBoundingBoxFromPool(world, x, y, z, entity);

        if (axisalignedbb1 != null && mask.intersectsWith(axisalignedbb1)) {
            list.add(axisalignedbb1);
        }
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity) {
        handleCollision(world, i, j, k, entity, false);
    }

    public void handleCollision(World world, int i, int j, int k, Entity entity, boolean flag) {
        int l = world.getBlockMetadata(i, j, k);
        if (((l & 0x8) == 0) || (((l & 0xC) == 8) && (!isTensile(world, i, j - 1, k)))) {
            return;
        }
        if (flag) {
            return;
        }
        if (((l & 0xC) == 8) && (isTensile(world, i, j - 1, k))) {
            entity.fallDistance = 0.0F;
            if (entity.isSneaking()) {
                entity.motionY = 0.081D;
                entity.posY = entity.lastTickPosY;
            }
            switch (l & 0x3) {
                case 0:
                    if (entity.isSneaking()) {
                        entity.motionZ = ((k + 0.5D - entity.posZ) / 10.0D);
                    } else if (entity.boundingBox.maxX > i + 0.9D) {
                        entity.setPosition(i + 0.9D - entity.width / 2.0D, entity.boundingBox.minY <= j + 0.9D ? entity.posY : j + 1.5D + entity.height, entity.posZ);
                        entity.motionX = 0.0D;
                        entity.motionY = 0.15D;
                    }
                    break;
                case 1:
                    if (entity.isSneaking()) {
                        entity.motionX = ((i + 0.5D - entity.posX) / 10.0D);
                    } else if (entity.boundingBox.maxZ > k + 0.9D) {
                        entity.setPosition(entity.posX, entity.boundingBox.minY <= j + 0.9D ? entity.posY : j + 1.5D + entity.height, k + 0.9D - entity.width / 2.0D);
                        entity.motionZ = 0.0D;
                        entity.motionY = 0.15D;
                    }
                    break;
                case 2:
                    if (entity.isSneaking()) {
                        entity.motionZ = ((k + 0.5D - entity.posZ) / 10.0D);
                    } else if (entity.boundingBox.minX < i + 0.1D) {
                        entity.setPosition(i + 0.1D + entity.width / 2.0D, entity.boundingBox.minY <= j + 0.9D ? entity.posY : j + 1.5D + entity.height, entity.posZ);
                        entity.motionX = 0.0D;
                        entity.motionY = 0.15D;
                    }
                    break;
                case 3:
                    if (entity.isSneaking()) {
                        entity.motionX = ((i + 0.5D - entity.posX) / 10.0D);
                    } else if (entity.boundingBox.minZ < k + 0.1D) {
                        entity.setPosition(entity.posX, entity.boundingBox.minY <= j + 0.9D ? entity.posY : j + 1.5D + entity.height, k + 0.1D + entity.width / 2.0D);
                        entity.motionZ = 0.0D;
                        entity.motionY = 0.15D;
                    }
                    break;
            }
        } else if ((l & 0xC) == 12) {
            double d = entity.posX - (i + 0.5F);
            double d1 = entity.posZ - (k + 0.5F);
            double d2 = Math.sqrt(d * d + d1 * d1);
            double d3 = (entity.rotationYaw - 90.0F) / 180.0F * 3.141593F;
            double d4 = Math.atan2(d1, d);
            boolean flag1 = (d3 - d4 + 7.068584203720093D) % 6.283185958862305D < 1.570796489715576D;
            boolean flag2 = (flag1) && (d2 < 0.5D) && ((Math.abs(entity.motionX) > 0.01D) || (Math.abs(entity.motionZ) > 0.01D));
            if ((entity.isSneaking()) || (flag1)) {
                entity.fallDistance = 0.0F;
                if (entity.motionY < -0.15D) {
                    entity.motionY = -0.15D;
                }
            }
            if ((entity.isSneaking()) || (flag2)) {
                if (entity.isSneaking()) {
                    entity.motionY = 0.081D;
                    entity.posY = entity.lastTickPosY;
                }
                double d5 = Math.min(Math.abs(d4 - d3), 1.0D) * (d4 <= d3 ? -1 : 1);
                if (Math.abs(d4 - d3) < 0.03D) {
                    d5 = 0.0D;
                }
                double d6 = (d3 - d4 <= 0.1D ? 0.0D : Math.cos(d4 - 0.1D) * 0.1000000014901161D) + (d3 - d4 <= 0.1D ? 0.0D : Math.cos(d4 - 0.1D) * 0.1000000014901161D);
                double d7 = (d4 - d3 <= 0.1D ? 0.0D : Math.sin(d4 + 0.1D) * 0.1000000014901161D) + (d3 - d4 <= 0.1D ? 0.0D : Math.sin(d4 + 0.1D) * 0.1000000014901161D);
                if ((d2 >= 0.21D) || (!flag2)) {
                    d5 = 0.0D;
                }
                entity.setPosition(i + 0.5F + Math.cos(d4 - d5) * Math.max(d2, 0.21D), entity.posY, k + 0.5F + Math.sin(d4 - d5) * Math.max(d2, 0.21D));
                if ((d2 < 0.21D) && (flag2)) {
                    entity.motionX *= 0.75D;
                    entity.motionY = 0.2D;
                    entity.motionZ *= 0.75D;
                    Float float1 = (float) (d4 - d5) * 180.0F / 3.141593F + 90.0F;
                    Float float2 = (float1 - entity.rotationYaw + 720.0F + 720.0F) % 360.0F - 180.0F;
                    if (Math.abs(float2.floatValue()) > 160.0F) {
                        float1 = entity.rotationYaw;
                    }
                    entity.rotationYaw = float1;
                }
            }
        }
        if (((entity instanceof EntityPlayer)) && (entity.lastTickPosY % 1.0D < 0.5D) && (entity.posY % 1.0D > 0.5D)) {
            world.playSoundAtEntity(entity, this.stepSound.getBreakSound(), this.stepSound.getVolume() * 0.5F, this.stepSound.getPitch() * 0.75F);
        }
    }

    public boolean mountZipline(ItemStack itemstack, EntityLivingBase entityliving, World world, int i, int j, int k, int l) {
        if (world.getBlock(i, j - 1, k).getMaterial() != Material.air || world.getBlock(i, j - 2, k).getMaterial() != Material.air) {
            return false;
        }
        int i1 = world.getBlockMetadata(i, j, k);
        if ((i1 & 0xC) == 12) {
            return false;
        }
        EntityHandlebar entityhandlebar = new EntityHandlebar(world, i + 0.5D, j + 0.5D, k + 0.5D);
        entityhandlebar.setPosition(i + 0.5D, j + 0.5D - entityhandlebar.height, k + 0.5D);
        double d = i - entityliving.posX;
        double d1 = j - entityliving.boundingBox.minY - 0.5D;
        double d2 = k - entityliving.posZ;
        entityhandlebar.motionX = entityliving.motionX;
        entityhandlebar.motionZ = entityliving.motionZ;
        double d3 = MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2);
        if (d3 < 4.0D) {
            world.spawnEntityInWorld(entityhandlebar);
            entityliving.mountEntity(entityhandlebar);
            itemstack.stackSize -= 1;
            if (entityliving instanceof EntityPlayer) {
                ((EntityPlayer) entityliving).triggerAchievement(mod_zipline.achievementRideZipline);
            }
            return true;
        }

        return false;
    }

    public double getZiplineFriction(Entity entity, World world, int i, int j, int k, int l) {
        double d = entity.motionX * entity.motionX + entity.motionZ * entity.motionZ;
        if (d == 0.0D) {
            return 1.0D;
        }

        return 1.0D - 0.015D * Math.min(1.0D, 1.0D / d);
    }

    public Vec3 getZiplineMotion(Entity entity, World world, int i, int j, int k, int l) {
        IHandlebar ihandlebar = (IHandlebar) entity;
        if ((l & 0xC) == 12 || ((l & 0xC) == 8 && isTensile(world, i, j - 1, k))) {
            if (ihandlebar != null) {
                ihandlebar.dismount();
            }
            return null;
        }
        boolean flag = (l & 0x1) == 0;
        int i1 = flag ? 1 : 0;
        int j1 = flag ? 0 : 1;
        int k1 = (getTensileMetadata(world, i + i1, j, k + j1, isTensile(world, i + i1, j + 1, k + j1) ? 6 : 0) & 0x6) >> 1;
        int l1 = (getTensileMetadata(world, i - i1, j, k - j1, isTensile(world, i - i1, j + 1, k - j1) ? 6 : 0) & 0x6) >> 1;
        if ((l & 0x8) == 0) {
            if (k1 <= l1) {
                l1 = (l & 0x6) >> 1;
            }
            if (k1 >= l1) {
                k1 = (l & 0x6) >> 1;
            }
        }
        Vec3 vec3d;
        double d = heights[k1] - heights[l1];
        if (d == 0.0D) {
            vec3d = Vec3.createVectorHelper(0.0D, 0.0D, 0.0D);
        } else {
            double d1 = Math.min(heights[k1], heights[l1]);
            double d2 = 0.05D * Math.max(0.0D, entity.posY + entity.height - j - d1) * (d <= 0.0D ? -1 : 1);
            vec3d = Vec3.createVectorHelper(-i1 * d2, 0.0D, -j1 * d2);
        }
        return vec3d;
    }

    public void conformToZipline(Entity entity, World world, int i, int j, int k, int l) {
        if ((l & 0xC) == 12 || ((l & 0xC) == 8 && isTensile(world, i, j - 1, k))) {
            return;
        }
        boolean flag = (l & 0x1) == 0;
        int i1 = flag ? 1 : 0;
        int j1 = flag ? 0 : 1;
        int k1 = (getTensileMetadata(world, i + i1, j, k + j1, isTensile(world, i + i1, j + 1, k + j1) ? 6 : 0) & 0x6) >> 1;
        int l1 = (getTensileMetadata(world, i - i1, j, k - j1, isTensile(world, i - i1, j + 1, k - j1) ? 6 : 0) & 0x6) >> 1;
        if ((l & 0x8) == 0) {
            if (k1 <= l1) {
                l1 = (l & 0x6) >> 1;
            }
            if (k1 >= l1) {
                k1 = (l & 0x6) >> 1;
            }
        }
        double d = flag ? entity.posX : entity.posZ;
        double d1 = heights[k1] - heights[l1];
        double d2 = heights[l1] + j - (flag ? i : k) * d1;
        entity.posY = (d * d1 + d2 - entity.height);
        if (flag) {
            entity.posZ = (k + 0.5D);
            entity.motionZ = 0.0D;
        } else {
            entity.posX = (i + 0.5D);
            entity.motionX = 0.0D;
        }
    }

    public Block.SoundType getStepSound(Entity entity, World world, int i, int j, int k, int l) {
        return this.stepSound;
    }
}