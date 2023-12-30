package spireMapOverhaul.zones.CosmicEukotranpha.powers;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import spireMapOverhaul.zones.CosmicEukotranpha.util.GeneralUtils;
import spireMapOverhaul.zones.CosmicEukotranpha.util.TextureLoader;

import static spireMapOverhaul.zones.CosmicEukotranpha.util.CosmicShortcuts.atb;
public abstract class BasePower extends AbstractPower{protected AbstractCreature source;protected String[]DESCRIPTIONS;public AbstractPlayer p=AbstractDungeon.player;
    public boolean overrideCanNegWithUnique=false;public static int offSet=0;
    private static PowerStrings getPowerStrings(String ID){return CardCrawlGame.languagePack.getPowerStrings(ID);}
    public BasePower(){/*Nothing Yet*/}
    public BasePower(String ID){this(ID,false);}
    public BasePower(String ID,boolean offShift){PowerStrings strings=getPowerStrings(ID);if(offShift){this.ID=ID+offSet;offSet++;}else{this.ID=ID;}this.name=strings.NAME;this.DESCRIPTIONS=strings.DESCRIPTIONS;
        String unPrefixed=GeneralUtils.removePrefix(ID);Texture normalTexture=TextureLoader.getPowerTexture(unPrefixed);Texture hiDefImage=TextureLoader.getHiDefPowerTexture(unPrefixed);
            if(hiDefImage!=null){region128=new TextureAtlas.AtlasRegion(hiDefImage,0,0,hiDefImage.getWidth(),hiDefImage.getHeight());
                if(normalTexture!=null)
                    region48=new TextureAtlas.AtlasRegion(normalTexture,0,0,normalTexture.getWidth(),normalTexture.getHeight());
            }else if(normalTexture!=null){this.img=normalTexture;
                region48=new TextureAtlas.AtlasRegion(normalTexture,0,0,normalTexture.getWidth(),normalTexture.getHeight());}}

    public void offsett(){this.ID+=offSet;offSet++;}public void offsett(int off){this.ID+=off;off++;}
    public void stackPower(int stackAmount){if(this.amount==-1&&(!canGoNegative||(canGoNegative&&overrideCanNegWithUnique))){}else{this.fontScale=8.0F;this.amount+=stackAmount;}}
    public void reducePower(){reducePower(1);}public void reducePower(int reduceAmount){if(this.amount==-1&&(!canGoNegative||(canGoNegative&&overrideCanNegWithUnique))){}else if(this.amount-reduceAmount<=0){this.fontScale=8.0F;this.amount=0;}else{this.fontScale=8.0F;this.amount-=reduceAmount;}if(amount==0){remThis();}}
    public void poO(AbstractPower po){if(po.amount!=0){atb((AbstractGameAction)new ApplyPowerAction((AbstractCreature)owner,(AbstractCreature)owner,po,po.amount,true));}}
    public void poT(AbstractCreature t,AbstractPower po){if(po.amount!=0){atb((AbstractGameAction)new ApplyPowerAction((AbstractCreature)t,(AbstractCreature)owner,po,po.amount,true));}}
    public void poOCanApplyZero(AbstractPower po){atb((AbstractGameAction)new ApplyPowerAction((AbstractCreature)owner,(AbstractCreature)owner,po,po.amount,true));}
    public void poTCanApplyZero(AbstractCreature t,AbstractPower po){atb((AbstractGameAction)new ApplyPowerAction((AbstractCreature)t,(AbstractCreature)owner,po,po.amount,true));}
    public void dmg(AbstractCreature t,int d){atb(new DamageAction(t,new DamageInfo(owner,d),AbstractGameAction.AttackEffect.NONE,true));}
    public void dmgAll(int d){atb(new DamageAllEnemiesAction(null,DamageInfo.createDamageMatrix(d,true),DamageInfo.DamageType.THORNS,AbstractGameAction.AttackEffect.NONE,true));}
    public void blck(int b){atb(new GainBlockAction(owner,owner,b));}public void blck(int b,AbstractCreature target){atb(new GainBlockAction(target,owner,b));}

    public void remThis(){atb(new RemoveSpecificPowerAction(owner,owner,this));} public void reducePowerSuper(int am){super.reducePower(am);}
    public BasePower(String id, PowerType powerType, boolean isTurnBased, AbstractCreature owner, int amount) {
        this(id, powerType, isTurnBased, owner, null, amount);
    }
    public BasePower(String id, PowerType powerType, boolean isTurnBased, AbstractCreature owner, AbstractCreature source, int amount) {
        this(id, powerType, isTurnBased, owner, source, amount, true);
    }
    public BasePower(String id, PowerType powerType, boolean isTurnBased, AbstractCreature owner, AbstractCreature source, int amount, boolean initDescription) {
        this(id, powerType, isTurnBased, owner, source, amount, initDescription, true);
    }
    public BasePower(String id, PowerType powerType, boolean isTurnBased, AbstractCreature owner, AbstractCreature source, int amount, boolean initDescription, boolean loadImage) {
        this.ID = id;
        this.isTurnBased = isTurnBased;

        PowerStrings strings = getPowerStrings(this.ID);
        this.name = strings.NAME;
        this.DESCRIPTIONS = strings.DESCRIPTIONS;

        this.owner = owner;
        this.source = source;
        this.amount = amount;
        this.type = powerType;

        if (loadImage)
        {
            String unPrefixed = GeneralUtils.removePrefix(id);
            Texture normalTexture = TextureLoader.getPowerTexture(unPrefixed);
            Texture hiDefImage = TextureLoader.getHiDefPowerTexture(unPrefixed);
            if (hiDefImage != null)
            {
                region128 = new TextureAtlas.AtlasRegion(hiDefImage, 0, 0, hiDefImage.getWidth(), hiDefImage.getHeight());
                if (normalTexture != null)
                    region48 = new TextureAtlas.AtlasRegion(normalTexture, 0, 0, normalTexture.getWidth(), normalTexture.getHeight());
            }
            else if (normalTexture != null)
            {
                this.img = normalTexture;
                region48 = new TextureAtlas.AtlasRegion(normalTexture, 0, 0, normalTexture.getWidth(), normalTexture.getHeight());
            }
        }

        if (initDescription)
            this.updateDescription();
    }
}