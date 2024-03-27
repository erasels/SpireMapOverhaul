package spireMapOverhaul.zones.heavenlyClouds;

import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.Byrd;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.util.TexLoader;
import spireMapOverhaul.util.Wiz;
import spireMapOverhaul.zoneInterfaces.CombatModifyingZone;
import spireMapOverhaul.zoneInterfaces.RenderableZone;
import spireMapOverhaul.zoneInterfaces.RewardModifyingZone;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static spireMapOverhaul.util.Wiz.atb;

public class HeavenlyCloudsZone extends AbstractZone implements CombatModifyingZone, RenderableZone, RewardModifyingZone {
    public static final String ID = "HeavenlyClouds";
    private static final float HEALTH_MODIFIER = 0.70f;
    private Texture Cloud1 = TexLoader.getTexture(SpireAnniversary6Mod.makeImagePath("backgrounds/heavenlyClouds/Cloud1.png"));
    private Texture Cloud2 = TexLoader.getTexture(SpireAnniversary6Mod.makeImagePath("backgrounds/heavenlyClouds/Cloud2.png"));
    private Texture Cloud3 = TexLoader.getTexture(SpireAnniversary6Mod.makeImagePath("backgrounds/heavenlyClouds/Cloud3.png"));
    private Texture Cloud4 = TexLoader.getTexture(SpireAnniversary6Mod.makeImagePath("backgrounds/heavenlyClouds/Cloud4.png"));
    private Texture Cloud5 = TexLoader.getTexture(SpireAnniversary6Mod.makeImagePath("backgrounds/heavenlyClouds/Cloud5.png"));
    private Texture RainbowCloud = TexLoader.getTexture(SpireAnniversary6Mod.makeImagePath("backgrounds/heavenlyClouds/RainbowCloud.png"));
    private Texture FaceCloud = TexLoader.getTexture(SpireAnniversary6Mod.makeImagePath("backgrounds/heavenlyClouds/CloudWithAFace.png"));
    private List<Cloud> clouds = new ArrayList<>();
    private Texture[] specialClouds = {RainbowCloud, FaceCloud}; // Array to store special cloud textures
    private float cloudSpawnTimer = 0;
    private float cloudSpawnInterval = 2.1f; // Adjust this value to change the cloud spawn frequency
    private float cloudMinY = 700;
    private float cloudMaxY = Settings.HEIGHT;
    private int initialCloudCount = 12; // Number of clouds to spawn initially
    private boolean initialCloudSpawned = false;

    public HeavenlyCloudsZone() {
        super(ID, Icons.MONSTER);
        this.width = 2;
        this.maxWidth = 3;
        this.height = 3;
    }

    @Override
    public AbstractZone copy() {
        return new HeavenlyCloudsZone();
    }

    @Override
    public Color getColor() {
        return Color.SKY.cpy();
    }

    @Override
    public boolean canSpawn() {
        // Don't spawn in Act 3
        return this.isAct(1) || this.isAct(2);
    }

    @Override
    public void atBattleStart() {
        UIStrings uistrings = CardCrawlGame.languagePack.getUIString(SpireAnniversary6Mod.makeID("ByrdTalk"));
        String[] TEXT = uistrings.TEXT;
        int flightAmount = 3;
        int numNonMinionEnemies = 0;
        for (AbstractMonster mo : Wiz.getEnemies()) {
            if (!mo.hasPower(MinionPower.POWER_ID)) {
                numNonMinionEnemies++;
            }
        }
        if (numNonMinionEnemies == 1) {
            flightAmount++;
        }
        for (AbstractMonster mo : Wiz.getEnemies()) {
            if (mo.id.equals(Byrd.ID)) {
                atb(new ApplyPowerAction(mo, mo, new StrengthPower(mo, 1), 1));
                atb(new TalkAction(mo, TEXT[AbstractDungeon.monsterRng.random(TEXT.length - 1)]));
            } else if (!mo.hasPower(MinionPower.POWER_ID)) {
                atb(new ApplyPowerAction(mo, mo, new HeavenlyFlightPower(mo, flightAmount), flightAmount));
                mo.currentHealth = Math.max((int)((float)mo.currentHealth * HEALTH_MODIFIER), 1);
                mo.maxHealth = Math.max((int)((float)mo.maxHealth * HEALTH_MODIFIER), 1);
                mo.healthBarUpdatedEvent();
                mo.currentBlock = (int)((float)mo.currentBlock * HEALTH_MODIFIER); // wreck spheric guardian
            }
        }
    }

