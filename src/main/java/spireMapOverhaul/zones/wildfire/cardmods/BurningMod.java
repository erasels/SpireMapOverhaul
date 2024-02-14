package spireMapOverhaul.zones.wildfire.cardmods;

import basemod.abstracts.AbstractCardModifier;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.util.extraicons.ExtraIcons;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.ChemicalX;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.util.TexLoader;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.wildfire.powers.BurningPower;

import static spireMapOverhaul.SpireAnniversary6Mod.makeUIPath;
import static spireMapOverhaul.zones.wildfire.FormatHelper.insertAfterText;

public class BurningMod extends AbstractCardModifier {
    public static final String ID = SpireAnniversary6Mod.makeID("Wildfire:BurningMod");
    private static final Texture ICON = TexLoader.getTexture(makeUIPath("Wildfire/BurningIcon.png"));
    private static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    int amount;

    public BurningMod(int amount) {
        this.amount = amount;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        if (card.cost == -1) {
            if (card.target == AbstractCard.CardTarget.ENEMY || card.target == AbstractCard.CardTarget.SELF_AND_ENEMY) {
                return insertAfterText(rawDescription, String.format(TEXT[3], amount));
            } else if (card.target == AbstractCard.CardTarget.ALL_ENEMY || card.target == AbstractCard.CardTarget.ALL) {
                return insertAfterText(rawDescription, String.format(TEXT[4], amount));
            } else {
                return insertAfterText(rawDescription, String.format(TEXT[5], amount));
            }
        } else {
            if (card.target == AbstractCard.CardTarget.ENEMY || card.target == AbstractCard.CardTarget.SELF_AND_ENEMY) {
                return insertAfterText(rawDescription, String.format(TEXT[0], amount));
            } else if (card.target == AbstractCard.CardTarget.ALL_ENEMY || card.target == AbstractCard.CardTarget.ALL) {
                return insertAfterText(rawDescription, String.format(TEXT[1], amount));
            } else {
                return insertAfterText(rawDescription, String.format(TEXT[2], amount));
            }
        }
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        if (card.cost == -1) {
            int times = card.energyOnUse;
            if (Wiz.adp().hasRelic(ChemicalX.ID)) {
                times += ChemicalX.BOOST;
            }
            if (card.target == AbstractCard.CardTarget.ENEMY || card.target == AbstractCard.CardTarget.SELF_AND_ENEMY) {
                for (int i = 0 ; i < times ; i++) {
                    Wiz.applyToEnemy((AbstractMonster) target, new BurningPower(target, amount));
                }
            } else if (card.target == AbstractCard.CardTarget.ALL_ENEMY || card.target == AbstractCard.CardTarget.ALL) {
                for (int i = 0 ; i < times ; i++) {
                    Wiz.forAllMonstersLiving(mon -> Wiz.applyToEnemy(mon, new BurningPower(mon, amount)));
                }
            } else {
                for (int i = 0 ; i < times ; i++) {
                    AbstractMonster mon = AbstractDungeon.getRandomMonster();
                    if (mon != null) {
                        Wiz.applyToEnemy(mon, new BurningPower(mon, amount));
                    }
                }
            }
        } else {
            if (card.target == AbstractCard.CardTarget.ENEMY || card.target == AbstractCard.CardTarget.SELF_AND_ENEMY && target instanceof AbstractMonster) {
                Wiz.applyToEnemy((AbstractMonster) target, new BurningPower(target, amount));
            } else if (card.target == AbstractCard.CardTarget.ALL_ENEMY || card.target == AbstractCard.CardTarget.ALL) {
                Wiz.forAllMonstersLiving(mon -> Wiz.applyToEnemy(mon, new BurningPower(mon, amount)));
            } else {
                AbstractMonster mon = AbstractDungeon.getRandomMonster();
                if (mon != null) {
                    Wiz.applyToEnemy(mon, new BurningPower(mon, amount));
                }
            }
        }
    }

    @Override
    public void onRender(AbstractCard card, SpriteBatch sb) {
        ExtraIcons.icon(ICON).render(card);
    }

    @Override
    public void onSingleCardViewRender(AbstractCard card, SpriteBatch sb) {
        ExtraIcons.icon(ICON).render(card);
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new BurningMod(amount);
    }
}
