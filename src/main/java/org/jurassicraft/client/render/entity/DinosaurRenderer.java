package org.jurassicraft.client.render.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.client.render.entity.dinosaur.DinosaurRenderInfo;
import org.jurassicraft.client.render.entity.dinosaur.layers.*;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.DinosaurEntity;
import org.jurassicraft.server.entity.GrowthStage;

import java.awt.*;
import java.util.Random;

@SideOnly(Side.CLIENT)
public class DinosaurRenderer extends RenderLiving<DinosaurEntity>
{
    public Dinosaur dinosaur;
    public DinosaurRenderInfo renderInfo;

    public Random random;

    public DinosaurRenderer(DinosaurRenderInfo renderInfo, RenderManager renderManager)
    {
        super(renderManager, renderInfo.getModel(GrowthStage.INFANT), renderInfo.getShadowSize());

        this.dinosaur = renderInfo.getDinosaur();
        this.random = new Random();
        this.renderInfo = renderInfo;

        if (dinosaur.getOverlayTypes() != null)
            for (int i = 0; i < dinosaur.getOverlayTypes().length; i++)
            {
                if (dinosaur.getOverlayTypes()[i].equalsIgnoreCase("mouth"))
                    this.addLayer(new LayerMouth(this));
                if (dinosaur.getOverlayTypes()[i].equalsIgnoreCase("teeth"))
                    this.addLayer(new LayerTeeth(this));
                if (dinosaur.getOverlayTypes()[i].equalsIgnoreCase("nostrils"))
                    this.addLayer(new LayerNostrils(this));
                if (dinosaur.getOverlayTypes()[i].equalsIgnoreCase("eyes"))
                    this.addLayer(new LayerEyes(this));
                if (dinosaur.getOverlayTypes()[i].equalsIgnoreCase("eyelids"))
                    this.addLayer(new LayerEyelids(this));
                if (dinosaur.getOverlayTypes()[i].equalsIgnoreCase("claws"))
                    this.addLayer(new LayerClaws(this));
                if (dinosaur.getOverlayTypes()[i].equalsIgnoreCase("stripes"))
                    this.addLayer(new LayerStripes(this));
            }
    }


    @Override
    public void preRenderCallback(DinosaurEntity entity, float partialTick)
    {
        float scaleModifier = entity.getAttributes().getScaleModifier();
        float scale = (float) entity.interpolate(this.dinosaur.getScaleInfant(), this.dinosaur.getScaleAdult()) * scaleModifier;

        this.shadowSize = scale * this.renderInfo.getShadowSize();

        GlStateManager.translate(this.dinosaur.getOffsetX() * scale, this.dinosaur.getOffsetY() * scale, this.dinosaur.getOffsetZ() * scale);

        String name = entity.getCustomNameTag();

        switch (name)
        {
            case "Kashmoney360":
            case "JTGhawk137":
                GlStateManager.scale(0.1F, scale, scale);
                break;
            case "Gegy":
                GlStateManager.scale(scale, 0.01F, scale);
                break;
            case "Notch":
                GlStateManager.scale(scale * 2, scale * 2, scale * 2);
                break;
            case "jglrxavpok":
                GlStateManager.scale(scale, scale, scale * -1);
                break;
            case "Wyn":
                int color = Color.HSBtoRGB((entity.world.getTotalWorldTime() % 1000) / 100f, 1f, 1f);
                GlStateManager.color((color & 0xFF) / 255f, ((color >> 8) & 0xFF) / 255f, ((color >> 16) & 0xFF) / 255f);
            default:
                GlStateManager.scale(scale, scale, scale);
                break;
        }
    }

    @Override
    public void doRender(DinosaurEntity entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        this.mainModel = this.renderInfo.getModel(entity.getGrowthStage());
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    public ResourceLocation getEntityTexture(DinosaurEntity entity)
    {
        GrowthStage growthStage = entity.getGrowthStage();
        if (!this.dinosaur.doesSupportGrowthStage(growthStage))
        {
            growthStage = GrowthStage.ADULT;
        }
        return entity.isMale() ? this.dinosaur.getMaleTexture(growthStage) : this.dinosaur.getFemaleTexture(growthStage);
    }

    @Override
    protected void applyRotations(DinosaurEntity entity, float p_77043_2_, float p_77043_3_, float partialTicks)
    {
        GlStateManager.rotate(180.0F - p_77043_3_, 0.0F, 1.0F, 0.0F);
    }
}
