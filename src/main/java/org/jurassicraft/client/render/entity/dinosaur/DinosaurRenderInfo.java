package org.jurassicraft.client.render.entity.dinosaur;

import net.ilexiconn.llibrary.client.model.tabula.TabulaModel;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.client.model.AnimatableModel;
import org.jurassicraft.client.model.animation.EntityAnimator;
import org.jurassicraft.client.render.entity.DinosaurRenderer;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.DinosaurEntity;
import org.jurassicraft.server.entity.GrowthStage;
import org.jurassicraft.server.tabula.TabulaModelHelper;

import java.util.Locale;

@SideOnly(Side.CLIENT)
public class DinosaurRenderInfo implements IRenderFactory<DinosaurEntity> {
    private static TabulaModel DEFAULT_EGG_MODEL;
    private static ResourceLocation DEFAULT_EGG_TEXTURE;

    static {
        try {
            DEFAULT_EGG_MODEL = new TabulaModel(TabulaModelHelper.loadTabulaModel("/assets/jurassicraft/models/entities/egg/tyrannosaurus"));
            DEFAULT_EGG_TEXTURE = new ResourceLocation(JurassiCraft.MODID, "textures/entities/egg/tyrannosaurus.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final Dinosaur dinosaur;
    private final EntityAnimator<?> animator;
    private final AnimatableModel modelAdult;
    private AnimatableModel modelInfant;
    private AnimatableModel modelJuvenile;
    private AnimatableModel modelAdolescent;
    private TabulaModel eggModel;
    private ResourceLocation eggTexture;
    private float shadowSize = 0.65F;

    public DinosaurRenderInfo(Dinosaur dinosaur, EntityAnimator<?> animator, float shadowSize) {
        this.dinosaur = dinosaur;
        this.animator = animator;
        this.shadowSize = shadowSize;

        this.modelAdult = this.loadModel(GrowthStage.ADULT);
        this.modelInfant = this.loadModel(GrowthStage.INFANT);
        this.modelJuvenile = this.loadModel(GrowthStage.JUVENILE);
        this.modelAdolescent = this.loadModel(GrowthStage.ADOLESCENT);

        try {
            String name = dinosaur.getName().toLowerCase(Locale.ENGLISH);
            this.eggModel = new TabulaModel(TabulaModelHelper.loadTabulaModel("/assets/jurassicraft/models/entities/egg/" + name));
            this.eggTexture = new ResourceLocation(JurassiCraft.MODID, "textures/entities/egg/" + name + ".png");
        } catch (Exception e) {
        }
    }

    public ModelBase getModel(GrowthStage stage) {
        switch (stage) {
            case INFANT:
                return this.modelInfant;
            case JUVENILE:
                return this.modelJuvenile;
            case ADOLESCENT:
                return this.modelAdolescent;
            default:
                return this.modelAdult;
        }
    }

    public ModelBase getEggModel() {
        return this.eggModel == null ? DEFAULT_EGG_MODEL : this.eggModel;
    }

    public ResourceLocation getEggTexture() {
        return this.eggTexture == null ? DEFAULT_EGG_TEXTURE : this.eggTexture;
    }

    public EntityAnimator<?> getModelAnimator() {
        return this.animator;
    }

    public float getShadowSize() {
        return this.shadowSize;
    }

    public AnimatableModel loadModel(GrowthStage stage) {
        if (!this.dinosaur.doesSupportGrowthStage(stage)) {
            return this.modelAdult;
        }
        return new AnimatableModel(this.dinosaur.getModelContainer(stage), this.getModelAnimator());
    }

    public Dinosaur getDinosaur() {
        return this.dinosaur;
    }

    @Override
    public Render<? super DinosaurEntity> createRenderFor(RenderManager manager) {
        return new DinosaurRenderer(this, manager);
    }
}
