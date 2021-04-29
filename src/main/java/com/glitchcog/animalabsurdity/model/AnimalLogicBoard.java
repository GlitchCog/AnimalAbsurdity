package com.glitchcog.animalabsurdity.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class AnimalLogicBoard
{
    private int width;
    private int height;

    private final int number;
    private int[][] board;
    private AnimalLogicPiece[] pieces;

    public AnimalLogicBoard(int number)
    {
        this(PieceAnimal.values(), PieceColor.values(), number);
    }

    public List<String> validate()
    {
        List<String> errors = new ArrayList<String>();
        Set<Integer> existingPieces = new HashSet<Integer>();
        for (int y = 0; y < this.board.length; y++)
        {
            for (int x = 0; x < this.board[y].length; x++)
            {
                int pid = this.board[y][x];
                if (existingPieces.contains(pid))
                {
                    errors.add("Duplicate piece found: " + getPieces()[pid]);
                }
                else
                {
                    existingPieces.add(pid);
                }
            }
        }
        for (int i = 0; i < PieceAnimal.values().length * PieceColor.values().length; i++)
        {
            if (!existingPieces.contains(i))
            {
                errors.add("Missing piece: " + getPieces()[i]);
            }
        }
        return errors;
    }

    protected AnimalLogicBoard(PieceAnimal[] animals, PieceColor[] colors, int number)
    {
        this.number = number;
        int boardSize = animals.length * colors.length;
        this.width = (int) Math.sqrt(boardSize);
        this.height = (int) Math.sqrt(boardSize);
        board = new int[height][width];
        int pi = 0;
        for (int y = 0; y < this.board.length; y++)
        {
            for (int x = 0; x < this.board[y].length; x++)
            {
                this.board[y][x] = pi++;
            }
        }
        pieces = new AnimalLogicPiece[boardSize];
        for (int i = 0; i < animals.length; i++)
        {
            for (int j = 0; j < colors.length; j++)
            {
                try
                {
                    pieces[i + j * colors.length] = new AnimalLogicPiece(animals[i], colors[j]);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public int getNumber()
    {
        return number;
    }

    public void setPiece(int row, int col, AnimalLogicPiece piece)
    {
        setPiece(row, col, piece.getAnimal(), piece.getColor());
    }

    public void setPiece(int row, int col, PieceAnimal animal, PieceColor color)
    {
        int i = 0;
        for (i = 0; i < pieces.length; i++)
        {
            AnimalLogicPiece check = pieces[i];
            if (check.getAnimal() == animal && check.getColor() == color)
            {
                check.setOnBoard(true);
                board[row][col] = i;
                return;
            }
        }
    }

    public boolean isEmpty()
    {
        for (AnimalLogicPiece p : pieces)
        {
            if (p.isOnBoard())
            {
                return false;
            }
        }
        return true;
    }

    public void randomize()
    {
        Random rnd = new Random();
        int[] pull = new int[width * height];
        boolean[] used = new boolean[pull.length];
        for (int i = 0; i < pull.length; i++)
        {
            pull[i] = i;
        }
        for (int r = 0; r < board.length; r++)
        {
            for (int c = 0; c < board[r].length; c++)
            {
                int valueIndex;
                do
                {
                    valueIndex = rnd.nextInt(pull.length);
                } while (used[valueIndex]);
                board[r][c] = pull[valueIndex];
                used[valueIndex] = true;
            }
        }
    }

    public int[][] getBoard()
    {
        return board;
    }

    public AnimalLogicPiece getPieceAt(int row, int col)
    {
        if (row < 0 || col < 0 || row >= board.length || col >= board[row].length)
        {
            return null;
        }
        return pieces[board[row][col]];
    }

    public AnimalLogicPiece[] getPieces()
    {
        return pieces;
    }

    @Override
    public String toString()
    {
        final char div = '\t';
        String output = "";
        for (int r = 0; r < board.length; r++)
        {
            for (int c = 0; c < board[r].length; c++)
            {
                output += "" + pieces[board[r][c]] + div;
            }
            output += "\n";
        }
        return output;
    }

    public AnimalLogicPiece get(AnimalLogicPiece findMe)
    {
        for (AnimalLogicPiece p : pieces)
        {
            if (p.equals(findMe))
            {
                return p;
            }
        }
        return null;
    }

    public void reset()
    {
        for (AnimalLogicPiece p : pieces)
        {
            p.setDeadEnd(false);
            p.setOnBoard(true);
        }
    }

    public void loadPuzzle(AnimalLogicBoard other)
    {
        for (int y = 0; y < this.board.length; y++)
        {
            for (int x = 0; x < this.board[y].length; x++)
            {
                this.board[y][x] = other.board[y][x];
            }
        }
    }

}