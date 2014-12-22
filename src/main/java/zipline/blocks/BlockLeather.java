package zipline.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

import java.util.Random;

public class BlockLeather extends BlockTensile {
    public BlockLeather(Material material) {
        super(material);
        pixelsWide = 16.0D;
        boundingWidth = 1.0D;
        crossPieces = new double[0];
    }

    @Override
    public Item getItemDropped(int i, Random random, int j) {
        return Items.leather;
    }

    @Override
    public int quantityDropped(Random random) {
/* 27 */
        return 1;
    }

    @Override
    public int damageDropped(int i) {
/* 32 */
        return 0;
    }
}