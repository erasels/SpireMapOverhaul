package spireMapOverhaul.zones.thieveshideout.monsters;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.*;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.megacrit.cardcrawl.vfx.combat.HeartBuffEffect;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;
import spireMapOverhaul.SpireAnniversary6Mod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

public class ThiefKing extends CustomMonster {
    public static final String ID = SpireAnniversary6Mod.makeID("ThiefKing");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    private boolean firstMove = true;
    private int stealExperienceCount = 0;
    private static final byte STEAL_VITALITY_DEBUFF = 1;
    private static final byte STEAL_EXPERIENCE_ATTACK = 2;
    private static final byte THE_REVEAL_BUFF = 3;
    private static final byte BLADE_FLURRY_ATTACK = 4;
    private static final int STEAL_VITALITY_STATS = 1;
    private static final int STEAL_EXPERIENCE_DAMAGE = 9;
    private static final int A3_STEAL_EXPERIENCE_DAMAGE = 10;
    private static final int THE_REVEAL_BUFFS = 3;
    private static final int A18_THE_REVEAL_BUFFS = 4;
    private static final int THE_REVEAL_BLOCK = 20;
    private static final int A8_THE_REVEAL_BLOCK = 30;
    private static final int BLADE_FLURRY_DAMAGE = 0;
    private static final int A3_BLADE_FLURRY_DAMAGE = 1;
    private static final int BLADE_FLURRY_HITS = 5;
    private static final int HP = 95;
    private static final int A8_HP = 100;
    private int stealExperienceDamage;
    private int theRevealBuffs;
    private int theRevealBlock;
    private int bladeFlurryDamage;

    public ThiefKing() {
        this(0.0f, 0.0f);
    }

