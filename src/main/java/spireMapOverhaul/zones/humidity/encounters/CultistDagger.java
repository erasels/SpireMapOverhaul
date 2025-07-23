package spireMapOverhaul.zones.humidity.encounters;

import basemod.ReflectionHacks;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.unique.RitualDaggerAction;
import com.megacrit.cardcrawl.actions.utility.ShowCardAndPoofAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.colorless.RitualDagger;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.Cultist;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.PaperFrog;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.megacrit.cardcrawl.stances.NeutralStance;
import javassist.CtBehavior;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.humidity.HumidityZone;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class CultistDagger {
    @SpirePatch(clz = Cultist.class, method = "takeTurn")
    public static class MovePatch {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Foo(Cultist __instance) {
            if (HumidityZone.isNotInZone()) return;
            RitualDagger card = new RitualDagger();
            card.current_x = __instance.drawX;
            card.current_y = __instance.drawY + 150 * Settings.scale;
            Wiz.atb(new ShowCardAndPoofAction(card));

            //we can't use calculateCardDamage as it will apply player buffs too.
            //so just set damage to 15 and then check for enemy Vulnerable.
            applyMonsterPowers(card, __instance);
            RitualDaggerAction action = new RitualDaggerAction(__instance, new DamageInfo(__instance, card.damage, card.damageTypeForTurn), card.magicNumber, card.uuid);
            TemporaryRitualDaggers.card.set(action, card);
            Wiz.atb(action);
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(MathUtils.class, "random");
                return LineFinder.findInOrder(ctBehavior, finalMatcher);
            }
        }
    }

    public static void applyMonsterPowers(AbstractCard card, AbstractMonster monster) {
        float tmp = card.baseDamage;
        if (monster.hasPower(VulnerablePower.POWER_ID)) {
            if (Wiz.adp().hasRelic(PaperFrog.ID)) {
                tmp *= 1.75f;
            } else {
                tmp *= 1.5f;
            }
        }
        if (card.baseDamage != MathUtils.floor(tmp)) {
            card.isDamageModified = true;
        }
        card.damage = MathUtils.floor(tmp);
        AbstractStance actualStance = Wiz.adp().stance;
        ArrayList<AbstractPower> actualPowers = new ArrayList<>(Wiz.adp().powers);
        ArrayList<AbstractRelic> actualRelics = new ArrayList<>(Wiz.adp().relics);
        Wiz.adp().stance = new NeutralStance();
        Wiz.adp().powers.clear();
        Wiz.adp().relics.clear();
        card.calculateCardDamage(monster);
        Wiz.adp().stance = actualStance;
        Wiz.adp().powers = actualPowers;
        Wiz.adp().relics = actualRelics;

    }

    @SpirePatch2(clz = Cultist.class, method = "getMove")
    public static class NamePatch {
        @SpirePostfixPatch
        public static void Foo(Cultist __instance, int num) {
            if (HumidityZone.isNotInZone()) return;
            String INCANTATION_NAME = ReflectionHacks.getPrivateStatic(Cultist.class, "INCANTATION_NAME");
            if (Objects.equals(__instance.moveName, INCANTATION_NAME))
                __instance.moveName = new RitualDagger().name;
        }
    }

    @SpirePatch(clz = RitualDaggerAction.class, method = SpirePatch.CLASS)
    public static class TemporaryRitualDaggers {
        public static SpireField<AbstractCard> card = new SpireField<>(() -> null);
    }

    @SpirePatch(clz = RitualDaggerAction.class, method = "update")
    public static class TemporaryCardUpgradePatch {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Foo(RitualDaggerAction __instance) {
            if (HumidityZone.isNotInZone()) return;
            UUID uuid = ReflectionHacks.getPrivate(__instance, RitualDaggerAction.class, "uuid");
            int increaseAmount = ReflectionHacks.getPrivate(__instance, RitualDaggerAction.class, "increaseAmount");
            AbstractCard c = TemporaryRitualDaggers.card.get(__instance);
            if (c != null) {
                if (c.uuid.equals(uuid)) {
                    c.misc += increaseAmount;
                    c.baseDamage = c.misc;
                    c.isDamageModified = false;
                    applyMonsterPowers(c, (AbstractMonster) __instance.target);
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractDungeon.class, "player");
                return LineFinder.findInOrder(ctBehavior, finalMatcher);
            }
        }
    }

}
