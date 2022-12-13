package detection;

import java.io.File;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.photo.Photo;

public class Restoration extends Util {

	public Restoration(File file) {
		super(file);
	}
	
	@Override
	public void setDestFile(String destFile) {
		super.setDestFile(destFile);
	}

	public Mat scratchDetection() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		sourceFile = file.getAbsolutePath();
		
		Mat src = Imgcodecs.imread(sourceFile);
		Mat dst = new Mat();
		
//		double[] low_green = {25, 52, 72};
//		double[] high_green = {110, 200, 200};
//		Imgproc.cvtColor(src, dst, Imgproc.COLOR_BGR2HSV);
//		Core.inRange(src, new Scalar(low_green), new Scalar(high_green), dst);
		
		Imgproc.Canny(src, dst, 100, 200);
		
		setDestFile("images/mask.jpg");
		Imgcodecs.imwrite(destFile, dst);
		System.out.println("Finished!");
		return dst;
	}
	
	public void run() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		sourceFile = file.getAbsolutePath();
		
		Mat src = Imgcodecs.imread(sourceFile);
		Mat dst = new Mat();
		
		Mat mask = scratchDetection();
		
		if (src.empty()) {
			System.out.println("Error opening image!");
		} else {
			try {
				Photo.inpaint(src, mask, dst, 3, Photo.INPAINT_TELEA);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		setDestFile("images/result.jpg");
		Imgcodecs.imwrite(destFile, dst);
		System.out.println("Restored Successful!");
	}
	
	public static void main(String[] args) {
		String path = "images/scratch_2.jpg";
		File source = new File(path);
		Restoration restore = new Restoration(source);
		restore.scratchDetection();
		restore.run();
	}
}
