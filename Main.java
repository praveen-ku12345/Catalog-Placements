import org.json.JSONObject;
import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        try {
            
            String jsonContent = new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get("input.json")));
            JSONObject data = new JSONObject(jsonContent);
            List<Point> points = decodePoints(data);
            JSONObject keys = data.getJSONObject("keys");
            int k = keys.getInt("k");
            double constant = findConstant(points, k);
            System.out.println("Constant term (c): " + constant);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<Point> decodePoints(JSONObject data) {
        List<Point> points = new ArrayList<>();
        for (String key : data.keySet()) {
            if (key.equals("keys")) continue;

            try {
                int x = Integer.parseInt(key);  // x is the key as integer
                JSONObject valueData = data.getJSONObject(key);
                int base = Integer.parseInt(valueData.getString("base"));
                long y = convertBaseToLong(valueData.getString("value"), base);

                points.add(new Point(x, y));
            } catch (Exception e) {
                System.err.println("Error decoding point with key: " + key);
                e.printStackTrace();
            }
        }
        return points;
    }

    private static long convertBaseToLong(String valueStr, int base) {
        try {
            // Convert the value string based on the given base
            return Long.parseLong(valueStr, base);
        } catch (NumberFormatException e) {
            System.err.println("Error converting value '" + valueStr + "' with base " + base);
            return 0;
        }
    }

    private static double findConstant(List<Point> points, int k) {
        if (points.size() < k) throw new IllegalArgumentException("Not enough points");

        double constant = 0.0;
        for (int i = 0; i < k; i++) {
            double term = points.get(i).y;
            for (int j = 0; j < k; j++) {
                if (i != j) {
                    term *= (0.0 - points.get(j).x) / (points.get(i).x - points.get(j).x);
                }
            }
            constant += term;
        }
        return constant;
    }

    static class Point {
        int x;
        long y;

        Point(int x, long y) {
            this.x = x;
            this.y = y;
        }
    }
}
