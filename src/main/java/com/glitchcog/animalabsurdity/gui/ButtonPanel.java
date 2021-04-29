package com.glitchcog.animalabsurdity.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.glitchcog.animalabsurdity.PuzzleLoader;
import com.glitchcog.animalabsurdity.model.AnimalLogicBoard;

public class ButtonPanel extends JPanel
{
    private static final long serialVersionUID = 1L;

    private AnimalLogicBoard board;

    private BoardPanel boardPanel;

    private JSlider speed;

    private JLabel speedLabel;

    private JButton solve;

    private Timer solveClock;

    private Timer redrawClock;

    private SolutionPanel solution;

    private JButton reset;

    private JList<Integer> puzzles;

    public ButtonPanel(AnimalLogicBoard board, BoardPanel boardPanel, SolutionPanel solution)
    {
        this.board = board;
        this.boardPanel = boardPanel;
        this.solution = solution;
        build();
    }

    public AnimalLogicBoard getBoard()
    {
        return board;
    }

    private Thread workerThread;

    private static final int MAX_SPEED = 78;

    private void build()
    {
        speed = new JSlider(0, MAX_SPEED, 30);
        speed.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                solveClock.setDelay(getMsDelay());
                speedLabel.setText(getSpeedLabelText());
            }
        });
        speedLabel = new JLabel(getSpeedLabelText());
        solve = new JButton("Solve");
        reset = new JButton("Reset");
        reset.setEnabled(false);

        ActionListener solverAl = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (e.getSource() == solve)
                {
                    reset.setEnabled(true);
                    solve.setEnabled(false);
                    SolverWorker worker = new SolverWorker(solution.getSolver());
                    workerThread = new Thread(worker);
                    workerThread.start();
                    solveClock.start();
                }
                else if (e.getSource() == reset)
                {
                    reset();
                }
                boardPanel.repaint();
                solution.repaint();
            }
        };

        solve.addActionListener(solverAl);
        reset.addActionListener(solverAl);
        solveClock = new Timer(getMsDelay(), new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (!solution.isInitialized())
                {
                    solution.initSolve();
                }
                else
                {
                    boolean solved = solution.next();
                    if (solved)
                    {
                        solveClock.stop();
                        redrawClock.stop();
                        solve.setSelected(false);
                    }
                    else
                    {
                        boardPanel.repaint();
                        solution.repaint();
                    }
                }
            }
        });
        redrawClock = new Timer(10, new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                boardPanel.repaint();
                solution.repaint();
            }
        });

        puzzles = new JList<Integer>(PuzzleLoader.getBoardNames().toArray(new Integer[PuzzleLoader.getBoardNames().size()]));
        puzzles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        puzzles.setVisibleRowCount(3);
        puzzles.setLayoutOrientation(JList.HORIZONTAL_WRAP);

        puzzles.addListSelectionListener(new ListSelectionListener()
        {
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                board.loadPuzzle(PuzzleLoader.getBoard(puzzles.getSelectedValue()));
                reset();
                boardPanel.repaint();
                solution.repaint();
            }
        });

        add(new JScrollPane(puzzles, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
        add(solve);
        add(reset);
        add(speed);
        add(speedLabel);

        redrawClock.start();
    }

    private void reset()
    {
        solve.setEnabled(true);
        reset.setEnabled(false);
        if (workerThread != null)
        {
            workerThread.interrupt();
        }
        solveClock.stop();
        solution.reset(board);
    }

    public boolean isSolving()
    {
        return solve.isSelected();
    }

    private int getMsDelay()
    {
        return getMsDelay(speed.getValue());
    }

    private int getMsDelay(int ticksPerMinute)
    {
        return (int) (1000.0 / (speed.getValue() / 8.0 + 0.25));
    }

    private String getSpeedLabelText()
    {
        String mps = "" + ((int) (100000.0 / getMsDelay())) / 100.0;
        while (mps.length() < 4)
        {
            mps += '0';
        }
        return mps + " moves per second";
    }
}