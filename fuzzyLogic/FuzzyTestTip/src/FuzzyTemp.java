import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.rule.Variable;


public class FuzzyTemp {
	
	public static void main(String[] args) throws Exception {
        // Load from 'FCL' file
        String fileName = "/home/mario/Downloads/temp.fcl";
        FIS fis = FIS.load(fileName,true);

        // Error while loading?
        if( fis == null ) { 
            System.err.println("Can't load file: '" + fileName + "'");
            return;
        }

//        FIS functionBlock = null;
		// Show 
        JFuzzyChart.get().chart(fis.getFunctionBlock("temperature") );

        // Set inputs
        fis.setVariable("temp_in", 16);
        //fis.setVariable("food", 7);

        // Evaluate
        fis.evaluate();

        // Show output variable's chart
        Variable tip = fis.getVariable("temp_out");
//        Variable tip = functionBlock.getVariable("tip");
        JFuzzyChart.get().chart(tip, tip.getDefuzzifier(), true);

        System.out.println(fis.getVariable("temp_out").defuzzify());
        // Print ruleSet
        System.out.println(fis);
    }

}