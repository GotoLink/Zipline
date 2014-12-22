package zipline;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public final class RenderHandlebar extends Render {
    public static final Render INSTANCE = new RenderHandlebar();

    private RenderHandlebar() {
    }

    @Override
    public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return null;
    }
}