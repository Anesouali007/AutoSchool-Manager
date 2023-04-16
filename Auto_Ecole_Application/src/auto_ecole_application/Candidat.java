/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auto_ecole_application;

import java.awt.Image;
import java.awt.event.MouseEvent;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.Period;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author yazid
 */
public class Candidat extends javax.swing.JFrame {

    public Connection connection_db() throws SQLException {
        con = DriverManager.getConnection("jdbc:mysql://localhost/auto_ecole", "root", "");
        return con;
    }
    Connection con = null;
    Statement st = null;
    ResultSet rs = null;
    DefaultTableModel model;

    /**
     * Creates new form Candidat
     */
    public Candidat() {
        initComponents();
        selectionner_candidats();
        statistique();
    }

    public void statistique() {
        try {
            con = connection_db();
            String query1 = "SELECT COUNT(*) FROM candidats";
            Statement stmt1 = con.createStatement();
            ResultSet rs1 = stmt1.executeQuery(query1);
            rs1.next();
            int count1 = rs1.getInt(1);
            nbr_voiture.setText("" + count1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void selectionner_candidats() {
        try {
            con = connection_db();
            PreparedStatement st = con.prepareStatement("select * from candidats");
            rs = st.executeQuery();
            while (rs.next()) {
                String id = rs.getString("id_candidat");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String nom_prenom = nom + " " + prenom;
                String indice_pays = rs.getString("telephone");
                String ind_pays = indice_pays.substring(1, 3);
                String telephone = rs.getString("telephone");
                String tele = telephone.substring(3, 12);
                Date date_naissance = rs.getDate("date_naissance");
                String adresse = rs.getString("adresse");
                String email = rs.getString("email");
                String carte = rs.getString("carte_identite");
                String sexes=rs.getString("sexe");
                String Date = rs.getString("Date_de_creation").substring(0, 16);
                Object[] obj = {id, nom_prenom, date_naissance, adresse, telephone, email, sexes,carte, Date};
                model = (DefaultTableModel) tableau_candidat.getModel();
                model.addRow(obj);
            }
            tableau_candidat.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    table1MouseReleased(evt);
                }

                private void table1MouseReleased(MouseEvent evt) {
                    int selectionner = tableau_candidat.getSelectedRow();
                    String string_selectionner = String.valueOf(selectionner);
                    if (est_chiffre(string_selectionner) == false) {
                        JOptionPane.showMessageDialog(null, "Aucun Enregistrement n'est selectionné !:", null, JOptionPane.ERROR_MESSAGE);
                    } else {
                        DefaultTableModel model = (DefaultTableModel) tableau_candidat.getModel();
                        try {
                            con = connection_db();
                            PreparedStatement st = con.prepareStatement("select * from candidats where id_candidat=?");
                            st.setString(1, model.getValueAt(selectionner, 0).toString());
                            rs = st.executeQuery();
                            while (rs.next()) {
                                String id_sel = rs.getString("id_candidat");
                                Blob blob1 = rs.getBlob("photo");
                                byte[] imagebyte = blob1.getBytes(1, (int) blob1.length());
                                ImageIcon imag = new ImageIcon(new ImageIcon(imagebyte).getImage().getScaledInstance(300, 300, Image.SCALE_DEFAULT));
                                ajouter_image.setIcon(imag);
                                titre_veh.setText("Candidat N° : ");
                                id_veh.setText(id_sel);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Erreur 404 :" + ex.getMessage(), null, JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur 404 :" + e.getMessage(), null, JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean est_chiffre(String chaine) {
        boolean est_chiifre = true;
        for (int i = 0; i < chaine.length(); i++) {
            if (!Character.isDigit(chaine.charAt(i))) {
                est_chiifre = false;
            }
        }
        return est_chiifre;
    }
    public int calculer_age(java.sql.Date Date_naissance) {
        LocalDate date_naissance = Date_naissance.toLocalDate();
        LocalDate date_actuel = LocalDate.now();
        Period difference = Period.between(date_naissance, date_actuel);
        int age = difference.getYears();
        return age;
    }

    public void rechercher_candidat() {
        String type_recherche = type_rech.getSelectedItem().toString();
        String numero_recherche = recherche.getText();
        if (numero_recherche.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vous devez remplir le champs  !", null, JOptionPane.ERROR_MESSAGE);
        } else {
            if (est_chiffre(numero_recherche) == false) {
                JOptionPane.showMessageDialog(null, "Numéro saisie est incorrecte !", null, JOptionPane.ERROR_MESSAGE);
            } else {
                switch (type_recherche) {
                    case "ID Candidat":
                        permuter_ligne_tableau(numero_recherche);
                        break;
                    case "Carte identité":
                        if (numero_recherche.length() != 9) {
                            JOptionPane.showMessageDialog(null, "Le numero de la carte  doit être 9 chiffres !", null, JOptionPane.ERROR_MESSAGE);
                        }
                        try {
                            con = connection_db();
                            PreparedStatement st = con.prepareStatement("select * from candidats where carte_identite=?");
                            st.setString(1, numero_recherche);
                            rs = st.executeQuery();
                            while (rs.next() == true) {
                                String id = rs.getString("id_candidat");
                                permuter_ligne_tableau(id);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, ex.getMessage(), null, JOptionPane.ERROR_MESSAGE);
                        }
                        break;
                }
            }
        }
    }

    public void permuter_ligne_tableau(String numero_recherche) {
        boolean trouvé = false;
        int colonne_id = model.findColumn("ID candidat");
        for (int row = 0; row < model.getRowCount(); row++) {
            Object value_id = model.getValueAt(row, colonne_id);
            Object value_nom_prenom = model.getValueAt(row, 1);
            Object value_date_naiss = model.getValueAt(row, 2);
            Object value_adresse = model.getValueAt(row, 3);
            Object value_tele = model.getValueAt(row, 4);
            Object value_email = model.getValueAt(row, 5);
            Object value_carte_id = model.getValueAt(row, 6);
            Object value_date = model.getValueAt(row, 7);
            if (value_id.equals(numero_recherche)) {
                Object ancien_id = model.getValueAt(0, 0);
                Object ancien_nom_prenom = model.getValueAt(0, 1);
                Object ancien_date_naiss = model.getValueAt(0, 2);
                Object ancien_adresse = model.getValueAt(0, 3);
                Object ancien_tele = model.getValueAt(0, 4);
                Object ancien_email = model.getValueAt(0, 5);
                Object ancien_carte_id = model.getValueAt(0, 6);
                Object ancien_date = model.getValueAt(0, 7);
                JOptionPane.showMessageDialog(null, "Candidat est trouvé !", null, JOptionPane.INFORMATION_MESSAGE);
                trouvé = true;
                model.setValueAt(value_id, 0, 0);
                model.setValueAt(value_nom_prenom, 0, 1);
                model.setValueAt(value_date_naiss, 0, 2);
                model.setValueAt(value_adresse, 0, 3);
                model.setValueAt(value_tele, 0, 4);
                model.setValueAt(value_email, 0, 5);
                model.setValueAt(value_carte_id, 0, 6);
                model.setValueAt(value_date, 0, 7);
                model.setValueAt(ancien_id, row, 0);
                model.setValueAt(ancien_nom_prenom, row, 1);
                model.setValueAt(ancien_date_naiss, row, 2);
                model.setValueAt(ancien_adresse, row, 3);
                model.setValueAt(ancien_tele, row, 4);
                model.setValueAt(ancien_email, row, 5);
                model.setValueAt(ancien_carte_id, row, 6);
                model.setValueAt(ancien_date, row, 7);
            }
        }
        if (trouvé == false) {
            JOptionPane.showMessageDialog(null, "Candidat    n'est  pas trouvé !", null, JOptionPane.ERROR_MESSAGE);
        }
    }

    /*  public void mise_a_jour() {
        int rows = model.getRowCount();
        String[] ids = new String[rows];
        String[] noms = new String[rows];
        String[] prenom = new String[rows];
        String[] date_naiss = new String[rows];
        String[] adresses = new String[rows];
        String[] telephones = new String[rows];
        String[] emails = new String[rows];
        String[] carte_ids = new String[rows];
        String[] date_ins = new String[rows];
        try {
            con = connection_db();
            PreparedStatement st = con.prepareStatement("select * from candidats");
            rs = st.executeQuery();
            int i = 0;
            while (rs.next() && i < rows) {
                ids[i] = rs.getString("id_candidat");
                noms[i] = rs.getString("nom");
                prenom[i] = rs.getString("prenom");
                date_naiss[i] = rs.getString("date_naissance");
                adresses[i] = rs.getString("adresse");
                telephones[i]=rs.getString("telephone");
                emails[i] = rs.getString("email");
                carte_ids[i]=rs.getString("carte_identite");
                date_ins[i]=rs.getString("Date_de_creation");
                i++;
            }
            for (int j = 0; j < ids.length; j++) {
                model.setValueAt(ids[j], j, 0);
                model.setValueAt(noms[j]+" "+prenom[j], j, 1);
                model.setValueAt(date_naiss[j], j, 2);
                model.setValueAt(adresses[j], j, 3);
                model.setValueAt(telephones[j], j, 4);
                model.setValueAt(emails[j], j, 5);
                model.setValueAt(carte_ids[j], j, 6);
                model.setValueAt(date_ins[j], j, 7);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur 404 :" + ex.getMessage(), null, JOptionPane.ERROR_MESSAGE);
        }
    }
     */
    public void supprimer_candidat() {
        String id = id_veh.getText();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Aucun Candidat n'est selectionné ! ", null, JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                String delete = "delete from candidats where id_candidat=?";
                PreparedStatement st = con.prepareStatement(delete);
                st.setString(1, id);
                st.executeUpdate();
                JOptionPane.showMessageDialog(null, "Profil d'un candidat supprimé avec succès ", null, JOptionPane.INFORMATION_MESSAGE);
                titre_veh.setText("");
                id_veh.setText("");
                ajouter_image.setIcon(null);
                this.dispose();
                Candidat c2 = new Candidat();
                c2.setVisible(true);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erreur 404 :" + ex.getMessage(), null, JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void afficher_sur_formulaire() {
        if (id_veh.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Aucun Véhicule n'est selectionné :", null, JOptionPane.ERROR_MESSAGE);
        } else {
            ajouter_candidat ajouter = new ajouter_candidat(this);
            ajouter.setVisible(true);
            ajouter.affichage(id_veh.getText());
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

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        date_actuel_var = new javax.swing.JLabel();
        jour_semaine = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        id_veh = new javax.swing.JLabel();
        titre_veh = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableau_candidat = new rojerusan.RSTableMetro();
        ajouter_image = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        type_rech = new rojerusan.RSComboMetro();
        recherche = new app.bolivia.swing.JCTextField();
        jLabel14 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        nbr_voiture = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 3, new java.awt.Color(0, 102, 153)));

        jPanel2.setBackground(new java.awt.Color(0, 102, 153));
        jPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel2MouseClicked(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Algerian", 1, 36)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("X");
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Admin");

        jLabel7.setIcon(new javax.swing.ImageIcon("C:\\Users\\yazid\\OneDrive\\Bureau\\Auto_Ecole\\icons\\male_user_50px.png")); // NOI18N

        date_actuel_var.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        date_actuel_var.setForeground(new java.awt.Color(255, 255, 255));

        jour_semaine.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        jour_semaine.setForeground(new java.awt.Color(255, 255, 255));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons/icons8_Rewind_48px.png"))); // NOI18N
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel6MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addGap(407, 407, 407)
                .addComponent(jour_semaine)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(date_actuel_var)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addGap(26, 26, 26)
                .addComponent(jLabel4)
                .addGap(38, 38, 38)
                .addComponent(jLabel3)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(jLabel4)
                        .addComponent(jLabel5)
                        .addComponent(jLabel7)
                        .addComponent(date_actuel_var)
                        .addComponent(jour_semaine)))
                .addGap(22, 22, 22))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createMatteBorder(10, 3, 0, 3, new java.awt.Color(0, 102, 153)));

        jLabel19.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel19.setText("Liste des candidats");

        jPanel5.setBackground(new java.awt.Color(0, 102, 153));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        jLabel8.setBackground(new java.awt.Color(255, 255, 255));
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Blue-Add-Button-PNG.png"))); // NOI18N
        jLabel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel8MouseClicked(evt);
            }
        });

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/delete.png"))); // NOI18N
        jLabel9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel9MouseClicked(evt);
            }
        });

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/edit.png"))); // NOI18N
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });

        id_veh.setFont(new java.awt.Font("Arial", 3, 24)); // NOI18N

        titre_veh.setFont(new java.awt.Font("Arial", 3, 24)); // NOI18N

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/email-2.png"))); // NOI18N
        jLabel11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel11MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titre_veh)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(id_veh)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(32, 32, 32))
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(505, 505, 505)
                .addComponent(jLabel11)
                .addGap(32, 32, 32)
                .addComponent(jLabel2)
                .addGap(34, 34, 34)
                .addComponent(jLabel9)
                .addGap(33, 33, 33)
                .addComponent(jLabel8)
                .addGap(38, 38, 38))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(id_veh)
                            .addComponent(titre_veh))
                        .addGap(36, 36, 36))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel19)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(11, 11, 11)))
                        .addGap(21, 21, 21))))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 2, 10, 2, new java.awt.Color(0, 102, 153)));

        tableau_candidat.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID candidat", "Nom et Prenom", "Date naissance", "adresse", "telephone", "email", "sexe", "carte identité", "Date inscription"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableau_candidat.setColorBackgoundHead(new java.awt.Color(0, 102, 153));
        tableau_candidat.setColorFilasBackgound2(new java.awt.Color(255, 255, 255));
        tableau_candidat.setColorFilasForeground1(new java.awt.Color(0, 0, 0));
        tableau_candidat.setColorFilasForeground2(new java.awt.Color(0, 0, 0));
        tableau_candidat.setColorSelBackgound(new java.awt.Color(0, 102, 153));
        tableau_candidat.setFillsViewportHeight(true);
        tableau_candidat.setFocusCycleRoot(true);
        tableau_candidat.setFuenteFilas(new java.awt.Font("Arial", 1, 18)); // NOI18N
        tableau_candidat.setFuenteFilasSelect(new java.awt.Font("Arial", 1, 18)); // NOI18N
        tableau_candidat.setRowHeight(50);
        jScrollPane1.setViewportView(tableau_candidat);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE)
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(102, 102, 255)));

        jLabel10.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel10.setText("Gérer vos Candidats");

        jPanel10.setBackground(new java.awt.Color(0, 102, 153));

        type_rech.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "ID Candidat", "Carte identité" }));
        type_rech.setColorBorde(new java.awt.Color(255, 255, 255));
        type_rech.setColorFondo(new java.awt.Color(0, 102, 153));
        type_rech.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N

        recherche.setBackground(new java.awt.Color(0, 102, 153));
        recherche.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        recherche.setForeground(new java.awt.Color(255, 255, 255));
        recherche.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        recherche.setOpaque(false);
        recherche.setPhColor(new java.awt.Color(255, 255, 255));
        recherche.setPlaceholder("Tapez votre recherche Ici ..");
        recherche.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rechercheActionPerformed(evt);
            }
        });

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ser.png"))); // NOI18N
        jLabel14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel14MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(type_rech, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(recherche, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 76, Short.MAX_VALUE)
                .addComponent(jLabel14)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel14)
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(type_rech, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(recherche, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(27, 27, 27))
        );

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 0, 2, 0, new java.awt.Color(0, 0, 0)));

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel12.setText("Totale");

        nbr_voiture.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        nbr_voiture.setText("5");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(nbr_voiture)))
                .addGap(22, 22, 22))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12)
                .addGap(26, 26, 26)
                .addComponent(nbr_voiture)
                .addContainerGap(29, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(66, 66, 66)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 545, Short.MAX_VALUE)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31))
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addGap(678, 678, 678)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(679, Short.MAX_VALUE)))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(70, 70, 70)
                        .addComponent(jLabel10)))
                .addContainerGap(34, Short.MAX_VALUE))
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addGap(30, 30, 30)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(31, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 84, Short.MAX_VALUE)
                        .addComponent(ajouter_image, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(ajouter_image, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_jLabel3MouseClicked

    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked
        // TODO add your handling code here:
        this.dispose();

    }//GEN-LAST:event_jLabel6MouseClicked

    private void jLabel8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel8MouseClicked
        // TODO add your handling code here:
        ajouter_candidat ajou = new ajouter_candidat(this);
        ajou.setVisible(true);

    }//GEN-LAST:event_jLabel8MouseClicked

    private void jLabel9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel9MouseClicked
        // TODO add your handling code here:
        supprimer_candidat();

    }//GEN-LAST:event_jLabel9MouseClicked

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        // TODO add your handling code here:
        afficher_sur_formulaire();

    }//GEN-LAST:event_jLabel2MouseClicked

    private void rechercheActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rechercheActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rechercheActionPerformed

    private void jLabel14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel14MouseClicked
        // TODO add your handling code here:
        rechercher_candidat();
    }//GEN-LAST:event_jLabel14MouseClicked

    private void jPanel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MouseClicked
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jPanel2MouseClicked

    private void jLabel11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel11MouseClicked
        // TODO add your handling code here:
        if (id_veh.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Aucun Candidat n'est selectionné ! ", null, JOptionPane.ERROR_MESSAGE);
        } else {
            String id_s = id_veh.getText();
            try {
                con = connection_db();
                PreparedStatement st = con.prepareStatement("select * from candidats where id_candidat=?");
                st.setString(1, id_s);
                rs = st.executeQuery();
                if (rs.next()) {
                    String email = rs.getString("email");
                    message_form msg = new message_form();
                    msg.envoi(email);
                    msg.setVisible(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_jLabel11MouseClicked

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
            java.util.logging.Logger.getLogger(Candidat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Candidat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Candidat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Candidat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Candidat().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ajouter_image;
    private javax.swing.JLabel date_actuel_var;
    private javax.swing.JLabel id_veh;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel jour_semaine;
    private javax.swing.JLabel nbr_voiture;
    private app.bolivia.swing.JCTextField recherche;
    private rojerusan.RSTableMetro tableau_candidat;
    private javax.swing.JLabel titre_veh;
    private rojerusan.RSComboMetro type_rech;
    // End of variables declaration//GEN-END:variables
}
