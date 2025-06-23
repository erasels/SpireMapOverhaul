package spireMapOverhaul.zones.humidity.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.ShoutAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.EscapeAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.GremlinLeader;
import com.megacrit.cardcrawl.powers.AbstractPower;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOPower;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.humidity.HumidityZone;
import spireMapOverhaul.zones.humidity.encounters.monsters.joustimplementation.JoustManagerPower;
import spireMapOverhaul.zones.invasion.powers.DrawReductionSingleTurnPower;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class GremlinLavosCorePower extends AbstractSMOPower implements InvisiblePower {
    public static final String POWER_ID = SpireAnniversary6Mod.makeID("GremlinLavosCorePower");
    public static final String NAME = "(DNT)";
    public static String ZONE_ID = HumidityZone.ID;
    public static final String ID = makeID("GremlinLavos");
    public static final String RETREAT = CardCrawlGame.languagePack.getMonsterStrings(ID).DIALOG[0];

    public GremlinLavosCorePower(AbstractCreature owner) {
        super(POWER_ID,NAME,ZONE_ID, AbstractPower.PowerType.BUFF, false, owner, 1);
    }

    public void onDeath() {
        //if gremlin leader is only monster alive, flee
        if(Wiz.getEnemies().size()==1) {
            for (AbstractMonster m : Wiz.getEnemies()) {
                if (m instanceof GremlinLeader){
                    AbstractDungeon.actionManager.addToBottom(new ShoutAction(m, RETREAT, 0.5F, 1.2F));
                    //the isDying check might be redundant but we're just copypasting at this point
                    if (!m.isDying) {
                        AbstractDungeon.actionManager.addToBottom(new EscapeAction(m));
                    }
                }
            }
        }
    }
}
