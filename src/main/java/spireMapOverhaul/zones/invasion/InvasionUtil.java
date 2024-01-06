package spireMapOverhaul.zones.invasion;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.Apotheosis;
import com.megacrit.cardcrawl.cards.colorless.MasterOfStrategy;
import com.megacrit.cardcrawl.cards.colorless.Mayhem;
import com.megacrit.cardcrawl.cards.colorless.SecretTechnique;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.util.ActUtil;
import spireMapOverhaul.zones.invasion.cards.*;
import spireMapOverhaul.zones.invasion.monsters.LivingStormcloud;
import spireMapOverhaul.zones.invasion.monsters.OpulentOffering;
import spireMapOverhaul.zones.invasion.monsters.OrbOfFire;
import spireMapOverhaul.zones.invasion.monsters.ShimmeringMirage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InvasionUtil {
    public static ArrayList<AbstractCard> getRewardCards(int numCards) {
        List<AbstractCard> spells = getSpells();
        List<AbstractCard> blades = getBlades();
        ArrayList<AbstractCard> cards = new ArrayList<>();
        switch (ActUtil.getRealActNum()) {
            case 1:
                cards.addAll(spells);
                break;
            case 2:
                cards.addAll(blades);
                break;
            case 3:
                cards.add(new Apotheosis());
                cards.add(new MasterOfStrategy());
                cards.add(new Mayhem());
                cards.add(new SecretTechnique());
                cards.add(new HandOfTheAbyss());
                cards.add(spells.get(AbstractDungeon.cardRng.random(spells.size() - 1)));
                cards.add(blades.get(AbstractDungeon.cardRng.random(blades.size() - 1)));
                break;
        }
        numCards = Math.min(numCards, cards.size());

        Collections.shuffle(cards, new java.util.Random(AbstractDungeon.cardRng.randomLong()));
        return new ArrayList<>(cards.subList(0, numCards));
    }

    public static List<AbstractCard> getSpells() {
        return Arrays.asList(
                new DarkRitual(),
                new Foresee(),
                new Languish(),
                new LightningBolt(),
                new LightningHelix(),
                new MirarisWake(),
                new Staggershock(),
                new SteelWall(),
                new WallOfBlossoms()
        );
    }

    public static List<AbstractCard> getBlades() {
        return Arrays.asList(
                new EarthblessedBlade(),
                new FireblessedBlade(),
                new IceblessedBlade(),
                new VoidblessedBlade(),
                new WindblessedBlade()
        );
    }

    public static AbstractMonster[] generateElementalGroup() {
        float[] groupPositions = {-450.0F, -200.0F, 50.0F};
        ArrayList<String> monstersList = new ArrayList<>();
        monstersList.add(OrbOfFire.ID);
        monstersList.add(LivingStormcloud.ID);
        monstersList.add(OpulentOffering.ID);
        monstersList.add(ShimmeringMirage.ID);
        Collections.shuffle(monstersList, new java.util.Random(AbstractDungeon.monsterRng.randomLong()));

        AbstractMonster[] monsters = new AbstractMonster[3];
        for (int i = 0; i < 3; i++) {
            monsters[i] = getElemental(monstersList.get(i), groupPositions[i], 125.0F);
        }

        return monsters;
    }

    public static AbstractMonster getElemental(String elementalID, float x, float y) {
        if (elementalID.equals(OrbOfFire.ID)) {
            return new OrbOfFire(x, y);
        }
        if (elementalID.equals(LivingStormcloud.ID)) {
            return new LivingStormcloud(x, y);
        }
        if (elementalID.equals(OpulentOffering.ID)) {
            return new OpulentOffering(x, y);
        }
        if (elementalID.equals(ShimmeringMirage.ID)) {
            return new ShimmeringMirage(x, y);
        }
        SpireAnniversary6Mod.logger.warn("Didn't match any elemental. ElementalID:" + elementalID);
        return new OrbOfFire(x, y);
    }
}
