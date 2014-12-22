package zipline;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityArrow extends net.minecraft.entity.projectile.EntityArrow {
    public int ropeCount;
    private int ropeCountTemp = 0;
    public double startX;
    public double startY;
    public double startZ;

    public EntityArrow(World world) {
        super(world);
        setRopeCount(mod_zipline.ropeLength);
    }

    public EntityArrow(World world, double d, double d1, double d2) {
        this(world);
        this.startX = d;
        this.startY = d1;
        this.startZ = d2;
        setPosition(d, d1, d2);
        this.yOffset = 0.0F;
    }

    public EntityArrow(World world, EntityLivingBase entityliving, float f) {
        super(world, entityliving, f);
        this.startX = this.posX;
        this.startY = this.posY;
        this.startZ = this.posZ;
        setRopeCount(mod_zipline.ropeLength);
    }

    public ItemStack getItem() {
        return new ItemStack(mod_zipline.arrowRopeID, 1);
    }

    public void setRopeCount(int value) {
        ropeCount = ropeCountTemp = value;
    }

    @Override
    public void onUpdate() {
        Vec3 vec3d = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
        Vec3 vec3d1 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        MovingObjectPosition movingobjectposition = this.worldObj.func_147447_a(vec3d, vec3d1, false, true, false);
        if (movingobjectposition != null) {
            NBTTagCompound tag = new NBTTagCompound();
            this.writeToNBT(tag);
            if (tag.getByte("inGround") != 1) {
                hitGround((int) movingobjectposition.hitVec.xCoord, (int) movingobjectposition.hitVec.yCoord, (int) movingobjectposition.hitVec.zCoord, movingobjectposition);
            }
        }
        super.onUpdate();
    }

    public void hitGround(int i, int j, int k, MovingObjectPosition movingobjectposition) {
        if (movingobjectposition != null && movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            int l = movingobjectposition.sideHit;
            Block cr5 = mod_zipline.ropeID;
            if (l != 0 && l != 1 && this.worldObj.getBlock(i, j + 1, k + 1) != cr5 && this.worldObj.getBlock(i, j + 1, k - 1) != cr5 && this.worldObj.getBlock(i + 1, j + 1, k) != cr5 && this.worldObj.getBlock(i - 1, j + 1, k) != cr5 && isValidMaterial(this.worldObj.getBlock(movingobjectposition.blockX, movingobjectposition.blockY, movingobjectposition.blockZ).getMaterial())) {
                i = movingobjectposition.blockX;
                j = movingobjectposition.blockY;
                k = movingobjectposition.blockZ;
                worldObj.func_147480_a(i, j, k, true);
            } else {
                byte byte0 = l != 4 ? (byte) (l != 5 ? 0 : 1) : -1;
                byte byte1 = l != 0 ? (byte) (l != 1 ? 0 : 1) : -1;
                byte byte2 = l != 2 ? (byte) (l != 3 ? 0 : 1) : -1;
                i = movingobjectposition.blockX + byte0;
                j = movingobjectposition.blockY + byte1;
                k = movingobjectposition.blockZ + byte2;
            }
        }
        TensilePlacer tensileplacer;
        if (this.shootingEntity != null) {
            tensileplacer = new TensilePlacer(i, j, k, (int) this.shootingEntity.posX, (int) this.shootingEntity.boundingBox.minY, (int) this.shootingEntity.posZ);
        } else {
            tensileplacer = new TensilePlacer(i, j, k, (int) this.startX, (int) this.startY, (int) this.startZ);
        }

        this.ropeCount = tensileplacer.place(this.worldObj, mod_zipline.ropeID, ropeCountTemp);

        if (this.ropeCount > 0) {
            EntityItem entityitem = new EntityItem(this.worldObj, tensileplacer.currentX + 0.5F, tensileplacer.currentY + 0.5F, tensileplacer.currentZ + 0.5F, new ItemStack(mod_zipline.ropeID, this.ropeCount));
            this.worldObj.spawnEntityInWorld(entityitem);
        }
        EntityArrow entityarrow = new EntityArrow(this.worldObj, i + 0.5F, j + 0.5F, k + 0.5F);
        entityarrow.shootingEntity = this.shootingEntity;
        entityarrow.canBePickedUp = 1;
        entityarrow.setVelocity(this.motionX, this.motionY, this.motionZ);
        this.worldObj.spawnEntityInWorld(entityarrow);
        entityarrow.setDead();
    }

    private boolean isValidMaterial(Material material) {
        return (material == Material.grass || material == Material.ground || material == Material.leaves || material == Material.plants);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setInteger("RopeCount", this.ropeCount);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        this.ropeCount = nbttagcompound.getInteger("RopeCount");
    }
}