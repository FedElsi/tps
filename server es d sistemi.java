
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class CalcolatriceServer {

    private static final int PORTA = 8844;

    public static void main(String[] args) {
        int operazioniEseguite = 0;   // contatore operazioni con RISULTATO
        boolean running = true;       // per chiudere il server con QUIT

        try (ServerSocket serverSocket = new ServerSocket(PORTA)) {
            System.out.println("Server avviato sulla porta " + PORTA);

            // Ciclo principale: accetta i client finché non riceve QUIT
            while (running) {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(
                             new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter out = new PrintWriter(
                             clientSocket.getOutputStream(), true)) {

                    System.out.println("Client connesso: " + clientSocket.getInetAddress());

                    String richiesta;
                    // Ciclo di comunicazione con il singolo client
                    while ((richiesta = in.readLine()) != null) {
                        richiesta = richiesta.trim();

                        // Comando di chiusura server
                        if (richiesta.equalsIgnoreCase("QUIT")) {
                            out.println("CHIUSURA: Operazioni eseguite: "
                                    + operazioniEseguite + ". Arrivederci!");
                            running = false;   // esce dal ciclo esterno
                            break;
                        }

                        String risposta = elaboraRichiesta(richiesta);

                        // Conta solo le operazioni che producono un risultato
                        if (risposta.startsWith("RISULTATO:")) {
                            operazioniEseguite++;
                        }

                        out.println(risposta);
                    }

                    System.out.println("Client disconnesso.");

                } catch (IOException e) {
                    System.err.println("Errore durante la comunicazione con il client: " + e.getMessage());
                }
            }

            System.out.println("Server in chiusura...");

        } catch (IOException e) {
            System.err.println("Impossibile avviare il server: " + e.getMessage());
        }
    }

    /**
     * Elabora una stringa del tipo "NUMERO1 OPERAZIONE NUMERO2"
     * Restituisce "RISULTATO: x" oppure "ERRORE: ..."
     */
    private static String elaboraRichiesta(String richiesta) {
        String[] parti = richiesta.trim().split("\\s+");
        if (parti.length != 3) {
            return "ERRORE: Formato non valido. Usa: NUMERO OPERAZIONE NUMERO";
        }

        double num1;
        double num2;
        String operazione = parti[1];

        try {
            num1 = Double.parseDouble(parti[0]);
            num2 = Double.parseDouble(parti[2]);
        } catch (NumberFormatException e) {
            return "ERRORE: Formato non valido. Usa: NUMERO OPERAZIONE NUMERO";
        }

        double risultato;

        switch (operazione) {
            case "+":
                risultato = num1 + num2;
                break;
            case "-":
                risultato = num1 - num2;
                break;
            case "*":
                risultato = num1 * num2;
                break;
            case "/":
                if (num2 == 0) {
                    return "ERRORE: Divisione per zero non consentita";
                }
                risultato = num1 / num2;
                break;
            default:
                return "ERRORE: Operazione non supportata. Usa: + - * /";
        }

        return "RISULTATO: " + risultato;
    }
}
