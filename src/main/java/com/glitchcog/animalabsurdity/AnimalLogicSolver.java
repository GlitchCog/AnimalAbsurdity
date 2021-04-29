package com.glitchcog.animalabsurdity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import com.glitchcog.animalabsurdity.model.AnimalLogicBoard;
import com.glitchcog.animalabsurdity.model.AnimalLogicPath;
import com.glitchcog.animalabsurdity.model.AnimalLogicPiece;
import com.glitchcog.animalabsurdity.model.PieceAnimal;
import com.glitchcog.animalabsurdity.model.PieceColor;

public class AnimalLogicSolver
{
    private AnimalLogicBoard board;

    private Set<AnimalLogicPath> deadEnds;

    private Map<AnimalLogicPath, List<AnimalLogicPiece>> possibilitiesPerPath;

    private Stack<AnimalLogicPath> solutions;

    private boolean init;

    public AnimalLogicSolver(AnimalLogicBoard board)
    {
        this.board = board;
        reset(board);
    }

    public boolean isInitialized()
    {
        return init;
    }

    public void reset(AnimalLogicBoard board)
    {
        init = false;
        board.reset();
        if (solutions == null)
        {
            solutions = new Stack<AnimalLogicPath>();
        }
        else
        {
            solutions.clear();
        }
        deadEnds = new HashSet<AnimalLogicPath>();
        possibilitiesPerPath = new HashMap<AnimalLogicPath, List<AnimalLogicPiece>>();
    }

    public AnimalLogicPath getSolution()
    {
        try
        {
            if (solutions != null)
            {
                for (AnimalLogicPath p : solutions)
                {
                    if (p.isSolution())
                    {
                        return p;
                    }
                }
            }
        }
        catch (Exception e)
        {
            // Bad way to handle a stack that isn't thread safe, but whatevs
        }
        return null;
    }

    public Stack<AnimalLogicPath> getSolutions()
    {
        return solutions;
    }

    public AnimalLogicPath solve()
    {
        init = true;
        solutions = new Stack<AnimalLogicPath>();
        solutions = solve(solutions);
        if (solutions != null)
        {
            for (AnimalLogicPath path : solutions)
            {
                if (path.isSolution())
                {
                    return path;
                }
            }
        }
        return null;
    }

    private boolean goNextStep;

    public boolean next()
    {
        goNextStep = true;
        if (solutions != null && !solutions.isEmpty())
        {
            AnimalLogicPath p = solutions.get(solutions.size() - 1);
            if (p != null && p.getPieces() != null)
            {
                return p.getPieces().size() == PieceAnimal.values().length * PieceColor.values().length;
            }
        }
        return false;
    }

    public boolean isStillWorking()
    {
        return !board.isEmpty();
    }

    public Stack<AnimalLogicPath> solve(Stack<AnimalLogicPath> solution)
    {
        if (solution.isEmpty())
        {
            // First path object to avoid NPEs
            solution.add(new AnimalLogicPath());
        }
        // The problem is this gets caught in a look where it finds a dead end, then backs out, but then proceeds to
        // continue into the same dead end over and over again. The possibilities array needs to persist such that we
        // continue through it rather than start over and regenerate it
        while (isStillWorking())
        {
            while (!goNextStep)
            {
                // @formatter:off
                try { Thread.sleep(50); } catch (InterruptedException e) {}
                // @formatter:on
            }
            goNextStep = false;
            AnimalLogicPath currentPath = solution.peek();
            if (currentPath.isDeadEnd())
            {
                return null;
            }
            else
            {
                List<AnimalLogicPiece> possibilities = currentPath.getPossibilities(possibilitiesPerPath, board);

                if (possibilities.isEmpty() || deadEnds.contains(currentPath))
                {
                    AnimalLogicPath deadEndPath = solution.pop();
                    AnimalLogicPiece endPiece = currentPath.getLastPiece();
                    endPiece.setDeadEnd(true);
                    board.get(endPiece).setOnBoard(true);
                    deadEnds.add(deadEndPath);
                    return null;
                }
                else
                {
                    for (AnimalLogicPiece pos : possibilities)
                    {
                        if (!pos.isDeadEnd())
                        {
                            board.get(pos).setOnBoard(false);
                            AnimalLogicPath trialPath = new AnimalLogicPath(currentPath, pos);
                            if (trialPath.isDeadEnd())
                            {
                                deadEnds.add(trialPath);
                                return null;
                            }
                            else
                            {
                                solution.push(trialPath);
                                solve(solution);
                            }
                        }
                    }
                }
            }
        }

        return solution;
    }

}