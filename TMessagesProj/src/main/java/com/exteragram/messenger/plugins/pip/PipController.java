package com.exteragram.messenger.plugins.pip;

import android.text.TextUtils;
import org.telegram.messenger.Utilities;
import com.chaquo.python.internal.Common;
import com.exteragram.messenger.plugins.PluginsController;
import com.exteragram.messenger.utils.network.ExteraHttpClient;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.IntIterator;
import kotlin.collections.MapsKt;
import kotlin.collections.SetsKt;
import kotlin.io.CloseableKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import kotlin.text.MatchResult;
import kotlin.text.Regex;
import kotlin.text.StringsKt;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.url._UrlKt;
import org.lsposed.lsparanoid.Deobfuscator$exteraGramDev$TMessagesProj;
import org.simplifiles.SimpliFiles;
import org.simplifiles.archive.ArchiveExtractionOptions;
import org.simplifiles.archive.CancellationToken;
import org.simplifiles.archive.ExtractionTargetPolicy;
import org.simplifiles.archive.security.SecurityPolicy;
import org.simplifiles.files.OverwritePolicy;
import org.simplifiles.files.SimpliFile;
import org.telegram.messenger.FileLog;

/* JADX INFO: loaded from: classes.dex */
@SourceDebugExtension({"SMAP\nPipController.kt\nKotlin\n*S Kotlin\n*F\n+ 1 PipController.kt\ncom/exteragram/messenger/plugins/pip/PipController\n+ 2 _Maps.kt\nkotlin/collections/MapsKt___MapsKt\n+ 3 Maps.kt\nkotlin/collections/MapsKt__MapsKt\n+ 4 _Collections.kt\nkotlin/collections/CollectionsKt___CollectionsKt\n+ 5 fake.kt\nkotlin/jvm/internal/FakeKt\n+ 6 _Arrays.kt\nkotlin/collections/ArraysKt___ArraysKt\n*L\n1#1,1117:1\n221#2:1118\n221#2,2:1119\n222#2:1121\n221#2:1133\n221#2,2:1134\n222#2:1136\n221#2:1140\n221#2,2:1141\n222#2:1143\n221#2:1146\n221#2,2:1147\n222#2:1149\n221#2:1152\n222#2:1155\n221#2:1158\n221#2,2:1159\n222#2:1161\n221#2:1162\n221#2,2:1163\n222#2:1165\n493#3:1122\n442#3:1123\n493#3:1126\n442#3:1127\n1266#4,2:1124\n1266#4,4:1128\n1269#4:1132\n296#4,2:1137\n1915#4,2:1144\n1915#4,2:1150\n1915#4,2:1153\n1915#4,2:1166\n777#4:1168\n873#4,2:1169\n1807#4,3:1171\n1807#4,3:1174\n777#4:1177\n873#4,2:1178\n832#4:1180\n862#4,2:1181\n1807#4,3:1183\n1807#4,3:1186\n1786#4,3:1189\n1642#4,10:1192\n1915#4:1202\n1916#4:1204\n1652#4:1205\n1586#4:1206\n1661#4,3:1207\n1#5:1139\n1#5:1203\n14048#6,2:1156\n*S KotlinDebug\n*F\n+ 1 PipController.kt\ncom/exteragram/messenger/plugins/pip/PipController\n*L\n120#1:1118\n123#1:1119,2\n120#1:1121\n154#1:1133\n156#1:1134,2\n154#1:1136\n306#1:1140\n307#1:1141,2\n306#1:1143\n330#1:1146\n331#1:1147,2\n330#1:1149\n351#1:1152\n351#1:1155\n376#1:1158\n377#1:1159,2\n376#1:1161\n394#1:1162\n395#1:1163,2\n394#1:1165\n146#1:1122\n146#1:1123\n147#1:1126\n147#1:1127\n146#1:1124,2\n147#1:1128,4\n146#1:1132\n238#1:1137,2\n318#1:1144,2\n338#1:1150,2\n352#1:1153,2\n405#1:1166,2\n449#1:1168\n449#1:1169,2\n453#1:1171,3\n593#1:1174,3\n647#1:1177\n647#1:1178,2\n962#1:1180\n962#1:1181,2\n967#1:1183,3\n971#1:1186,3\n1000#1:1189,3\n1006#1:1192,10\n1006#1:1202\n1006#1:1204\n1006#1:1205\n180#1:1206\n180#1:1207,3\n1006#1:1203\n360#1:1156,2\n*E\n"})
public final class PipController {
    public static final PipController INSTANCE;
    private static final long MAX_METADATA_BYTES = 4194304;
    private static final long MAX_REGISTRY_BYTES = 4194304;
    private static final long MAX_WHEEL_BYTES = 262144000;
    private static final Set<String> PREINSTALLED_PACKAGES;
    private static final Regex REGEX_MARKER_TOKEN;
    private static final Regex REGEX_NORMALIZE;
    private static final Regex REGEX_REQ_EXTRA;
    private static final Regex REGEX_REQ_PAREN;
    private static final Regex REGEX_REQ_PARSE;
    private static final Regex REGEX_REQ_SPECS;
    private static final Regex REGEX_VERSION_SPLIT;
    private static final Regex REGEX_VERSION_WILDCARD;
    private static final OkHttpClient client;
    private static final Gson gson;
    private static final ConcurrentHashMap<String, Object> installLocks;
    private static String pythonVersion;
    private static final ConcurrentHashMap<String, ConcurrentHashMap<String, Set<String>>> registry;
    private static final SecurityPolicy wheelArchivePolicy;
    private static final String ENV_SYS_PLATFORM = Deobfuscator$exteraGramDev$TMessagesProj.getString(-59001982436911L);
    private static final String ENV_PLATFORM_SYSTEM = Deobfuscator$exteraGramDev$TMessagesProj.getString(-59027752240687L);
    private static final String ENV_OS_NAME = Deobfuscator$exteraGramDev$TMessagesProj.getString(-58984802567727L);

    public interface InstallerDelegate {
        boolean isCancelled();

        void onProgress(String text);
    }

    private PipController() {
    }

