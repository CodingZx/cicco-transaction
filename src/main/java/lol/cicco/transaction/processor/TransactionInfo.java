package lol.cicco.transaction.processor;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

class TransactionInfo {
    private PlatformTransactionManager manager;
    private TransactionStatus status;

    TransactionInfo(PlatformTransactionManager manager, TransactionStatus status){
        this.manager = manager;
        this.status = status;
    }

    void commit(){
        manager.commit(status);
    }

    void rollback(){
        manager.rollback(status);
    }
}
