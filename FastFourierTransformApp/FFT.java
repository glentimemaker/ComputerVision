import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.pearsoneduc.ip.op.FFTException;
import com.pearsoneduc.ip.util.Complex;

public class FFT extends JFrame {

	// constant variables
	private static final String NO_DATA = "no spectral data available";
	private static final String INVALID_PARAMS = "invalid filter parameters";
	private static final double TWO_PI = 2.0 * Math.PI;

	//Image variable
	BufferedImage image2 = null;
	BufferedImage image3 = null;
	BufferedImage image_null = null;
	BufferedImage imagelow1c = null;
	BufferedImage imagelow2c = null;
	BufferedImage imagelow3c = null;
	BufferedImage imagehigh1c = null;
	BufferedImage imagehigh2c = null;

	// tabbedPane Panels
	JPanel firstPanel = new JPanel();
	JPanel secondPanel = new JPanel();
	JPanel thirdPanel = new JPanel();
	JTabbedPane tabbedPane = new JTabbedPane();

	//Layout variable
	JPanel panel1 = new JPanel();
	JPanel panel2 = new JPanel();
	JPanel panel3 = new JPanel();
	JPanel panel4 = new JPanel();
	JPanel panel5 = new JPanel();
	JPanel panel6 = new JPanel();
	JPanel panel7 = new JPanel();
	JPanel panel8 = new JPanel();
	JPanel mpanel2 = new JPanel();
	JPanel mpanel3 = new JPanel();

	JLabel label1 = new JLabel();
	JLabel label2 = new JLabel();
	JLabel label3 = new JLabel();
	JLabel label4 = new JLabel();
	JLabel label5 = new JLabel();
	JLabel label6 = new JLabel();
	JLabel label7 = new JLabel();
	JLabel label8 = new JLabel();
	JButton b_trans = new JButton("fft");
	JButton low = new JButton("fft");
	JButton high = new JButton("fft");
	JButton low2 = new JButton("fft");
	double r2;
	double r3;
	ImageIcon icon1;
	ImageIcon icon2;
	ImageIcon icon3;
	ImageIcon icon4;
	ImageIcon icon5;
	ImageIcon icon6;
	ImageIcon icon7;
	ImageIcon icon8;
	
	// Data variable
	private Complex[] data;
	private int log2w;
	private int log2h;
	private int width;
	private int height;
	private boolean spectral = false;
	

