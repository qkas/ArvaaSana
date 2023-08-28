import java.util.Scanner;
import java.util.Random;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.charset.Charset;

/**
 * Sananarvaus peli
 * 
 * Jokaisella pelikierroksella ohjelma valitsee satunnaisesti yhden 4-kirjaimisen sanan, jota käyttäjän tulee arvata.
 * Käyttäjä syöttää minkä tahansa 4-kirjaimisen sanan, jonka oikeista/vääristä kirjaimista ohjelma antaa palautetta. 
 * Käyttäjä arvailee sanaa ja ohjelma antaa vihjeitä, kunnes käyttäjä arvaa sanan oikein. Kun käyttäjä arvaa sanan oikein,
 * ohjelma kertoo kierroksen arvausyritysten määrän sekä tämänhetkisen ennätyksen. Käyttäjä saa pelata niin monta peliä,
 * kun hän haluu. Jokaisen kierroksen jälkeen ennätys päivitetään. 
 * 
 * Kierroksen voi myös luovuttaa arvaamalla sanan "kerro", jolloin ohjelma kertoo oikean sanan ja kierros päättyy
 * ilman arvaus- ja ennätysmerkintöjä. 
 */
public class harkka {
    public static void main(String[] args) {
        ohjeet();

        String jatketaanko;
        int ennatys = 0;
        do {
            // Ohjelma valitsee uuden sanan.
            String oikeaSana = arvottuSana();

            int arvauksienMaara = 0;
            String arvaus = "";
            do {
                // Käyttäjältä kysytään arvausta.
                if (arvauksienMaara == 0) {
                    System.out.print("Syötä arvauksesi: ");
                } else {
                    System.out.print("Syötä uusi arvauksesi: ");
                }
                Scanner sc = new Scanner(System.in);
                arvaus = sc.nextLine();
                while (arvaus.length() != 4 && !arvaus.equals("kerro")) {
                    System.out.print("Syötäthän 4-kirjaimisen sanan: ");
                    arvaus = sc.nextLine();
                }
                arvauksienMaara++;
                
                // Arvausta tarkistetaan
                if (arvaus.toLowerCase().equals(oikeaSana.toLowerCase())) {
                    System.out.println("Oikein! Sana oli " + "\u001B[32m" + oikeaSana + "\u001B[0m.");
                    System.out.println("\nOnneksi olkoon, sait sanan oikein " + arvauksienMaara + " yrityksellä");
                    if (ennatys == 0 || ennatys > arvauksienMaara) {
                        ennatys = arvauksienMaara;
                        System.out.println("Uusi ennätys: " + ennatys);
                    } else {
                        System.out.println("Tämänhetkinen ennätys: " + ennatys);
                    }
                } else if (arvaus.equals("kerro")) {
                    System.out.println("Sana oli " + "\u001B[32m" + oikeaSana + "\u001B[0m."); 
                } else {
                    System.out.print("Sana ei ollut oikea. ");
                    annaVihje(oikeaSana, arvaus);
                }
            } while (!arvaus.equals(oikeaSana) && !arvaus.equals("kerro"));

            System.out.print("\nHaluatko jatkaa? (K/E): ");
            Scanner jatk = new Scanner(System.in, Charset.forName("ISO-8859-1"));
            jatketaanko = jatk.nextLine();
        } while (!jatketaanko.equals("E") && !jatketaanko.equals("e"));
        System.out.println("\nKiitos kun pelasit!");
    }

    /**
     * Tulostaa pelin ohjeet.
     */
    static void ohjeet() {
        System.out.println("\nTervetuloa pelaamaan sanapeliä.\n");
        System.out.println("Ohjelma valitsee satunnaisesti suomalaisen 4-kirjaimisen sanan, joka sinun pitää arvata.\n");
        System.out.println("Aloita arvaamalla mikä tahansa suomalaisen 4-kirjaiminen sana, niin saat tietää arvotun sanan ja arvatun sanan yhteiset kirjaimet. ");
        System.out.println("\u001B[32m" + "Vihreä kirjain" + "\u001B[0m" + " tarkoittaa, että kirjain on sanassa sekä se on oikeassa kohdassa. ");
        System.out.println("\u001B[33m" + "Keltainen []" + "\u001B[0m" + " tarkoittaa, että syöttämäsi merkki on sanassa, mutta väärässä kohdassa. ");
        System.out.println("\u001B[31m" + "Punainen []" + "\u001B[0m" + " tarkoittaa, että syöttämääsi merkkiä ei ole sanassa. ");
        System.out.println("Arvailu jatkuu, kunnes arvaat sanan oikein. Jokaisen arvauksen jälkeen saat uuden vihjeen oikeista kirjaimista. ");
        System.out.println("Jos haluat luovuttaa, syötä sanaksi 'kerro', jolloin kierros päättyy ilman arvaus- ja ennätysmerkintöjä.\n");
    }
    
    /**
     * Metodi arpoo sanalistasta yhden sanan ja palauttaa sen.
     * 
     * Käy läpi tiedoston sanaLista.txt ja lisää sen sanat sanat-taulukkoon.
     * Sanoista valitaan satunnaisesti yksi ja metodi palauttaa sanan funktioon.
     * Jos tiedoston lukemisessa ilmenee virhe, metodi palauttaa sanan "vika".
     * 
     * @return Ohjelman arpoma sana
     */
    static String arvottuSana() {
        try {
            File tiedosto = new File("sanaLista.txt");
            Scanner lukija = new Scanner(tiedosto, StandardCharsets.UTF_8);
            String data = "";
            while (lukija.hasNextLine()) {
                data += lukija.nextLine() + " ";
            }
            lukija.close();
            String[] sanat = data.split(" ");
            
            int i = 0;
            while (sanat[i].contains("ä") || sanat[i].contains("ö")) {
                Random rnd = new Random();
                i = rnd.nextInt(sanat.length);
            }

            System.out.println("Uusi sana arvottu.");
            return sanat[i];
        } catch (Exception e) {
            System.out.println("Sanalistaa ei voitu lukea. ");
            return "vika";
        }
    }

    /**
     * Ohjelma vertaa käyttäjän syöttämää saanaa oikeaan sanaan ja tulostaa palautetta sen kirjaimista.
     * 
     * Ohjelma hakee parametreina oikean sanan ja käyttäjän syöttämän sanan ja tekee näistä char-taulukot.
     * Sitten se vertaa näiden paikkoja ja tulostaa kirjaimet punaisena (jos kirjainta ei ole sanassa),
     * keltaisena (jos kirjain ei ole oikeassa kohdassa), vihreänä (jos kirjain on oikeassa kohdassa).
     * 
     * @param oikeaSana
     * @param arvaus
     */
    static void annaVihje(String oikeaSana, String arvaus) {
        char[] oikeaSanaMerkkeina = oikeaSana.toCharArray();
        char[] arvausMerkkeina = arvaus.toCharArray();

        System.out.print("Vihje: ");
        for (int i = 0; i < 4; i++) {
            if (arvausMerkkeina[i] == oikeaSanaMerkkeina[i]) {
                System.out.print("\u001B[32m" + oikeaSanaMerkkeina[i] + "\u001B[0m"); // Vihreä kirjain
            } else if (oikeaSana.contains(String.valueOf(arvausMerkkeina[i]))) {
                System.out.print("\u001B[33m" + "[]" + "\u001B[0m"); // Keltainen '_'-merkki
            } else {
                System.out.print("\u001B[31m" + "[]" + "\u001B[0m"); // Punainen '_'-merkki
            }
        }
        System.out.println();
    }
}
