package com.glitchcog.animalabsurdity.model;

import java.awt.Color;

public enum PieceColor
{
    RED(0, new Color(255, 48, 48)), YELLOW(1, new Color(255, 180, 0)), GREEN(2, new Color(150, 220, 0)), BLUE(3, new Color(50, 165, 150));

    private final int value;
    private final Color color;

    private PieceColor(int value, Color color)
    {
        this.value = value;
        this.color = color;
    }

    public int getValue()
    {
        return value;
    }

    public Color getColor()
    {
        return color;
    }
}