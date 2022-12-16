
package caw.atm;



public class Main {

    public static void main(String[] args) {
        Bankomat bankomat = new Bankomat(new Bank());
        bankomat.Run();
        
    }
}
