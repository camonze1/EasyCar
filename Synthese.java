import java.sql.*;
import java.io.*;
import java.util.*;

public class Synthese {

    public static void main(String[] args) throws Exception {
        try
        {
            Scanner sc = new Scanner(System.in);
            String url = "jdbc:oracle:thin:@charlemagne.iutnc.univ-lorraine.fr:1521:infodb";
            Connection cnt = DriverManager.getConnection(url,"launois15u", "Basile2499");

            System.out.println("\n" + "            Agence EasyCar          " + "\n");
            System.out.println("        1 - Etablir un devis        "+ "\n");
            System.out.println("        2 - Verifier la disponibilite d'un vehicule"+ "\n");
            System.out.println("        3 - Ajouter une reservation "+ "\n");
            System.out.println("        4 - Supprimer une reservation" + "\n");
            System.out.println("        5 - Afficher une reservation" + "\n");
            System.out.println("        6 - Afficher le catalogue   " + "\n");
            System.out.println("        7 - Quitter                 " + "\n");
            System.out.println("                Choix :             " + "\n");

            int choix = 0;

            while(choix!=7){
                
                System.out.println("Que voulez vous choisir a present : " + "\n");
                choix = sc.nextInt();
                String blanc = sc.nextLine();

                switch(choix){

                    case 1 : 

                        CallableStatement stmt = cnt.prepareCall("{ ? = call devis(?,?,?)}");
                        stmt.registerOutParameter(1, Types.DECIMAL, 2);
                        
                        System.out.println("Veuillez renseigner le type de categorie :");
                        String categ = sc.nextLine();

                        System.out.println("Veuillez renseigner la date de debut :");
                        String datedebut = sc.nextLine();
                        
                        System.out.println("Veuillez renseigner la date de fin :");
                        String datefin = sc.nextLine();

                        stmt.setString(2,categ);
                        stmt.setString(3,datedebut);
                        stmt.setString(4,datefin);

                        stmt.execute();
                        double devis = stmt.getDouble(1);

                        System.out.println("Votre devis est de : " + devis + " euros" + "\n");
                        
                    break;
                    
                    case 2 : 

                        CallableStatement stmt2 = cnt.prepareCall("{ ? = call vehiculedispo(?,?,?)}");
                        stmt2.registerOutParameter(1, Types.CHAR);

                        System.out.println("Veuillez renseigner le type de categorie :");
                        String categ2 = sc.nextLine();
                        stmt2.setString(2,categ2);

                        System.out.println("Veuillez renseigner la date de debut :");
                        String datedebut2 = sc.nextLine();
                        stmt2.setString(3,datedebut2);

                        System.out.println("Veuillez renseigner la date de fin :");
                        String datefin2 = sc.nextLine();
                        stmt2.setString(4,datefin2);

                        stmt2.execute();
                        String dispo2 = stmt2.getString(1);

                        if (dispo2==null){
                        System.out.println("Aucun vehicule disponible" + "\n");
                        } else {
                        System.out.println("L'immatriculation du premier vehicule est : " + dispo2 + "\n");
                        }
                        
                    break;

                    case 3 : 

                        CallableStatement stmt3 = cnt.prepareCall("{ call AddReserv(?,?,?,?,?,?,?,?)}");
            
                        System.out.println("Veuillez renseigner la date de reservation :");
                        String datereserv3 = sc.nextLine();
                        stmt3.setString(1,datereserv3);

                        System.out.println("Veuillez renseigner la date de depart :");
                        String datedep3 = sc.nextLine();
                        stmt3.setString(2,datedep3);

                        System.out.println("Veuillez renseigner la date de retour :");
                        String dateretour3 = sc.nextLine();
                        stmt3.setString(3,dateretour3);

                        System.out.println("Veuillez renseigner le numero du client :");
                        String numcl3 = sc.nextLine();
                        stmt3.setString(4,numcl3);

                        System.out.println("Veuillez renseigner le numero de l'agence :");
                        String numag3 = sc.nextLine();
                        stmt3.setString(5,numag3);

                        System.out.println("Veuillez renseigner le numero du vehicule :");
                        String numvehic3 = sc.nextLine();
                        stmt3.setString(6,numvehic3);

                        System.out.println("Veuillez renseigner la remise de vehicule :");
                        int remvehic3 = sc.nextInt();
                        stmt3.setInt(7,remvehic3);     
                        
                        stmt3.registerOutParameter(8, Types.NUMERIC);
                        stmt3.execute();
                        int sortie3 = stmt3.getInt(8);
                        
                        /**
                        Recherche du numero de reservation crée
                        */

                        PreparedStatement p_trouve3 = cnt.prepareStatement("select numreserv from reservation where datedep = ? and dateretour = ? and numclient = ? and numagence = ? and numvehic = ?");
                        p_trouve3.setString(1, datedep3);
                        p_trouve3.setString(2, dateretour3);
                        p_trouve3.setString(3, numcl3);
                        p_trouve3.setString(4, numag3);
                        p_trouve3.setString(5, numvehic3);

                        ResultSet res_trouve3 = p_trouve3.executeQuery();

                        while(res_trouve3.next()){

                            String numres3 = res_trouve3.getString("numreserv");

                            if (sortie3==0){
                                System.out.println("La reservation a ete enregistree sous le numero de reservation : " + numres3 + "\n");
                            } else {
                                System.out.println("La reservation n'a pas pu avoir lieu" + "\n");
                            }   
                        }

                        res_trouve3.close();
                        p_trouve3.close();


                    break;

                    case 4 :

                        System.out.println("Veuillez entrez le numero de reservation : ");
                        String numreserv4 = sc.nextLine();
                        
                        CallableStatement req4 = cnt.prepareCall("{call suppreserv(?,?)}");
                        req4.setString(1, numreserv4);
                    
                        req4.registerOutParameter(2, Types.INTEGER);
                        req4.execute(); 
                        
                        int sortie4 = req4.getInt(2);

                        if (sortie4==0){
                            System.out.println("La reservation " + numreserv4 + " a bien ete supprime." + "\n");
                        } else {
                            System.out.println("La suppression n'a pas pu avoir lieu car le numero de reservation n'existe pas" + "\n");
                        }   
                

                    break;

                    case 5 :

                        System.out.println("Veuillez entrer le numero de votre reservation s'il vous plait : " + "\n");

                        String numres5 = sc.nextLine();

                        CallableStatement reserv5 = cnt.prepareCall("{ ? = call reservfact(?)}");
                        reserv5.registerOutParameter(1, Types.DECIMAL);
                        reserv5.setString(2, numres5);
                        reserv5.execute();
                        int existe5 = reserv5.getInt(1);

                        if (existe5==0){

                            CallableStatement reserv51 = cnt.prepareCall("{ ? = call reserv(?)}");
                            reserv51.registerOutParameter(1, Types.DECIMAL);
                            reserv51.setString(2, numres5);
                            reserv51.execute();
                            int existe51 = reserv51.getInt(1);

                            if (existe51==0){
                                System.out.println("La reservation n'existe pas" + "\n");

                            } else {

                                CallableStatement afficher51 = cnt.prepareCall("{ call affichres(?,?,?,?,?,?,?,?,?)}");
                                afficher51.setString(1, numres5);
                                afficher51.registerOutParameter(2, Types.VARCHAR);
                                afficher51.registerOutParameter(3, Types.VARCHAR);
                                afficher51.registerOutParameter(4, Types.VARCHAR);
                                afficher51.registerOutParameter(5, Types.VARCHAR);
                                afficher51.registerOutParameter(6, Types.VARCHAR);
                                afficher51.registerOutParameter(7, Types.VARCHAR);
                                afficher51.registerOutParameter(8, Types.INTEGER);
                                afficher51.registerOutParameter(9, Types.VARCHAR);

                                afficher51.execute();

                                String numclient51 = afficher51.getString(2);
                                String nom51 = afficher51.getString(3);
                                String prenom51 = afficher51.getString(4);
                                String datedep51 = afficher51.getString(5);
                                String datefin51 = afficher51.getString(6);
                                String codecateg51 = afficher51.getString(7);
                                int montant51 = afficher51.getInt(8);
                                String regle51 = afficher51.getString(9);

                                System.out.println(" Le numero du client est : " + numclient51 + "\n");
                                System.out.println(" Le nom et le prenom du client sont : " + nom51 + " " + prenom51 + "\n");
                                System.out.println(" La date de depart est : " + datedep51 + "\n");
                                System.out.println(" La date de retour est : " + datefin51 + "\n");
                                System.out.println(" Le code de categorie de la voiture est : " + codecateg51 + "\n");
                                System.out.println(" Le montant de la location est : " + montant51 + "\n");
                                System.out.println(" La somme est regle (o) sinon (n) : " + regle51 + "\n");
                            }

                        } else {
                            CallableStatement afficher5 = cnt.prepareCall("{ call affichfact(?,?,?,?,?,?,?,?,?)}");
                            afficher5.setString(1, numres5);
                            afficher5.registerOutParameter(2, Types.VARCHAR);
                            afficher5.registerOutParameter(3, Types.VARCHAR);
                            afficher5.registerOutParameter(4, Types.VARCHAR);
                            afficher5.registerOutParameter(5, Types.VARCHAR);
                            afficher5.registerOutParameter(6, Types.VARCHAR);
                            afficher5.registerOutParameter(7, Types.VARCHAR);
                            afficher5.registerOutParameter(8, Types.INTEGER);
                            afficher5.registerOutParameter(9, Types.VARCHAR);

                            afficher5.execute();

                            String numclient5 = afficher5.getString(2);
                            String nom5 = afficher5.getString(3);
                            String prenom5 = afficher5.getString(4);
                            String datedep5 = afficher5.getString(5);
                            String datefin5 = afficher5.getString(6);
                            String codecateg5 = afficher5.getString(7);
                            int montant5 = afficher5.getInt(8);
                            String regle5 = afficher5.getString(9);
                        
                            System.out.println(" Le numero du client est : " + numclient5 + "\n");
                            System.out.println(" Le nom et le prenom du client sont : " + nom5 + " " + prenom5 + "\n");
                            System.out.println(" La date de depart est : " + datedep5 + "\n");
                            System.out.println(" La date de retour est : " + datefin5 + "\n");
                            System.out.println(" Le code de categorie de la voiture est : " + codecateg5 + "\n");
                            System.out.println(" Le montant de la location est : " + montant5 + "\n");
                            System.out.println(" La somme est regle (o) sinon (n) : " + regle5 + "\n");}

                    break;

                    case 6 :

                        String reqVille6 = "select distinct villeAgence from agence order by villeAgence ASC";
                        String reqAgence6 = "select nomAgence, telAgence from agence where villeAgence = ?";
                        String reqCode6 = "select distinct codeCateg from categorie";
                        String reqCateg6 = "select distinct libelleCateg, PrixJour from categorie inner join vehicule on categorie.codecateg = vehicule.codecateg inner join agence on agence.numagence = vehicule.numagence where vehicule.codeCateg = ?" ;
                        String reqVehic6 = "select numVehic from categorie inner join vehicule on categorie.codecateg = vehicule.codecateg where vehicule.codecateg = ?";            

                        PreparedStatement p_ville6 = cnt.prepareStatement(reqVille6); 
                        PreparedStatement p_agence6 = cnt.prepareStatement(reqAgence6);
                        PreparedStatement p_code6 = cnt.prepareStatement(reqCode6);
                        PreparedStatement p_categ6 = cnt.prepareStatement(reqCateg6);
                        PreparedStatement p_vehic6 = cnt.prepareStatement(reqVehic6);

                        ResultSet resp_ville6 = p_ville6.executeQuery(); 

                        while(resp_ville6.next())
                        {
                            String ville6 = resp_ville6.getString("villeAgence");
                            System.out.println(ville6);

                            p_agence6.setString(1, ville6);
                            ResultSet resp_agence6 = p_agence6.executeQuery();
                            
                            while (resp_agence6.next())
                            {
                                String nom6 = resp_agence6.getString("nomAgence");
                                System.out.print("  " + nom6 + " ");

                                String tel6 = resp_agence6.getString("telAgence");
                                System.out.println(tel6);

                                ResultSet resp_code6 = p_code6.executeQuery();  
                                
                                while(resp_code6.next())
                                {                                       
                                    String code6 = resp_code6.getString("codeCateg");
                                    
                                    p_categ6.setString(1, code6);
                                    ResultSet resp_categ6 = p_categ6.executeQuery();

                                    while (resp_categ6.next()) 
                                    {
                                        String libelle6 = resp_categ6.getString("libelleCateg");
                                        double prix6 = resp_categ6.getDouble("PrixJour");
                                        
                                        System.out.println("\t" + code6 + " " + libelle6 + " " + prix6 + " ");

                                        p_vehic6.setString(1, code6);
                                        ResultSet resp_vehic6 = p_vehic6.executeQuery();

                                        while(resp_vehic6.next())
                                        {                           
                                            String numvehic6 = resp_vehic6.getString("numVehic");

                                            System.out.println("\t\t" + " - " + numvehic6 + " " + "\n");
                                        }

                                        System.out.println("");
                                        resp_vehic6.close();
                                    }
                                    resp_categ6.close();
                                }
                                resp_code6.close();
                            }            
                            resp_agence6.close();
                        }
                        resp_ville6.close();
                        p_categ6.close();
                        p_vehic6.close();
                        p_ville6.close();

                    break;

                    case 7 :

                        System.out.println("\n" + "Au revoir et a tres bientot ;) \n");
                        System.out.println("MULLER Oceane, LAUNOIS Camille \n");
                        System.exit(0);

                    break;
                }
            }
        }
        
        catch(Exception e)
        {
            System.out.println("Une erreur s'est produite \n");
            e.printStackTrace();
        }
    }
}