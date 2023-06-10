package Main;

import com.sun.jdi.connect.spi.ClosedConnectionException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class Bank {
    private static final Map<String, Account> accounts = new ConcurrentHashMap<>();
    private final Random random = new Random();

    public synchronized boolean isFraud(String fromAccountNum, String toAccountNum, long amount)
            throws InterruptedException {
        Thread.sleep(1000);
        return random.nextBoolean();
    }

    /**
     * TODO: реализовать метод. Метод переводит деньги между счетами. Если сумма транзакции > 50000,
     * то после совершения транзакции, она отправляется на проверку Службе Безопасности – вызывается
     * метод isFraud. Если возвращается true, то делается блокировка счетов (как – на ваше
     * усмотрение)
     */
    public void transfer(String fromAccountNum, String toAccountNum, long amount) throws ClosedConnectionException, IllegalAccessException {
        Account from = accounts.get(fromAccountNum);
        Account to = accounts.get(toAccountNum);
//        System.out.println("------------------------------------------------------");
//        System.out.println("Пришло значение - " + fromAccountNum + " Key : " + accounts.get(fromAccountNum).getAccNumber() + " balance : " + getBalance(fromAccountNum) + " " + accounts.get(fromAccountNum).getStatus());
//        System.out.println("Пришо значение - " + toAccountNum + " Key : " + accounts.get(toAccountNum).getAccNumber() + " balance : " + getBalance(toAccountNum) + " " + accounts.get(toAccountNum).getStatus());
        if (from == null || to == null) {
            throw new IllegalAccessException("Некорректный счет");
        }
        assert from != null;
        if (from.getMoney() < amount) {
            throw new IllegalStateException("Недостаточно средств для перевода");
        }
        assert to != null;
        if (to.getStatus().equals(AccountStatus.BLOCKED) || from.getStatus().equals(AccountStatus.BLOCKED)) {
            throw new ClosedConnectionException("Счет заблокирован");
        }
        try {
            if (amount > 50000 && isFraud(fromAccountNum, toAccountNum, amount)) {
                accounts.get(fromAccountNum).setStatus(AccountStatus.BLOCKED);
                accounts.get(toAccountNum).setStatus(AccountStatus.BLOCKED);
            } else {
                from.setMoney(from.getMoney() - amount);
                to.setMoney(to.getMoney() + amount);
                accounts.get(fromAccountNum).setStatus(AccountStatus.NORMAL);
                accounts.get(toAccountNum).setStatus(AccountStatus.NORMAL);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        System.out.println("Новое значение - " + "Номер транзакции " + (cont++) + " "+ fromAccountNum + " Key : " + accounts.get(fromAccountNum).getAccNumber() + " balance : " + getBalance(fromAccountNum) + " " + accounts.get(fromAccountNum).getStatus());
//        System.out.println("Новое значение - " + toAccountNum + " Key : " + accounts.get(toAccountNum).getAccNumber() + " balance : " + getBalance(toAccountNum) + " " + accounts.get(toAccountNum).getStatus());
    }


    /**
     * TODO: реализовать метод. Возвращает остаток на счёте.
     */
    public long getBalance(String accountNum) {
        AtomicLong accountBalance = new AtomicLong();
        accounts.forEach((key, account) -> {
            if (accountNum.equals(key)) {
                accountBalance.addAndGet(account.getMoney());
            }
        });
        return accountBalance.get();
    }

    public long getSumAllAccounts(Map<String,Account> accountsMap) {
        AtomicLong sum = new AtomicLong(0L);
        for (String key : accountsMap.keySet()) {
            sum.addAndGet(accountsMap.get(key).getMoney());
        }
        return sum.get();
    }

    public void addAccount(String key, Account account){
        accounts.put(key, account);
    }

    public Map<String, Account> getAccounts() {
        return accounts;
    }

}