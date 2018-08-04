package org.jurassicraft.client.model.animation.entity.vehicle;

import com.google.common.collect.Lists;
import net.ilexiconn.llibrary.client.model.tabula.ITabulaModelAnimator;
import net.ilexiconn.llibrary.client.model.tabula.TabulaModel;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import org.jurassicraft.server.entity.ai.util.InterpValue;
import org.jurassicraft.server.entity.vehicle.CarEntity;
import org.jurassicraft.server.entity.vehicle.HelicopterEntity;

import java.util.List;

public class CarAnimator implements ITabulaModelAnimator<CarEntity> {
    private final List<CarAnimator.Door> doorList = Lists.newArrayList();
    public float partialTicks;

    public CarAnimator addDoor(Door door) {
	this.doorList.add(door);
	return this;
    }
    
    @Override
    public void setRotationAngles(TabulaModel model, CarEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float rotationYaw, float rotationPitch, float scale) {
        if(!(entity instanceof HelicopterEntity)) {
            doorList.forEach(door -> {
                InterpValue value = door.getInterpValue(entity);
                CarEntity.Seat seat = door.getSeat(entity);
                CarEntity.Seat closestSeat = seat;
                EntityPlayer player = Minecraft.getMinecraft().player;
                Vec3d playerPos = player.getPositionVector();
                for (Door door1 : this.doorList) {
                    if (door1.getSeat(entity).getPos().distanceTo(playerPos) <= closestSeat.getPos().distanceTo(playerPos)) {
                        closestSeat = door1.getSeat(entity);
                    }
                }
                value.setTarget(Math.toRadians(entity.getPassengers().contains(player) || seat.getOccupant() != null || closestSeat != seat || closestSeat.getPos().distanceTo(playerPos) > 4D ? 0F : door.isLeft() ? 60F : -60F));
                model.getCube(door.getName()).rotateAngleY = (float) value.getValueForRendering(partialTicks);
            });

            AdvancedModelRenderer wheelHolderFront = model.getCube("wheel holder front");
            AdvancedModelRenderer wheelHolderBack = model.getCube("wheel holder back");

            float wheelRotation = entity.prevWheelRotateAmount + (entity.wheelRotateAmount - entity.prevWheelRotateAmount) * partialTicks;
            float wheelRotationAmount = entity.wheelRotation - entity.wheelRotateAmount * (1.0F - partialTicks);

            if (entity.backward()) {
                wheelRotationAmount = -wheelRotationAmount;
            }

            wheelHolderFront.rotateAngleX = wheelRotationAmount * 0.5F;
            wheelHolderBack.rotateAngleX = wheelRotationAmount * 0.5F;

            entity.steerAmount.setTarget(Math.toRadians(entity.left() ? 40.0F : entity.right() ? -40.0F : 0.0F) * wheelRotation);

            float steerAmount = (float) entity.steerAmount.getValueForRendering(partialTicks);

            model.getCube("steering wheel main").rotateAngleZ = steerAmount;
            wheelHolderFront.rotateAngleY = -steerAmount * 0.15F;
        }
	
    }
    
    public static class Door {
		private final String name;
		private final int seatIndex;
		private final boolean isLeft;

		public Door(String name, int seatIndex, boolean isLeft) {
			this.name = name;
			this.seatIndex = seatIndex;
			this.isLeft = isLeft;
		}

		public InterpValue getInterpValue(CarEntity entity) {
			return getSeat(entity).getInterpValue();
		}

		public String getName() {
			return name;
		}

		public CarEntity.Seat getSeat(CarEntity entity) {
			return entity.getSeat(seatIndex);
		}

		public boolean isLeft() {
			return isLeft;
		}

		}
}
