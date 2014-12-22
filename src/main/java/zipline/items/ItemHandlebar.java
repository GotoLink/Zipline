package zipline.items;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import zipline.blocks.IZipline;

public class ItemHandlebar extends Item {
    public ItemHandlebar() {
        super();
        this.maxStackSize = 1;
    }

    @Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l, float vecX, float vecY, float vecZ) {
        Block block = world.getBlock(i, j, k);
        entityplayer.swingItem();
        return block instanceof IZipline && ((IZipline) block).mountZipline(itemstack, entityplayer, world, i, j, k, l);
    }
}