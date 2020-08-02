package gg.codie.mineonline;

import gg.codie.mineonline.api.MinecraftAPI;
import gg.codie.utils.JSONUtils;
import gg.codie.utils.MD5Checksum;
import jdk.nashorn.api.scripting.URLReader;
import org.json.JSONArray;
import org.json.JSONObject;
import org.lwjgl.Sys;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public class MinecraftVersionInfo {
    private static JSONArray versionsJson;
    private static LinkedList<MinecraftVersion> versions = new LinkedList();

    static {
        loadVersions();
    }

    public static MinecraftVersion getVersionByMD5(String md5) {
        List<MinecraftVersion> matches = versions.stream().filter(version -> version.md5.equals(md5)).collect(Collectors.toList());
        if(matches.size() < 1) {
            System.out.println("Unrecognized jar " + md5);
            return null;
        }
        return matches.get(0);
    }

    public static class MinecraftVersion {
        public final String sha256;
        public final String name;
        public final String md5;
        public final String type;
        public final boolean baseURLHasNoPort;
        public final boolean enableScreenshotPatch;
        public final String baseVersion;
        public final boolean hasHeartbeat;
        public final boolean enableFullscreenPatch;
        public final String info;
        public final String[] clientVersions;
        public final boolean forceFullscreenMacos;
        public final boolean enableMacosCursorPatch;
        public final boolean legacy;

        private MinecraftVersion(
                String sha256,
                String name,
                String baseVersion,
                String md5,
                String type,
                boolean baseURLHasNoPort,
                boolean enableScreenshotPatch,
                boolean hasHeartbeat,
                boolean enableFullscreenPatch,
                String info,
                String[] clientVersions,
                boolean forceFullscreenMacos,
                boolean enableMacosCursorPatch,
                boolean legacy
        ) {
            this.sha256 = sha256;
            this.name = name;
            this.md5 = md5;
            this.type = type;
            this.baseURLHasNoPort = baseURLHasNoPort;
            this.enableScreenshotPatch = enableScreenshotPatch;
            this.baseVersion = baseVersion;
            this.hasHeartbeat = hasHeartbeat;
            this.enableFullscreenPatch = enableFullscreenPatch;
            this.info = info;
            this.clientVersions = clientVersions;
            this.forceFullscreenMacos = forceFullscreenMacos;
            this.enableMacosCursorPatch = enableMacosCursorPatch;
            this.legacy = legacy;
        }
    }

    private static void fetchVersions() {
        try {
            JSONArray versions = MinecraftAPI.getVersionsInfo();

            //Write JSON file
            try (FileWriter file = new FileWriter(LauncherFiles.CACHED_VERSION_INFO_PATH, false)) {
                file.write(versions.toString());
                file.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void loadVersions() {
        fetchVersions();

        URL path = LauncherFiles.VERSION_INFO_PATH;

        try {
            if (new File(LauncherFiles.CACHED_VERSION_INFO_PATH).exists())
                path = Paths.get(LauncherFiles.CACHED_VERSION_INFO_PATH).toUri().toURL();
        } catch (Exception ex) {

        }

        try (URLReader input = new URLReader(path)) {
            // load a settings file
            char[] buffer = new char[8096];
            int bytes_read = 0;
            StringBuffer stringBuffer = new StringBuffer();
            while ((bytes_read = input.read(buffer, 0, 8096)) != -1) {
                for(int i = 0; i < bytes_read; i++) {
                    stringBuffer.append(buffer[i]);
                }
            }

            versionsJson = new JSONArray(stringBuffer.toString());

            Iterator versionIterator = versionsJson.iterator();

            while (versionIterator.hasNext()) {
                JSONObject object = (JSONObject)versionIterator.next();
                versions.add(fromJSONObject(object));
            }
        } catch (IOException ex) {
            System.err.println("Failed to load Minecraft Version information!");
            versions = new LinkedList<>();
        }

        if (new File(LauncherFiles.CUSTOM_VERSION_INFO_PATH).exists()) {
            try (FileReader input = new FileReader(LauncherFiles.CUSTOM_VERSION_INFO_PATH)) {
                // load a settings file
                char[] buffer = new char[8096];
                int bytes_read = 0;
                StringBuffer stringBuffer = new StringBuffer();
                while ((bytes_read = input.read(buffer, 0, 8096)) != -1) {
                    for(int i = 0; i < bytes_read; i++) {
                        stringBuffer.append(buffer[i]);
                    }
                }

                versionsJson = new JSONArray(stringBuffer.toString());

                Iterator versionIterator = versionsJson.iterator();

                while (versionIterator.hasNext()) {
                    JSONObject object = (JSONObject)versionIterator.next();
                    versions.add(fromJSONObject(object));
                }
            } catch (IOException ex) {
                System.err.println("Failed to load custom Minecraft Version information!");
            }
        }
    }

    public static String getAppletClass(String path) throws IOException {
        JarFile jarFile = new JarFile(path);
        Enumeration allEntries = jarFile.entries();
        while (allEntries.hasMoreElements()) {
            JarEntry entry = (JarEntry) allEntries.nextElement();
            String classCanonicalName = entry.getName();

            if(!classCanonicalName.contains(".class"))
                continue;

            classCanonicalName = classCanonicalName.replace("/", ".");
            classCanonicalName = classCanonicalName.replace(".class", "");

            String className = classCanonicalName;
            if(classCanonicalName.lastIndexOf(".") > -1) {
                className = classCanonicalName.substring(classCanonicalName.lastIndexOf(".") + 1);
            }

            if(className.equals("MinecraftApplet")) {
                return classCanonicalName;
            }
        }
        return null;
    }

    public static boolean isRunnableJar(String path) throws IOException {
        JarFile jarFile = new JarFile(path);
        Enumeration allEntries = jarFile.entries();
        while (allEntries.hasMoreElements()) {
            JarEntry entry = (JarEntry) allEntries.nextElement();
            String classCanonicalName = entry.getName();

            if(!classCanonicalName.contains(".class"))
                continue;

            classCanonicalName = classCanonicalName.replace("/", ".");
            classCanonicalName = classCanonicalName.replace(".class", "");

            String className = classCanonicalName;
            if(classCanonicalName.lastIndexOf(".") > -1) {
                className = classCanonicalName.substring(classCanonicalName.lastIndexOf(".") + 1);
            }

            if (className.equals("MinecraftApplet")) {
                return true;
            } else if (className.equals("Minecraft")) {
                return true;
            }
//            else if (className.equals("MinecraftLauncher")) {
//                return true;
//            }
            else if (className.equals("LauncherFrame")) {
                return true;
            }
            else if (className.equals("RubyDung")) {
                return true;
            }
            else if (className.equals("Main")) {
                return true;
            }
        }
        
        try {
            System.out.println("Incompatible Jar MD5: " + MD5Checksum.getMD5Checksum(path));
        } catch (Exception ex) {

        }

        return false;
    }

    private static MinecraftVersion fromJSONObject(JSONObject object) {
        return new MinecraftVersion(
                (object.has("sha256") ? object.getString("sha256") : null),
                object.getString("name"),
                object.getString("baseVersion"),
                object.getString("md5"),
                object.getString("type"),
                (object.has("baseURLHasNoPort") && object.getBoolean("baseURLHasNoPort")),
                (object.has("enableScreenshotPatch") && object.getBoolean("enableScreenshotPatch")),
                (object.has("hasHeartbeat") && object.getBoolean("hasHeartbeat")),
                (object.has("enableFullscreenPatch") && object.getBoolean("enableFullscreenPatch")),
                (object.has("info") ? object.getString("info") : null),
                (object.has("clientVersions") ? JSONUtils.getStringArray(object.getJSONArray("clientVersions")) : new String[0]),
                (object.has("forceFullscreenMacos") && object.getBoolean("forceFullscreenMacos")),
                (object.has("enableMacosCursorPatch") && object.getBoolean("enableMacosCursorPatch")),
                (object.has("legacy") && object.getBoolean("legacy"))
        );
    }

    public static MinecraftVersion getVersion(String path) {
        try {
            String md5 = MD5Checksum.getMD5Checksum(path);
            return getVersionByMD5(md5);
        } catch (Exception e) {
            return null;
        }
    }

    public static void launchMinecraft(String jarPath, String serverIP, String serverPort, String mpPass) throws Exception {
        MinecraftVersion minecraftVersion = getVersion(jarPath);

        if(minecraftVersion != null) {
            if (minecraftVersion.legacy) {
                new LegacyMinecraftLauncher(jarPath, serverIP, serverPort, mpPass).startMinecraft();
            } else {
                MinecraftLauncher.startProcess(jarPath, serverIP, serverPort);
            }
        } else {
            // TODO:
            // - Look for the main class and guess if it's legacy.
        }
    }
}
