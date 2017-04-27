/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.TaxiSimulator;
import controller.Utils;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author joseb
 */
public class TaxiFrame extends javax.swing.JFrame {

    /**
     * Creates new form TaxiFrame
     */
    
    private static int SIZE = 35;
    
    private static TaxiSimulator _taxiSimulator;
    private static ImageIcon BusyTaxiDown = new ImageIcon(new ImageIcon("src/img/BusyTaxiDown.jpg").getImage().getScaledInstance(SIZE, SIZE, Image.SCALE_SMOOTH));
    private static ImageIcon BusyTaxiLeft = new ImageIcon(new ImageIcon("src/img/BusyTaxiLeft.jpg").getImage().getScaledInstance(SIZE, SIZE, Image.SCALE_SMOOTH));
    private static ImageIcon BusyTaxiRight = new ImageIcon(new ImageIcon("src/img/BusyTaxiRight.jpg").getImage().getScaledInstance(SIZE, SIZE, Image.SCALE_SMOOTH));
    private static ImageIcon BusyTaxiUp = new ImageIcon(new ImageIcon("src/img/BusyTaxiUp.jpg").getImage().getScaledInstance(SIZE, SIZE, Image.SCALE_SMOOTH));
    private static ImageIcon Dirt = (new ImageIcon("src/img/Dirt.jpg"));
    private static ImageIcon Grass = new ImageIcon(new ImageIcon("src/img/Grass.jpg").getImage().getScaledInstance(SIZE, SIZE, Image.SCALE_SMOOTH));
    private static ImageIcon Road = new ImageIcon(new ImageIcon("src/img/Road.jpg").getImage().getScaledInstance(SIZE, SIZE, Image.SCALE_SMOOTH));
    private static ImageIcon Person1 = new ImageIcon(new ImageIcon("src/img/Person1.jpg").getImage().getScaledInstance(SIZE, SIZE, Image.SCALE_SMOOTH));
    private static ImageIcon Person2 = new ImageIcon(new ImageIcon("src/img/Person2.jpg").getImage().getScaledInstance(SIZE, SIZE, Image.SCALE_SMOOTH));
    private static ImageIcon Person3 = new ImageIcon(new ImageIcon("src/img/Person3.jpg").getImage().getScaledInstance(SIZE, SIZE, Image.SCALE_SMOOTH));
    private static ImageIcon Person4 = new ImageIcon(new ImageIcon("src/img/Person4.jpg").getImage().getScaledInstance(SIZE, SIZE, Image.SCALE_SMOOTH));
    private static ImageIcon RoadRoute = new ImageIcon(new ImageIcon("src/img/RoadRoute.jpg").getImage().getScaledInstance(SIZE, SIZE, Image.SCALE_SMOOTH));
    private static ImageIcon Sidewalk = new ImageIcon(new ImageIcon("src/img/Sidewalk.jpg").getImage().getScaledInstance(SIZE, SIZE, Image.SCALE_SMOOTH));
    private static ImageIcon Smoke = new ImageIcon(new ImageIcon("src/img/Smoke.jpg").getImage().getScaledInstance(SIZE, SIZE, Image.SCALE_SMOOTH));
    private static ImageIcon TaxiDown = new ImageIcon(new ImageIcon("src/img/TaxiDown.jpg").getImage().getScaledInstance(SIZE, SIZE, Image.SCALE_SMOOTH));
    private static ImageIcon TaxiLeft = new ImageIcon(new ImageIcon("src/img/TaxiLeft.jpg").getImage().getScaledInstance(SIZE, SIZE, Image.SCALE_SMOOTH));
    private static ImageIcon TaxiRight = new ImageIcon(new ImageIcon("src/img/TaxiRight.jpg").getImage().getScaledInstance(SIZE, SIZE, Image.SCALE_SMOOTH));
    private static ImageIcon TaxiUp = new ImageIcon(new ImageIcon("src/img/TaxiUp.jpg").getImage().getScaledInstance(SIZE, SIZE, Image.SCALE_SMOOTH));
    
