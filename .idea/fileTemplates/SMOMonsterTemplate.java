#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "") package ${PACKAGE_NAME};#end
#parse("File Header.java")

#if (${ATLAS_NAME_OPTIONAL} != "" && ${SKELETON_NAME_OPTIONAL} != "")
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
#end
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractSMOMonster;

public class ${NAME} extends AbstractSMOMonster {
    public static final String ID = SpireAnniversary6Mod.makeID("${NAME}");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final int MAX_HEALTH = ${MAX_HP};
    private static final String IMG = SpireAnniversary6Mod.makeImagePath("monsters/" + "${BIOME_ID}" + "/" + "${IMG_NAME}");
    #if (${ATLAS_NAME_OPTIONAL} != "" && ${SKELETON_NAME_OPTIONAL} != "")
    private static final String ATLAS = SpireAnniversary6Mod.makeImagePath("monsters/" + "${BIOME_ID}" + "/" + "${ATLAS_NAME_OPTIONAL}");
    private static final String SKELETON = SpireAnniversary6Mod.makeImagePath("monsters/" + "${BIOME_ID}" + "/" + "${SKELETON_NAME_OPTIONAL}");
    #end
    public ${NAME}() {
        super(NAME, ID, MAX_HEALTH, ${HB_X}, ${HB_Y}, ${HB_W}, ${HB_H}, IMG, ${OFFSET_X}, ${OFFSET_Y});
        #if (${ATLAS_NAME_OPTIONAL} != "" && ${SKELETON_NAME_OPTIONAL} != "")
        this.loadAnimation(ATLAS, SKELETON, 1.00f);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "${IDLE_ANIM_OPTIONAL}", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        #end
    }
    
    @Override
    public void takeTurn() {
    
    }
    
    @Override
    protected void getMove(int i) {
    
    }
}
