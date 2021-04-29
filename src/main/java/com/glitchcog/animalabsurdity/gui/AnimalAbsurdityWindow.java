package com.glitchcog.animalabsurdity.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.glitchcog.animalabsurdity.AnimalLogicSolver;
import com.glitchcog.animalabsurdity.model.AnimalLogicBoard;

public class AnimalAbsurdityWindow extends JFrame
{
    private static final long serialVersionUID = 1L;

    public static final Color BACKGROUND_COLOR = new Color(40, 20, 10);

    private JPanel content;

    public AnimalAbsurdityWindow(AnimalLogicBoard board, AnimalLogicSolver solver)
    {
        super("Animal Logic Solver");

        setSize(1024, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new GridLayout(1, 1));

        content = new JPanel();
        content.setLayout(new GridBagLayout());
        BoardPanel boardPanel = new BoardPanel(board);
        SolutionPanel solutionPanel = new SolutionPanel(solver);
        ButtonPanel buttonPanel = new ButtonPanel(board, boardPanel, solutionPanel);
        addComponents(boardPanel, solutionPanel, buttonPanel);
        add(content);
    }

    public void addComponents(BoardPanel board, SolutionPanel solution, ButtonPanel buttons)
    {
        GridBagConstraints gbc = getGbc();
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 0.85;
        content.add(board, gbc);
        gbc.gridy++;
        gbc.weighty = 0.15;
        content.add(solution, gbc);
        gbc.gridy++;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.SOUTH;
        content.add(buttons, gbc);
    }

    public static GridBagConstraints getGbc()
    {
        return new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
    }
}