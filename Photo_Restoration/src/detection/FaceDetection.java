package detection;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import java.io.*;

public class FaceDetection extends Util {
	
	public FaceDetection(File file) {
		super(file);
	}

	@Override
	public void setDestFile(String destFile) {
		super.setDestFile(destFile);
	}

	// Implement detect face
	public Mat faceDetection() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		sourceFile = file.getAbsolutePath();
		Mat src = Imgcodecs.imread(sourceFile);
		
		String cmlFile = "xml/lbpcascade_frontalface.xml";
		CascadeClassifier cc = new CascadeClassifier(cmlFile);
		
		MatOfRect faceDetection = new MatOfRect();
		cc.detectMultiScale(src, faceDetection);
		
		for (Rect rect : faceDetection.toArray()) {
			Imgproc.rectangle(src, new Point(rect.x, rect.y),
	                new Point(rect.x + rect.width, rect.y + rect.height),
	                new Scalar(0, 255, 0), 3);
		}
		setDestFile("images/result.jpg");
		Imgcodecs.imwrite(destFile, src);
		return src;
	}
	
	// Check if has face detecion
	public boolean isFaceDetection() {
		if (faceDetection() != null) {
			return true;
		}
		return false;
	}
}
