import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

import java.io.*;
import java.util.ArrayList;

class Sheet extends JPanel {
    // To be overriden by inner class
    public void onDiskRead() {}
    public void onDiskFree() {}
    
    private String filename;
    
    public int cellWidth = 64; public int cellHeight = 20;
    
    // Do not assign to these directly, use selectCell()
    public int selectedCellx; public int selectedCelly;
    
    // public so SheetFrame can scroll it
    public int scrollx; public int scrolly;
    
    public int maxCellsx; public int maxCellsy;
    
    public final int bufferLimit = 8000; // in bytes
    
    public Sheet(String filename, int maxCellsx, int maxCellsy) {
        this.filename = filename;
        this.maxCellsx = maxCellsx; this.maxCellsy = maxCellsy;
        scrollx = 0; scrolly = 0;
        selectCell(0, 0);
        
        addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
                int cellx = (int) ((double) e.getX() / cellWidth);
                int celly = (int) ((double) e.getY() / cellHeight);
                if (cellx + scrollx <= maxCellsx && celly + scrolly <= maxCellsy) {
                    selectCell(cellx + scrollx, celly + scrolly);
                }
            }
            
            public void mousePressed(MouseEvent e) {}
            public void mouseReleased(MouseEvent e) {}
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
        });
    }
    
    public String getCell(int x, int y) {
        if (x <= maxCellsx && y <= maxCellsy) {
            return x + ", " + y;
        } else {
            return null;
        }
    }
    
    // is overriden in SheetFrame to support input field
    public void selectCell(int x, int y) {
        selectedCellx = x; selectedCelly = y;
        repaint();
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        int rows = (int) Math.ceil((double) getWidth() / cellWidth);
        int cols = (int) Math.ceil((double) getHeight() / cellHeight);
        
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                
                // real cell position on screen
                int realx = x * cellWidth;
                int realy = y * cellHeight;
                
                // draw cell background, if selected
                if (x + scrollx == selectedCellx && y + scrolly == selectedCelly) {
                    g.setColor(new Color(130, 130, 255));
                    g.fillRect(realx, realy, cellWidth, cellHeight);
                }
                
                // draw cell borders
                g.setColor(Color.GRAY);
                g.fillRect(realx, realy, cellWidth, 1);
                g.fillRect(realx, realy + cellHeight, cellWidth, 1);
                g.fillRect(realx, realy, 1, cellHeight);
                g.fillRect(realx + cellWidth, realy, 1, cellHeight);
                
                g.setColor(Color.BLACK);
                g.setFont(new Font("Sans-Serif", Font.PLAIN, 12));
                
                String cellContent = getCell(x + scrollx, y + scrolly);
                if (cellContent != null) {
                    g.drawString(cellContent, realx + 1, realy + cellHeight - 1);
                }
            }
        }
    }
}