package marten.aoe.test;

import marten.aoe.aspectj.NoNullEntries;
import marten.aoe.aspectj.NotNull;
import marten.aoe.aspectj.Range;

import org.junit.Assert;
import org.junit.Test;

public final class AnnotatedRestrictionsTest {
    public class CrashDummy {
        public CrashDummy(@NotNull String object, @NoNullEntries String[] array, @Range(from = 0, to = 9) int number) {}
        public void testMethod(@NotNull String object, @NoNullEntries String[] array, @Range(from = 0, to = 9) int number) {}
    }
    @Test
    public void nullArgumentInitialization () {
        try {
            new AnnotatedRestrictionsTest.CrashDummy(null, new String[] {""}, 0);
        }
        catch (IllegalArgumentException e) {
            return;
        }
        Assert.fail("No exception caught");
    }
    @Test
    public void nullArrayInitialization () {
        try {
            new AnnotatedRestrictionsTest.CrashDummy("", null, 0);
        }
        catch (IllegalArgumentException e) {
            return;
        }
        Assert.fail("No exception caught");
    }
    @Test
    public void nullValueInArrayInitialization () {
        try {
            new AnnotatedRestrictionsTest.CrashDummy("", new String[] {null}, 0);
        }
        catch (IllegalArgumentException e) {
            return;
        }
        Assert.fail("No exception caught");
    }
    @Test
    public void intOutOfRangeInitialization () {
        try {
            new AnnotatedRestrictionsTest.CrashDummy("", new String[] {""}, -1);
        }
        catch (IllegalArgumentException e) {
            return;
        }
        Assert.fail("No exception caught");
    }
    @Test
    public void nullArgumentMethod () {
        CrashDummy dummy = new AnnotatedRestrictionsTest.CrashDummy("", new String[] {""}, 0);
        try {
            dummy.testMethod(null, new String[] {""}, 0);
        }
        catch (IllegalArgumentException e) {
            return;
        }
        Assert.fail("No exception caught");
    }
    @Test
    public void nullArrayMethod () {
        CrashDummy dummy = new AnnotatedRestrictionsTest.CrashDummy("", new String[] {""}, 0);
        try {
            dummy.testMethod("", null, 0);
        }
        catch (IllegalArgumentException e) {
            return;
        }
        Assert.fail("No exception caught");
    }
    @Test
    public void nullValueInArrayMethod () {
        CrashDummy dummy = new AnnotatedRestrictionsTest.CrashDummy("", new String[] {""}, 0);
        try {
            dummy.testMethod("", new String[] {null}, 0);
        }
        catch (IllegalArgumentException e) {
            return;
        }
        Assert.fail("No exception caught");
    }
    @Test
    public void intOutOfRangeMethod () {
        CrashDummy dummy = new AnnotatedRestrictionsTest.CrashDummy("", new String[] {""}, 0);
        try {
            dummy.testMethod("", new String[] {""}, -1);
        }
        catch (IllegalArgumentException e) {
            return;
        }
        Assert.fail("No exception caught");
    }
    @Test
    public void everythingIsAlright () {
        CrashDummy dummy = new AnnotatedRestrictionsTest.CrashDummy("", new String[] {""}, 0);
        dummy.testMethod("", new String[] {""}, 0);
    }
}
