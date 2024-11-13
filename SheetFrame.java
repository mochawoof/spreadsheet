import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

class SheetFrame extends JFrame {
    private int maxCellsx = 500; private int maxCellsy = 500;
    private int scrollMultiplier = 5;
    
    private Sheet sheet;
    
    private JLabel cellLabel;
    
    private void updateCellLabel(int pointingx, int pointingy) {
        if (sheet != null) {
            cellLabel.setText("(" + sheet.selectedCellx + ", " + sheet.selectedCelly + ")");
            
            if (pointingx <= sheet.maxCellsx && pointingy <= sheet.maxCellsy) {
                cellLabel.setText(cellLabel.getText() + " (" + pointingx + ", " + pointingy + ")");
            }
        }
    }
    
    private void updateCellLabelMouseCoords(int mousex, int mousey) {
        updateCellLabel((mousex / sheet.cellWidth) + sheet.scrollx, (mousey / sheet.cellHeight) + sheet.scrolly);
    }
    
    public SheetFrame() {
        setTitle("Spreadsheet");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(Resources.getAsImage("res/icon.png"));
        setSize(800, 600);
        setLayout(new BorderLayout());
        
        try {
            for (UIManager.LookAndFeelInfo lafInfo : UIManager.getInstalledLookAndFeels()) {
                if (lafInfo.getName().contains("Nimbus")) {
                    UIManager.setLookAndFeel(lafInfo.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Menu bar
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        JMenu editMenu = new JMenu("Edit");
        menuBar.add(editMenu);
        
        // Status bar
        JPanel statusBar = new JPanel();
        statusBar.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        statusBar.setLayout(new BoxLayout(statusBar, BoxLayout.X_AXIS));
        add(statusBar, BorderLayout.PAGE_END);
        
        JLabel diskStatusLabel = new JLabel();
        
        ImageIcon greenIcon = Resources.getAsImageIcon("res/green.png");
        ImageIcon grayIcon = Resources.getAsImageIcon("res/gray.png");
        
        diskStatusLabel.setIcon(grayIcon);
        diskStatusLabel.setToolTipText("Disk not in use");
        diskStatusLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        statusBar.add(diskStatusLabel);
        
        statusBar.add(Box.createGlue());
        
        cellLabel = new JLabel();
        statusBar.add(cellLabel);
        
        // Center panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        add(centerPanel, BorderLayout.CENTER);
        
        JTextField inputField = new JTextField();
        centerPanel.add(inputField, BorderLayout.PAGE_START);
        
        ScrollBar vscroll = new ScrollBar(maxCellsy, 0, 0, true) {
            @Override
            public void onScroll() {
                sheet.scrolly = this.position;
                sheet.repaint();
            }
        };;
        centerPanel.add(vscroll, BorderLayout.LINE_END);
        ScrollBar hscroll = new ScrollBar(maxCellsx, 0, 0, false) {
            @Override
            public void onScroll() {
                sheet.scrollx = this.position;
                sheet.repaint();
            }
        };
        centerPanel.add(hscroll, BorderLayout.PAGE_END);
        
        // Add sheet
        sheet = new Sheet("hi.csv", maxCellsx, maxCellsy) {
            @Override
            public void onDiskRead() {
                diskStatusLabel.setIcon(greenIcon);
                diskStatusLabel.setToolTipText("Disk in use...");
            }
            @Override
            public void onDiskFree() {
                diskStatusLabel.setIcon(grayIcon);
                diskStatusLabel.setToolTipText("Disk not in use");
            }
            @Override
            public void selectCell(int x, int y) {
                super.selectCell(x, y);
                updateCellLabel(x, y);
                inputField.setText(super.getCell(x, y));
            }
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                int rows = (int) Math.ceil((double) this.getWidth() / super.cellWidth);
                int cols = (int) Math.ceil((double) this.getHeight() / super.cellHeight);
                
                vscroll.span = cols;
                hscroll.span = rows;
            }
        };
        
        sheet.addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
                vscroll.scroll(e.getWheelRotation() * scrollMultiplier);
            }
        });
        
        sheet.addMouseMotionListener(new MouseMotionListener() {
            public void mouseMoved(MouseEvent e) {
                updateCellLabelMouseCoords(e.getX(), e.getY());
            }
            public void mouseDragged(MouseEvent e) {}
        });
        
        centerPanel.add(sheet, BorderLayout.CENTER);
        
        setVisible(true);
    }
}