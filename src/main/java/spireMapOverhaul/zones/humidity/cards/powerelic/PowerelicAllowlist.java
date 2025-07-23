package spireMapOverhaul.zones.humidity.cards.powerelic;

import com.megacrit.cardcrawl.relics.*;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.humidity.cards.powerelic.implementation.PowerelicCard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;


/// //////////////////////TLDR/////////////////////////
// 1. if a relic overrides onEquip, it's automatically blocklisted
// 2. if a relic doesn't override onEquip and you need to blocklist it, add it to blocklistedRelics
// 3. if a relic overrides onEquip and you want to allow it anyway,
//      add it to one of essentialEquipRelics or nonessentialEquipRelics:
//          3a. if the relic turns on/off via onEquip/onUnequip, add it to essentialEquipRelics
//      4a. if the relic changes your max energy or draw size, also add it to immediateOnequipRelics
//          3b. if the relic turns on/off by virtue of being in the relic list, add it to nonessentialEquipRelics
//              4b. if the relic would cause a permanent change by being duplicated, also add it to skipEquipIfTempRelics


public class PowerelicAllowlist {

    public static void populateEssentialEquipRelics() {
        //Relics whose functionality is tied to OnEquip/OnUnequip are listed here.
        //Modded relics may be added to the list.
        //Only allow a relic here if it is okay to repeatedly call OnEquip/OnUnequip on it.
        essentialEquipRelics = new HashSet<>(Arrays.asList(
                BustedCrown.ID,
                CoffeeDripper.ID,
                CursedKey.ID,
                DuVuDoll.ID,
                Ectoplasm.ID,
                FrozenEgg2.ID,
                MarkOfPain.ID,
                MoltenEgg2.ID,
                PhilosopherStone.ID,
                RingOfTheSerpent.ID,
                RunicDome.ID,
                SacredBark.ID,
                SneckoEye.ID,
                Sozu.ID,
                ToxicEgg2.ID,
                VelvetChoker.ID
        ));
    }

    public static void populateNonessentialEquipRelics() {
        //Relics which override OnEquip/OnUnequip but do not contain essential functionality therein are listed here.
        //In other words, relics in this list do not use OnEquip/OnUnequip to activate/deactivate them.
        //(For vanilla relics, these are counter relics where the counter is initially set to 0 when equipped.)
        //These will remain perpetually "equipped" even if they are not present in the player's relic list.
        //Note that OnUnequip will still be called if the relic card is removed from the deck,
        // and OnEquip will still be called if the relic card is duplicated.
        //Modded relics may be added to the list.

        nonessentialEquipRelics = new HashSet<>(Arrays.asList(
                Circlet.ID,
                HappyFlower.ID,
                IncenseBurner.ID,
                Inserter.ID,
                Necronomicon.ID,
                PrismaticShard.ID,
                Sundial.ID,
                TinyChest.ID
        ));
    }

    public static void populateImmediateOnequipRelics() {
        //A subset of essentialEquipRelics,
        //this modifies the card such that when the card is played,
        //changes to maximum energy will take effect immediately (player will also gain energy equal to the change)
        //and the player will draw cards equal to the change in hand size.
        //(This list does not affect the allowlist; relics on this list must still be added to the essentialEquipRelics list above.)
        immediateOnequipRelics = new HashSet<>(Arrays.asList(
                BustedCrown.ID,
                CoffeeDripper.ID,
                CursedKey.ID,
                Ectoplasm.ID,
                MarkOfPain.ID,
                PhilosopherStone.ID,
                RingOfTheSerpent.ID,
                RunicDome.ID,
                SneckoEye.ID,
                Sozu.ID,
                SlaversCollar.ID,
                VelvetChoker.ID
        ));
    }

    public static void populateSkipEquipIfTempRelics() {
        //A subset of nonessentialEquipRelics,
        //this modifies card duplication behavior such that when a relic card is duplicated,
        //onEquip/onUnequip will not be called unless the card is permanently added to the deck.
        //(Intended only for cards which contain permanent effects in onEquip which can still be undone by onUnequip.)
        //(This list does not affect the allowlist; relics on this list must still be added to the nonessentialEquipRelics list above.)
        skipEquipIfTempRelics = new HashSet<>(Collections.singletonList(
                Necronomicon.ID
        ));
    }

    public static void populateBlocklistedRelics() {
        //Modded relics which should never be converted to powers go here.
        //(Relics which override OnEquip are blocklisted by default.)
        blocklistedRelics = new HashSet<>(Collections.singletonList(
                //PowerelicRelic.ID,    //PowerelicRelic is exclusive to anniv7!
                //for later: we might be able to unlock circlet if there aren't any issues with it
                Circlet.ID
        ));
    }

    public static HashSet<String> essentialEquipRelics;
    public static HashSet<String> nonessentialEquipRelics;
    public static HashSet<String> immediateOnequipRelics;
    public static HashSet<String> skipEquipIfTempRelics;
    public static HashSet<String> blocklistedRelics;


    public static ArrayList<AbstractRelic> getAllConvertibleRelics() {
        ArrayList<AbstractRelic> convertibleRelics = new ArrayList<>();
        for (AbstractRelic relic : Wiz.adp().relics) {
            if (PowerelicAllowlist.isRelicConvertibleToCard(relic)) {
                convertibleRelics.add(relic);
            }
        }
        return convertibleRelics;
    }

    public static boolean isRelicConvertibleToCard(AbstractRelic relic) {
        if (PowerelicCard.PowerelicRelicContainmentFields.isContained.get(relic))
            return false;
        if (isBlocklistedRelic(relic))
            return false;
        if (!doesRelicOverrideOnEquip(relic))
            return true;
        if (isEssentialEquipRelic(relic))
            return true;
        return isNonessentialEquipRelic(relic);
    }

    public static boolean doesRelicOverrideOnEquip(AbstractRelic relic) {
        Class<?> relicClass = relic.getClass();
        try {
            return (relicClass.getMethod("onEquip").getDeclaringClass() != AbstractRelic.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }


    public static boolean isEssentialEquipRelic(AbstractRelic relic) {
        if (relic == null) return false;
        if (essentialEquipRelics == null) {
            populateEssentialEquipRelics();
        }
        return essentialEquipRelics.contains(relic.relicId);
    }

    public static boolean isImmediateOnequipRelic(AbstractRelic relic) {
        if (relic == null) return false;
        if (immediateOnequipRelics == null) {
            populateImmediateOnequipRelics();
        }
        return immediateOnequipRelics.contains(relic.relicId);
    }

    public static boolean isNonessentialEquipRelic(AbstractRelic relic) {
        if (relic == null) return false;
        if (nonessentialEquipRelics == null) {
            populateNonessentialEquipRelics();
        }
        return nonessentialEquipRelics.contains(relic.relicId);
    }

    public static boolean isSkipEquipIfTempRelic(AbstractRelic relic) {
        if (relic == null) return false;
        if (skipEquipIfTempRelics == null) {
            populateSkipEquipIfTempRelics();
        }
        return skipEquipIfTempRelics.contains(relic.relicId);
    }

    public static boolean isBlocklistedRelic(AbstractRelic relic) {
        if (relic == null) return false;
        if (blocklistedRelics == null) {
            populateBlocklistedRelics();
        }
        return blocklistedRelics.contains(relic.relicId);
    }
}
