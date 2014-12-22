package zipline;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;

public interface ICameraMod {
    public void modifyCamera(Entity entity, Block block, int x, int y, int z, float renderTick);
}