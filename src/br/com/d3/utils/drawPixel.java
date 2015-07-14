import java.awt.*;
import java.awt.image.*;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class drawPixel {
	static final int X = 380, Y = 250;
	static BufferedImage I = new BufferedImage(X, Y, BufferedImage.TYPE_INT_RGB);

	static public void main(String[] args) {
		for (int i = 0; i < X; ++i){
			for (int j = 0; j < Y; ++j){
				//I.setRGB(i, j, 0xffc068); // Tan packground
			}
		}
		for (int j = 0; j < 45; ++j){
			//I.setRGB(j, j, 0); // Scratch upper left corner
		}
		
		WritableRaster wr = I.getRaster();
		
		if (1 > 0) {
			int x = X - 40, y = Y - 40;
			int[] a = new int[y * x * 3]; // 96 bit pixels
			for (int j = 0; j < y; ++j){
				for (int i = 0; i < x; ++i) {
					int z = 3 * (j * x + i);
					a[z] = i;
					a[z + 1] = j;
					a[z + 2] = 128;
				}
			}
			wr.setPixels(30, 30, x, y, a);
		} 
		
		
		/*Frame f = new Frame("paint Example");
		f.add("Center", new MainCanvas());
		f.add(new Label(number + " "));
		f.setSize(new Dimension(X, Y + 22));
		f.setVisible(true);*/

		for (int q = 0; q < args.length; ++q) {
			int z = args[q].lastIndexOf('.');
			if (z > 0)
				try {
					ImageIO.write(I, args[q].substring(z + 1),
							new File(args[q]));
				} catch (IOException e) {
					System.err.println("image not saved.");
				}
		}
		
		
		int xS = 300, yS = 300;
		
		BufferedImage I = new BufferedImage(xS, yS, BufferedImage.TYPE_INT_RGB);
		int[] a = new int[yS * xS * 3]; // 96 bit pixels
		wr = I.getRaster();
		
		for (int j = 0; j < yS; ++j){
			for (int i = 0; i < xS; ++i) {
				int z = 3 * (j * xS + i);
				a[z] = i;
				a[z + 1] = j;
				a[z + 2] = 128;
			}
		}
		
		wr.setPixels(30, 30, xS, yS, a);
		
		try {
			ImageIO.write(I, "png", new File("teste2.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

class MainCanvas extends Canvas {
	public void paint(Graphics g) {
		g.drawImage(drawPixel.I, 0, 0, Color.red, null);
		Dimension s = getSize();
	}
}