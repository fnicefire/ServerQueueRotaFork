package nl.jouwapplicatie.serverQueue.checkers;

public class VersionUtil {
    public static boolean isAllowed(String playerVersion, String versionMode) {
        switch (versionMode.toLowerCase()) {
            case "old-mode":
                return isVersionBetween(playerVersion, "1.8.0", "1.12.2");
            case "new-mode":
                return isVersionBetween(playerVersion, "1.13.0", "1.21.10");
            case "all-mode":
            default:
                return isVersionBetween(playerVersion, "1.8.0", "1.21.10");
        }
    }

    private static boolean isVersionBetween(String version, String minVersion, String maxVersion) {
        return compareVersion(version, minVersion) >= 0 && compareVersion(version, maxVersion) <= 0;
    }

    public static int compareVersion(String v1, String v2) {
        String[] v1Parts = v1.split("\\.");
        String[] v2Parts = v2.split("\\.");

        int length = Math.max(v1Parts.length, v2Parts.length);
        for (int i = 0; i < length; i++) {
            int part1 = i < v1Parts.length ? Integer.parseInt(v1Parts[i]) : 0;
            int part2 = i < v2Parts.length ? Integer.parseInt(v2Parts[i]) : 0;
            if (part1 != part2) return part1 - part2;
        }
        return 0;
    }
}
