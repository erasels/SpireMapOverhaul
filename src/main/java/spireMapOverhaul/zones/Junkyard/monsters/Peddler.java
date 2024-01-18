package spireMapOverhaul.zones.Junkyard.monsters;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.FlickCoinEffect;
import com.megacrit.cardcrawl.vfx.combat.SmokeBombEffect;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.Junkyard.cards.Bargain;

import java.util.ArrayList;

public class Peddler extends CustomMonster {
    public static final String ID = SpireAnniversary6Mod.makeID("Peddler");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    private static final String IMG = SpireAnniversary6Mod.makeImagePath("monsters/Junkyard/Peddler/Peddler.png");
    private boolean firstMove = true;
    private static final byte FREE_SAMPLES = 1;
    private static final byte RUMMAGE = 2;
    private static final byte HAGGLE = 3;
    private static final byte PAYUP = 4;
    public static final byte ESCAPE = 5;

    private static final int RUMMAGE_DAMAGE = 6;
    private static final int RUMMAGE_BLOCK = 10;
    private static final int A2_RUMMAGE_DAMAGE = 9;
    private static final int A7_RUMMAGE_BLOCK = 12;

    private static final int PAYUP_DAMAGE = 12;
    private static final int A2_PAYUP_DAMAGE = 15;

    private static final int HP_MIN = 72;
    private static final int HP_MAX = 76;
    private static final int A7_HP_MIN = 75;
    private static final int A7_HP_MAX = 80;

    private static final int GOLDMIN = 78;
    private static final int GOLDMAX = 90;
    private static final int A16_GOLDMIN = 88;
    private static final int A16_GOLDMAX = 100;

    private int goldCost;
    private int rummageDamage;
    private int payupDamage;
    private int rummageBlock;

    private int move = 1;

    public ArrayList<AbstractCard> wares = new ArrayList<AbstractCard>();


    public Peddler() {
        this(0.0f, 0.0f);
    }

    public Peddler(final float x, final float y) {
        super(Peddler.NAME, ID, HP_MAX, -5.0F, 0, 155.0f, 250.0f, IMG, x, y);
        this.type = EnemyType.NORMAL;
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(A7_HP_MIN, A7_HP_MAX);
            this.rummageBlock = A7_RUMMAGE_BLOCK;
        } else {
            this.setHp(HP_MIN, HP_MAX);
            this.rummageBlock = RUMMAGE_BLOCK;
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            this.rummageDamage = A2_RUMMAGE_DAMAGE;
            this.payupDamage = A2_PAYUP_DAMAGE;
        } else {
            this.rummageDamage = RUMMAGE_DAMAGE;
            this.payupDamage = PAYUP_DAMAGE;
        }
        if (AbstractDungeon.ascensionLevel >= 16){
            goldCost = AbstractDungeon.cardRandomRng.random(A16_GOLDMIN, A16_GOLDMAX);
        }
        else {
            goldCost = AbstractDungeon.cardRandomRng.random(GOLDMIN, GOLDMAX);
        }
        this.damage.add(new DamageInfo(this, this.rummageDamage));
        this.damage.add(new DamageInfo(this, this.payupDamage));
    }

    public void GiveBadSamples(){
        for(int i = 0; i < 4; ++i) {
            AbstractCard card = Wiz.returnTrulyRandomRarityCardInCombat(AbstractCard.CardRarity.COMMON);
            wares.add(card);
            this.addToBot(new MakeTempCardInDrawPileAction(card, 1, true, true));
        }
        AbstractCard card = Wiz.returnTrulyRandomRarityCardInCombat(AbstractCard.CardRarity.CURSE);
        wares.add(card);
        this.addToBot(new MakeTempCardInDrawPileAction(card, 1, true, true));
    }

    public void GiveSamples(){
        for(int i = 0; i < 4; ++i) {
            AbstractCard card = Wiz.returnTrulyRandomRarityCardInCombat(AbstractCard.CardRarity.COMMON);
            this.addToBot(new MakeTempCardInDrawPileAction(card, 1, true, true));
        }
        AbstractCard card = Wiz.returnTrulyRandomRarityCardInCombat(AbstractCard.CardRarity.UNCOMMON);
        wares.add(card);
        this.addToBot(new MakeTempCardInDrawPileAction(card, 1, true, true));
    }

    public float randomOffset(){
        return (float)(Math.random() * 20f * Settings.scale) - (10f * Settings.scale);
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case FREE_SAMPLES:
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[0], 1.0f, 2.0f));
                if (AbstractDungeon.ascensionLevel >= 17) {
                    GiveBadSamples();
                } else {
                    GiveSamples();
                }
                break;
            case RUMMAGE:
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new FlickCoinEffect(hb.cX, hb.cY, AbstractDungeon.player.hb.cX + randomOffset(), AbstractDungeon.player.hb.cY + randomOffset())));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new FlickCoinEffect(hb.cX, hb.cY, AbstractDungeon.player.hb.cX + randomOffset(), AbstractDungeon.player.hb.cY + randomOffset())));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new FlickCoinEffect(hb.cX, hb.cY, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY)));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, rummageBlock));
                break;
            case HAGGLE:
                int decksize = (AbstractDungeon.player.drawPile.group.size() + AbstractDungeon.player.hand.size() + AbstractDungeon.player.discardPile.size());
                int strgain = decksize/3;
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[1] + decksize + DIALOG[2], 1.0f, 2.0f));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, strgain)));
                Bargain bargain = new Bargain(goldCost);
                bargain.setCost(goldCost);
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(bargain));
                break;
            case PAYUP:
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[3], 1.0f, 2.0f));
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                playSfx();
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                break;
            case ESCAPE:
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[4], 2.0f, 3.0f));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new SmokeBombEffect(this.hb.cX, this.hb.cY)));
                AbstractDungeon.actionManager.addToBottom(new EscapeAction(this));
                AbstractDungeon.actionManager.addToBottom(new SetMoveAction(this, (byte) 3, Intent.ESCAPE));
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    private void playSfx() {
        int roll = MathUtils.random(2);
        if (roll == 0) {
            AbstractDungeon.actionManager.addToBottom(new SFXAction("VO_LOOTER_1A"));
        } else if (roll == 1) {
            AbstractDungeon.actionManager.addToBottom(new SFXAction("VO_LOOTER_1B"));
        } else {
            AbstractDungeon.actionManager.addToBottom(new SFXAction("VO_LOOTER_1C"));
        }

    }

    public void doBusiness(){
        this.changeState("Business");
    }

    @Override
    public void changeState(String stateName) {
        this.setMove(ESCAPE, Intent.ESCAPE);
        this.createIntent();
    }

    @Override
    protected void getMove(final int num) {
        switch (move){
            case 1:
                this.setMove(FREE_SAMPLES, Intent.UNKNOWN);
                break;
            case 2:
                this.setMove(RUMMAGE, Intent.ATTACK_DEFEND, this.damage.get(0).base);
                break;
            case 3:
                this.setMove(HAGGLE, Intent.BUFF);
                break;
            case 4:
                this.setMove(PAYUP, Intent.ATTACK, this.damage.get(1).base);
                break;
            case 5:
                this.setMove(ESCAPE, Intent.ESCAPE);
                break;
        }
        move++;
        if (move > 4){
            move = 1;
        }
    }
}