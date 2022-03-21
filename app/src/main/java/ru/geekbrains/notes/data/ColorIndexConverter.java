package ru.geekbrains.notes.data;

import java.util.Observer;
import java.util.Random;

import ru.geekbrains.notes.R;

public class ColorIndexConverter {
    private static Random random = new Random();
    private static Object object = new Object();

    private static int[] colorIndex = {
            R.color.blue_100,
            R.color.green_100,
            R.color.mint_100
    };

    public static int randomColorIndex(){
        synchronized (object){
            return random.nextInt(colorIndex.length);
        }
    }

    public static int getColorByIndex(int index){
        if (index < 0 || index >= colorIndex.length)
            index = 0;
        return colorIndex[index];
    }

    public static int getIndexByColor(int color){
        for (int i = 0; i < colorIndex.length; i++){
            if (colorIndex[i] == color) {
                return i;
            }
        }
        return 0;
    }
}
