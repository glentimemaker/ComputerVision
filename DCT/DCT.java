public class DCT
	{
	    //Block size
	    public static int N        = 8;
	    
	    //zigZag index matrix
	    public static int [][]zigZag = new int[64][2];

	    //1D coefficient array
	    public static int []sequence = new int[N*N];
	    //coefficient array index, used to compress redundancy values
	    static int index = N*N;
	    
	    public static double [][]c        = new double[N][N];
	    // Transformed cosine matrix, N*N.
	    public static double [][]cT       = new double[N][N];

	    //Result matrix
	    public static int [][]resultDCT = new int[N][N];
	    
	    //Quantization matrix
	    public static int [][]quantum     = 
	    	{{16,11,10,16,26,40,51,61},
		    {12,12,14,19,26,58,60,55},
		    {14,13,16,24,40,57,69,56},
		    {14,17,22,29,51,87,80,62},
		    {18,22,37,56,68,109,103,77},
		    {24,35,55,64,81,104,113,92},
		    {49,64,78,87,103,121,120,101},
		    {72,92,95,98,112,100,103,99}};

	    //Original image
	    public static int [][]image = {
	    	{56,45,51,66,70,61,64,73},
	    	{63,59,56,90,109,85,69,72},
	    	{62,59,68,103,144,104,66,73},
	    	{63,58,71,132,134,106,70,69},
	    	{65,61,68,114,116,82,68,70},
	    	{79,65,60,67,77,68,58,75},
	    	{85,71,54,59,55,61,65,73},
	    	{87,79,69,58,65,72,78,96}
	    };
	    
	    /*
	     * Main Part
	     */
	    public DCT()
	    {
	        initZigZag();
	        initMatrix();
	    }

	    public static void main(String[] args){
	    	DCT jpegCompress = new DCT();
	    	
	    	System.out.print("The original image is: \n");
	    	for(int i=0; i<N; i++){
	    		for(int j=0; j<N; j++){
	    			System.out.format("%5d"+" ",image[i][j]);
	    		}
	    		System.out.print("\n");
	    	}
	    	System.out.print("\n");
	    	
	    	//ForwardDCT to the image
	    	resultDCT = forwardDCT(image);
	    	System.out.print("The result after discrete cosine transform:\n");
	    	for(int i=0; i<N; i++){
	    		for(int j=0; j<N; j++){
	    			System.out.format("%5d"+" ",resultDCT[i][j]);
	    		}
	    		System.out.print("\n");
	    	}
	    	System.out.print("\n");
	    	
	    	//Quantize the result
	    	resultDCT = quantizeImage(resultDCT);
	    	System.out.print("Quantize the result: \n");
	    	for(int i=0; i<N; i++){
	    		for(int j=0; j<N; j++){
	    			System.out.format("%5d"+" ", resultDCT[i][j]);
	    		}
	    		System.out.print("\n");
	    	}
	    	System.out.print("\n");
	    	
	    	//ZigZag the result
	    	sequence = zigzagResult(resultDCT);
	    	
	    	//Compress the result
	    	compress();
	    	
	    	System.out.print("Zig zag the matrix into 1D sequence and compress it: \n");
	    	for(int i=0; i<index+1; i++){
	    		System.out.format("%5d"+" ", sequence[i]);
	    	}
	    	System.out.print("\n");
	    	
	    }

	    //sets up the Cosine Transform Matrix and the Transposed CT.
	    private void initMatrix()
	    {
	        int i;
	        int j;

	        for (j = 0; j < N; j++)
	        {
	            double nn = (double)(N);
	            c[0][j]  = 1.0 / Math.sqrt(nn);
	            cT[j][0] = c[0][j];
	        }

	        for (i = 1; i < 8; i++)
	        {
	            for (j = 0; j < 8; j++)
	            {
	                double jj = (double)j;
	                double ii = (double)i;
	                c[i][j]  = Math.sqrt(2.0/8.0) * Math.cos(((2.0 * jj + 1.0) * ii * Math.PI) / (2.0 * 8.0));
	                cT[j][i] = c[i][j];
	            }
	        }
	    }

	   
	    //Initializes the ZigZag matrix, provide the index
	    private void initZigZag()
	    {
	        zigZag[0][0] = 0; // 0,0
	        zigZag[0][1] = 0;
	        zigZag[1][0] = 0; // 0,1
	        zigZag[1][1] = 1;
	        zigZag[2][0] = 1; // 1,0
	        zigZag[2][1] = 0;
	        zigZag[3][0] = 2; // 2,0
	        zigZag[3][1] = 0;
	        zigZag[4][0] = 1; // 1,1
	        zigZag[4][1] = 1;
	        zigZag[5][0] = 0; // 0,2
	        zigZag[5][1] = 2;
	        zigZag[6][0] = 0; // 0,3
	        zigZag[6][1] = 3;
	        zigZag[7][0] = 1; // 1,2
	        zigZag[7][1] = 2;
	        zigZag[8][0] = 2; // 2,1
	        zigZag[8][1] = 1;
	        zigZag[9][0] = 3; // 3,0
	        zigZag[9][1] = 0;
	        zigZag[10][0] = 4; // 4,0
	        zigZag[10][1] = 0;
	        zigZag[11][0] = 3; // 3,1
	        zigZag[11][1] = 1;
	        zigZag[12][0] = 2; // 2,2
	        zigZag[12][1] = 2;
	        zigZag[13][0] = 1; // 1,3
	        zigZag[13][1] = 3;
	        zigZag[14][0] = 0; // 0,4
	        zigZag[14][1] = 4;
	        zigZag[15][0] = 0; // 0,5
	        zigZag[15][1] = 5;
	        zigZag[16][0] = 1; // 1,4
	        zigZag[16][1] = 4;
	        zigZag[17][0] = 2; // 2,3
	        zigZag[17][1] = 3;
	        zigZag[18][0] = 3; // 3,2
	        zigZag[18][1] = 2;
	        zigZag[19][0] = 4; // 4,1
	        zigZag[19][1] = 1;
	        zigZag[20][0] = 5; // 5,0
	        zigZag[20][1] = 0;
	        zigZag[21][0] = 6; // 6,0
	        zigZag[21][1] = 0;
	        zigZag[22][0] = 5; // 5,1
	        zigZag[22][1] = 1;
	        zigZag[23][0] = 4; // 4,2
	        zigZag[23][1] = 2;
	        zigZag[24][0] = 3; // 3,3
	        zigZag[24][1] = 3;
	        zigZag[25][0] = 2; // 2,4
	        zigZag[25][1] = 4;
	        zigZag[26][0] = 1; // 1,5
	        zigZag[26][1] = 5;
	        zigZag[27][0] = 0; // 0,6
	        zigZag[27][1] = 6;
	        zigZag[28][0] = 0; // 0,7
	        zigZag[28][1] = 7;
	        zigZag[29][0] = 1; // 1,6
	        zigZag[29][1] = 6;
	        zigZag[30][0] = 2; // 2,5
	        zigZag[30][1] = 5;
	        zigZag[31][0] = 3; // 3,4
	        zigZag[31][1] = 4;
	        zigZag[32][0] = 4; // 4,3
	        zigZag[32][1] = 3;
	        zigZag[33][0] = 5; // 5,2
	        zigZag[33][1] = 2;
	        zigZag[34][0] = 6; // 6,1
	        zigZag[34][1] = 1;
	        zigZag[35][0] = 7; // 7,0
	        zigZag[35][1] = 0;
	        zigZag[36][0] = 7; // 7,1
	        zigZag[36][1] = 1;
	        zigZag[37][0] = 6; // 6,2
	        zigZag[37][1] = 2;
	        zigZag[38][0] = 5; // 5,3
	        zigZag[38][1] = 3;
	        zigZag[39][0] = 4; // 4,4
	        zigZag[39][1] = 4;
	        zigZag[40][0] = 3; // 3,5
	        zigZag[40][1] = 5;
	        zigZag[41][0] = 2; // 2,6
	        zigZag[41][1] = 6;
	        zigZag[42][0] = 1; // 1,7
	        zigZag[42][1] = 7;
	        zigZag[43][0] = 2; // 2,7
	        zigZag[43][1] = 7;
	        zigZag[44][0] = 3; // 3,6
	        zigZag[44][1] = 6;
	        zigZag[45][0] = 4; // 4,5
	        zigZag[45][1] = 5;
	        zigZag[46][0] = 5; // 5,4
	        zigZag[46][1] = 4;
	        zigZag[47][0] = 6; // 6,3
	        zigZag[47][1] = 3;
	        zigZag[48][0] = 7; // 7,2
	        zigZag[48][1] = 2;
	        zigZag[49][0] = 7; // 7,3
	        zigZag[49][1] = 3;
	        zigZag[50][0] = 6; // 6,4
	        zigZag[50][1] = 4;
	        zigZag[51][0] = 5; // 5,5
	        zigZag[51][1] = 5;
	        zigZag[52][0] = 4; // 4,6
	        zigZag[52][1] = 6;
	        zigZag[53][0] = 3; // 3,7
	        zigZag[53][1] = 7;
	        zigZag[54][0] = 4; // 4,7
	        zigZag[54][1] = 7;
	        zigZag[55][0] = 5; // 5,6
	        zigZag[55][1] = 6;
	        zigZag[56][0] = 6; // 6,5
	        zigZag[56][1] = 5;
	        zigZag[57][0] = 7; // 7,4
	        zigZag[57][1] = 4;
	        zigZag[58][0] = 7; // 7,5
	        zigZag[58][1] = 5;
	        zigZag[59][0] = 6; // 6,6
	        zigZag[59][1] = 6;
	        zigZag[60][0] = 5; // 5,7
	        zigZag[60][1] = 7;
	        zigZag[61][0] = 6; // 6,7
	        zigZag[61][1] = 7;
	        zigZag[62][0] = 7; // 7,6
	        zigZag[62][1] = 6;
	        zigZag[63][0] = 7; // 7,7
	        zigZag[63][1] = 7;
	    }

	    //DCT process
	    public static int[][] forwardDCT(int[][] image)
	    {
	        int output[][] = new int[N][N];
	        double temp[][] = new double[N][N];
	        double temp1;
	        int i;
	        int j;
	        int k;

	        for (i = 0; i < N; i++)
	        {
	            for (j = 0; j < N; j++)
	            {
	                temp[i][j] = 0.0;
	                for (k = 0; k < N; k++)
	                {
	                    temp[i][j] += (((int)(image[i][k]) - 128) * cT[k][j]);
	                }
	            }
	        }

	        for (i = 0; i < N; i++)
	        {
	            for (j = 0; j < N; j++)
	            {
	                temp1 = 0.0;

	                for (k = 0; k < N; k++)
	                {
	                    temp1 += (c[i][k] * temp[k][j]);
	                }

	                output[i][j] = (int)Math.round(temp1);
	            }
	        }

	        return output;
	    }

	    
	    //Quantization
	    public static int[][] quantizeImage(int inputData[][])
	    {
	        int outputData[][] = new int[N][N];
	        int i = 0;

	        for (i = 0; i < N; i++)
	            {
	                for(int j=0; j<N; j++){
		                outputData[i][j] = (int)(inputData[i][j] / quantum[i][j]);
	                }
	        		
	            }

	        return outputData;
	    }
	    
	    //Zig zag the result
	    public static int[] zigzagResult(int [][]resultDCT){
	    	
	    	int row;
	    	int col;
	    	
	    	for(int i=0; i<N*N; i++){
	    			row = zigZag[i][0];
	    			col = zigZag[i][1];
	    			sequence[i]= resultDCT[row][col];
	    	}
	    	
			return sequence;
	    }

	  
	    //Get the last index of the point which has value rather than 0
	    public static void compress(){
	    	
	        for(int i=N*N-1; i>-1; i--){
	        	index--;
	        	if(sequence[i]!=0){
	        		break;
	        	}
	        }
	        
	    }   

	}
