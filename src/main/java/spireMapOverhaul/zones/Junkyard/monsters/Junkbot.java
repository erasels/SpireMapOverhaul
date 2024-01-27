package spireMapOverhaul.zones.Junkyard.monsters;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.vfx.combat.PowerBuffEffect;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.util.TexLoader;
import spireMapOverhaul.zones.Junkyard.actions.DeactivateAction;
import spireMapOverhaul.zones.Junkyard.actions.GrabCardAction;
import spireMapOverhaul.zones.Junkyard.actions.RemoveHeldCardAction;

import java.util.ArrayList;

public class Junkbot extends CustomMonster {
    public static final String ID = SpireAnniversary6Mod.makeID("Junkbot");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    private static final String IMG = SpireAnniversary6Mod.makeImagePath("monsters/Junkyard/Junkbot/Junkbot.png");
    public static final String IMG_INACTIVE = SpireAnniversary6Mod.makeImagePath("monsters/Junkyard/Junkbot/Junkbot_inactive.png");
    private boolean firstMove = true;
    private static final byte GRAB_MOVE = 1;
    private static final byte REBOOT_MOVE = 2;
    private static final int GRAB_DAMAGE = 7;
    private static final int A2_GRAB_DAMAGE = 8;
    private static final int HP_MIN = 21;
    private static final int HP_MAX = 27;

    private static final int A7_HP_MIN = 26;
    private static final int A7_HP_MAX = 31;
    private int grabDamage;
    public boolean isActivated = true;
    private int chanceToDeactivate = 20;
    public ArrayList<AbstractCard> cardsToPreview = new ArrayList<>();
    private CardGroup g = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);


    public Junkbot() {
        this(0.0f, 0.0f);
    }

    public Junkbot(final float x, final float y) {
        super(Junkbot.NAME, ID, HP_MAX, -5.0F, 0, 155.0f, 150.0f, IMG, x, y);
        this.type = EnemyType.NORMAL;
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(A7_HP_MIN, A7_HP_MAX);
        } else {
            this.setHp(HP_MIN, HP_MAX);
        }

        if (AbstractDungeon.ascensionLevel >= 2) {
            this.grabDamage = A2_GRAB_DAMAGE;
        } else {
            this.grabDamage = GRAB_DAMAGE;
        }
        this.damage.add(new DamageInfo(this, this.grabDamage));
    }

    @Override
    public void usePreBattleAction() {
        int rand = AbstractDungeon.cardRng.random(0, 100);
        isActivated = (rand <= 60);
        if (!isActivated){
            setImage(TexLoader.getTexture(IMG_INACTIVE));
            addToBot(new GrabCardAction(this, new Wound()));
        }
        getMove(0);
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case GRAB_MOVE:
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                AbstractDungeon.actionManager.addToBottom(new GrabCardAction(this));
                if(AbstractDungeon.ascensionLevel >= 17) {
                    AbstractDungeon.actionManager.addToBottom(new GrabCardAction(this));
                }
                AbstractDungeon.actionManager.addToBottom(new DeactivateAction(this, chanceToDeactivate));
                break;
            case REBOOT_MOVE:
                isActivated = true;
                this.img = TexLoader.getTexture(IMG);
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new PowerBuffEffect(this.hb.cX, this.hb.cY, "Rebooted")));
                AbstractDungeon.actionManager.addToBottom(new RemoveHeldCardAction(this, cardsToPreview.get(0)));
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (isActivated) {
            this.setMove(MOVES[0], GRAB_MOVE, Intent.ATTACK_DEBUFF, this.damage.get(0).base);
        } else {
            this.setMove(MOVES[1], REBOOT_MOVE, Intent.UNKNOWN);
        }
    }

    public void setDeactivateChance(int chance){
        chanceToDeactivate = chance;
    }

    public void setImage(Texture tex){
        this.img = tex;
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        if (cardsToPreview.size() != 0) {
            for (int i = 0; i < cardsToPreview.size(); i++) {
                AbstractCard c = cardsToPreview.get(i);
                float widthspacing = AbstractCard.IMG_WIDTH_S + 100.0f * Settings.scale;
                c.target_x = c.current_x = this.hb.cX - (widthspacing/6) + (i * (widthspacing * 0.175f));
                c.target_y = c.current_y = this.hb.cY + (widthspacing/3) + (AbstractCard.IMG_HEIGHT_S * 0.25f);
                c.drawScale = c.targetDrawScale = c.hb.hovered ? 0.6f : 0.15f;
                c.render(sb);
            }
        }
    }

    public void removeHeldCard(AbstractCard card){
        cardsToPreview.remove(card);
    }

    @Override
    public void update() {
        super.update();
        for (AbstractCard c : cardsToPreview) {
                c.update();
                c.updateHoverLogic();
        }
    }
}