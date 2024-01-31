package spireMapOverhaul.zones.mycelium.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.exordium.Sentry;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.powers.SporeCloudPower;
import javassist.CannotCompileException;
import javassist.expr.ConstructorCall;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import javassist.expr.NewExpr;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.mycelium.Mycelium;

import java.util.Objects;

@SpirePatch(
        clz = Sentry.class,
        method = "usePreBattleAction"
)
public class CorruptSentryPatch {
    public static class values
    {
        public static final String ID = SpireAnniversary6Mod.makeID("InfectedSentry");
        private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        public static final String NAME = monsterStrings.NAME;
        public static final String DIALOG[] = monsterStrings.DIALOG;
    }
    @SpireInstrumentPatch
    public static ExprEditor CorruptSentry() {
        return new ExprEditor() {
            boolean done = false;
            @Override
            public void edit(NewExpr n) throws CannotCompileException {
                if (!done && n.getClassName().equals(ArtifactPower.class.getName())) {
                    n.replace(
                            "$_=" + CorruptSentryPatch.class.getName() + ".CorruptSentry($1, $2);"
                    );
                    done = true;
                }
            }
        };
    }
    
    public static AbstractPower CorruptSentry(AbstractCreature sentry, int amount)
    {
        AbstractZone curZone = Wiz.getCurZone();
        if(Wiz.getCurZone() != null && Wiz.getCurZone() instanceof Mycelium) {
            sentry.name = values.NAME;
            AbstractDungeon.actionManager.addToBottom(new TalkAction(sentry, values.DIALOG[0], 0.5F, 2.0F));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(sentry, sentry, new SporeCloudPower(sentry, 2)));
            return new PoisonPower(sentry, sentry, 6);
        }
        return new ArtifactPower(sentry, amount);
    }
}