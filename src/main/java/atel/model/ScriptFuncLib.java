package atel.model;

import java.util.List;

public abstract class ScriptFuncLib {
    private static ScriptFunc[] FUNCS;

    private static boolean initialized = false;

    private static ScriptField p(int index) {
        return p("p" + index, "unknown");
    }
    
    private static ScriptField p(String typeAndName) {
        return new ScriptField(typeAndName);
    }

    private static ScriptField p(String name, String type) {
        return new ScriptField(name, type);
    }

    private static ScriptFunc putUnknownFunc(int idx, String internalName, int inputs) {
        return putUnknownFunc(idx, internalName, "unknown", inputs);
    }

    private static ScriptFunc putUnknownFunc(int idx, String internalName, String returnType, int inputs) {
        ScriptField[] inputList = new ScriptField[inputs];
        for (int i = 0; i < inputs; i++) {
            inputList[i] = p(i+1);
        }
        return putUnknownFunc(idx, internalName, returnType, inputList);
    }

    private static ScriptFunc putUnknownFunc(int idx, String internalName, String returnType, ScriptField... inputList) {
        ScriptFunc func = new ScriptFunc(null, returnType, internalName, inputList);
        return putFuncWithIdx(idx, func);
    }

    private static ScriptFunc putUnknownFunc(int idx, int inputs) {
        return putUnknownFunc(idx, null, inputs);
    }

    private static ScriptFunc putFuncWithIdx(int idx, ScriptFunc func) {
        func.idx = idx;
        func.funcspace = idx / 0x1000;
        FUNCS[idx] = func;
        return func;
    }

    public static ScriptFunc get(int idx, List<StackObject> params) {
        return FUNCS[idx];
    }

