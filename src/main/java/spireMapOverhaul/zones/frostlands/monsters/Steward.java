package spireMapOverhaul.zones.frostlands.monsters;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateShakeAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.CanLoseAction;
import com.megacrit.cardcrawl.actions.unique.CannotLoseAction;
import com.megacrit.cardcrawl.actions.utility.HideHealthBarAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.vfx.combat.LightFlareParticleEffect;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.frostlands.powers.FrigidPower;

import static spireMapOverhaul.SpireAnniversary6Mod.modID;

public class Steward extends CustomMonster
{
    public static final String ID = SpireAnniversary6Mod.makeID("Steward");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    private static final String IMG = SpireAnniversary6Mod.makeImagePath("monsters/Frostlands/Steward.png");
    public boolean firstMove = true, spawned = false;
    private static final byte PREP_ATTACK = 1;
    private static final byte ATTACK = 2;
    private static final int HP = 48, DMG = 16;
    private static final int A7_HP = 64;
    float currentX, currentY, currentHB;

    public Steward() {
        this(0.0f, 0.0f);
    }

    public Steward(final float x, final float y) {
        super(Steward.NAME, ID, HP, -5.0F, 0, 336, 336, IMG, x, y);
        loadAnimation(modID + "Resources/images/monsters/Frostlands/Steward.atlas", modID + "Resources/images/monsters/Frostlands/Steward.json", 1.0F);
        state.setAnimation(0, "idle", true);
        currentX = x;
        currentY = y;
        currentHB = 336;
        type = EnemyType.NORMAL;
        int hp = HP;
        int dmg = DMG;
        if (AbstractDungeon.ascensionLevel >= 2)
            dmg++;
        if (AbstractDungeon.ascensionLevel >= 7)
            hp += 12;
        if (AbstractDungeon.ascensionLevel >= 17)
            dmg += 3;
        setHp(hp, hp + 8);
        damage.add(new DamageInfo(this, dmg));
    }

    public Steward(int HP, final float x, final float y) {
        super(Steward.NAME, ID, HP, -5.0F, 0,
                (336 * ((float) (HP + 64) /(float)((AbstractDungeon.ascensionLevel >= 7)?(A7_HP + 64):(HP + 64)))),
                (336 * ((float) (HP + 64) /(float)((AbstractDungeon.ascensionLevel >= 7)?(A7_HP + 64):(HP + 64)))),
                IMG, x, y);
        currentX = x;
        currentY = y;
        currentHB = ((float) (HP + 64) /(float)((AbstractDungeon.ascensionLevel >= 7)?(A7_HP + 64):(HP + 64)));
        loadAnimation(modID + "Resources/images/monsters/Frostlands/Steward.atlas", modID + "Resources/images/monsters/Frostlands/Steward.json",
                 1 / ((float) (HP + 64) /(float)((AbstractDungeon.ascensionLevel >= 7)?(A7_HP + 64):(HP + 64))));
        state.setAnimation(0, "idle", true);
        firstMove = false;
        spawned = true;
        type = EnemyType.NORMAL;
        int dmg = DMG;
        if (AbstractDungeon.ascensionLevel >= 2)
            dmg++;
        if (AbstractDungeon.ascensionLevel >= 17)
            dmg += 3;
        damage.add(new DamageInfo(this, (int)(dmg * .8f)));
        setHp(HP, HP);
    }


    @Override
    public void takeTurn() {
        if (firstMove)
        {
            firstMove = false;
            AbstractDungeon.effectsQueue.add(new LightFlareParticleEffect(drawX, drawY, Color.WHITE));
            Wiz.atb(new AnimateSlowAttackAction(this));
            Wiz.atb(new ApplyPowerAction(AbstractDungeon.player, this, new FrigidPower(AbstractDungeon.player, 1)));
        }
        else
        {
            AbstractDungeon.effectsQueue.add(new LightFlareParticleEffect(drawX, drawY, Color.WHITE));
            Wiz.atb(new AnimateSlowAttackAction(this));
            Wiz.atb(new DamageAction(AbstractDungeon.player, damage.get(damage.size()-1), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
            if(!spawned)
            {
                Wiz.atb(new CannotLoseAction());
                Wiz.atb(new AnimateShakeAction(this, 1.0F, 0.1F));
                Wiz.atb(new HideHealthBarAction(this));
                Wiz.atb(new SuicideAction(this, false));
                Wiz.atb(new WaitAction(1.0F));
                //Wiz.atb(new SFXAction("SLIME_SPLIT"));
                Wiz.atb(new SpawnMonsterAction(new Steward(currentHealth, currentX - currentHB / 2, currentY), false));
                Wiz.atb(new SpawnMonsterAction(new Steward(currentHealth, currentX + currentHB / 2, currentY), false));
                Wiz.atb(new CanLoseAction());
                //this.setMove(SPLIT_NAME, (byte)3, Intent.UNKNOWN);
            }
        }

        Wiz.atb(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (firstMove)
            setMove(MOVES[0], PREP_ATTACK, Intent.DEBUFF, 0);
        else if(!spawned)
            setMove(MOVES[1], ATTACK, Intent.ATTACK_BUFF, damage.get(damage.size()-1).base);
        else
            setMove(MOVES[2], ATTACK, Intent.ATTACK, damage.get(damage.size()-1).base);
    }
}