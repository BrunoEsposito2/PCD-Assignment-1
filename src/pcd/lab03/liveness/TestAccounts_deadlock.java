package pcd.lab03.liveness;

import java.util.*;

public class TestAccounts_deadlock {

	private static final int NUM_THREADS = 20;
	private static final int NUM_ACCOUNTS = 3;
	private static final int NUM_ITERATIONS = 10000000;
	private static final Random gen = new Random();
	private static final Account[] accounts = new Account[NUM_ACCOUNTS];
	
	public static void transferMoney(Account from,	Account to, int amount) throws InsufficientBalanceException {
		synchronized (from) {
			synchronized (to) {
				if (from.getBalance() < amount)
					throw new InsufficientBalanceException();
				from.debit(amount);
				to.credit(amount);
				try {
					Thread.sleep(100);
				} catch (Exception ex) {}
			}
		}
	}

	static class TransferThread extends Thread {
		public void run() {
			for (int i = 0; i < NUM_ITERATIONS; i++){
				int fromAcc = gen.nextInt(NUM_ACCOUNTS);
				int toAcc = 0;
				do {
					toAcc = gen.nextInt(NUM_ACCOUNTS);
				} while (toAcc == fromAcc);
				int amount = gen.nextInt(10);				
				try {
					log("Transferring from "+fromAcc+" to "+toAcc+" amount "+amount+"...");
					transferMoney(accounts[fromAcc],accounts[toAcc],amount);
					log("done.");
				} catch (InsufficientBalanceException ex){
					log("Not enough money.");
				}
			}
		}
		private void log(String msg){
			synchronized(System.out){
				System.out.println("["+this+"] "+msg);
			}
		}
	}
	
	public static void main(String[] args) {
		
		for (int i = 0; i < accounts.length; i++){
			accounts[i] = new Account(10);
		}
		
		for (int i = 0; i < NUM_THREADS; i++){
			new TransferThread().start();
		}
	}
}
