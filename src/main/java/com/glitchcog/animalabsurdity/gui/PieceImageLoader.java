package com.glitchcog.animalabsurdity.gui;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.LookupOp;
import java.awt.image.ShortLookupTable;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.glitchcog.animalabsurdity.model.PieceAnimal;
import com.glitchcog.animalabsurdity.model.PieceColor;

public class PieceImageLoader
{
    private static ShortLookupTable swapTable;
    private static BufferedImageOp swapOp;

    private static Map<String, BufferedImage> imageCache;

    private PieceImageLoader()
    {
    }

    static
    {
        imageCache = new HashMap<String, BufferedImage>();
        short[][] lookupArray = new short[4][256];
        for (int i = 0; i < lookupArray.length; i++)
        {
            for (short c = 0; c < lookupArray[i].length; c++)
            {
                lookupArray[i][c] = c;
            }
        }
        swapTable = new ShortLookupTable(0, lookupArray);
        swapOp = new LookupOp(swapTable, null);
    }

    public static BufferedImage loadImage(PieceAnimal animal, PieceColor color) throws IOException
    {
        final String key = getKey(animal, color);
        BufferedImage image = imageCache.get(key);

        if (image == null)
        {
            final String classPathUrlStr = animal.getFilename();
            URL url = PieceImageLoader.class.getClassLoader().getResource(classPathUrlStr);

            BufferedImage bi = ImageIO.read(url);
            if (bi == null)
            {
                throw new IOException("Null image");
            }

            Color fillColor = color.getColor();
            for (short i = 0; i < 256; i++)
            {
                swapTable.getTable()[0][i] = (short) ((i / 255.0f) * fillColor.getRed());
                swapTable.getTable()[1][i] = (short) ((i / 255.0f) * fillColor.getGreen());
                swapTable.getTable()[2][i] = (short) ((i / 255.0f) * fillColor.getBlue());
            }
            ColorModel cm = bi.getColorModel();
            WritableRaster raster = bi.copyData(null);
            image = new BufferedImage(cm, raster, cm.isAlphaPremultiplied(), null);
            image = swapOp.filter(bi, image);

            imageCache.put(key, image);
        }

        return image;
    }

    private static String getKey(PieceAnimal animal, PieceColor color)
    {
        return "" + color + " " + animal;
    }
}