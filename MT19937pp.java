import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class MT19937pp {

    private int seed;

    public MT19937pp(int seed) {
        this.seed = seed;
    }

    private double[] generate(int n) throws InterruptedException {
        double[] ans = new double[n];
        
        int numThreads = 10;
        ExecutorService service = Executors.newFixedThreadPool(numThreads);
        AtomicInteger idx = new AtomicInteger();
        for (int i = 0; i < numThreads; i++) {
            int numGen = n / numThreads + (i == 0 ? n % numThreads : 0);
            service.execute(() -> {
                int threadId = (int) Thread.currentThread().getId();
                MT19937 rand = new MT19937(seed ^ threadId);
                double[] nums = rand.generate(numGen);
                int localIdx = idx.getAndAdd(numGen);
                for (int l = 0; l < numGen; l++) {
                    ans[localIdx + l] = nums[l];
                }
            });
        }
        service.shutdown();
        service.awaitTermination(5, TimeUnit.SECONDS);
        return ans;
    }

    private class MT19937 {
        private final int n = 624;
        private final int f = 1812433253;
        private final int w = 32;
        private int[] state = new int[n];
        private int cnt;
    
        public MT19937(int seed) {
            state[0] = seed;
            this.cnt = 0;
            for (int i = 1; i < n; i++) {
                state[i] = f * (state[i - 1] ^ (state[i - 1] >> (w - 2))) + i;
            }
            twist();
        }
    
        private int h(int x) {
            if ((x & 1) == 0) {
                return x >> 1;
            }
            return (x >> 1) ^ (0x9908B0DF);
        }
    
        private void twist() {
            for (int i = 0; i < n; i++) {
                int lowerMask = (1 << (w - 1)) - 1;
                int upperMask = (1 << (w - 1));
    
                int temp = (upperMask & state[i]) | (lowerMask & state[(i + 1) % n]);
                state[i] = state[(i + 397) % n] ^ h(temp);
            }
        }
    
        private int temper() {
            if (cnt == n) {
                twist();
            }
            int y = state[(cnt + n) % n] ^ (state[(cnt + n) % n] >> 11);
            cnt++;
            y = y ^ ((y << 7) & 0x9D2C5680);
            y = y ^ ((y << 15) & 0xEFC60000);
            return y ^ (y >> 18);
        }
    
        public double[] generate(int k) {
            double[] ans = new double[k];
    
            while(k-- > 0) {
                int xn = temper();
                double un = 0;
                for (int i = 0; i < 32; i++) {
                    int z = xn & 1;
                    xn >>>= 1;
                    un += z * (1.0 / (1 << (i + 1)));
                    // un += z * Math.pow(2, -i - 1);
                }
                ans[ans.length - k - 1] = un;
            }
            return ans;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Scanner sc = new Scanner(System.in);
        // Prompt user
        int seed = sc.nextInt();
        int n = sc.nextInt();
        MT19937pp prng = new MT19937pp(seed);
        long start = System.currentTimeMillis();
        double[] nums = prng.generate(n);
        long end = System.currentTimeMillis();
        System.out.println(Arrays.toString(nums));
        System.out.println(n  + " PRNs generated in " + (end - start) + "ms.");
        sc.close();
    }
}
