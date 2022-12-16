package caw.atm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;



public class BankomatTest {

    IBank bankMock;
    Bankomat bankomat;
    List<User> users;
    List<Account> user1Account;
    User user1;
    List<Account> user2Account;
    User user2;
    Account account;



    @BeforeEach
    public void setUp() {
        bankMock = mock(IBank.class);
        bankomat = new Bankomat(bankMock);
        users = new LinkedList<>();
        user1Account = new ArrayList<>();
        user1 = new User("SunesKorvar", "KörvEGött", user1Account);
        user1Account.add(new Account("1", 0));
        user2Account = new ArrayList<>();
        user2 = new User("BörjesBurgare", "BurgareEBäst", user2Account);
        account = new Account("1", 0);
        when(bankMock.getUsers()).thenReturn(users);
    }

    @Test
    void logInTest_Success() {

        users.add(user1);

        when(bankMock.getUsers()).thenReturn(users);
        User tryLogIn = bankomat.logIn("SunesKorvar", "KörvEGött");

        assertAll(() -> assertEquals(tryLogIn.getName(), user1.getName()),
                () -> assertEquals(tryLogIn.getPassword(), user1.getPassword()),
                () -> assertEquals(tryLogIn.getAccounts().get(0).getAccountNumber(), "1"),
                () -> assertEquals(tryLogIn.getAccounts().get(0).getAccountBalance(), 0));
    }

    @Test
    void logIn_WithUsernameAndPassword_FailureDueToWrongPassword() {
        user1Account.add(new Account("1", 0));
        users.add(user1);

        when(bankMock.getUsers()).thenReturn(users);
        User tryLogIn = bankomat.logIn("SunesKorvar", "KörvEGött");

        assertAll(() -> assertEquals(tryLogIn.getName(), user1.getName()),
                () -> assertNotEquals(tryLogIn.getPassword(), user2.getPassword()), // Gets user2's password from above
                () -> assertEquals(tryLogIn.getAccounts().get(0).getAccountNumber(), "1"),
                () -> assertEquals(tryLogIn.getAccounts().get(0).getAccountBalance(), 0));
    }

    @Test
    void logIn_WithUsernameAndPassword_FailureDueToWrongUsername() {
        user1Account.add(new Account("1", 0));
        users.add(user1);

        when(bankMock.getUsers()).thenReturn(users);
        User tryLogIn = bankomat.logIn("SunesKorvar", "KörvEGött");

        assertAll(() -> assertNotEquals(tryLogIn.getName(), user2.getName()), // Gets user2's name from above
                () -> assertEquals(tryLogIn.getPassword(), user1.getPassword()),
                () -> assertEquals(tryLogIn.getAccounts().get(0).getAccountNumber(), "1"),
                () -> assertEquals(tryLogIn.getAccounts().get(0).getAccountBalance(), 0));
    }

    @Test
    void logIn_FailureDueToWrongUser() {
        user1Account.add(new Account("1", 0));
        users.add(user1);

        when(bankMock.getUsers()).thenReturn(users);
        User result = bankomat.logIn("SunesKorvar", "KörvEGött");

        assertAll(() -> assertEquals(result.getName(), user1.getName()),
                () -> assertEquals(result.getPassword(), user1.getPassword()),
                () -> assertEquals(result.getAccounts().get(0).getAccountNumber(), "1"),
                () -> assertEquals(result.getAccounts().get(0).getAccountBalance(), 0));
    }



    @Test
    void checkAccountBalance_ZeroMoney() {
       user1Account.add(new Account("1", 0));
       users.add(user1);

       assertEquals(0, bankomat.CheckAccountBalance(user1, "1"));
    }

    @Test
    void checkAccountBalance_SetAccountBalanceToPositiveNumber() {
        user1Account.add(new Account("1", 555));
        users.add(user1);

        assertEquals(555, bankomat.CheckAccountBalance(user1, "1"));
    }

    @Test
    void checkAccountBalance_SetAccountBalanceToNegativeNumber() {
        user1Account.add(new Account("1", -850));
        users.add(user1);

        assertEquals(-850, bankomat.CheckAccountBalance(user1, "1"));
    }

    @Test
    void checkAccountBalance_WrongUser_DefaultCash() {
        user1Account.add(new Account("1", 0));
        users.add(user1);

        assertEquals(-9999, bankomat.CheckAccountBalance(user2, "2"));
    }

    @Test
    void compareTwoDifferentAccountsBalances_ShowSame() {
        user1Account.add(new Account("1", 500));
        users.add(user1);
        user2Account.add(new Account("2", 500));
        users.add(user2);

        var testUser1 = bankomat.CheckAccountBalance(user1, "1");
        var testUser2 = bankomat.CheckAccountBalance(user2, "2");

        assertEquals(testUser1, testUser2);
    }



    @Test
    void depositMoney_Add55Then100() {
        bankomat.DepositMoney(account, 55);
        bankomat.DepositMoney(account, 100);
        assertEquals(155, account.accountBalance);
    }

    @Test
    void depositMoney_FromDebtOfMinus999To1() {
        account.setAccountBalance(-999);
        bankomat.DepositMoney(account, 1000);
        assertEquals(1, account.accountBalance);
    }

    @Test
    void depositMoney_MAX_VALUE() {
        bankomat.DepositMoney(account, Integer.MAX_VALUE);
        assertEquals(2147483647, account.accountBalance);
    }

    @Test
    void depositMoney_MAX_VALUE_Plus1EqualsOverflow() {
        bankomat.DepositMoney(account, Integer.MAX_VALUE);
        assertEquals(2147483647, account.accountBalance);
        bankomat.DepositMoney(account, 1);
        assertEquals(Integer.MIN_VALUE, account.accountBalance);
    }



    @Test
    void withdrawMoney_ShouldHave500AfterMultipleWithdrawals() {
        account.setAccountBalance(1000);
        bankomat.WithdrawMoney(account, 250);
        bankomat.WithdrawMoney(account, 150);
        bankomat.WithdrawMoney(account, 100);
        assertEquals(500, account.accountBalance);
    }

    @Test
    void withdrawMoney_WithdrawNegative() {
        bankomat.WithdrawMoney(account, -250);
        bankomat.WithdrawMoney(account, -150);
        bankomat.WithdrawMoney(account, -100);
        assertEquals(500,account.accountBalance);
    }

    @Test
    void withdrawMoney_ShouldNotBeAbleToWithdrawMoney() {
        account.setAccountBalance(500);
        bankomat.WithdrawMoney(account, 1000);
        assertEquals(500, account.accountBalance);

    }

    @Test
    void withdrawMoney_Integer_MIN_VALUE() {
        bankomat.WithdrawMoney(account, Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, account.accountBalance);
    }

    @Test
    void withdrawMoney_1MinusInteger_MIN_VALUE() {
        bankomat.DepositMoney(account, 1);
        bankomat.WithdrawMoney(account, Integer.MIN_VALUE);
        assertEquals(1 - Integer.MIN_VALUE, account.accountBalance);
    }
}
