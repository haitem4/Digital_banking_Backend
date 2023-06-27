package ma.emsi.digitalbankbackend.services;

import ma.emsi.digitalbankbackend.dtos.*;
import ma.emsi.digitalbankbackend.entities.BankAccount;
import ma.emsi.digitalbankbackend.entities.CurrentAccount;
import ma.emsi.digitalbankbackend.entities.Customer;
import ma.emsi.digitalbankbackend.entities.SavingAccount;
import ma.emsi.digitalbankbackend.exceptions.BankAccountNotFoundException;
import ma.emsi.digitalbankbackend.exceptions.CustomerNotFoundException;
import ma.emsi.digitalbankbackend.exceptions.InsufficientBalanceException;

import java.util.List;

public interface BankAccountService {
    CustomerDTO saveCustomer(CustomerDTO customerDTO);

    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    void deleteCustomer(Long id);

    CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;
    SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;
    List<CustomerDTO> listCustomers();
    BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;
    void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, InsufficientBalanceException;
    void credit(String accountId, double amount, String description) throws BankAccountNotFoundException;
    void transfert(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, InsufficientBalanceException;

    List<BankAccountDTO> bankAccountList();

    CustomerDTO getCustomer(Long id) throws CustomerNotFoundException;

    List<AccountOperationDTO> accountHistory(String accountId);

    AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException;

    List<CustomerDTO> searchCustomers(String keyword);
}
