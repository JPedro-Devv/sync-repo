import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import java.util.List;
import java.util.ArrayList;

public class GitWrapper {
    public static void main(String[] args) {
        List<String> comando = GitCommands.cmdFetch("/data/data/com.termux/files/home/codes/java/sync-repo");

        ProcessBuilder pb = new ProcessBuilder(comando);

        pb.redirectErrorStream(true);

        try {
            System.out.println("Iniciando processo...");
            Process p = pb.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                String linha;
                while ((linha = reader.readLine()) != null) {
                    System.out.println("LOG DO PROCESSO: " + linha);
                }
            }

            int numeroSaida = p.waitFor();
            System.out.println("Processo finalizado");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
