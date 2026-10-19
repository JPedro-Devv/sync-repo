package git;

import java.util.Arrays;
import java.util.Optional;

public enum Flags {
    CHECK("-c", "--check"),
    UPLOAD("-u", "--upload"),
    DOWNLOAD("-d", "--download"),
    ADD_PATH("-a", "--add-path"),
    REMOVE_PATH("-r", "--remove-path"),
    LIST_PATH("-l", "--list-path"),
    VERBOSE("-v", "--verbose"),
    HELP("-h", "--help");

    private final String flagCurta;
    private final String flagLonga;

    public String getFlagCurta() {
        return flagCurta;
    }

    public String getFlagLonga() {
        return flagLonga;
    }

    Flags(String flagCurta, String flagLonga) {
        this.flagCurta = flagCurta;
        this.flagLonga = flagLonga;
    }

    public static Optional<Flags> fromString(String arg) {
        if (arg == null || arg.isBlank()) return Optional.empty();

        return Arrays.stream(values())
                .filter(f -> arg.equals(f.flagCurta) || arg.equals(f.flagLonga))
                .findFirst();
    }

}
