import exceptions.NotGitRepositoryException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Gerencia o processamento e a persistência de arquivos utilizados pela aplicação.
 * <p>
 * Esta classe é responsável por ler e salvar os dados no arquivo utilizado pela aplicação,
 * ela tem como responsabilidade adicionar e remover caminhos, e listar os caminhos de dentro do arquivo.
 *
 * @author João Pedro
 * @version 1.0
 * @since 2026-10
 */
public class RepositoryManager {
    private final Path filePath;
    private final Set<String> repositories;

    public RepositoryManager(String filePathStr) throws IOException {
        this.filePath = Paths.get(filePathStr);
        this.repositories = new LinkedHashSet<>();
        load();
    }

    /**
     * Carrega os caminhos do arquivo para a memória.
     * <p>
     * @throws IOException se não conseguir ler o arquivo {@code filePath}
     */
    public synchronized void load() throws IOException {
        if (!Files.exists(filePath)) return;

        try {
            List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
            repositories.clear();

            for (String line : lines) {
                String trimmedLine = line.trim();
                if (!trimmedLine.isEmpty()) repositories.add(normalizePath(trimmedLine));
            }
        } catch (IOException e) {
            throw new IOException("Erro ao carregar arquivo de repositórios: " + filePath, e);
        }
    }

    /**
     * Salva o estado atual da memória de volta no arquivo.
     * <p>
     * @throws IOException se não conseguir salvar os dados no arquivo {@code filePath}
     */
    public synchronized void save() throws IOException {
        try {
            if (filePath.getParent() != null && !Files.exists(filePath.getParent())) {
                Files.createDirectories(filePath.getParent());
            }
            Files.write(filePath, repositories, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new IOException("Erro ao salvar arquivo de repositórios " + filePath, e);
        }
    }

    /**
     * Adiciona um novo caminho de repositório e salvando no arquivo.
     * <p>
     * @param path uma {@link String} contendo o caminho que será adicionado ao arquivo {@code filePath}.
     * @return retorna {@code true} caso consiga adicionar o caminho e caso não consiga, retorna {@code false}.
     * @throws IOException se não conseguir adicionar o caminho ao arquivo {@code filePath}.
     */
    public synchronized boolean add(String path) throws IOException {
        if (path == null || path.isEmpty()) return false;

        String normalizedPath = normalizePath(path);

        Path gitPath = Paths.get(normalizedPath);

        boolean isGitRepo = Files.isDirectory(gitPath)
                && Files.isDirectory(gitPath.resolve(".git"));

        if (!isGitRepo) {
            throw new NotGitRepositoryException("O caminho informado não é um repositório Git válido: " + normalizedPath);
        }

        boolean added = repositories.add(normalizedPath);
        if (added) save();

        return added;
    }

    /**
     * Remove um caminho de repositório e atualiza o arquivo.
     * <p>
     * @param indexPath um {@code int} contendo o index referente ao caminho que será removido do arquivo {@code filePath}.
     * @return retorna {@code true} caso consiga adicionar o caminho e caso não consiga, retorna {@code false}.
     * @throws IOException se não conseguir remover o caminho do arquivo {@code filePath}.
     */
    public synchronized boolean remove(int indexPath) throws IOException {
        if (indexPath == -1) return false;

        ArrayList<String> list = new ArrayList<>(repositories);

        String normalizedPath = normalizePath(list.get(indexPath));

        boolean removed = repositories.remove(normalizedPath);
        if (removed) save();

        return removed;
    }

    /**
     * Retorna uma cópia imutável ou lista dos caminhos salvos.
     * @return retorna uma {@code List<String>} contendo todos os caminhos salvos no arquivo {@code filePath}
     */
    public synchronized List<String> list() {
        return new ArrayList<>(repositories);
    }

    /**
     * Pega o caminho absoluto e normaliza com caminho passado como argumento.
     * @param path String
     * @return String
     */
    private String normalizePath(String path) {
        String sanitizedPath = path.replace("\\ ", " ");

        return Paths.get(sanitizedPath)
                .toAbsolutePath()
                .normalize()
                .toString();
    }

}
