package detection;

import java.io.File;

public class Util {
	File file;
	String sourceFile;
	String destFile;
	
	public Util(File file) {
		this.file = file;
	}
	
	public String getDestFile() {
		return destFile;
	}

	public void setDestFile(String destFile) {
		this.destFile = destFile;
	}
}
