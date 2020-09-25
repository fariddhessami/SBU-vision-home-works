import java.util.Scanner;
import java.util.Random;
import java.lang.Math;

public class Question_1 {
    public static void main(String[] args) {

        int grayScaleRange = 256;
        int frameDimensionSize = 20;

        Scanner sc = new Scanner(System.in);

        System.out.println("grayScaleRange ? ");
        grayScaleRange = sc.nextInt();
        System.out.println("grayScaleRange : " + grayScaleRange);

        System.out.println("frameDimensionSize ?");
        frameDimensionSize = sc.nextInt();
        System.out.println("frameDimensionSize : " + frameDimensionSize);



//        System.out.println("Combination : " + Combination(frameDimensionSize,grayScaleRange));

        Random rand = new Random();

        float randomNum = rand.nextFloat();

        System.out.println("randomNum : " + randomNum);


        MakeRandomIntensityHistogram(frameDimensionSize,grayScaleRange,rand);

    }

    public static int Combination(int r,int n){

        if(r>n || r<0 || n<0)
            return -7;

        System.out.println("Factorial of n : "+ n +" is : " + Factorial(n));
        System.out.println("Factorial of n-r : "+ (n-r) +" is : " + Factorial(n-r));
        System.out.println("Factorial of r : "+ r +" is : " + Factorial(r));



        return Factorial(n)/(Factorial(n-r)*Factorial(r));
    }

    public static int Factorial(int r) {

        if(r<0)
            return -1;
        else if(r==0 || r==1)
            return 1;
        else
            return r*Factorial(r-1);
    }

    //use Factorial map


    public static int[] MakeRandomIntensityHistogram
            (int dimensionSize, int grayScaleRange ,Random randomFunc){

        int numOfPixelsLeft = dimensionSize*dimensionSize;
        int[] resultArray = new int[grayScaleRange];


        System.out.println("numOfPixelsLeft : "+ numOfPixelsLeft);

        while (numOfPixelsLeft>0){

            System.out.println("while loop = numOfPixelsLeft : " + numOfPixelsLeft);

            // select a random intensity

            int randomIntense = randomFunc.nextInt(grayScaleRange+1);

//            int randomIntense = (int) Math.floor(randomFunc.nextDouble()*grayScaleRange);

            System.out.println("selected for intensity : "+ randomIntense);

            // select a random frequency
            int randomFrequency= randomFunc.nextInt(numOfPixelsLeft+1);

            System.out.println("selected for frequency : "+ randomFrequency);

            numOfPixelsLeft = numOfPixelsLeft - randomFrequency;

            System.out.println("numOfPixelsLeft : "+ numOfPixelsLeft);


            resultArray[randomIntense] = randomFrequency;
        }


        System.out.println("result array : ");
        for (int i = 0; i <resultArray.length ; i++) {
            System.out.println("i : " + i + " : " + resultArray[i]);
        }

        return resultArray;
    }

    public static int CountVariationsInOneFrame
            (int[] intensityArray,int frameDimensionSize){

        return -1;
    }
}
