package zipline.items;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IItemArrow {
    public EntityArrow getEntityArrow(World world, EntityLivingBase archer, ItemStack bow, ItemStack arrow);
}