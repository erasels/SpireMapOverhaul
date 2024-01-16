package spireMapOverhaul.zones.gremlinTown.cards;

import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOCard;
import spireMapOverhaul.zones.gremlinTown.GremlinTown;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

@NoPools
public class Shield extends AbstractSMOCard {
    public final static String ID = makeID(Shield.class.getSimpleName());
    private final static int BLOCK = 11;
    private final static int UPG_BLOCK = 3;
    private final static int COST = 1;

    public Shield() {
        super(ID, GremlinTown.ID, COST, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF, CardColor.COLORLESS);
        baseBlock = BLOCK;
        exhaust = true;
        tags.add(SpireAnniversary6Mod.Enums.GREMLIN);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
    }

    public void upp() {
        upgradeBlock(UPG_BLOCK);
    }
}
