package spireMapOverhaul.zones.frostlands.relics;

import com.evacipated.cardcrawl.mod.stslib.relics.OnPlayerDeathRelic;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.combat.SmokeBombEffect;
import spireMapOverhaul.abstracts.AbstractSMORelic;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.frostlands.FrostlandsZone;
import spireMapOverhaul.zones.frostlands.events.SnowmanMafiaEvent;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class OldHat extends AbstractSMORelic{
    public static final String ID = makeID(OldHat.class.getSimpleName());
    public static final int amount = 3;
    public OldHat() {
        super(ID, FrostlandsZone.ID, RelicTier.SPECIAL, LandingSound.CLINK);
    }

    @Override
    public void atTurnStart() {
        Wiz.atb(new ApplyPowerAction(Wiz.adp(), Wiz.adp(), new VigorPower(Wiz.adp(), amount)));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new OldHat();
    }

}
