import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class CurrencyConverter {

    static final String API_URL = "https://api.frankfurter.app/latest";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("========================================");
        System.out.println("        REAL-TIME CURRENCY CONVERTER    ");
        System.out.println("========================================");

        System.out.print("Enter base currency (e.g., USD): ");
        String baseCurrency = scanner.nextLine().trim().toUpperCase();

        System.out.print("Enter target currency (e.g., INR): ");
        String targetCurrency = scanner.nextLine().trim().toUpperCase();

        System.out.print("Enter amount to convert: ");
        double amount = scanner.nextDouble();

        System.out.println("\nFetching real-time exchange rates...");

        try {
            double rate = fetchExchangeRate(baseCurrency, targetCurrency);

            if (rate == -1) {
                System.out.println("Error: Invalid currency code or unsupported pair.");
                return;
            }

            double convertedAmount = amount * rate;

            System.out.println("----------------------------------------");
            System.out.printf("  %,.2f %s  =  %,.2f %s%n",
                    amount, baseCurrency, convertedAmount, targetCurrency);
            System.out.printf("  Exchange Rate: 1 %s = %.4f %s%n",
                    baseCurrency, rate, targetCurrency);
            System.out.println("  Source: Frankfurter API (ECB Rates)");
            System.out.println("----------------------------------------");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        scanner.close();
    }

    public static double fetchExchangeRate(String base, String target) throws Exception {
        String urlString = API_URL + "?from=" + base + "&to=" + target;
        URL url = new URL(urlString);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) return -1;

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        // Manual JSON parse: "rates":{"INR":83.5678}
        String json = response.toString();
        int ratesIndex = json.indexOf("\"rates\"");
        if (ratesIndex == -1) return -1;

        int targetIndex = json.indexOf("\"" + target + "\"", ratesIndex);
        if (targetIndex == -1) return -1;

        int colonIndex = json.indexOf(":", targetIndex);
        int commaOrBrace = json.indexOf("}", colonIndex);
        int comma = json.indexOf(",", colonIndex);
        int end = (comma != -1 && comma < commaOrBrace) ? comma : commaOrBrace;

        String rateStr = json.substring(colonIndex + 1, end).trim();
        return Double.parseDouble(rateStr);
    }
}
