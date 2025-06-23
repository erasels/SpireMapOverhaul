package spireMapOverhaul.zones.humidity.encounters;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.esotericsoftware.spine.Bone;
import com.esotericsoftware.spine.BoneData;
import com.esotericsoftware.spine.Skeleton;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.BookOfStabbing;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Enchiridion;
import com.megacrit.cardcrawl.relics.Necronomicon;
import com.megacrit.cardcrawl.relics.NilrysCodex;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.humidity.HumidityZone;
import spireMapOverhaul.zones.humidity.encounters.monsters.cursedtomeimplementation.CursedTomeManagerPower;

import java.util.ArrayList;

public class BookOfStabbingCursedTome {

    @SpirePatch(clz = AbstractRoom.class, method = SpirePatch.CLASS)
    public static class BookOfStabbingCursedTomeFields
    {
        public static SpireField<AbstractRelic> relic = new SpireField<>(() -> null);
        public static SpireField<Boolean> hideMonsterAndShowOnlyBook = new SpireField<>(() -> false);
        public static SpireField<Boolean> escapeRight = new SpireField<>(() -> false);

    }


    @SpirePatch2(clz = BookOfStabbing.class, method = "usePreBattleAction")
    public static class BookOfStabbingCursedTomeEventPowerPatch{
        @SpirePostfixPatch
        public static void usePreBattleAction(BookOfStabbing __instance){
            if(HumidityZone.isNotInZone())return;
            assignTomeToBookOfStabbing(__instance);
            Wiz.att(new ApplyPowerAction(Wiz.adp(), Wiz.adp(), new CursedTomeManagerPower(Wiz.adp())));
        }
    }


    public static void assignTomeToBookOfStabbing(BookOfStabbing book){
        ArrayList<AbstractRelic> possibleBooks = new ArrayList();
        if (!AbstractDungeon.player.hasRelic(Necronomicon.ID)) {
            possibleBooks.add(RelicLibrary.getRelic(Necronomicon.ID).makeCopy());
        }
        if (!AbstractDungeon.player.hasRelic(Enchiridion.ID)) {
            possibleBooks.add(RelicLibrary.getRelic(Enchiridion.ID).makeCopy());
        }
        if (!AbstractDungeon.player.hasRelic(NilrysCodex.ID)) {
            possibleBooks.add(RelicLibrary.getRelic(NilrysCodex.ID).makeCopy());
        }

        if (possibleBooks.size() == 0) {
            return;
        }
        AbstractRelic r = (AbstractRelic)possibleBooks.get(AbstractDungeon.miscRng.random(possibleBooks.size() - 1));

        BookOfStabbingCursedTomeFields.relic.set(Wiz.curRoom(),r);
        book.name = r.name;
    }

    @SpirePatch2(clz= AbstractRoom.class,method="addRelicToRewards",paramtypez = {AbstractRelic.RelicTier.class})
    public static class LootPatch{
        @SpirePrefixPatch
        public static SpireReturn<Void> Foo(AbstractRoom __instance){
            AbstractRelic relic = null;
            relic = BookOfStabbingCursedTomeFields.relic.get(__instance);
            if(relic!=null){
                __instance.rewards.add(new RewardItem(relic));
                return SpireReturn.Return();
            }else{
                return SpireReturn.Continue();
            }
        }
    }

    @SpirePatch2(clz = AbstractMonster.class,method="update")
    public static class BookDrawingPatch{
        @SpirePrefixPatch
        public static void Foo(AbstractMonster __instance){
            if(!(__instance instanceof BookOfStabbing))return;
            if(HumidityZone.isNotInZone())return;
            Skeleton skeleton = ReflectionHacks.getPrivate(__instance, AbstractCreature.class,"skeleton");
            //Ideally we fade out alpha instead of scale, but that doesn't seem to work for whatever reason
            if(BookOfStabbingCursedTomeFields.hideMonsterAndShowOnlyBook.get(Wiz.curRoom())){
                for(BoneData bone : skeleton.getData().getBones()){
                    if(bone.getName().equals("spine")) {
                        bone.setScaleX(MathHelper.slowColorLerpSnap(bone.getScaleX(), 0.0F));
                        bone.setScaleY(MathHelper.slowColorLerpSnap(bone.getScaleY(), 0.0F));
                    }
                }
            }
            if(BookOfStabbingCursedTomeFields.escapeRight.get(Wiz.curRoom())){
                if(Wiz.adp().drawX>=__instance.drawX){
                    if(!__instance.isDeadOrEscaped()) {
                        __instance.die();
                    }
                }
            }
        }
    }

    @SpirePatch2(clz = AbstractPlayer.class,method="updateEscapeAnimation")
    public static class CollectTomeAnimationPatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> Foo(AbstractPlayer __instance) {
            if (Wiz.curRoom() != null) {
                if (BookOfStabbingCursedTomeFields.escapeRight.get(Wiz.curRoom())) {
                    __instance.drawX += Gdx.graphics.getDeltaTime() * 500.0F * Settings.scale;
                    return SpireReturn.Return();
                }
            }
            return SpireReturn.Continue();
        }
    }

}
