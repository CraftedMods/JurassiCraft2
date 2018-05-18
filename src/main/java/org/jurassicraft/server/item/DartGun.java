package org.jurassicraft.server.item;

import java.util.Collections;
import java.util.List;

import org.jurassicraft.server.entity.TranquilizerDartEntity;

import com.google.common.collect.Lists;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class DartGun extends Item {
    
    private static final int MAX_CARRY_SIZE = 12; //TODO config ?
    
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);

        if (!playerIn.capabilities.isCreativeMode) {
            itemstack.shrink(1);
        }

        SoundEvent event = null;
        
        ItemStack dartItem = getDartItem(itemstack);
        if(dartItem.isEmpty()) {
            List<Slot> list = Lists.newArrayList(playerIn.inventoryContainer.inventorySlots);
            Collections.reverse(list);
            if(setDartItem(itemstack, 
        	    list.stream()
        	    .map(Slot::getStack)
        	    .filter(stack -> stack.getItem() instanceof Dart)
        	    .findFirst()
        	    .orElse(ItemStack.EMPTY))) {
        	event = SoundEvents.ENTITY_ITEM_PICKUP;
            } else {
        	event = SoundEvents.BLOCK_COMPARATOR_CLICK;
            }
        } else if (!worldIn.isRemote) {
            TranquilizerDartEntity dart = new TranquilizerDartEntity(worldIn, playerIn, dartItem);
            dart.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 2.5F, 0.5F);
            worldIn.spawnEntity(dart);
            dartItem.shrink(1);
            setDartItem(itemstack, dartItem);
            event = SoundEvents.ENTITY_SNOWBALL_THROW;
        }
        
        if(event != null) {
            worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, event, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
        }


        playerIn.addStat(StatList.getObjectUseStats(this));
        return new ActionResult(EnumActionResult.SUCCESS, itemstack);
    }
    
    public static ItemStack getDartItem(ItemStack dartGun) {
	NBTTagCompound nbt = dartGun.getOrCreateSubCompound("dart_gun");
	ItemStack stack = new ItemStack(nbt.getCompoundTag("itemstack"));
	stack.setCount(Math.min(stack.getCount(), MAX_CARRY_SIZE));
	return stack;
    }
    
    public static boolean setDartItem(ItemStack dartGun, ItemStack dartItem) {
	boolean hadItem = !dartItem.isEmpty();
	ItemStack dartItem2 = dartItem.splitStack(MAX_CARRY_SIZE);
	dartGun.getOrCreateSubCompound("dart_gun").setTag("itemstack", dartItem2.serializeNBT());
	return hadItem;
    }
}