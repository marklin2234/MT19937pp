# ThreadedMT19937

To use:

1. Download the latest release.
2. Run the jar file from the terminal using
```
java -jar [YOUR PATH]/PRNG.jar
```
3. Follow the CLI to generate your PRNs.

OR

1. Run
```
./rand.sh [seed] [n]
```
2. PRNs will be generated in nums.out in your current directory.

# Implementation
Implemented using the Mersenne Twister algorithm using Java's concurrency library.