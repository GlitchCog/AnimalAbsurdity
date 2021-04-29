package com.glitchcog.animalabsurdity;

import com.glitchcog.animalabsurdity.gui.AnimalAbsurdityWindow;
import com.glitchcog.animalabsurdity.model.AnimalLogicBoard;

public class AnimalAbsurdityMain
{
    public static void main(String[] args) throws Exception
    {
        PuzzleLoader.load();
        AnimalLogicBoard board = new AnimalLogicBoard(0);
        AnimalLogicSolver solver = new AnimalLogicSolver(board);
        AnimalAbsurdityWindow window = new AnimalAbsurdityWindow(board, solver);
        window.setVisible(true);
    }
}