import java.util.List;

public class GitCommands {

    public static void main(String[] args) {
        var r = cmdFetch(args[0]);
        System.out.println(r);
    }

    public static List<String> cmdFetch(String path) {
        List<String> fetch = List.of(
            "git",
             "-C",
             path,
             "status"
        );

        return fetch;
    }




}
