package zipline;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.material.Material;
import net.minecraft.dispenser.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import zipline.blocks.BlockLeather;
import zipline.blocks.BlockRope;
import zipline.blocks.BlockRopeBridge;
import zipline.blocks.BlockTensile;
import zipline.items.ItemArrowRope;
import zipline.items.ItemHandlebar;
import zipline.items.ItemRopeBlock;

@Mod(modid = "zipline", name = "Zipline", version = "${version}")
public final class mod_zipline{
    @SidedProxy(modId = "zipline", clientSide = "zipline.ClientProxy", serverSide = "zipline.CommonProxy")
    public static CommonProxy proxy;
    public static int ropeLength = 8;
    public static Achievement achievementMakeRope;
    public static Achievement achievementFireArrowRope;
    public static Achievement achievementBreakRopeBridge;
    public static Achievement achievementRideZipline;
    static Block ropeID;
    static BlockTensile ropeBridgeID;
    static BlockTensile clothSheetID;
    static BlockTensile leatherID;
    static Item arrowRopeID;
    static Item handlebarID;

    @Mod.EventHandler
    public void pre(FMLPreInitializationEvent event) {
        EntityRegistry.registerModEntity(EntityHandlebar.class, "RopeHandler", 0, this, 80, 3, true);

        ropeID = new BlockRope(Material.cloth).setBlockName("rope").setBlockTextureName("zipline:rope").setHardness(0.15F).setStepSound(Block.soundTypeCloth);
        GameRegistry.registerBlock(ropeID, ItemRopeBlock.class, "Rope");
        ropeBridgeID = (BlockTensile) new BlockRopeBridge(Material.wood).setBlockName("ropebridge").setBlockTextureName("zipline:ropebridge").setHardness(2.0F).setStepSound(Block.soundTypeWood);
        GameRegistry.registerBlock(ropeBridgeID, "RopeBridge");
        clothSheetID = (BlockTensile) new BlockTensile(Material.cloth).setWidthInfo(16.0D, 1.0D).setBlockName("clothsheet").setBlockTextureName("wool_colored_white").setHardness(0.1F).setStepSound(Block.soundTypeCloth);
        clothSheetID.crossPieces = new double[0];
        GameRegistry.registerBlock(clothSheetID, "ClothSheet");
        leatherID = (BlockTensile) new BlockLeather(Material.cloth).setBlockName("leather").setBlockTextureName("zipline:leather").setHardness(0.2F).setStepSound(Block.soundTypeCloth);
        GameRegistry.registerBlock(leatherID, "LeatherBlock");
        arrowRopeID = new ItemArrowRope().setTextureName("zipline:arrowrope").setUnlocalizedName("arrowrope");
        GameRegistry.registerItem(arrowRopeID, "ArrowRope");
        handlebarID = new ItemHandlebar().setTextureName("zipline:handlebar").setUnlocalizedName("handlebar");
        GameRegistry.registerItem(handlebarID, "HandleBar");
        Blocks.fire.setFireInfo(ropeID, 30, 20);
        Blocks.fire.setFireInfo(ropeBridgeID, 15, 20);
        Blocks.fire.setFireInfo(clothSheetID, 30, 60);
        proxy.preInit();
        MinecraftForge.EVENT_BUS.register(new ItemBow());
        FMLCommonHandler.instance().bus().register(this);
        int l = 8;
        int i1 = 1;
        achievementMakeRope = new Achievement("achievement.makeRope", "makeRope", l, i1, ropeID, AchievementList.buildWorkBench).registerStat();
        achievementFireArrowRope = new Achievement("achievement.fireArrowRope", "fireArrowRope", l, i1 + 2, arrowRopeID, achievementMakeRope).registerStat();
        achievementBreakRopeBridge = new Achievement("achievement.breakRopeBridge", "breakRopeBridge", l + 2, i1 + 2, ropeBridgeID, achievementFireArrowRope).registerStat();
        achievementRideZipline = new Achievement("achievement.rideZipline", "rideZipline", l, i1 + 4, handlebarID, achievementFireArrowRope).registerStat();
        BlockDispenser.dispenseBehaviorRegistry.putObject(arrowRopeID, new BehaviorDefaultDispenseItem() {
            @Override
            public ItemStack dispenseStack(IBlockSource blockSource, ItemStack itemStack)
            {
                World world = blockSource.getWorld();
                IPosition iposition = BlockDispenser.func_149939_a(blockSource);
                IProjectile iprojectile = ((ItemArrowRope) arrowRopeID).getEntityArrow(world, iposition.getX(), iposition.getY(), iposition.getZ(), itemStack);
                EnumFacing enumfacing = BlockDispenser.func_149937_b(blockSource.getBlockMetadata());
                iprojectile.setThrowableHeading((double)enumfacing.getFrontOffsetX(), (double)((float)enumfacing.getFrontOffsetY() + 0.1F), (double)enumfacing.getFrontOffsetZ(), 1.1F, 6.0F);
                world.spawnEntityInWorld((Entity)iprojectile);
                itemStack.splitStack(1);
                return itemStack;
            }
        });
    }

    @Mod.EventHandler
    public void addRecipes(FMLInitializationEvent event) {
        GameRegistry.addRecipe(new ItemStack(Items.string, 4), "!", '!', Blocks.wool);
        GameRegistry.addRecipe(new ItemStack(ropeID), "S", "S", 'S', Items.string);
        GameRegistry.addRecipe(new ItemStack(Items.string, 2), "!", '!', ropeID);
        GameRegistry.addRecipe(new ItemStack(ropeBridgeID, 3), "S#S", "S#S", "S#S", '#', Blocks.planks, 'S', ropeID);
        GameRegistry.addRecipe(new ItemStack(clothSheetID, 9), "SSS", 'S', Blocks.wool);
        GameRegistry.addRecipe(new ItemStack(Blocks.wool, 3), "SSS", "SSS", "SSS", 'S', clothSheetID);
        GameRegistry.addShapelessRecipe(new ItemStack(arrowRopeID), Items.arrow, ropeID, ropeID, ropeID, ropeID, ropeID, ropeID, ropeID, ropeID);

        for (int i = 0; i < 7; i++) {
            GameRegistry.addShapelessRecipe(new ItemStack(arrowRopeID, 1, 7 - i), new ItemStack(arrowRopeID, 1, (8 - i) % 8), ropeID, ropeID, ropeID, ropeID, ropeID, ropeID, ropeID, ropeID);
        }

        GameRegistry.addRecipe(new ItemStack(handlebarID), " ! ", "! !", '!', Items.stick);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void takenFromCrafting(PlayerEvent.ItemCraftedEvent event) {
        if (event.crafting.getItem() == Item.getItemFromBlock(ropeID)) {
            event.player.triggerAchievement(achievementMakeRope);
        }
    }
}