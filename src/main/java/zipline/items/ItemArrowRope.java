package zipline.items;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemArrowRope extends Item implements IItemArrow {
    public static int minDamage = 4;

    public ItemArrowRope() {
        super();
        setMaxDamage(8 + minDamage);
    }

    public static int damageToCount(int i) {
        if (i == 0) {
            i = 8;
        }
        return (9 - i) * 8;
    }

    public static int countToDamage(int i) {
        int j = 9 - i / 8;
        if (j == 8) {
            j = 0;
        }
        return j;
    }

    public EntityArrow getEntityArrow(World world, EntityLivingBase entityliving, ItemStack item, ItemStack itemstack) {
        zipline.EntityArrow entityarrow = new zipline.EntityArrow(world, entityliving, 0.0F);
        entityarrow.setRopeCount(damageToCount(itemstack.getItemDamage()));
        return entityarrow;
    }

    public EntityArrow getEntityArrow(World world, double d, double d1, double d2, ItemStack itemstack) {
        zipline.EntityArrow entityarrow = new zipline.EntityArrow(world, d, d1, d2);
        entityarrow.setRopeCount(damageToCount(itemstack.getItemDamage()));
        return entityarrow;
    }

    public static void playSound(World world, EntityPlayer entityplayer) {
        world.playSoundAtEntity(entityplayer, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 0.8F));
    }
}