package org.jdom.xpath;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;

import junit.framework.TestCase;

public class XPathConcurrencyTest extends TestCase {
	private static final int NUM_INSTANCES = 100;
	
	/**
     * Testa 10 chiamate concorrenti con 10 login differenti e la stessa tessera:
	 * Deve consegnare una sola copia e il Set tokens deve contenere 10 elementi
	 * 
     * @throws InterruptedException
     */
    @Test
    public void testConcurrentXPathInstanceCreation() throws InterruptedException {
    	
        final CountDownLatch startLatch = new CountDownLatch(1);
        final CountDownLatch finishedLatch = new CountDownLatch(NUM_INSTANCES);
        final AtomicBoolean failed = new AtomicBoolean(false);
        final List<XPath> exprs = new CopyOnWriteArrayList<XPath>();
		
        ExecutorService executor = Executors.newFixedThreadPool(NUM_INSTANCES);

        for (int i = 0; i < NUM_INSTANCES; i++) {
            executor.execute(new Runnable() {
                public void run() {
                    try {
                        startLatch.await();
                        XPath dataBollaExpression = XPath.newInstance("//bol:dataBolla");
                        exprs.add(dataBollaExpression);
                        failed.set(false);
                    } catch (Throwable t) {
                    	t.printStackTrace();
                        failed.set(true);
                    } finally {
                        finishedLatch.countDown();
                    }
                }
            });
        }

        startLatch.countDown();
        finishedLatch.await();
        assertFalse(failed.get());
        System.out.println("size=" + exprs.size());
        assertFalse(containsDuplicates(exprs));
    }
    
    /**
     * @param input
     * @return
     */
    public static boolean containsDuplicates(List<XPath> input) {
        for (int i = 0; i < input.size(); i++) {
            for (int j = 0; j < input.size(); j++) {
                if (input.get(i) == input.get(j) && i != j) {
                    return true;
                }
            }
        }
        return false;
    }

}
