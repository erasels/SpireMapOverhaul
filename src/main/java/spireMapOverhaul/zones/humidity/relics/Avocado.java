package spireMapOverhaul.zones.humidity.relics;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import spireMapOverhaul.abstracts.AbstractSMORelic;
import spireMapOverhaul.zones.humidity.HumidityZone;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class Avocado extends AbstractSMORelic implements CustomSavable<Integer> {
    public static final String ID = makeID(Avocado.class.getSimpleName());
    public static final int MINIMUM_HP = 7;
    public int hpLost = 0;

    public Avocado() {
        super(ID, HumidityZone.ID, RelicTier.SPECIAL, LandingSound.FLAT);
    }

    public int getHPGain() {
        return Math.max(hpLost / 2, MINIMUM_HP);
    }

    public String getUpdatedDescription() {
        if (getHPGain() <= MINIMUM_HP) {
            return this.DESCRIPTIONS[0] + this.DESCRIPTIONS[1] + MINIMUM_HP + this.DESCRIPTIONS[3];
        } else {
            return this.DESCRIPTIONS[0] + this.DESCRIPTIONS[2] + getHPGain() + this.DESCRIPTIONS[3];
        }
    }

    public void onEquip() {
        AbstractDungeon.player.increaseMaxHp(getHPGain(), true);
    }


    public AbstractRelic makeCopy() {
        return new Avocado();
    }

    @Override
    public Integer onSave() {
        return hpLost;
    }

    @Override
    public void onLoad(Integer integer) {
        if (integer == null) integer = 0;
        this.hpLost = integer;
        description = getUpdatedDescription();
        tips.clear();
        tips.add(new PowerTip(name, description));
    }
}
