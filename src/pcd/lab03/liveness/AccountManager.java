package pcd.lab03.liveness;

public class AccountManager {
	
	private final Account[] accounts;

	public AccountManager(int nAccounts, int amount){
		accounts = new Account[nAccounts];
		for (int i = 0; i < accounts.length; i++){
			accounts[i] = new Account(amount);
		}
	}
	
	public void transferMoney(int from,	int to, int amount) throws InsufficientBalanceException {
		
		int first = from;
		int last = to;
		
		if (first > last){
			last = first;
			first = to;
		}
		
		synchronized (accounts[first]) {
			synchronized (accounts[last]) {
				if (accounts[from].getBalance() < amount)
					throw new InsufficientBalanceException();
				accounts[from].debit(amount);
				accounts[to].credit(amount);
			}
		}
	}
}

