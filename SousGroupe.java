
import java.util.HashMap;
import java.sql.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Classe créant les requêtes nécessaires à transformer les données de l'ancienne table sous_groupe
 * @author Fabien
 */
public class SousGroupe {
    public static HashMap<Integer, Integer> hmsousgroupe;
    
    public static void requetes(Connection con) throws SQLException{
        
        /*Les deux premières requêtes ne servent qu'à récupérer les valeurs d'auto-incrément des tables
        groupement_local et sous_groupe. Ces valeurs sont stockées dans aigroupe et aisgroupe
        */
        
        
        String query = "SHOW TABLE STATUS WHERE name=groupement_local;";
        Statement stmt =con.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        rs.next();
        int aigroupe = rs.getInt("Auto_increment");
        
        query = "SHOW TABLE STATUS WHERE name=sous_groupe;";
        rs = stmt.executeQuery(query);
        rs.next();
        int aisgroupe = rs.getInt("Auto_increment");
        
        //Ici on va prendre les éléments de la table sous-groupe un par un.
        
        for (int i=1; i<aisgroupe; i++){
            query = "SELECT * FROM sous_groupe WHERE sous_groupe_id="+i+";";
            rs = stmt.executeQuery(query);
            //Si l'élément existe
            if(rs.next()){
                //On stocke son nom et l'id du groupe auquel il est associé
                String nom= rs.getString("sous_groupe_nom");
                int idg = rs.getInt("sous_groupe_groupe_groupe_id");
                //On stocke dans la HashMap son ancien id et l'id qu'il va avoir dans la table groupement_local
                hmsousgroupe.put(i, aigroupe);
                //On incrémente aigroupe car il y aura un tuple de plus
                aigroupe++;
                //On insère l'élément dans la table groupement_local
                query = "INSERT INTO groupement_local VALUES ('"+nom+"', 'sous-groupe', "+idg+");";
                stmt.executeUpdate(query);
            }
        }
        stmt.close();
    }
}
