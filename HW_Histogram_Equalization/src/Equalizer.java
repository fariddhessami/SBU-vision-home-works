import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Equalizer {

    public static int grayScaleRange = 256;
    public static int[] numPerIntensity;
    public static ArrayList<Pair> arrOfPairsPerIntense[];
    public static int mean;

    public static ArrayList<Integer> aboveTheMean;
    public static ArrayList<Integer> aroundTheMean;
    public static ArrayList<Integer> belowTheMean;

    public static void main(String[] args) throws IOException {



        File file = new File("./picture-example-Low.png");
        BufferedImage img = ImageIO.read(file);
        int width = img.getWidth();
        int height = img.getHeight();

        int numOfPixels = width*height;

        mean =  numOfPixels /  grayScaleRange;
        System.out.println("mean : " + mean);

        int[][] imgArr = new int[width][height];

        numPerIntensity = new int[grayScaleRange];


        arrOfPairsPerIntense = new ArrayList[grayScaleRange];
        for (int i = 0; i <grayScaleRange ; i++) {
            arrOfPairsPerIntense[i] = new ArrayList<Pair>();
        }

        aboveTheMean = new ArrayList<>();
        aroundTheMean = new ArrayList<>();
        belowTheMean = new ArrayList<>();

        Raster raster = img.getData();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int intensity = raster.getSample(i, j, 0);
                imgArr[i][j] = intensity;
                numPerIntensity[intensity]++;

                Pair currentPair = new Pair(i,j);
                Pair_Value currentPv = new Pair_Value(intensity,currentPair);

                arrOfPairsPerIntense[intensity].add(currentPair);

//                DetermineGroupAndAdd(currentPv);
            }
        }

        determineStatusOfGroups();

        SortArrays();

        logGroupSizes();
        logEntireGroups();

        while (aboveTheMean.size()<=20) {
            if (belowTheMean.size()>0 && aboveTheMean.size()>0){
                //select one of the group "the above the mean"
                int selectedAboveMean = aboveTheMean.get(0);

                //scan for one of the group "the below the mean"
                int selectedBelowMean =
                        belowTheMean.get(belowTheMean.size()-1);

                swapRandomPixel(selectedAboveMean,selectedBelowMean);

            }
        }

        logGroupSizes();

        logEntireGroups();

    }//main

    private static void determineStatusOfGroups() {

        for (int i = 0; i <numPerIntensity.length ; i++) {
            int intenseFreq = numPerIntensity[i];

            if (intenseFreq>(mean+1)){
                aboveTheMean.add(i);
            }else if (intenseFreq<mean){
                belowTheMean.add(i);
            }else{
                aroundTheMean.add(i);
            }
        }
    }

    private static void SortArrays() {
        aboveTheMean.sort(Integer::compareTo);
        aroundTheMean.sort(Integer::compareTo);
        belowTheMean.sort(Integer::compareTo);
    }


    public static void swapRandomPixel(int startIntense,int destIntense){

        Random rand = new Random();
        int randomPixelIndex = rand.nextInt
                (arrOfPairsPerIntense[startIntense].size());




        numPerIntensity[startIntense]--;
        numPerIntensity[destIntense]++;

        Random rand = new Random();
        int randomNum = rand.nextInt(a_array.size());


        // so we should get a pixel

        Pair_Value aPair = a_array.remove(randomNum);

        int xValue = aPair.value;
        int yValue = bPair.value;

        aPair.value = yValue;
        bPair.value = xValue;

        DetermineGroupAndAdd(aPair);
        DetermineGroupAndAdd(bPair);


        SortArrays();
    }

    private static void DetermineGroupAndAdd(Pair_Value aPair) {

        //this is wrong we should've look at histogram

        int intensity = aPair.value;

        if (intensity>(mean+1)){
            aboveTheMean.add(aPair);
        }else if (intensity<mean){
            belowTheMean.add(aPair);
        }else{
            aroundTheMean.add(aPair);
        }

    }

    public static void logGroupSizes() {
        System.out.println(aboveTheMean.size()+"aboveTheMean.size()");
        System.out.println(aroundTheMean.size()+"aroundTheMean.size()");
        System.out.println(belowTheMean.size()+"belowTheMean.size()");
    }

    public static void logEntireGroups() {
        for (int i = 0; i <aboveTheMean.size() ; i++) {
            System.out.println("above i : "+ i + " : " + aboveTheMean.get(i));
        }
        for (int i = 0; i <aroundTheMean.size() ; i++) {
            System.out.println("around i : "+ i + " : " + aroundTheMean.get(i));
        }
        for (int i = 0; i <belowTheMean.size() ; i++) {
            System.out.println("below i : "+ i + " : " + belowTheMean.get(i));
        }

    }

//    public static void swapRandomPixel
//            (int startIntense,int destIntense) throws Exception {
//
//        int startIntenseSize = arrOfPairsPerIntense[startIntense].size();
//        int destIntenseSize = arrOfPairsPerIntense[destIntense].size();
//
//        if(startIntense<=0){
//            throw new Exception("error in start size");
//        }
//
//        numPerIntensity[startIntense]--;
//        numPerIntensity[destIntense]++;
//
//        Random rand = new Random();
//
//        float randomNum = rand.nextInt(startIntenseSize);
//
//        System.out.println("randomNum : " + randomNum);
//
//    }

    public static void EqualizeForIntense(int intesity) throws Exception {

    }

}

class Pair{
    public int x;
    public int y;

    public Pair(int i, int j) {
        this.x=x;
        this.x=x;
    }
}

class Pair_Value{
    public int value;
    public Pair pair;

    public Pair_Value(int value,Pair pair){
        this.value = value;
        this.pair = pair;
    }

    public int compareTo(Pair_Value pv) {
        return Integer.compare(pv.value,this.value);
    }
}