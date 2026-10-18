import java.io.IOException;
import java.util.List;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CommandLineInterface {
    private static GitRepositoriesFile file;
    private static GitService gitService;

    private static void setup() throws IOException {
        String homePath = System.getProperty("user.home");
        Path completePath = Paths.get(homePath, ".git_repos");
        file = GitRepositoriesFile.builder()
                .path(completePath)
                .build();
        gitService = new GitService(new GitExecutor());
    }
    public static void start(String[] args) throws IOException {

        if (args.length == 0) {
            cmdHelp();
            return;
        }

        setup();

        for (int i = 0; i < args.length; i++) {
            String argAtual = args[i];
            String proximoArg = ((i + 1) < args.length) ? args[i + 1] : "";

            SubComandos comando = SubComandos.fromString(args[i]);

            if (comando == null) {
                System.err.println("Argumento ou opção desconhecida: " + argAtual);
                break;
            }
          
            switch (comando) {
                case ADD_PATH -> {
                    if (nextArgIsBlank(proximoArg, "Erro: erro genérico")) break;
                    addPath(proximoArg);
                    i++;
                }
                case REMOVE_PATH -> {
                    if (nextArgIsBlank(proximoArg, "Erro: erro genérico")) break;
                        removePath(proximoArg);
                        i++;
                }
                case LIST_PATH -> {
                    if (nextArgIsBlank(proximoArg, "Erro: erro genérico")) break;
                        boolean isVerbose = (SubComandos.fromString(proximoArg).equals(SubComandos.VERBOSE));
                        listPath(isVerbose);
                        i++;
                }
                case CHECK, UPLOAD, DOWNLOAD, HELP -> {
                    for (String path : file.listPaths()) {
                        gitService.status(path);
                    }
                }

            }
        }
    }

    private static void cmdHelp() {
        System.out.println("Usage: java Main [options]");
        System.out.println("Usage: java Main [-a|--add-path|-r|--remove-path] 'path'");
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

    public static void listPath(boolean isVerbose) throws IOException {
        List<String> paths = file.listPaths();

        if (paths.isEmpty()) {
            alertarArquivoVazio(file.toString());
            return;
        }

        if (isVerbose) {
            for (int i = 0; i < paths.size(); i++) {
                System.out.printf("id: %s - caminho: %s\n", i, paths.get(i));
            }
            return;
        }

        for (int i = 0; i < paths.size(); i++) {
            System.out.printf("%s - %s\n", i, paths.get(i));
        }
    }

  public static boolean nextArgIsBlank(String nextArg, String errorMessage) {
      if (nextArg.isBlank()) {
          System.err.println(errorMessage);
          cmdHelp();
          return true;
      }
      return false;
  }

  public static void addPath(String valorPath) throws IOException {
      file.savePath(valorPath);
  }

  public static void removePath(String valorIndex) throws IOException {
      int index = (valorIndex.matches("\\d+")) ? Integer.parseInt(valorIndex) : -1;

      file.removePath(index);
  }
}
