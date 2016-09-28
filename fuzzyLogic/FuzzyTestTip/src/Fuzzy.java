
import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
//import net.sourceforge.jFuzzyLogic.rule.FuzzyRuleSet;
//import net.sourceforge.jFuzzyLogic.*;
import net.sourceforge.jFuzzyLogic.rule.Variable;

/**
 * Test parsing an FCL file
 * @author pcingola@users.sourceforge.net
 */
public class Fuzzy {
    public static void main(String[] args) throws Exception {
        // Load from 'FCL' file
        String fileName = "/home/mario/Downloads/tipper.fcl";
        FIS fis = FIS.load(fileName,true);

        // Error while loading?
        if( fis == null ) { 
            System.err.println("Can't load file: '" + fileName + "'");
            return;
        }

//        FIS functionBlock = null;
		// Show 
        JFuzzyChart.get().chart(fis.getFunctionBlock("tipper") );

        // Set inputs
        fis.setVariable("service", 3);
        fis.setVariable("food", 7);

        // Evaluate
        fis.evaluate();

        // Show output variable's chart
        Variable tip = fis.getVariable("tip");
//        Variable tip = functionBlock.getVariable("tip");
        JFuzzyChart.get().chart(tip, tip.getDefuzzifier(), true);

        System.out.println(fis.getVariable("tip").defuzzify());
        // Print ruleSet
        System.out.println(fis);
    }
}