package spireMapOverhaul.zones.invasion.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOCard;

public class Languish extends AbstractSMOCard {
    public static final String ID = SpireAnniversary6Mod.makeID("Languish");
    private static final int COST = 0;
    private static final int EFFECT = 4;

    public Languish() {
        super(ID, COST, CardType.SKILL, CardRarity.SPECIAL, CardTarget.ALL_ENEMY, CardColor.COLORLESS);
        this.baseMagicNumber = EFFECT;
        this.magicNumber = this.baseMagicNumber;
        this.exhaust = true;
        this.isEthereal = true;
    }

    public void use(AbstractPlayer p, AbstractMonster monster) {
        for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            this.addToBot(new DamageAction(m, new DamageInfo(AbstractDungeon.player, this.magicNumber, DamageInfo.DamageType.HP_LOSS)));
            this.addToBot(new ApplyPowerAction(m, p, new StrengthPower(m, -this.magicNumber), -this.magicNumber));
            if (m != null && !m.hasPower("Artifact")) {
                this.addToBot(new ApplyPowerAction(m, p, new GainStrengthPower(m, this.magicNumber), this.magicNumber));
            }
        }
    }

    public void upp() {
        this.isEthereal = false;
    }
}
