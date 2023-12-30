package spireMapOverhaul.zones.CosmicEukotranpha.cards.cardMods;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;import spireMapOverhaul.SpireAnniversary6Mod;
public class PlayTwiceMod extends AbstractCardModifier{public static final String ID=SpireAnniversary6Mod.makeID(
				PlayTwiceMod.class.getSimpleName());public AbstractCardModifier makeCopy(){return new
				PlayTwiceMod(key);}public String identifier(AbstractCard card){return ID;}public int key=0;public int am=0;public int change=0;
public PlayTwiceMod(){}public PlayTwiceMod(int key){this.key=key;}//Keys
public void onUse(AbstractCard card,AbstractCreature target,UseCardAction action){
	if(!card.purgeOnUse){AbstractMonster m=null;if(action.target!=null){m=(AbstractMonster)action.target;}
		AbstractCard tmp=card.makeSameInstanceOf();AbstractDungeon.player.limbo.addToBottom(tmp);
		tmp.current_x=card.current_x;tmp.current_y=card.current_y;tmp.target_x=(float)Settings.WIDTH/2.0F-300.0F*Settings.scale;tmp.target_y=(float)Settings.HEIGHT/2.0F;
		if(m!=null){tmp.calculateCardDamage(m);}tmp.purgeOnUse=true;
		AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(tmp,m,card.energyOnUse,true,true),true);}}
public String modifyDescription(String rawDescription,AbstractCard card){return "Test";}}