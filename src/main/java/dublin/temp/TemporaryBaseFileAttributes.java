package dublin.temp;

import java.io.File;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.BasicFileAttributes;

public class TemporaryBaseFileAttributes implements BasicFileAttributes {

    private File file;

    public TemporaryBaseFileAttributes(File file) {
        this.file = file;
    }

    public FileTime creationTime() {
        return FileTime.fromMillis(file.lastModified());
    }

    public Object fileKey() {
        return file.getAbsolutePath();
    }

    public boolean isDirectory() {
        return file.isDirectory();
    }

    public boolean isOther() {
        return false;
    }

    public boolean isRegularFile() {
        return !file.isDirectory();
    }

    public boolean isSymbolicLink() {
        return false;
    }

    public FileTime lastAccessTime() {
        return FileTime.fromMillis(file.lastModified());
    }

    public FileTime lastModifiedTime() {
        return FileTime.fromMillis(file.lastModified());
    }

    public long size() {
        return file.length();
    }

}
