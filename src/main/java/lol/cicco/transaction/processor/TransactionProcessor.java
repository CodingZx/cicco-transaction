package lol.cicco.transaction.processor;

import lol.cicco.transaction.annotation.MoreTransactional;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.Assert;

import java.util.Stack;

@Aspect
@Component
public class TransactionProcessor implements ApplicationContextAware {

    private ApplicationContext context;

    @Pointcut(value = "@annotation(lol.cicco.transaction.annotation.MoreTransactional)")
    public void txPointCut() {
    }

    @Around("txPointCut()&&@annotation(tx)")
    public Object around(ProceedingJoinPoint pjp, MoreTransactional tx) throws Throwable {
        if (tx.transactionManager().length == 0) {
            return pjp.proceed();
        }

        Stack<PlatformTransactionManager> managers = new Stack<>();
        Stack<TransactionStatus> status = new Stack<>();

        for (String txManagerName : tx.transactionManager()) {
            var transactionManager = context.getBean(txManagerName, PlatformTransactionManager.class);

            Assert.notNull(transactionManager, "The {" + txManagerName + "} is not a bean in spring.");

            managers.add(transactionManager);

            var transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition(tx.propagation().value()));
            status.add(transactionStatus);
        }
        try {
            Object ret = pjp.proceed();

            while(!managers.isEmpty()){
                var manager = managers.pop();
                manager.commit(status.pop());
            }
            return ret;
        } catch (Exception e) {
            if (checkException(e, tx.rollbackFor())) {  // 校验异常信息
                while(!managers.isEmpty()){
                    var manager = managers.pop();
                    manager.rollback(status.pop());
                }
            }
            throw e;
        }
    }

    private boolean checkException(Exception e, Class<? extends Throwable>[] defineRollbackFor) {
        for (var exceptionCls : defineRollbackFor) {
            try {
                exceptionCls.cast(e);
                return true;
            } catch (Exception ignore) {}
        }
        return false;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) {
        this.context = applicationContext;
    }
}
