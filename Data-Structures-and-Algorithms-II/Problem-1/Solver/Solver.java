package Solver;
import java.util.Arrays;

public class Solver {
    int nDreams, nNumbers;
    int[] dreams, numbers;


    /**
     * Constructor
     * Sorts the numbers array for binary search
     * 
     * @param nDreams - number of dreams
     * @param nNumbers - number of numbers
     * @param dreams - array of dreams
     * @param numbers - array of numbers
    */
    public Solver(int nDreams, int nNumbers, int[] dreams, int[] numbers){
        this.nDreams = nDreams;
        this.nNumbers = nNumbers;
        this.dreams = dreams;

        // Sort the numbers array for binary search (O(n log n))
        Arrays.sort(numbers);
        this.numbers = numbers;
    }


    /**
     * Calculates the minimum wasted capacity when packing all the dreams in the given order.
     * Uses dynamic programming to solve the problem.
     * 
     * @return minimum wasted capacity
    */
    public int solve(){

        // Create an array to store the minimum wasted capacity
        int w[] = new int[nDreams + 1];

        // Initialize the array with the maximum value for comparison
        Arrays.fill(w, Integer.MAX_VALUE);

        // Base case
        w[0] = 0;

        // Step through the dreams 
        for(int j = 1; j <= nDreams; j++){
            // Sum of the dreams being packed
            int sum = 0;

            // Step the current dream back to the first dream
            for(int k = j - 1; k >= 0; k--){

                sum += dreams[k];

                // If the sum exceeds the largest number, there is no need to continue
                if(sum > numbers[nNumbers - 1]){
                    break;
                }

                // Find the index of the number that is closest to the sum (O(log n))
                int idx = Arrays.binarySearch(numbers, sum);
                // If the value is not found, get the index of the number that is larger than the sum
                idx = idx >= 0 ? idx : -idx - 1;

                    // Calculate the minimum wasted capacity
                    if (idx < nNumbers) {
                        int min = w[k] + numbers[idx] - sum;
                        if(min < w[j]){
                            w[j] = min;
                        }
                    }
            }
        }
        return w[nDreams];
    }
}
