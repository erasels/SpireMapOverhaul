package spireMapOverhaul.zones.gremlincamp.monsters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.actions.common.AllEnemyApplyPowerAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.SpeechBubble;
import com.megacrit.cardcrawl.vfx.combat.HealEffect;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOMonster;
import spireMapOverhaul.util.TexLoader;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zones.gremlincamp.EmberEffect;
import spireMapOverhaul.zones.gremlincamp.GremlinCamp;
import spireMapOverhaul.zones.gremlincamp.PlayerPoisonPower;
import spireMapOverhaul.zones.gremlincamp.SmokeEffect;

import java.util.ArrayList;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;
import static spireMapOverhaul.SpireAnniversary6Mod.makeMonsterPath;

public class GremlinCook extends AbstractSMOMonster {
    public static final String ID = makeID(GremlinCook.class.getSimpleName());
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    private static final Texture cauldron = TexLoader.getTexture(makeMonsterPath(GremlinCamp.ID+"/cauldron.png"));
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;

    private static final Byte ALL_BUFF = 0, ALL_BULK = 1, POISON = 2, TACKLE = 3;

    private int healAmt, strAmt, poisonAmt;
    private boolean secondMove = false;
    private float cauldron_x;

    public GremlinCook() {
        this(0, 0);
    }

    public GremlinCook(float x, float y) {
        super(NAME, ID, 50, 0, 0, 120f, 210f, SpireAnniversary6Mod.makeMonsterPath("GremlinCamp/GremlinCook.png"), x, y);
        this.type = EnemyType.ELITE;
        setHp(calcAscensionTankiness(56), calcAscensionTankiness(62));
        addMove(ALL_BUFF, Intent.BUFF);
        addMove(ALL_BULK, Intent.BUFF);
        addMove(POISON, Intent.STRONG_DEBUFF);
        addMove(TACKLE, Intent.ATTACK_DEBUFF, calcAscensionDamage(10));

        healAmt = calcAscensionSpecial(10);
        strAmt = AbstractDungeon.ascensionLevel >= 18? 3: 2;
        poisonAmt = calcAscensionSpecial(4);

        cauldron_x = hb.x;
    }

    @Override
    public void takeTurn() {
        switch (nextMove) {
            case 0:
                if(!secondMove)
                    AbstractDungeon.effectList.add(new SpeechBubble(hb.cX + dialogX, hb.cY + dialogY, 1.5F, DIALOG[0], false));
                useShakeAnimation(Settings.ACTION_DUR_MED);
                Wiz.atb(new AllEnemyApplyPowerAction(this, strAmt, (m) -> new StrengthPower(m, strAmt)));
                break;
            case 1:
                if(!secondMove)
                    AbstractDungeon.effectList.add(new SpeechBubble(hb.cX + dialogX, hb.cY + dialogY, 1.5F, DIALOG[0], false));
                useShakeAnimation(Settings.ACTION_DUR_FAST);
                Wiz.atb(new AbstractGameAction() {
                    @Override
                    public void update() {
                        isDone = true;
                        Wiz.forAllMonstersLiving(m -> {
                            if(!GremlinCook.ID.equals(m.id)) {
                                m.increaseMaxHp(healAmt, true);
                                AbstractDungeon.effectList.add(new HealEffect(m.hb.cX, m.hb.cY, healAmt));
                            }
                        });
                    }
                });
                break;
            case 2:
                useFastAttackAnimation();
                Wiz.atb(new ApplyPowerAction(AbstractDungeon.player, this, new PlayerPoisonPower(AbstractDungeon.player, poisonAmt), poisonAmt, AbstractGameAction.AttackEffect.POISON));
                break;
            case 3:
                DamageInfo info = new DamageInfo(this, moves.get(nextMove).baseDamage, DamageInfo.DamageType.NORMAL);
                info.applyPowers(this, AbstractDungeon.player);
                useSlowAttackAnimation();
                addToBot(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                Wiz.atb(new MakeTempCardInDiscardAction(new Dazed(), calcAscensionSpecial(2)));
        }

        addToBot(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i) {
        int enemies = Wiz.getEnemies().size();

        //First turn always buffs
        if(firstMove) {
            firstMove = false;
            if(enemies > 1 && i > 50) {
                setMoveShortcut(ALL_BULK, MOVES[ALL_BULK]);
            } else {
                setMoveShortcut(ALL_BUFF, MOVES[ALL_BUFF]);
            }
            return;
        }

        if(!secondMove) {
            secondMove = true;
            setMoveShortcut(POISON, MOVES[POISON]);
            return;
        }

        ArrayList<Byte> possibilities = new ArrayList<>();
        // Can buff everyone if not alone or late in combat and last move wasn't buff
        if((enemies > 1 || GameActionManager.turn > 5) && !lastMove(ALL_BUFF)) {
            possibilities.add(ALL_BUFF);
        }

        if(enemies == 3 && !lastMove(ALL_BULK)) {
            possibilities.add(ALL_BULK);
        }

        // Turn 3+ can start poisoning player if it wasn't done last turn
        if(!lastMove(POISON)) {
            possibilities.add(POISON);
        }

        // When alone or nothing else, start tackling the fucker
        if(enemies == 1 || possibilities.isEmpty()) {
            possibilities.add(TACKLE);
        }

        byte move = Wiz.getRandomItem(possibilities, AbstractDungeon.aiRng);
        setMoveShortcut(move, MOVES[move]);
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        //This will always render, even if dead or escaped
        sb.setColor(Color.WHITE);
        float w = cauldron.getWidth() * Settings.scale, h = cauldron.getHeight() * Settings.scale;
        sb.draw(cauldron, cauldron_x - w, hb.y, w, h);
    }

    private static final float CEMBER_MAX = 0.5f, CSMOKE_MAX = 0.7f;
    private float cEmber = 1f, cSmoke = 1;

    @Override
    public void update() {
        super.update();
        cEmber -= Gdx.graphics.getRawDeltaTime();
        cSmoke -= Gdx.graphics.getRawDeltaTime();

        if(cEmber <= 0) {
            cEmber = CEMBER_MAX * MathUtils.random(0.5f, 1);
            EmberEffect eff = new EmberEffect(MathUtils.random(cauldron_x - cauldron.getWidth() * Settings.scale, cauldron_x), hb.y + 10f * Settings.scale);
            AbstractDungeon.effectList.add(eff);
        }
        if(cSmoke <= 0) {
            cSmoke = CSMOKE_MAX * MathUtils.random(0.5f, 1);
            SmokeEffect eff = new SmokeEffect(MathUtils.random(cauldron_x - cauldron.getWidth() * Settings.scale, cauldron_x), hb.y + ((cauldron.getHeight() - 20) * Settings.scale));
            AbstractDungeon.effectList.add(eff);
        }
    }
}
