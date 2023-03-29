package common;

public class StringUtil {
    public static String extractFilename(String path) {
        String[] parts = null;
        String result = "";

        if (path.contains("/")) {
            parts = path.split("/");
        } else if (path.contains("\\")) {
            parts = path.split("\\\\");
        } else {
            return removeExtension(path);
        }

        result = parts[parts.length - 1];

        if (result.contains(".go")) {
            result = result.substring(0, result.length() - 3);
        }

        return result;
    }

    public static String removeExtension(String filename) {
        int lastDot = filename.lastIndexOf(".");
        return filename.substring(0, lastDot);
    }

    public static String repeat(String s, int n) {
        String result = "";
        
        for (int i = 0; i < n; i++)
            result += s;

        return result;
    }
}
