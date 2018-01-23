package ut.uk.ac.gla.stephen;

import org.junit.Test;
import uk.ac.gla.stephen.api.MyPluginComponent;
import uk.ac.gla.stephen.impl.MyPluginComponentImpl;

import static org.junit.Assert.assertEquals;

public class MyComponentUnitTest
{
    @Test
    public void testMyName()
    {
        MyPluginComponent component = new MyPluginComponentImpl(null);
        assertEquals("names do not match!", "myComponent",component.getName());
    }
}