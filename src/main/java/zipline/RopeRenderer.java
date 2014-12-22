package zipline;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import zipline.blocks.BlockTensile;

public class RopeRenderer implements ISimpleBlockRenderingHandler {
    public static final ISimpleBlockRenderingHandler INSTANCE = new RopeRenderer();
    private final int renderId;

    private RopeRenderer() {
        renderId = RenderingRegistry.getNextAvailableRenderId();
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {

    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int i, int j, int k, Block block, int modelId, RenderBlocks renderer) {
        Tessellator tessellator = Tessellator.instance;
        int f = block.getMixedBrightnessForBlock(world, i, j, k);
        tessellator.setBrightness(f);
        tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
        ((BlockTensile) block).render(world.getBlockMetadata(i, j, k), i, j, k, world, renderer);
        return true;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return false;
    }

    @Override
    public int getRenderId() {
        return renderId;
    }
}
