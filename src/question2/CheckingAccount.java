package question2;

//*** Race Conditions occurring in this class ***
public class CheckingAccount {
    private int balance;

    public CheckingAccount(int initialBalance) {
        balance = initialBalance;
    }

    public boolean withdraw(int amount) {

        //*** Race Condition: "Check then Act" ***
        if (amount <= balance) {
            try {
                Thread.sleep((int) (Math.random() * 200));
            } catch (InterruptedException ie) {
            }

            //*** Race Condition: "Read Modify Write" ***
            balance -= amount;
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        final question3.CheckingAccount ca = new question3.CheckingAccount(100);
        Runnable r = new Runnable() {
            @Override
            public void run() {
                String name = Thread.currentThread().getName();
                for (int i = 0; i < 10; i++)
                    System.out.println(name + " withdraws $10: " +
                            ca.withdraw(10));
            }
        };
        Thread thdHusband = new Thread(r);
        thdHusband.setName("Husband");
        Thread thdWife = new Thread(r);
        thdWife.setName("Wife");
        thdHusband.start();
        thdWife.start();
    }

}
