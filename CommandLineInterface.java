import java.io.IOException;
import java.util.List;

public class CommandLineInterface {
    private static GitRepositoriesFile file;

    private static void setup() throws IOException {
        file = GitRepositoriesFile.builder()
                .path("/home/jp/.git_repos.txt")
                .build();
    }
    public static void start(String[] args) throws IOException {
        setup();

        if (args.length == 0) {
            cmdHelp();
            return;
        }

        for (int i = 0; i < args.length; i++) {
            String argAtual = args[i];

            SubComandos comando = SubComandos.fromString(args[i]);

            if (comando != null) {
                switch (comando) {
                    case ADD_PATH -> {
                        if (i + 1 < args.length) {
                            String valorPath = args[i + 1];

                            executarAcaoComando(comando, valorPath, 0);

                            i++;
                        } else {
                            System.err.println("Erro: O comando " + argAtual + " exige um valor/path logo após.");
                            cmdHelp();
                        }
                    }
                    case REMOVE_PATH -> {
                        if (i + 1 < args.length) {
                            int index = (args[i + 1].matches("\\d+")) ? Integer.parseInt(args[i + 1]) : -1;

                            executarAcaoComando(comando, null, index);
                        }
                    }
                    case LIST_PATH -> {
                        if (i + 1 < args.length) {
                            boolean isVerbose = (SubComandos.fromString(args[i + 1]).equals(SubComandos.VERBOSE));

                            listarPaths(isVerbose);
                        } else listarPaths(false);
                    }
                    case CHECK, UPLOAD, DOWNLOAD, HELP -> System.out.println("Executando comando simples: " + comando);

                }
            } else {
                System.out.println("Argumento ou opção desconhecida: " + argAtual);
            }
        }
    }

    private static void cmdHelp() {
        System.out.println("Usage: java Main [options]");
        System.out.println("Usage: java Main [-a|--add-path | -r|--remove-path] 'path'");
        System.out.println("Usage: java Main [-l|--list-path] [-v|--verbose]");
        System.out.println();
        System.out.println("Options:");
        System.out.println(
                """
                  -c | --check        Checa se os repositórios locais estão sincronizados com os repositórios remotos.
                  -u | --upload       Atualiza os repositórios remotos com os arquivos locais.
                  -d | --download     Baixa os arquivos do repositório remoto para os arquivos locais.
                  -a | --add-path     Adiciona os caminhos dos repositórios locais para as operações.
                  -r | --remove-path  Remove os caminhos dos repositórios locais.
                  -l | --list-path    Lista os caminhos específicados no arquivo de apoio.
                  -v | --verbose      Exibe de forma verbosa a lista de caminhos do arquivo de apoio.
                  -h | --help         Mostra essa tela com as informações de ajuda.
                """
        );
    }

    public static void alertarArquivoVazio(String path) {
        System.out.printf("O arquivo %s está vazio, adicione algum caminho ao arquivo manualmente ou use o sub-comando '%s'|'%s'.%n",
                path,
                SubComandos.ADD_PATH.getFlagCurta(),
                SubComandos.ADD_PATH.getFlagLonga()
        );
    }

    public static void executarAcaoComando(SubComandos comando, String valorPath, int index) throws IOException {
        switch (comando) {
            case ADD_PATH -> file.savePath(valorPath);
            case REMOVE_PATH -> file.removePath(index);
        }
    }

    public static void listarPaths(boolean isVerbose) throws IOException {
        List<String> paths = file.listPaths();

        if (paths.isEmpty()) {
            alertarArquivoVazio(file.toString());
            return;
        }

        for (int i = 0; i < paths.size(); i++){
            if (isVerbose) System.out.printf("id: %s - caminho: %s\n", i, paths.get(i));
            else System.out.printf("%s - %s\n", i, paths.get(i));
        }

    }
}
