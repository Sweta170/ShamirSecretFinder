import java.io.*;
import java.math.BigInteger;
import java.nio.file.*;
import java.util.*;
import org.json.*;

public class SecretFinder {

    public static void main(String[] args) throws IOException {
        // Load both testcases
        List<String> testCaseFiles = Arrays.asList("input1.json", "input2.json");

        for (String file : testCaseFiles) {
            JSONObject json = new JSONObject(Files.readString(Paths.get(file)));
            JSONObject keys = json.getJSONObject("keys");
            int n = keys.getInt("n");
            int k = keys.getInt("k");

            // Store (x, y) pairs
            List<BigInteger> xList = new ArrayList<>();
            List<BigInteger> yList = new ArrayList<>();

            for (String key : json.keySet()) {
                if (key.equals("keys")) continue;
                int x = Integer.parseInt(key);
                JSONObject val = json.getJSONObject(key);
                int base = Integer.parseInt(val.getString("base"));
                BigInteger y = new BigInteger(val.getString("value"), base);

                xList.add(BigInteger.valueOf(x));
                yList.add(y);
            }

            // Pick only first k entries for interpolation
            BigInteger secret = lagrangeInterpolation(xList.subList(0, k), yList.subList(0, k), BigInteger.ZERO);
            System.out.println("Secret (c) for file " + file + ": " + secret);
        }
    }

    
    private static BigInteger lagrangeInterpolation(List<BigInteger> x, List<BigInteger> y, BigInteger xVal) {
        BigInteger result = BigInteger.ZERO;
        int k = x.size();

        for (int i = 0; i < k; i++) {
            BigInteger term = y.get(i);
            for (int j = 0; j < k; j++) {
                if (i != j) {
                    BigInteger numerator = xVal.subtract(x.get(j));
                    BigInteger denominator = x.get(i).subtract(x.get(j));
                    term = term.multiply(numerator).divide(denominator);
                }
            }
            result = result.add(term);
        }

        return result;
    }
}
