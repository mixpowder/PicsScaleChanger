import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.regex.Pattern;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

public class PicsScaleChanger {

	private int width;
	private int height;
	private String fileName;

	public PicsScaleChanger(String[] args) {
		this.width = Integer.parseInt(args[0]);
		this.height = Integer.parseInt(args[1]);
		this.fileName = args[2];
	}

	public boolean resize() {
		String[] data = fileName.split(Pattern.quote("."));
		String filename = "", extension = data[data.length - 1];
		for(int i = 0; i < data.length - 1; i ++) {
			filename += data[i];
		}

		try {
			BufferedImage original = ImageIO.read(new File(fileName));

			BufferedImage scaleImg = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
			scaleImg.createGraphics().drawImage(
					original.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING),
					0, 0, width, height, null);

			try (ImageOutputStream imageStream = ImageIO.createImageOutputStream(new File(filename + "-resize." + extension))) {
				ImageWriter writer = ImageIO.getImageWritersByFormatName("png").next();
				writer.setOutput(imageStream);
				writer.write(new IIOImage(scaleImg, null, null));
				imageStream.flush();
				writer.dispose();
			}
		}catch(Exception e) {
			System.out.println(e);
		}
		return true;
	}

	public static void main(String[] args) {
		if(args.length == 3) {
			new PicsScaleChanger(args).resize();
		}else {
			System.out.println("data is wrong");
		}

	}
}