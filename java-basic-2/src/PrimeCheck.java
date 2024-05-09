import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrimeCheck {
    public static void main(String[] args) {
        // List to store primes
        List<Integer> primes = new ArrayList<>();

        // Find primes between 3 and 100,000
        for (int i = 3; i <= 100000; i++) {
            if (isPrime(i)) {
                primes.add(i);
            }
        }

        // Classify primes based on last digit
        Map<Integer, List<Integer>> primeGroups = new HashMap<>();
        for (int prime : primes) {
            int lastDigit = prime % 10;
            primeGroups.computeIfAbsent(lastDigit, k -> new ArrayList<>()).add(prime);
        }

        // Output prime groups
        System.out.println("Prime Groups:");
        for (Map.Entry<Integer, List<Integer>> entry : primeGroups.entrySet()) {
            System.out.println("Last Digit " + entry.getKey() + ": " + entry.getValue());
        }

        // Count consecutive last digit pairs
        Map<String, Integer> pairsCount = new HashMap<>();
        for (int i = 0; i < primes.size() - 1; i++) {
            int currentPrime = primes.get(i);
            int nextPrime = primes.get(i + 1);
            int currentLastDigit = currentPrime % 10;
            int nextLastDigit = nextPrime % 10;
            if (currentLastDigit != 5) {
                String pair = currentLastDigit + "-" + nextLastDigit;
                pairsCount.put(pair, pairsCount.getOrDefault(pair, 0) + 1);
            }
        }

        // Sort and output pairs by count
        List<Map.Entry<String, Integer>> sortedPairs = new ArrayList<>(pairsCount.entrySet());
        sortedPairs.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        System.out.println("\nConsecutive Last Digit Pairs Ranking:");
        for (Map.Entry<String, Integer> entry : sortedPairs) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " occurrences");
        }
    }

    // Function to check if a number is prime
    private static boolean isPrime(int n) {
        if (n <= 1) return false;
        if (n <= 3) return true;
        if (n % 2 == 0 || n % 3 == 0) return false;
        for (int i = 5; i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0) return false;
        }
        return true;
    }
}
