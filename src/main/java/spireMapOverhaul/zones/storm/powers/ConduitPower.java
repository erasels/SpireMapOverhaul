package spireMapOverhaul.zones.storm.powers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.patches.NeutralPowertypePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import spireMapOverhaul.abstracts.AbstractSMOPower;
import spireMapOverhaul.zones.storm.StormZone;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.atb;

public class ConduitPower extends AbstractSMOPower {
    public static final String POWER_ID = makeID("ConduitPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private Color redColor = new Color(1.0F, 0.0F, 0.0F, 1.0F);

    public ConduitPower(AbstractCreature target) {
        super(POWER_ID, NAME, StormZone.ID, NeutralPowertypePatch.NEUTRAL, false, target, target instanceof AbstractPlayer ? target.maxHealth / 20 : target.maxHealth / 10);
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public void atEndOfRound() {
        atb(new VFXAction(new LightningEffect(owner.drawX, owner.drawY)));
        atb(new RemoveSpecificPowerAction(owner, owner, this));
        atb(new DamageAction(owner, new DamageInfo(null, amount, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.NONE, true));
        atb(new SFXAction("ORB_LIGHTNING_EVOKE"));
    }

    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
        redColor.a = c.a;
        c = redColor;
        FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(amount), x, y, fontScale, c);
    }

}

