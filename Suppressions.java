/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.*;

/**
 * Classe faisant les suppressions des attributs et tables obsolètes.
 * @author Fabien
 */
public class Suppressions {
    
    public static void requetes(Connection con) throws SQLException{
        
        //suppressions des deux attributs devenus inutiles de la table espece
        
        String query = "ALTER TABLE espece DROP COLUMN espece_sous_groupe_sous_groupe_id;";
        Statement stmt = con.createStatement();
        stmt.executeUpdate(query);
        
        query = "ALTER TABLE espece DROP COLUMN espece_sous_famille_sous_famille_id;";
        stmt.executeUpdate(query);
        
        //suppressions des tables devenues inutiles
        
        query = "DROP TABLE IF EXISTS espece_has_sous_groupe;";
        stmt.executeUpdate(query);
        
        query = "DROP TABLE IF EXISTS sous_famille_has_sous_groupe;";
        stmt.executeUpdate(query);
        
        query = "DROP TABLE IF EXISTS famille_has_sous_groupe;";
        stmt.executeUpdate(query);
        
        query = "DROP TABLE IF EXISTS super_famille_has_sous_groupe;";
        stmt.executeUpdate(query);
        
        query = "DROP TABLE IF EXISTS sous_groupe;";
        stmt.executeUpdate(query);
        
        query = "DROP TABLE IF EXISTS sous_famille;";
        stmt.executeUpdate(query);
        
        query = "DROP TABLE IF EXISTS famille;";
        stmt.executeUpdate(query);
        
        query = "DROP TABLE IF EXISTS super_famille;";
        stmt.executeUpdate(query);
}
    }
    
