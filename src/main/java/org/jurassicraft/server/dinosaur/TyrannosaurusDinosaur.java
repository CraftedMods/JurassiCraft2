package org.jurassicraft.server.dinosaur;

import java.util.ArrayList;

import org.jurassicraft.server.entity.Diet;
import org.jurassicraft.server.entity.dinosaur.TyrannosaurusEntity;
import org.jurassicraft.server.period.TimePeriod;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

public class TyrannosaurusDinosaur extends Dinosaur {
    public TyrannosaurusDinosaur() {
        super();

        this.setName("Tyrannosaurus");
        this.setDinosaurClass(TyrannosaurusEntity.class);
        this.setDinosaurType(DinosaurType.AGGRESSIVE);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0x4E502C, 0x353731);
        this.setEggColorFemale(0xBA997E, 0x7D5D48);
        this.setHealth(10, 80);
        this.setSpeed(0.35, 0.42);
        this.setAttackSpeed(1.1);
        this.setStrength(5, 20);
        this.setMaximumAge(this.fromDays(60));
        this.setEyeHeight(0.6F, 3.8F);
        this.setSizeX(0.45F, 3.0F);
        this.setSizeY(0.8F, 4.0F);
        this.setStorage(54);
        this.setDiet(Diet.CARNIVORE.get());
        this.setBones("arm_bones", "foot_bones", "leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder_bone", "skull", "tail_vertebrae", "tooth");
        this.setOverlayTypes("mouth", "teeth", "nostrils", "eyes", "eyelids", "claws", "stripes");
        this.setHeadCubeName("Head");
        this.setScale(2.4F, 0.35F);
        this.setMaxHerdSize(3);
        this.setAttackBias(1000.0);
        this.setBreeding(false, 2, 4, 60, false, true);

        String[][] recipe = {
                {"", "", "","neck_vertebrae","skull"},
                {"tail_vertebrae", "pelvis", "ribcage","shoulder_bone","tooth"},
                {"", "leg_bones", "leg_bones", "arm_bones", ""},
                {"", "foot_bones", "foot_bones", "", ""}};
        this.setRecipe(recipe);
        
        ArrayList<Biome> biomeList = new ArrayList<Biome>();
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST));
        this.setSpawn(5, biomeList.toArray(new Biome[biomeList.size()]));
    }
}
