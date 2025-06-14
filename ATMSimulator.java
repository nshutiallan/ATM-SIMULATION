import java.util.InputMismatchException; 
import java.util.Scanner; 

class BankAccount {
    protected double balance; 

    public BankAccount(double initialBalance) {
        this.balance = initialBalance; 
    }

    public void deposit(double amount) { 
        if (amount > 0) {
            balance += amount;
            System.out.printf("Successfully deposited: $%.2f%n", amount);
        } else {
            System.out.println("Deposit amount must be positive.");
        }
    }

    public void checkBalance() { /
        System.out.printf("Your current balance is: $%.2f%n", balance);
    }
}


class ATM extends BankAccount {
    private String pin;  

    public ATM(String userPin, double initialBalance) {
        super(initialBalance);  
        this.pin = userPin;
    }

    public boolean validatePin(String inputPin) {
        return this.pin.equals(inputPin);
    }

    public void withdraw(double amount) throws InsufficientFundsException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }
        if (amount > balance) {
            throw new InsufficientFundsException("Insufficient balance.");
        }
        balance -= amount;
        System.out.printf("Successfully withdrew: $%.2f%n", amount);
    }

    public void changePin(String oldPin, String newPin) {
        if (validatePin(oldPin)) {
            this.pin = newPin;
            System.out.println("PIN successfully changed.");
        } else {
            System.out.println("Incorrect current PIN. PIN change failed.");
        }
    }
}


class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }
}

public class ATMSimulator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ATM myATM = new ATM("1234", 1000.00);

        System.out.print("Enter your PIN: ");
        String inputPin = scanner.nextLine();

        if (!myATM.validatePin(inputPin)) {
            System.out.println("Invalid PIN. Access Denied.");
            return;
        }

        int choice = -1;
        while (true) {
            try {
                System.out.println("\n1. Check Balance\n2. Deposit\n3. Withdraw\n4. Change PIN\n0. Exit");
                System.out.print("Choose an option: ");
                choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        myATM.checkBalance();
                        break;
                    case 2:
                        System.out.print("Enter deposit amount: ");
                        double depositAmount = scanner.nextDouble();
                        scanner.nextLine(); 
                        System.out.print("Enter a note for the deposit: ");
                        String note = scanner.nextLine();
                        myATM.deposit(depositAmount);
                        System.out.println("Note: " + note);
                        break;
                    case 3:
                        System.out.print("Enter withdrawal amount: ");
                        double withdrawalAmount = scanner.nextDouble();
                        if (withdrawalAmount > myATM.balance) {
                            System.out.println("You have insufficient balance.");
                        } else {
                            myATM.withdraw(withdrawalAmount);
                        }
                        break;
                    case 4:
                        scanner.nextLine(); 
                        System.out.print("Enter current PIN: ");
                        String oldPin = scanner.nextLine();
                        System.out.print("Enter new PIN: ");
                        String newPin = scanner.nextLine();
                        myATM.changePin(oldPin, newPin);
                        break;
                    case 0:
                        System.out.println("Thank you for using our ATM. Goodbye!");
                        System.out.print("Would you like to restart the session? (yes/no): ");
                        scanner.nextLine();
                        String restart = scanner.nextLine();
                        if (restart.equalsIgnoreCase("yes")) {
                            continue;
                        } else {
                            System.out.println("Session ended.");
                            scanner.close();
                            return;
                        }
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input type. Please enter numbers only.");
                scanner.nextLine(); 
            } catch (InsufficientFundsException | IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
