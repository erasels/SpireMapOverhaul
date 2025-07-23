package spireMapOverhaul.zones.humidity.encounters.monsters;

import basemod.ReflectionHacks;
import com.esotericsoftware.spine.BoneData;
import com.esotericsoftware.spine.Skeleton;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.monsters.city.BookOfStabbing;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Enchiridion;
import com.megacrit.cardcrawl.relics.Necronomicon;
import com.megacrit.cardcrawl.relics.NilrysCodex;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.humidity.encounters.monsters.cursedtomeimplementation.CursedTomeManagerPower;

import java.util.ArrayList;

public class HumidityBookOfStabbing extends BookOfStabbing {
    @SpirePatch(clz = AbstractRoom.class, method = SpirePatch.CLASS)
    public static class BookOfStabbingCursedTomeFields {
        public static SpireField<AbstractRelic> relic = new SpireField<>(() -> null);
        public static SpireField<Boolean> hideMonsterAndShowOnlyBook = new SpireField<>(() -> false);
        public static SpireField<Boolean> escapeRight = new SpireField<>(() -> false);

    }


    public void usePreBattleAction() {
        super.usePreBattleAction();
        assignTomeToBookOfStabbing(this);
        Wiz.att(new ApplyPowerAction(Wiz.adp(), Wiz.adp(), new CursedTomeManagerPower(Wiz.adp())));
    }

    public static void assignTomeToBookOfStabbing(HumidityBookOfStabbing book) {
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
        AbstractRelic r = possibleBooks.get(AbstractDungeon.miscRng.random(possibleBooks.size() - 1));

        BookOfStabbingCursedTomeFields.relic.set(Wiz.curRoom(), r);
        book.name = r.name;
    }

    public void update() {
        Skeleton skeleton = ReflectionHacks.getPrivate(this, AbstractCreature.class, "skeleton");
        //Ideally we fade out alpha instead of scale, but that doesn't seem to work for whatever reason
        if (BookOfStabbingCursedTomeFields.hideMonsterAndShowOnlyBook.get(Wiz.curRoom())) {
            for (BoneData bone : skeleton.getData().getBones()) {
                if (bone.getName().equals("spine")) {
                    bone.setScaleX(MathHelper.slowColorLerpSnap(bone.getScaleX(), 0.0F));
                    bone.setScaleY(MathHelper.slowColorLerpSnap(bone.getScaleY(), 0.0F));
                }
            }
        }
        if (BookOfStabbingCursedTomeFields.escapeRight.get(Wiz.curRoom())) {
            if (Wiz.adp().drawX >= this.drawX) {
                if (!this.isDeadOrEscaped()) {
                    this.die();
                }
            }
        }
        super.update();
    }
}
