package com.glitchcog.animalabsurdity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.glitchcog.animalabsurdity.model.AnimalLogicBoard;
import com.glitchcog.animalabsurdity.model.PieceAnimal;
import com.glitchcog.animalabsurdity.model.PieceColor;

public class PuzzleLoader
{
    public static Map<Integer, AnimalLogicBoard> boards;

    public static void load() throws FileNotFoundException, IOException
    {
        boards = null;
        String line = null;
        try (InputStream is = PuzzleLoader.class.getClassLoader().getResourceAsStream("puzzles.dat"))
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            try
            {
                int row = 0;
                boards = new HashMap<Integer, AnimalLogicBoard>();
                AnimalLogicBoard board = null;
                while ((line = reader.readLine()) != null)
                {
                    if (line.length() == 2)
                    {
                        if (board != null)
                        {
                            List<String> errors = board.validate();
                            if (!errors.isEmpty())
                            {
                                System.out.println("Puzzle #" + board.getNumber());
                                for (String error : errors)
                                {
                                    System.out.println("   " + error);
                                }
                            }
                        }
                        int number = Integer.parseInt(line);
                        board = new AnimalLogicBoard(number);
                        boards.put(number, board);
                        row = 0;
                    }
                    else
                    {
                        String[] codedPieces = line.split("\t");
                        for (int i = 0; i < codedPieces.length; i++)
                        {
                            String[] ac = codedPieces[i].split(" ");
                            board.setPiece(row, i, PieceAnimal.valueOf(ac[1]), PieceColor.valueOf(ac[0]));
                        }
                        row++;
                    }
                }
            }
            catch (Exception e)
            {
                System.out.println(line);
                e.printStackTrace();
            }
        }
    }

    public static AnimalLogicBoard getBoard(int idx)
    {
        return boards.get(idx);
    }

    public static Set<Integer> getBoardNames()
    {
        return boards.keySet();
    }
}