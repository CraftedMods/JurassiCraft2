package org.jurassicraft.server.entity.ai.animations;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import org.jurassicraft.client.model.animation.EntityAnimation;
import org.jurassicraft.server.api.Animatable;
import org.jurassicraft.server.entity.DinosaurEntity;
import org.jurassicraft.server.entity.ai.Herd;
import org.jurassicraft.server.entity.ai.Mutex;

public class PeckGroundAnimationAI<T extends EntityLiving & Animatable> extends EntityAIBase {
    protected T animatable;

    public PeckGroundAnimationAI(T entity) {
        super();
        this.animatable = entity;
        this.setMutexBits(Mutex.ANIMATION);
    }

    @Override
    public boolean shouldExecute() {
        if (this.animatable instanceof DinosaurEntity) {
            DinosaurEntity dinosaur = (DinosaurEntity) this.animatable;
            if (!(dinosaur.herd != null && dinosaur.herd.state == Herd.State.MOVING)) {
                return false;
            }
        }
        return !(this.animatable.isDead || this.animatable.getAttackTarget() != null || this.animatable.isSleeping() || this.animatable.getAnimation() != EntityAnimation.IDLE.get()) && !this.animatable.isSwimming() && this.animatable.getRNG().nextDouble() < 0.01;
    }

    @Override
    public void startExecuting() {
        this.animatable.setAnimation(EntityAnimation.PECKING.get());
    }

    @Override
    public boolean continueExecuting() {
        return false;
    }
}
