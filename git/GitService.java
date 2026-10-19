package git;

import java.io.IOException;
import java.util.List;

public class GitService {

    private final GitExecutor gitExecutor;
    public GitService(GitExecutor gitExecutor) {
        this.gitExecutor = gitExecutor;
    }

    public void status(String path) throws IOException, InterruptedException {
        List<String> command = GitCommands.status(path);

        try {
            var res = gitExecutor.execute(command);
            System.out.printf(
                    """
                    Processo finalizado com exit code: %s
                    Output:
                    %s
                    """,
                    res.exitCode(),
                    res.output()
            );
        } catch (IOException ioex) {
            throw new IOException("Erro ao tentar iniciar o processo do GIT: " + ioex);
        } catch (InterruptedException iex) {
            throw new InterruptedException("Processo do GIT parado de forma abrupta: "  + iex);
        }
    }




}
