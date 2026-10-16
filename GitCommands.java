import java.util.ArrayList;
import java.util.List;

public  class GitCommands {

    private static List<String> initialCommand(String path) {
        return List.of(
                "git",
                "-C",
                path
        );
    }

    public static List<String> fetch(String path) {
        List<String> fetch = new ArrayList<>(initialCommand(path));

        fetch.add("fetch");

        return fetch;
    }

    public static List<String> status(String path) {
        List<String> status = new ArrayList<>(initialCommand(path));

        status.add("status");
        status.add("--porcelain");

        return status;
    }

    public static List<String> push(String path) {
        List<String> push = new ArrayList<>(initialCommand(path));

        push.add("push");
        push.add("origin");
        push.add("main");

        return push;
    }

    public static List<String> pull(String path) {
        List<String> pull = new ArrayList<>(initialCommand(path));

        pull.add("pull");
        pull.add("origin");
        pull.add("main");

        return pull;
    }

    public static List<String> revList(String path) {
        List<String> revList = new ArrayList<>(initialCommand(path));

        revList.add("rev-list");
        revList.add("--left-right");
        revList.add("--count");
        revList.add("HEAD..main");

        return revList;
    }


}
