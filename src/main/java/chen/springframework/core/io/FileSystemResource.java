package chen.springframework.core.io;

import com.sun.istack.internal.Nullable;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

public class FileSystemResource extends AbstractResource {

    private final String path;

    private final File file;

    private final Path filePath;

    public FileSystemResource(File file) {
        this.file = file;
        this.path = file.getPath();
        this.filePath = file.toPath();
    }

    public FileSystemResource(String path) {
        this.file = new File(path);
        this.path = path;
        this.filePath = this.file.toPath();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        try {
            return Files.newInputStream(this.filePath);
        }
        catch (NoSuchFileException ex) {
            throw new FileNotFoundException(ex.getMessage());
        }
    }

    @Override
    public String getDescription() {
        return "file [" + (this.file != null ? this.file.getAbsolutePath() : this.filePath.toAbsolutePath()) + "]";
    }
}
