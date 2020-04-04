package com.pplenty.studytoby;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by yusik on 2020/04/05.
 */
public class TransactionAdvice implements MethodInterceptor {

    PlatformTransactionManager transactionManager;

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        // 트랜잭션 시작
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {

            // 비지니스 로직
            Object ret = invocation.proceed();

            // 커밋
            transactionManager.commit(status);
            return ret;

        } catch (RuntimeException e) {
            // 롤백
            transactionManager.rollback(status);
            throw e;
        }
    }
}
