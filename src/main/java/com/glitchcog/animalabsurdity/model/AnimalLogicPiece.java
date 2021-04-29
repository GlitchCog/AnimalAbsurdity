package com.glitchcog.animalabsurdity.model;

import java.awt.image.BufferedImage;
import java.io.IOException;

import com.glitchcog.animalabsurdity.gui.PieceImageLoader;

public class AnimalLogicPiece
{
    private final PieceAnimal animal;
    private final PieceColor color;
    private final BufferedImage image;
    private boolean onBoard;
    private boolean deadEnd;

    public AnimalLogicPiece(final PieceAnimal animal, final PieceColor color) throws IOException
    {
        this.animal = animal;
        this.color = color;
        this.image = PieceImageLoader.loadImage(animal, color);
    }

    /**
     * Deep copy
     * 
     * @param animalLogicPiece
     */
    public AnimalLogicPiece(AnimalLogicPiece copy)
    {
        this.animal = copy.animal;
        this.color = copy.color;
        this.image = copy.image;
        this.onBoard = copy.onBoard;
        this.deadEnd = false;
    }

    public PieceAnimal getAnimal()
    {
        return animal;
    }

    public PieceColor getColor()
    {
        return color;
    }

    public BufferedImage getImage()
    {
        return image;
    }

    public boolean isOnBoard()
    {
        return onBoard;
    }

    public void setOnBoard(boolean onBoard)
    {
        this.onBoard = onBoard;
    }

    @Override
    public String toString()
    {
        return color + " " + animal;
    }

    public boolean isDeadEnd()
    {
        return deadEnd;
    }

    public void setDeadEnd(boolean deadEnd)
    {
        this.deadEnd = deadEnd;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((animal == null) ? 0 : animal.hashCode());
        result = prime * result + ((color == null) ? 0 : color.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AnimalLogicPiece other = (AnimalLogicPiece) obj;
        if (animal != other.animal)
            return false;
        if (color != other.color)
            return false;
        return true;
    }


}