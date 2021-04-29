package com.glitchcog.animalabsurdity.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class BoardPanelMouse extends MouseAdapter
{
    private final BoardPanel bp;

    public BoardPanelMouse(BoardPanel bp)
    {
        this.bp = bp;
    }

    public void mouseClicked(MouseEvent e)
    {
    }

    public void mousePressed(MouseEvent e)
    {
        bp.selectPiece(e.getX(), e.getY());
        bp.setSelectedCoor(e.getX(), e.getY());
    }

    public void mouseReleased(MouseEvent e)
    {
        bp.swapPieces(e.getX(), e.getY());
    }

    public void mouseEntered(MouseEvent e)
    {
    }

    public void mouseExited(MouseEvent e)
    {
    }

    public void mouseWheelMoved(MouseWheelEvent e)
    {
    }

    public void mouseDragged(MouseEvent e)
    {
        bp.setSelectedCoor(e.getX(), e.getY());
    }

    public void mouseMoved(MouseEvent e)
    {
    }

}