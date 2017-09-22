package ut.com.atlassian.tutorial.myyPlugin;

import org.junit.Test;
import com.atlassian.tutorial.myyPlugin.api.MyPluginComponent;
import com.atlassian.tutorial.myyPlugin.impl.MyPluginComponentImpl;

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