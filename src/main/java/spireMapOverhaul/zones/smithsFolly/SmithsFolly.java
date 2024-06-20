package spireMapOverhaul.zones.smithsFolly;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.unique.IncreaseMaxHpAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.shrines.AccursedBlacksmith;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.powers.RegenerateMonsterPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.abstracts.AbstractZone;
import spireMapOverhaul.util.TexLoader;
import spireMapOverhaul.zoneInterfaces.*;

import java.util.*;
import java.util.stream.Collectors;

public class SmithsFolly extends AbstractZone implements CombatModifyingZone, OnTravelZone, RewardModifyingZone, ModifiedEventRateZone, RenderableZone {
    public static final String ID = "SmithsFolly";
    private Texture bg = TexLoader.getTexture(SpireAnniversary6Mod.makeBackgroundPath("smithsFolly/bg.png"));

    public SmithsFolly() {
        super(ID, Icons.MONSTER);
        this.width = 2;
        this.height = 4;
    }

    @Override
    public AbstractZone copy() {
        return new SmithsFolly();
    }

    @Override
    public Color getColor() {
        return new Color(0.349f, 0.231f, 0.145f, 0.8f);
    }

    @Override
    public boolean canSpawn() {
        return !this.isAct(2);
    }

    @Override
    public void replaceRooms(com.megacrit.cardcrawl.random.Random rng) {
        //Replace all shop rooms with monster rooms
        for (MapRoomNode node : this.nodes) {
            if(node.room != null && ShopRoom.class.equals(node.room.getClass())) {
                node.setRoom(new MonsterRoom());
            }
        }
    }

    @Override
    public boolean allowSideConnections() {
        return false;
    }

    @Override
    public boolean allowAdditionalPaths() {
        return false;
    }

    @Override
    public void onEnter() {
        upgradeRarestCard();
    }

    @Override
    public void onExit() {
        upgradeRarestCard();
    }

    @Override
    public void atPreBattle() {
        if (AbstractDungeon.getCurrRoom().monsters == null) {
            return;
        }
        if (!AbstractDungeon.getCurrMapNode().hasEmeraldKey) {
            AbstractRoom abstractRoom = AbstractDungeon.getCurrRoom();

            int modifierIndex = AbstractDungeon.miscRng.random(0, 3);
            switch (modifierIndex) {
                case 0: {
                    for (final AbstractMonster m : abstractRoom.monsters.monsters) {
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, new StrengthPower(m, AbstractDungeon.actNum + 1), AbstractDungeon.actNum + 1));
                    }
                    break;
                }
                case 1: {
                    for (final AbstractMonster m : abstractRoom.monsters.monsters) {
                        AbstractDungeon.actionManager.addToBottom(new IncreaseMaxHpAction(m, 0.25f, true));
                    }
                    break;
                }
                case 2: {
                    for (final AbstractMonster m : abstractRoom.monsters.monsters) {
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, new MetallicizePower(m, AbstractDungeon.actNum * 2 + 2), AbstractDungeon.actNum * 2 + 2));
                    }
                    break;
                }
                case 3: {
                    for (final AbstractMonster m : abstractRoom.monsters.monsters) {
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, new RegenerateMonsterPower(m, 1 + AbstractDungeon.actNum * 2), 1 + AbstractDungeon.actNum * 2));
                    }
                    break;
                }
            }
        }
    }

    @Override
    public RewardItem getAdditionalReward() {
        if (!AbstractDungeon.getCurrMapNode().hasEmeraldKey && AbstractDungeon.getCurrRoom() instanceof MonsterRoomElite) {
            if (!Settings.hasEmeraldKey) {
                return new RewardItem(null, RewardItem.RewardType.EMERALD_KEY);
            }
        }
        return null;
    }

    private void upgradeRarestCard() {
        AbstractPlayer player = AbstractDungeon.player;
        if (player == null) return;

        // Define the rarity order
        List<AbstractCard.CardRarity> rarityOrder = Arrays.asList(
                AbstractCard.CardRarity.RARE,
                AbstractCard.CardRarity.UNCOMMON,
                AbstractCard.CardRarity.COMMON,
                AbstractCard.CardRarity.SPECIAL,
                AbstractCard.CardRarity.BASIC
        );

        // Find the upgradable cards, grouped by rarity
        Map<AbstractCard.CardRarity, List<AbstractCard>> upgradableCardsByRarity = player.masterDeck.group.stream()
                .filter(AbstractCard::canUpgrade)
                .collect(Collectors.groupingBy(card -> card.rarity));

        // Find the rarest category with upgradable cards
        for (AbstractCard.CardRarity rarity : rarityOrder) {
            List<AbstractCard> upgradableCards = upgradableCardsByRarity.getOrDefault(rarity, Collections.emptyList());
            if (!upgradableCards.isEmpty()) {
                // Shuffle and upgrade a random card from the rarest category
                Collections.shuffle(upgradableCards, new Random(AbstractDungeon.miscRng.randomLong()));
                AbstractCard cardToUpgrade = upgradableCards.get(0);
                cardToUpgrade.upgrade();
                AbstractDungeon.player.bottledCardUpgradeCheck(cardToUpgrade);

                // Show upgrade effect (optional)
                AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(cardToUpgrade.makeStatEquivalentCopy()));
                AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect((float) Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
                break;
            }
        }
    }


    @Override
    protected boolean canIncludeEarlyRows() {
        return false;
    }

    @Override
    public float zoneSpecificEventRate() {
        return 0.4f;
    }

    @Override
    public Set<String> addSpecificEvents() {
        Set<String> baseGameEvents = new HashSet<>();
        baseGameEvents.add(AccursedBlacksmith.ID);
        return baseGameEvents;
    }

    @Override
    public void postRenderCombatBackground(SpriteBatch sb) {
        sb.draw(bg, 0, 0, Settings.WIDTH, Settings.HEIGHT);
    }
}