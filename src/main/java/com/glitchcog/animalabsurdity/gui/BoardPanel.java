package com.glitchcog.animalabsurdity.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import com.glitchcog.animalabsurdity.model.AnimalLogicBoard;
import com.glitchcog.animalabsurdity.model.AnimalLogicPiece;

public class BoardPanel extends JPanel
{
    private static final long serialVersionUID = 1L;

    private AnimalLogicBoard board;

    private BufferedImage boardImage;

    private AnimalLogicPiece selectedPiece;
    private int selectedX;
    private int selectedY;

    public void setSelectedCoor(int selectedX, int selectedY)
    {
        this.selectedX = selectedX;
        this.selectedY = selectedY;
    }

    private final static int OFFSET_LEFT = 300;
    private final static int OFFSET_TOP = 220;

    private final static int SPACING_HORIZ = 90;
    private final static int SPACING_VERT = 20;

    public BoardPanel(AnimalLogicBoard board)
    {
        this.board = board;
        try
        {
            final String classPathUrlStr = "al_board.png";
            URL url = getClass().getClassLoader().getResource(classPathUrlStr);
            this.boardImage = ImageIO.read(url);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        BoardPanelMouse mouse = new BoardPanelMouse(this);
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
        addMouseWheelListener(mouse);
    }

    public double getBoardScale()
    {
        if ((double) getWidth() / (double) getHeight() > (double) boardImage.getWidth() / (double) boardImage.getHeight())
        {
            // scale by height
            return (double) getHeight() / (double) boardImage.getHeight();
        }
        else
        {
            // scale by width
            return (double) getWidth() / (double) boardImage.getWidth();
        }
    }

    @Override
    public void paint(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(AnimalAbsurdityWindow.BACKGROUND_COLOR);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        final int bw = (int) (boardImage.getWidth() * getBoardScale());
        final int bh = (int) (boardImage.getHeight() * getBoardScale());

        int cx = (getWidth() - bw) / 2;
        int cy = (getHeight() - bh) / 2;

        int offsetLeft = (int) (OFFSET_LEFT * getBoardScale());
        int offsetTop = (int) (OFFSET_TOP * getBoardScale());
        int spacingHoriz = (int) (SPACING_HORIZ * getBoardScale());
        int spacingVert = (int) (SPACING_VERT * getBoardScale());

        g2d.drawImage(boardImage, cx, cy, bw, bh, null);
        for (int y = 0; y < 4; y++)
        {
            for (int x = 0; x < 4; x++)
            {
                AnimalLogicPiece p = board.getPieceAt(y, x);
                BufferedImage pieceImage = p.getImage();
                final int pw = (int) (pieceImage.getWidth() * 2.0 * getBoardScale());
                final int ph = (int) (pieceImage.getHeight() * 2.0 * getBoardScale());

                if (p.isOnBoard() && p != selectedPiece)
                {
                    int drawX = cx + offsetLeft + x * (pw + spacingHoriz) - pw / 2;
                    int drawY = cy + offsetTop + y * (ph + spacingVert) - ph / 2;
                    // @formatter:off
                    g2d.drawImage(pieceImage, drawX, drawY, pw, ph, null);
                    // @formatter:on
                }
            }
        }
        if (selectedPiece != null)
        {
            BufferedImage selPieceImg = selectedPiece.getImage();
            int drawW = (int) (selPieceImg.getWidth() * 2.0 * getBoardScale());
            int drawH = (int) (selPieceImg.getHeight() * 2.0 * getBoardScale());
            g2d.drawImage(selPieceImg, selectedX - drawW / 2, selectedY - drawH / 2, drawW, drawH, null);
        }
    }

    public int getBoardWidth()
    {
        return (int) (boardImage.getWidth(null) * getBoardScale());
    }

    public int getBoardHeight()
    {
        return (int) (boardImage.getHeight(null) * getBoardScale());
    }

    private static final int HORIZ_MARGIN = 155;
    private static final int VERT_MARGIN = 100;
    private static final int CELL_WIDTH = 335;
    private static final int CELL_HEIGHT = 240;
    private static final int CELL_ROW_GAP = 40;

    public int[] getMouseCell(int pixelX, int pixelY)
    {
        int row = 0;
        int col = 0;

        final int bw = (int) (boardImage.getWidth() * getBoardScale());
        final int bh = (int) (boardImage.getHeight() * getBoardScale());

        int cx = (getWidth() - bw) / 2;
        int cy = (getHeight() - bh) / 2;

        int vertMargin = (int) (VERT_MARGIN * getBoardScale());
        int horizMargin = (int) (HORIZ_MARGIN * getBoardScale());
        int cellHeight = (int) (CELL_HEIGHT * getBoardScale());
        int cellWidth = (int) (CELL_WIDTH * getBoardScale());
        int cellRowGap = (int) (CELL_ROW_GAP * getBoardScale());

        if (pixelX < cx + horizMargin || pixelY < cy + vertMargin || pixelX > cx + bw - horizMargin || pixelY > cy + bh - vertMargin)
        {
            // Out of bounds
            return null;
        }

        pixelX -= cx;
        pixelY -= cy;

        row = (pixelY - vertMargin) / (cellHeight + cellRowGap);
        col = (pixelX - horizMargin) / cellWidth;

        return new int[] { row, col };
    }

    /**
     * Get the piece being clicked on by the mouse
     * 
     * @param pressX
     * @param pressY
     * @return piece
     */
    public AnimalLogicPiece selectPiece(int mousePixelX, int mousePixelY)
    {
        selectedPiece = null;

        int[] cellCoor = getMouseCell(mousePixelX, mousePixelY);

        if (cellCoor != null && cellCoor.length == 2)
        {
            selectedPiece = board.getPieceAt(cellCoor[0], cellCoor[1]);
        }

        return selectedPiece;
    }

    public void swapPieces(int mousePixelX, int mousePixelY)
    {
        if (selectedPiece != null)
        {
            AnimalLogicPiece pieceA = selectedPiece;
            selectedPiece = null;
            int[] cellCoor = getMouseCell(mousePixelX, mousePixelY);
            Integer spCellRow = null;
            Integer spCellCol = null;
            for (int row = 0; row < 4 && spCellRow == null; row++)
            {
                for (int col = 0; col < 4 && spCellCol == null; col++)
                {
                    if (pieceA == board.getPieceAt(row, col))
                    {
                        spCellRow = row;
                        spCellCol = col;
                    }
                }
            }

            if (cellCoor != null && spCellRow != null && spCellCol != null)
            {
                AnimalLogicPiece pieceB = board.getPieceAt(cellCoor[0], cellCoor[1]);
                board.setPiece(cellCoor[0], cellCoor[1], pieceA);
                board.setPiece(spCellRow, spCellCol, pieceB);
            }
        }
    }
}