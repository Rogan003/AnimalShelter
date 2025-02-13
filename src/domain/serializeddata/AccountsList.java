package domain.serializeddata;

import java.util.ArrayList;
import java.util.stream.Collectors;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import domain.model.Account;

@XStreamAlias("accountsList")
public class AccountsList {

    public static AccountsList instance = null;

    @XStreamAlias("accounts")
    private ArrayList<Account> accounts;

    private AccountsList() {
        this.accounts = new ArrayList<Account>();
    }

    public static AccountsList getInstance() {
        if (instance == null) {
            instance = new AccountsList();
        }

        return instance;
    }

    public int generateId() {
        int numAccounts = accounts.size();
        return ++numAccounts;
    }

    public static void setInstance(AccountsList accountsList) {
        instance = accountsList;
    }

    public ArrayList<Account> getAccounts() {
        return this.accounts;
    }

    public void setAccounts(ArrayList<Account> accounts) {
        this.accounts = accounts;
    }

    public void addAccount(Account account) {
        this.accounts.add(account);
    }
    public Account createAccount(String username, String password){
        Account acc = new Account(generateId(),username,password);
        addAccount(acc);
        return acc;
    }
    public Account getByUsername(String username) {
        ArrayList<Account> accountList = (ArrayList<Account>) accounts
                .stream()
                .filter(account -> account.getUsername().equals(username))
                .collect(Collectors.toList());
        if (accountList.size() == 0) {
            return null;
        }

        return accountList.get(0);
    }
    public Account getById(Integer id) {
        for (Account account : accounts) {
            if (account.getId().equals(id)) {
                return account;
            }
        }
        return null;
    }

    public void changePassword(Integer id, String newPassword) {
        Account account = getById(id);
        account.setPassword(newPassword);
    }
}