package com.glitchcog.animalabsurdity.gui;

import javax.swing.SwingWorker;

import com.glitchcog.animalabsurdity.AnimalLogicSolver;

public class SolverWorker extends SwingWorker<Boolean, Void>
{
    private AnimalLogicSolver solver;

    public SolverWorker(AnimalLogicSolver solver)
    {
        this.solver = solver;
    }

    protected Boolean doInBackground() throws Exception
    {
        solver.solve();
        while (solver.isStillWorking())
        {
            // @formatter:off
            try { Thread.sleep(50); } catch (InterruptedException e) {}
            // @formatter:on
        }
        return Boolean.TRUE;
    }
}