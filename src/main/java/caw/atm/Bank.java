package caw.atm;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Bank implements IBank {

    @Override
    public List<User> getUsers() {
        List<User> bankUsers = new LinkedList<>();
        List<Account> user1Accounts = new ArrayList<>();
        user1Accounts.add(new Account("123456", 11));
        user1Accounts.add(new Account("987654", 12));
        User user1 = new User("Sune", "password", user1Accounts);
        bankUsers.add(user1);
        List<Account> user2Accounts = new ArrayList<>();
        user2Accounts.add(new Account("45753", 101));
        user2Accounts.add(new Account("455543", 102));
        User user2 = new User("KalleSvensson", "password", user2Accounts);
        bankUsers.add(user2);
        return bankUsers;
    }

    
}
