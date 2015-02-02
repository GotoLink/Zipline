package zipline.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import zipline.mod_zipline;

public class BlockRopeBridge extends BlockTensile {
    public BlockRopeBridge(Material material) {
        super(material);
        setWidthInfo(16.0D, 1.0D);
        crossPieces = new double[]{0.1875D, 0.8125D};
    }

    @Override
    public void onEntityWalking(World world, int i, int j, int k, Entity l) {
        if (l instanceof EntityPlayer && world.isAirBlock(i, j - 1, k)) {
            double d = l.posX - i;
            double d1 = l.posY - j;
            double d2 = l.posZ - k;
            if (Math.sqrt(d * d + d2 * d2) <= 1.0D && d1 > 0.0D && d1 < 3.0D) {
                ((EntityPlayer) l).triggerAchievement(mod_zipline.achievementBreakRopeBridge);
            }
        }
    }

    @Override
    public boolean isLadder(IBlockAccess world, int x, int y, int z, EntityLivingBase entity){
        Block up = world.getBlock(x, y+1, z);
        if(up instanceof BlockAir){
            int meta = world.getBlockMetadata(x, y, z);
            return meta > 7 && meta < 12;
        }
        return up instanceof ITensile;
    }
}