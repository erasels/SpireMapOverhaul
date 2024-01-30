package spireMapOverhaul.zones.mycelium.monsters;

import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOMonster;

public class MutedWhaleSpawn extends AbstractSMOMonster {
    public static final String ID = SpireAnniversary6Mod.makeID("MutedWhaleSpawn");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final int MAX_HEALTH = 55;
    private static final String IMG = SpireAnniversary6Mod.makeImagePath("monsters/" + "Mycelium" + "/" + "skeleton.png");
    private static final String ATLAS = SpireAnniversary6Mod.makeImagePath("monsters/" + "Mycelium" + "/" + "skeleton.atlas");
    private static final String SKELETON = SpireAnniversary6Mod.makeImagePath("monsters/" + "Mycelium" + "/" + "skeleton.json");
    
    public MutedWhaleSpawn() {
        super(NAME, ID, MAX_HEALTH, 0, 0, 200, 150, IMG, 0, 0);
        this.loadAnimation(ATLAS, SKELETON, 1.00f);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "animation0", true);
        e.setTime(e.getEndTime() * MathUtils.random());
    }
    
    @Override
    public void takeTurn() {
    
    }
    
    @Override
    protected void getMove(int i) {
    
    }
}
