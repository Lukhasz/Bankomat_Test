package caw.atm;

import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

public class Bankomat {

    private User loggedInUser;
    private IBank bank;

    public Bankomat() {
        bank = new Bank();
    }

    public Bankomat(IBank bank) {
        this.bank = bank;
    }

    public void Run() {

        String userName = scannerGetString("Enter Username: ");
        String password = scannerGetString("Password: ");

        loggedInUser = logIn(userName, password);
        if (loggedInUser == null) {
            System.exit(0);
        } else {
            System.out.println("Welcome " + loggedInUser.getName());
        }

        System.out.println("Accountnumber : Balans");
        for (Account currentAccount : loggedInUser.getAccounts()) {
            System.out.println(currentAccount.getAccountNumber() + " : "
                    + CheckAccountBalance(this.loggedInUser, "" + currentAccount.getAccountNumber()));

        }
        int menychoice;
        do {
            printMenu();
            menychoice = scannerGetInt("Meny Command: ");

            switch (menychoice) {
                case 1:
                    for (Account cUA : loggedInUser.getAccounts()) {
                        System.out.println(cUA.getAccountNumber() + " : "
                                + CheckAccountBalance(this.loggedInUser, "" + cUA.getAccountNumber()));

                    }
                    break;
                case 2:{
                    Account currentAccount=null;
                    do{
                        String accountNumber = scannerGetString("Write Accountnumber");
                        currentAccount=getCorrectAccount(accountNumber);
                    } while (currentAccount==null);
                    int amount = scannerGetInt("Write Amount to deposit");
                    Account newBalans = DepositMoney(currentAccount, amount);
                    setNewBalansAccountBackToUsersAccount(newBalans);
                    break;}
                case 3:{
                    Account currentAccount=null;
                    do{
                        String accountNumber = scannerGetString("Write Accountnumber");
                        currentAccount=getCorrectAccount(accountNumber);
                    } while (currentAccount==null);
                    int amount = scannerGetInt("Write Amount to withdraw");
                    Account newBalans = WithdrawMoney(currentAccount, amount);
                    setNewBalansAccountBackToUsersAccount(newBalans);
                    break;}
                case 4:
                    break;

            }

        } while (menychoice != 4);

    }
    
    private boolean setNewBalansAccountBackToUsersAccount(Account newBalans){
        ListIterator<Account> accountIterator = loggedInUser.getAccounts().listIterator();
        while(accountIterator.hasNext()){
            Account accountIteration = accountIterator.next();
            if (newBalans.getAccountNumber().equals(accountIteration.getAccountNumber())){
                accountIterator.set(newBalans);
                return true;
            }
        }
         return false;
    }
    
    private Account getCorrectAccount(String accountnumber){
        for (Account currentAccount : loggedInUser.getAccounts()) {
            if (accountnumber.equals(currentAccount.accountNumber)){
                return currentAccount;
            }
        }
        
        return null;
        
    }

    User logIn(String userName, String password) {
        List<User> users = bank.getUsers();
        for (User u : users) {
            if (u.getName().equals(userName) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }

    private void printMenu() {
        System.out.println("1. Check Account Status");
        System.out.println("2. Deposit Money");
        System.out.println("3. Withdraw Money");
        System.out.println("4. Log Out//End");
    }

    private String scannerGetString(String message) {
        String out = null;

        Scanner scanner = new Scanner(System.in);
        boolean entryCorrect = false;
        do {
            System.out.println(message);
            out = scanner.nextLine();
            if (out.isBlank() || out.isEmpty()) {
                System.out.println("Incorrect input. Try again.");
            } else {
                entryCorrect = true;
            }

        } while (!entryCorrect);
//        scanner.close();
        return out;
    }

    private int scannerGetInt(String message) {
        String out = null;

        Scanner scanner = new Scanner(System.in);
        boolean entryCorrect = false;
        do {
            System.out.println(message);
            out = scanner.nextLine();
            if (out.isBlank() || out.isEmpty()) {
                System.out.println("Incorrect input. Try again.");
            } else {
                entryCorrect = true;
            }

        } while (!entryCorrect);
        return Integer.valueOf(out);
    }

    public int CheckAccountBalance(User currentUser, String accountNumber) {
        for (Account cUA : currentUser.getAccounts()) {
            if (cUA.getAccountNumber().equals(accountNumber)) {
                return cUA.getAccountBalance();
            }
        }
        return -9999;
    }

    public Account DepositMoney(Account account, Integer money) {
        Integer currentAmount = account.accountBalance;
        currentAmount += money;
        account.setAccountBalance(currentAmount);
        System.out.println("New Balans on Accountnumber: " + account.getAccountNumber() + " is " + account.getAccountBalance());
        return account;
    }

    public Account WithdrawMoney(Account account, Integer money) {
        Integer currentAmount = account.accountBalance;
        if(currentAmount<money){
            System.out.println("Now enough money in account. Only " + currentAmount);
            return account;
        } 
        currentAmount -= money;
        account.setAccountBalance(currentAmount);
        System.out.println("New Balans on Accountnumber: " + account.getAccountNumber() + " is " + account.getAccountBalance());
        return account;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

}
