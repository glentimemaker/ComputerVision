import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class EqualizeImage {
	public EqualizeImage(){
		
	}
	
	public static void main(String[] argv) throws IOException{
		BufferedImage image = ImageIO.read(new File(argv[0]));
		int[] hist_image = new int[256];
		hist_image = getHistogram(image);
		BufferedImage image_hist = convertToImage(hist_image);
		File outputfile1 = new File("HistImage.jpg");
		ImageIO.write(image_hist, "jpg", outputfile1);
		System.out.println("Histogram of Image saved!");
		
		BufferedImage image_equalized = equalizeImage(hist_image, image);
		saveImage(image_equalized);
		System.out.println("Image Equalization finished!");
		
		int[] hist_image_equal = new int[256];
		hist_image_equal = getHistogram(image_equalized);
		
		BufferedImage image_equal_hist = convertToImage(hist_image_equal);
		File outputfile3 = new File("HistEqualizedImg.jpg");
		ImageIO.write(image_equal_hist, "jpg", outputfile3);
		System.out.println("Histogram of Equalized Image saved!");
		
	}
	
	private static BufferedImage convertToImage(int[] hist_image) {
		
		int max=0;
		for (int i=0; i<hist_image.length; i++){
		    if ( max < hist_image[i] ) {
		        max = hist_image[i];
		    }
		}
		BufferedImage image_hist = new BufferedImage(256, (int) Math.floor(max/6), BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster raster = image_hist.getRaster();
		for(int w = 0; w<image_hist.getWidth(); w++)
		{
			for(int h = 0 ; h<image_hist.getHeight(); h++){
				if (h<Math.ceil(max/6-hist_image[w]/6)){
					raster.setSample(w, h, 0, 255);
				}
				else{
					raster.setSample(w, h, 0, 0);
				}
									
			}
				
		}
		
		return image_hist;
	}
	
	private static void saveImage(BufferedImage image) throws IOException {
		File outputfile = new File("EqualizedImg.jpg");
		ImageIO.write(image, "jpg", outputfile);
		
	}
	
	private static BufferedImage equalizeImage(int[] hist_image, BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage image_equal = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster raster = image_equal.getRaster();
		Raster raster_image = image.getRaster();
		int L = 256;
		float a = (float)(255.0/(height*width));
		long sumr = 0;
		long[] Hc = new long[L];
		
		Hc[0] = (long) (a*hist_image[0]);
		for (int i = 0; i < L; i ++){
			//System.out.println(hist_image[i]);
			sumr += hist_image[i];
	        int valr = (int) (sumr * a);
	        if(valr > 255) {
	            Hc[i] = 255;
	        }
	        else Hc[i] = valr;
	        //System.out.println(Hc[i]);
		}
		
		for (int y = 0; y < height; y++){
			for (int x= 0; x < width; x++){
				int index = raster_image.getSample(x, y, 0);
				raster.setSample(x, y, 0, Hc[index]);
			}
		}
		
		return image_equal;
	}
	
	private static int[] getHistogram(BufferedImage image) {
		int height = image.getHeight();
        int width = image.getWidth();
        int[] bins = new int[256];
        
        Raster raster = image.getRaster();
        for(int i=0; i < width ; i++)
        {
            for(int j=0; j < height ; j++)
            {
               
				int hist = raster.getSample(i,j, 0);
				bins[hist]++;
                
            }
        }
        return bins;
	}

}
