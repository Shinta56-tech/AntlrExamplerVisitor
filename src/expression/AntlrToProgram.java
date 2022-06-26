package expression;

import java.util.ArrayList;
import java.util.List;

import antlr.ExprBaseVisitor;
import antlr.ExprParser.ProgramContext;

public class AntlrToProgram extends ExprBaseVisitor<Program>{
	
	public List<String> semanticErrors; // to be accessed by the main application program

	@Override
	public Program visitProgram(ProgramContext ctx) {
		Program prog = new Program();
		
		semanticErrors = new ArrayList<>();
		// a helper visitor for transforming each subtree into an Expression object
		AntlrToExpression exprVisitor = new AntlrToExpression(semanticErrors);
		for(int i = 0; i < ctx.getChildCount(); i ++) {
			if(i == ctx.getChildCount() - 1) {
				/* last child of the start symbol prog is EOF */
				// Do not visit this child and attempt to convert it to an Expression object
			}
			else {
				prog.addExpression(exprVisitor.visit(ctx.getChild(i)));
			}
		}
		return prog;
	}
	
}
