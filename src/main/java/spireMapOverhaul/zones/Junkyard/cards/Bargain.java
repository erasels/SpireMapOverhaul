package spireMapOverhaul.zones.Junkyard.cards;

import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOCard;
import spireMapOverhaul.zones.Junkyard.Junkyard;
import spireMapOverhaul.zones.Junkyard.actions.BuyCardAction;
import spireMapOverhaul.zones.Junkyard.monsters.Peddler;

import java.util.ArrayList;

@NoPools
public class Bargain extends AbstractSMOCard {
    public static final String ID = SpireAnniversary6Mod.makeID("Bargain");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    private static final int COST = 0;
    public int goldCost;
    public ArrayList<AbstractCard> cardsOnOffer = new ArrayList<AbstractCard>();
    private float previewTimer = 0.0f;
    private float previewWindow = 0.8f;
    private int index = 0;

    public Bargain() {
        super(ID, Junkyard.ID, COST, CardType.SKILL, CardRarity.SPECIAL, CardTarget.NONE, CardColor.COLORLESS);
        this.selfRetain = true;
        this.exhaust = true;
        goldCost = 50;
    }

    public Bargain(int cost) {
        super(ID, Junkyard.ID, COST, CardType.SKILL, CardRarity.SPECIAL, CardTarget.NONE, CardColor.COLORLESS);
        this.selfRetain = true;
        this.exhaust = true;
        goldCost = cost;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new BuyCardAction(cardsOnOffer, goldCost));
        Peddler peddler;
        for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters){
            if (monster.id.equals(Peddler.ID)){
                peddler = (Peddler)monster;
                peddler.doBusiness();
            }
        }
    }

    public void setCost(int cost){
        goldCost = cost;
    }

    @Override
    public void applyPowers(){
        super.applyPowers();
        Peddler peddler;
        cardsOnOffer.clear();
        for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters){
            if (monster.id.equals(Peddler.ID)){
                peddler = (Peddler)monster;
                cardsOnOffer.addAll(peddler.wares);
            }
        }
        this.rawDescription = EXTENDED_DESCRIPTION[0] + goldCost + EXTENDED_DESCRIPTION[1];
        initializeDescription();
    }

    @Override
    public void update(){
        super.update();
        if (!cardsOnOffer.isEmpty()) {
            previewTimer += Gdx.graphics.getDeltaTime();
            if (previewTimer > previewWindow) {
                index++;
                if (index >= cardsOnOffer.size()) {
                    index = 0;
                }
                this.cardsToPreview = cardsOnOffer.get(index);
                previewTimer = 0.0f;
            }
        }
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (!super.canUse(p, m)){
            return false;
        }
        else {
            return p.gold >= goldCost;
        }
    }

    public void upp() {
        goldCost -= 10;
    }

    @Override
    public AbstractCard makeCopy(){
        return new Bargain(goldCost);
    }
}
