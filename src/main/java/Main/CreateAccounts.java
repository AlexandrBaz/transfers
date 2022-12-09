package Main;

public class CreateAccounts implements Runnable {

    private final int numberAcsPerThread;
    private final Bank bank;

    public CreateAccounts(Bank bank, int numberAcsPerThread) {
        this.numberAcsPerThread = numberAcsPerThread;
        this.bank = bank;
    }

    @Override
    public synchronized void run() {
        for (int i = 0; i < numberAcsPerThread; i++) {
            Account account = new Account((long) (100_000 + Math.random() * 100_000),"408" + i, AccountStatus.NORMAL);
            bank.addAccount(String.valueOf(i), account);
        }
    }
}
