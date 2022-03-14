package pcd.lab03.liveness;

import java.util.concurrent.locks.*;

class AccountWithLock {

	private int balance;
	public Lock lock = new ReentrantLock();
	
	public AccountWithLock(int amount){
		balance = amount;
	}

	public int getBalance(){
		return balance;
	}

	public void debit(int amount){
		balance-=amount;
	}

	public void credit(int amount){
		balance+=amount;
	}
}
