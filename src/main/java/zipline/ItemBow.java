package zipline;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;

import java.util.Random;

public final class ItemBow {
    public static final Random random = new Random();

    @SubscribeEvent
    public void onBowLoose(ArrowLooseEvent event) {
        if (canFireRopeArrow(event.entityPlayer.inventory)) {
            float f = event.charge / 20.0F;
            f = (f * f + f * 2.0F) / 3.0F;
            if (f < 0.1D) {
                return;
            }
            if (f > 1.0F) {
                f = 1.0F;
            }
            EntityArrow entityarrow = new EntityArrow(event.entityPlayer.worldObj, event.entityPlayer, f * 2.0F);
            if (f == 1.0F) {
                entityarrow.setIsCritical(true);
            }
            event.entityPlayer.worldObj.playSoundAtEntity(event.entityPlayer, "random.bow", 1.0F, 1.0F / (random.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
            event.entityPlayer.inventory.consumeInventoryItem(mod_zipline.arrowRopeID);
            if (!event.entityPlayer.worldObj.isRemote) {
                event.entityPlayer.worldObj.spawnEntityInWorld(entityarrow);
            }
        }
    }

    @SubscribeEvent
    public void onBowStart(ArrowNockEvent event) {
        if (canFireRopeArrow(event.entityPlayer.inventory)) {
            event.entityPlayer.setItemInUse(event.result, event.result.getMaxItemUseDuration());
            event.setCanceled(true);
        }
    }

    private boolean canFireRopeArrow(InventoryPlayer inventory) {
        return !inventory.hasItem(Items.arrow) && inventory.hasItem(mod_zipline.arrowRopeID);
    }
}