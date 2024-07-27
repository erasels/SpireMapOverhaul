package spireMapOverhaul.zones.grass.vegetables;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.OrbStrings;
import spireMapOverhaul.SpireAnniversary6Mod;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class AbstractVegetableData {
    private static final Map<String, AbstractVegetableData> STATIC_DATA = new HashMap<>();

    private final Constructor<? extends AbstractVegetable> constructor;
    public final String ID;
    public String imagePath;
    public OrbStrings strings;
    public int maxUpgradeLevel = 5;
    public float bounce = 200f;
    public float duration = 0.6f;
    public float rotation = 600f;

    public AbstractVegetableData(Constructor<? extends AbstractVegetable> constructor, String id, String imagePath) {
        this(constructor, id, imagePath, CardCrawlGame.languagePack.getOrbString(id));
    }

    public AbstractVegetableData(Constructor<? extends AbstractVegetable> constructor, String id, String imagePath, OrbStrings strings) {
        this.constructor = constructor;
        this.ID = id;
        this.imagePath = imagePath;
        this.strings = strings != null ? strings : new OrbStrings();
    }

    public static AbstractVegetableData get(String id) {
        return STATIC_DATA.get(id);
    }

    private static String getImagePath(String name) {
        return SpireAnniversary6Mod.makeImagePath("ui/Grass/vegetables/" + name + ".png");
    }

    public static AbstractVegetableData register(Class<? extends AbstractVegetable> className) {
        String sName = className.getSimpleName();
        String id = SpireAnniversary6Mod.makeID(sName);
        try {
            Constructor<? extends AbstractVegetable> c = (Constructor<? extends AbstractVegetable>) className.getConstructor();
            return register(new AbstractVegetableData(c, id, getImagePath(sName)));
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to find no-parameter constructor for class " + className.getSimpleName(), e);
        }
    }

    public static AbstractVegetableData register(AbstractVegetableData data) {
        STATIC_DATA.put(data.ID, data);
        return data;
    }

    public AbstractVegetable create() {
        try {
            return constructor.newInstance();
        }
        catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Failed to create vegetable with ID " + ID, e);
        }
    }

}
