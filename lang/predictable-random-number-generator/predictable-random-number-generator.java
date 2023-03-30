// Import of the Random library when in a critical security context
import java.util.Random;
import java.security.SecureRandom;

public class Main {
    public static void main() {
        // ruleid: predictable-random-number-generator
        Random random = new Random().nextInt(10000, 99999);
        // ok: predictable-random-number-generator
        Random random = new SecureRandom().nextInt();
    }
}