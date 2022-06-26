package app;

import java.io.IOException;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import antlr.ExprLexer;
import antlr.ExprParser;
import expression.AntlrToProgram;
import expression.ExpressionProcessor;
import expression.MyErrorListener;
import expression.Program;

public class ExpressionApp {
	
	public static void main(String[] args) {
		if(args.length != 1) {
			System.err.print("Usage: file name");
		}
		else {
			String fileName = args[0];
			ExprParser parser = getParser(fileName);
			
			// tell ANTLR to build a pars tree
			// parse from the start symbol 'prog'
			ParseTree antlerAST = parser.prog();
			
			if(MyErrorListener.hasError) {
				/* let the syntax error be reported */
			}
			else {
				// Create a visitor for converting the parse tree into Program/Expression object
				AntlrToProgram progVisitor = new AntlrToProgram();
				Program prog = progVisitor.visit(antlerAST);
				
				if(progVisitor.semanticErrors.isEmpty()) {
					ExpressionProcessor ep = new ExpressionProcessor(prog.expressions);
					for(String evaluation: ep.getEvaluationResults()) {
						System.out.println(evaluation);
					}
				}
				else {
					for(String err: progVisitor.semanticErrors) {
						System.out.println(err);
					}
				}
			}
		}
	}
	
	/*
	 * Here tye types of parser and lexer are specific to the
	 * grammar name Expr.g4.
	 */
	private static ExprParser getParser(String fileName) {
		ExprParser parser = null;
		
		try {
			CharStream input = CharStreams.fromFileName(fileName);
			ExprLexer lexer = new ExprLexer(input);
			CommonTokenStream tokens = new CommonTokenStream(lexer);
			parser = new ExprParser(tokens);
			
			// syntac error handling
			parser.removeErrorListeners();
			parser.addErrorListener(new MyErrorListener());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return parser;
	}
}
