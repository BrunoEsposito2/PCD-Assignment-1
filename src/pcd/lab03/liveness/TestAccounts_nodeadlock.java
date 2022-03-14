package pcd.lab03.liveness;

import java.util.*;

public class TestAccounts_nodeadlock {

	private static final int NUM_THREADS = 20;
	private static final int NUM_ACCOUNTS = 5;
	private static final int NUM_ITERATIONS = 10000000;
	private static final Random gen = new Random();

	static class TransferThread extends Thread {
		AccountManager man;

		TransferThread(AccountManager man){
			this.man = man;
		}
		
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
					man.transferMoney(fromAcc,toAcc,amount);
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
		AccountManager man = new AccountManager(NUM_ACCOUNTS,1000);
		for (int i = 0; i < NUM_THREADS; i++){
			new TransferThread(man).start();
		}
	}
}
