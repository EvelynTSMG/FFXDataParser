package model;

import script.model.ScriptConstants;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class GearDataObject {
    static Map<Integer, String> abilities;

    private int[] bytes;

    boolean isBukiGet;
    int alwaysZero3;
    int alwaysZero4;
    int alwaysZeroOrOne;
    int unknownC;
    int unknownD;
    int variousFlags;
    int character;
    int alwaysZero1;
    int armorByte;
    int alwaysZero2;
    int formula;
    int power;
    int crit;
    int slots;
    String ability1;
    String ability2;
    String ability3;
    String ability4;

    boolean armor;
    boolean flag1;
    boolean flag2;
    boolean celestial;
    boolean brotherhood;

    public GearDataObject() {}

    public GearDataObject(int[] bytes) throws IOException {
        isBukiGet = (bytes.length == 16);
        this.bytes = bytes;
        prepareMaps();
        if (isBukiGet) {
            mapBytesBukiGet();
        } else {
            mapBytesNormal();
        }
        mapFlags();
    }

    private void mapBytesNormal() {
        alwaysZero3 = bytes[0x00];
        alwaysZero4 = bytes[0x01];
        alwaysZeroOrOne = bytes[0x02];
        variousFlags = bytes[0x03];
        character = bytes[0x04];
        armorByte = bytes[0x05];
        alwaysZero1 = bytes[0x06];
        alwaysZero2 = bytes[0x07];
        formula = bytes[0x08];
        power = bytes[0x09];
        crit = bytes[0x0A];
        slots = bytes[0x0B];
        unknownC = bytes[0x0C];
        unknownD = bytes[0x0D];
        ability1 = abilities.get(read2Bytes(0x0E));
        ability2 = abilities.get(read2Bytes(0x10));
        ability3 = abilities.get(read2Bytes(0x12));
        ability4 = abilities.get(read2Bytes(0x14));
    }

    private void mapBytesBukiGet() {
        alwaysZeroOrOne = 1;
        variousFlags = bytes[0x00];
        character = bytes[0x01];
        armorByte = bytes[0x02];
        alwaysZero1 = bytes[0x03];
        formula = bytes[0x04];
        power = bytes[0x05];
        crit = bytes[0x06];
        slots = bytes[0x07];
        ability1 = abilities.get(read2Bytes(0x08));
        ability2 = abilities.get(read2Bytes(0x0A));
        ability3 = abilities.get(read2Bytes(0x0C));
        ability4 = abilities.get(read2Bytes(0x0E));
    }

    private void mapFlags() {
        armor = armorByte != 0;
        flag1 = (variousFlags & 0x01) > 0;
        flag2 = (variousFlags & 0x02) > 0;
        celestial = (variousFlags & 0x04) > 0;
        brotherhood = (variousFlags & 0x08) > 0;
    }

    @Override
    public String toString() {
        String specialType = getSpecialType();
        String abilityString = getAbilityString();
        return "{ " + ScriptConstants.getEnumMap("actor").get(character).name +
                ", " + (armor ? "Armor" : "Weapon") + (armorByte > 1 ? "[" + armorByte + "]" : "") +
                ", F=" + formula +
                ", Power=" + power +
                ", Crit=" + crit + '%' +
                ", Slots=" + slots + ' ' +
                abilityString +
                specialType +
                (flag2 ? ", Hidden in Menu" : "") +
                (brotherhood ? ", Brotherhood" : "") +
                (alwaysZero1 != 0 ? ", 1 not Zero!=" + formatUnknownByte(alwaysZero1) : "") +
                (alwaysZero2 != 0 ? ", 2 not Zero!=" + formatUnknownByte(alwaysZero2) : "") +
                (alwaysZero3 != 0 ? ", 3 not Zero!=" + formatUnknownByte(alwaysZero3) : "") +
                (alwaysZero4 != 0 ? ", 4 not Zero!=" + formatUnknownByte(alwaysZero4) : "") +
                (alwaysZeroOrOne > 1 ? ", byte 2 greater than 1!=" + formatUnknownByte(alwaysZeroOrOne) : "") +
                (unknownC != 0 ? ", UC=" + formatUnknownByte(unknownC) : "") +
                (unknownD != 0 ? ", UD=" + formatUnknownByte(unknownD) : "") +
                " }";
    }

    private String getAbilityString() {
        String abilityString = "[";
        if (slots >= 1) {
            abilityString += ability1;
            if (slots >= 2 || !ability2.equals("Empty")) {
                abilityString += ", " + (slots < 2 ? "!" : "") + ability2;
                if (slots >= 3 || !ability3.equals("Empty")) {
                    abilityString += ", " + (slots < 3 ? "!" : "") + ability3;
                    if (slots >= 4 || !ability4.equals("Empty")) {
                        abilityString += ", " + (slots < 4 ? "!" : "") + ability4;
                    }
                }
            }
        }
        abilityString += "]";
        return abilityString;
    }

    private String getSpecialType() {
        String specialType = "";
        if (flag1) {
            specialType = ", Special: ";
            if (brotherhood) {
                if (flag2) {
                    specialType += "Original Brotherhood";
                } else {
                    specialType += "Other Brotherhood?";
                }
                if (celestial) {
                    specialType += ", CL";
                }
            } else if (flag2) {
                specialType += "Aeon " + (celestial ? "Weapon" : "Armor");
            } else {
                specialType += "(No BH, No F2)";
            }
        } else if (celestial) {
            specialType = ", Celestial";
        }
        return specialType;
    }

    private static String formatUnknownByte(int bt) {
        return String.format("%02x", bt) + '=' + String.format("%03d", bt) + '(' + String.format("%8s", Integer.toBinaryString(bt)).replace(' ', '0') + ')';
    }

    private static void prepareMaps() {
        ScriptConstants.initialize();
        if (abilities == null) {
            abilities = new HashMap<>();
            abilities.put(0x00FF, "Empty");
            abilities.put(0x8000, "Sensor");
            abilities.put(0x8001, "First Strike");
            abilities.put(0x8002, "Initiative");
            abilities.put(0x8003, "Counterattack");
            abilities.put(0x8004, "Evade & Counter");
            abilities.put(0x8005, "Magic Counter");
            abilities.put(0x8006, "Magic Booster");
            abilities.put(0x8007, "Alchemy");
            abilities.put(0x8008, "Auto-Potion");
            abilities.put(0x8009, "Auto-Med");
            abilities.put(0x800A, "Auto-Phoenix");
            abilities.put(0x800B, "Piercing");
            abilities.put(0x800C, "Half MP Cost");
            abilities.put(0x800D, "One MP Cost");
            abilities.put(0x800E, "Double Overdrive");
            abilities.put(0x800F, "Triple Overdrive");
            abilities.put(0x8010, "SOS-Overdrive");
            abilities.put(0x8011, "Overdrive -> AP");
            abilities.put(0x8012, "Double AP");
            abilities.put(0x8013, "Triple AP");
            abilities.put(0x8014, "No AP");
            abilities.put(0x8015, "Pickpocket");
            abilities.put(0x8016, "Master Thief");
            abilities.put(0x8017, "Break HP Limit");
            abilities.put(0x8018, "Break MP Limit");
            abilities.put(0x8019, "Break Damage Limit");
            abilities.put(0x801A, "Gillionaire");
            abilities.put(0x801B, "HP Stroll");
            abilities.put(0x801C, "MP Stroll");
            abilities.put(0x801D, "No Encounters");
            abilities.put(0x801E, "Firestrike");
            abilities.put(0x801F, "Fire Ward");
            abilities.put(0x8020, "Fireproof");
            abilities.put(0x8021, "Fire Eater");
            abilities.put(0x8022, "Icestrike");
            abilities.put(0x8023, "Ice Ward");
            abilities.put(0x8024, "Iceproof");
            abilities.put(0x8025, "Ice Eater");
            abilities.put(0x8026, "Thunderstrike");
            abilities.put(0x8027, "Thunder Ward");
            abilities.put(0x8028, "Thunderproof");
            abilities.put(0x8029, "Thunder Eater");
            abilities.put(0x802A, "Waterstrike");
            abilities.put(0x802B, "Water Ward");
            abilities.put(0x802C, "Waterproof");
            abilities.put(0x802D, "Water Eater");
            abilities.put(0x802E, "Deathstrike");
            abilities.put(0x802F, "Deathtouch");
            abilities.put(0x8030, "Deathproof");
            abilities.put(0x8031, "Death Ward");
            abilities.put(0x8032, "Zombiestrike");
            abilities.put(0x8033, "Zombietouch");
            abilities.put(0x8034, "Zombieproof");
            abilities.put(0x8035, "Zombie Ward");
            abilities.put(0x8036, "Stonestrike");
            abilities.put(0x8037, "Stonetouch");
            abilities.put(0x8038, "Stoneproof");
            abilities.put(0x8039, "Stone Ward");
            abilities.put(0x803A, "Poisonstrike");
            abilities.put(0x803B, "Poisontouch");
            abilities.put(0x803C, "Poisonproof");
            abilities.put(0x803D, "Poison Ward");
            abilities.put(0x803E, "Sleepstrike");
            abilities.put(0x803F, "Sleeptouch");
            abilities.put(0x8040, "Sleepproof");
            abilities.put(0x8041, "Sleep Ward");
            abilities.put(0x8042, "Silencestrike");
            abilities.put(0x8043, "Silencetouch");
            abilities.put(0x8044, "Silenceproof");
            abilities.put(0x8045, "Silence Ward");
            abilities.put(0x8046, "Darkstrike");
            abilities.put(0x8047, "Darktouch");
            abilities.put(0x8048, "Darkproof");
            abilities.put(0x8049, "Dark Ward");
            abilities.put(0x804A, "Slowstrike");
            abilities.put(0x804B, "Slowtouch");
            abilities.put(0x804C, "Slowproof");
            abilities.put(0x804D, "Slow Ward");
            abilities.put(0x804E, "Confuseproof");
            abilities.put(0x804F, "Confuse Ward");
            abilities.put(0x8050, "Berserkproof");
            abilities.put(0x8051, "Berserk Ward");
            abilities.put(0x8052, "Curseproof");
            abilities.put(0x8053, "Curse Ward");
            abilities.put(0x8054, "Auto-Shell");
            abilities.put(0x8055, "Auto-Protect");
            abilities.put(0x8056, "Auto-Haste");
            abilities.put(0x8057, "Auto-Regen");
            abilities.put(0x8058, "Auto-Reflect");
            abilities.put(0x8059, "SOS-Shell");
            abilities.put(0x805A, "SOS-Protect");
            abilities.put(0x805B, "SOS-Haste");
            abilities.put(0x805C, "SOS-Regen");
            abilities.put(0x805D, "SOS-Reflect");
            abilities.put(0x805E, "SOS-NulTide");
            abilities.put(0x805F, "SOS-NulFrost");
            abilities.put(0x8060, "SOS-NulShock");
            abilities.put(0x8061, "SOS-NulBlaze");
            abilities.put(0x8062, "Strength +3%");
            abilities.put(0x8063, "Strength +5%");
            abilities.put(0x8064, "Strength +10%");
            abilities.put(0x8065, "Strength +20%");
            abilities.put(0x8066, "Magic +3%");
            abilities.put(0x8067, "Magic +5%");
            abilities.put(0x8068, "Magic +10%");
            abilities.put(0x8069, "Magic +20%");
            abilities.put(0x806A, "Defense +3%");
            abilities.put(0x806B, "Defense +5%");
            abilities.put(0x806C, "Defense +10%");
            abilities.put(0x806D, "Defense +20%");
            abilities.put(0x806E, "Magic Defense +3%");
            abilities.put(0x806F, "Magic Defense +5%");
            abilities.put(0x8070, "Magic Defense +10%");
            abilities.put(0x8071, "Magic Defense +20%");
            abilities.put(0x8072, "HP +5%");
            abilities.put(0x8073, "HP +10%");
            abilities.put(0x8074, "HP +20%");
            abilities.put(0x8075, "HP +30%");
            abilities.put(0x8076, "MP +5%");
            abilities.put(0x8077, "MP +10%");
            abilities.put(0x8078, "MP +20%");
            abilities.put(0x8079, "MP +30%");
            abilities.put(0x807A, "Capture");
            abilities.put(0x807B, "Aeon Super Ribbon");
            abilities.put(0x807C, "Distill Power");
            abilities.put(0x807D, "Distill Mana");
            abilities.put(0x807E, "Distill Speed");
            abilities.put(0x807F, "Distill Ability");
            abilities.put(0x8080, "Ribbon");
        }
    }

    private int read2Bytes(int offset) {
        int val = bytes[offset];
        val += bytes[offset+1] * 0x100;
        return val;
    }
}
