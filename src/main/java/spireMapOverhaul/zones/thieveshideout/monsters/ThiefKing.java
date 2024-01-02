package spireMapOverhaul.zones.thieveshideout.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
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
    private static final String IMG = SpireAnniversary6Mod.makeImagePath("monsters/WarGolem/WarGolem.png"); //TODO: Replace with appropriate recolor
    private boolean firstMove = true;
    private static final byte STEAL_VITALITY_DEBUFF = 1;
    private static final byte STAB_ATTACK = 2;
    private static final byte STEAL_EXPERIENCE_ATTACK = 3;
    private static final byte THE_REVEAL_BUFF = 4;
    private static final byte BLADE_FLURRY_ATTACK = 5;
    private static final int STEAL_VITALITY_STATS = 1;
    private static final int STAB_DAMAGE = 12;
    private static final int A3_STAB_DAMAGE = 14;
    private static final int STEAL_EXPERIENCE_DAMAGE = 9;
    private static final int A3_STEAL_EXPERIENCE_DAMAGE = 10;
    private static final int STEAL_EXPERIENCE_CARDS = 1;
    private static final int A18_STEAL_EXPERIENCE_CARDS = 2;
    private static final int THE_REVEAL_BUFFS = 3;
    private static final int A18_THE_REVEAL_BUFFS = 4;
    private static final int THE_REVEAL_BLOCK = 20;
    private static final int A8_THE_REVEAL_BLOCK = 30;
    private static final int BLADE_FLURRY_DAMAGE = 0;
    private static final int A3_BLADE_FLURRY_DAMAGE = 1;
    private static final int BLADE_FLURRY_HITS = 5;
    private static final int HP = 95;
    private static final int A8_HP = 100;
    private int stabDamage;
    private int stealExperienceDamage;
    private int stealExperienceCards;
    private int theRevealBuffs;
    private int theRevealBlock;
    private int bladeFlurryDamage;

    public ThiefKing() {
        this(0.0f, 0.0f);
    }

    public ThiefKing(final float x, final float y) {
        super(NAME, ID, HP, -5.0F, 0, 230.0f, 300.0f, IMG, x, y);
        this.type = EnemyType.ELITE;
        if (AbstractDungeon.ascensionLevel >= 8) {
            this.setHp(A8_HP);
            this.theRevealBlock = A8_THE_REVEAL_BLOCK;
        } else {
            this.setHp(HP);
            this.theRevealBlock = THE_REVEAL_BLOCK;
        }

        if (AbstractDungeon.ascensionLevel >= 3) {
            this.stabDamage = A3_STAB_DAMAGE;
            this.stealExperienceDamage = A3_STEAL_EXPERIENCE_DAMAGE;
            this.bladeFlurryDamage = A3_BLADE_FLURRY_DAMAGE;
        } else {
            this.stabDamage = STAB_DAMAGE;
            this.stealExperienceDamage = STEAL_EXPERIENCE_DAMAGE;
            this.bladeFlurryDamage = BLADE_FLURRY_DAMAGE;
        }
        this.damage.add(new DamageInfo(this, this.stabDamage));
        this.damage.add(new DamageInfo(this, this.stealExperienceDamage));
        this.damage.add(new DamageInfo(this, this.bladeFlurryDamage));

        if (AbstractDungeon.ascensionLevel >= 18) {
            this.stealExperienceCards = A18_STEAL_EXPERIENCE_CARDS;
            this.theRevealBuffs = A18_THE_REVEAL_BUFFS;
        } else {
            this.stealExperienceCards = STEAL_EXPERIENCE_CARDS;
            this.theRevealBuffs = THE_REVEAL_BUFFS;
        }
    }

    @Override
    public void takeTurn() {
        if (this.firstMove) {
            this.firstMove = false;
        }
        switch (this.nextMove) {
            case STEAL_VITALITY_DEBUFF: {
                this.addToBot(new AnimateSlowAttackAction(this));
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new StrengthPower(AbstractDungeon.player, -STEAL_VITALITY_STATS)));
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new DexterityPower(AbstractDungeon.player, -STEAL_VITALITY_STATS)));
                if (!AbstractDungeon.player.orbs.isEmpty()) {
                    this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new FocusPower(AbstractDungeon.player, -STEAL_VITALITY_STATS)));
                }
                break;
            }
            case STAB_ATTACK: {
                this.addToBot(new AnimateFastAttackAction(this));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                break;
            }
            case STEAL_EXPERIENCE_ATTACK: {
                this.addToBot(new AnimateSlowAttackAction(this));
                this.addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
                this.addToBot(new AbstractGameAction() {
                    @Override
                    public void update() {
                        ArrayList<AbstractCard> cards = AbstractDungeon.player.drawPile.group.stream()
                                .filter(ThiefKing::isValidToSteal)
                                .sorted(Comparator.comparingInt(c -> cardRarityToInt(c.rarity)))
                                .collect(Collectors.toCollection(ArrayList::new));
                        ArrayList<AbstractCard> cardsToRemove = new ArrayList<>();
                        if (cards.size() <= ThiefKing.this.stealExperienceCards) {
                            cardsToRemove.addAll(cards);
                        }
                        else if (cards.get(0).rarity == cards.get(1).rarity) {
                            AbstractCard.CardRarity rarity = cards.get(0).rarity;
                            cards.removeIf(c -> c.rarity != rarity);
                            Collections.shuffle(cards);
                            cardsToRemove.add(cards.get(0));
                            cardsToRemove.add(cards.get(1));
                        }
                        else {
                            cardsToRemove.add(cards.get(0));
                            AbstractCard.CardRarity rarity = cards.get(1).rarity;
                            cards.removeIf(c -> c.rarity != rarity);
                            if (cards.size() > 1) {
                                Collections.shuffle(cards);
                            }
                            cardsToRemove.add(cards.get(0));
                        }
                        for (AbstractCard c : cardsToRemove) {
                            AbstractDungeon.player.drawPile.removeCard(c);
                            this.addToBot(new VFXAction(new PurgeCardEffect(c)));
                        }
                        this.isDone = true;
                    }
                });
                break;
            }
            case THE_REVEAL_BUFF:
                this.addToBot(new TalkAction(this, DIALOG[0], 0.5F, 2.0F));
                this.addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, this.theRevealBuffs)));
                this.addToBot(new ApplyPowerAction(this, this, new RitualPower(this, this.theRevealBuffs, false)));
                this.addToBot(new ApplyPowerAction(this, this, new MetallicizePower(this, this.theRevealBuffs)));
                this.addToBot(new GainBlockAction(this, this.theRevealBlock));
                this.addToBot(new ApplyPowerAction(this, this, new BarricadePower(this)));
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
        } else if (this.lastMove(STEAL_VITALITY_DEBUFF)) {
            this.setMove(MOVES[1], STAB_ATTACK, Intent.ATTACK, this.stabDamage);
        } else if (this.lastMove(STAB_ATTACK)) {
            this.setMove(MOVES[2], STEAL_EXPERIENCE_ATTACK, Intent.ATTACK_DEBUFF, this.stealExperienceDamage);
        } else if (this.lastMove(STEAL_EXPERIENCE_ATTACK)) {
            this.setMove(MOVES[3], THE_REVEAL_BUFF, Intent.DEFEND_BUFF);
        } else {
            this.setMove(MOVES[4], BLADE_FLURRY_ATTACK, Intent.ATTACK, this.bladeFlurryDamage, BLADE_FLURRY_HITS, true);
        }
    }
}
