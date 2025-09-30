package utils;

import java.util.concurrent.ThreadLocalRandom;

public class AccountNumberGenerator {
    public static String next() {
        long n = ThreadLocalRandom.current().nextLong(1_000_000_000L, 10_000_000_000L);
        return String.valueOf(n);
    }
}