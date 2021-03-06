package sanasampo;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import sanasampo.data.Hakemisto;
import sanasampo.data.Ruudukko;
import sanasampo.data.Sanakirja;
import sanasampo.logic.Haku;
import sanasampo.ui.Kayttoliittyma;

/** Vastaanottaa käyttäjän syötteen, aloittaa haun, luo ja näyttää näkymän (GUI) */

public class Sampo {

    /** Ohjelman käyttöliittymä */
    private Kayttoliittyma ui;
    
    /** Sanakirjat sisältävä hakemisto */
    private Hakemisto hakemisto;
    
    /** Käyttäjän määrittelemät kirjaimet sisältävä ruudukko */
    private Ruudukko ruudukko;
    
    /** Käyttäjän syöte, jolla ruudukko alustetaan */
    private String syote;
    
    /** Hakualgoritmin toteuttava luokka */
    private Haku haku;
    
    /** Kysyy käyttäjältä kirjaimet, pyrkii alustamaan niillä ruudukon ja näyttää
     * virhedialogin validoinnin epäonnistuessa. Aloittaa haun ja kutsuu esiin 
     * käyttöliittymän. Jos parametri löytyy koitetaan alustaa sillä. 
     * @see sanasampo.data.Ruudukko#validoi(String)
     */
     public void kaynnista(String k) {
        if(k.isEmpty()) kysyKirjaimet();
        else syote = k;
        while (!ruudukko.alusta(syote)) {
            JOptionPane.showMessageDialog(null, 
                    "Grid size should be equilateral (3x3, 4x4..) and contain no special characters!\n"
                    + "At the moment only dimensions up to 9x9 are accepted. ", 
                    "Grid Initialization Error!", JOptionPane.INFORMATION_MESSAGE);
            kysyKirjaimet();
        }
       hae();
       nayta();
    }

    /**Parametriton käynnistys välittää kuormitetulle konstruktorille
     tyhjän syötteen*/
    public void kaynnista()  {
       kaynnista("");
    }
    
    //** Parametrinen asennus asentaa Sammon oletussanakirjalla */
    public void asenna() {
        asenna(new Sanakirja());
    }
   
    /** Alustaa hakemiston ja ruudukon */
    public void asenna(Sanakirja s) {
        hakemisto = new Hakemisto(s);
        ruudukko = new Ruudukko();
    }

    /** Näyttää käyttäjälle syötekentän ja kysyy syötettä */
    private void kysyKirjaimet() {
        syote = JOptionPane.showInputDialog(null, "Grid contents in one line: ", 
                "Grid selection", 1);
    }

    /** Välittää hakualgoritmin toteuttavalle luokalle ruudukon ja käytetyn hakemiston
     * ja käynnistää haun */
    private void hae() {
        haku = new Haku(hakemisto, ruudukko);
        haku.kaynnista();
    }

    /** Kutsuu esiin käyttöliittymän (GUI), joka näyttää päättyneen haun tulokset.*/
    private void nayta() {
        ui = new Kayttoliittyma(ruudukko, haku.getTulos(), haku.getHitlist(), this);
        SwingUtilities.invokeLater(ui);
    }
    
    public ArrayList<String> getTulos(){
        return haku.getTulos();
    }
    
    public Kayttoliittyma getUi(){
        return ui;
    }
    
    public void exit(){
        System.exit(0);
    }
}
