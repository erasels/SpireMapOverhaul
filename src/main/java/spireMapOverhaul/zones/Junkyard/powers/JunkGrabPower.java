//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package spireMapOverhaul.zones.Junkyard.powers;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StasisPower;
import spireMapOverhaul.SpireAnniversary6Mod;
import spireMapOverhaul.zones.Junkyard.monsters.Junkbot;

public class JunkGrabPower extends StasisPower {
    public static final String POWER_ID = SpireAnniversary6Mod.makeID("JunkStasis");
    public AbstractCard card;

    public JunkGrabPower(AbstractCreature owner, AbstractCard card) {
        super(owner, card);
        ID += card.uuid;
    }
}
