
package spireMapOverhaul.zones.gremlinTown.powers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOPower;
import spireMapOverhaul.zones.gremlinTown.monsters.GremlinNib;

import static spireMapOverhaul.util.Wiz.atb;

public class GremlinNibPower extends AbstractSMOPower {
    public static final String POWER_ID = SpireAnniversary6Mod.makeID("GremlinNib");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public GremlinNibPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, PowerType.BUFF,false, owner, amount);
        isTwoAmount = true;
        amount2 = 0;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0].replace("{0}", String.valueOf(amount));
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.ATTACK)
            amount2++;

        if (amount2 >= amount) {
            amount2 = 0;
            if (owner instanceof GremlinNib) {  // This should always be true
                atb(new AbstractGameAction() {
                    @Override
                    public void update() {
                        isDone = true;
                        flash();
                        CardCrawlGame.sound.play("STANCE_ENTER_WRATH");
                        ((GremlinNib) owner).setMove(GremlinNib.CRIT, AbstractMonster.Intent.ATTACK_DEBUFF,
                                ((GremlinNib) owner).critDmg);
                        ((GremlinNib) owner).createIntent();
                    }
                });
            }
        }
    }

    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
        greenColor2.a = c.a;
        c = greenColor2;

        FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(amount), x, y, fontScale, c);

        redColor2.a = c.a;
        c = redColor2;

        FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(amount2), x,
                y + 15.0F * Settings.scale, fontScale, c);
    }
}
