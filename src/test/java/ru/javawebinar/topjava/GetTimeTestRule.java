package ru.javawebinar.topjava;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;

import java.util.Date;

import static org.slf4j.LoggerFactory.getLogger;

public class GetTimeTestRule implements TestRule {
    private static final Logger log = getLogger(GetTimeTestRule.class);
    private static Date startDate;
    private static StringBuilder executionTimes = new StringBuilder("\n\n    Test's execution time:\n\n");

    public Statement apply(Statement base, Description description) {
        if (description.isTest()) {
            return new Statement() {
                public void evaluate() throws Throwable {
                    before();
                    try {
                        executionTimes.append(description.getMethodName() + " :");
                        base.evaluate();
                        verify();
                    } finally {
                        after();
                    }
                }
            };
        }
        if (description.isSuite()) {
            return new Statement() {
                public void evaluate() throws Throwable {
                    beforeClass();
                    try {
                        base.evaluate();
                        verifyClass();
                    } finally {
                        afterClass();
                    }
                }
            };
        }
        return base;
    }

    public void before() throws Exception {
        //let the implementer decide whether this method is useful to implement
        startDate = new Date();
    }

    public void after() {
        //let the implementer decide whether this method is useful to implement
        long time = new Date().getTime() - startDate.getTime();
        executionTimes.append(time + " ms\n");
        log.debug("Execution time:" + time + " ms");
    }

    /**
     * Only runs for Tests that pass
     */
    public void verify() {
        //let the implementer decide whether this method is useful to implement
    }

    public void beforeClass() throws Exception {
    }

    public void afterClass() {
        log.debug(executionTimes.toString());
    }

    /**
     * Only runs for Suites that pass
     */
    public void verifyClass() {
        verify();
    }
}

//    public Statement apply(Statement statement, Description description) {
//        return new Statement() {
//            @Override
//            public void evaluate() throws Throwable {
//                Date start = new Date();
//                statement.evaluate();
//                long time = new Date().getTime() - start.getTime();
//                log.debug("Execution time:" + time + " ms");
//            }
//        };
//    }

