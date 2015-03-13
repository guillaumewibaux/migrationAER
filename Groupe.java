/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.*;

/**
 * Classe créant les requêtes nécessaires à transformer les données de l'ancienne table groupe
 * @author Fabien
 */
public class Groupe {
    public static void requetes(Connection con) throws SQLException{
        
        //Renommage de la table en groupement_local, puis renommage des attributs existants
        
        String query = "ALTER TABLE groupe RENAME TO groupement_local;";
        Statement stmt =con.createStatement();
        stmt.executeUpdate(query);
        
        query = "ALTER TABLE groupement_local RENAME COLUMN groupe_id TO groupement_local_id;";
        stmt.executeUpdate(query);
        
        query = "ALTER TABLE groupement_local RENAME COLUMN groupe_nom TO groupement_local_nom;";
        stmt.executeUpdate(query);
        
        //Ajout des attributs de la table groupement_local qui n'existaient pas dans la table groupe
        
        query = "ALTER TABLE groupement_local ADD(type_groupement_local_intitule varchar(255) NOT NULL"
                + " FOREIGN KEY (`type_groupement_local_intitule`) "
                + "REFERENCES `type_groupement_local` (`type_groupement_local_intitule`));";
        stmt.executeUpdate(query);
        
        query= "ALTER TABLE groupement_local ADD(groupement_local_pere_id int(11)"
                + " FOREIGN KEY (`groupement_local_pere_id`) REFERENCES `groupement_local` (`groupement_local_id`));";
        stmt.executeUpdate(query);
        
        //On met à jour le type de groupement_local (tous des groupes pour l'instant)
        
        query= "UPDATE groupement_local SET type_groupement_local_intitule='groupe';";
        stmt.executeUpdate(query);
        
        //on renomme les clés étrangères référençant la table groupement_local
        
        query= "ALTER TABLE date_charniere RENAME COLUMN date_charniere_groupe_groupe_id TO"
                + " date_charniere_groupement_local_id;";
        stmt.executeUpdate(query);
        
        query= "ALTER TABLE membre_is_expert_on_groupe RENAME COLUMN "
                + "groupe_groupe_id TO groupement_local_id;";
        stmt.executeUpdate(query);
        
        query= "ALTER TABLE membre_is_expert_on_groupe RENAME TO membre_is_expert_on_groupement_local;";
        stmt.executeUpdate(query);
        
        query= "ALTER TABLE stade_sexe_hierarchie_dans_groupe RENAME COLUMN "
                + "groupe_groupe_id TO groupement_groupement_id;";
        stmt.executeUpdate(query);
        
        stmt.close();
    }
    
}
