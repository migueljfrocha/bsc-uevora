import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import Graph.Graph;


public class Main {
    public static void main(String[] args) throws IOException{
        try (BufferedReader bf = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            int nHabitants = Integer.parseInt(bf.readLine());

            Graph graph = new Graph(nHabitants);

            for (int i = 1; i < nHabitants; i++) {
                line = bf.readLine();
                String[] parts = line.split(" ");
                int a = Integer.parseInt(parts[0]);
                int b = Integer.parseInt(parts[1]);
                // Add the edge to the graph
                graph.addEdge(a, b);

            }

            //Solve the problem
            System.out.println(graph.getDiameter());

        }
    }
    
}