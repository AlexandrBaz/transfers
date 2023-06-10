package Main;

public class Main {
    private static final int numberAcsPerThread = 10;
    private static final int numberOfThreads = 10;
    private static final int numberOfTransactions = 10;

    public static void main(String[] args) {
        Bank bank = new Bank();
        CreateAccounts createAccounts = new CreateAccounts(bank, numberAcsPerThread);
        startTreadsAndWaitEnds(createAccounts);
        long balance = bank.getSumAllAccounts(bank.getAccounts());
        System.out.println("Баланс на начало транзакций : " + balance);
        Transfers transactions = new Transfers(bank, numberOfTransactions);
        startTreadsAndWaitEnds(transactions);
        System.out.println("Разница в балансе после финиша: " + (balance - bank.getSumAllAccounts(bank.getAccounts())));
        System.out.println("Баланс после : " + bank.getSumAllAccounts(bank.getAccounts()));
    }
    private static void startTreadsAndWaitEnds(Object object){
        Thread[] threadsTrans = new Thread[numberOfThreads];
        for (int i = 0; i < threadsTrans.length; i++) {
            threadsTrans[i] = new Thread((Runnable) object);
            threadsTrans[i].start();
        }
        for (Thread threadsTran : threadsTrans) {
            try {
                threadsTran.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public static int getDelta() {
        return (int) Math.ceil((numberOfThreads * numberOfTransactions) * 0.05);
    }
}