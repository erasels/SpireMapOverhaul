package spireMapOverhaul.zones.candyland.consumables.rare;

import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import spireMapOverhaul.zones.candyland.consumables.AbstractConsumable;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.atb;


public class Cocktail extends AbstractConsumable {
    public final static String ID = makeID(Cocktail.class.getSimpleName());

    public Cocktail() {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        tags.add(CardTags.HEALING);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractPotion potion = AbstractDungeon.returnRandomPotion();
        while(potion.isThrown){
            potion = AbstractDungeon.returnRandomPotion(potion.rarity, false);
        }
        atb(new ObtainPotionAction(potion));
    }

    @Override
    public void upp() {
        upgradeBaseCost(0);
    }
}