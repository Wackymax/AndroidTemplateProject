package template.entelect.co.za.template.common.color;

import android.graphics.Color;
import android.support.v4.graphics.ColorUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by hennie.brink on 2017/03/02.
 */

public class HarmonyColorGenerator {


    private static final Random random = new Random();

    public static Integer generateGoldenRatio(long seed) {

        return generateGoldenRatio(seed, 180, 0.2f, 0.3f);
    }

    public static Integer generateGoldenRatio(long seed, int startingRadius, float saturationVariance, float luminanceVariance) {

        double radius = (seed * 137.5) + startingRadius;

        radius = (int) (Math.asin(Math.sin(radius)) * 100);

        float newSaturation = saturationVariance + (random.nextFloat() - 0.5f) * saturationVariance;
        float newLuminance = luminanceVariance + (random.nextFloat() - 0.5f) * luminanceVariance;

        return ColorUtils.HSLToColor(new float[]{(float) radius, newSaturation, newLuminance});
    }

    public static Integer generateRandomPastel() {
        return generateRandomFromMix(Color.WHITE);
    }

    public static Integer generateRandomFromMix(Integer mix) {
        Random random = new Random();
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);

        // mix the color
        if (mix != null) {
            red = (red + mix >> 0x0) / 2;
            green = (green + mix >> 0x2) / 2;
            blue = (blue + mix >> 0x4) / 2;
        }
        return Color.argb(255, red, green, blue);
    }

    public static Integer generateColor() {

        return generateColor(random.nextLong());
    }

    public static Integer generateColor(long seed) {

        return generateColors(1, seed).get(0);
    }

    public static List<Integer> generateColors(int colorCount, long seed) {

        Random random = new Random(seed);
        List<Integer> colors = new ArrayList<>();

        float referenceAngle = 60;

        for (int i = 0; i < colorCount; i++) {
            float randomAngle = random.nextFloat() * 250;
            //exclude pink colors
            while(randomAngle > 290 && randomAngle < 335){
                randomAngle = random.nextFloat() * 250;
            }

            float newSaturation = 0.3f + random.nextFloat() * 0.7f;
            float newLuminance = 0.3f + random.nextFloat() * 0.4f;

            int color = ColorUtils.HSLToColor(new float[]{(referenceAngle + randomAngle), newSaturation, newLuminance});
            color = ColorUtils.setAlphaComponent(color, 100);
            colors.add(color);
        }

        return colors;
        //return generateColorsHarmony2(colorCount, 150, -60, 130, 200, 0, 1f, 0f, 0.5f, 0.2f);
    }

    public static List<Integer> generateSequentialColors(int colorCount, int variance) {

        List<Integer> colors = new ArrayList<>();

        if (colorCount <= 0)
            colorCount = 1;

        float spacing = ((360f - variance) / colorCount);

        for (int i = 0; i < colorCount; i++) {
            float randomAngle = (spacing * i) + random.nextInt(variance);

            float newSaturation = 0.3f + random.nextFloat() * 0.7f;
            float newLuminance = 0.3f + random.nextFloat() * 0.4f;

            int color = ColorUtils.HSLToColor(new float[]{(randomAngle), newSaturation, newLuminance});
            color = ColorUtils.setAlphaComponent(color, 100);
            colors.add(color);
        }

        return colors;
        //return generateColorsHarmony2(colorCount, 150, -60, 130, 200, 0, 1f, 0f, 0.5f, 0.2f);
    }

    public static List<Integer> generateColorsHarmony(
            int colorCount,
            float offsetAngle1,
            float offsetAngle2,
            float rangeAngle0,
            float rangeAngle1,
            float rangeAngle2,
            float saturation, float luminance) {
        List<Integer> colors = new ArrayList<Integer>();

        float referenceAngle = random.nextFloat() * 360;

        for (int i = 0; i < colorCount; i++) {
            float randomAngle = random.nextFloat() * (rangeAngle0 + rangeAngle1 + rangeAngle2);

            if (randomAngle > rangeAngle0) {
                if (randomAngle < rangeAngle0 + rangeAngle1) {
                    randomAngle += offsetAngle1;
                } else {
                    randomAngle += offsetAngle2;
                }
            }

            int color = ColorUtils.HSLToColor(new float[]{((referenceAngle + randomAngle) / 360.0f) % 1.0f, saturation, luminance});
            colors.add(color);
        }

        return colors;
    }

    public static List<Integer> generateColorsHarmony2(
            int colorCount,
            float offsetAngle1,
            float offsetAngle2,
            float rangeAngle0,
            float rangeAngle1,
            float rangeAngle2,
            float saturation, float saturationRange,
            float luminance, float luminanceRange) {
        List<Integer> colors = new ArrayList<>();

        float referenceAngle = random.nextFloat() * 360;

        for (int i = 0; i < colorCount; i++) {
            float randomAngle = random.nextFloat() * (rangeAngle0 + rangeAngle1 + rangeAngle2);

            if (randomAngle > rangeAngle0) {
                if (randomAngle < rangeAngle0 + rangeAngle1) {
                    randomAngle += offsetAngle1;
                } else {
                    randomAngle += offsetAngle2;
                }
            }

            float newSaturation = saturation + (random.nextFloat() - 0.5f) * saturationRange;
            float newLuminance = luminance + (random.nextFloat() - 0.5f) * luminanceRange;

            int color = ColorUtils.HSLToColor(new float[]{(referenceAngle + randomAngle), newSaturation, newLuminance});
            colors.add(color);
        }

        return colors;
    }
}
