package spireMapOverhaul.zones.gremlinTown.cards;

import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOCard;
import spireMapOverhaul.zones.gremlinTown.GremlinTown;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.util.Wiz.*;

@NoPools
public class PoisonSpray extends AbstractSMOCard {
    public final static String ID = makeID(PoisonSpray.class.getSimpleName());
    private final static int COST = 2;
    private final static int DAMAGE = 7;
    private final static int UPG_DAMAGE = 2;

    public PoisonSpray() {
        super(ID, GremlinTown.ID, COST, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY, CardColor.COLORLESS);
        baseDamage = DAMAGE;
        tags.add(SpireAnniversary6Mod.Enums.GREMLIN);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        DamageInfo info2 = new DamageInfo(adp(), damage, DamageInfo.DamageType.NORMAL);
        atb(new AbstractGameAction() {
            @Override
            public void update() {
                if (m == null || adp() != null && adp().isDying || m.isDeadOrEscaped()) {
                    isDone = true;
                } else {
                    tickDuration();
                    if (isDone) {
                        AbstractDungeon.effectList.add(new FlashAtkImgEffect(m.hb.cX, m.hb.cY, AttackEffect.POISON, false));
                        m.damage(info2);
                        if (m.lastDamageTaken > 0)
                            applyToEnemy(m, new PoisonPower(m, adp(), m.lastDamageTaken));

                        if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead())
                            AbstractDungeon.actionManager.clearPostCombatActions();
                        else
                            att(new WaitAction(0.1F));
                    }
                }
            }
        });
    }

    public void upp() {
        upgradeDamage(UPG_DAMAGE);
    }
}
