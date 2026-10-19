import exceptions.NotGitRepositoryException;
import git.Flags;
import git.GitExecutor;
import git.GitService;

import java.io.IOException;
import java.util.List;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class AppCLI {
    private static RepositoryManager file;
    private static GitService gitService;

    public static void main(String[] args) {
        try {
            setup();
            start(args);
        } catch (IOException ioex) {
            System.err.println("Erro de I/O: " + ioex.getMessage());
            System.exit(1);
        } catch (InterruptedException iex) {
            System.err.println("Erro de Thread: " + iex.getMessage());
            System.exit(2);
        } catch (NotGitRepositoryException ngrex) {
            System.err.println("Erro de Git: " + ngrex.getMessage());
            System.exit(3);
        }

    }
    private static void setup() throws IOException {
        String homePath = System.getProperty("user.home");
        Path completePath = Paths.get(homePath, ".git_repos");

        file = new RepositoryManager(completePath.toString());

        gitService = new GitService(new GitExecutor());
    }


    public static void start(String[] args) throws IOException, InterruptedException {

        if (args.length == 0) {
            cmdHelp();
            return;
        }

        for (int i = 0; i < args.length; i++) {
            int proximoArgumento = i + 1;

            String argAtual = args[i];
            Optional<String> proximoArg = proximoArgumento < args.length && !args[proximoArgumento].isBlank()
                    ? Optional.of(args[proximoArgumento])
                    : Optional.empty();

            Flags flag = Flags.fromString(argAtual).
                    orElseGet(() -> {
                        System.err.println("Argumento ou opção desconhecida: " + argAtual);
                        return Flags.HELP;
                    });

            switch (flag) {
                case ADD_PATH -> {
                    if (proximoArg.isEmpty()) {
                        errorMessage("Erro: erro genérico");
                        break;
                    }
                    addPath(proximoArg.get());
                    i++;
                }
                case REMOVE_PATH -> {
                    if (proximoArg.isEmpty()) {
                        errorMessage("Erro: erro genérico");
                        break;
                    }
                    removePath(proximoArg.get());
                    i++;
                }
                case LIST_PATH -> {
                    boolean isVerbose = Flags.fromString(proximoArg.orElse(""))
                            .filter(f -> f.equals(Flags.VERBOSE))
                            .isPresent();

                    listPath(isVerbose);
                    if (isVerbose) i++;
                }
                case CHECK -> cmdCheck();
                case UPLOAD -> cmdUpload();
                case DOWNLOAD -> cmdDownload();
                case HELP -> cmdHelp();

            }
        }
    }

    public static void addPath(String valorPath) throws IOException {
        if (file.add(valorPath)) System.out.println(valorPath + " adicionado com sucesso!");
        else System.out.println("falha ao adicionar caminho");
    }

    public static void removePath(String indexPath) throws IOException {
        int index = (indexPath.matches("\\d+")) ? Integer.parseInt(indexPath) : -1;

        if (file.remove(index)) System.out.println("caminho removido com sucesso!");
        else System.out.println("falha ao remover caminho");
    }

    public static void listPath(boolean isVerbose) {
        List<String> paths = file.list();

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

    private static void cmdCheck() throws IOException, InterruptedException {
        for (String path : file.list()) {
            gitService.status(path);
        }
    }

    private static void cmdUpload() {
    }

    private static void cmdDownload() {
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
                Flags.ADD_PATH.getFlagCurta(),
                Flags.ADD_PATH.getFlagLonga()
        );
    }

    private static void errorMessage(String errorMessage) {
        System.err.println(errorMessage);
        cmdHelp();
    }
}
