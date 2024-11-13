import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

class ScrollBar extends JPanel {
    private ScrollBar thisScrollBar;
    
    public int max;
    public int span;
    public int position;
    
    // isVertical cannot be changed after instantiation
    private boolean isVertical;
    
    private boolean isMouseDown;
    
    // To be overriden
    public void onScroll() {}
    
    public ScrollBar(int max, int span, int position, boolean isVertical) {
        thisScrollBar = this;
        this.max = max; this.span = span; this.position = position; this.isVertical = isVertical;
        
        if (!isVertical) {
            setPreferredSize(new Dimension(0, 10));
        } else {
            setPreferredSize(new Dimension(10, 0));
        }
        setBackground(new Color(200, 200, 200));
        
        addMouseListener(new MouseListener() {
            @Override
            public void mousePressed(MouseEvent e) {
                isMouseDown = true;
                new SwingWorker() {
                    public Integer doInBackground() {
                        while (isMouseDown) {
                            Point scrollBarPosition = getLocationOnScreen();
                            Point mousePosition = MouseInfo.getPointerInfo().getLocation();
                            
                            if (!isVertical) {
                                thisScrollBar.position = (int) ((((double) mousePosition.x - scrollBarPosition.x) / getWidth()) * max);
                            } else {
                                thisScrollBar.position = (int) ((((double) mousePosition.y - scrollBarPosition.y) / getHeight()) * max);
                            }
                            
                            // Clamp position
                            if (thisScrollBar.position < 0) {
                                thisScrollBar.position = 0;
                            } else if (thisScrollBar.position > max - span) {
                                thisScrollBar.position = max - span;
                            }
                            
                            thisScrollBar.repaint();
                            onScroll();
                        }
                        return 1;
                    }
                }.execute();
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                isMouseDown = false;
            }
            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            public void mouseClicked(MouseEvent e) {}
        });
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.setColor(Color.GRAY);
        if (!isVertical) {
            int barWidth = (int) (((double) getWidth() / max) * span);
            int barPosition = (int) (((double) getWidth() / max) * position);
            g.fillRect(barPosition, 0, barWidth, getHeight());
        } else {
            int barHeight = (int) (((double) getHeight() / max) * span);
            int barPosition = (int) (((double) getHeight() / max) * position);
            g.fillRect(0, barPosition, getWidth(), barHeight);
        }
    }
}