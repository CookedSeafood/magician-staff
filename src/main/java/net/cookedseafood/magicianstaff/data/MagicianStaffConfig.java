package net.cookedseafood.magicianstaff.data;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.mutable.MutableInt;

public class MagicianStaffConfig {
    public static final float MANA_CONSUMPTION = 4.0f;
    public static final float MOVEMENT_SPEED = 1.0f;
    public static final short EXPLOSION_FUSE = 120;
    public static final byte EXPLOSION_RADIUS = 1;
    public static float manaConsumption;
    public static float movementSpeed;
    public static short explosionFuse;
    public static byte explosionRadius;

    public static int reload() {
        String configString;
        try {
            configString = FileUtils.readFileToString(new File("./config/chorus-staff.json"), StandardCharsets.UTF_8);
        } catch (IOException e) {
            reset();
            return 1;
        }

        JsonObject config = new Gson().fromJson(configString, JsonObject.class);
        if (config == null) {
            reset();
            return 1;
        }

        return reload(config);
    }

    public static int reload(JsonObject config) {
        MutableInt counter = new MutableInt(0);

        if (config.has("manaConsumption")) {
            manaConsumption = config.get("manaConsumption").getAsFloat();
            counter.increment();
        } else {
            manaConsumption = MANA_CONSUMPTION;
        }

        if (config.has("movementSpeed")) {
            movementSpeed = config.get("movementSpeed").getAsFloat();
            counter.increment();
        } else {
            movementSpeed = MOVEMENT_SPEED;
        }

        if (config.has("explosionFuse")) {
            explosionFuse = config.get("explosionFuse").getAsShort();
            counter.increment();
        } else {
            explosionFuse = EXPLOSION_FUSE;
        }

        if (config.has("explosionRadius")) {
            explosionRadius = config.get("explosionRadius").getAsByte();
            counter.increment();
        } else {
            explosionRadius = EXPLOSION_RADIUS;
        }

        return counter.intValue();
    }

    public static void reset() {
        manaConsumption = MANA_CONSUMPTION;
        movementSpeed = MOVEMENT_SPEED;
        explosionFuse = EXPLOSION_FUSE;
        explosionRadius = EXPLOSION_RADIUS;
    }
}
