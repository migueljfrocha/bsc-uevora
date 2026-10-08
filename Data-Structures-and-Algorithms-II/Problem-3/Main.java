import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import FlowNetwork.FlowNetwork;

public class Main {
    public static void main(String[] args) throws IOException {

        int nParticipants, nDeclarations;
        FlowNetwork cardExchangeNetwork;

        try(BufferedReader bf = new BufferedReader(new InputStreamReader(System.in))){
            String[] line = bf.readLine().split(" ");
            nParticipants = Integer.parseInt(line[0]);
            nDeclarations = Integer.parseInt(line[1]);

            int nNodes = nParticipants * 2 + 2; // nParticipants * 2 + source + sink
            int source = nNodes - 2;
            int sink = nNodes - 1;
            cardExchangeNetwork = new FlowNetwork(nNodes, source, sink);

            // Build the source and sink connections
            for (int i = 0; i < nParticipants; i++) {
                cardExchangeNetwork.addEdge(source, i, 1);
                cardExchangeNetwork.addEdge(i + nParticipants, sink, 1);
            }
            // Build the edges between participants
            for (int i = 0; i < nDeclarations; i++) {
                line = bf.readLine().split(" ");
                int P1 = Integer.parseInt(line[0]);
                int P2 = Integer.parseInt(line[1]);
                cardExchangeNetwork.addEdge(P1, P2 + nParticipants, 1);
            }
        }

        int maxFlowValue = cardExchangeNetwork.edmondsKarp();

        if(maxFlowValue == nParticipants){
            System.out.println("YES");
        }else{
            System.out.println("NO");
        }
    }
}


