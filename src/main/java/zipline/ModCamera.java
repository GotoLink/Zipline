package zipline;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class ModCamera extends ModHooks<Block, ICameraMod> {
    public static final ModCamera INSTANCE = new ModCamera();

    private ModCamera() {
    }

    public void modifyCamera(EntityPlayer entityplayer, float f) {
        World world = entityplayer.worldObj;
        int i = MathHelper.floor_double(entityplayer.posX);
        int j = MathHelper.floor_double(entityplayer.boundingBox.minY);
        int k = j;
        int l = MathHelper.floor_double(entityplayer.posZ);
        Block i1 = world.getBlock(i, j, l);
        ICameraMod icameramod = getModifier(i1);
        if (icameramod == null) {
            i1 = world.getBlock(i, k = j + 1, l);
            icameramod = getModifier(i1);
            if (icameramod == null) {
                i1 = world.getBlock(i, k = j - 1, l);
                icameramod = getModifier(i1);
            }
        }
        if (icameramod != null) {
            icameramod.modifyCamera(entityplayer, i1, i, k, l, f);
        }
    }
}