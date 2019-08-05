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
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.Assert;

import java.util.*;

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

        Set<String> txNames = new HashSet<>(List.of(tx.transactionManager()));

        List<TransactionInfo> stack = new ArrayList<>(txNames.size());

        for (String txManagerName : tx.transactionManager()) {
            var transactionManager = context.getBean(txManagerName, PlatformTransactionManager.class);

            Assert.notNull(transactionManager, "The {" + txManagerName + "} is not a bean in spring.");

            var transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition(tx.propagation().value()));

            stack.add(new TransactionInfo(transactionManager, transactionStatus));
        }
        try {
            Object ret = pjp.proceed();

            for (int i = stack.size() - 1; i >= 0; --i) {
                stack.get(i).commit();
            }
            return ret;
        } catch (Exception e) {
            if (checkException(e, tx.rollbackFor())) {  // 校验异常信息
                for (int i = stack.size() - 1; i >= 0; --i) {
                    stack.get(i).rollback();
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
            } catch (Exception ignore) {
            }
        }
        return false;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) {
        this.context = applicationContext;
    }
}