    public static void initialize() {
        if (initialized) {
            return;
        }
        initialized = true;
        FUNCS = new ScriptFunc[0x10000];
        putFuncWithIdx(0x0000, new ScriptFunc("wait", "unknown", null, p("frames", "int")));
        putFuncWithIdx(0x0001, new ScriptFunc("loadModel", "unknown", null, p("model")));
        putFuncWithIdx(0x0002, new ScriptFunc("attachToCamera", "unknown", null, p("ID", "int"), p("int"), p("unused", "int")));
        putFuncWithIdx(0x0003, new ScriptFunc("attachToLevelPart", "unknown", null, p("partIndex", "int")));
        putFuncWithIdx(0x0004, new ScriptFunc("attachToMapGroup", "unknown", null, p("groupIndex", "int")));
        putFuncWithIdx(0x0005, new ScriptFunc("applyTransform", "unknown", null, true));
        putUnknownFunc(0x0006, 3);
        putUnknownFunc(0x0007, 1);
        putFuncWithIdx(0x0010, new ScriptFunc("getEntranceIndex", "int", null, true));
        putFuncWithIdx(0x0011, new ScriptFunc("transitionToMap?", "unknown", null, p("map"), p("entranceIndex?", "int")));
        putUnknownFunc(0x0012, 2);
        putFuncWithIdx(0x0013, new ScriptFunc("setPosition", "unknown", null, p("x", "float"), p("y", "float"), p("z", "float")));
        putFuncWithIdx(0x0015, new ScriptFunc("setDestination", "unknown", null, p("x", "float"), p("y", "float"), p("z", "float")));
        putFuncWithIdx(0x0016, new ScriptFunc("setMotionSpeed", "unknown", null, p("speed", "float")));
        putUnknownFunc(0x0017, 1); // noclip: set motion threshold/radius, used for collision and checking if a destination is reached
        putFuncWithIdx(0x0018, new ScriptFunc("startMotion", "unknown", null, p("activeBits", "int"), p("flags", "bitfield"), p("targetWorker", "worker")));
        putFuncWithIdx(0x0019, new ScriptFunc("startRotation", "unknown", null, p("activeBits", "int"), p("flags", "bitfield"), p("targetWorker", "worker"))); // see noclip for flags
        putFuncWithIdx(0x001A, new ScriptFunc("waitForMotion", "unknown", null));
        putFuncWithIdx(0x001B, new ScriptFunc("waitForRotation", "unknown", null));
        putFuncWithIdx(0x001C, new ScriptFunc("setMotionTiming", "unknown", null, p("currTime?", "float"), p("duration?", "int")));
        putFuncWithIdx(0x001D, new ScriptFunc("setRotationTiming", "unknown", null, p("currTime?", "float"), p("duration?", "int")));
        putFuncWithIdx(0x001F, new ScriptFunc("yawToDestination", "float", null));
        putFuncWithIdx(0x0020, new ScriptFunc("pitchToDestination", "float", null));
        putUnknownFunc(0x0021, 1);
        putFuncWithIdx(0x0023, new ScriptFunc("setVelocityYaw", "unknown", null, p("angle", "float"))); // Never used by the game
        putFuncWithIdx(0x0024, new ScriptFunc("setVelocityPitch", "unknown", null, p("angle", "float"))); // Never used by the game
        putUnknownFunc(0x0025, 1); // noclip: 25-7 set a contextually-dependent vector, sometimes euler angles, 26/27 are never used by the game
        putFuncWithIdx(0x0028, new ScriptFunc("setRotationTarget1", "unknown", null, p("angle", "float")));
        putFuncWithIdx(0x0029, new ScriptFunc("setRotationTarget2", "unknown", null, p("angle", "float")));
        putFuncWithIdx(0x002A, new ScriptFunc("setRotationTarget3", "unknown", null, p("angle", "float")));
        putUnknownFunc(0x002B, 1);
        putUnknownFunc(0x002C, 1);
        putUnknownFunc(0x002D, 1);
        putUnknownFunc(0x002E, 1);
        putUnknownFunc(0x002F, 1);
        putUnknownFunc(0x0030, 1);
        putFuncWithIdx(0x0033, new ScriptFunc("getWorkerIndex", "int", null, p("worker"))); // maybe arg is only useful for battle, would be just echoed back in events
        putFuncWithIdx(0x0034, new ScriptFunc("enableInteractionAtLevel", "unknown", null, p("level", "int"))); // "level" from game, "thread" in noclip (like signal priority)
        putFuncWithIdx(0x0035, new ScriptFunc("disableInteractionAtLevel", "unknown", null, p("level", "int")));
        putFuncWithIdx(0x0036, new ScriptFunc("stopOtherMotion", "unknown", null, p("worker")));
        putUnknownFunc(0x0037, 1);
        putFuncWithIdx(0x0038, new ScriptFunc("getWorkerX?", "float", null, p("worker")));
        putFuncWithIdx(0x0039, new ScriptFunc("getWorkerY?", "float", null, p("worker")));
        putFuncWithIdx(0x003A, new ScriptFunc("getWorkerZ?", "float", null, p("worker")));
        putUnknownFunc(0x003D, 0);
        putUnknownFunc(0x003F, 0);
        putFuncWithIdx(0x0042, new ScriptFunc("linkWorkerToChr", "unknown", null, p("btlChr")));
        putUnknownFunc(0x0043, 0);
        putUnknownFunc(0x0044, 0);
        putUnknownFunc(0x0046, 0);
        putUnknownFunc(0x0047, 0);
        putUnknownFunc(0x004C, 1);
        putFuncWithIdx(0x004D, new ScriptFunc("controllerButtonPressed1?", "bool", null, p("controllerButton")));
        putUnknownFunc(0x0050, 1);
        putFuncWithIdx(0x0051, new ScriptFunc("controllerButtonPressed2?", "bool", null, p("controllerButton")));
        putFuncWithIdx(0x0054, new ScriptFunc("setCollisionEdgePosition", "unknown", null, p("x1", "float"), p("y1", "float"), p("z1", "float"), p("x2", "float"), p("y2", "float"), p("z2", "float")));
        putFuncWithIdx(0x0055, new ScriptFunc("setCollisionHeightRange", "unknown", null, p("range", "float")));
        putFuncWithIdx(0x0056, new ScriptFunc("setCollisionShapeActive", "unknown", null, p("active", "bool")));
        putFuncWithIdx(0x0057, new ScriptFunc("setCollisionZoneCenter", "unknown", null, p("x", "float"), p("y", "float"), p("z", "float")));
        putFuncWithIdx(0x0058, new ScriptFunc("setCollisionZoneSize", "unknown", null, p("xRange", "float"), p("zRange", "float")));
        putFuncWithIdx(0x0059, new ScriptFunc("setCollisionZoneSize", "unknown", null, p("xRange", "float"), p("yRange", "float"), p("zRange", "float")));
        putUnknownFunc(0x005A, 1); // noclip: collision detection
        putFuncWithIdx(0x005C, new ScriptFunc("setCollisionShapeActive", "unknown", null, p("active", "bool"))); // same as 006C
        putFuncWithIdx(0x005D, new ScriptFunc("enablePlayerControl?", "unknown", null, true));
        putFuncWithIdx(0x005E, new ScriptFunc("disablePlayerControl?", "unknown", null, true));
        putFuncWithIdx(0x005F, new ScriptFunc("halt", "unknown", null, true));
        putUnknownFunc(0x0060, 1);
        putFuncWithIdx(0x0061, new ScriptFunc("setTalkRange?", "unknown", null, p("range", "float")));
        putUnknownFunc(0x0062, 1); // noclip: collision detection
        putUnknownFunc(0x0063, 1); // noclip: collision detection
        putFuncWithIdx(0x0064, new ScriptFunc("displayFieldString", "unknown", null, p("boxIndex", "int"), p("string", "localString")));
        putFuncWithIdx(0x0065, new ScriptFunc("positionText", "unknown", null, p("boxIndex", "int"), p("x", "int"), p("y", "int"), p("align", "textAlignment")));
        putFuncWithIdx(0x0066, new ScriptFunc("setTextHasTransparentBackdrop", "unknown", null, p("boxIndex", "int"), p("transparent", "bool")));
        putFuncWithIdx(0x0069, new ScriptFunc(p("boxIndex", "int"), p("int"), p("int")));
        putFuncWithIdx(0x006A, new ScriptFunc(p("boxIndex", "int"), p(2)));
        putFuncWithIdx(0x006B, new ScriptFunc(p("boxIndex", "int")));
        putFuncWithIdx(0x006C, new ScriptFunc("setMovementSpeed", "unknown", null, p("speed", "float")));
        putUnknownFunc(0x006D, 1);
        putUnknownFunc(0x006E, 1);
        putFuncWithIdx(0x006F, new ScriptFunc("setAllRotationRate1", "unknown", null, p("rate", "float")));
        putFuncWithIdx(0x0070, new ScriptFunc("setAllRotationRate2", "unknown", null, p("rate", "float")));
        putFuncWithIdx(0x0071, new ScriptFunc("setAllRotationRate3", "unknown", null, p("rate", "float")));
        putUnknownFunc(0x0074, 1); // noclip: get/set frames?
        putFuncWithIdx(0x0076, new ScriptFunc("getWorkerType", "eventWorkerType", null));
        putFuncWithIdx(0x0077, new ScriptFunc("stopWorkerMotion", "unknown", null, p("worker")));
        putFuncWithIdx(0x0078, new ScriptFunc("stopWorkerRotation", "unknown", null, p("worker")));
        putUnknownFunc(0x007A, 2); // noclip: collision detection
        putFuncWithIdx(0x007C, new ScriptFunc("waitForText", "unknown", null, p("boxIndex", "int"))); // equivalent to 0x0084 with second param 0
        putFuncWithIdx(0x007D, new ScriptFunc("?awaitPlayerDialogueChoice", "int", null, p("boxIndex", "int")));
        putUnknownFunc(0x007F, 1);
        putUnknownFunc(0x0080, 0); // noclip: getMapPointPosition1()
        putUnknownFunc(0x0081, 0); // noclip: getMapPointPosition2()
        putUnknownFunc(0x0082, 0); // noclip: getMapPointPosition3()
        putUnknownFunc(0x0083, 0); // noclip: getMapPointHeading()
        putFuncWithIdx(0x0084, new ScriptFunc("waitForText", "unknown", null, p("boxIndex", "int"), p(2)));
        putUnknownFunc(0x0085, 1); // noclip: setCollisionRadius()
        putUnknownFunc(0x0086, 0);
        putFuncWithIdx(0x0087, new ScriptFunc("?CurrentMap", "int", null, false));
        putUnknownFunc(0x0088, 0);
        putUnknownFunc(0x0089, 0);
        putUnknownFunc(0x008B, 0); // noclip: checking line crossing
        putUnknownFunc(0x008D, 1);
        putUnknownFunc(0x008E, 1); // noclip: setCollisionHeight()
        putFuncWithIdx(0x008F, new ScriptFunc("isTextGone", "bool", null, p("boxIndex", "int")));
        putUnknownFunc(0x0090, 0); // noclip: getYaw1()
        putUnknownFunc(0x0091, 0);
        putUnknownFunc(0x0092, 1); // noclip: getYaw2()
        putUnknownFunc(0x0093, 1);
        putFuncWithIdx(0x0094, new ScriptFunc("setGravity", "unknown", null, p("g", "int")));
        putFuncWithIdx(0x0095, new ScriptFunc(p("float"))); // noclip: setRotationYaw()
        putUnknownFunc(0x0096, 1); // noclip: setRotationPitch()
        putFuncWithIdx(0x0097, new ScriptFunc("getTextState", "textState", null, p("boxIndex", "int")));
        putUnknownFunc(0x0098, 2);
        putUnknownFunc(0x009A, 1);
        putUnknownFunc(0x009B, 2);
        putFuncWithIdx(0x009D, new ScriptFunc("setTextFlags", "unknown", null, p("boxIndex", "int"), p("textFlags", "textFlagBitfield")));
        putFuncWithIdx(0x009E, new ScriptFunc("prepareStringIntVariable", "unknown", null, p("boxIndex", "int"), p("varIndex", "int"), p("stringVarType"), p("value", "int")));
        putUnknownFunc(0x00A2, 5);
        putFuncWithIdx(0x00A3, new ScriptFunc("getWorkerPosition", "unknown", null, p("worker"), p("xDest", "pointer"), p("yDest", "pointer"), p("zDest", "pointer")));
        putUnknownFunc(0x00A4, 2);
        putFuncWithIdx(0x00A5, new ScriptFunc("horizontalWorkerDist", "unknown", null, p("workerA", "worker"), p("workerB", "worker")));
        putFuncWithIdx(0x00A6, new ScriptFunc("GetRandomInRange", "int", null, p("maxExclusive", "int")));
        putUnknownFunc(0x00A7, 1); // noclip: setIsNotActive()
        putUnknownFunc(0x00A8, 1); // noclip: setIsActive()
        putFuncWithIdx(0x00A9, new ScriptFunc("GetRandomValue", "int", null, true));
        putFuncWithIdx(0x00AA, new ScriptFunc("SetRandomEncountersActive", "unknown", null, p("active", "bool")));
        putUnknownFunc(0x00AB, 2);
        putUnknownFunc(0x00AC, 1);
        putUnknownFunc(0x00B1, 1);
        putUnknownFunc(0x00B2, 1);
        putUnknownFunc(0x00B3, 1);
        putUnknownFunc(0x00B4, 1);
        putUnknownFunc(0x00BA, 0);
        putUnknownFunc(0x00BB, 1);
        putUnknownFunc(0x00BC, 1);
        putUnknownFunc(0x00BD, 1);
        putUnknownFunc(0x00BE, 1);
        putUnknownFunc(0x00BF, 1); // \
        putUnknownFunc(0x00C0, 1); // -- Technically these aren't certain (zkrn0300)
        putUnknownFunc(0x00C1, 1); // /
        putUnknownFunc(0x00C2, 1); // noclip: getHeading()
        putUnknownFunc(0x00C4, 0);
        putFuncWithIdx(0x00C5, new ScriptFunc("registerTurnMotions?", "unknown", null, p("p1", "motion"), p("p2", "motion"), p("p3", "motion"), p("p4", "motion")));
        putUnknownFunc(0x00C6, 0);
        putUnknownFunc(0x00C7, null, "motion", 1);
        putUnknownFunc(0x00C8, 3);
        putFuncWithIdx(0x00C9, new ScriptFunc("getScriptEntrypointForEntrance", "int", null, p("mapEntrance", "int"))); // a signal is sent for this entrypoint to an event-defined worker when the map loads
        putFuncWithIdx(0x00CA, new ScriptFunc("addPartyMember", "unknown", null, p("playerChar")));
        putFuncWithIdx(0x00CB, new ScriptFunc("removePartyMember", "unknown", null, p("playerChar"))); // Party member is greyed out in the list
        putUnknownFunc(0x00CC, 1);
        putUnknownFunc(0x00CE, 1);
        putUnknownFunc(0x00CF, 1);
        putUnknownFunc(0x00D0, 0);
        putUnknownFunc(0x00D1, 0);
        putUnknownFunc(0x00D2, 0);
        putFuncWithIdx(0x00D5, new ScriptFunc("playFieldVoiceLine", "unknown", null, p("voiceFileIndex", "int")));
        putUnknownFunc(0x00D6, 0);
        putUnknownFunc(0x00D7, 0);
        putUnknownFunc(0x00D8, 0);
        putUnknownFunc(0x00D9, 0);
        putUnknownFunc(0x00DA, 0);
        putFuncWithIdx(0x00DB, new ScriptFunc("loadAndPlayMoveAnimation", "unknown", null, p("moveAnim")));
        putFuncWithIdx(0x00DC, new ScriptFunc("awaitLoadingOfMoveAnimation", "unknown", null, true));
        putUnknownFunc(0x00DE, 0);
        putFuncWithIdx(0x00DF, new ScriptFunc("playSfx", "unknown", null, p("sfx")));
        putUnknownFunc(0x00E0, 1);
        putUnknownFunc(0x00E1, 0);
        putUnknownFunc(0x00E2, 1);
        putUnknownFunc(0x00E3, 1);
        putUnknownFunc(0x00E4, 1);
        putUnknownFunc(0x00E6, 1);
        putFuncWithIdx(0x00E7, new ScriptFunc("putPartyMemberInSlot", "unknown", null, p("slot", "int"), p("playerChar")));
        putFuncWithIdx(0x00E8, new ScriptFunc("storeFrontlineInArray", "unknown", null, p("slot1", "pointer"), p("slot2", "pointer"), p("slot3", "pointer")));
        putUnknownFunc(0x00E9, 1);
        putUnknownFunc(0x00EB, 0);
        putFuncWithIdx(0x00EC, new ScriptFunc("findPartyMemberFormationIndex", "int", null, p("playerChar")));
        putFuncWithIdx(0x00ED, new ScriptFunc("getWorkerLinkedChr", "btlChr", null, true)); // get ID of attached actor
        putUnknownFunc(0x00EE, 1);
        putUnknownFunc(0x00EF, 1);
        putUnknownFunc(0x00F2, 1);
        putUnknownFunc(0x00F3, 1);
        putFuncWithIdx(0x00F4, new ScriptFunc("pointToYaw?", "float", null, p("x", "float"), p("y", "float"), p("z", "float"))); // y is unused
        putFuncWithIdx(0x00F5, new ScriptFunc("pointToPitch?", "float", null, p("x", "float"), p("y", "float"), p("z", "float")));
        putFuncWithIdx(0x00F6, new ScriptFunc("waitForWorker", "float", null, p("level", "int"), p("worker"))); // waits until no signal is active or queued at level for the worker
        putUnknownFunc(0x00F8, 3);
        putUnknownFunc(0x00F9, 0);
        putUnknownFunc(0x00FA, 1);
        putFuncWithIdx(0x00FB, new ScriptFunc("getCurrentHoveredDialogueChoice", "int", null, p("boxIndex", "int")));
        putUnknownFunc(0x00FD, 3);
        putUnknownFunc(0x00FE, 2);
        putUnknownFunc(0x0100, 3);
        putFuncWithIdx(0x0102, new ScriptFunc("setBgmToLoad?", "unknown", null, p("bgm")));
        putFuncWithIdx(0x0103, new ScriptFunc("unloadBgm?", "unknown", null, p("bgm")));
        putFuncWithIdx(0x0104, new ScriptFunc("playBgm?", "unknown", null, p("bgm")));
        putFuncWithIdx(0x0105, new ScriptFunc("loadBgm?", "unknown", null, true));
        putUnknownFunc(0x0106, 3);
        putUnknownFunc(0x0107, 2);
        putUnknownFunc(0x0108, 5);
        putUnknownFunc(0x0109, 1);
        putUnknownFunc(0x010A, 1);
        putFuncWithIdx(0x010B, new ScriptFunc("warpToMap?", "unknown", null, p("map"), p("entranceIndex?", "int")));
        putUnknownFunc(0x010C, 2);
        putFuncWithIdx(0x010D, new ScriptFunc("SetPrimerCollected", "unknown", null, p("primerIndex", "int")));
        putFuncWithIdx(0x010E, new ScriptFunc("CollectedPrimersBitfield", "bitfield", null, false));
        putUnknownFunc(0x010F, 4);
        putUnknownFunc(0x0110, 1);
        putFuncWithIdx(0x0111, new ScriptFunc("StorePartyMemberSetup", "unknown", null, true));
        putFuncWithIdx(0x0112, new ScriptFunc("RestorePartyMemberSetup", "unknown", null, true));
        putUnknownFunc(0x0114, 2);
        putUnknownFunc(0x0115, 1);
        putUnknownFunc(0x0116, 1);
        putUnknownFunc(0x0117, 1);
        putUnknownFunc(0x0119, 1);
        putFuncWithIdx(0x011A, new ScriptFunc(p("p1", "pointer"), p(2), p("p3", "pointer")));
        putFuncWithIdx(0x011B, new ScriptFunc("setSplashSprite", "unknown", null, p("splashIndex", "int"), p("pointer")));
        putFuncWithIdx(0x011C, new ScriptFunc("hide?Splash", "unknown", null, p("splashIndex", "int")));
        putFuncWithIdx(0x011D, new ScriptFunc("?setSplashAlpha", "unknown", null, p("splashIndex", "int"), p("alpha", "int")));
        // 011E sets a brightness value of splash
        putFuncWithIdx(0x011F, new ScriptFunc("?setSplashPosition", "unknown", null, p("splashIndex", "int"), p("x?", "int"), p("y?", "int")));
        putFuncWithIdx(0x0120, new ScriptFunc("scale?Splash", "unknown", null, p("splashIndex", "int"), p("scaleX?", "float"), p("scaleY?", "float")));
        putFuncWithIdx(0x0121, new ScriptFunc("movementStickTilt?", "int", null, p("axis", "int")));
        putUnknownFunc(0x0122, 1);
        putFuncWithIdx(0x0126, new ScriptFunc("warpToPoint", "float", null, p("x", "float"), p("y", "float"), p("z", "float")));
        putUnknownFunc(0x0127, 1);
        putUnknownFunc(0x0128, 2);
        putFuncWithIdx(0x0129, new ScriptFunc("?setSplashColor", "unknown", null, p("splashIndex", "int"), p("r?", "int"), p("g?", "int"), p("b?", "int")));
        putUnknownFunc(0x012A, 2);
        putUnknownFunc(0x012B, 1);
        // putFuncWithIdx(0x012B, new ScriptFunc("workerWithinRadius", "bool", null, p("worker"), p("radius", "int"))); // Not sure how this happened, but this seems to be wrong. It's a 1 argument function for sure.
        putFuncWithIdx(0x012D, new ScriptFunc("GetAffectionValue", "int", null, p("char")));
        putFuncWithIdx(0x012E, new ScriptFunc("IncreaseAffectionValue", "unknown", null, p("char"), p("amount", "int")));
        putFuncWithIdx(0x012F, new ScriptFunc("DecreaseAffectionValue", "unknown", null, p("char"), p("amount", "int"))); // Never used, will set to 0 if it would go negative
        putFuncWithIdx(0x0130, new ScriptFunc("SetAffectionValue", "unknown", null, p("char"), p("amount", "int")));
        putFuncWithIdx(0x0132, new ScriptFunc("setActorCollisionActive", "unknown", null, p("worker"), p("active", "bool")));
        putFuncWithIdx(0x0133, new ScriptFunc("setActorCollisionActive2", "unknown", null, p("worker"), p("active", "bool"))); // same as 0132
        putFuncWithIdx(0x0134, new ScriptFunc("loadModel2", "unknown", null, p("model"))); // same as 0001
        putFuncWithIdx(0x0135, new ScriptFunc("PlayerTotalGil", "int", null, false));
        putFuncWithIdx(0x0136, new ScriptFunc("obtainGil", "unknown", null, p("amount", "int")));
        putFuncWithIdx(0x0137, new ScriptFunc("tryPayGil", "bool", null, p("amount", "int")));
        putUnknownFunc(0x0138, 1);
        putUnknownFunc(0x0139, 8);
        putFuncWithIdx(0x0139, new ScriptFunc("requestNumericInput?", "int", null, p(1), p(2), p(3), p(4), p(5), p("x?", "int"), p("y?", "int"), p("align", "textAlignment")));
        putFuncWithIdx(0x013B, new ScriptFunc("displayFieldChoice", "int", null, p("boxIndex", "int"), p("string", "localString"), p(3), p(4), p("x", "int"), p("y", "int"), p("align", "textAlignment")));
        putUnknownFunc(0x013D, 1);
        putUnknownFunc(0x013E, 1);
        putFuncWithIdx(0x013F, new ScriptFunc("resolveStringMacro", "macroString", null, p("section", "stringVarType"), p("entryIndex", "int")));
        putUnknownFunc(0x0140, 6);
        putFuncWithIdx(0x0141, new ScriptFunc("?setBoxToConstructedString", "unknown", null, p("boxIndex", "int"), p("constructedString", "unknown"), p("pointer?", "pointer")));
        putUnknownFunc(0x0142, 2);
        putUnknownFunc(0x0143, 1);
        putUnknownFunc(0x0144, 1);
        putUnknownFunc(0x0145, 2);
        putUnknownFunc(0x0146, 1);
        putUnknownFunc(0x0148, 2);
        putUnknownFunc(0x0149, 1);
        putFuncWithIdx(0x014A, new ScriptFunc("SetBattleFlags", "unknown", null, p("p1", "battleFlags1Bitfield"), p(2), p(3)));
        putFuncWithIdx(0x014D, new ScriptFunc("HighestAffectionChar", "char", null, false));
        putFuncWithIdx(0x014E, new ScriptFunc("analogStickAngle", "float", null, p("deadzone", "float"), p("x", "float"), p("y", "float")));
        putUnknownFunc(0x014F, 3);
        putUnknownFunc(0x0151, 1);
        putUnknownFunc(0x0154, 0);
        putUnknownFunc(0x0155, 1);
        putUnknownFunc(0x0156, 0);
        putUnknownFunc(0x0157, 1);
        putUnknownFunc(0x0158, 1);
        putUnknownFunc(0x0159, 2);
        putFuncWithIdx(0x015B, new ScriptFunc("obtainTreasure", "unknown", null, p("textboxIndex", "int"), p("treasure")));
        putUnknownFunc(0x015D, 1);
        putUnknownFunc(0x015E, 1);
        putFuncWithIdx(0x015F, new ScriptFunc(p("?addKeyItem")));
        putFuncWithIdx(0x0160, new ScriptFunc("hasKeyItem", "bool", null, p("keyItem")));
        putFuncWithIdx(0x0161, new ScriptFunc("?removeKeyItem", p("keyItem")));
        putUnknownFunc(0x0166, 1);
        putUnknownFunc(0x0167, 0);
        putFuncWithIdx(0x016A, new ScriptFunc(p("bgm"), p(2)));
        putFuncWithIdx(0x016B, new ScriptFunc(p("bgm"), p(2), p(3)));
        putUnknownFunc(0x016C, 0);
        putFuncWithIdx(0x016D, new ScriptFunc(p("bgm")));
        putUnknownFunc(0x016E, 2);
        putUnknownFunc(0x016F, 1);
        putFuncWithIdx(0x0171, new ScriptFunc("fillPartyMemberHp", "unknown", null, p("playerChar")));
        putFuncWithIdx(0x0172, new ScriptFunc("fillPartyMemberMp", "unknown", null, p("playerChar")));
        putUnknownFunc(0x0174, 0);
        putUnknownFunc(0x0177, 1);
        putUnknownFunc(0x0179, 1);
        putUnknownFunc(0x017A, 1);
        putUnknownFunc(0x017B, 1);
        putUnknownFunc(0x017C, 1);
        putFuncWithIdx(0x017E, new ScriptFunc("playBgmWithVol?", "unknown", null, p("bgm"), p(2)));
        putUnknownFunc(0x017F, 1);
        putUnknownFunc(0x0180, 2);
        putUnknownFunc(0x0181, 0);
        putUnknownFunc(0x0184, 1);
        putUnknownFunc(0x0185, 2);
        putUnknownFunc(0x0188, 1);
        putUnknownFunc(0x0189, 1);
        putUnknownFunc(0x018A, 1);
        putUnknownFunc(0x018B, 1);
        putUnknownFunc(0x018F, 2);
        putUnknownFunc(0x0192, 1);
        putUnknownFunc(0x0193, 1);
        putUnknownFunc(0x0194, 1);
        putUnknownFunc(0x0195, 0);
        putFuncWithIdx(0x0196, new ScriptFunc("bindGfxToSelf", "unknown", null, p("gfxIndex", "int"))); // only works for level geometry
        putUnknownFunc(0x0197, 1);
        putFuncWithIdx(0x0198, new ScriptFunc("enableVisualEffectIndex", "unknown", null, p("levelPartIndex", "int"), p("effectType"), p("runOnce", "bool")));
        putFuncWithIdx(0x0199, new ScriptFunc("enableOwnVisualEffectIndex", "unknown", null, p("effectType"), p("runOnce", "bool")));
        putUnknownFunc(0x019C, 0);
        putFuncWithIdx(0x019D, new ScriptFunc("detach", "unknown", null, p("unloadModel", "bool")));
        putUnknownFunc(0x019E, 0);
        putUnknownFunc(0x019F, 1);
        putUnknownFunc(0x01A0, 3);
        putFuncWithIdx(0x01A4, new ScriptFunc("setVisualEffectParameter", "unknown", null, p("partIndex", "int"), p("value", "float")));
        putFuncWithIdx(0x01A5, new ScriptFunc("setAnimatedTextureFrame", "unknown", null, p("textureIndex", "int"), p("frame", "int"))); // used for blitzball scoreboard
        putUnknownFunc(0x01A6, 0);
        putFuncWithIdx(0x01A7, new ScriptFunc("obtainTreasureSilently", "unknown", null, p("treasure")));
        putUnknownFunc(0x01A8, 1);
        putFuncWithIdx(0x01AB, new ScriptFunc("TotalBattlesFought", "int", null, false));
        putUnknownFunc(0x01AC, 1);
        putUnknownFunc(0x01AD, 1);
        putUnknownFunc(0x01AF, 2);
        putUnknownFunc(0x01B0, 2);
        putUnknownFunc(0x01B1, 0);
        putFuncWithIdx(0x01B2, new ScriptFunc("getItemCount", "int", null, p("item", "move")));
        putUnknownFunc(0x01B5, 0);
        putFuncWithIdx(0x01B6, new ScriptFunc("?isBrotherhoodUnpowered", "bool", null, true));
        putFuncWithIdx(0x01B7, new ScriptFunc("applyBrotherhoodPowerup", "unknown", null, p("treasure")));
        putFuncWithIdx(0x01B8, new ScriptFunc("enableBrotherhood", "unknown", null, true));
        putFuncWithIdx(0x01BA, new ScriptFunc(p("worker"), p(2)));
        putUnknownFunc(0x01BB, 0);
        putUnknownFunc(0x01BC, 1);
        putUnknownFunc(0x01BD, 1);
        putUnknownFunc(0x01BF, 0);
        putUnknownFunc(0x01C0, 2);
        putUnknownFunc(0x01C2, 0);
        putUnknownFunc(0x01C3, 1);
        putUnknownFunc(0x01C4, 1);
        putUnknownFunc(0x01C5, 2);
        putUnknownFunc(0x01C6, 0);
        putFuncWithIdx(0x01C8, new ScriptFunc("disableOwnVisualEffect", "unknown", null, p("effectType", "int"))); // Never used by the game
        putFuncWithIdx(0x01C9, new ScriptFunc("disableVisualEffect", "unknown", null, p("levelPartIndex", "int"), p("effectType")));
        putUnknownFunc(0x01CA, 0);
        putFuncWithIdx(0x01CC, new ScriptFunc("CurrentPlayingMusic", "bgm", null, false));
        putUnknownFunc(0x01CE, 0);
        putUnknownFunc(0x01CF, 0);
        putUnknownFunc(0x01D0, 0);
        putUnknownFunc(0x01D1, 1);
        putFuncWithIdx(0x01D2, new ScriptFunc("enteredAirshipPasswordEquals", "bool", null, p("string", "localString")));
        putUnknownFunc(0x01D4, 2);
        putFuncWithIdx(0x01D9, new ScriptFunc("?ActivateRikkuName", "unknown", null, true));
        putUnknownFunc(0x01DA, 4);
        putUnknownFunc(0x01DC, 0);
        putUnknownFunc(0x01DE, 0);
        putUnknownFunc(0x01E0, 1);
        putUnknownFunc(0x01E2, 0);
        putUnknownFunc(0x01E3, 0);
        putUnknownFunc(0x01E4, 4);
        putUnknownFunc(0x01E5, 3);
        putUnknownFunc(0x01E6, 3);
        putUnknownFunc(0x01E7, 2);
        putUnknownFunc(0x01E9, 2);
        putUnknownFunc(0x01EB, 1);
        putUnknownFunc(0x01EE, 0);
        putUnknownFunc(0x01F0, 1);
        putFuncWithIdx(0x01F2, new ScriptFunc("getActiveAeonsBitfield", "aeonsBitfield", null, true));
        putFuncWithIdx(0x01F3, new ScriptFunc("?constructAeonChoiceString", "unknown", null, p("choosableAeons", "aeonsBitfield"), p("totalAeons", "aeonsBitfield"), p("pointer")));
        putFuncWithIdx(0x01F4, new ScriptFunc("?getAeonFromChoice", "playerChar", null, p("choice", "int"), p("aeonsBitfield")));
        putUnknownFunc(0x01F5, 1);
        putFuncWithIdx(0x01F6, new ScriptFunc("pressControllerButton", "unknown", null, p("controllerButton")));
        putUnknownFunc(0x01F8, 2);
        putFuncWithIdx(0x01F9, new ScriptFunc("removePartyMemberCompletely", "unknown", null, p("playerChar"))); // Party member is hidden in the list
        putUnknownFunc(0x01FA, 0);
        putUnknownFunc(0x01FB, 0);
        putFuncWithIdx(0x01FC, new ScriptFunc("teachAbilityToPartyMemberSilently", "unknown", null, p("playerChar"), p("charMove")));
        putUnknownFunc(0x0200, 1);
        putUnknownFunc(0x0201, 0);
        putUnknownFunc(0x0202, 1);
        putFuncWithIdx(0x0203, new ScriptFunc(p("model")));
        putUnknownFunc(0x0204, 1);
        putUnknownFunc(0x0205, 0);
        putUnknownFunc(0x0206, 0);
        putUnknownFunc(0x0207, 0);
        putUnknownFunc(0x0209, 0);
        putUnknownFunc(0x020A, 0);
        putUnknownFunc(0x020B, 0);
        putUnknownFunc(0x020D, 1);
        putUnknownFunc(0x020F, 1);
        putFuncWithIdx(0x0210, new ScriptFunc("setMonsterArenaUnlocked", "unknown", null, p("monsterArenaUnlock")));
        putUnknownFunc(0x0212, 2);
        putUnknownFunc(0x0213, 1);
        putFuncWithIdx(0x0215, new ScriptFunc("grantCelestialUpgrade", "unknown", null, p("playerChar"), p("level", "int")));
        putFuncWithIdx(0x0216, new ScriptFunc("teachAbilityToPartyMemberWithMsg", "unknown", null, p("boxIndex?", "int"), p("playerChar"), p("charMove")));
        putUnknownFunc(0x0217, 0);
        putUnknownFunc(0x021A, 2);
        putUnknownFunc(0x021B, 2);
        putFuncWithIdx(0x021D, new ScriptFunc("equipBrotherhoodToTidus", "unknown", null, true));
        putUnknownFunc(0x021E, 0);
        putUnknownFunc(0x0220, 1);
        putUnknownFunc(0x0221, 1);
        putFuncWithIdx(0x0225, new ScriptFunc("setShopPrices", "unknown", null, p("percentage", "int")));
        putUnknownFunc(0x0226, 1);
        putUnknownFunc(0x0229, 0);
        putUnknownFunc(0x022B, 0);
        putUnknownFunc(0x022D, 1);
        putUnknownFunc(0x022E, 4);
        putUnknownFunc(0x022F, 2);
        putUnknownFunc(0x0231, 1);
        putFuncWithIdx(0x0232, new ScriptFunc("pressControllerButton2", "unknown", null, p("controllerButton")));
        putUnknownFunc(0x0233, 1);
        putUnknownFunc(0x0234, 0);
        putUnknownFunc(0x0235, 0);
        putFuncWithIdx(0x0236, new ScriptFunc("?getStringAsChoice", "unknown", null, p("string", "localString")));
        putFuncWithIdx(0x0237, new ScriptFunc("?constructChoiceStringFromChoiceArray", "unknown", null, p("choiceCount", "int"), p("?availableChoiceBitfield", "bitfield"), p("pointerToChoiceArray", "pointer"), p("pointer2", "pointer")));
        putUnknownFunc(0x0239, 1);
        putUnknownFunc(0x023A, 1);
        putUnknownFunc(0x023B, 1); // These two could add up to 3 differently...
        putUnknownFunc(0x023C, 2); // But this seems most reasonable. Check nagi0700
        putUnknownFunc(0x023D, 0);
        putUnknownFunc(0x023E, 1);
        putUnknownFunc(0x023F, 1);
        putFuncWithIdx(0x0240, new ScriptFunc("setPartyMemberEquipmentHidden", "unknown", null, p("playerChar"), p("hide", "bool")));
        putUnknownFunc(0x0241, 1);
        putUnknownFunc(0x0242, 0);
        putUnknownFunc(0x0243, 0);
        putUnknownFunc(0x0244, 0);
        putUnknownFunc(0x0245, 0);
        putUnknownFunc(0x024A, 1);
        putUnknownFunc(0x024B, 2);
        putUnknownFunc(0x024C, 0);
        putUnknownFunc(0x024D, 1);
        putUnknownFunc(0x0251, 1);
        putUnknownFunc(0x0253, 0);
        putFuncWithIdx(0x0254, new ScriptFunc("setSphereGrid", "unknown", null, p("sphereGrid")));
        putUnknownFunc(0x0255, 0);
        putUnknownFunc(0x0256, 1);
        putUnknownFunc(0x0257, 1);
        putUnknownFunc(0x0259, 2);
        putFuncWithIdx(0x025B, new ScriptFunc("unlockAchievement", "unknown", null, p("achievement")));
        putUnknownFunc(0x025C, 0);
        putUnknownFunc(0x025D, 1);
        putUnknownFunc(0x025E, 1);
        putUnknownFunc(0x025F, 2);
        putUnknownFunc(0x0260, 1);
        putUnknownFunc(0x0261, 1);
        putUnknownFunc(0x0262, 0);
        putUnknownFunc(0x0263, 0);
        putUnknownFunc(0x0264, 1);
        putUnknownFunc(0x0265, 0);
        putUnknownFunc(0x0266, 0);
        putUnknownFunc(0x0267, 7);
        putUnknownFunc(0x1000, 4);
        putFuncWithIdx(0x1001, new ScriptFunc("sin", "float", null, p("float")));
        putFuncWithIdx(0x1002, new ScriptFunc("cos", "float", null, p("float")));
        putFuncWithIdx(0x1005, new ScriptFunc("atan2", "float", null, p("y?", "float"), p("x?","float")));
        putFuncWithIdx(0x1006, new ScriptFunc("sqrt", "float", null, p("float")));
        putUnknownFunc(0x100A, 1);
        putUnknownFunc(0x1013, 1);
        putUnknownFunc(0x1015, 6);
        putFuncWithIdx(0x1019, new ScriptFunc("abs", "float", null, p("float")));
        putFuncWithIdx(0x101A, new ScriptFunc("distance", "float", null, p("x1", "float"), p("y1", "float"), p("x2", "float"), p("y2", "float")));
        putUnknownFunc(0x101B, 6);
        putUnknownFunc(0x101C, 2);
        putUnknownFunc(0x4001, 1);
        putFuncWithIdx(0x4003, new ScriptFunc("fadeinFromColor", "unknown", null, p("frames", "int"), p("red", "int"), p("green", "int"), p("blue", "int")));
        putFuncWithIdx(0x4004, new ScriptFunc("fadeinFromBlack?", "unknown", null, p("frames", "int")));
        putFuncWithIdx(0x4005, new ScriptFunc("fadeoutToBlack?", "unknown", null, p("frames", "int")));
        putFuncWithIdx(0x4006, new ScriptFunc("fadeinFromWhite", "unknown", null, p("frames", "int")));
        putFuncWithIdx(0x4007, new ScriptFunc("fadeoutToWhite", "unknown", null, p("frames", "int")));
        putFuncWithIdx(0x4008, new ScriptFunc("setScreenOverlayColor", "unknown", null, p("red", "int"), p("green", "int"), p("blue", "int"), p("alpha", "int")));
        putFuncWithIdx(0x4009, new ScriptFunc("dimScreen", "unknown", null, p("red", "int"), p("green", "int"), p("blue", "int"), p("factor", "int"))); // blends (screen - color)*factor, super weird if color isn't close to black
        putFuncWithIdx(0x400A, new ScriptFunc("motionBlurEffect", "unknown", null, p("alpha", "int"))); // alpha for overlaying previous frame
        putFuncWithIdx(0x400B, new ScriptFunc("disableFade", "unknown", null)); // only for the black and white fades
        putFuncWithIdx(0x400C, new ScriptFunc("disableScreenOverlayColor", "unknown", null));
        putFuncWithIdx(0x400D, new ScriptFunc("waitForFade", "unknown", null)); // only for the black and white fades
        putUnknownFunc(0x400E, 1);
        putUnknownFunc(0x400F, 1);
        putFuncWithIdx(0x4013, new ScriptFunc("setActorLight", "unknown", null, p("lightIndex", "int"), p("r", "int"), p("b", "int"), p("g", "int"), p("polar", "float"), p("azimuth", "float")));
        putUnknownFunc(0x4014, 1);
        putUnknownFunc(0x4015, 2);
        putUnknownFunc(0x4016, 2);
        putFuncWithIdx(0x4017, new ScriptFunc("setInvisibleWallState", "unknown", null, p("polygonGroup", "int"), p("state", "invisWallState")));
        putUnknownFunc(0x4019, 1);
        putFuncWithIdx(0x401A, new ScriptFunc("cameraCrossFade", "unknown", null, p("frames", "int"), p("alpha", "int")));
        putUnknownFunc(0x401B, 1);
        putUnknownFunc(0x401C, 1);
        putFuncWithIdx(0x401D, new ScriptFunc("showModularMenu", "unknown", null, p("menu")));
        putUnknownFunc(0x401F, 0);
        putUnknownFunc(0x4020, 0);
        putUnknownFunc(0x4022, 1);
        putUnknownFunc(0x4023, 1);
        putUnknownFunc(0x4024, 0);
        putUnknownFunc(0x4025, 9);
        putUnknownFunc(0x402A, 2);
        putUnknownFunc(0x4034, 1);
        putFuncWithIdx(0x4036, new ScriptFunc("setWindParams", "unknown", null, p("strength?", "float"), p("pitch", "float"), p("yaw", "float")));
        putUnknownFunc(0x4039, 4);
        putUnknownFunc(0x403A, 1);
        putUnknownFunc(0x403B, 2);
        putUnknownFunc(0x403C, 1);
        putUnknownFunc(0x403E, 2);
        putUnknownFunc(0x403F, 0);
        putUnknownFunc(0x4040, 1);
        putUnknownFunc(0x4043, 2);
        putUnknownFunc(0x4044, 3);
        putUnknownFunc(0x4045, 1);
        putUnknownFunc(0x4046, 1);
        putFuncWithIdx(0x5000, new ScriptFunc("playCharMotion", "unknown", null, p("motion")));
        putUnknownFunc(0x5001, 1); // noclip: load motion group
        putFuncWithIdx(0x5002, new ScriptFunc("playCharMotion2", "unknown", null, p("motion")));
        putFuncWithIdx(0x5003, new ScriptFunc("?awaitMotion", "unknown", null, true));
        putFuncWithIdx(0x5004, new ScriptFunc("?shouldAwaitMotion", "bool", null, true));
        putFuncWithIdx(0x5005, new ScriptFunc("resetMotion", "unknown", null, true));
        putFuncWithIdx(0x5006, new ScriptFunc("scaleActor", "unknown", null, p("scaleXYZ", "float")));
        putFuncWithIdx(0x5007, new ScriptFunc("scaleActor", "unknown", null, p("scaledDimensions","dimensionsBitfield"), p("scaleX", "float"), p("scaleY", "float"), p("scaleZ", "float")));
        putFuncWithIdx(0x5008, new ScriptFunc("?setHideEvent", "unknown", null, p("bool")));
        putFuncWithIdx(0x5009, new ScriptFunc("setActorWeight", "float", null, p("weight", "float")));
        putUnknownFunc(0x500A, 1); // cur_actor->__0x4f8 = p1
        putFuncWithIdx(0x500B, new ScriptFunc("setActorGroundHeight", "unknown", null, p("height", "float")));
        putUnknownFunc(0x500C, 1); // `chEnGravity`; noted invalid by dbgPrintf; En might stand for Enable?; supposed to set the gravity mode instead
        putFuncWithIdx(0x500D, new ScriptFunc("?setHideEvent2", "unknown", null, p("bool")));
        putUnknownFunc(0x500E, 1);
        putUnknownFunc(0x500F, 1);
        putFuncWithIdx(0x5010, new ScriptFunc("loadModelMotionGroup", "unknown", null, p("model"), p("motionType")));
        putFuncWithIdx(0x5011, new ScriptFunc("disposeMotionGroup", "unknown", null, p(1)));
        putFuncWithIdx(0x5013, new ScriptFunc("setFieldModeAndMotionType", "unknown", null, p("motionType")));
        putFuncWithIdx(0x5014, new ScriptFunc("setShade", "unknown", null, p("target?", "float"), p("latch?", "unknown")));
        putFuncWithIdx(0x5015, new ScriptFunc("setShade", "unknown", null, p("targetR", "float"), p("targetG", "float"), p("targetB", "float"), p("latch?", "unknown")));
        putFuncWithIdx(0x5016, new ScriptFunc("setOffset", "unknown", null, p("x", "float"), p("y", "float"), p("z", "float")));
        putFuncWithIdx(0x5017, new ScriptFunc("setGravityMode", "unknown", "chSetGravMode", p("gravityMode")));
        putFuncWithIdx(0x5018, new ScriptFunc("setMotionSpeed", "unknown", null, p("motionSpeed", "float")));
        putFuncWithIdx(0x5019, new ScriptFunc(p("motion"), p(2), p(3), p(4), p(5)));
        putFuncWithIdx(0x501A, new ScriptFunc(p("motion"), p(2), p(3), p(4), p(5)));
        putFuncWithIdx(0x501B, new ScriptFunc("playCharMotion3", "unknown", null, p("motion")));
        putFuncWithIdx(0x501D, new ScriptFunc("playCharMotion4", "unknown", null, p("motion")));
        putFuncWithIdx(0x501E, new ScriptFunc("?awaitMotion2", "unknown", null, true)); // also sets something in relation to g_debugVoiceSync
        putFuncWithIdx(0x501F, new ScriptFunc("?shouldAwaitMotion2", "bool", null, true));
        putFuncWithIdx(0x5020, new ScriptFunc("resetMotion2", "unknown", null, true));
        putFuncWithIdx(0x5021, new ScriptFunc("setMotionSpeed2", "float", null, p("motionSpeed", "float")));
        putUnknownFunc(0x5022, 1); // cur_actor->__0x737 = p1
        putUnknownFunc(0x5023, 1); // Doesn't do anything
        putUnknownFunc(0x5024, 1); // Doesn't do anything
        putFuncWithIdx(0x5025, new ScriptFunc("setMotionMoves", "unknown", null, p(1), p("motion")));
        putUnknownFunc(0x5026, 5);
        putUnknownFunc(0x5027, 8);
        putFuncWithIdx(0x5028, new ScriptFunc("?setMoveSetChangeRun", "unknown", null, p("float")));
        putFuncWithIdx(0x5029, new ScriptFunc("setShadowMode", "unknown", null, p(1)));
        putFuncWithIdx(0x502A, new ScriptFunc("?setClip", "unknown", null, p("bool")));
        putUnknownFunc(0x502B, 0);
        putUnknownFunc(0x502C, 2);
        putFuncWithIdx(0x502D, new ScriptFunc("setAttachedToWorker", "unknown", null, p("worker"), p("?attachmentPoint", "int")));
        putFuncWithIdx(0x5030, new ScriptFunc("?getSound", "unknown", null, true));
        putFuncWithIdx(0x5031, new ScriptFunc("?getOtherActorSound", "unknown", null, p("worker")));
        putUnknownFunc(0x5032, 0);
        putFuncWithIdx(0x5033, new ScriptFunc("?setTransparent", "unknown", null, p("?target"), p("?latch")));
        putFuncWithIdx(0x5034, new ScriptFunc("?setMotionHokan", "unknown", null, p(1)));
        putFuncWithIdx(0x5035, new ScriptFunc("getSequenceFrame", "int", null, true));
        putUnknownFunc(0x5036, 5);
        putUnknownFunc(0x5037, 4);
        putUnknownFunc(0x5038, 4);
        putFuncWithIdx(0x5039, new ScriptFunc("setThickness", "float", null, p("thickness", "float")));
        putFuncWithIdx(0x503A, new ScriptFunc("setClipZ", "float", null, p("clipZ", "float")));
        putFuncWithIdx(0x503B, new ScriptFunc("disposeOtherActorMotionGroup", "unknown", null, p("btlChr"), p(2)));
        putUnknownFunc(0x503C, 1);
        putUnknownFunc(0x503D, 1);
        putUnknownFunc(0x503E, 2);
        putFuncWithIdx(0x503F, new ScriptFunc("playCharMotion4", "unknown", null, p("motion")));
        putFuncWithIdx(0x5040, new ScriptFunc("playCharMotion5", "unknown", null, p("motion")));
        putFuncWithIdx(0x5042, new ScriptFunc(p("motion"), p(2), p(3), p(4), p(5)));
        putFuncWithIdx(0x5043, new ScriptFunc(p("worker"), p("motion"), p(3), p(4), p(5), p(6)));
        putFuncWithIdx(0x5044, new ScriptFunc(p("worker"), p("motion"), p(3), p(4), p(5), p(6)));
        putFuncWithIdx(0x5045, new ScriptFunc(p("worker"), p("motion"), p(3), p(4), p(5), p(6)));
        putFuncWithIdx(0x5046, new ScriptFunc(p("worker"), p("motion"), p(3), p(4), p(5), p(6)));
        putFuncWithIdx(0x5047, new ScriptFunc("playOtherActorMotion", "unknown", null, p("worker"), p("motion")));
        putFuncWithIdx(0x5048, new ScriptFunc("playOtherActorMotion2", "unknown", null, p("worker"), p("motion")));
        putFuncWithIdx(0x5049, new ScriptFunc("?awaitOtherActorMotion", "unknown", null, p("worker")));
        putFuncWithIdx(0x504A, new ScriptFunc("?shouldAwaitOtherActorMotion", "bool", null, p("worker")));
        putFuncWithIdx(0x504B, new ScriptFunc("resetOtherActorMotion", "unknown", null, p("worker")));
        putFuncWithIdx(0x504C, new ScriptFunc("setOtherActorMotionSpeed", "unknown", null, p("worker"), p("motionSpeed", "float")));
        putUnknownFunc(0x504D, 1);
        putFuncWithIdx(0x504E, new ScriptFunc("?loadModelAsync", "unknown", null, p("model")));
        putFuncWithIdx(0x504F, new ScriptFunc("?loadModelSync", "unknown", null, p("model")));
        putUnknownFunc(0x5050, null, "unknown", p("worker"), p(2)); // same as 5022 but on target actor
        putFuncWithIdx(0x5051, new ScriptFunc("getMotionFrame", "int", null, true));
        putUnknownFunc(0x5052, 5);
        putUnknownFunc(0x5053, 2);
        putFuncWithIdx(0x5054, new ScriptFunc("modelSetHide", "unknown", null, p(1), p(2)));
        putFuncWithIdx(0x5055, new ScriptFunc("modelGetHide", "unknown", null, p(1))); // never used by the game
        putFuncWithIdx(0x5056, new ScriptFunc("setCalcNorm", "unknown", null, p("bool")));
        putFuncWithIdx(0x5057, new ScriptFunc("setCalcNorm", "unknown", null, p("worker"), p("bool")));
        putUnknownFunc(0x5058, 1);
        putUnknownFunc(0x5059, 2);
        putFuncWithIdx(0x505A, new ScriptFunc("textureSetImageBase", "unknown", null, p(1), p(2)));
        putFuncWithIdx(0x505B, new ScriptFunc("textureSetClutBase", "unknown", null, p(1), p(2)));
        putFuncWithIdx(0x505C, new ScriptFunc("?setMotionNextHokan", "unknown", null, p(1)));
        putUnknownFunc(0x505D, null, "motion", 0); // returns cur_actor->__0x72c
        putFuncWithIdx(0x505E, new ScriptFunc("getChrSpeed", "float", null, true));
        putFuncWithIdx(0x505F, new ScriptFunc("motionSetLoop", "unknown", null, p(1)));
        putFuncWithIdx(0x5060, new ScriptFunc("?idEnableShade", "unknown", null, p("bool")));
        putFuncWithIdx(0x5061, new ScriptFunc("?loadMotionGroups", "unknown", null, p("model"), p(2)));
        putFuncWithIdx(0x5062, new ScriptFunc(p("model"), p(2), p("bool")));
        putFuncWithIdx(0x5063, new ScriptFunc(p("model"), p(2)));
        putFuncWithIdx(0x5064, new ScriptFunc(p(1), p("bool")));
        putFuncWithIdx(0x5065, new ScriptFunc("?startReadMotionGroup", "unknown", null, p(1)));
        putFuncWithIdx(0x5066, new ScriptFunc("?syncReadMotionGroup", "unknown", null, p(1)));
        putFuncWithIdx(0x5067, new ScriptFunc("?setDrawCull", "unknown", null, p("bool")));
        putUnknownFunc(0x5068, 0); // doesn't exist
        putFuncWithIdx(0x5069, new ScriptFunc("setKeepFps", "unknown", null, p("bool")));
        putFuncWithIdx(0x506A, new ScriptFunc("setPLight", "unknown", null, p("enabled", "bool")));
        putFuncWithIdx(0x506B, new ScriptFunc("getPLight", "bool", null, true));
        putFuncWithIdx(0x506C, new ScriptFunc("setPLightMask", "unknown", null, p("enabled", "bool"), p("mask", "unknown"))); // never used by the game
        putFuncWithIdx(0x506D, new ScriptFunc("?setPitch", "unknown", null, p(1), p(2)));
        putFuncWithIdx(0x506E, new ScriptFunc("bindSetScale", "unknown", null, p("scale", "float")));
        putFuncWithIdx(0x506F, new ScriptFunc("setHideCalc", "unknown", null, p(1)));
        putUnknownFunc(0x5070, 0);
        putFuncWithIdx(0x5071, new ScriptFunc("?textureLoadImage", "unknown", null, p(1)));
        putUnknownFunc(0x5072, 0); // returns cur_actor->__0x824
        putFuncWithIdx(0x5073, new ScriptFunc("setOverlapHit", "unknown", null, p("enabled", "bool")));
        putFuncWithIdx(0x5074, new ScriptFunc("getOverlapHit", "bool", null));
        putFuncWithIdx(0x5075, new ScriptFunc("getOtherActorMotionFrame", "int", null, p("worker")));
        putUnknownFunc(0x5076, 1); // cur_actor->__0x4d4 = p1
        putUnknownFunc(0x5077, 0); // returns cur_actor->__0x4d4
        putFuncWithIdx(0x5078, new ScriptFunc("?textureSetAnim", "unknown", null, p(1), p(2)));
        putFuncWithIdx(0x5079, new ScriptFunc("setNeck", "unknown", null, p("enabled", "bool")));
        putFuncWithIdx(0x507A, new ScriptFunc("setNeckFaceActor", "unknown", null, p("worker")));
        putFuncWithIdx(0x507B, new ScriptFunc("setNeckFacePoint", "unknown", null, p("x", "float"), p("y", "float"), p("z", "float")));
        putUnknownFunc(0x507C, 0); // resets facing? looks forward? //TODO: test
        putFuncWithIdx(0x507D, new ScriptFunc("setNeckRotation", "unknown", null, p(1), p(2)));
        putUnknownFunc(0x507E, 2); // tgt_actor->__0x4d4 = p1
        putFuncWithIdx(0x507F, new ScriptFunc("setModelAlpha", "unknown", null, p(1), p(2)));
        putUnknownFunc(0x5080, 1); // cur_actor->__0x51c = p1
        putUnknownFunc(0x5081, 1); // cur_actor->__0x198 bit 3 = p1
        putFuncWithIdx(0x5082, new ScriptFunc("setCloth", "unknown", null, p("enabled", "bool")));
        putFuncWithIdx(0x5083, new ScriptFunc("vulSetClipMode", "unknown", null, p(1)));
        putFuncWithIdx(0x5084, new ScriptFunc("setNeckSpeed", "unknown", null, p("speed", "float")));
        putFuncWithIdx(0x5085, new ScriptFunc("setShadowHeight", "unknown", null, p("height", "float")));
        putUnknownFunc(0x5086, 0);
        putUnknownFunc(0x5087, 0); // doesn't exist
        putFuncWithIdx(0x5088, new ScriptFunc("setShadeId", "unknown", null, p(1), p("float")));
        putFuncWithIdx(0x5089, new ScriptFunc(p("motion"), p(2), p(3), p(4), p(5)));
        putFuncWithIdx(0x508A, new ScriptFunc(p("worker"), p("motion"), p(2), p(3), p(4), p(5)));
        putFuncWithIdx(0x508B, new ScriptFunc(p("motion"), p(2), p(3), p(4), p(5)));
        putFuncWithIdx(0x508C, new ScriptFunc(p("worker"), p("motion"), p(2), p(3), p(4), p(5)));
        putFuncWithIdx(0x508D, new ScriptFunc("?textureSetAnimTimer", "unknown", null, p(1)));
        putFuncWithIdx(0x508E, new ScriptFunc("setClothGravity", "unknown", null, p("enabled", "bool")));
        putUnknownFunc(0x508F, 1); // cur_actor->__0x870 = p1
        putUnknownFunc(0x5090, 1); // cur_actor->__0x871 = p1
        putUnknownFunc(0x6000, "camSleep", 1);
        putUnknownFunc(0x6001, "camWakeUp", 1);
        putUnknownFunc(0x6002, "camSetPos", 3);
        putUnknownFunc(0x6003, "camGetPos", 3);
        putUnknownFunc(0x6004, "camSetPolar", 3);
        putUnknownFunc(0x6005, "camSetPolarOffset", 3);
        putUnknownFunc(0x6006, "camSetHypot", 6);
        putUnknownFunc(0x6007, "camSetHypot2", 6);
        putUnknownFunc(0x6008, "camSetHypot3", 6);
        putUnknownFunc(0x6009, "camSetAct", 3);
        putUnknownFunc(0x600A, "camSetFilter", 5);
        putUnknownFunc(0x600B, "camSetFilter2", 5);
        putUnknownFunc(0x600C, "camSetFilterY", 3);
        putUnknownFunc(0x600D, "camSetFilterY2", 3);
        putUnknownFunc(0x600E, "camSleepFilter", 1);
        putUnknownFunc(0x600F, "camResetFilter", 0);
        putUnknownFunc(0x6010, "camMove", 1);
        putUnknownFunc(0x6011, "camMovePolar", 1);
        putUnknownFunc(0x6012, "camMoveCos", 1);
        putUnknownFunc(0x6013, "camMovePolarCos", 1);
        putUnknownFunc(0x6014, "camMoveAcc", 4);
        putUnknownFunc(0x6015, "camMovePolarAcc", 4);
        putUnknownFunc(0x6016, "camResetMove", 0);
        putUnknownFunc(0x6017, "camSetInertia", 4);
        putUnknownFunc(0x6018, "camSetDirVector", 3);
        putUnknownFunc(0x6019, "camResetDirVector", 0);
        putUnknownFunc(0x601A, "camWait", 0);
        putUnknownFunc(0x601B, "camCheck", 0);
        putUnknownFunc(0x601C, "camSetDataPoint", 2);
        putUnknownFunc(0x601D, "camSetDataPointHypot", 4);
        putUnknownFunc(0x601E, "camSetDataPoint2", 2);
        putUnknownFunc(0x601F, "camSetDataPointHypot2", 4);
        putUnknownFunc(0x6020, "refSetPos", 3);
        putUnknownFunc(0x6021, "refGetPos", 3);
        putUnknownFunc(0x6022, "refSetPolar", 3);
        putUnknownFunc(0x6023, "refSetPolarOffset", 3);
        putUnknownFunc(0x6024, "refSetHypot", 6);
        putUnknownFunc(0x6025, "refSetHypot2", 6);
        putUnknownFunc(0x6026, "refSetHypot3", 6);
        putUnknownFunc(0x6027, "refSetAct", 3);
        putUnknownFunc(0x6028, "refSetFilter", 5);
        putUnknownFunc(0x6029, "refSetFilter2", 5);
        putUnknownFunc(0x602A, "refSetFilterY", 3);
        putUnknownFunc(0x602B, "refSetFilterY2", 3);
        putUnknownFunc(0x602C, "refSleepFilter", 1);
        putUnknownFunc(0x602D, "refResetFilter", 0);
        putUnknownFunc(0x602E, "refMove", 1);
        putUnknownFunc(0x602F, "refMovePolar", 1);
        putUnknownFunc(0x6030, "refMoveCos", 1);
        putUnknownFunc(0x6031, "refMovePolarCos", 1);
        putUnknownFunc(0x6032, "refMoveAcc", 4);
        putUnknownFunc(0x6033, "refMovePolarAcc", 4);
        putUnknownFunc(0x6034, "refResetMove", 0);
        putUnknownFunc(0x6035, "refSetInertia", 4);
        putUnknownFunc(0x6036, "refSetDirVector", 4);
        putUnknownFunc(0x6037, "refResetDirVector", 0);
        putUnknownFunc(0x6038, "refWait", 0);
        putUnknownFunc(0x6039, "refCheck", 0);
        putUnknownFunc(0x603A, "camSetRoll", 1);
        putUnknownFunc(0x603B, "camSetScrDpt", 1);
        putUnknownFunc(0x603C, "camSetAct2", 4);
        putUnknownFunc(0x603D, "refSetAct2", 4);
        putUnknownFunc(0x603E, "camSetBtl", 3);
        putUnknownFunc(0x603F, "refSetBtl", 3);
        putUnknownFunc(0x6040, "camSetBtlPolar", 6);
        putUnknownFunc(0x6041, "refSetBtlPolar", 6);
        putUnknownFunc(0x6042, "refMoveStat", 1);
        putUnknownFunc(0x6043, "camMoveStat", 1);
        putUnknownFunc(0x6044, "camSetBtlPolar2", 6);
        putUnknownFunc(0x6045, "refSetBtlPolar2", 6);
        putUnknownFunc(0x6046, "camSetSpline", 4);
        putUnknownFunc(0x6047, "refSetSpline", 4);
        putUnknownFunc(0x6048, "camStartSpline", 0);
        putUnknownFunc(0x6049, "camRegSpline", 0);
        putUnknownFunc(0x604A, "refStartSpline", 0);
        putUnknownFunc(0x604B, "refRegSpline", 0);
        putUnknownFunc(0x604C, "camSetChrPolar", 6);
        putUnknownFunc(0x604D, "camSetChrPolar2", 6);
        putUnknownFunc(0x604E, "camScrSet", 6);
        putUnknownFunc(0x604F, "camScrOff", 1);
        putUnknownFunc(0x6050, "camDrawSet", 5);
        putUnknownFunc(0x6051, "camDrawLink", 2);
        putUnknownFunc(0x6052, "camScrLink", 2);
        putUnknownFunc(0x6053, "camScrMove", 2);
        putUnknownFunc(0x6054, "camScrMoveCos", 2);
        putUnknownFunc(0x6055, "camScrMoveAcc", 4);
        putUnknownFunc(0x6056, "camDrawMove", 2);
        putUnknownFunc(0x6057, "camDrawMoveCos", 2);
        putUnknownFunc(0x6058, "camDrawMoveAcc", 4);
        putUnknownFunc(0x6059, "refSetSplineFilter", 2);
        putUnknownFunc(0x605A, "refSetSplineFilter2", 3);
        putUnknownFunc(0x605B, "camSetSpline2", 2);
        putUnknownFunc(0x605C, "refSetShake", 5);
        putUnknownFunc(0x605D, "camSetShake", 5);
        putUnknownFunc(0x605E, "camSetScreenShake", 6);
        putUnknownFunc(0x605F, "refResetShake", 0);
        putUnknownFunc(0x6060, "camResetShake", 0);
        putUnknownFunc(0x6061, "camResetScreenShake", 1);
        putUnknownFunc(0x6062, "refWaitShake", 0);
        putUnknownFunc(0x6063, "camWaitShake", 0);
        putUnknownFunc(0x6064, "camWaitScreenShake", 1);
        putUnknownFunc(0x6065, "camPriority", 1);
        putUnknownFunc(0x6066, "refSetShakeB", 5);
        putUnknownFunc(0x6067, "camSetShakeB", 5);
        putUnknownFunc(0x6068, "camSetScreenShakeB", 6);
        putUnknownFunc(0x6069, "refSetShake2", 5);
        putUnknownFunc(0x606A, "camSetShake2", 5);
        putUnknownFunc(0x606B, "camSetScreenShake2", 6);
        putUnknownFunc(0x606C, "refSetShake2B", 5);
        putUnknownFunc(0x606D, "camSetShake2B", 5);
        putUnknownFunc(0x606E, "camSetScreenShake2B", 6);
        putUnknownFunc(0x606F, "refSetShake3", 5);
        putUnknownFunc(0x6070, "camSetShake3", 5);
        putUnknownFunc(0x6071, "camSetScreenShake3", 6);
        putUnknownFunc(0x6072, "refSetShake3B", 5);
        putUnknownFunc(0x6073, "camSetShake3B", 5);
        putUnknownFunc(0x6074, "camSetScreenShake3B", 6);
        putUnknownFunc(0x6075, "camScrSetCam", 2);
        putUnknownFunc(0x6076, "camFreeBattle", 0);
        putUnknownFunc(0x6077, "camGetRoll", "float", 0);
        putUnknownFunc(0x6078, "camGetScrDpt", "float", 0);
        putUnknownFunc(0x6079, "camScrResetMove", 1);
        putUnknownFunc(0x607A, "camDrawResetMove", 1);
        putUnknownFunc(0x607B, "camScrWait", 1);
        putUnknownFunc(0x607C, "camDrawWait", 1);
        putUnknownFunc(0x607D, "camBlur", 1);
        putUnknownFunc(0x607E, "camFocus", 1);
        putUnknownFunc(0x607F, "camSetFocus", 2);
        putUnknownFunc(0x6080, "camRand", 1);
        putUnknownFunc(0x6081, "refSetShake4", 5);
        putUnknownFunc(0x6082, "camSetShake4", 5);
        putUnknownFunc(0x6083, "refSetShake5", 5);
        putUnknownFunc(0x6084, "camSetShake5", 5);
        putUnknownFunc(0x6085, "camGetRealPos", 3);
        putUnknownFunc(0x6086, "refGetRealPos", 3);
        putUnknownFunc(0x6087, "refReset", 0);
        putUnknownFunc(0x6088, "camReset", 0);
        putUnknownFunc(0x7000, "btlTerminateAction", 0);
        putUnknownFunc(0x7001, "btlSetRandPosFlag", 1);
        putFuncWithIdx(0x7002, new ScriptFunc("launchBattle", "unknown", "btlExe", p("encounter"), p("transition", "battleTransition")));
        putUnknownFunc(0x7003, "btlDirTarget", 2);
        putUnknownFunc(0x7004, "btlSetDirRate", 1);
        putFuncWithIdx(0x7005, new ScriptFunc("isWater?", "int", "btlGetWater", false));
        putUnknownFunc(0x7006, "btlDirBasic", 2);
        putFuncWithIdx(0x7007, new ScriptFunc("startMotion?", "unknown", "btlSetMotion", p("stdmotion")));
        putFuncWithIdx(0x7008, new ScriptFunc("awaitMotion?", "unknown", "btlWaitMotion", true));
        putFuncWithIdx(0x7009, new ScriptFuncAccessor("setSelfGravity", null, "btlSetGravity", "=", p("AffectedByGravity", "bool")));
        putFuncWithIdx(0x700A, new ScriptFunc("setHeight?", "unknown", "btlSetHeight", p("int"), p("float")));
        putFuncWithIdx(0x700B, new ScriptFunc("performMove", "bool", "btlSetDirectCommand", p("target", "btlChr"), p("move")));
        putUnknownFunc(0x700C, "btlMove", 8);
        putUnknownFunc(0x700D, "btlDirPos", 2);
        putUnknownFunc(0x700E, "btlSetDamage", 1);
        putFuncWithIdx(0x700F, new ScriptFuncAccessor("readBtlChrProperty", "btlChr", "btlGetStat", null, "btlChrProperty"));
        putFuncWithIdx(0x7010, new ScriptFunc("findMatchingChr", "btlChr", "btlSearchChr", p("group", "btlChr"), p("property", "btlChrProperty"), p("unused", "unknown"), p("selector")));
        putUnknownFunc(0x7011, "btlCameraMode", 1);
        putUnknownFunc(0x7012, "btlTerminateEffect", 0);
        putUnknownFunc(0x7013, "btlChrSp", 1);
        putFuncWithIdx(0x7014, new ScriptFunc("chosenMove?", "move", "btlGetComNum", false));
        putFuncWithIdx(0x7015, new ScriptFunc("print?", "unknown", "btlPrint", p("int")));
        putFuncWithIdx(0x7016, new ScriptFunc("stopMotion?", "unknown", "btlTerminateMotion", p("stdmotion")));
        putUnknownFunc(0x7017, "btlSetNormalEffect", 2);
        putFuncWithIdx(0x7018, new ScriptFuncAccessor("writeBtlChrProperty", "btlChr", "btlSetStat", "=", "btlChrProperty"));
        putFuncWithIdx(0x7019, new ScriptFunc("usedMove", "move", "btlGetReCom", false));
        putFuncWithIdx(0x701A, new ScriptFuncAccessor("readMoveProperty", "move", "btlGetComInfo", null, "moveProperty"));
        putFuncWithIdx(0x701B, new ScriptFunc("overrideAttemptedAction", "unknown", "btlChangeReCom", p("target", "btlChr"), p("move")));
        putUnknownFunc(0x701C, "btlSetMotionLevel", 1);
        putUnknownFunc(0x701D, "btlGetMotionLevel", 0);
        putFuncWithIdx(0x701E, new ScriptFunc("countChrOverlap", "int", "btlCountChr", p("group", "btlChr"), p("btlChr")));
        putUnknownFunc(0x701F, "btlChgWaitMotion", 1);
        putUnknownFunc(0x7020, "btlCheckStartEffect", 0);
        putFuncWithIdx(0x7021, new ScriptFunc("dereferenceCharacter", "btlChr", "btlGetChrNum", p("btlChr")));
        putFuncWithIdx(0x7022, new ScriptFunc("SetAmbushState", "unknown", "btlSetFirstAttack", p("ambushState")));
        putUnknownFunc(0x7023, "btlDistTarget", 1);
        putFuncWithIdx(0x7024, new ScriptFunc("CurrentEncounter", "encounter", "btlGetBtlScene", false));
        putFuncWithIdx(0x7025, new ScriptFunc("findMatchingChrIncludingUntargetable?", "btlChr", "btlSearchChr2", p("group", "btlChr"), p("property", "btlChrProperty"), p("unknown"), p("selector")));
        putFuncWithIdx(0x7026, new ScriptFunc(null, "unknown", "btlSetWeak", p("weakState")));
        putUnknownFunc(0x7027, "btlGetWeak", "weakState", 0);
        putFuncWithIdx(0x7028, new ScriptFunc("scaleOwnSize", "unknown", "btlSetScale", p("x?", "float"), p("y?", "float"), p("z?", "float")));
        putFuncWithIdx(0x7029, new ScriptFuncAccessor("setSelfFloating", null, "btlSetFly", "=", p("floating", "bool")));
        putUnknownFunc(0x702A, "btlCheckBtlPos", 0);
        putUnknownFunc(0x702B, "btlCheckMotion", 0);
        putUnknownFunc(0x702C, "btlSetHoming", 9);
        putUnknownFunc(0x702D, "btlResetMove", 0);
        putUnknownFunc(0x702E, "btlMoveTargetDist", "float", 1);
        putUnknownFunc(0x702F, "btlOut", 1);
        putUnknownFunc(0x7030, "btlGetMoveFlag", 0);
        putUnknownFunc(0x7031, "btlStartMotion", 0);
        putFuncWithIdx(0x7032, new ScriptFuncAccessor("setActorFacingAngle", "btlChr", "btlSetBtlPosDir", "=", p("facingAngle", "float")));
        putUnknownFunc(0x7033, "btlSetEnMapID", 1);
        putFuncWithIdx(0x7034, new ScriptFunc("endBattle", "unknown", "btlComplete", p("battleEndType")));
        putFuncWithIdx(0x7035, new ScriptFunc("BattleEndType", "battleEndType", "btlGetCompInfo", false));
        putUnknownFunc(0x7036, "btlSetTrans", 3);
        putFuncWithIdx(0x7037, new ScriptFunc("addMove", "unknown", "btlAddCom", p("btlChr"), p("move")));
        putFuncWithIdx(0x7038, new ScriptFunc("removeMove", "unknown", "btlDelCom", p("btlChr"), p("move")));
        putUnknownFunc(0x7039, "btlTerminateDeath", 0);
        putUnknownFunc(0x703A, "btlSetSpeed", 1);
        putFuncWithIdx(0x703B, new ScriptFunc("setMoveDisabled", "unknown", "btlSetCommandUse", p("btlChr"), p("move"), p("disabled", "bool")));
        putFuncWithIdx(0x703C, new ScriptFunc("runEncounterScriptA", "unknown", "btlOff", p("encScript", "int")));
        putUnknownFunc(0x703D, "btlOn", 0);
        putUnknownFunc(0x703E, "btlWait", 0);
        putUnknownFunc(0x703F, "camReq", 2);
        putUnknownFunc(0x7040, "btlMagicStart", 1);
        putUnknownFunc(0x7041, "btlMagicEnd", 0);
        putFuncWithIdx(0x7042, new ScriptFunc("displayBattleString", "unknown", "btlMes", p("boxIndex", "int"), p("string", "localString"), p("x?", "int"), p("y?", "int"), p("align", "textAlignment")));
        putFuncWithIdx(0x7043, new ScriptFunc("closeTextOnConfirm", "unknown", "btlMesWait", p("boxIndex", "int")));
        putFuncWithIdx(0x7044, new ScriptFunc("closeTextImmediately", "unknown", "btlMesClose", p("boxIndex", "int")));
        putUnknownFunc(0x7045, "btlDistTargetFrame", 1);
        putUnknownFunc(0x7046, "btlSplineStart", 1);
        putUnknownFunc(0x7047, "btlSplineRegist", 2);
        putUnknownFunc(0x7048, "btlSplineRegistPos", 4);
        putUnknownFunc(0x7049, "btlSplineMove", 4);
        putFuncWithIdx(0x704A, new ScriptFunc(null, "unknown", "btlCheckMove", p("btlChr")));
        putFuncWithIdx(0x704B, new ScriptFunc(null, "unknown", "btlReqMotion", p("btlChr"), p("motionIndex", "int"), p("?await", "bool")));
        putFuncWithIdx(0x704C, new ScriptFunc(null, "unknown", "btlWaitReqMotion", p("btlChr")));
        putUnknownFunc(0x704D, "btlSetDeathLevel", 1);
        putUnknownFunc(0x704E, "btlSetDeathPattern", 1);
        putUnknownFunc(0x704F, "btlSetEventChrFlag", 2);
        putFuncWithIdx(0x7050, new ScriptFunc("revive/reinitialize", "unknown", "btlResetParam", p("btlChr")));
        putUnknownFunc(0x7051, "btlWaitNormalEffect", 0);
        putFuncWithIdx(0x7052, new ScriptFunc("attachActor", "unknown", "btlChrLink", p( "btlChr"), p( "host","btlChr"), p( "attachmentPoint","int")));
        putUnknownFunc(0x7053, "btlMoveJump", 9);
        putFuncWithIdx(0x7054, new ScriptFunc(null, "unknown", "btlSetChrPosElem", p("btlChr"), p(2), p(3)));
        putUnknownFunc(0x7055, "btlSetBodyHit", 1);
        putUnknownFunc(0x7056, "btlSetSpecialBattle", 1);
        putUnknownFunc(0x7057, "btlDirMove", 4);
        putFuncWithIdx(0x7058, new ScriptFunc(null, "unknown", "btlCheckMotionNum", p("btlChr"), p("stdmotion")));
        putUnknownFunc(0x7059, "btlMoveTargetDist2D", "float", 1);
        putFuncWithIdx(0x705A, new ScriptFunc("forcePerformMove", "bool", "btlSetAbsCommand", p("target", "btlChr"), p("move")));
        putUnknownFunc(0x705B, "btlGetCamWidth", "float", 1);
        putUnknownFunc(0x705C, "btlGetCamHeight", "float", 1);
        putUnknownFunc(0x705D, "btlSetBindEffect", 2);
        putUnknownFunc(0x705E, "btlResetBindEffect", 0);
        putUnknownFunc(0x705F, "btlPrintF", 1);
        putUnknownFunc(0x7060, "btlSetStatEff", 0);
        putUnknownFunc(0x7061, "btlClearStatEff", 0);
        putUnknownFunc(0x7062, "btlSetHitEffect", 2);
        putUnknownFunc(0x7063, "btlWaitHitEffect", 0);
        putFuncWithIdx(0x7064, new ScriptFunc("loadBattleVoiceLine", "unknown", "btlVoiceStandby", p("voiceFileIndex", "int")));
        putUnknownFunc(0x7065, "btlVoiceStart", 0);
        putUnknownFunc(0x7066, "btlVoiceStop", 0);
        putUnknownFunc(0x7067, "btlGetVoiceStatus", 0);
        putUnknownFunc(0x7068, "btlVoiceSync", 0);
        putUnknownFunc(0x7069, "btlSearchChrCamera", 4);
        putUnknownFunc(0x706A, "btlCheckTargetOwn", 1);
        putFuncWithIdx(0x706B, new ScriptFunc(null, "unknown", "btlSetModelHide", p("btlChr"), p("part", "int"), p("show?", "bool")));
        putUnknownFunc(0x706C, "btlSoundEffectNormal", 2);
        putFuncWithIdx(0x706D, new ScriptFunc(null, "unknown", "btlSoundStreamNormal", p("btlChr"), p(2)));
        putFuncWithIdx(0x706E, new ScriptFunc(null, "unknown", "btlReqVoice", p("btlChr"), p(2)));
        putUnknownFunc(0x706F, "btlSetMotion2", 1);
        putUnknownFunc(0x7070, "btlStatusOn", 0);
        putUnknownFunc(0x7071, "btlStatusOff", 0);
        putFuncWithIdx(0x7072, new ScriptFunc("displayBattleDialogueLine", "unknown", "btlmes2", p("boxIndex", "int"), p("string", "localString")));
        putUnknownFunc(0x7073, "btlAttachWeapon", 1);
        putUnknownFunc(0x7074, "btlDetachWeapon", 1);
        putFuncWithIdx(0x7075, new ScriptFunc(null, "unknown", "btlReqWeaponMotion", p("btlChr"), p(2), p(3)));
        putUnknownFunc(0x7076, "btlBallSplineMove", 3);
        putUnknownFunc(0x7077, "btlDistTargetFrameBall", 2);
        putFuncWithIdx(0x7078, new ScriptFuncAccessor("readMovePropertyForActor", "move", "btlGetComInfo2", null, "moveProperty", p("btlChr")));
        putUnknownFunc(0x7079, "btlResetWeapon", 0);
        putFuncWithIdx(0x707A, new ScriptFunc(null, "unknown", "btlGetCalcResult", p("btlChr")));
        putFuncWithIdx(0x707B, new ScriptFunc(null, "unknown", "btlSoundEffect", p("btlChr"), p(2)));
        putUnknownFunc(0x707C, "btlWaitSound", 0);
        putFuncWithIdx(0x707D, new ScriptFunc("setDebugFlag?", "unknown", "btlSetDebug", p("battleDebugFlag"), p("active", "bool")));
        putUnknownFunc(0x707E, "btlGetDebug", 1);
        putFuncWithIdx(0x707F, new ScriptFunc(null, "unknown", "btlSetBtlPos", p("btlChr")));
        putUnknownFunc(0x7080, "btlChangeAuron", 1);
        putUnknownFunc(0x7081, "btlWaitExe", 0);
        putUnknownFunc(0x7082, "btlSetFreeEffect", 2);
        putUnknownFunc(0x7083, "btlSetAfterImage", 2);
        putUnknownFunc(0x7084, "btlResetAfterImage", 0);
        putUnknownFunc(0x7085, "btlMoveAttack", 8);
        putUnknownFunc(0x7086, "btlUseChrMpLimit", 0);
        putFuncWithIdx(0x7087, new ScriptFunc(null, "unknown", "btlSoundEffectFade", p("btlChr"), p(2), p(3)));
        putFuncWithIdx(0x7088, new ScriptFunc(null, "unknown", "btlRegSoundEffect", p("btlChr"), p(2)));
        putUnknownFunc(0x7089, "btlRegSoundEffectFade", 3);
        putFuncWithIdx(0x708A, new ScriptFunc(null, "unknown", "btlInitEncount", p("encounter")));
        putFuncWithIdx(0x708B, new ScriptFunc(null, "unknown", "btlGetEncount", p("encounter")));
        putFuncWithIdx(0x708C, new ScriptFunc("setEncounterEnabled", "unknown", "btlSetEncount", p("encounter"), p("active", "bool")));
        putUnknownFunc(0x708D, "btlGetLastActionChr", "btlChr", 0);
        putUnknownFunc(0x708E, "btlCheckBtlPos2", 0);
        putUnknownFunc(0x708F, "btlDirPosBasic", 1);
        putUnknownFunc(0x7090, "btlSetCriticalEffect", 1);
        putFuncWithIdx(0x7091, new ScriptFunc("changeActorNameToCharName", "unknown", "btlChangeChrName", p("btlChr"), p("newName", "playerChar")));
        putFuncWithIdx(0x7092, new ScriptFunc(null, "unknown", "btlGetGroundDist", p("btlChr")));
        putUnknownFunc(0x7093, "btlCheckDirFlag", 0);
        putUnknownFunc(0x7094, "btlSetTransVisible", 3);
        putUnknownFunc(0x7095, "btlGetMoveFrameRest", 0);
        putUnknownFunc(0x7096, "btlGetReflect", 0);
        putFuncWithIdx(0x7097, new ScriptFunc("runEncounterScriptB", "unknown", "btlOff2", p("encScript", "int")));
        putUnknownFunc(0x7098, "btlCheckDefenseMotion", "bool", 0);
        putUnknownFunc(0x7099, "btlSetCursorType", 1);
        putUnknownFunc(0x709A, "btlCheckPoison", 0);
        putUnknownFunc(0x709B, "btlGetChrPosY", "float", 1);
        putUnknownFunc(0x709C, "btlGetTargetDir", "float", 2);
        putUnknownFunc(0x709D, "btlWaitMotion_avoid", 0);
        putFuncWithIdx(0x709E, new ScriptFunc(null, "unknown", "btlSetMotionSignal", p("btlChr"), p(2), p(3)));
        putUnknownFunc(0x709F, "btlGetChrTargetDir", "float", 1);
        putUnknownFunc(0x70A0, "btlSetUpVectorFlag", 1);
        putFuncWithIdx(0x70A1, new ScriptFunc("dereferenceEnemy", "btlChr", "btlGetChrNum2", p("btlChr")));
        putUnknownFunc(0x70A2, "btlMotionRead", 1);
        putUnknownFunc(0x70A3, "btlSetMotionAbs", 1);
        putUnknownFunc(0x70A4, "btlMotionDispose", 0);
        putFuncWithIdx(0x70A5, new ScriptFunc(null, "unknown", "btlSetMapCenter", p("x", "float"), p("y", "float"), p("z", "float")));
        putUnknownFunc(0x70A6, "btlSetEscape", 1);
        putUnknownFunc(0x70A7, "btlGetMotionData", "float", 2);
        putFuncWithIdx(0x70A8, new ScriptFuncAccessor("setMotionValue", "btlChr", "btlSetMotionData", "=", "motionProperty"));
        putUnknownFunc(0x70A9, "btlmeswait_voice", 1);
        putFuncWithIdx(0x70AA, new ScriptFuncAccessor("readBtlChrProperty2", null, "btlGetStat2", null, "btlChrProperty"));
        putFuncWithIdx(0x70AB, new ScriptFuncAccessor("setBtlChrProperty2", null, "btlSetStat2", "=", "btlChrProperty"));
        putFuncWithIdx(0x70AC, new ScriptFuncAccessor("readMotionProperty2", null, "btlGetMotionData2", null, "motionProperty"));
        putUnknownFunc(0x70AD, "btlCheckWakkaWeapon", 0);
        putUnknownFunc(0x70AE, "btlGetLastDeathChr", 0);
        putUnknownFunc(0x70AF, "btlGetVoiceFlag", 0);
        putUnknownFunc(0x70B0, "btlDistTargetFrame2", 1);
        putUnknownFunc(0x70B1, "btlPrintSp", 1);
        putFuncWithIdx(0x70B2, new ScriptFuncAccessor("setMotionValue", null, "btlSetMotionData2", "=", "motionProperty"));
        putUnknownFunc(0x70B3, "btlVoiceSet", 1);
        putUnknownFunc(0x70B4, "btlFadeOutWeapon", 0);
        putUnknownFunc(0x70B5, "btlResetMotionSpeed", 0);
        putFuncWithIdx(0x70B6, new ScriptFunc(null, "unknown", "btlDistTargetFrameSpd", p("btlChr")));
        putUnknownFunc(0x70B7, "btlmesa", 2);
        putUnknownFunc(0x70B8, "btlSetSkipMode", 1);
        putUnknownFunc(0x70B9, "btlGetCamWidth2", "float", 1);
        putUnknownFunc(0x70BA, "btlGetCamHeight2", "float", 1);
        putUnknownFunc(0x70BB, "btlMoveLeave", 1);
        putUnknownFunc(0x70BC, "btlWaitNomEff", 1);
        putUnknownFunc(0x70BD, "btlWaitHitEff", 1);
        putUnknownFunc(0x70BE, "btlGetChrDir", "float", 1);
        putUnknownFunc(0x70BF, "btlSetBindScale", 1);
        putUnknownFunc(0x70C0, "btlGetHeight", "float", 1);
        putUnknownFunc(0x70C1, "btlDistTarget2", 2);
        putUnknownFunc(0x70C2, "btlGetTargetDirH", "float", 2);
        putUnknownFunc(0x70C3, "btlGetChrTargetDir2", "float", 1);
        putUnknownFunc(0x70C4, "btlEquipWakkaWeapon", 1);
        putUnknownFunc(0x70C5, "btlCheckRetBtlPos", 0);
        putUnknownFunc(0x70C6, "btlGetCameraBuffer", 1);
        putUnknownFunc(0x70C7, "btlGetCameraBufferFloat", "float", 1);
        putUnknownFunc(0x70C8, "btlSoundEffect2", 2);
        putFuncWithIdx(0x70C9, new ScriptFunc(null, "unknown", "btlSoundEffect3", p("btlChr"), p(2)));
        putUnknownFunc(0x70CA, "btlRegSoundEffect2", 2);
        putUnknownFunc(0x70CB, "btlRegSoundEffect3", 2);
        putFuncWithIdx(0x70CC, new ScriptFunc("initializeMatchingGroupTo", "unknown", "btlSetOwnTarget", p("btlChr")));
        putFuncWithIdx(0x70CD, new ScriptFunc("addToMatchingGroup", "unknown", "btlAddOwnTarget", p("btlChr")));
        putFuncWithIdx(0x70CE, new ScriptFunc("removeFromMatchingGroup", "unknown", "btlSubOwnTarget", p("btlChr")));
        putUnknownFunc(0x70CF, "btlGetReverbe", 0);
        putUnknownFunc(0x70D0, "btlSetCameraSelectMode", 1);
        putFuncWithIdx(0x70D1, new ScriptFunc(null, "unknown", "btlGetNomEff", p("btlChr")));
        putUnknownFunc(0x70D2, "btlGetHitEff", 1);
        putUnknownFunc(0x70D3, "btlSetNomEff", 3);
        putUnknownFunc(0x70D4, "btlSetHitEff", 3);
        putFuncWithIdx(0x70D5, new ScriptFunc("setSummoner", "unknown", "btlSetSummoner", p("btlChr")));
        putFuncWithIdx(0x70D6, new ScriptFunc("calculateAverageDamage", "int", "btlGetAssumeDamage", p("user", "btlChr"), p("target", "btlChr"), p("move")));
        putFuncWithIdx(0x70D7, new ScriptFunc(null, "unknown", "btlSetDamageMotion", p("btlChr"), p("stdmotion")));
        putUnknownFunc(0x70D8, "btlSetAnimaChainOff", 1);
        putUnknownFunc(0x70D9, "btlExeAnimaChainOff", 0);
        putUnknownFunc(0x70DA, "btlGetFirstAttack", "ambushState", 0);
        putUnknownFunc(0x70DB, "btlGetAnimaChainOff", 0);
        putFuncWithIdx(0x70DC, new ScriptFunc("changeChrName", "unknown", "btlChangeChrNameID", p("btlChr"), p("string", "localString")));
        putUnknownFunc(0x70DD, "btlSetDebugCount", 1);
        putFuncWithIdx(0x70DE, new ScriptFunc("SubtitlesEnabled?", "bool", "btlGetSubTitle", false));
        putFuncWithIdx(0x70DF, new ScriptFunc(null, "unknown", "btlCheckBtlScene", p("encounter")));
        putFuncWithIdx(0x70E0, new ScriptFunc("isCounterattackAllowed", "bool", "btlGetReaction", true));
        putUnknownFunc(0x70E1, "btlGetNormalAttack", 0);
        putFuncWithIdx(0x70E2, new ScriptFunc(null, "unknown", "btlSetTexAnime", p("btlChr"), p(2)));
        putUnknownFunc(0x70E3, "btlGetEffectMemory", 1);
        putUnknownFunc(0x70E4, "btlGetCalcResultLimit", 2);
        putUnknownFunc(0x70E5, "btlSetNomEffReg", 3);
        putFuncWithIdx(0x70E6, new ScriptFunc(null, "unknown", "btlSetHitEffReg", p("btlChr"), p(2), p(3)));
        putUnknownFunc(0x70E7, "btlSetRandomTarget", 1);
        putFuncWithIdx(0x70E8, new ScriptFunc("PlayerTotalGil", "int", "btlGetGold", false));
        putFuncWithIdx(0x70E9, new ScriptFunc("YojimboHireAnswer", "int", "btlGetYoujinboType", false));
        putFuncWithIdx(0x70EA, new ScriptFunc("setYojimboHireAnswer", "unknown", "btlSetYoujinboType", p("int")));
        putUnknownFunc(0x70EB, "btlGetYoujinboRandom", 0);
        putUnknownFunc(0x70EC, "btlGetItemNum", 1);
        putFuncWithIdx(0x70ED, new ScriptFunc("giveItem", "unknown", "btlGetItem", p("item", "move"), p("amount", "int")));
        putFuncWithIdx(0x70EE, new ScriptFunc("RollYojimboMove", "move", "btlGetYoujinboCommand", p("motivation", "int"), p("unknown")));
        putFuncWithIdx(0x70EF, new ScriptFunc(null, "unknown", "btlSetEffSignal", p("btlChr"), p(2)));
        putUnknownFunc(0x70F0, "btlGetCameraCount", "float", 0);
        putFuncWithIdx(0x70F1, new ScriptFunc("clearOwnMoves?", "unknown", "btlCommandClear", true));
        putFuncWithIdx(0x70F2, new ScriptFunc("addMoveToSelf?", "unknown", "btlCommandSet", p("move")));
        putFuncWithIdx(0x70F3, new ScriptFunc("RollMagusRandom", "bool", "btlCheckMegasRandom", p("unknown"), p("chance", "int")));
        putUnknownFunc(0x70F4, "btlGetCommandTarget", 2);
        putUnknownFunc(0x70F5, "btlCheckUseCommand", 2);
        putUnknownFunc(0x70F6, "btlInitCommandBuffer", 0);
        putFuncWithIdx(0x70F7, new ScriptFunc(null, "unknown", "btlSetCommandBuffer", p("move")));
        putUnknownFunc(0x70F8, "btlGetCommandBuffer", "move", 0);
        putUnknownFunc(0x70F9, "btlSearchChr3", 5);
        putUnknownFunc(0x70FA, "btlSetMegasRandomCommand", 1);
        putFuncWithIdx(0x70FB, new ScriptFunc(null, "btlChr", "btlGetCommandTargetSearch", p("move"), p("targeting", "magusTarget"), p("property", "btlChrProperty"), p("selector"), p("chance", "int")));
        putUnknownFunc(0x70FC, "btlGetMegasRandomCommand", 0);
        putFuncWithIdx(0x70FD, new ScriptFunc("increaseMagusMotivationAndOverdrive", "unknown", "btlSetUpLimit", p("overdrive", "int"), p("motivation", "int")));
        putFuncWithIdx(0x70FE, new ScriptFunc(null, "unknown", "btlSetUpLimit2", p(1), p(2), p("overdrive", "int"), p("motivation", "int")));
        putUnknownFunc(0x70FF, "btlSetDeltaTarget", 0);
        putUnknownFunc(0x7100, "btlCheckReqMotion", 1);
        putFuncWithIdx(0x7101, new ScriptFunc("isDebugBattleStart", "unknown", "btlGetFullCommand", true));
        putFuncWithIdx(0x7102, new ScriptFunc("makeChrHeadFaceChr?", "unknown", "btlFaseTarget", p("btlChr"), p("target","btlChr")));
        putFuncWithIdx(0x7103, new ScriptFunc("makeChrHeadFacePoint?", "unknown", "btlFaseTargetXYZ", p("btlChr"), p("x", "float"), p("y", "float"), p("z", "float")));
        putFuncWithIdx(0x7104, new ScriptFunc("changeMoveAnimation", "unknown", "btlSetCommandEffect", p("move"), p("anim1", "int"), p("anim2", "int")));
        putUnknownFunc(0x7105, "btlWaitStone", 0);
        putFuncWithIdx(0x7106, new ScriptFunc("doesChrKnowMove", "bool", "btlCheckGetCommand", p("btlChr"), p("move")));
        putUnknownFunc(0x7107, "btlDirPosBasic2", 1);
        putUnknownFunc(0x7108, "btlDirBasic2", 2);
        putFuncWithIdx(0x7109, new ScriptFunc(null, "unknown", "btlSetAppear", p("btlChr"), p(2), p(3)));
        putUnknownFunc(0x710A, "btlSetSummonTiming", 0);
        putUnknownFunc(0x710B, "btlWaitSummonTiming", 0);
        putUnknownFunc(0x710C, "btlTerminateStone", 0);
        putUnknownFunc(0x710D, "btlDefensePosOff", 0);
        putUnknownFunc(0x710E, "btlGetWakkaLimitSkill", 0);
        putUnknownFunc(0x710F, "btlGetWakkaLimitNum", 0);
        putFuncWithIdx(0x7110, new ScriptFunc("activateMouthMovement", "unknown", "btlMouseOn", p("btlChr")));
        putFuncWithIdx(0x7111, new ScriptFunc("deactivateMouthMovement", "unknown", "btlMouseOff", p("btlChr")));
        putUnknownFunc(0x7112, "btlDirMove2", 4);
        putUnknownFunc(0x7113, "btlMonsterFarm", 0);
        putUnknownFunc(0x7114, "btlSphereMonitor", 0);
        putUnknownFunc(0x7115, "btlDirResetLeave", 0);
        putUnknownFunc(0x7116, "btlSetSummonDefenseEffect", 0);
        putFuncWithIdx(0x7117, new ScriptFunc("overrideDeathAnimationWithAction", "unknown", "btlSetDeathCommand", p("target", "btlChr"), p("move")));
        putUnknownFunc(0x7118, "btlSetSummonGameOver", 1);
        putUnknownFunc(0x7119, "btlSetCounterFlag", 1);
        putUnknownFunc(0x711A, "btlSetWind", 4);
        putUnknownFunc(0x711B, "btlSetCameraStandard", 0);
        putUnknownFunc(0x711C, "btlSetGameOverEffNum", 1);
        putUnknownFunc(0x711D, "btlSetShadowHeight", 1);
        putFuncWithIdx(0x7120, new ScriptFunc("displayBattleSystem01String?", "unknown", null, p("boxIndex", "int"), p("string", "system01String")));
        putUnknownFunc(0x7123, 1);
        putUnknownFunc(0x7124, 1);
        putUnknownFunc(0x7125, 0);
        putUnknownFunc(0x7126, 1);
        putUnknownFunc(0x7127, 2);

        putFuncWithIdx(0x8000, new ScriptFunc("setLevelLayerVisibility", "unknown", null, p("layerIndex", "int"), p("visible", "bool")));
        putFuncWithIdx(0x8001, new ScriptFunc("setSkyboxVisibility", "unknown", null, p("visible", "bool")));
        putFuncWithIdx(0x8002, new ScriptFunc("setGfxActive?", "unknown", null, p("gfxIndex", "int"), p("active", "bool"))); // GFX are either lights or particle effects
        putFuncWithIdx(0x8003, new ScriptFunc("startGfxTimer", "unknown", null, p("gfxIndex", "int")));
        putFuncWithIdx(0x8004, new ScriptFunc("waitForGfxStopped", "unknown", null, p("gfxIndex", "int"))); // equivalent to 8005 with true
        putFuncWithIdx(0x8005, new ScriptFunc("waitForGfxEnding", "unknown", null, p("gfxIndex", "int"), p("waitForChildren", "bool")));
        putUnknownFunc(0x8006, 1);
        putFuncWithIdx(0x8007, new ScriptFunc("setAllGfxActive", "unknown", null, true));
        putFuncWithIdx(0x8008, new ScriptFunc("stopAllGfx", "unknown", null, true));
        putFuncWithIdx(0x8009, new ScriptFunc("bindGfxToTarget", "unknown", null, p("gfxIndex", "int"), p("target", "int"))); // for actors, target is a bone index
        putFuncWithIdx(0x800A, new ScriptFunc("bindGfxPosition", "unknown", "mpfpbindpos", p("gfxIndex", "int"), p("x", "float"), p("y", "float"), p("z", "float")));
        putFuncWithIdx(0x800B, new ScriptFunc("unbindGfx", "unknown", null, p("gfxIndex", "int")));
        putFuncWithIdx(0x800C, new ScriptFunc("setGfxEnabledGlobal", "unknown", null, p("enabled", "bool"))); // controls all gfx rendering/updates 
        putFuncWithIdx(0x800D, new ScriptFunc("setGfxVisibility", "unknown", null, p("gfxIndex", "int"), p("visible", "bool")));
        // next several are about the image planes used for forced-perspective areas
        putFuncWithIdx(0x800F, new ScriptFunc("?show2DLayer", "unknown", null, p("layerIndex", "int"))); // clear image hidden flag
        putFuncWithIdx(0x8010, new ScriptFunc("?hide2DLayer", "unknown", null, p("layerIndex", "int"))); // set image hidden flag
        putFuncWithIdx(0x8011, new ScriptFunc("?set2DLayerPos", "unknown", null, p("layerIndex", "int"), p("?x", "int"), p("?y", "int")));
        putUnknownFunc(0x8014, 2); // set image depth
        putFuncWithIdx(0x801D, new ScriptFunc("?set2DLayerTexture", "unknown", null, p("layerIndex", "int"), p("textureIndex", "int")));
        putFuncWithIdx(0x801E, new ScriptFunc(p("layerIndex", "int")));
        putFuncWithIdx(0x801F, new ScriptFunc(p("layerIndex", "int")));
        putFuncWithIdx(0x8020, new ScriptFunc(p("layerIndex", "int")));
        putFuncWithIdx(0x8021, new ScriptFunc(p("layerIndex", "int")));
        putFuncWithIdx(0x8022, new ScriptFunc(p("layerIndex", "int"), p(2)));
        putUnknownFunc(0x8026, 5);
        putUnknownFunc(0x802C, 0);
        putUnknownFunc(0x802D, 0);
        putFuncWithIdx(0x802E, new ScriptFunc(p("layerIndex", "int"), p(2)));
        putFuncWithIdx(0x802F, new ScriptFunc(p("layerIndex", "int")));
        putFuncWithIdx(0x8030, new ScriptFunc("?set2DLayerOpacity", "unknown", null, p("layerIndex", "int"), p("opacity", "int"))); // set image opacity
        putUnknownFunc(0x8032, 1); // minimap related?
        putFuncWithIdx(0x8035, new ScriptFunc("setGfxGroupActive", "unknown", null, p("group", "int"), p("active", "bool")));
        putFuncWithIdx(0x8036, new ScriptFunc("setGfxGroupVisibility", "unknown", null, p("group", "int"), p("visible", "bool")));
        putFuncWithIdx(0x8037, new ScriptFunc("setFogParams", "unknown", null, p("near", "float"), p("far", "float"), p("maxAlpha", "float")));
        putUnknownFunc(0x8038, 1); // set/get fog components
        putUnknownFunc(0x8039, 0);
        putUnknownFunc(0x803A, 1);
        putUnknownFunc(0x803B, 0);
        putUnknownFunc(0x803C, 1);
        putUnknownFunc(0x803D, 0);
        putFuncWithIdx(0x803E, new ScriptFunc("setFogColor", "unknown", null, p("red", "int"), p("green", "int"), p("blue", "int")));
        putFuncWithIdx(0x803F, new ScriptFunc("setFogRed", "unknown", null, p("red", "int")));
        putFuncWithIdx(0x8040, new ScriptFunc("setFogGreen", "unknown", null, p("green", "int")));
        putFuncWithIdx(0x8041, new ScriptFunc("setFogBlue", "unknown", null, p("blue", "int")));
        putFuncWithIdx(0x8042, new ScriptFunc("getFogRed", "int", null, true));
        putFuncWithIdx(0x8043, new ScriptFunc("getFogGreen", "int", null, true));
        putFuncWithIdx(0x8044, new ScriptFunc("getFogBlue", "int", null, true));
        putFuncWithIdx(0x8045, new ScriptFunc("setClearColor", "unknown", null, p("red", "int"), p("green", "int"), p("blue", "int")));
        putUnknownFunc(0x8049, 0);
        putUnknownFunc(0x804A, 0);
        putUnknownFunc(0x804B, 0);
        putUnknownFunc(0x804F, 3);
        putFuncWithIdx(0x8059, new ScriptFunc("bindGfxToMapGroup", "unknown", null, p("gfxIndex", "int"), p("groupIndex", "int")));
        putFuncWithIdx(0x805B, new ScriptFunc("setGfxPosition", "unknown", null, p("gfxIndex", "int"), p("x", "float"), p("y", "float"), p("z", "float")));
        putUnknownFunc(0x805C, 1);
        putUnknownFunc(0x805D, 0);
        putUnknownFunc(0x805E, 1);
        putFuncWithIdx(0x805F, new ScriptFunc("stopGfx", "unknown", null, p("gfxIndex", "int")));
        putFuncWithIdx(0x8060, new ScriptFunc("stopGfxGroup", "unknown", null, p("groupIndex", "int")));
        putFuncWithIdx(0x8066, new ScriptFunc("setGfxPausedGlobal", "unknown", null, p("paused", "bool")));
        putUnknownFunc(0x8067, 4);
        putUnknownFunc(0x806A, 1); // set an image plane's map group
        putUnknownFunc(0x806B, 1); // totally empty?!
        putFuncWithIdx(0xB000, new ScriptFunc("loadFmv", "unknown", null, p("fmv"), p("flags", "bitfield")));
        putFuncWithIdx(0xB001, new ScriptFunc("?unloadFmv", "unknown", null, true));
        putUnknownFunc(0xB002, 0);
        putFuncWithIdx(0xB003, new ScriptFunc("CurrentFmvPlaybackProgress", "int", null, false));
        putFuncWithIdx(0xB004, new ScriptFunc("?awaitFmv", "unknown", null, true));
        putFuncWithIdx(0xB009, new ScriptFunc("?playLoadedFmv", "unknown", null, true));
        putUnknownFunc(0xB00A, 2);
        putUnknownFunc(0xB00B, 0);
        putUnknownFunc(0xB00C, 0);
        putUnknownFunc(0xB00D, 0);
        putUnknownFunc(0xC002, 1);
        putUnknownFunc(0xC003, 1);
        putUnknownFunc(0xC007, 2);
        putUnknownFunc(0xC009, 1);
        putUnknownFunc(0xC00B, 1);
        putUnknownFunc(0xC00C, 1);
        putUnknownFunc(0xC00D, 1);
        putUnknownFunc(0xC013, 0);
        putUnknownFunc(0xC014, 0);
        putUnknownFunc(0xC018, 1);
        putUnknownFunc(0xC022, 1);
        putFuncWithIdx(0xC024, new ScriptFunc("launchBattleAlwaysWin?", "unknown", null, p("encounter"), p("transition", "battleTransition")));
        putUnknownFunc(0xC025, 1);
        putFuncWithIdx(0xC027, new ScriptFunc("RemoveAllKeyItemsAndPrimersRequireDebug", "unknown", null, true)); // Never called
        putUnknownFunc(0xC028, 0);
        putUnknownFunc(0xC02A, 1);
        putUnknownFunc(0xC02C, 1);
        putUnknownFunc(0xC02F, 2);
        putUnknownFunc(0xC030, 0);
        putUnknownFunc(0xC031, 1);
        putUnknownFunc(0xC036, 1);
        putUnknownFunc(0xC03B, 0);
        putUnknownFunc(0xC03C, 0);
        putUnknownFunc(0xC051, 1);
        putUnknownFunc(0xC052, 0);
        putUnknownFunc(0xC053, 4);
        putUnknownFunc(0xC054, 1);
        putUnknownFunc(0xC055, 1);
        putUnknownFunc(0xC056, 3);
        putUnknownFunc(0xC057, 1);
        putUnknownFunc(0xC058, 1);
        putUnknownFunc(0xC05B, 1);
    }
}
