package com.glitchcog.animalabsurdity.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Stack;

import javax.swing.JPanel;

import com.glitchcog.animalabsurdity.AnimalLogicSolver;
import com.glitchcog.animalabsurdity.model.AnimalLogicBoard;
import com.glitchcog.animalabsurdity.model.AnimalLogicPath;
import com.glitchcog.animalabsurdity.model.PieceAnimal;
import com.glitchcog.animalabsurdity.model.PieceColor;

public class SolutionPanel extends JPanel
{
    private static final long serialVersionUID = 1L;

    private AnimalLogicSolver solver;

    public SolutionPanel(AnimalLogicSolver solver)
    {
        this.solver = solver;
    }

    public AnimalLogicSolver getSolver()
    {
        return solver;
    }

    private double getSolPieceScale(int pieceWidth)
    {
        return (double) getWidth() / pieceWidth / PieceAnimal.values().length / PieceColor.values().length;
    }

    @Override
    public void paint(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(AnimalAbsurdityWindow.BACKGROUND_COLOR);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        Stack<AnimalLogicPath> paths = solver.getSolutions();
        if (paths != null && !paths.isEmpty() && paths.get(paths.size() - 1).getPieces() != null && !paths.get(paths.size() - 1).getPieces().isEmpty())
        {
            AnimalLogicPath path;
            if (solver.getSolution() != null)
            {
                path = solver.getSolution();
            }
            else
            {
                path = paths.get(paths.size() - 1);
                g2d.setColor(path.getLastPiece().getColor().getColor().darker().darker());
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }

            for (int xi = 0; xi < path.getPieces().size(); xi++)
            {
                BufferedImage img = path.getPieces().get(xi).getImage();
                int y = (int) (getHeight() - img.getHeight() * getSolPieceScale(img.getWidth())) / 2;
                int w = (int) (img.getWidth() * getSolPieceScale(img.getWidth()));
                int h = (int) (img.getHeight() * getSolPieceScale(img.getWidth()));
                g2d.drawImage(img, xi * w, y, w, h, null);
            }
        }
    }

    public boolean next()
    {
        return solver.next();
    }

    public void reset(AnimalLogicBoard board)
    {
        solver.reset(board);
    }

    public void initSolve()
    {
        solver.solve();
    }

    public boolean isInitialized()
    {
        return solver.isInitialized();
    }
}