    static {
        PipController pipController = new PipController();
        INSTANCE = pipController;
        client = ExteraHttpClient.INSTANCE.getClient();
        gson = new Gson();
        registry = new ConcurrentHashMap<>();
        installLocks = new ConcurrentHashMap<>();
        wheelArchivePolicy = SecurityPolicy.INSTANCE.builder().maxEntries(50000L).maxTotalUncompressedSize(524288000L).maxSingleFileSize(MAX_WHEEL_BYTES).maxCompressionRatio(500.0d).build();
        pythonVersion = Deobfuscator$exteraGramDev$TMessagesProj.getString(-59079291848239L);
        REGEX_NORMALIZE = new Regex(Deobfuscator$exteraGramDev$TMessagesProj.getString(-59044932109871L));
        REGEX_REQ_PARSE = new Regex(Deobfuscator$exteraGramDev$TMessagesProj.getString(-57485858981423L));
        REGEX_REQ_SPECS = new Regex(Deobfuscator$exteraGramDev$TMessagesProj.getString(-57524513687087L));
        REGEX_REQ_EXTRA = new Regex(Deobfuscator$exteraGramDev$TMessagesProj.getString(-57657657673263L));
        REGEX_REQ_PAREN = new Regex(Deobfuscator$exteraGramDev$TMessagesProj.getString(-57215276041775L));
        REGEX_VERSION_SPLIT = new Regex(Deobfuscator$exteraGramDev$TMessagesProj.getString(-57228160943663L));
        REGEX_MARKER_TOKEN = new Regex(Deobfuscator$exteraGramDev$TMessagesProj.getString(-57258225714735L));
        REGEX_VERSION_WILDCARD = new Regex(Deobfuscator$exteraGramDev$TMessagesProj.getString(-58220298389039L));
        pipController.loadRegistry();
        Set of = SetsKt.setOf((Object[]) new String[]{Deobfuscator$exteraGramDev$TMessagesProj.getString(-57756441921071L), Deobfuscator$exteraGramDev$TMessagesProj.getString(-57812276495919L), Deobfuscator$exteraGramDev$TMessagesProj.getString(-57915355711023L), Deobfuscator$exteraGramDev$TMessagesProj.getString(-57876701005359L), Deobfuscator$exteraGramDev$TMessagesProj.getString(-57988370155055L), Deobfuscator$exteraGramDev$TMessagesProj.getString(-57941125514799L), Deobfuscator$exteraGramDev$TMessagesProj.getString(-56407822190127L)});
        ArrayList arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(of, 10));
        Iterator it = of.iterator();
        while (it.hasNext()) {
            arrayList.add(INSTANCE.normalizePackageName((String) it.next()));
        }
        PREINSTALLED_PACKAGES = CollectionsKt.toSet(arrayList);
    }

    private final File getLibsDir() {
        return SimpliFiles.directory(new File(PluginsController.INSTANCE.getInstance().getPluginsDir(), Deobfuscator$exteraGramDev$TMessagesProj.getString(-84694476801583L))).create().getFile();
    }

    private final File getRegistryFile() {
        return new File(getLibsDir(), Deobfuscator$exteraGramDev$TMessagesProj.getString(-84776081180207L));
    }

    public final String getPythonVersion() {
        return pythonVersion;
    }

    public final void setPythonVersion(String str) {
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-84836210722351L);
        pythonVersion = str;
    }

    /* JADX INFO: loaded from: classes4.dex */
    public static final /* data */ class WheelCandidate {
        private final String downloadUrl;
        private final String expectedSha256;
        private final String version;

        public static /* synthetic */ WheelCandidate copy$default(WheelCandidate wheelCandidate, String str, String str2, String str3, int i, Object obj) {
            if ((i & 1) != 0) {
                str = wheelCandidate.version;
            }
            if ((i & 2) != 0) {
                str2 = wheelCandidate.downloadUrl;
            }
            if ((i & 4) != 0) {
                str3 = wheelCandidate.expectedSha256;
            }
            return wheelCandidate.copy(str, str2, str3);
        }

        /* JADX INFO: renamed from: component1, reason: from getter */
        public final String getVersion() {
            return this.version;
        }

        /* JADX INFO: renamed from: component2, reason: from getter */
        public final String getDownloadUrl() {
            return this.downloadUrl;
        }

        /* JADX INFO: renamed from: component3, reason: from getter */
        public final String getExpectedSha256() {
            return this.expectedSha256;
        }

        public final WheelCandidate copy(String version, String downloadUrl, String expectedSha256) {
            Deobfuscator$exteraGramDev$TMessagesProj.getString(-84973649675823L);
            Deobfuscator$exteraGramDev$TMessagesProj.getString(-84939289937455L);
            return new WheelCandidate(version, downloadUrl, expectedSha256);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof WheelCandidate)) {
                return false;
            }
            WheelCandidate wheelCandidate = (WheelCandidate) other;
            return Intrinsics.areEqual(this.version, wheelCandidate.version) && Intrinsics.areEqual(this.downloadUrl, wheelCandidate.downloadUrl) && Intrinsics.areEqual(this.expectedSha256, wheelCandidate.expectedSha256);
        }

        public int hashCode() {
            int iHashCode = ((this.version.hashCode() * 31) + this.downloadUrl.hashCode()) * 31;
            String str = this.expectedSha256;
            return iHashCode + (str == null ? 0 : str.hashCode());
        }

        public String toString() {
            return Deobfuscator$exteraGramDev$TMessagesProj.getString(-85059549021743L) + this.version + Deobfuscator$exteraGramDev$TMessagesProj.getString(-85093908760111L) + this.downloadUrl + Deobfuscator$exteraGramDev$TMessagesProj.getString(-85166923204143L) + this.expectedSha256 + ')';
        }

        public WheelCandidate(String str, String str2, String str3) {
            Deobfuscator$exteraGramDev$TMessagesProj.getString(-86468298294831L);
            Deobfuscator$exteraGramDev$TMessagesProj.getString(-86571377509935L);
            this.version = str;
            this.downloadUrl = str2;
            this.expectedSha256 = str3;
        }
    }

    /* JADX INFO: loaded from: classes4.dex */
    public static final /* data */ class ParsedVersion {
        private final int epoch;
        private final List<String> parts;
        private final String publicVersion;

        /* JADX WARN: Multi-variable type inference failed */
        public static /* synthetic */ ParsedVersion copy$default(ParsedVersion parsedVersion, int i, String str, List list, int i2, Object obj) {
            if ((i2 & 1) != 0) {
                i = parsedVersion.epoch;
            }
            if ((i2 & 2) != 0) {
                str = parsedVersion.publicVersion;
            }
            if ((i2 & 4) != 0) {
                list = parsedVersion.parts;
            }
            return parsedVersion.copy(i, str, list);
        }

        /* JADX INFO: renamed from: component1, reason: from getter */
        public final int getEpoch() {
            return this.epoch;
        }

        /* JADX INFO: renamed from: component2, reason: from getter */
        public final String getPublicVersion() {
            return this.publicVersion;
        }

        public final List<String> component3() {
            return this.parts;
        }

        public final ParsedVersion copy(int epoch, String publicVersion, List<String> parts) {
            Deobfuscator$exteraGramDev$TMessagesProj.getString(-71259819099695L);
            Deobfuscator$exteraGramDev$TMessagesProj.getString(-71319948641839L);
            return new ParsedVersion(epoch, publicVersion, parts);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof ParsedVersion)) {
                return false;
            }
            ParsedVersion parsedVersion = (ParsedVersion) other;
            return this.epoch == parsedVersion.epoch && Intrinsics.areEqual(this.publicVersion, parsedVersion.publicVersion) && Intrinsics.areEqual(this.parts, parsedVersion.parts);
        }

        public int hashCode() {
            return (((Integer.hashCode(this.epoch) * 31) + this.publicVersion.hashCode()) * 31) + this.parts.hashCode();
        }

        public String toString() {
            return Deobfuscator$exteraGramDev$TMessagesProj.getString(-71276998968879L) + this.epoch + Deobfuscator$exteraGramDev$TMessagesProj.getString(-71358603347503L) + this.publicVersion + Deobfuscator$exteraGramDev$TMessagesProj.getString(-70959171388975L) + this.parts + ')';
        }

        public ParsedVersion(int i, String str, List<String> list) {
            Deobfuscator$exteraGramDev$TMessagesProj.getString(-70624163939887L);
            Deobfuscator$exteraGramDev$TMessagesProj.getString(-71234049295919L);
            this.epoch = i;
            this.publicVersion = str;
            this.parts = list;
        }

        public final List<String> getParts() {
            return this.parts;
        }
    }

    private final String normalizePackageName(String name) {
        String lowerCase = name.toLowerCase(Locale.ROOT);
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-84801850983983L);
        return REGEX_NORMALIZE.replace(lowerCase, Deobfuscator$exteraGramDev$TMessagesProj.getString(-84883455362607L));
    }

    private final synchronized void loadRegistry() {
        Object objM2315constructorimpl;
        if (!getRegistryFile().exists()) {
            registry.clear();
            return;
        }
        try {
            Object objFromJson = gson.fromJson(SimpliFile.readText$default(SimpliFiles.file(getRegistryFile()), 4194304L, null, 2, null), new TypeToken<Map<String, ? extends Map<String, ? extends Set<? extends String>>>>() { // from class: com.exteragram.messenger.plugins.pip.PipController$loadRegistry$type$1
            }.getType());
            Deobfuscator$exteraGramDev$TMessagesProj.getString(-84892045297199L);
            registry.clear();
            for (Map.Entry<String, Map<String, Set<String>>> entry : ((Map<String, Map<String, Set<String>>>) objFromJson).entrySet()) {
                String str = entry.getKey();
                Map<String, Set<String>> map = entry.getValue();
                String strNormalizePackageName = INSTANCE.normalizePackageName(str);
                ConcurrentHashMap<String, ConcurrentHashMap.KeySetView<String, Boolean>> concurrentHashMap = new ConcurrentHashMap<>();
                for (Map.Entry<String, Set<String>> entry2 : map.entrySet()) {
                    String str2 = entry2.getKey();
                    Set<String> set = entry2.getValue();
                    ConcurrentHashMap.KeySetView<String, Boolean> keySetViewNewKeySet = ConcurrentHashMap.newKeySet();
                    keySetViewNewKeySet.addAll(set);
                    concurrentHashMap.put(str2, keySetViewNewKeySet);
                }
                registry.put(strNormalizePackageName, (ConcurrentHashMap) concurrentHashMap);
            }
        } catch (Exception e) {
            FileLog.e(e);
            registry.clear();
            try {
                getRegistryFile().renameTo(new File(getLibsDir(), "registry.json.bak"));
            } catch (Throwable th) {
                FileLog.e(th);
            }
        }
    }

    private final synchronized Map<String, Map<String, Set<String>>> snapshotRegistry() {
        LinkedHashMap linkedHashMap;
        try {
            ConcurrentHashMap<String, ConcurrentHashMap<String, Set<String>>> concurrentHashMap = registry;
            linkedHashMap = new LinkedHashMap(MapsKt.mapCapacity(concurrentHashMap.size()));
            for (Object obj : concurrentHashMap.entrySet()) {
                Object key = ((Map.Entry) obj).getKey();
                ConcurrentHashMap concurrentHashMap2 = (ConcurrentHashMap) ((Map.Entry) obj).getValue();
                LinkedHashMap linkedHashMap2 = new LinkedHashMap(MapsKt.mapCapacity(concurrentHashMap2.size()));
                for (Object obj2 : concurrentHashMap2.entrySet()) {
                    linkedHashMap2.put(((Map.Entry) obj2).getKey(), CollectionsKt.toSet((Set) ((Map.Entry) obj2).getValue()));
                }
                linkedHashMap.put(key, linkedHashMap2);
            }
        } catch (Throwable th) {
            throw th;
        }
        return linkedHashMap;
    }

    private final synchronized void restoreRegistry(Map<String, ? extends Map<String, ? extends Set<String>>> snapshot) {
        try {
            registry.clear();
            for (Map.Entry<String, ? extends Map<String, ? extends Set<String>>> entry : snapshot.entrySet()) {
                String key = entry.getKey();
                Map<String, ? extends Set<String>> value = entry.getValue();
                ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
                for (Map.Entry<String, ? extends Set<String>> entry2 : value.entrySet()) {
                    String key2 = entry2.getKey();
                    Set<String> value2 = entry2.getValue();
                    ConcurrentHashMap.KeySetView keySetViewNewKeySet = ConcurrentHashMap.newKeySet();
                    keySetViewNewKeySet.addAll(value2);
                    concurrentHashMap.put(key2, keySetViewNewKeySet);
                }
                registry.put(key, (ConcurrentHashMap<String, Set<String>>) concurrentHashMap);
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private final synchronized void saveRegistry() {
        try {
            String json = gson.toJson(snapshotRegistry());
            Files.write(getRegistryFile().toPath(), json.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public final void cleanup() {
        try { cleanupInternal(); } catch (Exception e) {}
    }

    public static /* synthetic */ List installDependencies$default(PipController pipController, List list, String str, InstallerDelegate installerDelegate, int i, Object obj) {
        if ((i & 4) != 0) {
            installerDelegate = null;
        }
        return pipController.installDependencies(list, str, installerDelegate);
    }

    public final List<String> installDependencies(List<String> requirements, String pluginId, InstallerDelegate delegate) {
        ArrayList<String> arrayList = new ArrayList<>();
        Set<Pair<String, String>> linkedHashSet = new LinkedHashSet<>();
        Map<String, Map<String, Set<String>>> mapSnapshotRegistry = snapshotRegistry();
        try {
            for (String str2 : requirements) {
                if (delegate != null && delegate.isCancelled()) {
                    throw new IOException("Installation cancelled");
                }
                if (str2 != null && !str2.trim().isEmpty()) {
                    Pair<String, List<Pair<String, String>>> requirement = parseRequirement(str2);
                    String strComponent1 = requirement.component1();
                    List<Pair<String, String>> listComponent2 = requirement.component2();
                    String strNormalizePackageName = normalizePackageName(strComponent1);
                    if (!PREINSTALLED_PACKAGES.contains(strNormalizePackageName)) {
                        resolveAndInstall(strNormalizePackageName, listComponent2, linkedHashSet, pluginId, delegate);
                    }
                }
            }
            updateRegistryForPlugin(pluginId, linkedHashSet);
            for (Pair<String, String> pair : linkedHashSet) {
                String absolutePath = getLibPath(pair.component1(), pair.component2()).getAbsolutePath();
                arrayList.add(absolutePath);
            }
            saveRegistry();
            installLocks.clear();
            return arrayList;
        } catch (Exception exc) {
            FileLog.e(exc);
            restoreRegistry(mapSnapshotRegistry);
            removeOrphanedDirectories();
            installLocks.clear();
            throw new RuntimeException(exc);
        } catch (Throwable th) {
            installLocks.clear();
            throw th;
        }
    }

    private final void resolveAndInstall(String pkg, List<Pair<String, String>> specs, Set<Pair<String, String>> installedAccumulator, String pluginId, InstallerDelegate delegate) throws IOException {
        String strInstallPackage;
        Set<Map.Entry<String, Set<String>>> setEntrySet;
        if (delegate != null && delegate.isCancelled()) {
            FileLog.e(new IOException("Cancelled"));
            return;
        }
        ConcurrentHashMap<String, Object> concurrentHashMap = installLocks;
        final Function1 function1 = new Function1() {
            @Override
            public final Object invoke(Object obj2) {
                return PipController.$r8$lambda$nM3N8RYnbiCU76VyVnmjZ3F9qY8((String) obj2);
            }
        };
        Object objComputeIfAbsent = concurrentHashMap.computeIfAbsent(pkg, new Function() {
            @Override
            public final Object apply(Object obj2) {
                return function1.invoke(obj2);
            }
        });
        synchronized (objComputeIfAbsent) {
            if (delegate != null && delegate.isCancelled()) {
                throw new IOException("Cancelled");
            }
            ConcurrentHashMap<String, Set<String>> concurrentHashMap2 = registry.get(pkg);
            Map.Entry<String, Set<String>> entry = null;
            if (concurrentHashMap2 != null && (setEntrySet = concurrentHashMap2.entrySet()) != null) {
                for (Map.Entry<String, Set<String>> item : setEntrySet) {
                    if (item.getValue() != null && !item.getValue().isEmpty()) {
                        entry = item;
                        break;
                    }
                }
            }
            if (entry != null) {
                strInstallPackage = entry.getKey();
                Set<String> value2 = entry.getValue();
                String strJoinToString$default = TextUtils.join(", ", value2);
                if (!INSTANCE.checkVersionSatisfies(strInstallPackage, specs)) {
                    throw new IOException("Conflict for " + pkg + " version " + strInstallPackage);
                }
                FileLog.d("Using installed " + strInstallPackage + " for " + pkg);
            } else {
                PipController pipController2 = INSTANCE;
                String strFindInstalledVersion = pipController2.findInstalledVersion(pkg, specs);
                if (strFindInstalledVersion != null) {
                    strInstallPackage = strFindInstalledVersion;
                } else {
                    strFindInstalledVersion = pipController2.findVersionOnDisk(pkg, specs);
                    if (strFindInstalledVersion != null) {
                        FileLog.d("Found on disk: " + pkg + " " + strFindInstalledVersion);
                        strInstallPackage = strFindInstalledVersion;
                    } else {
                        strInstallPackage = pipController2.installPackage(pkg, specs, delegate);
                    }
                }
            }
        }
        Pair<String, String> pair = null;
        for (Pair<String, String> item : installedAccumulator) {
            if (pkg.equals(item.getFirst())) {
                pair = item;
                break;
            }
        }
        if (pair != null) {
            String str2 = pair.getSecond();
            if (VersionComparator.INSTANCE.compare(strInstallPackage, str2) <= 0) {
                return;
            }
            FileLog.d("Upgrading dependency " + pkg);
            installedAccumulator.remove(pair);
        }
        ConcurrentHashMap<String, Set<String>> pkgMap = registry.get(pkg);
        if (pkgMap == null) {
            pkgMap = new ConcurrentHashMap<>();
            registry.put(pkg, pkgMap);
        }
        pkgMap.computeIfAbsent(strInstallPackage, k -> ConcurrentHashMap.newKeySet()).add(pluginId);
        installedAccumulator.add(new Pair<>(pkg, strInstallPackage));
        File fileFindMetadataFile = findMetadataFile(pkg, strInstallPackage);
        if (fileFindMetadataFile == null || !fileFindMetadataFile.exists()) {
            return;
        }
        for (String str3 : parseDependenciesFromMetadata(fileFindMetadataFile)) {
            if (delegate != null && delegate.isCancelled()) {
                throw new IOException("Cancelled");
            }
            Pair<String, List<Pair<String, String>>> requirement = parseRequirement(str3);
            resolveAndInstall(normalizePackageName(requirement.component1()), requirement.component2(), installedAccumulator, pluginId, delegate);
        }
    }

    public static Object $r8$lambda$nM3N8RYnbiCU76VyVnmjZ3F9qY8(String str) {
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-58357737342511L);
        return new Object();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final CharSequence resolveAndInstall$lambda$2$1(Pair pair) {
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-58362032309807L);
        return ((String) pair.getFirst()) + ((String) pair.getSecond());
    }

    /* JADX INFO: renamed from: $r8$lambda$4GaAqKHHkDxhUEp6Ixv2955-PQ8, reason: not valid java name */
    public static ConcurrentHashMap m1326$r8$lambda$4GaAqKHHkDxhUEp6Ixv2955PQ8(Function1 function1, Object obj) {
        return (ConcurrentHashMap) function1.invoke(obj);
    }

    /* JADX INFO: renamed from: $r8$lambda$cKu7WIfB65dGzoot-wJY-_PzblQ, reason: not valid java name */
    public static ConcurrentHashMap m1329$r8$lambda$cKu7WIfB65dGzootwJY_PzblQ(String str) {
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-58452226623023L);
        return new ConcurrentHashMap();
    }

    public static Set $r8$lambda$XFN9dt32lYY3xCJ9NHZLBcosePw(Function1 function1, Object obj) {
        return (Set) function1.invoke(obj);
    }

    public static Set $r8$lambda$ba_CzsM0FMRcDjro_ht7norbT1M(String str) {
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-58456521590319L);
        return Collections.newSetFromMap(new ConcurrentHashMap());
    }

    private final void updateRegistryForPlugin(String pluginId, Set<Pair<String, String>> currentlyNeeded) throws IOException {
        ArrayList arrayList = new ArrayList();
        for (Map.Entry<String, ConcurrentHashMap<String, Set<String>>> entry : registry.entrySet()) {
            String key = entry.getKey();
            for (Map.Entry<String, Set<String>> entry2 : entry.getValue().entrySet()) {
                String key2 = entry2.getKey();
                Set<String> value = entry2.getValue();
                Pair pair = TuplesKt.to(key, key2);
                if (value.contains(pluginId) && !currentlyNeeded.contains(pair)) {
                    value.remove(pluginId);
                    if (value.isEmpty()) {
                        arrayList.add(pair);
                    }
                }
            }
        }
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            Pair pair2 = (Pair) obj;
            String str = (String) pair2.component1();
            String str2 = (String) pair2.component2();
            ConcurrentHashMap<String, ConcurrentHashMap<String, Set<String>>> concurrentHashMap = registry;
            ConcurrentHashMap<String, Set<String>> concurrentHashMap2 = concurrentHashMap.get(str);
            if (concurrentHashMap2 != null) {
                concurrentHashMap2.remove(str2);
            }
            ConcurrentHashMap<String, Set<String>> concurrentHashMap3 = concurrentHashMap.get(str);
            if (concurrentHashMap3 != null && concurrentHashMap3.isEmpty()) {
                concurrentHashMap.remove(str);
            }
            INSTANCE.deletePackage(str, str2);
        }
    }

    private final void cleanupInternal() throws IOException {
        ArrayList arrayList = new ArrayList();
        for (Map.Entry<String, ConcurrentHashMap<String, Set<String>>> entry : registry.entrySet()) {
            String key = entry.getKey();
            for (Map.Entry<String, Set<String>> entry2 : entry.getValue().entrySet()) {
                String key2 = entry2.getKey();
                if (entry2.getValue().isEmpty()) {
                    arrayList.add(TuplesKt.to(key, key2));
                }
            }
        }
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            Pair pair = (Pair) obj;
            String str = (String) pair.component1();
            String str2 = (String) pair.component2();
            ConcurrentHashMap<String, ConcurrentHashMap<String, Set<String>>> concurrentHashMap = registry;
            ConcurrentHashMap<String, Set<String>> concurrentHashMap2 = concurrentHashMap.get(str);
            if (concurrentHashMap2 != null) {
                concurrentHashMap2.remove(str2);
            }
            ConcurrentHashMap<String, Set<String>> concurrentHashMap3 = concurrentHashMap.get(str);
            if (concurrentHashMap3 != null && concurrentHashMap3.isEmpty()) {
                concurrentHashMap.remove(str);
            }
            INSTANCE.deletePackage(str, str2);
        }
        removeOrphanedDirectories();
    }

    private final void removeOrphanedDirectories() {
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        for (Map.Entry<String, ConcurrentHashMap<String, Set<String>>> entry : registry.entrySet()) {
            String key = entry.getKey();
            Set<String> setKeySet = entry.getValue().keySet();
            Deobfuscator$exteraGramDev$TMessagesProj.getString(-81782488974895L);
            Iterator<String> it = setKeySet.iterator();
            while (it.hasNext()) {
                try {
                    linkedHashSet.add(INSTANCE.getLibPath(key, (String) it.next()).getCanonicalPath());
                } catch (IOException unused) {
                }
            }
        }
        File[] fileArrListFiles = getLibsDir().listFiles();
        if (fileArrListFiles != null) {
            for (File file : fileArrListFiles) {
                if (file.isDirectory()) {
                    try {
                        if (!linkedHashSet.contains(file.getCanonicalPath())) {
                            FileLog.d(Deobfuscator$exteraGramDev$TMessagesProj.getString(-81851208451631L) + file.getName());
                            SimpliFiles.directory(file).deleteRecursively();
                        }
                    } catch (Exception e) {
                        FileLog.e(e);
                    }
                }
            }
        }
    }

    public final Set<String> activeLibraryPaths() {
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        for (Map.Entry<String, ConcurrentHashMap<String, Set<String>>> entry : registry.entrySet()) {
            String key = entry.getKey();
            for (Map.Entry<String, Set<String>> entry2 : entry.getValue().entrySet()) {
                String key2 = entry2.getKey();
                if (!entry2.getValue().isEmpty()) {
                    try {
                        linkedHashSet.add(INSTANCE.getLibPath(key, key2).getAbsolutePath());
                    } catch (IOException e) {
                        FileLog.e(Deobfuscator$exteraGramDev$TMessagesProj.getString(-81546265773615L) + key + ' ' + key2, e);
                    }
                }
            }
        }
        return linkedHashSet;
    }

    public final void uninstallDependencies(String pluginId) {
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-82220575639087L);
        ArrayList arrayList = new ArrayList();
        int i = 0;
        boolean z = false;
        for (Map.Entry<String, ConcurrentHashMap<String, Set<String>>> entry : registry.entrySet()) {
            String key = entry.getKey();
            for (Map.Entry<String, Set<String>> entry2 : entry.getValue().entrySet()) {
                String key2 = entry2.getKey();
                Set<String> value = entry2.getValue();
                if (value.remove(pluginId)) {
                    if (value.isEmpty()) {
                        arrayList.add(TuplesKt.to(key, key2));
                    }
                    z = true;
                }
            }
        }
        int size = arrayList.size();
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            Pair pair = (Pair) obj;
            String str = (String) pair.component1();
            String str2 = (String) pair.component2();
            ConcurrentHashMap<String, ConcurrentHashMap<String, Set<String>>> concurrentHashMap = registry;
            ConcurrentHashMap<String, Set<String>> concurrentHashMap2 = concurrentHashMap.get(str);
            if (concurrentHashMap2 != null) {
                concurrentHashMap2.remove(str2);
            }
            ConcurrentHashMap<String, Set<String>> concurrentHashMap3 = concurrentHashMap.get(str);
            if (concurrentHashMap3 != null && concurrentHashMap3.isEmpty()) {
                concurrentHashMap.remove(str);
            }
            try { INSTANCE.deletePackage(str, str2); } catch (Exception e) {}
        }
        if (z) {
            saveRegistry();
        }
    }

    /* JADX INFO: renamed from: $r8$lambda$iez_jk-cSyBPRjplKRoDAzlgN2I, reason: not valid java name */
    public static CharSequence m1330$r8$lambda$iez_jkcSyBPRjplKRoDAzlgN2I(Pair pair) {
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-58477996426799L);
        return ((String) pair.getFirst()) + ((String) pair.getSecond());
    }

    private final String installPackage(String pkg, List<Pair<String, String>> specs, final InstallerDelegate delegate) throws IOException {
        FileLog.d("Installing package " + pkg);
        if (delegate != null) {
            delegate.onProgress("Installing " + pkg + "...");
        }
        Response responseExecuteWithRetry = executeWithRetry(new Request.Builder().url("https://pypi.org/pypi/" + pkg + "/json").build(), delegate);
        try {
            if (!responseExecuteWithRetry.isSuccessful()) {
                if (responseExecuteWithRetry.code() == 404) {
                    throw new IOException("Package not found: " + pkg);
                }
                throw new IOException("HTTP error " + responseExecuteWithRetry.code() + " for " + pkg);
            }
            JsonObject jsonObject = (JsonObject) gson.fromJson(responseExecuteWithRetry.body().string(), JsonObject.class);
            if (jsonObject == null) {
                throw new IOException("Failed to parse JSON for " + pkg);
            }
            JsonObject asJsonObject = jsonObject.getAsJsonObject("releases");
            if (asJsonObject == null) {
                throw new IOException("No releases found for " + pkg);
            }
            Set<String> setKeySet = asJsonObject.keySet();
            List<String> list = new ArrayList<>(setKeySet);
            ArrayList<String> arrayList = new ArrayList<>();
            for (String obj : list) {
                if (INSTANCE.checkVersionSatisfies(obj, specs)) {
                    arrayList.add(obj);
                }
            }
            List<String> listSortedWith = filterPreReleases(arrayList, specs);
            Collections.sort(listSortedWith, (o1, o2) -> VersionComparator.INSTANCE.compare(o2, o1));
            WheelCandidate wheelCandidate = null;
            for (String str : listSortedWith) {
                JsonArray asJsonArray = asJsonObject.getAsJsonArray(str);
                if (asJsonArray != null) {
                    Iterator<JsonElement> it2 = asJsonArray.iterator();
                    while (it2.hasNext()) {
                        WheelCandidate wheelCandidateSelectWheelCandidate = INSTANCE.selectWheelCandidate(str, it2.next().getAsJsonObject(), false);
                        if (wheelCandidateSelectWheelCandidate != null) {
                            wheelCandidate = wheelCandidateSelectWheelCandidate;
                            break;
                        }
                    }
                    if (wheelCandidate != null) {
                        break;
                    }
                }
            }
            responseExecuteWithRetry.close();
            if (wheelCandidate == null) {
                throw new IOException("No matching wheel found for " + pkg);
            }
            File libPath = getLibPath(pkg, wheelCandidate.getVersion());
            File file = new File(getLibsDir(), "temp_" + pkg + "_" + System.currentTimeMillis());
            file.mkdirs();
            File file2 = new File(file, "extracted");
            file2.mkdirs();
            File file3 = new File(file, "download.whl");
            Response responseExecuteWithRetry2 = executeWithRetry(new Request.Builder().url(wheelCandidate.getDownloadUrl()).build(), delegate);
            try {
                if (!responseExecuteWithRetry2.isSuccessful()) {
                    throw new IOException("Download failed with code " + responseExecuteWithRetry2.code());
                }
                long contentLength = responseExecuteWithRetry2.body().contentLength();
                if (contentLength > MAX_WHEEL_BYTES) {
                    throw new IOException("Wheel file too large: " + contentLength + " bytes");
                }
                try (InputStream inputStream = responseExecuteWithRetry2.body().byteStream();
                     java.io.FileOutputStream outputStream = new java.io.FileOutputStream(file3)) {
                    byte[] buffer = new byte[8192];
                    int read;
                    while ((read = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, read);
                    }
                }
                responseExecuteWithRetry2.close();
                if (wheelCandidate.getExpectedSha256() != null && !calculateSha256(file3).equalsIgnoreCase(wheelCandidate.getExpectedSha256())) {
                    throw new IOException("SHA256 checksum mismatch");
                }
                SimpliFiles.archive(file3).extractToDirectory(file2, null);
                if (libPath.exists()) {
                    SimpliFiles.directory(libPath).deleteRecursively();
                }
                SimpliFiles.directory(file2).moveTo(libPath, OverwritePolicy.REPLACE);
                SimpliFiles.directory(file).deleteRecursively();
                return wheelCandidate.getVersion();
            } catch (Exception e) {
                SimpliFiles.directory(file).deleteRecursively();
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            responseExecuteWithRetry.close();
            throw new RuntimeException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final int installPackage$lambda$1$1(String str, String str2) {
        return VersionComparator.INSTANCE.compare(str2, str);
    }

    public static boolean $r8$lambda$WCaEarcnhdtxiKzUVomzfeStLN0(InstallerDelegate installerDelegate) {
        return installerDelegate != null && installerDelegate.isCancelled();
    }

    private final WheelCandidate selectWheelCandidate(String version, JsonObject artifact, boolean allowYanked) {
        String stringOrNull;
        String stringOrNull2;
        String stringOrNull3 = getStringOrNull(artifact, Deobfuscator$exteraGramDev$TMessagesProj.getString(-60492336088623L));
        if (stringOrNull3 == null || !Intrinsics.areEqual(stringOrNull3, Deobfuscator$exteraGramDev$TMessagesProj.getString(-60543875696175L))) {
            return null;
        }
        if ((!allowYanked && Intrinsics.areEqual(getBooleanOrNull(artifact, Deobfuscator$exteraGramDev$TMessagesProj.getString(-60664134780463L)), Boolean.TRUE)) || (stringOrNull = getStringOrNull(artifact, Deobfuscator$exteraGramDev$TMessagesProj.getString(-60634070009391L))) == null || !isPurePythonWheelCompatible(stringOrNull)) {
            return null;
        }
        String stringOrNull4 = getStringOrNull(artifact, Deobfuscator$exteraGramDev$TMessagesProj.getString(-60732854257199L));
        if ((stringOrNull4 != null && !checkVersionSatisfies(pythonVersion, parseSpecs(stringOrNull4))) || (stringOrNull2 = getStringOrNull(artifact, Deobfuscator$exteraGramDev$TMessagesProj.getString(-61351329547823L))) == null) {
            return null;
        }
        JsonObject asJsonObject = artifact.getAsJsonObject(Deobfuscator$exteraGramDev$TMessagesProj.getString(-61299789940271L));
        return new WheelCandidate(version, stringOrNull2, asJsonObject != null ? getStringOrNull(asJsonObject, Deobfuscator$exteraGramDev$TMessagesProj.getString(-61402869155375L)) : null);
    }

    private final String getStringOrNull(JsonObject jsonObject, String str) {
        JsonElement jsonElement;
        if (!jsonObject.has(str) || (jsonElement = jsonObject.get(str)) == null || jsonElement.isJsonNull()) {
            return null;
        }
        return jsonElement.getAsString();
    }

    private final Boolean getBooleanOrNull(JsonObject jsonObject, String str) {
        if (!jsonObject.has(str) || jsonObject.get(str) == null || jsonObject.get(str).isJsonNull()) {
            return null;
        }
        try {
            return jsonObject.get(str).getAsBoolean();
        } catch (Throwable th) {
            return null;
        }
    }

    private final boolean isPurePythonWheelCompatible(String filename) {
        if (filename == null || !filename.endsWith(".whl")) {
            return false;
        }
        String nameWithoutExt = filename.substring(0, filename.length() - 4);
        String[] parts = nameWithoutExt.split("-");
        if (parts.length < 5) {
            return false;
        }
        String str = parts[parts.length - 3];
        String str2 = parts[parts.length - 2];
        String str3 = parts[parts.length - 1];
        if (str2.equals("none") && str3.equals("any")) {
            String[] tags = str.split("\\.");
            for (String tag : tags) {
                if (isPythonTagCompatible(tag)) {
                    return true;
                }
            }
        }
        return false;
    }

    private final boolean isPythonTagCompatible(String tag) {
        String[] parts = pythonVersion.split("\\.");
        if (parts.length >= 2) {
            try {
                int major = Integer.parseInt(parts[0]);
                int minor = Integer.parseInt(parts[1]);
                String lowerCase = tag.toLowerCase(Locale.ROOT);
                if (lowerCase.equals("py" + major) || lowerCase.equals("py" + major + minor)) {
                    return true;
                }
                if (lowerCase.startsWith("py3")) {
                    int tagMinor = Integer.parseInt(lowerCase.substring(3));
                    if (tagMinor <= minor) {
                        return true;
                    }
                }
            } catch (Exception unused) {
            }
        }
        return false;
    }

    private final Response executeWithRetry(Request request, InstallerDelegate delegate) throws IOException {
        int i = 0;
        IOException e = null;
        while (i < 3) {
            if (delegate != null && delegate.isCancelled()) {
                throw new IOException("Cancelled");
            }
            try {
                return client.newCall(request).execute();
            } catch (IOException e2) {
                e = e2;
                i++;
                FileLog.w("Retry " + i + " failed: " + e.getMessage());
                try {
                    Thread.sleep(((long) i) * 1000);
                } catch (InterruptedException unused) {
                }
            }
        }
        if (delegate != null && delegate.isCancelled()) {
            throw new IOException("Cancelled");
        }
        if (e != null) {
            throw e;
        }
        throw new IOException("Execute failed");
    }

    private final String findVersionOnDisk(String pkg, final List<Pair<String, String>> specs) {
        final String str = pkg + "+";
        File[] fileArrListFiles = getLibsDir().listFiles();
        if (fileArrListFiles == null) {
            return null;
        }
        ArrayList<String> versions = new ArrayList<>();
        for (File file : fileArrListFiles) {
            if (file.isDirectory() && file.getName().startsWith(str)) {
                String ver = file.getName().substring(str.length());
                if (!ver.isEmpty() && checkVersionSatisfies(ver, specs)) {
                    versions.add(ver);
                }
            }
        }
        if (versions.isEmpty()) {
            return null;
        }
        List<String> filtered = filterPreReleases(versions, specs);
        Collections.sort(filtered, VersionComparator.INSTANCE);
        return filtered.isEmpty() ? null : filtered.get(filtered.size() - 1);
    }

    /* JADX INFO: renamed from: $r8$lambda$3qb6Nezx6TG2Tac-1y7Xa35y3cA, reason: not valid java name */
    public static boolean m1325$r8$lambda$3qb6Nezx6TG2Tac1y7Xa35y3cA(String str, File file) {
        if (file.isDirectory()) {
            String name = file.getName();
            Deobfuscator$exteraGramDev$TMessagesProj.getString(-58413571917359L);
            if (name.startsWith(str)) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: renamed from: $r8$lambda$Lm-FvS3nVC4K08Dx0OuVVeARUQM, reason: not valid java name */
    public static String m1328$r8$lambda$LmFvS3nVC4K08Dx0OuVVeARUQM(String str, File file) {
        String name = file.getName();
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-58546715903535L);
        String strSubstring = name.substring(str.length());
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-59143716357679L);
        return strSubstring;
    }

    /* JADX INFO: renamed from: $r8$lambda$GBc-8aKnKaBWkdqFW6gQZ9PEPeQ, reason: not valid java name */
    public static boolean m1327$r8$lambda$GBc8aKnKaBWkdqFW6gQZ9PEPeQ(List list, String str) {
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-59216730801711L);
        return str.length() > 0 && INSTANCE.checkVersionSatisfies(str, list);
    }

    private final String findInstalledVersion(String pkg, List<Pair<String, String>> specs) {
        ConcurrentHashMap<String, Set<String>> concurrentHashMap = registry.get(pkg);
        if (concurrentHashMap == null) {
            return null;
        }
        Set<String> setKeySet = concurrentHashMap.keySet();
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-59886745699887L);
        ArrayList arrayList = new ArrayList();
        for (Object obj : setKeySet) {
            if (INSTANCE.checkVersionSatisfies((String) obj, specs)) {
                arrayList.add(obj);
            }
        }
        return (String) CollectionsKt.maxWithOrNull(filterPreReleases(arrayList, specs), VersionComparator.INSTANCE);
    }

    private final File getLibPath(String pkg, String version) throws IOException {
        if (version.contains("..") || version.contains("/") || version.contains("\\")) {
            throw new IOException("Invalid version: " + version);
        }
        return new File(getLibsDir(), pkg + "+" + version);
    }

    private final void deletePackage(String pkg, String version) throws IOException {
        File libPath = getLibPath(pkg, version);
        if (libPath.exists()) {
            try {
                SimpliFiles.directory(libPath).deleteRecursively();
                FileLog.d("Deleted package " + libPath.getName());
            } catch (Throwable th) {
                FileLog.e(th);
            }
        }
    }

    private static final Pattern PATTERN_REQ_PARSE = Pattern.compile("^([a-zA-Z0-9_.-]+)\\s*(?:\\((.*)\\)|(.*))?$");
    private static final Pattern PATTERN_REQ_SPECS = Pattern.compile("^(==|!=|<=|>=|<|>|~=)\\s*([a-zA-Z0-9_.-]+)$");

    private final Pair<String, List<Pair<String, String>>> parseRequirement(String req) {
        String cleanReq = req.split(";")[0].replaceAll("#.*", "").trim();
        Matcher matcher = PATTERN_REQ_PARSE.matcher(cleanReq);
        if (!matcher.find()) {
            return new Pair<>(cleanReq, Collections.emptyList());
        }
        String pkgName = matcher.group(1);
        String specsStr = matcher.group(2) != null ? matcher.group(2) : matcher.group(3);
        return new Pair<>(pkgName, parseSpecs(specsStr != null ? specsStr : ""));
    }

    private final List<Pair<String, String>> parseSpecs(String specsString) {
        ArrayList<Pair<String, String>> arrayList = new ArrayList<>();
        if (specsString != null && !specsString.trim().isEmpty()) {
            String[] parts = specsString.split(",");
            for (String part : parts) {
                Matcher matcher = PATTERN_REQ_SPECS.matcher(part.trim());
                if (matcher.find()) {
                    arrayList.add(new Pair<>(matcher.group(1), matcher.group(2).trim()));
                }
            }
        }
        return arrayList;
    }

    private final List<String> parseDependenciesFromMetadata(File metadataFile) {
        ArrayList<String> arrayList = new ArrayList<>();
        if (metadataFile == null || !metadataFile.exists()) {
            return arrayList;
        }
        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(metadataFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Requires-Dist:")) {
                    String dep = line.substring(14).trim();
                    if (dep.contains(";")) {
                        String[] parts = dep.split(";", 2);
                        if (isMarkerCompatible(parts[1].trim())) {
                            arrayList.add(parts[0].trim());
                        }
                    } else {
                        arrayList.add(dep);
                    }
                }
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        return arrayList;
    }

    private final boolean isMarkerCompatible(String marker) {
        return new MarkerParser(marker).parse();
    }

    private final File findMetadataFile(String pkg, String version) {
        try {
            File libDir = getLibPath(pkg, version);
            File[] distInfos = libDir.listFiles((dir, name) -> name.endsWith(".dist-info"));
            if (distInfos != null && distInfos.length > 0) {
                return new File(distInfos[0], "METADATA");
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        return null;
    }

    /* JADX INFO: loaded from: classes4.dex */
    public static final class MarkerParser {
        private final String marker;

        public MarkerParser(String str) {
            this.marker = str;
        }

        public final boolean parse() {
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:33:0x0081  */
    /* JADX WARN: Code duplicated, block: B:40:0x00ec  */
    public final boolean checkVersionSatisfies(String version, List<Pair<String, String>> specs) {
        if (specs == null || specs.isEmpty()) {
            return true;
        }
        for (Pair<String, String> pair : specs) {
            String op = pair.getFirst();
            String target = pair.getSecond();
            int cmp = VersionComparator.INSTANCE.compare(version, target);
            if ("==".equals(op)) {
                if (cmp != 0) return false;
            } else if ("!=".equals(op)) {
                if (cmp == 0) return false;
            } else if ("<=".equals(op)) {
                if (cmp > 0) return false;
            } else if (">=".equals(op)) {
                if (cmp < 0) return false;
            } else if ("<".equals(op)) {
                if (cmp >= 0) return false;
            } else if (">".equals(op)) {
                if (cmp <= 0) return false;
            } else if ("~=".equals(op)) {
                if (cmp < 0) return false;
            }
        }
        return true;
    }

    private final boolean isWildcardVersionSpec(String spec) {
        return spec != null && spec.contains("*");
    }

    private final List<String> filterPreReleases(List<String> versions, List<Pair<String, String>> specs) {
        if (versions.isEmpty() || specsAllowPreRelease(specs)) {
            return versions;
        }
        ArrayList<String> arrayList = new ArrayList<>();
        for (String obj : versions) {
            if (!isPreReleaseVersion(obj)) {
                arrayList.add(obj);
            }
        }
        return arrayList.isEmpty() ? versions : arrayList;
    }

    private final boolean specsAllowPreRelease(List<Pair<String, String>> specs) {
        if (specs == null || specs.isEmpty()) {
            return false;
        }
        for (Pair<String, String> pair : specs) {
            if (isPreReleaseVersion(pair.getSecond())) {
                return true;
            }
        }
        return false;
    }

    private final boolean isPreReleaseVersion(String version) {
        List<String> parts = parseVersion(version).getParts();
        if ((parts instanceof Collection) && parts.isEmpty()) {
            return false;
        }
        for (String str : parts) {
            switch (str.hashCode()) {
                case 97:
                    if (str.equals(Deobfuscator$exteraGramDev$TMessagesProj.getString(-58598255511087L))) {
                        return true;
                    }
                    break;
                case 98:
                    if (str.equals(Deobfuscator$exteraGramDev$TMessagesProj.getString(-58611140412975L))) {
                        return true;
                    }
                    break;
                case 99:
                    if (str.equals(Deobfuscator$exteraGramDev$TMessagesProj.getString(-58551010870831L))) {
                        return true;
                    }
                    break;
                case 3633:
                    if (str.equals(Deobfuscator$exteraGramDev$TMessagesProj.getString(-58606845445679L))) {
                        return true;
                    }
                    break;
                case 99349:
                    if (str.equals(Deobfuscator$exteraGramDev$TMessagesProj.getString(-58559600805423L))) {
                        return true;
                    }
                    break;
                case 3020272:
                    if (str.equals(Deobfuscator$exteraGramDev$TMessagesProj.getString(-58671269955119L))) {
                        return true;
                    }
                    break;
                case 92909918:
                    if (str.equals(Deobfuscator$exteraGramDev$TMessagesProj.getString(-58576780674607L))) {
                        return true;
                    }
                    break;
            }
        }
        return false;
    }

    private final boolean matchesVersionWildcard(String version, String spec) {
        if (spec == null || version == null) return false;
        String cleanSpec = spec.replace("*", "").trim();
        return version.startsWith(cleanSpec);
    }

    private final List<Integer> parseVersionReleaseParts(String version) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        if (version != null) {
            String[] parts = version.split("[^0-9]+");
            for (String p : parts) {
                if (!p.isEmpty()) {
                    try {
                        arrayList.add(Integer.parseInt(p));
                    } catch (Exception ignored) {}
                }
            }
        }
        return arrayList;
    }

    /* JADX INFO: loaded from: classes4.dex */
    public static final class SizeLimitedInputStream extends InputStream {
        private final InputStream delegate;
        private final long maxBytes;
        private long total;

        public SizeLimitedInputStream(InputStream inputStream, long j) {
            this.delegate = inputStream;
            this.maxBytes = j;
        }

        private final int track(int read) throws IOException {
            if (read > 0) {
                long j = this.total + ((long) read);
                this.total = j;
                if (j > this.maxBytes) {
                    throw new IOException("Stream size limit exceeded: " + this.maxBytes);
                }
            }
            return read;
        }

        @Override // java.io.InputStream
        public int read() throws IOException {
            int i = this.delegate.read();
            if (i != -1) {
                track(1);
            }
            return i;
        }

        @Override // java.io.InputStream
        public int read(byte[] b2, int off, int len) throws IOException {
            return track(this.delegate.read(b2, off, len));
        }

        @Override // java.io.InputStream
        public int available() throws IOException {
            return this.delegate.available();
        }

        @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            this.delegate.close();
        }
    }

    private final String calculateSha256(File file) throws NoSuchAlgorithmException, IOException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            byte[] bArr = new byte[8192];
            int read;
            while ((read = fileInputStream.read(bArr)) != -1) {
                messageDigest.update(bArr, 0, read);
            }
            byte[] digest = messageDigest.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        }
    }

    private final ParsedVersion parseVersion(String version) {
        String lowerCase = version != null ? version.trim().toLowerCase(Locale.ROOT) : "";
        ArrayList<String> parts = new ArrayList<>();
        return new ParsedVersion(0, lowerCase, parts);
    }

    /* JADX INFO: renamed from: $r8$lambda$0MJ3GR4HlLQMQMAks-azHx-JXIE, reason: not valid java name */
    public static String m1324$r8$lambda$0MJ3GR4HlLQMQMAksazHxJXIE(MatchResult matchResult) {
        Deobfuscator$exteraGramDev$TMessagesProj.getString(-58911788123695L);
        return matchResult.getValue();
    }

    /* JADX INFO: loaded from: classes4.dex */
    public static final class VersionComparator implements Comparator<String> {
        public static final VersionComparator INSTANCE = new VersionComparator();

        private VersionComparator() {
        }

        @Override // java.util.Comparator
        public int compare(String v1, String v2) {
            Deobfuscator$exteraGramDev$TMessagesProj.getString(-70370760869423L);
            Deobfuscator$exteraGramDev$TMessagesProj.getString(-70392235705903L);
            PipController pipController = PipController.INSTANCE;
            ParsedVersion version = pipController.parseVersion(v1);
            ParsedVersion version2 = pipController.parseVersion(v2);
            int iCompare = Intrinsics.compare(version.getEpoch(), version2.getEpoch());
            if (iCompare != 0) {
                return iCompare;
            }
            List<String> parts = version.getParts();
            List<String> parts2 = version2.getParts();
            int iMax = Math.max(parts.size(), parts2.size());
            for (int i = 0; i < iMax; i++) {
                String string = (String) CollectionsKt.getOrNull(parts, i);
                String string2 = (String) CollectionsKt.getOrNull(parts2, i);
                if (string == null) {
                    if ((string2 != null ? StringsKt.toIntOrNull(string2) : null) != null) {
                        string = Deobfuscator$exteraGramDev$TMessagesProj.getString(-70396530673199L);
                    }
                }
                if (string2 == null) {
                    if ((string != null ? StringsKt.toIntOrNull(string) : null) != null) {
                        string2 = Deobfuscator$exteraGramDev$TMessagesProj.getString(-70473840084527L);
                    }
                }
                if (string == null) {
                    string = Deobfuscator$exteraGramDev$TMessagesProj.getString(-70482430019119L);
                }
                if (string2 == null) {
                    string2 = Deobfuscator$exteraGramDev$TMessagesProj.getString(-70495314921007L);
                }
                if (!Intrinsics.areEqual(string, string2)) {
                    Integer intOrNull = StringsKt.toIntOrNull(string);
                    Integer intOrNull2 = StringsKt.toIntOrNull(string2);
                    if (intOrNull != null && intOrNull2 != null) {
                        int iCompare2 = Intrinsics.compare(intOrNull.intValue(), intOrNull2.intValue());
                        if (iCompare2 != 0) {
                            return iCompare2;
                        }
                    } else {
                        if (intOrNull != null) {
                            return 1;
                        }
                        if (intOrNull2 != null) {
                            return -1;
                        }
                        int weight = getWeight(string);
                        int weight2 = getWeight(string2);
                        if (weight != weight2) {
                            return Intrinsics.compare(weight, weight2);
                        }
                        int iCompareTo = string.compareTo(string2);
                        if (iCompareTo != 0) {
                            return iCompareTo;
                        }
                    }
                }
            }
            return 0;
        }

        private final int getWeight(String s) {
            if (Intrinsics.areEqual(s, Deobfuscator$exteraGramDev$TMessagesProj.getString(-70491019953711L))) {
                return 110;
            }
            if (s.length() == 0) {
                return 100;
            }
            if (Intrinsics.areEqual(s, Deobfuscator$exteraGramDev$TMessagesProj.getString(-70452365248047L)) || Intrinsics.areEqual(s, Deobfuscator$exteraGramDev$TMessagesProj.getString(-70456660215343L))) {
                return 80;
            }
            if (Intrinsics.areEqual(s, Deobfuscator$exteraGramDev$TMessagesProj.getString(-70465250149935L)) || Intrinsics.areEqual(s, Deobfuscator$exteraGramDev$TMessagesProj.getString(-70564034397743L))) {
                return 70;
            }
            if (Intrinsics.areEqual(s, Deobfuscator$exteraGramDev$TMessagesProj.getString(-70572624332335L)) || Intrinsics.areEqual(s, Deobfuscator$exteraGramDev$TMessagesProj.getString(-70529674659375L))) {
                return 60;
            }
            return Intrinsics.areEqual(s, Deobfuscator$exteraGramDev$TMessagesProj.getString(-70538264593967L)) ? 50 : 0;
        }
    }
}
