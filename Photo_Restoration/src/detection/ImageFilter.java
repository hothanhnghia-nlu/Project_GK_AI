package detection;

import java.io.File;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class ImageFilter extends Util {
	
	public ImageFilter(File file) {
		super(file);
	}
	
	@Override
	public void setDestFile(String destFile) {
		super.setDestFile(destFile);
	}

	// Implement bilateral filter to denoise and make smooth image
	public void bilateralFilter() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		sourceFile = file.getAbsolutePath();
		
		Mat src = Imgcodecs.imread(sourceFile, Imgcodecs.IMREAD_COLOR);
		Mat dst = new Mat();
		
		if (src.empty()) {
			System.out.println("Error opening image!");
		} else {
			Imgproc.bilateralFilter(src, dst, 9, 50, 50);
		}
		setDestFile("images/result.jpg");
		Imgcodecs.imwrite(destFile, dst);
		System.out.println("Image Processed");
	}
}
