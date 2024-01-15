package spireMapOverhaul.zones.invasion.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.invasion.powers.HoverPower;

public class LivingStormcloud extends CustomMonster
{
    public static final String ID = SpireAnniversary6Mod.makeID("LivingStormcloud");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    private static final String IMG = SpireAnniversary6Mod.makeImagePath("monsters/Invasion/LivingStormcloud/LivingStormcloud.png");
    private boolean firstMove = true;
    private static final byte ZAP_ATTACK = 1;
    private static final byte GUST_ATTACK = 2;
    private static final byte RAIN_BUFF = 3;
    private static final int ZAP_ATTACK_DAMAGE = 3;
    private static final int ZAP_ATTACK_HITS = 2;
    private static final int A2_ZAP_ATTACK_HITS = 3;
    private static final int GUST_ATTACK_DAMAGE = 8;
    private static final int A2_GUST_ATTACK_DAMAGE = 9;
    private static final int GUST_HOVER_AMOUNT = 5;
    private static final int RAIN_HEAL = 2;
    private static final int STARTING_HOVER = 25;
    private static final int A17_STARTING_HOVER = 35;
    private static final int MAX_HOVER = 60;
    private static final int HP_MIN = 26;
    private static final int HP_MAX = 28;
    private static final int A7_HP_MIN = 27;
    private static final int A7_HP_MAX = 29;
    private int zapHits;
    private int gustDamage;
    private int startingHover;
    private int currentHover;

    public LivingStormcloud() {
        this(0.0f, 0.0f);
    }

    public LivingStormcloud(final float x, final float y) {
        super(LivingStormcloud.NAME, ID, HP_MAX, -5.0F, 0, 165.0F, 130.0F, IMG, x, y);
        this.type = EnemyType.NORMAL;
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(A7_HP_MIN, A7_HP_MAX);
        } else {
            this.setHp(HP_MIN, HP_MAX);
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            this.zapHits = A2_ZAP_ATTACK_HITS;
            this.gustDamage = A2_GUST_ATTACK_DAMAGE;
        } else {
            this.zapHits = ZAP_ATTACK_HITS;
            this.gustDamage = GUST_ATTACK_DAMAGE;
        }
        if (AbstractDungeon.ascensionLevel >= 17) {
            this.startingHover = A17_STARTING_HOVER;
        } else {
            this.startingHover = STARTING_HOVER;
        }
        this.damage.add(new DamageInfo(this, ZAP_ATTACK_DAMAGE));
        this.damage.add(new DamageInfo(this, this.gustDamage));
    }

    private void addHover(int amount) {
        int hoverToAdd = amount;
        if (this.currentHover + hoverToAdd > MAX_HOVER) {
            hoverToAdd = MAX_HOVER - this.currentHover;
        }
        if (hoverToAdd > 0) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new HoverPower(this, amount)));
        }
        this.currentHover += amount;
    }

    @Override
    public void usePreBattleAction() {
        this.addHover(this.startingHover);
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        switch (this.nextMove) {
            case ZAP_ATTACK:
                for (int i = 0; i < this.zapHits; i++) {
                    AbstractDungeon.actionManager.addToBottom(new SFXAction("ORB_LIGHTNING_EVOKE", 0.1F));
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new LightningEffect(AbstractDungeon.player.drawX, AbstractDungeon.player.drawY), Settings.FAST_MODE ? 0.0F : 0.1F));
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.NONE));
                }
                break;
            case GUST_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                this.addHover(GUST_HOVER_AMOUNT);
                break;
            case RAIN_BUFF:
                for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                    if (m == this || !m.isDying) {
                        AbstractDungeon.actionManager.addToBottom(new HealAction(m, this, RAIN_HEAL));
                    }
                }
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        int move;
        if (this.firstMove) {
            move = num < 60 ? ZAP_ATTACK : RAIN_BUFF;
        }
        else {
            if (this.lastMove(RAIN_BUFF)) {
                move = num < 70 ? ZAP_ATTACK : GUST_ATTACK;
            }
            else if (this.lastMove(GUST_ATTACK)) {
                move = num < 60 ? ZAP_ATTACK : RAIN_BUFF;
            }
            else if (this.lastMove(ZAP_ATTACK) && this.lastMoveBefore(ZAP_ATTACK)) {
                move = num < 60 ? RAIN_BUFF : GUST_ATTACK;
            }
            else {
                move = num < 30 ? ZAP_ATTACK : num < 70 ? RAIN_BUFF : GUST_ATTACK;
            }
        }
        switch (move) {
            case ZAP_ATTACK:
                this.setMove(MOVES[0], ZAP_ATTACK, Intent.ATTACK, ZAP_ATTACK_DAMAGE, this.zapHits, true);
                break;
            case GUST_ATTACK:
                this.setMove(MOVES[1], GUST_ATTACK, Intent.ATTACK_BUFF, this.gustDamage);
                break;
            case RAIN_BUFF:
                this.setMove(MOVES[2], RAIN_BUFF, Intent.BUFF);
                break;
        }
    }
}