import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.List;

public class GitExecutor {
//    public static void main(String[] args) {
//        List<String> comando = GitCommands.revList("/home/jp/Área de trabalho/Códigos/Java/sync-repo");
//
//        ProcessBuilder pb = new ProcessBuilder(comando);
//
//        pb.redirectErrorStream(true);
//
//        try {
//            System.out.println("Iniciando processo...");
//            Process p = pb.start();
//
//            try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
//                String linha;
//                while ((linha = reader.readLine()) != null) {
//                    System.out.println("LOG DO PROCESSO: " + linha);
//                }
//            }
//
//            int numeroSaida = p.waitFor();
//            System.out.println("Processo finalizado");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//    }

    public static void cmdFetch(String path) {
        List<String> comando = GitCommands.fetch(path);

        ProcessBuilder pb = new ProcessBuilder(comando);
        pb.redirectOutput(ProcessBuilder.Redirect.DISCARD);
        pb.redirectError(ProcessBuilder.Redirect.DISCARD);

        try {
            System.out.println("Iniciando processo...");
            Process p = pb.start();

            p.waitFor();
            System.out.println("Processo finalizado");

        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public static List<String> cmdStatus(String path) {
        List<String> comando = GitCommands.status(path);
        List<String> response = new ArrayList<>();

        ProcessBuilder pb = new ProcessBuilder(comando);
        pb.redirectErrorStream(true);

        try {
            System.out.println("Iniciando processo...");

            Process p = pb.start();

            try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                String linha;
                while ((linha = br.readLine()) != null) {
                    response.add(linha);
                }
            }

            p.waitFor();
            System.out.println("Processo finalizado");

        } catch (IOException | InterruptedException ex ) {
            ex.printStackTrace();
        }
        return response;
    }


}