	public FFT(BufferedImage image) throws FFTException, IOException {
		super("FFT & FFT Inverse");
		tabbedPane.add("FFT", firstPanel);
		tabbedPane.add("Low Pass", secondPanel);
		tabbedPane.add("High Pass", thirdPanel);
		add(tabbedPane);

		image2 = new BufferedImage(image.getWidth(), image.getHeight(),
				BufferedImage.TYPE_BYTE_GRAY);
		image3 = new BufferedImage(image.getWidth(), image.getHeight(),
				BufferedImage.TYPE_BYTE_GRAY);
		image_null = new BufferedImage(image.getWidth(), image.getHeight(),
				BufferedImage.TYPE_BYTE_GRAY);
		// image = convertType(image);
		WritableRaster raster = image_null.getRaster();
		for (int w = 0; w < image_null.getWidth(); ++w) {
			for (int h = 0; h < image_null.getHeight(); ++h) {
				raster.setSample(w, h, 0, 255);
			}

		}

		/*
		 * First panel layout
		 */
		firstPanel.setBackground(Color.WHITE);

		panel1.add(label1);
		panel2.add(label2);
		panel3.add(label3);
		label1.setSize(300, 300);
		label2.setSize(300, 300);
		label3.setSize(300, 300);

		Image newimg1 = image.getScaledInstance(label1.getWidth(),
				label1.getHeight(), java.awt.Image.SCALE_SMOOTH);
		Image newimg2 = image_null.getScaledInstance(label2.getWidth(),
				label2.getHeight(), java.awt.Image.SCALE_SMOOTH);
		icon1 = new ImageIcon(newimg1);
		icon2 = new ImageIcon(newimg2);
		icon3 = new ImageIcon(newimg2);

		label1.setIcon(icon1);
		label2.setIcon(icon2);
		label3.setIcon(icon3);

		firstPanel.add(panel1);
		firstPanel.add(panel2);
		firstPanel.add(panel3);
		firstPanel.add(b_trans, BorderLayout.SOUTH);

		/*
		 * second
		 */
		secondPanel.setBackground(Color.WHITE);
		panel4.add(label4);
		panel5.add(label5);
		panel6.add(label6);
		label4.setSize(300, 300);
		label5.setSize(300, 300);
		label6.setSize(300, 300);

		final BufferedImage imagelow1 = convertType(ImageIO.read(new File(
				"Sinusoidal.jpg")));
		imagelow1c = new BufferedImage(imagelow1.getWidth(),
				imagelow1.getHeight(), BufferedImage.TYPE_BYTE_GRAY);

		final BufferedImage imagelow2 = convertType(ImageIO.read(new File(
				"Salt.jpg")));
		imagelow2c = new BufferedImage(imagelow2.getWidth(),
				imagelow2.getHeight(), BufferedImage.TYPE_BYTE_GRAY);

		final BufferedImage imagelow3 = convertType(ImageIO.read(new File(
				"Gaussian.jpg")));
		imagelow3c = new BufferedImage(imagelow3.getWidth(),
				imagelow3.getHeight(), BufferedImage.TYPE_BYTE_GRAY);

		Image newimg4 = imagelow1.getScaledInstance(label4.getWidth(),
				label4.getHeight(), java.awt.Image.SCALE_SMOOTH);
		Image newimg5 = imagelow2.getScaledInstance(label5.getWidth(),
				label5.getHeight(), java.awt.Image.SCALE_SMOOTH);
		Image newimg6 = imagelow3.getScaledInstance(label6.getWidth(),
				label6.getHeight(), java.awt.Image.SCALE_SMOOTH);
		icon4 = new ImageIcon(newimg4);
		icon5 = new ImageIcon(newimg5);
		icon6 = new ImageIcon(newimg6);

		label4.setIcon(icon4);
		label5.setIcon(icon5);
		label6.setIcon(icon6);

		JLabel slabel2 = new JLabel("Filter Radius");
		JSlider slider2 = new JSlider(JSlider.HORIZONTAL, 0, 10, 0);

		slabel2.setOpaque(true);
		slider2.setMajorTickSpacing(1);
		slider2.setPaintTicks(true);

		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put(new Integer(10), new JLabel("1.0"));
		labelTable.put(new Integer(5), new JLabel("0.5"));
		labelTable.put(new Integer(0), new JLabel("0.0"));
		slider2.setLabelTable(labelTable);
		slider2.setPaintLabels(true);
		slider2.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(javax.swing.event.ChangeEvent ce) {
				JSlider source2 = (JSlider) ce.getSource();
				r2 = source2.getValue();
			}
		});

		mpanel2.add(slabel2);
		mpanel2.add(slider2);
		// mpanel2.add(low);

		secondPanel.add(panel4);
		secondPanel.add(panel5);
		secondPanel.add(panel6);
		secondPanel.add(mpanel2);
		secondPanel.add(low2);
		
		/*
		 * third
		 */
		thirdPanel.setBackground(Color.WHITE);
		panel7.add(label7);
		panel8.add(label8);
		label7.setSize(300, 300);
		label8.setSize(300, 300);

		final BufferedImage imagehigh1 = convertType(ImageIO.read(new File(
				"Blur1.jpg")));
		imagehigh1c = new BufferedImage(imagehigh1.getWidth(),
				imagehigh1.getHeight(), BufferedImage.TYPE_BYTE_GRAY);

		final BufferedImage imagehigh2 = convertType(ImageIO.read(new File(
				"Blur2.jpg")));
		imagehigh2c = new BufferedImage(imagehigh2.getWidth(),
				imagehigh2.getHeight(), BufferedImage.TYPE_BYTE_GRAY);

		Image newimg7 = imagehigh1.getScaledInstance(label7.getWidth(),
				label7.getHeight(), java.awt.Image.SCALE_SMOOTH);
		Image newimg8 = imagehigh2.getScaledInstance(label8.getWidth(),
				label8.getHeight(), java.awt.Image.SCALE_SMOOTH);
		icon7 = new ImageIcon(newimg7);
		icon8 = new ImageIcon(newimg8);

		label7.setIcon(icon7);
		label8.setIcon(icon8);

		JLabel slabel3 = new JLabel("Filter Radius");
		JSlider slider3 = new JSlider(JSlider.HORIZONTAL, 0, 10, 0);

		slabel3.setOpaque(true);
		slider3.setMajorTickSpacing(1);
		slider3.setPaintTicks(true);

		Hashtable<Integer, JLabel> labelTable2 = new Hashtable<Integer, JLabel>();
		labelTable2.put(new Integer(10), new JLabel("1.0"));
		labelTable2.put(new Integer(5), new JLabel("0.5"));
		labelTable2.put(new Integer(0), new JLabel("0.0"));
		slider3.setLabelTable(labelTable2);
		slider3.setPaintLabels(true);
		slider3.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(javax.swing.event.ChangeEvent ce) {
				JSlider source = (JSlider) ce.getSource();
				r3 = source.getValue();
			}
		});

		mpanel3.add(slabel3);
		mpanel3.add(slider3);
		mpanel3.add(high);
		thirdPanel.add(panel7);
		thirdPanel.add(panel8);
		thirdPanel.add(mpanel3);

		// first transform
		initImgData(image);
		// BufferedImage initImage = toImage(null);
		// File outputfile = new File("initImage.jpg");
		// ImageIO.write(initImage, "jpg", outputfile);
		b_trans.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {

				transform();

				BufferedImage image_change = null;
				try {
					image_change = getSpectrum();
				} catch (FFTException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Image newimg2 = image_change.getScaledInstance(
						label2.getWidth(), label2.getHeight(),
						java.awt.Image.SCALE_SMOOTH);
				label2.setIcon(new ImageIcon(newimg2));
				transform();
				try {
					image3 = toImage(image3);
					// File outputfile = new File("imagefft.jpg");
					// ImageIO.write(image3, "jpg", outputfile);
					Image newimg3 = image3.getScaledInstance(label3.getWidth(),
							label3.getHeight(), java.awt.Image.SCALE_SMOOTH);
					label3.setIcon(new ImageIcon(newimg3));
					System.out.print("inverse successfully");
				} catch (FFTException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		// second transform

		low2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					r2 = (double) (r2 / 10);
					System.out.print("filter radius=" + r2 + "\n");
					initImgData(imagelow1);
					transform();

					butterworthLowPassFilter(r2);
					transform();
					imagelow1c = toImage(imagelow1c);
					Image newimg1 = imagelow1c.getScaledInstance(
							label4.getWidth(), label4.getHeight(),
							java.awt.Image.SCALE_SMOOTH);
					label4.setIcon(new ImageIcon(newimg1));

					initImgData(imagelow2);
					transform();
					butterworthLowPassFilter(r2);
					transform();
					imagelow2c = toImage(imagelow2c);
					Image newimg2 = imagelow2c.getScaledInstance(
							label5.getWidth(), label5.getHeight(),
							java.awt.Image.SCALE_SMOOTH);
					label5.setIcon(new ImageIcon(newimg2));

					initImgData(imagelow3);
					transform();
					butterworthLowPassFilter(r2);
					transform();
					imagelow3c = toImage(imagelow3c);
					Image newimg3 = imagelow3c.getScaledInstance(
							label6.getWidth(), label6.getHeight(),
							java.awt.Image.SCALE_SMOOTH);
					label6.setIcon(new ImageIcon(newimg3));
				} catch (FFTException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		high.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					r3 = (double) (r3 / 10);
					System.out.print("filter radius=" + r3 + "\n");
					initImgData(imagehigh1);
					transform();
					butterworthHighPassFilter(r3);
					transform();
					imagehigh1c = toImage(imagehigh1c);
					Image newimg1 = imagehigh1c.getScaledInstance(
							label7.getWidth(), label7.getHeight(),
							java.awt.Image.SCALE_SMOOTH);
					label7.setIcon(new ImageIcon(newimg1));

					initImgData(imagehigh2);
					transform();
					butterworthHighPassFilter(r3);
					transform();
					imagehigh2c = toImage(imagehigh2c);
					Image newimg2 = imagehigh2c.getScaledInstance(
							label8.getWidth(), label8.getHeight(),
							java.awt.Image.SCALE_SMOOTH);
					label8.setIcon(new ImageIcon(newimg2));
				} catch (FFTException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

	}

	private void initImgData(BufferedImage image) throws FFTException {
		if (image.getType() != BufferedImage.TYPE_BYTE_GRAY)
			throw new FFTException("image must be 8-bit greyscale");

		// Compute dimensions, allowing for zero padding

		log2w = powerOfTwo(image.getWidth()); // find the power of the width by
												// using log 2.
		log2h = powerOfTwo(image.getHeight());
		width = 1 << log2w;// make the width to be binary type.
		height = 1 << log2h;

		// Allocate storage for results of FFT

		data = new Complex[width * height];
		for (int i = 0; i < data.length; ++i)
			data[i] = new Complex();

		Raster rasterc = image.getRaster();

		for (int y = 0; y < image.getHeight(); ++y)
			for (int x = 0; x < image.getWidth(); ++x) {
				data[y * width + x].re = rasterc.getSample(x, y, 0);
			}

	}

	public static void main(String args[]) throws FFTException, IOException {
		BufferedImage image = ImageIO.read(new File(
				"matthead.png"));
		image = convertType(image);
		FFT fft = new FFT(image);
		fft.setSize(1000, 450);

		fft.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		fft.setVisible(true);
	}

	private static BufferedImage convertType(BufferedImage image) {
		BufferedImage imagec = new BufferedImage(image.getWidth(),
				image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster rasterc = imagec.getRaster();
		Raster raster = image.getRaster();
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				rasterc.setSample(i, j, 0, raster.getSample(i, j, 0));
			}
		}
		return imagec;

	}

	//Forward and Inverse transform, decide by direction
	public void transform() {

		int x, y, i;
		Complex[] row = new Complex[width];
		for (x = 0; x < width; ++x)
			row[x] = new Complex();
		Complex[] column = new Complex[height];
		for (y = 0; y < height; ++y)
			column[y] = new Complex();

		int direction;
		if (spectral)
			direction = -1; // inverse transform
		else
			direction = 1; // forward transform

		// Perform FFT on each row

		for (y = 0; y < height; ++y) {
			for (i = y * width, x = 0; x < width; ++x, ++i) {
				row[x].re = data[i].re;
				row[x].im = data[i].im;
			}
			reorder(row, width);
			fft(row, width, log2w, direction);
			for (i = y * width, x = 0; x < width; ++x, ++i) {
				data[i].re = row[x].re;
				data[i].im = row[x].im;
			}
		}

		// Perform FFT on each column

		for (x = 0; x < width; ++x) {
			for (i = x, y = 0; y < height; ++y, i += width) {
				column[y].re = data[i].re;
				column[y].im = data[i].im;
			}
			reorder(column, height);
			fft(column, height, log2h, direction);
			for (i = x, y = 0; y < height; ++y, i += width) {
				data[i].re = column[y].re;
				data[i].im = column[y].im;
			}
		}

		if (spectral)
			spectral = false;
		else
			spectral = true;

	}

	//Get spectrum
	public BufferedImage getSpectrum() throws FFTException, IOException {

		if (!spectral)
			throw new FFTException(NO_DATA);

		// Collect magnitudes and find maximum

		float[] magData = new float[width * height];
		float maximum = calculateMagnitudes(magData);
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster raster = image.getRaster();

		// Shift, rescale and copy to image

		double scale = 255.0 / Math.log(maximum + 1.0);
		int x2 = width / 2, y2 = height / 2;
		int sx, sy, value;
		for (int y = 0; y < height; ++y) {
			sy = shift(y, y2);
			for (int x = 0; x < width; ++x) {
				sx = shift(x, x2);
				value = (int) Math.round(scale
						* Math.log(magData[sy * width + sx] + 1.0));
				raster.setSample(x, y, 0, value);
			}
		}
		// saveImage(image);
		return image;

	}

	//Magnitude
	public float getMagnitude(int u, int v) throws FFTException {
		if (!spectral)
			throw new FFTException(NO_DATA);
		if (u >= 0 && u < width && v >= 0 && v < height)
			return data[v * width + u].getMagnitude();
		else
			return 0.0f;
	}

	//Phase
	public float getPhase(int u, int v) throws FFTException {
		if (!spectral)
			throw new FFTException(NO_DATA);
		if (u >= 0 && u < width && v >= 0 && v < height)
			return data[v * width + u].getPhase();
		else
			return 0.0f;
	}

	//Save data to BufferedImage Image
	public BufferedImage toImage(BufferedImage image) throws FFTException {
		return toImage(image, 0);
	}

	public BufferedImage toImage(BufferedImage image, int bias)
			throws FFTException {

		if (spectral)
			throw new FFTException("cannot transfer spectral data to an image");

		if (image == null)
			image = new BufferedImage(width, height,
					BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster raster = image.getRaster();

		int w = Math.min(image.getWidth(), width);
		int h = Math.min(image.getHeight(), height);
		// If destination image is bigger, zero it

		if (w < image.getWidth() || h < image.getHeight())
			for (int y = 0; y < image.getHeight(); ++y)
				for (int x = 0; x < image.getWidth(); ++x)
					raster.setSample(x, y, 0, 0);

		// Copy real component of data to destination image

		int i = 0, value;
		for (int y = 0; y < height; ++y)
			for (int x = 0; x < width; ++x, ++i) {
				value = Math.max(0,
						Math.min(255, bias + Math.round(data[i].re)));
				raster.setSample(x, y, 0, value);
			}

		return image;

	}

	//Reorder for fft
	private static void reorder(Complex[] data, int n) {
		int j = 0, m;
		for (int i = 0; i < n; ++i) {
			if (i > j)
				data[i].swapWith(data[j]);
			m = n >> 1;
			while ((j >= m) && (m >= 2)) {
				j -= m;
				m >>= 1;
			}
			j += m;
		}
	}

	//One dimension fft
	private static void fft(Complex[] data, int size, int log2n, int dir) {

		double angle, wtmp, wpr, wpi, wr, wi, tmpr, tmpi;
		int n = 1, n2;
		for (int k = 0; k < log2n; ++k) {

			n2 = n;
			n <<= 1;
			angle = (-TWO_PI / n) * dir;
			wtmp = Math.sin(0.5 * angle);
			wpr = -2.0 * wtmp * wtmp;
			wpi = Math.sin(angle);
			wr = 1.0;
			wi = 0.0;

			for (int m = 0; m < n2; ++m) {
				for (int i = m; i < size; i += n) {
					int j = i + n2;
					tmpr = wr * data[j].re - wi * data[j].im;
					tmpi = wi * data[j].re + wr * data[j].im;
					data[j].re = (float) (data[i].re - tmpr);
					data[i].re += (float) tmpr;
					data[j].im = (float) (data[i].im - tmpi);
					data[i].im += (float) tmpi;
				}
				wtmp = wr;
				wr = wtmp * wpr - wi * wpi + wr;
				wi = wi * wpr + wtmp * wpi + wi;
			}

		}

		// Rescale results of inverse transform

		if (dir == -1)
			for (int i = 0; i < size; ++i) {
				data[i].re /= size;
				data[i].im /= size;
			}

	}

	//Butterworth Low Pass Filter
	public void butterworthLowPassFilter(double radius) throws FFTException {
		butterworthLowPassFilter(1, radius);
	}

	public void butterworthLowPassFilter(int n, double radius)
			throws FFTException {

		if (!spectral)
			throw new FFTException(NO_DATA);

		if (n < 1 || radius <= 0.0 || radius > 1.0)
			throw new FFTException(INVALID_PARAMS);

		int u2 = width / 2;
		int v2 = height / 2;
		int su, sv, i = 0;
		double mag, r, rmax = Math.min(u2, v2);

		for (int v = 0; v < height; ++v) {
			sv = shift(v, v2) - v2;
			for (int u = 0; u < width; ++u, ++i) {
				su = shift(u, u2) - u2;
				r = Math.sqrt(su * su + sv * sv) / rmax;
				mag = butterworthLowPassFunction(n, radius, r)
						* data[i].getMagnitude();
				data[i].setPolar(mag, data[i].getPhase());
			}
		}

	}

	// Butter Worth High Pass Filter
	public void butterworthHighPassFilter(double radius) throws FFTException {
		butterworthHighPassFilter(1, radius);
	}

	public void butterworthHighPassFilter(int n, double radius)
			throws FFTException {

		if (!spectral)
			throw new FFTException(NO_DATA);

		if (n < 1 || radius <= 0.0 || radius > 1.0)
			throw new FFTException(INVALID_PARAMS);

		int u2 = width / 2;
		int v2 = height / 2;
		int su, sv, i = 0;
		double mag, r, rmax = Math.min(u2, v2);

		for (int v = 0; v < height; ++v) {
			sv = shift(v, v2) - v2;
			for (int u = 0; u < width; ++u, ++i) {
				su = shift(u, u2) - u2;
				r = Math.sqrt(su * su + sv * sv) / rmax;
				mag = butterworthHighPassFunction(n, radius, r)
						* data[i].getMagnitude();
				data[i].setPolar(mag, data[i].getPhase());
			}
		}
	}

	public static final double butterworthLowPassFunction(int n, double radius,
			double r) {
		double p = Math.pow(r / radius, 2.0 * n);
		return 1.0 / (1.0 + p);
	}

	public static final double butterworthHighPassFunction(int n,
			double radius, double r) {
		try {
			double p = Math.pow(radius / r, 2.0 * n);
			return 1.0 / (1.0 + p);
		} catch (ArithmeticException e) {
			return 0.0;
		}
	}
	
	// Private Calculate function
	private static int powerOfTwo(int n) {
		int i = 0, m = 1;
		while (m < n) {
			m <<= 1;
			++i;
		}
		return i;
	}

	private float calculateMagnitudes(float[] mag) {
		float maximum = 0.0f;
		for (int i = 0; i < data.length; ++i) {
			mag[i] = data[i].getMagnitude();
			if (mag[i] > maximum)
				maximum = mag[i];
		}
		return maximum;
	}

	private static final int shift(int d, int d2) {
		return (d >= d2 ? d - d2 : d + d2);
	}

}