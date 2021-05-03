package calc.evaluate.parser;
;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RpnParserTest {
    RpnParser rpnParser;
    SymbolParser symbolParser = mock(SymbolParser.class);


    @Before
    public void setUp() throws Exception {
        rpnParser = spy(new RpnParser("ignored input"));
        when(rpnParser.createSymbolParser()).thenReturn(symbolParser);
        when(symbolParser.parse()).thenReturn(true);
    }

    @Test
    public void parse() {
        when(symbolParser.parse()).thenReturn(false);
        when(symbolParser.getError()).thenReturn("Test");
        Assert.assertFalse(rpnParser.parse());
        Assert.assertEquals("Parsing Error: Test", rpnParser.getError());
    }
}
