package pcd.lab03.liveness;

import java.util.*;

public class TestAccounts_nodeadlock_rand {

	private static final int NUM_THREADS = 20;
	private static final int NUM_ACCOUNTS = 5;
	private static final int NUM_ITERATIONS = 10000000;
	private static final Random gen = new Random();
	private static final AccountWithLock[] accounts = new AccountWithLock[NUM_ACCOUNTS];

	public static boolean transferMoney(AccountWithLock fromAcct, AccountWithLock toAcct, int amount, long timeout) throws InsufficientBalanceException, InterruptedException {

		long fixedDelay = 1;
		long randMod = 10;
		long stopTime = System.currentTimeMillis() + timeout;

		while (true) {
			if (fromAcct.lock.tryLock()) {
				try {
					if (toAcct.lock.tryLock()) {
						try {
							if (fromAcct.getBalance() < amount)
								throw new InsufficientBalanceException();
							else {
								fromAcct.debit(amount);
								toAcct.credit(amount);
								return true;
							}
						} finally {
							toAcct.lock.unlock();
						}
					}
				} finally {
					fromAcct.lock.unlock();
				}
			}
			
			if (System.currentTimeMillis() < stopTime){
				return false;
			}
			
			Thread.sleep(fixedDelay + gen.nextLong() % randMod);
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
					boolean done = transferMoney(accounts[fromAcc],accounts[toAcc],amount,10);
					if (done){
						log("done.");
					} else {
						log("timeed out.");
					}
				} catch (InsufficientBalanceException ex){
					log("Not enough money.");
				} catch (InterruptedException ex){
					log("failed because of an interruption.");
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
			accounts[i] = new AccountWithLock(1000);
		}

		for (int i = 0; i < NUM_THREADS; i++){
			new TransferThread().start();
		}
	}

}
