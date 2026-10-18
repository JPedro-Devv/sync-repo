import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class GitRepositoriesFile {
    private final Path path;
    private final File file;
    private final List<String> linhasArquivo;

    private GitRepositoriesFile(Builder builder) {
        this.path = builder.path;
        this.file = builder.file;
        this.linhasArquivo = builder.linhasArquivo;
    }

    private void loadFile() throws IOException {
        linhasArquivo.clear();

        List<String> linhasBrutas = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);

        linhasArquivo.addAll(linhasBrutas.stream()
                .filter(line -> !line.isBlank())
                .collect(Collectors.toSet()));
    }

    public void savePath(String content) throws IOException {
        if (content == null || content.isBlank()) throw new RuntimeException("Propriedade path vazia!");

        loadFile();

        linhasArquivo.add(content);

        try {
            Files.write(path, linhasArquivo);
        } catch (IOException ex) {
            throw new IOException(format("Erro ao salvar o path '%s' no arquivo!", path));
        }
    }

    public void removePath(int index) throws IOException {
        loadFile();

        linhasArquivo.remove(index);
        System.out.printf("--- linhas : %s ---\n", linhasArquivo);

        try {
            Files.write(path, linhasArquivo);
        } catch (IOException ex) {
            throw new IOException("Erro ao remover o path do arquivo");
        }
    }

    public List<String> listPaths() throws IOException {
        loadFile();

        return linhasArquivo;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Path path;
        private File file;
        private List<String> linhasArquivo;

        public Builder path(Path path) {
            this.path = path;
            return this;
        }

        public GitRepositoriesFile build() throws IOException {
            if (this.path == null) throw new IllegalArgumentException("O path não pode ser vazio!");

            file = new File(this.path.toString());
            linhasArquivo = new ArrayList<>();

            if (!file.exists()) {
                try {
                    Files.createFile(this.path);
                } catch (IOException ex) {
                    throw new IOException(format("O arquivo %s não foi encontrado e ocorreu um erro ao tentar cria-lo, erro: %s",
                            this.path,
                            ex.getMessage()));
                }
            }
            return new GitRepositoriesFile(this);
        }
    }
}
