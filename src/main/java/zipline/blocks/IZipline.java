package zipline.blocks;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public interface IZipline {
    public boolean mountZipline(ItemStack paramul, EntityLivingBase paramwd, World paramrv, int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    public double getZiplineFriction(Entity paramkj, World paramrv, int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    public Vec3 getZiplineMotion(Entity paramkj, World paramrv, int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    public void conformToZipline(Entity paramkj, World paramrv, int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    public Block.SoundType getStepSound(Entity paramkj, World paramrv, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
}