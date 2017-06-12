/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.Client;
import controller.TaxiSimulator;
import controller.Utils;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

/**
 *
 * @author joseb
 */
public class TaxiFrame extends javax.swing.JFrame implements Observer{

    /**
     * Creates new form TaxiFrame
     */
    
    private static controller.Map _map;
    
    private static final int SIZE = 35;
    private static final ImageIcon BusyTaxiDown = new ImageIcon(new ImageIcon("src/img/BusyTaxiDown.jpg").getImage().getScaledInstance(SIZE, SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon BusyTaxiLeft = new ImageIcon(new ImageIcon("src/img/BusyTaxiLeft.jpg").getImage().getScaledInstance(SIZE, SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon BusyTaxiRight = new ImageIcon(new ImageIcon("src/img/BusyTaxiRight.jpg").getImage().getScaledInstance(SIZE, SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon BusyTaxiUp = new ImageIcon(new ImageIcon("src/img/BusyTaxiUp.jpg").getImage().getScaledInstance(SIZE, SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon Dirt = (new ImageIcon("src/img/Dirt.jpg"));
    private static final ImageIcon Grass = new ImageIcon(new ImageIcon("src/img/Grass.jpg").getImage().getScaledInstance(SIZE, SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon Road = new ImageIcon(new ImageIcon("src/img/Road.jpg").getImage().getScaledInstance(SIZE, SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon Person1 = new ImageIcon(new ImageIcon("src/img/Person1.jpg").getImage().getScaledInstance(SIZE, SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon Person2 = new ImageIcon(new ImageIcon("src/img/Person2.jpg").getImage().getScaledInstance(SIZE, SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon Person3 = new ImageIcon(new ImageIcon("src/img/Person3.jpg").getImage().getScaledInstance(SIZE, SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon Person4 = new ImageIcon(new ImageIcon("src/img/Person4.jpg").getImage().getScaledInstance(SIZE, SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon RoadRoute = new ImageIcon(new ImageIcon("src/img/RoadRoute.jpg").getImage().getScaledInstance(SIZE, SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon Sidewalk = new ImageIcon(new ImageIcon("src/img/Sidewalk.jpg").getImage().getScaledInstance(SIZE, SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon Smoke = new ImageIcon(new ImageIcon("src/img/Smoke.jpg").getImage().getScaledInstance(SIZE, SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon TaxiDown = new ImageIcon(new ImageIcon("src/img/TaxiDown.jpg").getImage().getScaledInstance(SIZE, SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon TaxiLeft = new ImageIcon(new ImageIcon("src/img/TaxiLeft.jpg").getImage().getScaledInstance(SIZE, SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon TaxiRight = new ImageIcon(new ImageIcon("src/img/TaxiRight.jpg").getImage().getScaledInstance(SIZE, SIZE, Image.SCALE_SMOOTH));
    private static final ImageIcon TaxiUp = new ImageIcon(new ImageIcon("src/img/TaxiUp.jpg").getImage().getScaledInstance(SIZE, SIZE, Image.SCALE_SMOOTH));
    
    private static final Map<Character, ImageIcon> Icons;
    static{
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
    private JLabel hour;
    
    public TaxiFrame(controller.Map pMap) {
        initComponents();
        setResizable(false);
        _map = pMap;
        addLabels();
        setLocationRelativeTo(null);
        JLabel background = new JLabel(Dirt);
        PNLRoad.add(background);
        background.setSize(2000, 2000);
        background.setVisible(true);
        
    }
    
    public void seeBlockClients(int x, int y){
        ArrayList<Client> clients = (ArrayList<Client>) _map.getClients().clone();
        int minX = x-2;int maxX = x+2;
        int minY = y-2;int maxY = y+2;
        int clientsQtt = 0;
        for(Client client : clients){
            int clientX = client.getActualPosition().x;
            int clientY = client.getActualPosition().y;
            if(minX<=clientX && clientX<=maxX && minY<=clientY && clientY<=maxY ) {
                clientsQtt++;
            }
        }
        
        ArrayList<TaxiSimulator> taxis = (ArrayList<TaxiSimulator>) _map.getTaxis().clone();
        String taxisOnPoint="";
        for(TaxiSimulator taxi : taxis){
            if(taxi.getTaxiLocation().x == x && taxi.getTaxiLocation().y == y){
                if(taxisOnPoint.equals("")){taxisOnPoint="\nTaxi: \""+taxi.getTaxiName()+"\"";}
                else{taxisOnPoint+="\nTaxi: \""+taxi.getTaxiName()+"\"";}
            }
        }
        if(clientsQtt>0){
            JOptionPane.showMessageDialog(this,"Postion ("+x+","+y+") \nClients:"+clientsQtt+taxisOnPoint);
        }
        else{
            JOptionPane.showMessageDialog(this,"Postion ("+x+","+y+")"+taxisOnPoint);
        }
    }
    
    private void addLabels(){
        int width=0,height= _map.getPlottableMap().length;
        for (int i = 0; i < _map.getPlottableMap().length; i++) {
            String line = _map.getPlottableMap()[i];
            width = Integer.max(width, line.length());
            ArrayList<JLabel> labels = new ArrayList<>();
            for (int j = 0; j < line.length(); j++) {
                JLabel thumb;
                if(Icons.containsKey(line.charAt(j))){
                    thumb = new JLabel(Icons.get(line.charAt(j)));
                }
                else{
                    thumb = new JLabel("<html><span style='font-size:"+(SIZE-3)+"px;color:white;'>"+line.charAt(j)+"</span></html>");
                    thumb.setHorizontalTextPosition(thumb.CENTER);
                    thumb.setIcon(Road);
                }
                PNLRoad.add(thumb);
                thumb.setLocation(j * SIZE , i * SIZE);
                thumb.setSize(SIZE, SIZE);
                thumb.setVisible(true);
                JFrame frame = this;
                final int x = i; 
                final int y = j;
                thumb.addMouseListener(new MouseAdapter()  {  
                    public void mouseClicked(MouseEvent e){
                        seeBlockClients(x,y);
                    }  
                }); 
                labels.add(thumb);
            }
            imgLabels.add(labels);
            
        }
        this.setSize((width) * SIZE, (height) * SIZE + 32 + 23);
        
        hour = new JLabel("<html><span style='font-size:"+(SIZE)+"px;color:white;'>"+_map.getHour()+":00</span></html>",SwingConstants.RIGHT);
        PNLRoad.add(hour);
        hour.setLocation((int)((width) * SIZE - (SIZE*3.2)), 0);
        hour.setSize(SIZE*3, SIZE);
        hour.setVisible(true);
    }
    
    public void refeshLabels(){
        for (int i = 0; i < _map.getPlottableMap().length; i++) {
            String line = _map.getPlottableMap()[i];
            for (int j = 0; j < line.length(); j++) {
                if(String.valueOf(line.charAt(j)).matches("[a-zA-Z]")){}
                else if(imgLabels.get(i).get(j).getIcon() != Icons.get(line.charAt(j))){
                    imgLabels.get(i).get(j).setIcon(Icons.get(line.charAt(j)));
                }
            }
        }
        hour.setText("<html><span style='font-size:"+(SIZE-3)+"px;color:white;'>"+_map.getHour()+":00</span></html>");
    }
    
    @Override
    public void update(Observable o, Object arg) {
        refeshLabels();
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
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout PNLRoadLayout = new javax.swing.GroupLayout(PNLRoad);
        PNLRoad.setLayout(PNLRoadLayout);
        PNLRoadLayout.setHorizontalGroup(
            PNLRoadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        PNLRoadLayout.setVerticalGroup(
            PNLRoadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 274, Short.MAX_VALUE)
        );

        jMenu1.setText("File");

        jMenuItem1.setText("Add Taxi");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("Add Client");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

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

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        Integer intX = Utils.askInt("Please input the X location: ","X can only be positive.",this);
        if(intX == null){return;}
        
        Integer intY = Utils.askInt("Please input the Y location: ","Y can only be positive.",this);
        if(intY == null){return;}
        
        String taxiName = JOptionPane.showInputDialog("Please input the taxi name: ");
        if(taxiName==null)return;
        
        int resp = _map.addTaxi(intX, intY, taxiName);
        if(resp == -1){
            JOptionPane.showMessageDialog(this,"That's an invalid position.","Error",JOptionPane.ERROR_MESSAGE);
        }
        if(resp == -2) {
            JOptionPane.showMessageDialog(this,"That's an invalid name (already exists).","Error",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        Integer intHomeX = Utils.askInt("Please input the X home location: ","X can only be positive.",this);
        if(intHomeX == null){return;}

        Integer intHomeY = Utils.askInt("Please input the Y home location: ","Y can only be positive.",this);
        if(intHomeY == null){return;}
        
        Integer intGoHome = Utils.askInt("Please input the time when he go to home: ","The time can only be positive.",this);
        if(intGoHome == null){return;}
        
        Integer intJobX = Utils.askInt("Please input the X job location: ","X can only be positive.",this);
        if(intJobX == null){return;}
        
        Integer intJobY = Utils.askInt("Please input the Y job location: ","Y can only be positive.",this);
        if(intJobY == null){return;}
        
        Integer intGoJob = Utils.askInt("Please input the time when he go to work: ","The time can only be positive.",this);
        if(intGoJob == null){return;}
        
        _map.addClient(intHomeX, intHomeY, intGoHome, intJobX, intJobY, intGoJob);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

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
                controller.Map map = null;
                new TaxiFrame(map).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PNLRoad;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    // End of variables declaration//GEN-END:variables

}
