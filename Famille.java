/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.*;
import java.util.HashMap;


/**
 * Classe créant les requêtes nécessaires à transformer les données de l'ancienne table famille
 * @author Fabien
 */
public class Famille {
    public static HashMap<Integer, Integer> hmfamille;
    
    /*
    La démarche est la même que précédemment encore une fois. La seule chose à laquelle il faut faire attention
    est le groupement_scientifique père : est-ce une super-famille ou un ordre.
    */
    
    public static void requetes(Connection con) throws SQLException{
        String query = "SHOW TABLE STATUS WHERE name=groupement_scientifique;";
        Statement stmt =con.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        rs.next();
        int aigs = rs.getInt("Auto_increment");
        
        query = "SHOW TABLE STATUS WHERE name=famille;";
        rs = stmt.executeQuery(query);
        rs.next();
        int aifamille = rs.getInt("Auto_increment");
        
        for (int i=1; i<aifamille; i++){
            query = "SELECT * FROM famille WHERE famille_id="+i+";";
            rs = stmt.executeQuery(query);
            if(rs.next()){
                String nom= rs.getString("famille_nom");
                int idpere = rs.getInt("famille_super_famille_super_famille_id");
                query = "SELECT * FROM super_famille WHERE famille_id="+idpere+";";
                rs = stmt.executeQuery(query);
                rs.next();
                //Ici pour récupérer l'id du père on fait :
                if(rs.getInt("super_famille_existe")!=0){
                    //Si la super_famille existe, on passe par la HashMap
                    idpere = SuperFamille.hmsuperfamille.get(idpere);
                }else{
                    //Sinon on récupère l'attribut donnant le père de la super-famille
                    idpere = rs.getInt("super_famille_ordre_ordre_id");
                }
                hmfamille.put(i, aigs);
                aigs++;
                query = "INSERT INTO groupement_scientifique (groupement_scientifique_nom, "
                            + "type_groupement_scientifique_intitule, groupement_scientifique_pere_id)"
                            + " VALUES ('"+nom+"', 'famille', "+idpere+");";
                stmt.executeUpdate(query);
            }
        }
        
        query = "SHOW TABLE STATUS WHERE name=famille_has_sous_groupe;";
        rs = stmt.executeQuery(query);
        rs.next();
        int aifhsg = rs.getInt("Auto_increment");
        
        for(int i=1; i<aifhsg; i++){
            query = "SELECT * FROM famille_has_sous_groupe WHERE famille_has_sous_groupe_id ="+i+";";
            rs = stmt.executeQuery(query);
            if (rs.next()){
                int fid = rs.getInt("famille_famille_id");
                int sgroupeid = SousGroupe.hmsousgroupe.get(rs.getInt("sous_groupe_sous_groupe_id"));
                query = "INSERT INTO groupement_scientifique_is_in_groupement_local "
                        + "(groupement_scientifique_id, groupement_local_id) VALUES ("
                        + hmfamille.get(fid) + ", "+ sgroupeid + ");";
                stmt.executeUpdate(query);
            }
        }
        stmt.close();
    }
}
