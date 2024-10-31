package fr.insa.bourges.controleur;

import fr.insa.bourges.modele.Cheval;
import fr.insa.bourges.modele.FacadeGestionChevaux;
import fr.insa.bourges.modele.exceptions.ConflitNomChevalException;
import fr.insa.bourges.modele.exceptions.NomManquantException;
import fr.insa.bourges.modele.exceptions.PoidsIncoherentException;
import fr.insa.bourges.vues.GestionnaireVue;

import java.util.*;

public class Controleur implements LanceurOrdre{


    Map<TypeOrdre, Collection<EcouteurOrdre>> ecouteursOrdres;
    FacadeGestionChevaux facadeGestionChevaux;

    public Controleur(GestionnaireVue gestionnaireVue, FacadeGestionChevaux facadeGestionChevaux) {
        this.facadeGestionChevaux = facadeGestionChevaux;
        ecouteursOrdres = new HashMap<>();
        Arrays.stream(TypeOrdre.values()).forEach(
                t -> {
                    ecouteursOrdres.put(t, new ArrayList<>());
                }
        );
        gestionnaireVue.setControleur(this);
        gestionnaireVue.setAbonnement(this);


    }

    @Override
    public void abonnement(EcouteurOrdre ecouteurOrdre, TypeOrdre... types) {

        for(TypeOrdre t : types) {
            ecouteursOrdres.get(t).add(ecouteurOrdre);
        }


    }

    @Override
    public void fireOrdre(TypeOrdre e) {
        ecouteursOrdres.get(e).forEach(e1 -> {e1.traiter(e);});
    }

    public void run() {

        this.fireOrdre(TypeOrdre.DATA_LOAD);
        this.fireOrdre(TypeOrdre.SHOW_ACCUEIL);
    }

    public void gotoCreation() {
        this.fireOrdre(TypeOrdre.SHOW_CREATION);
    }

    public void gotoChevaux() {
        this.fireOrdre(TypeOrdre.SHOW_LISTE_CHEVAUX);
    }

    public void gotomenu() {
        this.fireOrdre(TypeOrdre.SHOW_ACCUEIL);
    }

    public void creerCheval(String nom, int poids) {

        try {
            facadeGestionChevaux.creationCheval(nom,poids);
            this.fireOrdre(TypeOrdre.DATA_CHEVAL_CREE);
            this.fireOrdre(TypeOrdre.SHOW_LISTE_CHEVAUX);
        } catch (NomManquantException e) {
            this.fireOrdre(TypeOrdre.ERREUR_NOM_MANQUANT);
        } catch (PoidsIncoherentException e) {
            this.fireOrdre(TypeOrdre.ERREUR_POIDS_INCOHERENT);
        } catch (ConflitNomChevalException e) {
            this.fireOrdre(TypeOrdre.ERREUR_CONFLIT_NOM_CHEVAL);
        }


    }

    public List<Cheval> getChevaux() {
        return facadeGestionChevaux.getChevaux();
    }
}
