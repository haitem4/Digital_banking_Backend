package ma.emsi.digitalbankbackend;

import ma.emsi.digitalbankbackend.dtos.BankAccountDTO;
import ma.emsi.digitalbankbackend.dtos.CurrentBankAccountDTO;
import ma.emsi.digitalbankbackend.dtos.CustomerDTO;
import ma.emsi.digitalbankbackend.dtos.SavingBankAccountDTO;
import ma.emsi.digitalbankbackend.entities.*;
import ma.emsi.digitalbankbackend.enums.AccountStatus;
import ma.emsi.digitalbankbackend.enums.OperationType;
import ma.emsi.digitalbankbackend.exceptions.BankAccountNotFoundException;
import ma.emsi.digitalbankbackend.exceptions.CustomerNotFoundException;
import ma.emsi.digitalbankbackend.exceptions.InsufficientBalanceException;
import ma.emsi.digitalbankbackend.repositories.AccountOperationRepository;
import ma.emsi.digitalbankbackend.repositories.BankAccountRepository;
import ma.emsi.digitalbankbackend.repositories.CustomerRepository;
import ma.emsi.digitalbankbackend.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class DigitalBankBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(DigitalBankBackendApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(BankAccountService bankAccountService){
        return args -> {
            Stream.of("Haitem","Zaid","Alae")
                    .forEach(name->{
                        CustomerDTO customer = new CustomerDTO();
                        customer.setName(name);
                        customer.setEmail(name+"@gmail.com");
                        bankAccountService.saveCustomer(customer);
                    });
            bankAccountService.listCustomers().forEach(customer -> {
                try {
                    bankAccountService.saveCurrentBankAccount(Math.random()*80000,8000,customer.getId());
                    bankAccountService.saveSavingBankAccount(Math.random()*120000,3.4,customer.getId());
                } catch (CustomerNotFoundException e) {
                    e.printStackTrace();
                }
            });

            for (BankAccountDTO bankAccount : bankAccountService.bankAccountList()){
                for (int i = 0; i < 5; i++) {
                    String accountId;
                    if (bankAccount instanceof SavingBankAccountDTO){
                        accountId = ((SavingBankAccountDTO) bankAccount).getId();
                    } else {
                        accountId = ((CurrentBankAccountDTO) bankAccount).getId();
                    }
                    bankAccountService.credit(accountId, 10000 + Math.random()*120000,"Credit");
                    bankAccountService.debit(accountId, 1000 + Math.random()*9000,"Debit");
                }
            }
        };
    }


    //@Bean
    CommandLineRunner start(CustomerRepository customerRepository,
                            BankAccountRepository bankAccountRepository,
                            AccountOperationRepository accountOperationRepository){
        return args -> {
            Stream.of("Haitem","Zaid","Alae")
                    .forEach(name->{
                        Customer customer = new Customer();
                        customer.setName(name);
                        customer.setEmail(name+"@gmail.com");
                        customerRepository.save(customer);
                    });

            customerRepository.findAll()
                    .forEach(customer -> {
                        CurrentAccount currentAccount = new CurrentAccount();
                        currentAccount.setId(UUID.randomUUID().toString());
                        currentAccount.setCreatedAt(new Date());
                        currentAccount.setBalance(Math.random()*85000);
                        currentAccount.setStatus(Math.random()>0.5 ? AccountStatus.CREATED : AccountStatus.ACTIVATED);
                        currentAccount.setCustomer(customer);
                        currentAccount.setOverDraft(7000);
                        bankAccountRepository.save(currentAccount);

                        SavingAccount savingAccount = new SavingAccount();
                        savingAccount.setId(UUID.randomUUID().toString());
                        savingAccount.setCreatedAt(new Date());
                        savingAccount.setBalance(Math.random()*85000);
                        savingAccount.setStatus(Math.random()>0.5 ? AccountStatus.CREATED : AccountStatus.ACTIVATED);
                        savingAccount.setCustomer(customer);
                        savingAccount.setInterestRate(3.2);
                        bankAccountRepository.save(savingAccount);
                    });

            bankAccountRepository.findAll()
                    .forEach(bankAccount -> {
                        for (int i=0; i<3; i++){
                            AccountOperation accountOperation = new AccountOperation();
                            accountOperation.setOperationDate(new Date());
                            accountOperation.setBankAccount(bankAccount);
                            accountOperation.setAmount(Math.random()*40000);
                            accountOperation.setType(Math.random()>0.5 ? OperationType.DEBIT : OperationType.CREDIT);
                            accountOperationRepository.save(accountOperation);
                        }
                    });
        };
    }
}
