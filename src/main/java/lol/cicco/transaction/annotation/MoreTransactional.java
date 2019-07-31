package lol.cicco.transaction.annotation;

import org.springframework.transaction.annotation.Propagation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MoreTransactional {

    /**
     * Some <em>qualifier</em> values for the specified transaction.
     */
    String[] transactionManager();

    /**
     * The transaction propagation type.
     */
    Propagation propagation() default Propagation.REQUIRED;

    /**
     * @see org.springframework.transaction.annotation.Transactional#rollbackFor()
     */
    Class<? extends Throwable>[] rollbackFor() default {};

}
