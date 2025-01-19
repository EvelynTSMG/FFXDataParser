package main;

import model.*;
import model.spheregrid.SphereGridLayoutDataObject;
import model.spheregrid.SphereGridNodeTypeDataObject;
import model.spheregrid.SphereGridSphereTypeDataObject;
import reading.Chunk;
import reading.ChunkedFileHelper;
import reading.DataFileReader;
import reading.FileAccessorWithMods;
import script.EncounterFile;
import script.EventFile;
import script.MonsterFile;
import script.model.ScriptConstants;
import script.model.ScriptFuncLib;

import java.io.File;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DataReadingManager {

    public static final String PATH_FFX_ROOT = "ffx_ps2/ffx/master/";
    public static final String ORIGINALS_FOLDER = "jppc/";
    public static final String LOCALIZED_FOLDER = "new_uspc/";
    public static final String PATH_ORIGINALS_ROOT = PATH_FFX_ROOT + ORIGINALS_FOLDER;
    public static final String PATH_LOCALIZED_ROOT = PATH_FFX_ROOT + LOCALIZED_FOLDER;
    public static final String PATH_ORIGINALS_KERNEL = PATH_ORIGINALS_ROOT + "battle/kernel/";
    public static final String PATH_LOCALIZED_KERNEL = PATH_LOCALIZED_ROOT + "battle/kernel/";
    public static final String PATH_MONSTER_FOLDER = PATH_ORIGINALS_ROOT + "battle/mon/";
    public static final String PATH_ORIGINALS_ENCOUNTER = PATH_ORIGINALS_ROOT + "battle/btl/";
    public static final String PATH_LOCALIZED_ENCOUNTER = PATH_LOCALIZED_ROOT + "battle/btl/";
    public static final String PATH_ORIGINALS_EVENT = PATH_ORIGINALS_ROOT + "event/obj/";
    public static final String PATH_LOCALIZED_EVENT = PATH_LOCALIZED_ROOT + "event/obj_ps3/";
    public static final String PATH_ABMAP = PATH_ORIGINALS_ROOT + "menu/abmap/";
    public static final String PATH_SKILL_TABLE_3 = PATH_LOCALIZED_KERNEL + "command.bin"; // "FILE07723.dat"; // "command.bin"; //
    public static final String PATH_SKILL_TABLE_4 = PATH_LOCALIZED_KERNEL + "monmagic1.bin"; // "FILE07740.dat"; // "monmagic1.bin"; //
    public static final String PATH_SKILL_TABLE_6 = PATH_LOCALIZED_KERNEL + "monmagic2.bin"; // "FILE07741.dat"; // "monmagic2.bin"; //
    public static final String PATH_SKILL_TABLE_2 = PATH_LOCALIZED_KERNEL + "item.bin"; // "FILE07734.dat"; // "item.bin"; //

    public static final String DEFAULT_LOCALIZATION = "us";
    public static final Map<String, String> LOCALIZATIONS = Map.of(
            "ch", "Chinese",
            "de", "German",
            "fr", "French",
            "it", "Italian",
            "jp", "Japanese",
            "kr", "Korean",
            "sp", "Spanish",
            "us", "English"
    );

    private static final boolean SKIP_BLITZBALL_EVENTS_FOLDER = true;
    private static final boolean ALLOW_DAT_FILES = true;

    public static String getDefaultLocalization() {
        return DEFAULT_LOCALIZATION;
    }

    public static String getLocalizationRoot(String localization) {
        return PATH_FFX_ROOT + "new_" + localization + "pc/";
    }

    public static void initializeInternals() {
        StringHelper.initialize();
        prepareStringMacros(PATH_LOCALIZED_ROOT + "menu/macrodic.dcp", false);
        ScriptConstants.initialize();
        ScriptFuncLib.initialize();
    }

    public static void readAndPrepareDataModel() {
        DataAccess.SG_SPHERE_TYPES = readSphereGridSphereTypes(PATH_LOCALIZED_KERNEL + "sphere.bin", false);
        prepareAbilities();
        DataAccess.PLAYER_CHAR_STATS = readPlayerCharStats(PATH_LOCALIZED_KERNEL + "ply_save.bin", false);
        DataAccess.GEAR_ABILITIES = readGearAbilities("battle/kernel/a_ability.bin", false);
        DataAccess.BUYABLE_GEAR = readWeaponPickups(PATH_ORIGINALS_KERNEL + "shop_arms.bin", false);
        DataAccess.WEAPON_PICKUPS = readWeaponPickups(PATH_ORIGINALS_KERNEL + "buki_get.bin", false);
        DataAccess.KEY_ITEMS = readKeyItemsFromFile(PATH_LOCALIZED_KERNEL + "important.bin", false);
        DataAccess.GEAR_SHOPS = readWeaponShops(PATH_ORIGINALS_KERNEL + "arms_shop.bin", false);
        DataAccess.ITEM_SHOPS = readItemShops(PATH_ORIGINALS_KERNEL + "item_shop.bin", false);
        DataAccess.TREASURES = readTreasures(PATH_ORIGINALS_KERNEL + "takara.bin", false);
        readMonsterFile(PATH_MONSTER_FOLDER, false);
        LOCALIZATIONS.forEach((key, name) -> DataAccess.addMonsterLocalizations(readMonsterLocalizations(key,false)));
        DataAccess.SG_NODE_TYPES = readSphereGridNodeTypes(PATH_LOCALIZED_KERNEL + "panel.bin", false);
        DataAccess.OSG_LAYOUT = readSphereGridLayout(PATH_ABMAP + "dat01.dat", PATH_ABMAP + "dat09.dat", false);
        DataAccess.SSG_LAYOUT = readSphereGridLayout(PATH_ABMAP + "dat02.dat", PATH_ABMAP + "dat10.dat", false);
        DataAccess.ESG_LAYOUT = readSphereGridLayout(PATH_ABMAP + "dat03.dat", PATH_ABMAP + "dat11.dat", false);
        DataAccess.GEAR_CUSTOMIZATIONS = readCustomizations(PATH_ORIGINALS_KERNEL + "kaizou.bin", false);
        DataAccess.AEON_CUSTOMIZATIONS = readCustomizations(PATH_ORIGINALS_KERNEL + "sum_grow.bin", false);
    }

    public static void prepareStringMacros(String filename, boolean print) {
        List<Chunk> chunks = ChunkedFileHelper.readGenericChunkedFile(filename, false, null, 16);
        if (chunks == null) {
            return;
        }
        MacroDictionaryFile macroDictionaryFile = new MacroDictionaryFile(chunks);
        macroDictionaryFile.publishStrings();
        if (print) {
            System.out.println(macroDictionaryFile);
        }
    }

    public static void prepareAbilities() {
        prepareAbilitiesFromFile("battle/kernel/command.bin", 3);
        prepareAbilitiesFromFile("battle/kernel/monmagic1.bin", 4);
        prepareAbilitiesFromFile("battle/kernel/monmagic2.bin", 6);
        prepareAbilitiesFromFile("battle/kernel/item.bin", 2);
    }

    private static void prepareAbilitiesFromFile(String path, int group) {
        AbilityDataObject[] abilities = readAbilitiesFromFile(getLocalizationRoot(getDefaultLocalization()) + path, group, getDefaultLocalization(), false);
        if (abilities == null) {
            System.err.println("Failed to load abilities from " + path + " (group " + group + ')');
            return;
        }
        LOCALIZATIONS.forEach((key, name) -> {
            AbilityDataObject[] localizations = readAbilitiesFromFile(getLocalizationRoot(key) + path, group, key, false);
            if (localizations != null) {
                for (int i = 0; i < localizations.length && i < abilities.length; i++) {
                    if (abilities[i] != null && localizations[i] != null) {
                        abilities[i].setLocalizations(localizations[i]);
                    }
                }
            }
        });
        System.arraycopy(abilities, 0, DataAccess.MOVES, 0x1000 * group, abilities.length);
    }

    public static AbilityDataObject[] readAbilitiesFromFile(String filename, int group, String localization, boolean print) {
        DataFileReader<AbilityDataObject> reader = new DataFileReader<>((bytes, stringBytes) -> new AbilityDataObject(bytes, stringBytes, localization, group)) {
            @Override
            public String indexWriter(int idx) {
                return String.format("%04X", idx + group * 0x1000);
            }
        };
        List<AbilityDataObject> list = reader.readGenericDataFile(filename, print);
        if (list == null) {
            return null;
        }
        AbilityDataObject[] array = new AbilityDataObject[list.size()];
        return list.toArray(array);
    }

    public static KeyItemDataObject[] readKeyItemsFromFile(String filename, boolean print) {
        DataFileReader<KeyItemDataObject> reader = new DataFileReader<>(KeyItemDataObject::new) {
            @Override
            public String indexWriter(int idx) {
                return "A0" + String.format("%02X", idx);
            }
        };
        List<KeyItemDataObject> list = reader.readGenericDataFile(filename, print);
        if (list == null) {
            return null;
        }
        KeyItemDataObject[] array = new KeyItemDataObject[list.size()];
        return list.toArray(array);
    }

    public static PlayerCharStatDataObject[] readPlayerCharStats(String filename, boolean print) {
        DataFileReader<PlayerCharStatDataObject> reader = new DataFileReader<>(PlayerCharStatDataObject::new);
        List<PlayerCharStatDataObject> list = reader.readGenericDataFile(filename, print);
        if (list == null) {
            return null;
        }
        PlayerCharStatDataObject[] array = new PlayerCharStatDataObject[list.size()];
        list.toArray(array);
        for (int i = 0; i < array.length; i++) {
            StringHelper.MACRO_LOOKUP.put(i, array[i].getName());
        }
        return array;
    }

    public static GearAbilityDataObject[] readGearAbilities(String path, boolean print) {
        GearAbilityDataObject[] abilities = readGearAbilitiesFromFile(getLocalizationRoot(getDefaultLocalization()) + path, getDefaultLocalization(), print);
        if (abilities == null) {
            return null;
        }
        LOCALIZATIONS.forEach((key, name) -> {
            GearAbilityDataObject[] localizations = readGearAbilitiesFromFile(getLocalizationRoot(key) + path, key, false);
            if (localizations != null) {
                for (int i = 0; i < localizations.length && i < abilities.length; i++) {
                    if (abilities[i] != null && localizations[i] != null) {
                        abilities[i].setLocalizations(localizations[i]);
                    }
                }
            }
        });
        return abilities;
    }

    public static GearAbilityDataObject[] readGearAbilitiesFromFile(String filename, String localization, boolean print) {
        DataFileReader<GearAbilityDataObject> reader = new DataFileReader<>((int[] bytes, int[] stringBytes) -> new GearAbilityDataObject(bytes, stringBytes, localization)) {
            @Override
            public String indexWriter(int idx) {
                return "80" + String.format("%02X", idx);
            }
        };
        List<GearAbilityDataObject> list = reader.readGenericDataFile(filename, print);
        if (list == null) {
            return null;
        }
        GearAbilityDataObject[] array = new GearAbilityDataObject[list.size()];
        if (print) {
            Set<Integer> groups = new HashSet<>();
            List<Integer> untakenGroups = new ArrayList<>();
            for (GearAbilityDataObject gearAbility : list) {
                groups.add(gearAbility.groupIndex);
            }
            for (int i = 0; i <= 0x82; i++) {
                if (!groups.contains(i)) {
                    untakenGroups.add(i);
                }
            }
            System.out.println("Untaken Groups: " + untakenGroups.stream().map(i -> ""+i).collect(Collectors.joining(",")));
        }
        return list.toArray(array);
    }

    public static MonsterFile readMonsterFile(String filename, boolean print) {
        File file = FileAccessorWithMods.getRealFile(filename);
        if (file.isDirectory()) {
            String[] contents = file.list();
            if (contents != null) {
                Arrays.stream(contents).filter(sf -> !sf.startsWith(".")).sorted().forEach(sf -> readMonsterFile(filename + '/' + sf, print));
            }
            return null;
        } else if (!(filename.endsWith(".bin") || (ALLOW_DAT_FILES && filename.endsWith(".dat")))) {
            return null;
        }
        List<Chunk> chunks = ChunkedFileHelper.readGenericChunkedFile(filename, print, null, true);
        MonsterFile monsterFile = new MonsterFile(chunks);
        try {
            int idx = Integer.parseInt(filename.substring(filename.length() - 7, filename.length() - 4), 10);
            DataAccess.MONSTERS[idx] = monsterFile;
        } catch (RuntimeException e) {
            System.err.println("Got exception while storing monster object (" + filename + ")");
            e.printStackTrace();
        }
        if (print) {
            monsterFile.parseScript();
            System.out.println(monsterFile);
        }
        return monsterFile;
    }

    public static EncounterFile readEncounterFile(String filename, final boolean print, final List<String> strings) {
        File file = FileAccessorWithMods.getRealFile(filename);
        if (file.isDirectory()) {
            String[] contents = file.list();
            if (contents != null) {
                Arrays.stream(contents).filter(sf -> !sf.startsWith(".")).sorted().forEach(sf -> readEncounterFile(filename + '/' + sf, print, strings));
            }
            return null;
        } else if (!(filename.endsWith(".bin") || (ALLOW_DAT_FILES && filename.endsWith(".dat")))) {
            return null;
        }
        List<String> actualStrings = strings;
        if (strings == null) {
            String stringFilePath = filename.replace(ORIGINALS_FOLDER, LOCALIZED_FOLDER);
            actualStrings = StringHelper.readStringFile(stringFilePath, false);
        }
        List<Integer> knownLengths = new ArrayList<>();
        knownLengths.add(null);
        knownLengths.add(null);
        knownLengths.add(FormationDataObject.LENGTH);
        List<Chunk> chunks = ChunkedFileHelper.readGenericChunkedFile(filename, print, knownLengths, true);
        EncounterFile encounterFile = new EncounterFile(chunks);
        try {
            // DataAccess.ENCOUNTERS[idx] = encounterFile;
        } catch (RuntimeException e) {
            System.err.println("Got exception while storing encounter object (" + filename + ")");
            e.printStackTrace();
        }
        if (print) {
            encounterFile.parseScript(actualStrings);
            System.out.println(encounterFile);
        }
        return encounterFile;
    }

    public static EventFile readEventFile(String filename, final boolean print, final List<String> strings) {
        File file = FileAccessorWithMods.getRealFile(filename);
        if (file.isDirectory()) {
            String[] contents = file.list();
            if (contents != null) {
                Arrays.stream(contents).filter(sf -> !sf.startsWith(".") && (!SKIP_BLITZBALL_EVENTS_FOLDER || !sf.equals("bl"))).sorted().forEach(sf -> readEventFile(filename + '/' + sf, print, strings));
            }
            return null;
        } else if (!(filename.endsWith(".ebp") || (ALLOW_DAT_FILES && filename.endsWith(".dat")))) {
            return null;
        }
        List<String> actualStrings = strings;
        if (strings == null) {
            String stringFilePath = filename.replace(ORIGINALS_FOLDER, LOCALIZED_FOLDER).replace("obj/", "obj_ps3/").replace(".ebp", ".bin");
            try {
                actualStrings = StringHelper.readStringFile(stringFilePath, false);
            } catch (Exception e) {
                System.out.println("Got exception while trying to parse strings: " + e.getLocalizedMessage());
            }
        }
        /* List<Integer> knownLengths = new ArrayList<>();
        knownLengths.add(null);
        knownLengths.add(null);
        knownLengths.add(0x8C);
        knownLengths.add(null);
        knownLengths.add(0x12C); */
        List<Chunk> chunks = ChunkedFileHelper.readGenericChunkedFile(filename, print, null, false);
        EventFile eventFile = new EventFile(chunks);
        try {
            // DataAccess.ENCOUNTERS[idx] = eventFile;
        } catch (RuntimeException e) {
            System.err.println("Got exception while storing encounter object (" + filename + ")");
            e.printStackTrace();
        }
        if (print) {
            eventFile.parseScript(actualStrings);
            System.out.println(eventFile);
        }
        return eventFile;
    }

    public static EncounterFile readEncounterFull(String btl, boolean print) {
        String endPath = btl + '/' + btl + ".bin";
        String originalsPath = PATH_ORIGINALS_ENCOUNTER + endPath;
        String localizedPath = PATH_LOCALIZED_ENCOUNTER + endPath;
        List<String> strings = StringHelper.readStringFile(localizedPath, false);
        return readEncounterFile(originalsPath, print, strings);
    }

    public static EventFile readEventFull(String event, boolean print) {
        String shortened = event.substring(0, 2);
        String midPath = shortened + '/' + event + '/' + event;
        String originalsPath = PATH_ORIGINALS_EVENT + midPath + ".ebp";
        String localizedPath = PATH_LOCALIZED_EVENT + midPath + ".bin";
        List<String> strings = StringHelper.readStringFile(localizedPath, false);
        return readEventFile(originalsPath, print, strings);
    }

    public static TreasureDataObject[] readTreasures(String filename, boolean print) {
        DataFileReader<TreasureDataObject> reader = new DataFileReader<>(TreasureDataObject::new);
        List<TreasureDataObject> list = reader.readGenericDataFile(filename, print);
        if (list == null) {
            return null;
        }
        TreasureDataObject[] array = new TreasureDataObject[list.size()];
        return list.toArray(array);
    }

    public static GearDataObject[] readWeaponPickups(String filename, boolean print) {
        DataFileReader<GearDataObject> reader = new DataFileReader<>(GearDataObject::new);
        List<GearDataObject> list = reader.readGenericDataFile(filename, print);
        if (list == null) {
            return null;
        }
        GearDataObject[] array = new GearDataObject[list.size()];
        return list.toArray(array);
    }

    public static GearShopDataObject[] readWeaponShops(String filename, boolean print) {
        DataFileReader<GearShopDataObject> reader = new DataFileReader<>(GearShopDataObject::new);
        List<GearShopDataObject> list = reader.readGenericDataFile(filename, print);
        if (list == null) {
            return null;
        }
        GearShopDataObject[] array = new GearShopDataObject[list.size()];
        return list.toArray(array);
    }

    public static ItemShopDataObject[] readItemShops(String filename, boolean print) {
        DataFileReader<ItemShopDataObject> reader = new DataFileReader<>(ItemShopDataObject::new);
        List<ItemShopDataObject> list = reader.readGenericDataFile(filename, print);
        if (list == null) {
            return null;
        }
        ItemShopDataObject[] array = new ItemShopDataObject[list.size()];
        return list.toArray(array);
    }

    public static CustomizationDataObject[] readCustomizations(String filename, boolean print) {
        DataFileReader<CustomizationDataObject> reader = new DataFileReader<>(CustomizationDataObject::new);
        List<CustomizationDataObject> list = reader.readGenericDataFile(filename, print);
        if (list == null) {
            return null;
        }
        CustomizationDataObject[] array = new CustomizationDataObject[list.size()];
        return list.toArray(array);
    }

    public static MonsterLocalizationDataObject[] readMonsterLocalizations(String localization, boolean print) {
        DataFileReader<MonsterLocalizationDataObject> reader = new DataFileReader<>((b, sb) -> new MonsterLocalizationDataObject(b, sb, localization));
        int fileIndex = 0;
        File file;
        List<MonsterLocalizationDataObject> fullList = new ArrayList<>();
        do {
            fileIndex++;
            String path = getLocalizationRoot(localization) + "battle/kernel/monster" + fileIndex + ".bin";
            file = FileAccessorWithMods.resolveFile(path, false);
            if (file.exists()) {
                List<MonsterLocalizationDataObject> list = reader.readGenericDataFile(path, print);
                if (list != null) {
                    fullList.addAll(list);
                }
            }
        } while (file.exists());
        MonsterLocalizationDataObject[] array = new MonsterLocalizationDataObject[fullList.size()];
        return fullList.toArray(array);
    }

    public static SphereGridSphereTypeDataObject[] readSphereGridSphereTypes(String filename, boolean print) {
        DataFileReader<SphereGridSphereTypeDataObject> reader = new DataFileReader<>(SphereGridSphereTypeDataObject::new);
        List<SphereGridSphereTypeDataObject> list = reader.readGenericDataFile(filename, print);
        if (list == null) {
            return null;
        }
        SphereGridSphereTypeDataObject[] array = new SphereGridSphereTypeDataObject[list.size()];
        return list.toArray(array);
    }

    public static SphereGridNodeTypeDataObject[] readSphereGridNodeTypes(String filename, boolean print) {
        DataFileReader<SphereGridNodeTypeDataObject> reader = new DataFileReader<>(SphereGridNodeTypeDataObject::new);
        List<SphereGridNodeTypeDataObject> list = reader.readGenericDataFile(filename, print);
        if (list == null) {
            return null;
        }
        SphereGridNodeTypeDataObject[] array = new SphereGridNodeTypeDataObject[list.size()];
        return list.toArray(array);
    }

    public static SphereGridLayoutDataObject readSphereGridLayout(String layout, String contents, boolean print) {
        int[] fullContentBytes = ChunkedFileHelper.fileToBytes(FileAccessorWithMods.resolveFile(contents, false));
        int[] contentBytes = fullContentBytes != null ? Arrays.copyOfRange(fullContentBytes, 0x8, fullContentBytes.length) : null;
        int[] layoutBytes = ChunkedFileHelper.fileToBytes(FileAccessorWithMods.resolveFile(layout, false));
        if (layoutBytes == null || contentBytes == null) {
            return null;
        }
        SphereGridLayoutDataObject obj = new SphereGridLayoutDataObject(layoutBytes, contentBytes);
        if (print) {
            System.out.println(obj);
        }
        return obj;
    }
}
