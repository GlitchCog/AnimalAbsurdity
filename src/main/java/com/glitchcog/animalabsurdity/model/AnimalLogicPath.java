package com.glitchcog.animalabsurdity.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class AnimalLogicPath
{
    private Stack<AnimalLogicPiece> pieces;

    public AnimalLogicPath()
    {
        this.pieces = new Stack<AnimalLogicPiece>();
    }

    public AnimalLogicPath(AnimalLogicPath prev, AnimalLogicPiece next)
    {
        this();
        for (AnimalLogicPiece p : prev.getPieces())
        {
            this.pieces.add(new AnimalLogicPiece(p));
        }
        this.pieces.add(next);
    }

    public List<AnimalLogicPiece> getPieces()
    {
        return pieces;
    }

    public List<AnimalLogicPiece> getPossibilities(Map<AnimalLogicPath, List<AnimalLogicPiece>> possibilitiesPerPath, final AnimalLogicBoard alBoard)
    {
        if (possibilitiesPerPath.get(this) != null)
        {
            List<AnimalLogicPiece> allPossibilities = possibilitiesPerPath.get(this);
            List<AnimalLogicPiece> onlyViablePossibilities = new ArrayList<AnimalLogicPiece>();
            for (AnimalLogicPiece p : allPossibilities)
            {
                if (!p.isDeadEnd())
                {
                    onlyViablePossibilities.add(p);
                }
            }
            return onlyViablePossibilities;
        }
        else
        {
            int[][] board = alBoard.getBoard();
            AnimalLogicPiece[] boardPieces = alBoard.getPieces();
            // These need to be copies that can be marked as dead-ends
            List<AnimalLogicPiece> possibilities = new ArrayList<AnimalLogicPiece>();
            for (int r = 0; r < board.length; r++)
            {
                int colLimit = 0;
                for (int c = 0; c < board[r].length; c++)
                {
                    colLimit = c + 1;
                    if (colLimit == board[r].length || boardPieces[board[r][colLimit - 1]].isOnBoard())
                    {
                        break;
                    }
                }
                for (int c = 0; c < colLimit; c++)
                {
                    AnimalLogicPiece testPiece = new AnimalLogicPiece(boardPieces[board[r][c]]);
                    if (testPiece.isOnBoard())
                    {
                        AnimalLogicPiece lastPiece = pieces.isEmpty() ? null : pieces.get(pieces.size() - 1);
                        // @formatter:off
                        if ((lastPiece == null || 
                        lastPiece.getAnimal() == testPiece.getAnimal() || 
                        lastPiece.getColor() == testPiece.getColor())
                        && !testPiece.isDeadEnd())
                        {
                            possibilities.add(testPiece);
                        }
                        // @formatter:on
                    }
                }
            }
            possibilitiesPerPath.put(this, possibilities);
            return possibilities;
        }
    }

    public boolean isDeadEnd()
    {
        return pieces.isEmpty() ? false : pieces.get(pieces.size() - 1).isDeadEnd();
    }

    public AnimalLogicPiece getLastPiece()
    {
        return pieces == null || pieces.isEmpty() ? null : pieces.get(pieces.size() - 1);
    }

    public boolean isSolution()
    {
        return pieces != null && pieces.size() == PieceAnimal.values().length * PieceColor.values().length;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        if (pieces != null)
        {
            for (AnimalLogicPiece p : pieces)
            {
                result = prime * result + p.hashCode();
            }
        }
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
        AnimalLogicPath other = (AnimalLogicPath) obj;
        if (pieces == null)
        {
            return other.pieces == null;
        }
        else if (other.pieces == null)
        {
            return false;
        }
        else if (pieces.size() != other.pieces.size())
        {
            return false;
        }
        else
        {
            for (int i = 0; i < pieces.size(); i++)
            {
                if (pieces.get(i) == null && other.pieces.get(i) == null)
                {
                    continue;
                }
                if (!pieces.get(i).equals(other.pieces.get(i)))
                {
                    return false;
                }
            }
            return true;
        }
    }

    @Override
    public String toString()
    {
        String output = "";
        for (AnimalLogicPiece p : pieces)
        {
            output += (output.isEmpty() ? "" : ", \n") + p.toString();
        }
        return output;
    }

    public boolean isEmpty()
    {
        return pieces == null || pieces.isEmpty();
    }
}