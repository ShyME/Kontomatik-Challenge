package me.imshy.client;

import me.imshy.account.AccountBalance;

import java.io.Closeable;
import java.util.List;

public interface BankConnection extends Closeable {

    List<AccountBalance> getAccountBalances();
}
