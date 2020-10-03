import javax.imageio.ImageIO;
import java.awt.*;
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

    public static IntensityStatus statusPerIntensity[];

    public static  int[][] resultImgArr;

    public static int wi,hi;

    public static void main(String[] args) throws IOException {



        File file = new File("./picture-example-Low.png");
        BufferedImage img = ImageIO.read(file);
        int width = img.getWidth();
        int height = img.getHeight();

        wi=width;
        hi=height;

        int numOfPixels = width*height;

        mean =  numOfPixels /  grayScaleRange;
        System.out.println("mean : " + mean);

        int[][] imgArr = new int[width][height];

        resultImgArr = new int[width][height];

        numPerIntensity = new int[grayScaleRange];


        arrOfPairsPerIntense = new ArrayList[grayScaleRange];
        for (int i = 0; i <grayScaleRange ; i++) {
            arrOfPairsPerIntense[i] = new ArrayList<Pair>();
        }

        aboveTheMean = new ArrayList<>();
        aroundTheMean = new ArrayList<>();
        belowTheMean = new ArrayList<>();

        statusPerIntensity = new IntensityStatus[grayScaleRange];

        Raster raster = img.getData();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int intensity = raster.getSample(i, j, 0);
                imgArr[i][j] = intensity;
                numPerIntensity[intensity]++;

                Pair currentPair = new Pair(i,j);
                Pair_Value currentPv = new Pair_Value(intensity,currentPair);

                arrOfPairsPerIntense[intensity].add(currentPair);
                System.out.println("salam 1234");



//                DetermineGroupAndAdd(currentPv);
            }
        }

        determineStatusOfGroups();

        SortArrays();

        System.out.println("before :");
//        logEntireGroups();
        logGroupSizes();
        logSumofGroupSizes();
        System.out.println("before 2:");


        int loops = grayScaleRange;
        int subLoops = 100000;

        for (int i = 0; i <loops ; i++) {
            for (int j = 0; j <subLoops ; j++) {
                if (belowTheMean.size()>0 && aboveTheMean.size()>0){
                    Random random = new Random();
                    int randomAboveIndex = random.nextInt(aboveTheMean.size());
                    int randomAbove = aboveTheMean.get(randomAboveIndex);
                    int answerBelowMean = findNearBelowMean(randomAbove,i+1);
                    if (answerBelowMean !=-1){
                        int answerIndex = belowTheMean.indexOf(answerBelowMean);
                        belowTheMean.remove(answerIndex);
                        aboveTheMean.remove(randomAboveIndex);
                        swapRandomPixel(randomAbove,answerBelowMean);
                    }
                }
            }
        }


//        while (loops!=0) {
//            //seems working... now we need a better logic
//            if (belowTheMean.size()>0 && aboveTheMean.size()>0){
//                //select one of the group "the above the mean"
//                int selectedAboveMean = aboveTheMean.remove(aboveTheMean.size()-1);
//
//                //scan for one of the group "the below the mean"
//                int selectedBelowMean =
//                        belowTheMean.remove(0);
//
//                swapRandomPixel(selectedAboveMean,selectedBelowMean);
//
//            }
//            loops--;
//        }

        System.out.println("after :");
        logGroupSizes();
