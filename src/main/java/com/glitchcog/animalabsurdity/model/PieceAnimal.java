package com.glitchcog.animalabsurdity.model;

public enum PieceAnimal
{
    CAMEL(0, "camel"), LION(1, "lion"), GIRAFFE(2, "giraffe"), HIPPO(3, "hippo");

    private final int value;
    private final String name;

    private PieceAnimal(final int value, final String name)
    {
        this.value = value;
        this.name = name;
    }

    public int getValue()
    {
        return value;
    }

    public String getFilename()
    {
        return "al_" + name + ".png";
    }
}