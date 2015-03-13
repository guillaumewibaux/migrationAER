/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.*;

/**
 *
 * @author Fabien
 */
public class Divers {
    
    public static void requetes(Connection con) throws SQLException{
        
        //création de l'attribut fiche_id_papier dans la table fiche
        
        String query = "ALTER TABLE fiche ADD (fiche_id_papier int(11));";
        Statement stmt = con.createStatement();
        stmt.executeUpdate(query);
        
        //ajout de la clé étrangère entre fiche et utm
        
        query = "ALTER TABLE fiche ADD CONSTRAINT fk_fiche_utm FOREIGN KEY (fiche_utm_utm) REFERENCES utms(utm);";
        stmt.executeUpdate(query);
    }
}
