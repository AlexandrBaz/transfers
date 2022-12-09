package Main;

public class Account {

    public Account(long money, String accNumber, Enum status) {
        this.money = money;
        this.accNumber = accNumber;
        this.status = status;
    }

    private long money;
    private String accNumber;
    private Enum status;

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public String getAccNumber() {
        return accNumber;
    }

    public void setAccNumber(String accNumber) {
        this.accNumber = accNumber;
    }

    public Enum getStatus() {
        return status;
    }

    public void setStatus(Enum status) {
        this.status = status;
    }

}
