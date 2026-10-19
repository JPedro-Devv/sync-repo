package git;

public record GitResult(
        int exitCode,
        String output
) {}
