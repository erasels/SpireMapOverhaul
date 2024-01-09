package spireMapOverhaul.zones.brokenSpace.cardmods;

import basemod.abstracts.AbstractCardModifier;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.PurgeField;
import com.megacrit.cardcrawl.cards.AbstractCard;

import static spireMapOverhaul.SpireAnniversary6Mod.makeID;

public class UnreadableCardMod extends AbstractCardModifier {
    public static final String ID = makeID("UnreadableCardMod");
    private String name;

    @Override
    public void onInitialApplication(AbstractCard card) {

        name = randomText(card.name.length());
        PurgeField.purge.set(card, true);
        card.isEthereal = true;


    }

    @Override
    public String modifyName(String cardName, AbstractCard card) {
        return name;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return randomText(rawDescription.length());
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public boolean removeAtEndOfTurn(AbstractCard card) {
        return true;
    }

    public String randomText(int length) {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*()_+{}[];':\",./<>?\\|`~-=_+??????????????";
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(chars.charAt((int) (Math.random() * chars.length())));
            if (Math.random() < 0.1) {
                switch ((int) (Math.random() * 2)) {
                    case 0:
                        builder.append(" ");
                        break;
                    case 1:
                        builder.append(" [E] ");
                        break;
                }
            }

            if (Math.random() < 0.1) {
                builder.append(" NL ");
            }
        }
        return builder.toString();
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new UnreadableCardMod();
    }
}
