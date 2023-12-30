package spireMapOverhaul.zones.gremlinTown.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.random.Random;

import java.util.ArrayList;

public class GremlinPotionsPatch {
    private static ArrayList<String> tempList = new ArrayList<>();

    @SpirePatch2(
            clz = PotionHelper.class,
            method = "getRandomPotion",
            paramtypez = {}
    )
    @SpirePatch2(
            clz = PotionHelper.class,
            method = "getRandomPotion",
            paramtypez = {Random.class}
    )
    public static class NoGremlinPotionsFromHelper {
        @SpirePrefixPatch
        public static void Prefix() {
            tempList = new ArrayList<>();
            tempList.addAll(PotionHelper.potions);
            tempList.removeIf(str -> (PotionHelper.getPotion(str).rarity != AbstractPotion.PotionRarity.PLACEHOLDER));
            PotionHelper.potions.removeIf(str -> (PotionHelper.getPotion(str).rarity == AbstractPotion.PotionRarity.PLACEHOLDER));
        }

        @SpirePostfixPatch
        public static void Postfix() {
            PotionHelper.potions.addAll(tempList);
        }
    }
}
