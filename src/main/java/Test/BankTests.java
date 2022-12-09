package Test;
import Main.Account;
import Main.AccountStatus;
import Main.Bank;
import com.sun.jdi.connect.spi.ClosedConnectionException;
import junit.framework.TestCase;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertThrows;

public class BankTests extends TestCase {
    Bank bank = new Bank();
    Map<String, Account> accountsTest;

    @Override
    protected void setUp() {
        accountsTest = new ConcurrentHashMap<>();
//        Bank bank = new Bank();
        Account account1 = new Account(100, "40800", AccountStatus.NORMAL);
        Account account2 = new Account(100_000, "40801", AccountStatus.NORMAL);
        Account account3 = new Account(150_000, "40802", AccountStatus.TRANSFER);
        Account account4 = new Account(200, "40802", AccountStatus.BLOCKED);

        accountsTest.put("0", account1);
        accountsTest.put("1", account2);
        accountsTest.put("2", account3);
        accountsTest.put("3", account4);

        bank.addAccount("0", account1);
        bank.addAccount("1", account2);
        bank.addAccount("2", account3);
        bank.addAccount("3", account4);

    }

    public void testGetBalance(){
        long actual = bank.getBalance("0");
        long expected = 100;
        assertEquals(expected, actual);
    }

    public void  testGetSumAllAccounts(){
        long actual = bank.getSumAllAccounts(accountsTest);
        long expected = 250_300;
        assertEquals(expected, actual);
    }

    public  void testAddAccount(){
        Account accountTest = new Account(200, "400803", AccountStatus.NORMAL);
        bank.addAccount("4", accountTest);
        Account actual = bank.getAccounts().get("4");
        assertEquals(accountTest, actual);
    }

    public void testThrowsIllegalAccessException(){
        assertThrows(IllegalAccessException.class, ()-> bank.transfer("0", "null", 0));
    }

    public void testThrowsIllegalStateException(){
        assertThrows(IllegalStateException.class, () -> bank.transfer("0", "1", 200));
    }

    public void testClosedConnectionException(){
        assertThrows(ClosedConnectionException.class, () -> bank.transfer("0", "3", 100));
    }

    public void testSuccessTransaction() throws ClosedConnectionException, IllegalAccessException {
        bank.transfer("1", "2", 100);
        long actual = bank.getBalance("1");
        long expected = 99_900;
        long actual2 = bank.getBalance("2");
        long expected2 = 150_100;
        assertEquals(expected, actual);
        assertEquals(expected2, actual2);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
