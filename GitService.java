import java.util.List;

public class GitService {

    public static void fetch(String path) {
        GitExecutor.cmdFetch(path);
    }

    public static void status(String path) {
        List<String> response = GitExecutor.cmdStatus("/home/jp/Área de trabalho/Códigos/Java/sync-repo");

        response.forEach(System.out::println);
    }
}