    private static final Map<Character, ImageIcon> Icons;
    static
    {
        Icons = new HashMap<>();
        Icons.put(' ', Road);
        Icons.put('+', RoadRoute);
        Icons.put('-', Sidewalk);
        Icons.put('*', Grass);
        Icons.put('.', Smoke);
        Icons.put('↓', TaxiDown);
        Icons.put('←', TaxiLeft);
        Icons.put('→', TaxiRight);
        Icons.put('↑', TaxiUp);
        Icons.put('⇓', BusyTaxiDown);
        Icons.put('⇐', BusyTaxiLeft);
        Icons.put('⇒', BusyTaxiRight);
        Icons.put('⇑', BusyTaxiUp);
        Icons.put((char) 1, Person1);
        Icons.put((char) 2, Person2);
        Icons.put((char) 3, Person3);
        Icons.put((char) 4, Person4);
    }
    
    private ArrayList<ArrayList<JLabel>> imgLabels = new ArrayList();
    
    
    public TaxiFrame(TaxiSimulator pTaxiSimulator) {
        initComponents();
        setResizable(false);
        _taxiSimulator = pTaxiSimulator;
        addLabels();
        setLocationRelativeTo(null);
        JLabel background = new JLabel(Dirt);
        PNLRoad.add(background);
        background.setSize(2000, 2000);
        background.setVisible(true);
        
    }
    
    private void addLabels(){
        int width=0,height=_taxiSimulator.getPlottableMap().length;
        for (int i = 0; i < _taxiSimulator.getPlottableMap().length; i++) {
            String line = _taxiSimulator.getPlottableMap()[i];
            width = Integer.max(width, line.length());
            ArrayList<JLabel> labels = new ArrayList<>();
            for (int j = 0; j < line.length(); j++) {
                JLabel thumb;
                if(Icons.containsKey(line.charAt(j))){
                    thumb = new JLabel(Icons.get(line.charAt(j)));
                }
                else{
                    thumb = new JLabel("<html><span style='font-size:"+(SIZE-3)+"px;color:white;'>"+line.charAt(j)+"</span></html>");
                }
                PNLRoad.add(thumb);
                thumb.setLocation(j * SIZE , i * SIZE);
                thumb.setSize(SIZE, SIZE);
                thumb.setVisible(true);
                labels.add(thumb);
            }
            imgLabels.add(labels);
        }
        this.setSize((width) * SIZE, (height) * SIZE + 32);
    }
    
    public void refeshLabels(){
        for (int i = 0; i < _taxiSimulator.getPlottableMap().length; i++) {
            String line = _taxiSimulator.getPlottableMap()[i];
            for (int j = 0; j < line.length(); j++) {
                if(line.charAt(j) == Utils.client){
                    if(!(imgLabels.get(i).get(j).getIcon() == Person1 
                        ||  imgLabels.get(i).get(j).getIcon() == Person2
                        ||  imgLabels.get(i).get(j).getIcon() == Person3 
                        ||  imgLabels.get(i).get(j).getIcon() == Person4)){
                        int  n =  (int )(Math.random() * 4 + 1);
                        imgLabels.get(i).get(j).setIcon(Icons.get((char) n));
                    }
                }
                else if(imgLabels.get(i).get(j).getIcon() != Icons.get(line.charAt(j))){
                    imgLabels.get(i).get(j).setIcon(Icons.get(line.charAt(j)));
                }
            }
        }
    }
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The conStent of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PNLRoad = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout PNLRoadLayout = new javax.swing.GroupLayout(PNLRoad);
        PNLRoad.setLayout(PNLRoadLayout);
        PNLRoadLayout.setHorizontalGroup(
            PNLRoadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        PNLRoadLayout.setVerticalGroup(
            PNLRoadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PNLRoad, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PNLRoad, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TaxiFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TaxiFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TaxiFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TaxiFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                TaxiSimulator taxiSimulator = null;
                new TaxiFrame(taxiSimulator).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PNLRoad;
    // End of variables declaration//GEN-END:variables
}