//        logEntireGroups();
        logEntireStatusOfGroups();




        renderResultImage();
        writeImage();

    }//main

    private static void writeImage() throws IOException {
        BufferedImage resBufImg = new
                BufferedImage(resultImgArr.length,
                resultImgArr[0].length,
                BufferedImage.TYPE_INT_ARGB);

        for (int i = 0; i <resultImgArr.length ; i++) {
            for (int j = 0; j <resultImgArr[0].length ; j++) {
                Color gray = new Color(resultImgArr[i][j],
                        resultImgArr[i][j], resultImgArr[i][j]);
                resBufImg.setRGB(i,j,gray.getRGB());

            }
        }

        try {
            File f = new File("picture-result.png");
            ImageIO.write(resBufImg, "png", f);
            System.out.println("alo! " + resultImgArr.length  + " , " + resultImgArr[0].length);
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }

    private static void determineStatusOfGroups() {

        for (int i = 0; i <numPerIntensity.length ; i++) {
            determineIntensityGroupStatus(i);
        }
    }

    private static void SortArrays() {
        aboveTheMean.sort((o1, o2) -> {return Integer.compare(numPerIntensity[((int) o1)],
            numPerIntensity[((int) o2)]);});
        aroundTheMean.sort((o1, o2) -> {return Integer.compare(numPerIntensity[((int) o1)],
                numPerIntensity[((int) o2)]);});
        belowTheMean.sort((o1, o2) -> {return Integer.compare(numPerIntensity[((int) o1)],
                numPerIntensity[((int) o2)]);});
    }


    public static void swapRandomPixel(int startIntense, int destIntense){

//        System.out.println("so we wanna swap : start intensity : " +
//                startIntense + " with freq : " + numPerIntensity[startIntense] +
//                " dest intensity : " + destIntense  + " with freq : " + numPerIntensity[destIntense]);

        Random rand = new Random();
        int randomPixelIndex = rand.nextInt
                (arrOfPairsPerIntense[startIntense].size());

        Pair randomPixel =
                arrOfPairsPerIntense[startIntense].remove(randomPixelIndex);
        arrOfPairsPerIntense[destIntense].add(randomPixel);

        System.out.println("behold : pixel pair : "+ randomPixel.x + " , " + randomPixel.y);

        numPerIntensity[startIntense]--;
        numPerIntensity[destIntense]++;


        determineIntensityGroupStatus(startIntense);
        determineIntensityGroupStatus(destIntense);

        logSumofGroupSizes();

        SortArrays();
    }

    public static void renderResultImage() {
        System.out.println("rendering : ...");


        for (int i = 0; i <arrOfPairsPerIntense.length ; i++) {
            for (Pair pair:arrOfPairsPerIntense[i]) {

                int x = pair.x;
                int y = pair.y;



                resultImgArr[x][y] = i;


            }
        }
    }

    private static void determineIntensityGroupStatus(int intensityGroup) {

        int intenseFreq = numPerIntensity[intensityGroup];
        IntensityStatus iStatus;

        if (intenseFreq>(mean+1)){
            aboveTheMean.add(intensityGroup);
            iStatus = IntensityStatus.above;
        }else if (intenseFreq<mean){
            belowTheMean.add(intensityGroup);
            iStatus = IntensityStatus.below;
        }else{
            aroundTheMean.add(intensityGroup);
            iStatus = IntensityStatus.around;
        }

        statusPerIntensity[intensityGroup] = iStatus;

//        System.out.println("log : intensity : "+ intensityGroup +
//                " has been found : " + iStatus + " freq : "+intenseFreq);
//        logGroupSizes();
    }

    public static void logGroupSizes() {
        System.out.println("aboveTheMean.size() : " + aboveTheMean.size());
        System.out.println("aroundTheMean.size() : " + aroundTheMean.size());
        System.out.println("belowTheMean.size() : " + belowTheMean.size());
    }

    public static void logSumofGroupSizes() {
//        System.out.println("sum : " + (aboveTheMean.size()
//                + aroundTheMean.size() + belowTheMean.size()));
    }

    public static void logEntireGroups() {
        for (int i = 0; i <aboveTheMean.size() ; i++) {
            System.out.println("above i : "+ i + " intensity : " + aboveTheMean.get(i)
                    + " freq : " + numPerIntensity[aboveTheMean.get(i)]);
        }
        for (int i = 0; i <aroundTheMean.size() ; i++) {
            System.out.println("around i : "+ i + " intensity : " + aroundTheMean.get(i)
                    + " freq : " +  numPerIntensity[aroundTheMean.get(i)]);
        }
        for (int i = 0; i <belowTheMean.size() ; i++) {
            System.out.println("below i : "+ i + " intensity : "  + belowTheMean.get(i)
                    + " freq : " +  numPerIntensity[belowTheMean.get(i)]);
        }

    }


    public static int findNearBelowMean(int aboveIntense,int scanRadius) {

        int candidateIntense = -1;

        for (int i = 1; i <scanRadius+1 ; i++) {
            if( !((aboveIntense + i >=grayScaleRange) || (aboveIntense + i < 0)) ){
                if(statusPerIntensity[aboveIntense + i] == IntensityStatus.below){
                    candidateIntense = aboveIntense + i;
                }
            }

            if( !((aboveIntense - i >=grayScaleRange) || (aboveIntense - i < 0)) ){
                if(statusPerIntensity[aboveIntense - i] == IntensityStatus.below){
                    candidateIntense = aboveIntense - i;
                }
            }
        }

        return candidateIntense;
    }

    public static void logEntireStatusOfGroups() {
        for (int i = 0; i <statusPerIntensity.length ; i++) {
            System.out.println("intensity : " + i +
                    " status :" + statusPerIntensity[i] +
                    " freq : " + numPerIntensity[i]);
        }
    }

    public static void printAllPairs() {
        for (int i = 0; i <arrOfPairsPerIntense.length ; i++) {
            for (Pair pair:arrOfPairsPerIntense[i]) {
                System.out.println("a pair x "+pair.x+" y "+ pair.y + " intensity : " + i);
            }
        }
    }

}

class Pair{
    public int x;
    public int y;

    public Pair(int i, int j) {
        this.x=i;
        this.y=j;
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
        //this logic is wrong
        return Integer.compare(Equalizer.numPerIntensity[this.value],
                Equalizer.numPerIntensity[pv.value]);
//        return Integer.compare(pv.value,this.value);
    }
}

enum IntensityStatus {
    above,
    around,
    below
}