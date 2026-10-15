public enum SubComandos {
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

    SubComandos(String flagCurta, String flagLonga) {
        this.flagCurta = flagCurta;
        this.flagLonga = flagLonga;
    }

    public static SubComandos fromString(String arg) {
        if (arg == null) return null;

        for (SubComandos c : values()) {
            if (c.flagCurta.equalsIgnoreCase(arg) || c.flagLonga.equalsIgnoreCase(arg)) {
                return c;
            }
        }
        return  null;
    }

}
