package org.freebencher;


import junit.framework.Assert;
import org.junit.Test;

/**
 * @author chenjianjx@gmail.com
 */
public class FbJobResultTest {

    @Test
    public void getMeanTimePerTest_TestEmptyResult() {
        FbJobResult result = new FbJobResult();
        Assert.assertEquals(0d, result.getMeanTimePerTest());
    }

    @Test
    public void getPercentInCertainTime_TestEmptyResult() {
        FbJobResult result = new FbJobResult();
        Assert.assertTrue(result.getPercentInCertainTime().isEmpty());
    }

    @Test
    public void getTestsPerSecond_TestEmptyResult() {
        FbJobResult result = new FbJobResult();
        Assert.assertEquals(0d, result.getTestsPerSecond());
    }

    @Test
    public void reportTest_EmptyResult() {
        FbJobResult result = new FbJobResult();
        System.out.println(result.report());
    }
}
