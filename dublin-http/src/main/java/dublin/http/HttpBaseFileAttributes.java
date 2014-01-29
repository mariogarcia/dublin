package dublin.http;

import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

public class HttpBaseFileAttributes implements BasicFileAttributes {

	@Override
	public FileTime lastModifiedTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FileTime lastAccessTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FileTime creationTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isRegularFile() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDirectory() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSymbolicLink() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isOther() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public long size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object fileKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
