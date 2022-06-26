package expression;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.Token;

import antlr.ExprBaseVisitor;
import antlr.ExprParser.AdditionContext;
import antlr.ExprParser.DeclarationContext;
import antlr.ExprParser.MultiplicationContext;
import antlr.ExprParser.NumberContext;
import antlr.ExprParser.VariableContext;

public class AntlrToExpression extends ExprBaseVisitor<Expression>{
	
	/*
	 *  Given that all visit_* methods are called in a top-down fashion,
	 *  we can be shre that the order in which we add declared variables in the 'vars' is
	 *  indentical to how they are declared in the input program.
	 */
	private List<String> vars; // stores all the variables declared in the program so far
	private List<String> semanitcErrors; // 1. duplicate declaration 2. reference to undeclared variable
	// Note that semantic errors are different from syntax errors.
	
	public AntlrToExpression(List<String> semanticErrors) {
		this.vars = new ArrayList<>();
		this.semanitcErrors = semanticErrors;
	}

	@Override
	public Expression visitDeclaration(DeclarationContext ctx) {
		// ID() is a method generated to corresopnd to the token ID in the source grammar.
		Token idToken = ctx.ID().getSymbol(); // equivalent to: ctx.getChilde(0).getSymbol()
		int line = idToken.getLine();
		int column = idToken.getCharPositionInLine() + 1;
		String id = ctx.getChild(0).getText();
		if (vars.contains(id)) {
			semanitcErrors.add("Error: variable " + id + " already declared (" + line + ", " + column + ")");
		}
		else {
			vars.add(id);
		}
		String type = ctx.getChild(2).getText();
		int value = Integer.parseInt(ctx.NUM().getText());
		return new VariableDeclation(id, type, value);
	}

	@Override
	public Expression visitMultiplication(MultiplicationContext ctx) {
		Expression left = visit(ctx.getChild(0));
		Expression right = visit(ctx.getChild(2));
		return new Multiplication(left, right);
	}

	@Override
	public Expression visitAddition(AdditionContext ctx) {
		Expression left = visit(ctx.getChild(0));
		Expression right = visit(ctx.getChild(2));
		return new Addition(left, right);
	}

	@Override
	public Expression visitVariable(VariableContext ctx) {
		Token idToken = ctx.ID().getSymbol();
		int line = idToken.getLine();
		int column = idToken.getCharPositionInLine() + 1;
		
		String id = ctx.getChild(0).getText();
		if (!vars.contains(id)) {
			semanitcErrors.add("Error: variable " + id + " not declared (" + line + ", " + column + ")");
		}
		return new Variable(id);
	}

	@Override
	public Expression visitNumber(NumberContext ctx) {
		String numText = ctx.getChild(0).getText();
		int num = Integer.parseInt(numText);
		return new Number(num);
	}
	
}
