package script;

import main.StringHelper;
import model.Nameable;
import reading.Chunk;
import model.MonsterLootDataObject;
import model.MonsterStatDataObject;

import java.util.Arrays;
import java.util.List;

/**
 * jppc/battle/mon/.../.bin
 */
public class MonsterFile implements Nameable {
    public ScriptObject monsterAi;
    public MonsterStatDataObject monsterStatData;
    public MonsterLootDataObject monsterLootData;
    Chunk scriptChunk;
    Chunk audioChunkApparently;
    int[] workerMappingBytes;
    int[] statBytes;
    int[] lootBytes;
    int[] englishTextBytes;

    public MonsterFile(List<Chunk> chunks) {
        mapChunks(chunks);
        mapObjects();
    }

    private void mapChunks(List<Chunk> chunks) {
        scriptChunk = chunks.get(0);
        workerMappingBytes = chunks.get(1).bytes;
        statBytes = chunks.get(2).bytes;
        lootBytes = chunks.get(4).bytes;
        audioChunkApparently = chunks.get(5);
        englishTextBytes = chunks.get(6).bytes;
    }

    private void mapObjects() {
        monsterAi = new ScriptObject(scriptChunk, workerMappingBytes);
        monsterStatData = new MonsterStatDataObject(statBytes, Arrays.copyOfRange(statBytes, MonsterStatDataObject.LENGTH, statBytes.length), "us");
        monsterLootData = new MonsterLootDataObject(lootBytes);
        MonsterStatDataObject englishTextStatData = new MonsterStatDataObject(englishTextBytes, Arrays.copyOfRange(englishTextBytes, MonsterStatDataObject.LENGTH, englishTextBytes.length), "us");
        monsterStatData.setLocalizations(englishTextStatData);
    }

    public void parseScript() {
        if (monsterAi != null) {
            monsterAi.parseScript(null);
        }
    }

    @Override
    public String toString() {
        StringBuilder full = new StringBuilder();
        full.append(getName()).append('\n');
        if (monsterAi != null) {
            full.append("- Script Code -").append('\n');
            full.append(monsterAi.allLinesString());
            full.append("- Headers -").append('\n');
            full.append(monsterAi.headersString()).append('\n');
        } else {
            full.append("Monster AI missing");
        }
        full.append("- Monster Stats -").append('\n');
        full.append(monsterStatData).append('\n');
        full.append("- Monster Loot -").append('\n');
        full.append(monsterLootData).append('\n');
        if (monsterStatData != null) {
            full.append("- Localized Strings -").append('\n');
            full.append("Name: ").append(monsterStatData.monsterName.getDefaultContent()).append('\n');
            full.append("- Sensor Text -\n");
            full.append(monsterStatData.monsterSensorText.getDefaultContent()).append('\n');
            full.append("- Scan Text -").append('\n');
            full.append(monsterStatData.monsterScanText.getDefaultContent()).append('\n');
        }
        return full.toString();
    }

    public String getName(String localization) {
        return monsterStatData != null ? monsterStatData.monsterName.getLocalizedContent(localization) : null;
    }
}
