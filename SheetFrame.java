import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

class SheetFrame extends JFrame {
    private int maxCellsx = 500; private int maxCellsy = 500;
    
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
        
        // Center panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        add(centerPanel, BorderLayout.CENTER);
        
        JTextField inputField = new JTextField();
        centerPanel.add(inputField, BorderLayout.PAGE_START);
        
        ScrollBar vscroll = new ScrollBar(maxCellsy, 0, 0, true);
        centerPanel.add(vscroll, BorderLayout.LINE_END);
        ScrollBar hscroll = new ScrollBar(maxCellsx, 0, 0, false);
        centerPanel.add(hscroll, BorderLayout.PAGE_END);
        
        // Add sheet
        centerPanel.add(new Sheet("hi.csv") {
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
        }, BorderLayout.CENTER);
        
        setVisible(true);
    }
}