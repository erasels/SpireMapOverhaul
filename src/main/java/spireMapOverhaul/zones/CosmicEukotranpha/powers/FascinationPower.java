package spireMapOverhaul.zones.CosmicEukotranpha.powers;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import spireMapOverhaul.SpireAnniversary6Mod;
public class FascinationPower extends BasePower implements CloneablePowerInterface{
    public static final String POWER_ID=SpireAnniversary6Mod.makeID("FascinationPower");private static final PowerStrings powerStrings=CardCrawlGame.languagePack.getPowerStrings(POWER_ID);public static final String NAME=powerStrings.NAME;public static final String[]DESCRIPTIONS=powerStrings.DESCRIPTIONS;
    private static int idOffset=0;public int shoo=0;
    public FascinationPower(AbstractCreature o,AbstractCreature s){super(POWER_ID);this.owner=o;this.source=s;
        this.canGoNegative=false;this.isTurnBased=false;this.type=PowerType.BUFF;this.amount=-1;shoo=AbstractDungeon.cardRandomRng.random(99);updateDescription();
        /*this.loadRegion("fascinationpower");this.region128=new TextureAtlas.AtlasRegion(tex84,0,0,84,84);this.region48=new TextureAtlas.AtlasRegion(tex32,0,0,32,32);*/}
    @Override public void updateDescription(){
        if(shoo<10){
            description=DESCRIPTIONS[0]+shoo;
        }else{
            description=DESCRIPTIONS[1]+shoo;
        }
    }
    @Override public AbstractPower makeCopy(){return new FascinationPower(owner,source);}}

//HAHAHAHAHA