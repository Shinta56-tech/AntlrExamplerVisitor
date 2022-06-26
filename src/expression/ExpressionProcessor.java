package expression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpressionProcessor {
	List<Expression> list;
	public Map<String, Integer> values; /* symbol table for string values of variable */
	
	public ExpressionProcessor (List<Expression> list) {
		this.list = list;
		values = new HashMap<>();
	}
	
	public List<String> getEvaluationResults() {
		List<String> evaluations = new ArrayList<>();
		
		for(Expression e: list) {
			if(e instanceof VariableDeclation) {
				VariableDeclation decl = (VariableDeclation) e;
				values.put(decl.id, decl.value);
			}
			else { // e instanseof Number, Variable, Addition, Subtraction
				String input = e.toString();
				int result = getEvalResults(e);
				evaluations.add(input + " is " + result);
			}
		}
		
		return evaluations;
	}
	
	private int getEvalResults(Expression e) {
		int result = 0;
		
		if(e instanceof Number) {
			Number num = (Number) e;
			result = num.num;
		}
		else if(e instanceof Variable) {
			Variable var = (Variable) e;
			result = values.get(var.id);
		}
		else if(e instanceof Addition) {
			Addition add = (Addition) e;
			int left = getEvalResults(add.left);
			int right = getEvalResults(add.right);
			result = left + right;
		}
		else {
			Multiplication mult = (Multiplication) e;
			int left = getEvalResults(mult.left);
			int right = getEvalResults(mult.right);
			result = left * right;
		}
		
		return result;
	}
	
}
