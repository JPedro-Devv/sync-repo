package git;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class GitExecutor {

    public GitResult execute(List<String> command) throws IOException, InterruptedException{
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);
        pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        Process process = pb.start();

        String output = new String(
                process.getInputStream().readAllBytes(),
                StandardCharsets.UTF_8
        );
        int exitCode = process.waitFor();

        return new GitResult(exitCode,output);
    }

}
