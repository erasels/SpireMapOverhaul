package spireMapOverhaul.zones.mirror;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import spireMapOverhaul.zones.mirror.powers.ReflectivePower;
import spireMapOverhaul.zones.mirror.powers.ShatteredPower;

import java.util.ArrayList;
import java.util.HashMap;

public class MirrorMoveData {
    public int damage = -1;
    public int hits = 0;
    public int block = 0;
    public int draw = 0;
    public boolean exhaustOther = false;
    public boolean vampire = false;
    public HashMap<String, Integer> buffs = new HashMap<>();
    public HashMap<String, Integer> debuffs = new HashMap<>();
    public int randomOrbs = 0;
    public int tempHp = 0;
    public boolean removeDebuff = false;

    public AbstractMonster monster;

    public static ArrayList<String> acceptableBuffIds = new ArrayList<String>() {{
        add(StrengthPower.POWER_ID);
        add(ArtifactPower.POWER_ID);
        add(PlatedArmorPower.POWER_ID);
        add(MetallicizePower.POWER_ID);
        add(ThornsPower.POWER_ID);
        add(RitualPower.POWER_ID);
        add(AngerPower.POWER_ID);
        add(IntangiblePower.POWER_ID);
        add(BufferPower.POWER_ID);
        add(ReflectivePower.POWER_ID);
    }};

    public static ArrayList<String> acceptableDebuffIds = new ArrayList<String>() {{
        add(StrengthPower.POWER_ID);
        add(VulnerablePower.POWER_ID);
        add(WeakPower.POWER_ID);
        add(PoisonPower.POWER_ID);
        add(ConstrictedPower.POWER_ID);
        add(ShatteredPower.POWER_ID);
    }};

    public void addBuff(AbstractPower p) {
        String id = p.ID.equals(SharpHidePower.POWER_ID) ? ThornsPower.POWER_ID : p.ID;
        if (acceptableBuffIds.contains(id)) {
            buffs.put(id, p.amount);
        } else {
            int amount = buffs.containsKey(ReflectivePower.POWER_ID) ? buffs.get(ReflectivePower.POWER_ID) + Math.abs(p.amount * 2) : Math.abs(p.amount * 2);
            buffs.put(ReflectivePower.POWER_ID, amount);
        }
    }

    public void addDebuff(AbstractPower p) {
        if (acceptableDebuffIds.contains(p.ID)) {
            debuffs.put(p.ID, Math.abs(p.amount));
        } else {
            int amount = debuffs.containsKey(ShatteredPower.POWER_ID) ? debuffs.get(ShatteredPower.POWER_ID) + Math.abs(p.amount) : Math.abs(p.amount);
            debuffs.put(ShatteredPower.POWER_ID, amount);
        }
    }

    public boolean isEmpty() {
        return damage <= 0 && block <= 0 && draw <= 0 && !exhaustOther && buffs.isEmpty() && debuffs.isEmpty() && randomOrbs <= 0 && tempHp <= 0 && !removeDebuff;
    }
}
