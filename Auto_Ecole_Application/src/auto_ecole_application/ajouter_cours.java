/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auto_ecole_application;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;
/**
 *
 * @author yazid
 */
public class ajouter_cours extends javax.swing.JFrame {

    Candidat c1 = new Candidat();
    Connection con;
    Statement st;
    ResultSet rs;

    /**
     * Creates new form ajouter_cours
     */
    public ajouter_cours() {
        initComponents();
        selectionner_candidat();
        selectionner_moniteurs();
        LocalDate date_de_jour = LocalDate.now();
        DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String date_actuell_maintenant = date_de_jour.format(formater);
    }

    public void selectionner_candidat() {
        try {
            con = c1.connection_db();
            PreparedStatement st = con.prepareStatement("select id_candidat from candidats");
            rs = st.executeQuery();

            while (rs.next()) {
                Object ids = rs.getString(1);
                candidat_choix.addItem(ids);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur :" + ex.getMessage(), null, JOptionPane.ERROR_MESSAGE);
        }
    }

    public void selectionner_moniteurs() {
        try {
            con = c1.connection_db();
            PreparedStatement st = con.prepareStatement("select id_moniteur from moniteurs");
            rs = st.executeQuery();
            while (rs.next()) {
                Object ids = rs.getString(1);
                choix_moniteur.addItem(ids);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur :" + ex.getMessage(), null, JOptionPane.ERROR_MESSAGE);
        }
    }

    public void selectionner_vehicule() {
        try {
            con = c1.connection_db();
            PreparedStatement st = con.prepareStatement("select id_vehicule from vehicules");
            rs = st.executeQuery();
            while (rs.next()) {
                Object ids = rs.getString(1);
                vehicule_choix.addItem(ids);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur :" + ex.getMessage(), null, JOptionPane.ERROR_MESSAGE);
        }
    }

    public void type_seance() {
        String type_seance = seance_choix.getSelectedItem().toString();
        switch (type_seance) {
            case "Aucun":
                JOptionPane.showMessageDialog(null, "Vous de devez choisir un type de séance !", null, JOptionPane.ERROR_MESSAGE);
                break;
            case "Code":
                label_veh.setText("Pas besoin du vehicule");
                vehicule_choix.removeAllItems();
                break;
            case "Créneau":
                label_veh.setText("Véhicule");
                selectionner_vehicule();
                break;
            case "Conduite":
                label_veh.setText("Véhicule");
                selectionner_vehicule();
                break;
        }
    }

    public boolean valider_formulaire() {
        java.util.Date date_naissance = date_cours.getDatoFecha();
        Long date_date = date_naissance.getTime();
        java.sql.Date date_cours_sql = new java.sql.Date(date_date);
        boolean valide = true;
        if (time1.getText().isEmpty() || time.getText().isEmpty() || seance_choix.getSelectedItem().toString() == "Aucun") {
            JOptionPane.showMessageDialog(null, "Vous devez remplir tout les champs !", null, JOptionPane.ERROR_MESSAGE);
            valide = false;
        } else {
            if (comparer_date(date_cours_sql) <= 0) {
                JOptionPane.showMessageDialog(null, "La date du cours  Doit être au mois supérieur d'une journée de la date actuelle !", null, JOptionPane.ERROR_MESSAGE);
                valide = false;
            }
        }   
        return valide;
    }
    public int comparer_date(java.sql.Date Date_en_parametre) {
        LocalDate date_saisi = Date_en_parametre.toLocalDate();
        LocalDate date_actuel = LocalDate.now();
        Period difference = Period.between(date_actuel, date_saisi);
        int diff = difference.getDays();
        return diff;
    }
    public void inserer_cours() {
        if (valider_formulaire() == true) {
            String vehicule = vehicule_choix.getSelectedItem().toString();
            String candidat = candidat_choix.getSelectedItem().toString();
            String moniteur = choix_moniteur.getSelectedItem().toString();
            String types = seance_choix.getSelectedItem().toString();
            if ((types == "Créneau" || types == "Conduite") && vehicule == "Aucun") {
                JOptionPane.showMessageDialog(null, "Il est obligatoirement de choisir un véhicule !:", null, JOptionPane.ERROR_MESSAGE);
            }
            try {
                
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erreur :" + ex.getMessage(), null, JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        rSYearDateBeanInfo1 = new rojeru_san.componentes.RSYearDateBeanInfo();
        roboto1 = new efectos.Roboto();
        timePicker1 = new com.raven.swing.TimePicker();
        timePicker2 = new com.raven.swing.TimePicker();
        kGradientPanel3 = new keeptoo.KGradientPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        rSMaterialButtonCircle1 = new rojerusan.RSMaterialButtonCircle();
        rSMaterialButtonCircle2 = new rojerusan.RSMaterialButtonCircle();
        vehicule_n = new javax.swing.JLabel();
        id_vehicule = new javax.swing.JLabel();
        candidat_choix = new rojerusan.RSComboMetro();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        seance_choix = new rojerusan.RSComboMetro();
        label_veh = new javax.swing.JLabel();
        vehicule_choix = new rojerusan.RSComboMetro();
        date_cours = new rojeru_san.componentes.RSDateChooser();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        time1 = new app.bolivia.swing.JCTextField();
        jLabel16 = new javax.swing.JLabel();
        time = new app.bolivia.swing.JCTextField();
        choix_moniteur = new rojerusan.RSComboMetro();

        timePicker1.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(255, 255, 255)));
        timePicker1.setForeground(new java.awt.Color(0, 102, 153));
        timePicker1.setDisplayText(time);

        timePicker2.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(255, 255, 255)));
        timePicker2.setForeground(new java.awt.Color(0, 102, 153));
        timePicker2.setDisplayText(time1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        kGradientPanel3.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(255, 255, 255)));
        kGradientPanel3.setkEndColor(new java.awt.Color(0, 102, 153));
        kGradientPanel3.setkGradientFocus(1000);
        kGradientPanel3.setkStartColor(new java.awt.Color(0, 102, 153));

        jLabel3.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("X");
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 4, Short.MAX_VALUE)
        );

        rSMaterialButtonCircle1.setBackground(new java.awt.Color(255, 255, 255));
        rSMaterialButtonCircle1.setBorder(null);
        rSMaterialButtonCircle1.setForeground(new java.awt.Color(0, 102, 153));
        rSMaterialButtonCircle1.setText("Modifier");
        rSMaterialButtonCircle1.setToolTipText("");
        rSMaterialButtonCircle1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        rSMaterialButtonCircle1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rSMaterialButtonCircle1MouseClicked(evt);
            }
        });

        rSMaterialButtonCircle2.setBackground(new java.awt.Color(255, 255, 255));
        rSMaterialButtonCircle2.setBorder(null);
        rSMaterialButtonCircle2.setForeground(new java.awt.Color(0, 102, 153));
        rSMaterialButtonCircle2.setText("Ajouter");
        rSMaterialButtonCircle2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        rSMaterialButtonCircle2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rSMaterialButtonCircle2MouseClicked(evt);
            }
        });

        vehicule_n.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        vehicule_n.setForeground(new java.awt.Color(255, 255, 255));
        vehicule_n.setToolTipText("");

        id_vehicule.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        id_vehicule.setForeground(new java.awt.Color(255, 255, 255));
        id_vehicule.setToolTipText("");

        candidat_choix.setColorArrow(new java.awt.Color(0, 102, 153));
        candidat_choix.setColorBorde(new java.awt.Color(255, 255, 255));
        candidat_choix.setColorFondo(new java.awt.Color(0, 102, 153));
        candidat_choix.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        candidat_choix.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                candidat_choixItemStateChanged(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Candidat");

        jLabel12.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Moniteur");

        jLabel13.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Type de séance");

        seance_choix.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Aucun", "Code", "Créneau", "Conduite" }));
        seance_choix.setColorArrow(new java.awt.Color(0, 102, 153));
        seance_choix.setColorBorde(new java.awt.Color(255, 255, 255));
        seance_choix.setColorFondo(new java.awt.Color(0, 102, 153));
        seance_choix.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        seance_choix.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                seance_choixItemStateChanged(evt);
            }
        });

        label_veh.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        label_veh.setForeground(new java.awt.Color(255, 255, 255));
        label_veh.setText("Véhicule");

        vehicule_choix.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Aucun" }));
        vehicule_choix.setColorArrow(new java.awt.Color(0, 102, 153));
        vehicule_choix.setColorBorde(new java.awt.Color(255, 255, 255));
        vehicule_choix.setColorFondo(new java.awt.Color(0, 102, 153));
        vehicule_choix.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N

        date_cours.setBackground(new java.awt.Color(0, 102, 153));
        date_cours.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 1, 2, 1, new java.awt.Color(255, 255, 255)));
        date_cours.setForeground(new java.awt.Color(255, 204, 204));
        date_cours.setColorBackground(new java.awt.Color(0, 102, 153));
        date_cours.setColorButtonHover(new java.awt.Color(0, 0, 0));
        date_cours.setColorDiaActual(new java.awt.Color(255, 102, 255));
        date_cours.setColorForeground(new java.awt.Color(0, 0, 0));
        date_cours.setColorSelForeground(new java.awt.Color(255, 255, 102));
        date_cours.setColorTextDiaActual(new java.awt.Color(51, 51, 51));
        date_cours.setPlaceholder("Cliquez pour selectionner une date");

        jLabel14.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Heure debut");

        jLabel15.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Date du cours");

        time1.setBackground(new java.awt.Color(0, 102, 153));
        time1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        time1.setForeground(new java.awt.Color(255, 255, 255));
        time1.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        time1.setOpaque(false);
        time1.setPhColor(new java.awt.Color(255, 255, 255));
        time1.setPlaceholder("Cliquer pour ajouter une heure");
        time1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                time1MouseClicked(evt);
            }
        });
        time1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                time1ActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Heure de fin");

        time.setBackground(new java.awt.Color(0, 102, 153));
        time.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        time.setForeground(new java.awt.Color(255, 255, 255));
        time.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        time.setOpaque(false);
        time.setPhColor(new java.awt.Color(255, 255, 255));
        time.setPlaceholder("Cliquer pour ajouter une heure");
        time.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                timeMouseClicked(evt);
            }
        });
        time.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                timeActionPerformed(evt);
            }
        });

        choix_moniteur.setColorArrow(new java.awt.Color(0, 102, 153));
        choix_moniteur.setColorBorde(new java.awt.Color(255, 255, 255));
        choix_moniteur.setColorFondo(new java.awt.Color(0, 102, 153));
        choix_moniteur.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N

        javax.swing.GroupLayout kGradientPanel3Layout = new javax.swing.GroupLayout(kGradientPanel3);
        kGradientPanel3.setLayout(kGradientPanel3Layout);
        kGradientPanel3Layout.setHorizontalGroup(
            kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, kGradientPanel3Layout.createSequentialGroup()
                        .addGroup(kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel3Layout.createSequentialGroup()
                                .addComponent(vehicule_n)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(id_vehicule)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel3)))
                        .addContainerGap())
                    .addGroup(kGradientPanel3Layout.createSequentialGroup()
                        .addGroup(kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(kGradientPanel3Layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(rSMaterialButtonCircle2, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 85, Short.MAX_VALUE)
                                .addComponent(rSMaterialButtonCircle1, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, kGradientPanel3Layout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addGroup(kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(date_cours, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(kGradientPanel3Layout.createSequentialGroup()
                                        .addGroup(kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(candidat_choix, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel11)
                                            .addComponent(jLabel13)
                                            .addComponent(seance_choix, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel14)
                                            .addComponent(jLabel15)
                                            .addComponent(time1, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel3Layout.createSequentialGroup()
                                                .addComponent(jLabel12)
                                                .addGap(249, 249, 249))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(label_veh)
                                                .addComponent(vehicule_choix, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(time, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, kGradientPanel3Layout.createSequentialGroup()
                                                    .addComponent(jLabel16)
                                                    .addGap(206, 206, 206)))
                                            .addComponent(choix_moniteur, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                        .addGap(34, 34, 34))))
        );
        kGradientPanel3Layout.setVerticalGroup(
            kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(kGradientPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(vehicule_n)
                        .addComponent(id_vehicule))
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addGroup(kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12))
                .addGap(28, 28, 28)
                .addGroup(kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(candidat_choix, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(choix_moniteur, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(60, 60, 60)
                .addGroup(kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(label_veh))
                .addGap(28, 28, 28)
                .addGroup(kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(seance_choix, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(vehicule_choix, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(49, 49, 49)
                .addComponent(jLabel15)
                .addGap(26, 26, 26)
                .addComponent(date_cours, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(69, 69, 69)
                .addGroup(kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel16))
                .addGap(36, 36, 36)
                .addGroup(kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(time1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(time, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 75, Short.MAX_VALUE)
                .addGroup(kGradientPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rSMaterialButtonCircle2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rSMaterialButtonCircle1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(73, 73, 73))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(kGradientPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jLabel3MouseClicked

    private void rSMaterialButtonCircle1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rSMaterialButtonCircle1MouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_rSMaterialButtonCircle1MouseClicked

    private void rSMaterialButtonCircle2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rSMaterialButtonCircle2MouseClicked
        // TODO add your handling code here:
        inserer_cours();
    }//GEN-LAST:event_rSMaterialButtonCircle2MouseClicked

    private void seance_choixItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_seance_choixItemStateChanged
        // TODO add your handling code here:
        type_seance();
    }//GEN-LAST:event_seance_choixItemStateChanged

    private void time1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_time1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_time1ActionPerformed

    private void timeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_timeActionPerformed

    private void timeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_timeMouseClicked
        // TODO add your handling code here:
        timePicker1.showPopup(this, 250, 300);
    }//GEN-LAST:event_timeMouseClicked

    private void time1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_time1MouseClicked
        // TODO add your handling code here:
        timePicker2.showPopup(this, 250, 250);
    }//GEN-LAST:event_time1MouseClicked

    private void candidat_choixItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_candidat_choixItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_candidat_choixItemStateChanged

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
            java.util.logging.Logger.getLogger(ajouter_cours.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ajouter_cours.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ajouter_cours.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ajouter_cours.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ajouter_cours().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private rojerusan.RSComboMetro candidat_choix;
    private rojerusan.RSComboMetro choix_moniteur;
    private rojeru_san.componentes.RSDateChooser date_cours;
    private javax.swing.JLabel id_vehicule;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private keeptoo.KGradientPanel kGradientPanel3;
    private javax.swing.JLabel label_veh;
    private rojerusan.RSMaterialButtonCircle rSMaterialButtonCircle1;
    private rojerusan.RSMaterialButtonCircle rSMaterialButtonCircle2;
    private rojeru_san.componentes.RSYearDateBeanInfo rSYearDateBeanInfo1;
    private efectos.Roboto roboto1;
    private rojerusan.RSComboMetro seance_choix;
    private app.bolivia.swing.JCTextField time;
    private app.bolivia.swing.JCTextField time1;
    private com.raven.swing.TimePicker timePicker1;
    private com.raven.swing.TimePicker timePicker2;
    private rojerusan.RSComboMetro vehicule_choix;
    private javax.swing.JLabel vehicule_n;
    // End of variables declaration//GEN-END:variables
}