    public ThiefKing(final float x, final float y) {
        super(NAME, ID, HP, -10.0F, -7.0F, 180.0F, 285.0F, null, x, y);
        this.type = EnemyType.ELITE;
        if (AbstractDungeon.ascensionLevel >= 8) {
            this.setHp(A8_HP);
            this.theRevealBlock = A8_THE_REVEAL_BLOCK;
        } else {
            this.setHp(HP);
            this.theRevealBlock = THE_REVEAL_BLOCK;
        }

        if (AbstractDungeon.ascensionLevel >= 3) {
            this.stealExperienceDamage = A3_STEAL_EXPERIENCE_DAMAGE;
            this.bladeFlurryDamage = A3_BLADE_FLURRY_DAMAGE;
        } else {
            this.stealExperienceDamage = STEAL_EXPERIENCE_DAMAGE;
            this.bladeFlurryDamage = BLADE_FLURRY_DAMAGE;
        }
        this.damage.add(new DamageInfo(this, this.stealExperienceDamage));
        this.damage.add(new DamageInfo(this, this.bladeFlurryDamage));

        if (AbstractDungeon.ascensionLevel >= 18) {
            this.theRevealBuffs = A18_THE_REVEAL_BUFFS;
        } else {
            this.theRevealBuffs = THE_REVEAL_BUFFS;
        }

        this.loadAnimation(SpireAnniversary6Mod.makeImagePath("monsters/ThiefKing/skeleton.atlas"), SpireAnniversary6Mod.makeImagePath("monsters/ThiefKing/skeleton.json"), 1.0F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        this.stateData.setMix("Hit", "Idle", 0.2F);
        this.state.setTimeScale(0.8F);
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        switch (this.nextMove) {
            case STEAL_VITALITY_DEBUFF:
                this.addToBot(new TalkAction(this, DIALOG[0], 0.5F, 2.0F));
                this.addToBot(new AnimateShakeAction(this, 0.5f, 0.1f));
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new StrengthPower(AbstractDungeon.player, -STEAL_VITALITY_STATS)));
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new DexterityPower(AbstractDungeon.player, -STEAL_VITALITY_STATS)));
                if (!AbstractDungeon.player.orbs.isEmpty()) {
                    this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new FocusPower(AbstractDungeon.player, -STEAL_VITALITY_STATS)));
                }
                break;
            case STEAL_EXPERIENCE_ATTACK:
                this.stealExperienceCount++;
                this.addToBot(new TalkAction(this, this.stealExperienceCount == 1 ? DIALOG[1] : DIALOG[2], 0.5F, 2.0F));
                this.addToBot(new AnimateSlowAttackAction(this));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
                this.addToBot(new AbstractGameAction() {
                    @Override
                    public void update() {
                        ArrayList<AbstractCard> cards = AbstractDungeon.player.drawPile.group.stream()
                                .filter(ThiefKing::isValidToSteal)
                                .sorted(Comparator.comparingInt(c -> cardRarityToInt(c.rarity)))
                                .collect(Collectors.toCollection(ArrayList::new));
                        AbstractCard cardToRemove;
                        if (cards.isEmpty()) {
                            cardToRemove = null;
                        }
                        else if (cards.size() == 1 || (cards.get(0).rarity != cards.get(1).rarity)) {
                            cardToRemove = cards.get(0);
                        }
                        else {
                            AbstractCard.CardRarity rarity = cards.get(0).rarity;
                            cards.removeIf(c -> c.rarity != rarity);
                            Collections.shuffle(cards);
                            cardToRemove = cards.get(0);
                        }
                        if (cardToRemove != null) {
                            AbstractDungeon.player.drawPile.removeCard(cardToRemove);
                            AbstractDungeon.effectsQueue.add(new PurgeCardEffect(cardToRemove));
                        }
                        this.isDone = true;
                    }
                });
                break;
            case THE_REVEAL_BUFF:
                this.addToBot(new ShoutAction(this, DIALOG[3], 0.5F, 2.0F));
                this.addToBot(new VFXAction(this, new InflameEffect(this), 0.25F));
                this.addToBot(new VFXAction(new HeartBuffEffect(this.hb.cX, this.hb.cY)));
                this.addToBot(new VFXAction(this, new InflameEffect(this), 0.25F));
                this.addToBot(new RemoveDebuffsAction(this));
                this.addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, this.theRevealBuffs)));
                this.addToBot(new ApplyPowerAction(this, this, new RitualPower(this, this.theRevealBuffs, false)));
                this.addToBot(new ApplyPowerAction(this, this, new MetallicizePower(this, this.theRevealBuffs)));
                this.addToBot(new ApplyPowerAction(this, this, new BarricadePower(this)));
                this.addToBot(new GainBlockAction(this, this.theRevealBlock));
                break;
            case BLADE_FLURRY_ATTACK:
                for (int i = 0; i < BLADE_FLURRY_HITS; i++) {
                    this.addToBot(new AnimateFastAttackAction(this));
                    this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(2), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                }
                break;
        }
        this.addToBot(new RollMoveAction(this));
    }

    private static boolean isValidToSteal(AbstractCard c) {
        return c.type != AbstractCard.CardType.STATUS && c.type != AbstractCard.CardType.CURSE && cardRarityToInt(c.rarity) < 5;
    }

    private static Integer cardRarityToInt(AbstractCard.CardRarity rarity) {
        switch (rarity) {
            case RARE:
                return 0;
            case UNCOMMON:
                return 1;
            case COMMON:
                return 2;
            case BASIC:
                return 3;
            case SPECIAL:
                return 4;
            case CURSE:
                return 5;
            default:
                return 6;
        }
    }

    @Override
    protected void getMove(final int num) {
        if (this.firstMove) {
            this.setMove(MOVES[0], STEAL_VITALITY_DEBUFF, Intent.DEBUFF);
        } else if (this.lastMove(STEAL_VITALITY_DEBUFF) || (this.lastMove(STEAL_EXPERIENCE_ATTACK) && this.lastMoveBefore(STEAL_VITALITY_DEBUFF))) {
            this.setMove(MOVES[1], STEAL_EXPERIENCE_ATTACK, Intent.ATTACK_DEBUFF, this.stealExperienceDamage);
        } else if (this.lastMove(STEAL_EXPERIENCE_ATTACK)) {
            this.setMove(MOVES[2], THE_REVEAL_BUFF, Intent.DEFEND_BUFF);
        } else {
            this.setMove(MOVES[3], BLADE_FLURRY_ATTACK, Intent.ATTACK, this.bladeFlurryDamage, BLADE_FLURRY_HITS, true);
        }
    }

    @Override
    public void changeState(String key) {
        if (key != null && key.equals("STAB")) {
            this.state.setAnimation(0, "Attack", false);
            this.state.addAnimation(0, "Idle", true, 0.0F);
        }
    }

    @Override
    public void damage(DamageInfo info) {
        super.damage(info);
        if (info.owner != null && info.type != DamageInfo.DamageType.THORNS && info.output > 0) {
            this.state.setAnimation(0, "Hit", false);
            this.state.setTimeScale(0.8F);
            this.state.addAnimation(0, "Idle", true, 0.0F);
        }
    }
}
