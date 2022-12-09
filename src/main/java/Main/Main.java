package Main;

public class Main {
    private static final int numberAcsPerThread = 1000;
    private static final int numberOfThreads = 10;
    private static final int numberOfTransactions = 10;

    public static void main(String[] args) {
        Bank bank = new Bank();
        CreateAccounts createAccounts = new CreateAccounts(bank, numberAcsPerThread);
        Thread[] threads = new Thread[numberOfThreads];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(createAccounts);
            threads[i].start();
        }
//      Ожидаем окончания потоков
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

//        Запуск транзакций
        long balance = bank.getSumAllAccounts(bank.getAccounts());
        System.out.println("Баланс на начало транзакций : " + balance);
        Transfers transactions = new Transfers(bank, numberOfTransactions);
        Thread[] threadsTrans = new Thread[numberOfThreads];
        for (int i = 0; i < threadsTrans.length; i++) {
            threadsTrans[i] = new Thread(transactions);
            threadsTrans[i].start();
        }

//      Ожидаем окончания потоков
        for (Thread threadsTran : threadsTrans) {
            try {
                threadsTran.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Разница в балансе после финиша: " + (balance - bank.getSumAllAccounts(bank.getAccounts())));
        System.out.println("Баланс после : " + bank.getSumAllAccounts(bank.getAccounts()));
    }
    public static int getDelta() {
        return (int) Math.ceil((numberOfThreads * numberOfTransactions) * 0.05);
    }
}