    private class Cloud {
        private Texture texture;
        private float x, y;
        private float speed;
        private float scale;
        private Color color;

        public Cloud(Texture texture, float x, float y, float speed, float scale, Color color) {
            this.texture = texture;
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.scale = scale;
            this.color = color;
        }
    }

    @Override
    public void postRenderCombatBackground(SpriteBatch sb) {
        if (!initialCloudSpawned) {
            spawnInitialClouds();
            initialCloudSpawned = true;
        }

        cloudSpawnTimer += Gdx.graphics.getDeltaTime();
        if (cloudSpawnTimer >= cloudSpawnInterval) {
            cloudSpawnTimer = 0;
            spawnCloud();
        }

        Iterator<Cloud> iter = clouds.iterator();
        while (iter.hasNext()) {
            Cloud cloud = iter.next();
            cloud.x += cloud.speed * Gdx.graphics.getDeltaTime();
            if (cloud.x > Settings.WIDTH) {
                iter.remove();
            } else {
                sb.setColor(cloud.color);
                sb.draw(cloud.texture, cloud.x, cloud.y, cloud.texture.getWidth() * cloud.scale, cloud.texture.getHeight() * cloud.scale);
            }
        }

        sb.setColor(Color.WHITE);
    }


    private void spawnCloud() {
        Texture cloudTexture;
        boolean isSpecialCloud = MathUtils.random() < 0.01f; // 1% chance to spawn a special cloud
        if (isSpecialCloud) {
            cloudTexture = specialClouds[MathUtils.random(specialClouds.length - 1)];
        } else {
            Texture[] cloudTextures = {Cloud1, Cloud2, Cloud3, Cloud4, Cloud5};
            cloudTexture = cloudTextures[MathUtils.random(cloudTextures.length - 1)];
        }
        float x = -cloudTexture.getWidth() - 5;
        float y = MathUtils.random(cloudMinY, cloudMaxY - cloudTexture.getHeight());
        float speed = MathUtils.random(50, 125); // Random speed between 50 and 125 pixels per second
        float scale = MathUtils.random(0.5f, 1.0f); // Random scale between 0.5 and 1.0
        Color color;
        if (isSpecialCloud) {
            color = Color.WHITE; // Special clouds retain their original appearance
        } else {
            float r = MathUtils.random(0.9f, 1.0f);
            float g = MathUtils.random(0.9f, 1.0f);
            float b = MathUtils.random(0.7f, 0.9f);
            float a = MathUtils.random(0.85f, 1.0f);
            color = new Color(r, g, b, a);
        }
        clouds.add(new Cloud(cloudTexture, x, y, speed, scale, color));
    }

    private void spawnInitialClouds() {
        for (int i = 0; i < initialCloudCount; i++) {
            Texture cloudTexture;
            boolean isSpecialCloud = MathUtils.random() < 0.01f; // 1% chance to spawn a special cloud
            if (isSpecialCloud) {
                cloudTexture = specialClouds[MathUtils.random(specialClouds.length - 1)];
            } else {
                Texture[] cloudTextures = {Cloud1, Cloud2, Cloud3, Cloud4, Cloud5};
                cloudTexture = cloudTextures[MathUtils.random(cloudTextures.length - 1)];
            }
            float x = MathUtils.random(-cloudTexture.getWidth(), Settings.WIDTH);
            float y = MathUtils.random(cloudMinY, cloudMaxY - cloudTexture.getHeight());
            float speed = MathUtils.random(50, 100); // Random speed between 50 and 100 pixels per second
            float scale = MathUtils.random(0.5f, 1.0f); // Random scale between 0.5 and 1.0
            Color color;
            if (isSpecialCloud) {
                color = Color.WHITE; // Special clouds retain their original appearance
            } else {
                float r = MathUtils.random(0.9f, 1.0f);
                float g = MathUtils.random(0.9f, 1.0f);
                float b = MathUtils.random(0.7f, 0.9f);
                float a = MathUtils.random(0.85f, 1.0f);
                color = new Color(r, g, b, a);
            }
            clouds.add(new Cloud(cloudTexture, x, y, speed, scale, color));
        }
    }

    @Override
    public void modifyRewardCards(ArrayList<AbstractCard> cards) {
        for (AbstractCard card : cards) {
            CardModifierManager.addModifier(card, new FlightMod());
        }
    }

}
