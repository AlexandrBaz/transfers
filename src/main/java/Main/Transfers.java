package Main;

import com.sun.jdi.connect.spi.ClosedConnectionException;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class Transfers implements Runnable {

    private static Bank bank;
    private static final int limit = 50000;
    private static int numberOfTransactions;
    private static long amount;
    static AtomicInteger numberOfLimitTransaction = new AtomicInteger();
    static boolean limitTransaction;

    public Transfers(Bank bank, int numberOfTransactions) {
        Transfers.bank = bank;
        Transfers.numberOfTransactions = numberOfTransactions;
    }

    @Override
    public synchronized void run() {
        for (int i = 0; i < numberOfTransactions; i++) {
            try {
                getFromAndToAccountNum();
            } catch (ClosedConnectionException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public void getFromAndToAccountNum() throws ClosedConnectionException, IllegalAccessException {
        String fromAccountNum;
        String toAccountNum;
        do {
            fromAccountNum = String.valueOf(ThreadLocalRandom.current().nextInt(0, bank.getAccounts().size()));
            toAccountNum = String.valueOf(ThreadLocalRandom.current().nextInt(0, bank.getAccounts().size()));
        }
        while (!fromAccountNum.equals(toAccountNum) && isBlocked(fromAccountNum) && isBlocked(toAccountNum));
        bank.getAccounts().get(fromAccountNum).setStatus(AccountStatus.TRANSFER);
        bank.getAccounts().get(toAccountNum).setStatus(AccountStatus.TRANSFER);
        amount = generateAmountToTransfer(fromAccountNum);
        bank.transfer(fromAccountNum, toAccountNum, amount);
    }

    public static boolean isBlocked(String accountNum) {
        return !bank.getAccounts().get(accountNum).getStatus().equals(AccountStatus.NORMAL);
    }

    public static long generateAmountToTransfer(String accountNum) {
        long accountMoney = bank.getAccounts().get(accountNum).getMoney();
        amount = ThreadLocalRandom.current().nextLong(0, accountMoney + 1);
        if (amount >= limit && checkLimitsOfTransactions()) {
            amount = ThreadLocalRandom.current().nextLong(0, limit);
        }
        return amount;
    }

    public static boolean checkLimitsOfTransactions() {
        numberOfLimitTransaction.incrementAndGet();
        int delta = Main.getDelta();
        if (numberOfLimitTransaction.intValue() >= delta) {
            numberOfLimitTransaction.set(delta);
            limitTransaction = true;
        }
        return limitTransaction;
    }
}
