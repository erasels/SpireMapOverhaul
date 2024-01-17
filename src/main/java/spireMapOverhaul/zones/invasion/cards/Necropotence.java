package spireMapOverhaul.zones.invasion.cards;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.VerticalAuraEffect;
import spireMapOverhaul.SpireAnniversary6Mod;

public class Necropotence extends AbstractInvasionZoneCard {
    public static final String ID = SpireAnniversary6Mod.makeID("Necropotence");
    private static final int COST = 0;
    private static final int DRAW = 1;
    private static final int UPGRADE_DRAW = 1;
    private static final int HEALTH_LOSS = 1;

    public Necropotence() {
        super(ID, COST, CardType.SKILL, CardRarity.SPECIAL, CardTarget.SELF, CardColor.COLORLESS);
        this.baseMagicNumber = DRAW;
        this.magicNumber = this.baseMagicNumber;
        this.selfRetain = true;
        this.returnToHand = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new VFXAction(p, new VerticalAuraEffect(Color.BLACK, p.hb.cX, p.hb.cY), 0.33F));
        this.addToBot(new VFXAction(p, new BorderLongFlashEffect(Color.MAGENTA), 0.0F, true));
        this.addToBot(new DrawCardAction(p, this.magicNumber));
        this.addToBot(new LoseHPAction(p, p, HEALTH_LOSS));
    }

    public void upp() {
        this.upgradeMagicNumber(UPGRADE_DRAW);
    }
}
