package zipline;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import zipline.blocks.IZipline;
import zipline.items.IHandlebar;

import java.util.List;

public class EntityHandlebar extends Entity implements IHandlebar {
    private int lastSoundPosX2, lastSoundPosZ2;

    public EntityHandlebar(World world) {
        super(world);
        setSize(0.6F, 1.8F);
    }

    public EntityHandlebar(World world, double d, double d1, double d2) {
        this(world);
        setPosition(d, d1, d2);
    }

    @Override
    public double getMountedYOffset() {
        return 0.0D;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox() {
        return this.riddenByEntity == null ? this.boundingBox : this.riddenByEntity.getBoundingBox();
    }

    @Override
    public boolean interactFirst(EntityPlayer entityplayer) {
        dismount();
        return true;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.riddenByEntity == null || this.riddenByEntity.isDead) {
            dismount();
        }
        int i = (int) Math.floor(this.posX);
        int j = (int) Math.floor(this.posY + this.height);
        int k = (int) Math.floor(this.posZ);
        Block block = this.worldObj.getBlock(i, j, k);
        if (!(block instanceof IZipline)) {
            j--;
            block = this.worldObj.getBlock(i, j, k);
        }
        if (!(block instanceof IZipline)) {
            j += 2;
            block = this.worldObj.getBlock(i, j, k);
        }
        if (!(block instanceof IZipline)) {
            dismount();
            return;
        }
        IZipline izipline = (IZipline) block;
        int l = this.worldObj.getBlockMetadata(i, j, k);
        double d = izipline.getZiplineFriction(this, this.worldObj, i, j, k, l);
        this.motionX *= d;
        this.motionY *= d;
        this.motionZ *= d;
        Vec3 vec3d = izipline.getZiplineMotion(this, this.worldObj, i, j, k, l);
        if (vec3d != null) {
            int i1 = (int) Math.floor(this.posX + this.motionX + vec3d.xCoord);
            int j1 = (int) Math.floor(this.posY + this.height + this.motionY + vec3d.yCoord);
            int l1 = (int) Math.floor(this.posZ + this.motionZ + vec3d.zCoord);
            if ((!this.worldObj.isAirBlock(i1, j1, l1) && !(this.worldObj.getBlock(i1, j1, l1) instanceof IZipline)) || (!this.worldObj.isAirBlock(i1, j1 - 1, l1) && !(this.worldObj.getBlock(i1, j1 - 1, l1) instanceof IZipline)) || (!this.worldObj.isAirBlock(i1, j1 - 2, l1) && !(this.worldObj.getBlock(i1, j1 - 2, l1) instanceof IZipline))) {
                this.motionX = (this.motionY = this.motionZ = 0.0D);
                this.prevPosX = (this.lastTickPosX = this.posX);
                this.prevPosY = (this.lastTickPosY = this.posY);
                this.prevPosZ = (this.lastTickPosZ = this.posZ);
            } else {
                if (this.riddenByEntity == null || !this.riddenByEntity.isSneaking()) {
                    addVelocity(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord);
                } else {
                    this.motionX *= 0.9D;
                    this.motionZ *= 0.9D;
                    int i2 = (int) (this.posX * 2.0D);
                    int j2 = (int) (this.posZ * 2.0D);
                    if (i2 != this.lastSoundPosX2 || j2 != this.lastSoundPosZ2) {
                        this.lastSoundPosX2 = i2;
                        this.lastSoundPosZ2 = j2;
                        Block.SoundType stepsound = izipline.getStepSound(this, this.worldObj, i, j, k, l);
                        this.worldObj.playSoundAtEntity(this, stepsound.getBreakSound(), stepsound.getVolume() * 0.5F, stepsound.getPitch() * 0.75F);
                    }
                }
                if (this.riddenByEntity != null) {
                    addVelocity(this.riddenByEntity.motionX, 0.0D, this.riddenByEntity.motionZ);
                }
                List list1 = this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ));
                if (list1 == null || list1.size() == 0) {
                    setPosition(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
                }
            }
        }
        if (this.riddenByEntity != null) {
            List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox);
            for (Object aList : list) {
                Entity entity = (Entity) aList;
                if (entity != this.riddenByEntity && entity.canBePushed()) {
                    entity.applyEntityCollision(this);
                }
            }
        }

        izipline.conformToZipline(this, this.worldObj, i, j, k, l);
    }

    public void dismount() {
        //Object obj = this.riddenByEntity != null && !this.riddenByEntity.isDead ? this.riddenByEntity : this;
        if (this.riddenByEntity != null) {
            Entity entity = this.riddenByEntity;
            double d = Math.floor(this.posX) + 0.5D;
            double d1 = this.posY + getMountedYOffset() + entity.getYOffset() + 0.5D;
            double d2 = Math.floor(this.posZ) + 0.5D;
            entity.mountEntity(null);
            entity.setPosition(d, d1, d2);
            entity.lastTickPosX = (entity.prevPosX = entity.posX);
            entity.lastTickPosY = (entity.prevPosY = entity.posY);
            entity.lastTickPosZ = (entity.prevPosZ = entity.posZ);
            entity.motionX = 0.0D;
            entity.motionY = 0.0D;
            entity.motionZ = 0.0D;
            entity.addVelocity(this.motionX, 0.0D, this.motionZ);
        }
        //EntityItem entityitem = new EntityItem(this.worldObj, ((Entity)obj).posX, ((Entity)obj).posY, ((Entity)obj).posZ, new ItemStack(zipline.mod_zipline.handlebarID));

        setDead();
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
    }

    @Override
    protected void entityInit() {
    }
}