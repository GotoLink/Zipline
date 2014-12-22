package zipline;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import zipline.blocks.BlockTensile;

/**
 * Created by Olivier on 10/12/2014.
 */
public class ClientProxy extends CommonProxy implements ICameraMod {
    @Override
    public void preInit() {
        RenderingRegistry.registerBlockHandler(RopeRenderer.INSTANCE);
        RenderingRegistry.registerEntityRenderingHandler(EntityHandlebar.class, RenderHandlebar.INSTANCE);
        FMLCommonHandler.instance().bus().register(this);
        MinecraftForge.EVENT_BUS.register(this);
        ModCamera.registerModifier(this, mod_zipline.ropeID);
    }

    @Override
    public int getRopeRenderType() {
        return RopeRenderer.INSTANCE.getRenderId();
    }

    @Override
    public void modifyCamera(Entity entity, Block i, int j, int k, int l, float f) {
        ((BlockTensile) i).modifyCamera(entity, j, k, l, f);
    }

    @SubscribeEvent
    public void onTickInGame(TickEvent.ClientTickEvent event) {
        BlockTensile.swingAmplitude *= BlockTensile.swingDecay;
        EntityPlayer thePlayer = FMLClientHandler.instance().getClientPlayerEntity();
        if (thePlayer != null && thePlayer.onGround) {
            BlockTensile.swingAmplitude += thePlayer.distanceWalkedModified - thePlayer.prevDistanceWalkedModified;
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        ModCamera.modifyCamera(FMLClientHandler.instance().getClientPlayerEntity(), event.partialTicks);
    }
}
