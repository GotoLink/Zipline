package zipline;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.HashMap;

public class ModCamera extends ModHooks {
    public static void registerModifier(ICameraMod icameramod, Block i) {
        registerModifier("camera", i, icameramod);
    }

    public static void modifyCamera(EntityPlayer entityplayer, float f) {
        HashMap hashmap = (HashMap) modList.get("camera");
        if (hashmap == null) {
            return;
        }
        World world = entityplayer.worldObj;
        int i = MathHelper.floor_double(entityplayer.posX);
        int j = MathHelper.floor_double(entityplayer.boundingBox.minY);
        int k = j;
        int l = MathHelper.floor_double(entityplayer.posZ);
        Block i1 = world.getBlock(i, j, l);
        ICameraMod icameramod = (ICameraMod) hashmap.get(i1);
        if (icameramod == null) {
            i1 = world.getBlock(i, k = j + 1, l);
            icameramod = (ICameraMod) hashmap.get(i1);
            if (icameramod == null) {
                i1 = world.getBlock(i, k = j - 1, l);
                icameramod = (ICameraMod) hashmap.get(i1);
            }
        }
        if (icameramod != null) {
            icameramod.modifyCamera(entityplayer, i1, i, k, l, f);
        }
    }
}