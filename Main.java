

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Fonction Main. Elle initialise la connexion à la base de données, puis appelle les différentes méthodes
 * créant les requêtes. Elle ferme ensuite la connexion. Toutes les méthodes requetes pouvant lancer des 
 * SQLException, la méthode Main les attrapent.
 * @author Fabien
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/testmigration", "root", "");
            TypesGroupements.requetes(con);
            Groupe.requetes(con);
            SousGroupe.requetes(con);
            Ordre.requetes(con);
            SuperFamille.requetes(con);
            Famille.requetes(con);
            SousFamille.requetes(con);
            Espece.requetes(con);
            Divers.requetes(con);
            Suppressions.requetes(con);
            con.close();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
