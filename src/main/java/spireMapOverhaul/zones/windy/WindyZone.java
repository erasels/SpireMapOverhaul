package spireMapOverhaul.zones.windy;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zoneInterfaces.CombatModifyingZone;
import spireMapOverhaul.zoneInterfaces.OnTravelZone;
import spireMapOverhaul.zones.windy.actions.FlyAwayAction;

public class WindyZone extends AbstractZone implements CombatModifyingZone {
    public static final String ID = "Windy";

    public WindyZone() {
        super(ID, Icons.MONSTER, Icons.EVENT);
        this.width = 2;
        this.maxWidth = 4;
        this.height = 3;
        this.maxHeight = 4;
    }

    @Override
    public AbstractZone copy() {
        return new WindyZone();
    }

    @Override
    public Color getColor() {
        return Color.WHITE.cpy();
    }

    public String getCombatText() {
        return "TEXT";
    }

    public void postDamageCheck(AbstractMonster m){
        SpireAnniversary6Mod.logger.info("hit! " + m.currentHealth + " " + m.maxHealth/10);
        if(m.currentHealth <= m.maxHealth/10 && !m.isDeadOrEscaped()){
            Wiz.atb(new FlyAwayAction(m));
        }
    }
}
