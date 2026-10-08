import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

import Solver.Solver;
public class Main {
    public static void main(String[] args) throws IOException {
        int nDreams, nNumbers;
        int[] dreams, numbers;

        // Read the input according to the specified format
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String[] line = reader.readLine().split(" ");

            // First line contains the number of numbers and dreams
            nNumbers = Integer.parseInt(line[0]);
            nDreams = Integer.parseInt(line[1]);

            numbers = new int[nNumbers];
            dreams = new int[nDreams];

            // Second line contains the numbers
            line = reader.readLine().split(" ");
            for (int i = 0; i < nNumbers; i++) {
                numbers[i] = Integer.parseInt(line[i]);
            }

            // Following lines contain the dream sizes
            for (int i = 0; i < nDreams; i++) {
                dreams[i] = Integer.parseInt(reader.readLine());
            }
        }

        // Create a new Solver object and solve the problem
        Solver problem = new Solver(nDreams, nNumbers, dreams, numbers);

        // Print the result
        System.out.println(problem.solve());
    }
}
